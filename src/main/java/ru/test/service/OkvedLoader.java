package ru.test.service;

import org.springframework.lang.NonNull;
import ru.test.dto.Okved;

import java.util.Map;

public interface OkvedLoader {

    @NonNull
    Map<String, Okved> getOkveds();

}
