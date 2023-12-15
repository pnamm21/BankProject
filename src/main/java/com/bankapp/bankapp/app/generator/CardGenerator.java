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

        if (cardType == CardType.VISA) {
            cardNumber.append("4");
        } else if (cardType == CardType.MASTERCARD) {
            cardNumber.append("5");
        }

        Random random = new Random();
        for (int i = 1; i < 16; i++) {
            int digit = random.nextInt(10);
            cardNumber.append(digit);
        }

        int checksum = calculateLunaChecksum(cardNumber.toString());
        cardNumber.append(checksum);

        return cardNumber.toString();
    }

    /**
     * Luhn algorithms
     *
     * @param cardNumber Credit card number (without checksum digit)
     * @return Luhn checksum digit
     */
    private static int calculateLunaChecksum(String cardNumber) {
        int sum = 0;
        boolean doubleDigit = false;

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

        int checksum = (sum % 10 == 0) ? 0 : (10 - (sum % 10));
        return checksum;
    }

    /**
     * Generates a random 3-digit CVV
     *
     * @return Random 3-digit CVV code
     */
    public static String generateCVVCode() {
        Random random = new Random();
        int cvv = 100 + random.nextInt(900);
        return String.valueOf(cvv);
    }

    public static LocalDateTime generateExpirationDate() {

        int month = ThreadLocalRandom.current().nextInt(1, 13);
        int year = LocalDateTime.now().getYear() + ThreadLocalRandom.current().nextInt(0, 11);
        int day = ThreadLocalRandom.current().nextInt(1, 29);
        int hour = ThreadLocalRandom.current().nextInt(0, 24);
        int minute = ThreadLocalRandom.current().nextInt(0, 60);
        int second = ThreadLocalRandom.current().nextInt(0, 60);

        LocalDateTime expirationDateTime = LocalDateTime.of(year, month, day, hour, minute, second);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String expirationDateStr = expirationDateTime.format(formatter);

        return LocalDateTime.parse(expirationDateStr, formatter);
    }

}