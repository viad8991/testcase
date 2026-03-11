package ru.test.service;

import org.junit.jupiter.api.Test;
import ru.test.exeptions.PhoneNumberExceptions;
import ru.test.service.impl.RussianPhoneNormalizer;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PhoneNormalizerTest {

    @Test
    void validateRusPhoneNormalizer() {
        final PhoneNormalizer rusPhoneNormalizer = new RussianPhoneNormalizer();

        Map.of(
                "+79200000000", "+79200000000",
                "79200000000", "+79200000000",
                "89200000000", "+79200000000",
                "+7 (920) 0000000", "+79200000000",
                "+7 (920) 000-00-00", "+79200000000",
                "+7 (920) 111 11 00", "+79201111100",
                "  +7    920 9 9 9 9 9 9 9  ", "+79209999999",
                "792000F00000", "+79200000000" // TODO в строке 12 симолов, во время нормализации буква стерлась и стала валидна с 11 цифрами
        ).forEach((phoneNumber, expected) -> {
            var actual = rusPhoneNormalizer.normalize(phoneNumber);

            assertEquals(expected, actual, "Номер телефона: " + phoneNumber);
        });

        List.of("+8200000000", "----", "       ", "+7920000000", "792000F0000", "foobar").forEach(phoneNumber ->
                assertThrows(
                        PhoneNumberExceptions.class,
                        () -> rusPhoneNormalizer.normalize(phoneNumber),
                        "Номер телефона: " + phoneNumber
                )
        );
    }

}