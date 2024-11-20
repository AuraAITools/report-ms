package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.clients.keycloak.KeycloakAuthClient;
import com.reportai.www.reportapi.dtos.requests.CreateClientAccountDTO;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.exceptions.http.HttpInstitutionNotFoundException;
import com.reportai.www.reportapi.mappers.InstitutionMappers;
import com.reportai.www.reportapi.mappers.StudentMappers;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final KeycloakAuthClient keycloakAuthClient;
    private final StudentService studentService;
    private final InstitutionRepository institutionRepository;

    @Autowired
    public AdminService(KeycloakAuthClient keycloakAuthClient, StudentService studentService, InstitutionRepository institutionRepository) {
        this.keycloakAuthClient = keycloakAuthClient;
        this.studentService = studentService;
        this.institutionRepository = institutionRepository;
    }

    @Transactional
    public Optional<Institution> getInstitutionById(UUID id) {
        return institutionRepository.findById(id);
    }

    @Transactional
    public Institution updateInstitution(UUID id, Institution updates) {
        Institution existingInstitution = institutionRepository.findById(id).orElseThrow(HttpInstitutionNotFoundException::new);
        Institution institution = InstitutionMappers.layover(existingInstitution, updates);
        return institutionRepository.save(institution);
    }

    @Transactional
    public String createStudentAccountForInstitution(CreateClientAccountDTO createClientAccountDTO, UUID institutionId) {
        // TODO: do some checks, user exists? students exists?
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(createClientAccountDTO.getEmail());
        userRepresentation.setFirstName(createClientAccountDTO.firstName);
        userRepresentation.setLastName(createClientAccountDTO.lastName);
        String createdUserId = keycloakAuthClient.createUserAccount(userRepresentation);
        // TODO: create students here
        studentService.createStudents(StudentMappers.convert(createClientAccountDTO));
        keycloakAuthClient.sendPendingActionsToUserEmail(createdUserId);
        return createdUserId;
    }
}
