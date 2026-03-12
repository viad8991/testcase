package ru.test.exeption;

import org.springframework.lang.NonNull;

public class InvalidPhoneNumberException extends RuntimeException {

    public InvalidPhoneNumberException(@NonNull String number) {
        super(String.format("Некорректный номер телефона: '%s'", number));
    }

    public InvalidPhoneNumberException(@NonNull String number, @NonNull Throwable throwable) {
        super(String.format("Ошибка при работе с номером телефона: '%s'", number), throwable);
    }

}
