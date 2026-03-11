package ru.test.exeptions;

import org.springframework.lang.NonNull;

public class PhoneNumberExceptions extends RuntimeException {

    public PhoneNumberExceptions(@NonNull String number) {
        super(String.format("Некорректный номер телефона: '%s'", number));
    }

    public PhoneNumberExceptions(@NonNull String number, @NonNull Throwable throwable) {
        super(String.format("Ошибка при работе с номером телефона: '%s'", number), throwable);
    }

}
