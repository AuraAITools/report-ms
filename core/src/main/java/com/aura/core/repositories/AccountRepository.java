package com.aura.core.repositories;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.links.AccountStudentLink;
import java.util.List;

public interface AccountRepository {
    Account save(Account account);

    List<AccountStudentLink> linkAccountToStudents(String accountId, List<String> studentIds);

    AccountStudentLink linkAccountToStudent(String accountId, String studentId);

}
