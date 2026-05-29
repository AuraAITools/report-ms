package com.aura.core.repositories;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.accounts.AccountFeature;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.links.AccountEducatorLink;
import com.aura.core.entities.links.AccountStudentLink;
import com.aura.core.entities.students.Student;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public interface AccountRepository {
    Account save(Account account);

    Account saveAndFlush(Account account);

    /**
     * Targeted single-column update for the account's lifecycle state. Avoids the
     * domain-mapper round-trip used by {@link #save(Account)}, which would null
     * out related-entity back-references (e.g. parent.account_id) on merge.
     */
    void updateStateById(String id, Account.STATE state);

    /**
     * Flips the boolean column for the given feature flag to true. Targeted
     * update for the same reason as {@link #updateStateById}: {@code save()}
     * would re-detach related collections and null parent.account_id.
     */
    void enableFeature(String id, AccountFeature feature);

    List<AccountStudentLink> linkAccountToStudents(String accountId, List<String> studentIds);

    AccountStudentLink linkAccountToStudent(String accountId, String studentId);

    AccountEducatorLink linkAccountToEducator(String accountId, String educatorId);

    @Nullable Account findById(String id);

    @Nullable Account findByEmailAndTenantId(String email, String tenantId);

    List<Account> findAllByEmail(String email);

    @Nullable Account findByUserIdAndTenantId(String userId, String tenantId);

    List<Account> findAllByTenantId(String tenantId);

    Map<String, List<Student>> findStudentsByAccountIds(List<String> accountIds);

    Map<String, List<Educator>> findEducatorsByAccountIds(List<String> accountIds);

    Map<String, Account> findAllByEducatorIds(List<String> educatorIds);
}
