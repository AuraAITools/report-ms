package com.aura.core.entities.links;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.educators.Educator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AccountEducatorLink {
    private Account account;

    private Educator educator;

}
