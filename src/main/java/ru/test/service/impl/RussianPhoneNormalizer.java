package ru.test.service.impl;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.test.exeption.InvalidPhoneNumberException;
import ru.test.service.PhoneNormalizer;

import java.util.regex.Pattern;

@Component
public class RussianPhoneNormalizer implements PhoneNormalizer {

    private final static Pattern NON_DIGIT_PATTERN = Pattern.compile("\\D");

    private final static int VALID_PHONE_LENGTH = 11;
    private final static int VALID_PHONE_LENGTH_WITHOUT_CODE = 10;
    private final static String VALID_PHONE_START = "+79";

    @NonNull
    @Override
    public String normalize(@NonNull String text) throws InvalidPhoneNumberException {
        if (!StringUtils.hasText(text))
            throw new InvalidPhoneNumberException(text);

        String cleaned = NON_DIGIT_PATTERN.matcher(text).replaceAll("");
        int phoneLength = cleaned.length();

        if (phoneLength == VALID_PHONE_LENGTH && (cleaned.startsWith("79") || cleaned.startsWith("89"))) {
            cleaned = VALID_PHONE_START + cleaned.substring(2);
        } else if (phoneLength == VALID_PHONE_LENGTH_WITHOUT_CODE && cleaned.startsWith("9")) {
            cleaned = VALID_PHONE_START + cleaned.substring(1);
        } else {
            throw new InvalidPhoneNumberException(text);
        }

        return cleaned;

        // private final static Pattern RUSSIAN_PHONE_FORMAT_PATTERN = Pattern.compile("^(\\+79)(d{10})");
        // if (RUSSIAN_PHONE_FORMAT_PATTERN.matcher(cleaned).matches()) {
        //     return cleaned;
        // }
        // throw new PhoneNumberExceptions(text);
    }
}
