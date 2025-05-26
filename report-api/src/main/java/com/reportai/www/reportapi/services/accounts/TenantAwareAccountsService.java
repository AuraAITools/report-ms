package com.reportai.www.reportapi.services.accounts;

import com.reportai.www.reportapi.clients.keycloak.exceptions.KeycloakUserAccountAlreadyExistsException;
import com.reportai.www.reportapi.clients.keycloak.exceptions.KeycloakUserAccountCreationException;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.attachments.AccountEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.AccountEducatorAttachmentRepository;
import com.reportai.www.reportapi.entities.attachments.AccountStudentAttachment;
import com.reportai.www.reportapi.entities.attachments.AccountStudentAttachmentRepository;
import com.reportai.www.reportapi.entities.educators.Educator;
import com.reportai.www.reportapi.entities.helpers.Attachment;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.mappers.AccountMappers;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import com.reportai.www.reportapi.services.educators.EducatorsService;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * IdpUserAccount (keycloak) and TenantAwareAccounts(Accounts in DB) have a one-to-many relationship
 * TenantAwareAccountsService manages TenantAwareAccounts
 * tenant aware accounts are accounts that belongs to a tenant (an institution)
 * keycloak accounts (different from tenant aware accounts) are not tenant aware accounts as
 * users have 1 keycloak account that might be used to access multiple TenantAwareAccounts
 */
@Slf4j
@Service
public class TenantAwareAccountsService implements ISimpleRead<Account> {
    private final AccountStudentAttachmentRepository accountStudentAttachmentRepository;
    private final AccountEducatorAttachmentRepository accountEducatorAttachmentRepository;
    private final InstitutionsService institutionsService;
    private final AccountRepository accountRepository;
    private final ClientResource clientResource;
    private final StudentsService studentsService;
    private final EducatorsService educatorsService;
    private final OutletsService outletsService;
    private final UsersResource usersResource;
    private final StudentRepository studentRepository;
    private final EducatorRepository educatorRepository;

    @Autowired
    public TenantAwareAccountsService(AccountStudentAttachmentRepository accountStudentAttachmentRepository, AccountEducatorAttachmentRepository accountEducatorAttachmentRepository, InstitutionsService institutionsService, AccountRepository accountRepository, ClientResource clientResource, StudentsService studentsService, EducatorsService educatorsService, OutletsService outletsService, UsersResource usersResource, StudentRepository studentRepository, EducatorRepository educatorRepository) {
        this.accountStudentAttachmentRepository = accountStudentAttachmentRepository;
        this.accountEducatorAttachmentRepository = accountEducatorAttachmentRepository;
        this.institutionsService = institutionsService;
        this.accountRepository = accountRepository;
        this.clientResource = clientResource;
        this.studentsService = studentsService;
        this.educatorsService = educatorsService;
        this.outletsService = outletsService;
        this.usersResource = usersResource;
        this.studentRepository = studentRepository;
        this.educatorRepository = educatorRepository;
    }

    @Override
    public JpaRepository<Account, UUID> getRepository() {
        return accountRepository;
    }


    /**
     * creates db account for a user
     * creates keycloak user account if it doesn't exist else add ext_attrs.tenant_ids to existing keycloak account
     *
     * @param account
     * @param institutionId
     * @return
     */
    @Transactional
    public Account createTenantAwareAccount(Account account, UUID institutionId) {

        // get institution
        Institution institution = institutionsService.findById(institutionId);

        List<UserRepresentation> idpUserAccounts = usersResource.searchByEmail(account.getEmail(), true);

        if (idpUserAccounts.size() > 1) {
            throw new ResourceAlreadyExistsException(String.format("account with email %s is already present in institution %s", account.getEmail(), institution.getName()));
        }

        // brand new user, create new keycloak user account and new tenant aware account
        if (idpUserAccounts.isEmpty()) {
            // create idpUserAccount in keycloak with tenant-ids as user attribute
            // TODO: change this logic to use modelmapper
            UserRepresentation userRepresentation = AccountMappers.convert(account, Map.of("tenant-ids", List.of(institutionId.toString())));

            accountRepository.saveAndFlush(account);
            Response response = usersResource.create(userRepresentation);

            if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                throw new KeycloakUserAccountAlreadyExistsException("keycloak user account already exists");
            }

            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                throw new KeycloakUserAccountCreationException("user account could not be created");
            }

