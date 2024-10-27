package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.clients.keycloak.KeycloakAuthClient;
import com.reportai.www.reportapi.dtos.requests.CreateClientAccountDTO;
import com.reportai.www.reportapi.mappers.StudentMappers;
import jakarta.transaction.Transactional;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminService {
    private final KeycloakAuthClient keycloakAuthClient;
    private final StudentService studentService;

    @Autowired
    public AdminService(KeycloakAuthClient keycloakAuthClient, StudentService studentService) {
        this.keycloakAuthClient = keycloakAuthClient;
        this.studentService = studentService;
    }

    @Transactional
    public String createStudentAccountForInstitution(CreateClientAccountDTO createClientAccountDTO, UUID institutionId) {
        // TODO: do some checks, user exists? students exists?
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(createClientAccountDTO.getEmail());
        userRepresentation.setFirstName(createClientAccountDTO.firstName);
        userRepresentation.setLastName(createClientAccountDTO.lastName);
        String createdUserId = keycloakAuthClient.createDefaultUserAccount(userRepresentation);
        // TODO: create students here
        studentService.createStudents(StudentMappers.convert(createClientAccountDTO));
        keycloakAuthClient.sendPendingActionsToUserEmail(createdUserId);
        return createdUserId;
    }


}
