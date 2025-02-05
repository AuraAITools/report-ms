package com.reportai.www.reportapi.services.clients;

import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Client;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.ClientRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    private final AccountRepository accountRepository;

    private final ClientResource clientResource;

    private final UsersResource usersResource;

    private static final List<String> GRANTED_CLIENT_ROLES = List.of("client-report-mobile");

    @Autowired
    public ClientService(ClientRepository clientRepository, AccountRepository accountRepository, ClientResource clientResource, UsersResource usersResource) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.clientResource = clientResource;
        this.usersResource = usersResource;
    }

    @Transactional
    public Client linkClientToAccount(Client client, UUID accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("account is not found"));
        if (account.getClient() != null) {
            throw new ResourceAlreadyExistsException("A client already exists for this account. you can only have 1 client per account");
        }
        UserResource userResource = usersResource.get(account.getUserId());

        // add roles for created user
        // TODO: see if we can add roles after email is verified
        List<RoleRepresentation> grantedTenantAwareRoles = GRANTED_CLIENT_ROLES
                .stream()
                .map(role -> {
                    String tenantAwareRole = String.format("%s_%s", account.getInstitution().getId(), role);
                    return clientResource.roles().get(tenantAwareRole).toRepresentation();
                }).toList();

        userResource.roles().clientLevel(clientResource.toRepresentation().getId()).add(grantedTenantAwareRoles);
        log.info("Client roles {} has been granted to user {}", String.join(", ", grantedTenantAwareRoles.stream().map(RoleRepresentation::getName).toList()), account.getUserId());

        return clientRepository.save(client);
    }
}
