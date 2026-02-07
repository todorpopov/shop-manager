package com.shop_manager.ui.utils;

import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.exceptions.ScreenCanceledException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InputUtility {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String readString(ScreenManager screenManager, String prompt, int minLength, int maxLength) {
        while (true) {
            System.out.print(prompt);
            String input = screenManager.nextLine();

            if (input.equals("0")) {
                throw new ScreenCanceledException("Operation cancelled by user.");
            }

            if (input.isBlank()) {
                System.out.println("Error: Input cannot be empty.");
                continue;
            }

            if (input.length() < minLength || input.length() > maxLength) {
                System.out.println("Error: Input length must be between " + minLength + " and " + maxLength + " characters.");
                continue;
            }

            return input;
        }
    }

    public static BigDecimal readBigDecimal(ScreenManager screenManager, String prompt, BigDecimal minValue) {
        while (true) {
            System.out.print(prompt);
            String input = screenManager.nextLine();

            if (input.equals("0")) {
                throw new ScreenCanceledException("Operation cancelled by user.");
            }

            if (input.isBlank()) {
                System.out.println("Error: Input cannot be empty.");
                continue;
            }

            try {
                BigDecimal value = new BigDecimal(input);

                if (minValue != null && value.compareTo(minValue) < 0) {
                    System.out.println("Error: Value must be at least " + minValue + ".");
                    continue;
                }

                return value;
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid decimal number.");
            }
        }
    }

    public static LocalDate readDate(ScreenManager screenManager, String prompt) {
        while (true) {
            System.out.print(prompt + " (yyyy-mm-dd): ");
            String input = screenManager.nextLine();

            if (input.equals("0")) {
                throw new ScreenCanceledException("Operation cancelled by user.");
            }

            if (input.isBlank()) {
                System.out.println("Error: Input cannot be empty.");
                continue;
            }

            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMATTER);

                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Error: Expiration date must be in the future.");
                    continue;
                }

                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Error: Please enter a valid date in the format yyyy-mm-dd.");
            }
        }
    }

    public static LocalDate readOptionalDate(ScreenManager screenManager, String prompt) {
        while (true) {
            System.out.print(prompt + " (yyyy-mm-dd or press Enter to skip): ");
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
                    continue;
                }

                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Error: Please enter a valid date in the format yyyy-mm-dd or press Enter to skip.");
            }
        }
    }

    public static <E extends Enum<E>> E readEnum(
        ScreenManager screenManager,
        String prompt,
        E[] enumValues
    ) {
        while (true) {
            System.out.println(prompt);
            for (int i = 0; i < enumValues.length; i++) {
                System.out.println("  " + (i + 1) + ". " + enumValues[i].name());
            }
            System.out.print("Please select an option (1-" + enumValues.length + "): ");

            String input = screenManager.nextLine();

            if (input.equals("0")) {
                throw new ScreenCanceledException("Operation cancelled by user.");
            }

            try {
                int choice = Integer.parseInt(input);

                if (choice < 1 || choice > enumValues.length) {
                    System.out.println("Error: Please enter a valid option between 1 and " + enumValues.length + ".");
                    continue;
                }

                return enumValues[choice - 1];
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        }
    }

    public static boolean readConfirmation(ScreenManager screenManager, String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = screenManager.nextLine().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Error: Please enter 'y' or 'n'.");
            }
        }
    }
}
