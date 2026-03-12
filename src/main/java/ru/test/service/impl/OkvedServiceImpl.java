package ru.test.service.impl;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.test.dto.Okved;
import ru.test.dto.OkvedLookupResult;
import ru.test.mapper.OkvedMapper;
import ru.test.service.OkvedLoader;
import ru.test.service.OkvedService;
import ru.test.service.PhoneNormalizer;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;
import static ru.test.util.Constants.UNKNOWN;

@Service
public class OkvedServiceImpl implements OkvedService {

    private final OkvedLoader okvedLoader;
    private final PhoneNormalizer phoneNormalizer;

    public OkvedServiceImpl(PhoneNormalizer phoneNormalizer, OkvedLoader okvedLoader) {
        this.phoneNormalizer = phoneNormalizer;
        this.okvedLoader = okvedLoader;
    }

    @Override
    public OkvedLookupResult findOkvedByPhone(@NonNull String phoneNumber) {
        String normalizePhoneNumber = phoneNormalizer.normalize(phoneNumber);
        List<Okved> okveds = okvedLoader.getOkveds();

        String potentialOkvedCode = fetchPotentialOkvedCode(normalizePhoneNumber);

        Okved findOkved = findOkved(okveds, potentialOkvedCode);
        int matchLength = getMatchLength(findOkved.code());

        return OkvedMapper.toOkvedLookupResult(normalizePhoneNumber, findOkved, matchLength);
    }

    @NonNull
    private String fetchPotentialOkvedCode(@NonNull String phoneNumber) {
        return phoneNumber
                .substring(phoneNumber.length() - 6);
    }

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
