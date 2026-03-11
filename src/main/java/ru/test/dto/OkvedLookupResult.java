package ru.test.dto;

public record OkvedLookupResult(String phoneNormalized, String okvedCode, String okvedName, Integer matchLength) {
}
