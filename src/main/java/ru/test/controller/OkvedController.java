package ru.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.test.dto.OkvedResponse;

@RequestMapping("/api/v1/okved")
public interface OkvedController {

    @PostMapping("/find")
    ResponseEntity<OkvedResponse> findOkved(@RequestParam @NonNull String phone);

}
