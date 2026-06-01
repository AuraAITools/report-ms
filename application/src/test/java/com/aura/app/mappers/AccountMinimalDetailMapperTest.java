package com.aura.app.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.aura.app.codegen.types.AccountMinimalDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AccountMinimalDetailMapper")
class AccountMinimalDetailMapperTest {

    @Test
    @DisplayName("Should wrap a non-null userId into a stub with only userId populated")
    void wrapsNonNullUserId() {
        AccountMinimalDetail result = AccountMinimalDetailMapper.INSTANCE.toMinimal("user-123");

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo("user-123");
        assertThat(result.getAccountId()).isNull();
        assertThat(result.getName()).isNull();
        assertThat(result.getUrl()).isNull();
    }

    @Test
    @DisplayName("Should return null when the userId is null (used for nullable updatedBy)")
    void returnsNullForNullUserId() {
        AccountMinimalDetail result = AccountMinimalDetailMapper.INSTANCE.toMinimal(null);

        assertThat(result).isNull();
    }
}
