package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.services.OnboardingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class OnboardingController {

    private final OnboardingService onboardingService;

    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @PostMapping("/onboard/parents")
    public ResponseEntity<Parent> onboard(@RequestBody @Valid Parent parent) {
        Parent onboardedParent = onboardingService.onboard(parent);
        return new ResponseEntity<>(onboardedParent, HttpStatus.CREATED);
    }

    @PostMapping("/onboard/educators")
    public ResponseEntity<Educator> onboard(@RequestBody @Valid Educator educator) {
        Educator onboardedEducator = onboardingService.onboard(educator);
        return new ResponseEntity<>(onboardedEducator, HttpStatus.CREATED);
    }

    @PostMapping("/onboard/students")
    public ResponseEntity<Student> onboard(@RequestBody @Valid Student student) {
        Student onboardedStudent = onboardingService.onboard(student);
        return new ResponseEntity<>(onboardedStudent, HttpStatus.CREATED);
    }

    @PostMapping("/onboard/institutions")
    public ResponseEntity<Institution> onboard(@RequestBody @Valid Institution institution) {
        Institution onboardedInstitution = onboardingService.onboard(institution);
        return new ResponseEntity<>(onboardedInstitution, HttpStatus.CREATED);
    }

}
