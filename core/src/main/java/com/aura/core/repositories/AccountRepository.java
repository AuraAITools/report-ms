package com.aura.core.repositories;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.links.AccountEducatorLink;
import com.aura.core.entities.links.AccountStudentLink;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface AccountRepository {
    Account save(Account account);

    List<AccountStudentLink> linkAccountToStudents(String accountId, List<String> studentIds);

    AccountStudentLink linkAccountToStudent(String accountId, String studentId);

    AccountEducatorLink linkAccountToEducator(String accountId, String educatorId);

    @Nullable Account findById(String id);
}
