package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.dtos.responses.AccountDTO;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.IAccount;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.ParentRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("v1")
@RestController
@Slf4j
public class UserController {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final InstitutionRepository institutionRepository;
    private final EducatorRepository educatorRepository;

    private final AccountService accountService;

    public UserController(ParentRepository parentRepository, StudentRepository studentRepository, InstitutionRepository institutionRepository, EducatorRepository educatorRepository, AccountService accountService) {
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.institutionRepository = institutionRepository;
        this.educatorRepository = educatorRepository;
        this.accountService = accountService;
    }

    @GetMapping("/users/{user_id}/accounts")
    public ResponseEntity<List<AccountDTO>> getAccountsOfUser(@PathVariable(name = "user_id") UUID userId) {
        List<AccountDTO> userAccounts = accountService.getAllAccountsOfUser(userId);
        return new ResponseEntity<>(userAccounts, HttpStatus.OK);
    }

    @GetMapping("/parents/{id}")
    public ResponseEntity<Parent> getParentById(@PathVariable(name = "id")UUID id) {
        Optional<Parent> parentResult = parentRepository.findById(id);

        if (parentResult.isEmpty()) {
            throw new NotFoundException("Parent not found");
        }

        return new ResponseEntity<>(parentResult.get(), HttpStatus.OK);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable(name = "id")UUID id) {
        Optional<Student> studentResult = studentRepository.findById(id);

        if (studentResult.isEmpty()) {
            throw new NotFoundException("Student not found");
        }

        return new ResponseEntity<>(studentResult.get(), HttpStatus.OK);
    }

    @GetMapping("/educators/{id}")
    public ResponseEntity<Educator> getEducatorById(@PathVariable(name = "id")UUID id) {
        Optional<Educator> educatorResult = educatorRepository.findById(id);

        if (educatorResult.isEmpty()) {
            throw new NotFoundException("Educator not found");
        }

        return new ResponseEntity<>(educatorResult.get(), HttpStatus.OK);
    }

    @GetMapping("/institutions/{id}")
    public ResponseEntity<Institution> getInstitutionById(@PathVariable(name = "id")UUID id) {
        Optional<Institution> institutionResult = institutionRepository.findById(id);

        if (institutionResult.isEmpty()) {
            throw new NotFoundException("Institution not found");
        }

        return new ResponseEntity<>(institutionResult.get(), HttpStatus.OK);
    }
}