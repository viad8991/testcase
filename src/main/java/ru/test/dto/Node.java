package ru.test.dto;

import org.springframework.lang.NonNull;

import java.util.List;

public record Node(@NonNull String code, @NonNull String name, @NonNull List<Node> items) {
}
