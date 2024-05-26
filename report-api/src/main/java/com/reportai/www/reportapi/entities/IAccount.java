package com.reportai.www.reportapi.entities;

import java.util.UUID;

public interface IAccount {
    String getEmail();

    UUID getUserId();

    UUID getId();

    String getName();

}