            String userId = CreatedResponseUtil.getCreatedId(response);
            log.info("successfully created user with userId: {}", userId);

            account.setUserId(userId);
            return accountRepository.save(account);
        }

        // idpUserAccount exists, check if tenantAwareAccount is already registered with idpUserAccount
        // if it exists, prevent creation of duplicate tenantAwareAccount
        if (accountRepository.findByEmail(account.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException(String.format("account with email %s is already present in institution %s", account.getEmail(), institution.getName()));
        }

        // idpUserAccount exists, there is already another tenantAwareAccount from another tenant registered to this idpUserAccount
        // in this case, just append tenant-id to existing user-attributes of idpUserAccount and attach tenant-specific roles
        UserRepresentation existingIdpUserAccount = idpUserAccounts.getFirst();
        account.setUserId(existingIdpUserAccount.getId());

        UserResource existingIdpUserAccountUserResource = usersResource.get(existingIdpUserAccount.getId());

        // append tenant-id
        addTenantIdAttributeIfAbsent(existingIdpUserAccountUserResource, institution);
        return accountRepository.save(account);
    }

    /**
     * add institution admin role to account
     *
     * @param accountId
     */
    @Transactional
    public void grantInstitutionAdminRole(UUID accountId) {
        Account account = findById(accountId);
        Objects.requireNonNull(account.getUserId(), "user id cannot be null");
        Objects.requireNonNull(account.getTenantId(), "tenantId cannot be null");


        String INSTITUTION_ADMIN_ROLE = String.format("%s_institution-admin", account.getTenantId());
        grantRolesOnIdp(account.getUserId(), List.of(INSTITUTION_ADMIN_ROLE));
    }

    /**
     * add outlet admin role of specified outlets to account
     * if outlet admin for a outlet has already been granted, skip
     *
     * @param accountId
     * @param outletIds
     */
    @Transactional
    public void grantOutletAdminRoles(UUID accountId, List<UUID> outletIds) {
        Account account = findById(accountId);
        Objects.requireNonNull(account.getTenantId(), "tenantId cannot be null");
        Objects.requireNonNull(account.getUserId(), "userId cannot be null");
        Objects.requireNonNull(outletIds, "outletId cannot be null");

        List<Outlet> outletsToGrant = outletsService.findByIds(outletIds);


        // grant roles on keycloak
        List<String> OUTLET_ADMIN_ROLES = outletsToGrant
                .stream()
                .map(outlet -> String.format("%s_%s_outlet-admin", outlet.getTenantId(), outlet.getId()))
                .toList();
        grantRolesOnIdp(account.getUserId(), OUTLET_ADMIN_ROLES);

    }

    /**
     * add educator role to account
     *
     * @param accountId account id
     */
    @Transactional
    public void grantEducatorRole(UUID accountId) {
        Account account = findById(accountId);
        Objects.requireNonNull(account.getTenantId(), "tenantId cannot be null");
        Objects.requireNonNull(account.getUserId(), "userId cannot be null");

        String EDUCATOR_ROLE = String.format("%s_%s", account.getTenantId(), "educator-report-mobile");
        grantRolesOnIdp(account.getUserId(), List.of(EDUCATOR_ROLE));
    }

    @Transactional
    public AccountEducatorAttachment attachEducatorToAccount(UUID educatorId, UUID accountId) {
        Account account = findById(accountId);
        Educator educator = educatorsService.findById(educatorId);
        AccountEducatorAttachment accountEducatorAttachment = Attachment.createAndSync(account, educator, new AccountEducatorAttachment());
        educator.getAccountEducatorAttachments().add(accountEducatorAttachment);
        account.getAccountEducatorAttachments().add(accountEducatorAttachment);
        return accountEducatorAttachmentRepository.saveAndFlush(accountEducatorAttachment);
    }

    /**
     * add student role to account
     *
     * @param accountId account id
     */
    @Transactional
    public void grantStudentRole(UUID accountId) {
        Account account = findById(accountId);
        Objects.requireNonNull(account.getTenantId(), "tenantId cannot be null");
        Objects.requireNonNull(account.getUserId(), "userId cannot be null");

        String STUDENT_ROLE = String.format("%s_%s", account.getTenantId(), "student-report-mobile");
        grantRolesOnIdp(account.getUserId(), List.of(STUDENT_ROLE));
    }

    /**
     * set relationship. this method should only be called after adding students to account
     *
     * @param accountId
     * @param relationship
     */
    @Transactional
    public void setRelationship(UUID accountId, Account.RELATIONSHIP relationship) {
        Objects.requireNonNull(relationship, "relationship cannot be set to null");
        Account account = findById(accountId);
        account.setRelationship(relationship);
    }

    /**
     * create and add student client role to account
     * This operation does not attach students to the student client role
     * you can use addStudentsToStudentClientRole to attach students to the student client role
     *
     * @param accountId id of managed account
     * @param studentId
     */
    @Transactional
    public AccountStudentAttachment attachStudentToAccount(UUID studentId, UUID accountId) {
        Account account = findById(accountId);
        Student student = studentsService.findById(studentId);
        Objects.requireNonNull(account.getTenantId(), "tenantId cannot be null");
        Objects.requireNonNull(account.getUserId(), "userId cannot be null");
        AccountStudentAttachment accountStudentAttachment = Attachment.createAndSync(account, student, new AccountStudentAttachment());
        student.getAccountStudentAttachments().add(accountStudentAttachment);
        account.getAccountStudentAttachments().add(accountStudentAttachment);
        return accountStudentAttachmentRepository.saveAndFlush(accountStudentAttachment);
    }

    @Transactional
    public List<AccountStudentAttachment> getAccountStudentAttachments(UUID accountId) {
        return findById(accountId).getAccountStudentAttachments().stream().toList();
    }

    @Transactional
    public List<AccountEducatorAttachment> getAccountEducatorAttachments(UUID accountId) {
        return findById(accountId).getAccountEducatorAttachments().stream().toList();
    }

    private static void addTenantIdAttributeIfAbsent(UserResource userResource, Institution institution) {
        // add new tenant_id attributes to existing user
        UserRepresentation userRepresentation = userResource.toRepresentation();
        Map<String, List<String>> attributes = userRepresentation.getAttributes();

        // sanity check that the existing user has the attrs set
        if (attributes == null || attributes.get("tenant-ids") == null) {
            log.error("user with no tenant found in system: {}", userRepresentation.getEmail());
            throw new RuntimeException("user with no tenant found in system");
        }

        List<String> tenantIdsAttribute = attributes.get("tenant-ids");

        // add tenant id if its not existing in list
        if (!tenantIdsAttribute.contains(institution.getId().toString())) {
            tenantIdsAttribute.add(institution.getId().toString());
            userRepresentation.setAttributes(Map.of("tenant-ids", tenantIdsAttribute));
            userResource.update(userRepresentation);
        }
    }

    /**
     * get all accounts in a tenant
     *
     * @return
     */
    @Transactional
    public Collection<Account> getAllTenantAwareAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Get all accounts in a tenant by email
     */
    @Transactional
    public Account getAllTenantAwareAccountsByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account with email " + email + " does not exist"));
    }

    /**
     * get required actions for an account in a tenant
     *
     * @param accountId
     * @return
     */
    public List<String> getPendingActionsOfTenantAwareAccount(UUID accountId) {
        Account account = findById(accountId);
        UserResource userResource = usersResource.get(account.getUserId());
        UserRepresentation userAccountInIdp = userResource.toRepresentation();
        return userAccountInIdp.getRequiredActions();
    }

    /**
     * grant roles on idp to user (keycloak)
     * this method is idempotent and does not throw an error if the roles are already granted
     *
     * @param userId
     * @param roles
     */
    private void grantRolesOnIdp(String userId, List<String> roles) {
        Objects.requireNonNull(userId, "userId cannot be null");
        List<RoleRepresentation> grantedTenantAwareRoles = roles
                .stream()
                .map(r -> clientResource.roles().get(r).toRepresentation())
                .toList();

        UserResource userResource = usersResource.get(userId);
        userResource.roles().clientLevel(clientResource.toRepresentation().getId()).add(grantedTenantAwareRoles);
        log.info("Client roles {} has been granted to user {}", String.join(", ", grantedTenantAwareRoles.stream().map(RoleRepresentation::getName).toList()), userId);
    }

}
