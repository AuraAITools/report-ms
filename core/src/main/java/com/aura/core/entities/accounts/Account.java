package com.aura.core.entities.accounts;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private String userId;

    private String email;

    private String firstName;

    private String lastName;

    private String contact;
    private RELATIONSHIP relationship;

    public enum RELATIONSHIP {
        PARENT,
        SELF
    }

}
