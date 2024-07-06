package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.services.OnboardingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("v1")
@RestController
@Slf4j
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
    public ResponseEntity<Student> onboard(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid Student student) {
        // TODO: yay i can extract from here
        log.info("email: "+ jwt.getClaim("email"));
        Student onboardedStudent = onboardingService.onboard(student);
        return new ResponseEntity<>(onboardedStudent, HttpStatus.CREATED);
    }

    @PostMapping("/onboard/institutions")
    public ResponseEntity<Institution> onboard(@RequestBody @Valid Institution institution) {
        Institution onboardedInstitution = onboardingService.onboard(institution);
        return new ResponseEntity<>(onboardedInstitution, HttpStatus.CREATED);
    }

}
