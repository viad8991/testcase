package ru.test.service.impl;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.test.dto.Okved;
import ru.test.dto.OkvedResponse;
import ru.test.mapper.OkvedMapper;
import ru.test.service.OkvedLoader;
import ru.test.service.OkvedMatcherService;
import ru.test.service.PhoneNormalizer;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;
import static ru.test.util.Constants.UNKNOWN;

@Service
public class OkvedPhoneMatcherServiceImpl implements OkvedMatcherService {

    private final OkvedLoader okvedLoader;
    private final PhoneNormalizer phoneNormalizer;

    public OkvedPhoneMatcherServiceImpl(PhoneNormalizer phoneNormalizer, OkvedLoader okvedLoader) {
        this.phoneNormalizer = phoneNormalizer;
        this.okvedLoader = okvedLoader;
    }

    @Override
    public OkvedResponse findOkved(@NonNull String text) {
        String normalizePhoneNumber = phoneNormalizer.normalize(text);
        List<Okved> okveds = okvedLoader.getOkveds();

        String potentialOkvedCode = fetchPotentialOkvedCode(normalizePhoneNumber);

        Okved findOkved = findOkved(okveds, potentialOkvedCode);
        int matchLength = getMatchLength(findOkved.code());

        return OkvedMapper.toResponse(normalizePhoneNumber, findOkved, matchLength);
    }

    @NonNull
    private String fetchPotentialOkvedCode(@NonNull String phoneNumber) {
        return phoneNumber
                .substring(phoneNumber.length() - 6);
    }

    // TODO нужно:
    //  +79279901111 -> 01.11.1 (5)
    //  +79279990113 -> 01.13   (4)
    //  +79279999901 -> 01      (2)
    @NonNull
    private Okved findOkved(@NonNull List<Okved> okveds, @NonNull String endPhone) {
        for (Okved okved : okveds) {
            String code = removeDots(okved.code());

            if (endPhone.startsWith(code)) { // TODO ОКВЕД по типу "01.13.4" никогда не будет найден
                Okved findOkved = isEmpty(okved.items())
                        ? okved
                        : findOkved(okved.items(), endPhone);

                return findOkved == Okved.UNKNOWN ? okved : findOkved;
            }
        }

        return Okved.UNKNOWN;
    }

    @NonNull
    private String removeDots(@NonNull String text) {
        return text.replace(".", "");
    }

    private int getMatchLength(@NonNull String code) {
        return UNKNOWN.equalsIgnoreCase(code) ? 0 : removeDots(code).length();
    }
}
