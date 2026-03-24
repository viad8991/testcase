package ru.test.service.impl;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.test.dto.Okved;
import ru.test.dto.OkvedResponse;
import ru.test.mapper.OkvedMapper;
import ru.test.service.OkvedLoader;
import ru.test.service.OkvedMatcherService;
import ru.test.service.PhoneNormalizer;

import java.util.Map;

@Service
public class OkvedPhoneMatcherServiceImpl implements OkvedMatcherService {

    private final static int MAX_OKVED_CODE_LENGTH = 6;

    private final OkvedLoader okvedLoader;
    private final PhoneNormalizer phoneNormalizer;

    public OkvedPhoneMatcherServiceImpl(PhoneNormalizer phoneNormalizer, OkvedLoader okvedLoader) {
        this.phoneNormalizer = phoneNormalizer;
        this.okvedLoader = okvedLoader;
    }

    @Override
    public OkvedResponse findOkved(@NonNull String text) {
        String normalizePhoneNumber = phoneNormalizer.normalize(text);

        String potentialOkvedCode = getPotentialOkvedCode(normalizePhoneNumber);
        Map<String, Okved> okvedCodeMap = okvedLoader.getOkveds();

        Map.Entry<Integer, Okved> findOkved = findOkved(okvedCodeMap, potentialOkvedCode);

        return OkvedMapper.toResponse(normalizePhoneNumber, findOkved.getValue(), findOkved.getKey());
    }

    @NonNull
    private String getPotentialOkvedCode(@NonNull String phoneNumber) {
        return phoneNumber
                .substring(phoneNumber.length() - MAX_OKVED_CODE_LENGTH);
    }

    @NonNull
    private Map.Entry<Integer, Okved> findOkved(@NonNull Map<String, Okved> okveds, @NonNull String endPhone) {
        for (int i = MAX_OKVED_CODE_LENGTH; i > 0; i--) {
            Okved okved = okveds.get(endPhone.substring(MAX_OKVED_CODE_LENGTH - i));

            if (okved != null) {
                return Map.entry(i, okved);
            }
        }

        return Map.entry(0, Okved.UNKNOWN);
    }
}
