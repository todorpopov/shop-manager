package com.shop_manager.ui.utils;

import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.exceptions.ScreenCanceledException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UpdateUtility {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String readUpdatedString(ScreenManager screenManager, String prompt, String currentValue) {
        System.out.print(prompt + " [" + currentValue + "]: ");
        String input = screenManager.nextLine();

        if (input.equals("0")) {
            throw new ScreenCanceledException("Operation cancelled by user.");
        }

        if (input.isBlank()) {
            return null;
        }

        if (input.length() > 255) {
            System.out.println("Error: Input length must be at most 255 characters.");
            return readUpdatedString(screenManager, prompt, currentValue);
        }

        return input;
    }

    public static BigDecimal readUpdatedBigDecimal(ScreenManager screenManager, String prompt, BigDecimal currentValue) {
        System.out.print(prompt + " [" + currentValue + "]: ");
        String input = screenManager.nextLine();

        if (input.equals("0")) {
            throw new ScreenCanceledException("Operation cancelled by user.");
        }

        if (input.isBlank()) {
            return null;
        }

        try {
            BigDecimal value = new BigDecimal(input);

            if (value.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Error: Value must be at least 0.");
                return readUpdatedBigDecimal(screenManager, prompt, currentValue);
            }

            return value;
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid decimal number.");
            return readUpdatedBigDecimal(screenManager, prompt, currentValue);
        }
    }

    public static Double readUpdatedDouble(ScreenManager screenManager, String prompt, double currentValue) {
        System.out.print(prompt + " [" + currentValue + "]: ");
        String input = screenManager.nextLine();

        if (input.equals("0")) {
            throw new ScreenCanceledException("Operation cancelled by user.");
        }

        if (input.isBlank()) {
            return null;
        }

        try {
            double value = Double.parseDouble(input);

            if (value < 0) {
                System.out.println("Error: Value must be at least 0.");
                return readUpdatedDouble(screenManager, prompt, currentValue);
            }

            return value;
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number.");
            return readUpdatedDouble(screenManager, prompt, currentValue);
        }
    }

    public static Integer readUpdatedInt(ScreenManager screenManager, String prompt, int currentValue) {
        System.out.print(prompt + " [" + currentValue + "]: ");
        String input = screenManager.nextLine();

        if (input.equals("0")) {
            throw new ScreenCanceledException("Operation cancelled by user.");
        }

        if (input.isBlank()) {
            return null;
        }

        try {
            int value = Integer.parseInt(input);

            if (value < 0) {
                System.out.println("Error: Value must be at least 0.");
                return readUpdatedInt(screenManager, prompt, currentValue);
            }

            return value;
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid integer.");
            return readUpdatedInt(screenManager, prompt, currentValue);
        }
    }

    public static <E extends Enum<E>> E readUpdatedEnum(
        ScreenManager screenManager,
        String prompt,
        E[] enumValues,
        E currentValue
    ) {
        System.out.println(prompt + " [" + currentValue.name() + "]:");
        System.out.println("  (Press Enter to keep current)");
        for (int i = 0; i < enumValues.length; i++) {
            System.out.println("  " + (i + 1) + ". " + enumValues[i].name());
        }
        System.out.print("Please select an option or press Enter to keep current: ");

        String input = screenManager.nextLine();

        if (input.equals("0")) {
            throw new ScreenCanceledException("Operation cancelled by user.");
        }

        if (input.isBlank()) {
            return null;
        }

        try {
            int choice = Integer.parseInt(input);

            if (choice < 1 || choice > enumValues.length) {
                System.out.println("Error: Please enter a valid option between 1 and " + enumValues.length + ".");
                return readUpdatedEnum(screenManager, prompt, enumValues, currentValue);
            }

            return enumValues[choice - 1];
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number or press Enter to keep current.");
            return readUpdatedEnum(screenManager, prompt, enumValues, currentValue);
        }
    }

    public static LocalDate readUpdatedFoodDate(ScreenManager screenManager, String prompt, LocalDate currentValue) {
        System.out.print(prompt + " (yyyy-mm-dd) [" + (currentValue != null ? currentValue : "N/A") + "]: ");
        String input = screenManager.nextLine();

        if (input.equals("0")) {
            throw new ScreenCanceledException("Operation cancelled by user.");
        }

        if (input.isBlank()) {
            return null;
        }

        try {
            LocalDate date = LocalDate.parse(input, DATE_FORMATTER);

            if (date.isBefore(LocalDate.now())) {
                System.out.println("Error: Expiration date must be in the future.");
                return readUpdatedFoodDate(screenManager, prompt, currentValue);
            }

            return date;
        } catch (DateTimeParseException e) {
            System.out.println("Error: Please enter a valid date in the format yyyy-mm-dd.");
            return readUpdatedFoodDate(screenManager, prompt, currentValue);
        }
    }

    public static LocalDate readUpdatedNonFoodDate(ScreenManager screenManager, String prompt, LocalDate currentValue) {
        System.out.print(prompt + " (yyyy-mm-dd or press Enter to skip) [" + (currentValue != null ? currentValue : "N/A") + "]: ");
        String input = screenManager.nextLine();

        if (input.equals("0")) {
            throw new ScreenCanceledException("Operation cancelled by user.");
        }

        if (input.isBlank()) {
            return null;
        }

        try {
            LocalDate date = LocalDate.parse(input, DATE_FORMATTER);

            if (date.isBefore(LocalDate.now())) {
                System.out.println("Error: Expiration date must be in the future.");
                return readUpdatedNonFoodDate(screenManager, prompt, currentValue);
            }

            return date;
        } catch (DateTimeParseException e) {
            System.out.println("Error: Please enter a valid date in the format yyyy-mm-dd or press Enter to skip.");
            return readUpdatedNonFoodDate(screenManager, prompt, currentValue);
        }
    }
}
