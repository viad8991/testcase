package ru.test.mapper;

import org.springframework.lang.NonNull;
import ru.test.dto.Okved;
import ru.test.dto.OkvedResponse;

public class OkvedMapper {

    private OkvedMapper() {
        //TODO лучше использовать org.mapstruct.mapstruct.
    }

    @NonNull
    public static OkvedResponse toResponse(@NonNull String phone, @NonNull Okved okved, int matchLength) {
        return new OkvedResponse(
                phone,
                okved.code(),
                okved.name(),
                matchLength
        );
    }

}
