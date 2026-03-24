package ru.test.dto;

import org.springframework.lang.NonNull;

public record Okved(@NonNull String code, @NonNull String name) {

    public static final Okved UNKNOWN = new Okved("unknown", "Неизвестный код.");

}
