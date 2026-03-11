package ru.test.mapper;

import org.springframework.lang.NonNull;
import ru.test.dto.Okved;
import ru.test.dto.OkvedLookupResult;
import ru.test.dto.OkvedResponse;

public class OkvedMapper {

    private OkvedMapper() {
        //TODO лучше использовать org.mapstruct.mapstruct.
    }

    @NonNull
    public static OkvedLookupResult toOkvedLookupResult(@NonNull String phone, @NonNull Okved okved, int matchLength) {
        return new OkvedLookupResult(
                phone,
                okved.code(),
                okved.name(),
                matchLength
        );
    }

    @NonNull
    public static OkvedResponse toResponse(@NonNull OkvedLookupResult result) {
        return new OkvedResponse(
                result.phoneNormalized(),
                result.okvedCode(),
                result.okvedName(),
                result.matchLength()
        );
    }

}
