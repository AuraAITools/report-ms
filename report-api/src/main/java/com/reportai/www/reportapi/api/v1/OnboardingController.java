package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.services.OnboardingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class OnboardingController {

    private final OnboardingService onboardingService;

    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }


    @PostMapping("/parent")
    public ResponseEntity<Parent> onboardParent(@ResponseBody Parent parent) {
        onboardingService.onboardToUser()
    }
}
