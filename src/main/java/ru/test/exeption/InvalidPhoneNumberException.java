package ru.test.exeption;

public class InvalidPhoneNumberException extends RuntimeException {

    public InvalidPhoneNumberException() {
        super("Некорректный номер телефона");
    }

}
