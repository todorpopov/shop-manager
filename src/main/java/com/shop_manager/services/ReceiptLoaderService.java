package com.shop_manager.services;

import com.shop_manager.models.Cashier;
import com.shop_manager.models.Product;
import com.shop_manager.models.Receipt;
import com.shop_manager.models.ReceiptItem;
import com.shop_manager.models.enums.ProductCategory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class ReceiptLoaderService {
    private static ReceiptLoaderService instance;

    private static final String FILE_PREFIX = "receipt_";
    private static final String FILE_SUFFIX = ".xml";
    private static final DateTimeFormatter FILE_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private final Path receiptsDir;

    private ReceiptLoaderService() {
        this.receiptsDir = Paths.get("receipts");
    }

    public static ReceiptLoaderService getInstance() {
        if (instance == null) {
            instance = new ReceiptLoaderService();
        }
        return instance;
    }

    public void saveReceipt(Receipt receipt) throws IOException {
        if (receipt == null) {
            throw new IllegalArgumentException("Receipt must not be null");
        }
        if (receipt.getId() == null) {
            throw new IllegalArgumentException("Receipt id must not be null");
        }
        if (receipt.getIssuedAt() == null) {
            throw new IllegalArgumentException("Receipt issuedAt must not be null");
        }

        Files.createDirectories(receiptsDir);
        Path filePath = receiptPath(receipt.getId(), receipt.getIssuedAt());

        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            Document document = buildReceiptDocument(receipt);
            writeDocument(document, outputStream);
        } catch (Exception ex) {
            throw new IOException("Failed to save receipt to file: " + filePath, ex);
        }
    }

    public List<Receipt> loadReceipts() throws IOException {
        if (!Files.exists(receiptsDir)) {
            return List.of();
        }

        List<Path> receiptFiles;
        try (Stream<Path> stream = Files.list(receiptsDir)) {
            receiptFiles = stream
                .filter(this::isReceiptFile)
                .sorted(Comparator.comparing(Path::getFileName))
                .toList();
        }

        List<Receipt> receipts = new ArrayList<>();
        for (Path receiptFile : receiptFiles) {
            try (InputStream inputStream = Files.newInputStream(receiptFile)) {
                receipts.add(parseReceiptDocument(inputStream));
            } catch (Exception ex) {
                throw new IOException("Failed to load receipt from file: " + receiptFile, ex);
            }
        }
        return receipts;
    }

    private boolean isReceiptFile(Path path) {
        String name = path.getFileName().toString();
        return name.startsWith(FILE_PREFIX) && name.endsWith(FILE_SUFFIX);
    }

    private Path receiptPath(Long receiptId, LocalDateTime issuedAt) {
        String formattedDateTime = issuedAt.format(FILE_DATE_TIME_FORMAT);
        return receiptsDir.resolve(FILE_PREFIX + receiptId + "_" + formattedDateTime + FILE_SUFFIX);
    }

    private Document buildReceiptDocument(Receipt receipt) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element receiptElement = document.createElement("receipt");
        receiptElement.setAttribute("id", receipt.getId().toString());
        document.appendChild(receiptElement);

        appendCashier(document, receiptElement, receipt.getCashier());
        appendTextElement(document, receiptElement, "issuedAt", receipt.getIssuedAt().toString());
        appendTextElement(document, receiptElement, "totalAmount", receipt.getTotalAmount().toPlainString());

        Element itemsElement = document.createElement("items");
        receiptElement.appendChild(itemsElement);

        for (ReceiptItem item : receipt.getItems()) {
            appendReceiptItem(document, itemsElement, item);
        }

        return document;
    }

    private void appendCashier(Document document, Element parent, Cashier cashier) {
        Element cashierElement = document.createElement("cashier");
        cashierElement.setAttribute("id", cashier.getId() == null ? "" : cashier.getId().toString());
        parent.appendChild(cashierElement);

        appendTextElement(document, cashierElement, "name", cashier.getName());
        appendTextElement(document, cashierElement, "monthlySalary", cashier.getMonthlySalary().toPlainString());
    }

    private void appendReceiptItem(Document document, Element parent, ReceiptItem item) {
        Element itemElement = document.createElement("item");
        parent.appendChild(itemElement);

        appendTextElement(document, itemElement, "quantity", String.valueOf(item.getQuantity()));
        appendTextElement(document, itemElement, "pricePerUnit", item.getPricePerUnit().toPlainString());

        Product product = item.getProduct();
        Element productElement = document.createElement("product");
        productElement.setAttribute("id", product.getId() == null ? "" : product.getId().toString());
        itemElement.appendChild(productElement);

        appendTextElement(document, productElement, "name", product.getName());
        appendTextElement(document, productElement, "deliveryPrice", product.getDeliveryPrice().toPlainString());
        appendTextElement(document, productElement, "expirationDate", product.getExpirationDate().toString());
        appendTextElement(document, productElement, "category", product.getCategory().name());
    }

    private void appendTextElement(Document document, Element parent, String name, String text) {
        Element element = document.createElement(name);
        element.appendChild(document.createTextNode(text));
        parent.appendChild(element);
    }

    private void writeDocument(Document document, OutputStream outputStream) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(new DOMSource(document), new StreamResult(outputStream));
    }

    private Receipt parseReceiptDocument(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);
        document.getDocumentElement().normalize();

        Element receiptElement = document.getDocumentElement();
        Long receiptId = parseOptionalLong(receiptElement.getAttribute("id"));

        Element cashierElement = findChildElement(receiptElement, "cashier");
        Cashier cashier = parseCashier(cashierElement);

        LocalDateTime issuedAt = LocalDateTime.parse(getChildText(receiptElement, "issuedAt"));
        BigDecimal totalAmount = new BigDecimal(getChildText(receiptElement, "totalAmount"));

        Element itemsElement = findChildElement(receiptElement, "items");
        List<ReceiptItem> items = parseItems(itemsElement);

        return new Receipt(receiptId, cashier, issuedAt, items, totalAmount);
    }

    private Cashier parseCashier(Element cashierElement) {
        Long cashierId = parseOptionalLong(cashierElement.getAttribute("id"));
        String name = getChildText(cashierElement, "name");
        BigDecimal monthlySalary = new BigDecimal(getChildText(cashierElement, "monthlySalary"));
        return new Cashier(cashierId, name, monthlySalary);
    }

    private List<ReceiptItem> parseItems(Element itemsElement) {
        List<ReceiptItem> items = new ArrayList<>();
        NodeList children = itemsElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && "item".equals(node.getNodeName())) {
                Element itemElement = (Element) node;
                items.add(parseItem(itemElement));
            }
        }
        return items;
    }

    private ReceiptItem parseItem(Element itemElement) {
        int quantity = Integer.parseInt(getChildText(itemElement, "quantity"));
        BigDecimal pricePerUnit = new BigDecimal(getChildText(itemElement, "pricePerUnit"));

        Element productElement = findChildElement(itemElement, "product");
        Product product = parseProduct(productElement);

        return new ReceiptItem(product, quantity, pricePerUnit);
    }

    private Product parseProduct(Element productElement) {
        Long productId = parseOptionalLong(productElement.getAttribute("id"));
        String name = getChildText(productElement, "name");
        BigDecimal deliveryPrice = new BigDecimal(getChildText(productElement, "deliveryPrice"));
        LocalDate expirationDate = LocalDate.parse(getChildText(productElement, "expirationDate"));
        ProductCategory category = ProductCategory.valueOf(getChildText(productElement, "category"));
        return new Product(productId, name, deliveryPrice, expirationDate, category);
    }

    private Long parseOptionalLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Long.parseLong(value);
    }

    private String getChildText(Element parent, String tagName) {
        Element child = findChildElement(parent, tagName);
        return child.getTextContent();
    }

    private Element findChildElement(Element parent, String tagName) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && tagName.equals(node.getNodeName())) {
                return (Element) node;
            }
        }
        throw new IllegalArgumentException("Missing element: " + tagName);
    }

    public void clearReceiptsDirectory() throws IOException {
        if (!Files.exists(this.receiptsDir)) {
            return;
        }

        try (var stream = Files.list(this.receiptsDir)) {
            stream.filter(Files::isRegularFile).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException ex) {
                    throw new RuntimeException("Failed to delete receipt file: " + path, ex);
                }
            });
        }
    }
}
