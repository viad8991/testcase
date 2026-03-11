package ru.test.service;

import org.springframework.lang.NonNull;
import ru.test.dto.Okved;

import java.util.List;

public interface OkvedLoader {

    @NonNull
    List<Okved> getOkveds();

}
