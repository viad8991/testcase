package ru.test.service;

import org.springframework.lang.NonNull;
import ru.test.exeptions.PhoneNumberExceptions;

public interface PhoneNormalizer {

    @NonNull
    String normalize(@NonNull String text) throws PhoneNumberExceptions;

}
