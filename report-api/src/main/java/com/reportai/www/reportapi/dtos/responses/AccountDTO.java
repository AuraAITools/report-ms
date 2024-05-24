package com.reportai.www.reportapi.dtos.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.reportai.www.reportapi.entities.IAccount;
import lombok.Builder;

import java.util.UUID;

@Builder
public class AccountDTO {
    public String email;

    public String name;

    public UUID id;

    @JsonProperty("user_id")
    public UUID userId;

    @JsonProperty("account_type")
    public ACCOUNT_TYPES accountType;

    public enum ACCOUNT_TYPES {
        INSTITUTION,
        PARENT,
        EDUCATOR,
        STUDENT
    }

    public static AccountDTO from(IAccount account, ACCOUNT_TYPES type) {
        return AccountDTO
                .builder()
                .id(account.getId())
                .email(account.getEmail())
                .userId(account.getUserId())
                .accountType(type)
                .name(account.getName())
                .build();
    }
}
