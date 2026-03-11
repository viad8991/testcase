package ru.test.dto;

import org.springframework.lang.NonNull;

import java.util.List;

public record Okved(@NonNull String code, @NonNull String name, @NonNull List<Okved> items) {

    public static final Okved UNKNOWN = new Okved("unknown", "Неизвестный код.", List.of());

}
