package com.bankapp.bankapp.app.generator;

import com.bankapp.bankapp.app.entity.enums.CardType;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class CardGenerator {

    /**
     * Generates a random 16-digit credit card number for the specified card type.
     *
     * @param cardType Card type (VISA or MASTERCARD)
     * @return Random 16-digit credit card number
     */
    public static String generateRandomCardNumber(CardType cardType) {
        StringBuilder cardNumber = new StringBuilder();

        // Set the initial digit based on card type
        if (cardType == CardType.VISA) {
            cardNumber.append("4"); // Visa card numbers start with 4
        } else if (cardType == CardType.MASTERCARD) {
            cardNumber.append("5"); // MasterCard numbers start with 5
        }

        // Generate 15 more random digits (total 16 digits including the initial digit)
        Random random = new Random();
        for (int i = 1; i < 16; i++) {
            int digit = random.nextInt(10); // Generate a random digit (0-9)
            cardNumber.append(digit);
        }

        // Calculate and append the checksum digit using Luhn algorithm
        int checksum = calculateLunaChecksum(cardNumber.toString());
        cardNumber.append(checksum);

        return cardNumber.toString();
    }

    /**
     * Calculates the Luhn checksum digit for a given credit card number.
     *
     * @param cardNumber Credit card number (without checksum digit)
     * @return Luhn checksum digit
     */
    private static int calculateLunaChecksum(String cardNumber) {
        int sum = 0;
        boolean doubleDigit = false;

        // Iterate through the card number digits from right to left
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }

        // Calculate the checksum digit that makes the sum a multiple of 10
        int checksum = (sum % 10 == 0) ? 0 : (10 - (sum % 10));
        return checksum;
    }

    /**
     * Generates a random 3-digit CVV code for a credit card.
     *
     * @return Random 3-digit CVV code
     */
    public static String generateCVVCode() {
        Random random = new Random();
        int cvv = 100 + random.nextInt(900); // Generate a random 3-digit number (between 100 and 999)
        return String.valueOf(cvv);
    }

    public static LocalDateTime generateExpirationDate() {
        // Generate a random expiration month (1 to 12)
        int month = ThreadLocalRandom.current().nextInt(1, 13);
        // Generate a random expiration year (current year to next 10 years)
        int year = LocalDateTime.now().getYear() + ThreadLocalRandom.current().nextInt(0, 11);
        // Generate a random expiration day (1 to 28) for simplicity (assuming all months have 28 days)
        int day = ThreadLocalRandom.current().nextInt(1, 29);
        // Generate a random expiration hour (0 to 23), minute (0 to 59), and second (0 to 59)
        int hour = ThreadLocalRandom.current().nextInt(0, 24);
        int minute = ThreadLocalRandom.current().nextInt(0, 60);
        int second = ThreadLocalRandom.current().nextInt(0, 60);

        // Create a LocalDateTime instance for the generated date and time
        LocalDateTime expirationDateTime = LocalDateTime.of(year, month, day, hour, minute, second);

        // Format the LocalDateTime as "yyyy-MM-dd'T'HH:mm:ss"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String expirationDateStr = expirationDateTime.format(formatter);

        // Parse the formatted string back to LocalDateTime to ensure it's valid
        return LocalDateTime.parse(expirationDateStr, formatter);
    }

}