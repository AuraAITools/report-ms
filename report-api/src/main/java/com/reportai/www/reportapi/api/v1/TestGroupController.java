package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.api.commons.SimpleCRUDController;
import com.reportai.www.reportapi.entities.Test;
import com.reportai.www.reportapi.entities.TestGroup;
import com.reportai.www.reportapi.services.commons.CRUDService;
import com.reportai.www.reportapi.services.TestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/test-groups")
public class TestGroupController extends SimpleCRUDController<TestGroup, UUID> {


    public TestGroupController(CRUDService<TestGroup, UUID> service, TestService testService) {
        super(service);
    }

    @PostMapping("/{id}/tests")
    public ResponseEntity<Test> createTestInTestGroup(@PathVariable UUID id, @RequestBody Test test) {
        TestGroup testGroup = service.findById(id);
        testGroup.getTests().add(test);
        service.update(testGroup);
        return new ResponseEntity<>(test, HttpStatus.CREATED);
    }
}
