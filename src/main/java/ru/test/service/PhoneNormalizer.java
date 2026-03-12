package ru.test.service;

import org.springframework.lang.NonNull;
import ru.test.exeption.InvalidPhoneNumberException;

public interface PhoneNormalizer {

    @NonNull
    String normalize(@NonNull String text) throws InvalidPhoneNumberException;

}
