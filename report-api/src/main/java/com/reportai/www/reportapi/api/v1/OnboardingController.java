package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.dtos.requests.CreateEducatorRequestBody;
import com.reportai.www.reportapi.dtos.requests.CreateInstitutionRequestBody;
import com.reportai.www.reportapi.dtos.requests.CreateParentRequestBody;
import com.reportai.www.reportapi.dtos.requests.CreateStudentRequestBody;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.services.OnboardingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.nonNull;

@RequestMapping("/api/v1")
@RestController
public class OnboardingController {

    private final OnboardingService onboardingService;

    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @PostMapping("/onboard/parents")
    public ResponseEntity<Parent> onboardParent(@RequestBody @Valid CreateParentRequestBody createParentRequestBody) {
        Parent onboardedParent = onboardingService.onboard(createParentRequestBody.toEntity());

        return new ResponseEntity<>(onboardedParent, HttpStatus.CREATED);
    }

    @PostMapping("/onboard/students")
    public ResponseEntity<Student> onboardStudent(@RequestBody @Valid CreateStudentRequestBody createStudentRequestBody) {
        Student onboardedStudent = onboardingService.onboard(createStudentRequestBody.toEntity());
        return new ResponseEntity<>(onboardedStudent, HttpStatus.CREATED);
    }

    @PostMapping("/onboard/educators")
    public ResponseEntity<Educator> onboardEducator(@RequestBody @Valid CreateEducatorRequestBody createEducatorRequestBody) {
        Educator onboardedEducator = onboardingService.onboard(createEducatorRequestBody.toEntity());
        return new ResponseEntity<>(onboardedEducator, HttpStatus.CREATED);
    }

    @PostMapping("/onboard/institutions")
    public ResponseEntity<Institution> onboardInstitution(@RequestBody @Valid CreateInstitutionRequestBody createInstitutionRequestBody) {
        Institution onboardedInstitution = onboardingService.onboard(createInstitutionRequestBody.toEntity());
        return new ResponseEntity<>(onboardedInstitution, HttpStatus.CREATED);
    }
}
