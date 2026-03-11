package ru.test.dto;

public record OkvedResponse(String phoneNormalized, String okvedCode, String okvedName, Integer matchLength) {
}
