package ru.test.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;
import ru.test.controller.OkvedController;
import ru.test.dto.OkvedResponse;
import ru.test.mapper.OkvedMapper;
import ru.test.service.OkvedService;

@RestController
public class OkvedControllerImpl implements OkvedController {

    private final OkvedService okvedService;

    public OkvedControllerImpl(OkvedService okvedService) {
        this.okvedService = okvedService;
    }

    @Override
    public ResponseEntity<OkvedResponse> findOkved(@NonNull String phone) {
        return ResponseEntity.ok(OkvedMapper.toResponse(okvedService.findOkvedByPhone(phone)));

    }

}
