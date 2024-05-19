package com.reportai.www.reportapi.api.v1;

//import com.reportai.www.reportapi.dtos.requests.CreateUserRequestBody;
import com.reportai.www.reportapi.services.OnboardingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

//    @PostMapping("/onboard/users")
//    public ResponseEntity<User> onboardUser(@RequestBody @Valid CreateUserRequestBody createUserRequestBody) {
//        User user = onboardingService.onboard(createUserRequestBody.toEntity());
//        return new ResponseEntity<>(user, HttpStatus.CREATED);
//    }

}
