package ru.test.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;
import ru.test.controller.OkvedController;
import ru.test.dto.OkvedResponse;
import ru.test.service.OkvedMatcherService;

@RestController
public class OkvedControllerImpl implements OkvedController {

    private final OkvedMatcherService okvedMatcherService;

    public OkvedControllerImpl(OkvedMatcherService okvedMatcherService) {
        this.okvedMatcherService = okvedMatcherService;
    }

    @Override
    public ResponseEntity<OkvedResponse> findOkved(@NonNull String phone) {
        return ResponseEntity.ok(okvedMatcherService.findOkved(phone));

    }

}
