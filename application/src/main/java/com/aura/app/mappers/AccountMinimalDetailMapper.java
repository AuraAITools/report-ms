package com.aura.app.mappers;

import com.aura.app.codegen.types.AccountMinimalDetail;
import org.jspecify.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMinimalDetailMapper {
    AccountMinimalDetailMapper INSTANCE = Mappers.getMapper(AccountMinimalDetailMapper.class);

    @Named("toMinimal")
    default @Nullable AccountMinimalDetail toMinimal(@Nullable String userId) {
        if (userId == null) {
            return null;
        }
        return AccountMinimalDetail.newBuilder().userId(userId).build();
    }
}
