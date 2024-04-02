package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Test;
import com.reportai.www.reportapi.entities.TestGroup;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.services.commons.CRUDServiceSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TestGroupService extends CRUDServiceSupport<TestGroup, UUID> {
    private final InstitutionService institutionService;

    private final TestService testService;
    public TestGroupService(JpaRepository<TestGroup, UUID> repository, InstitutionService institutionService, TestService testService) {
        super(repository);
        this.institutionService = institutionService;
        this.testService = testService;
    }

    public TestGroup createTestGroupInInstitution(UUID institutionId, TestGroup testGroup) {
        Institution institution = institutionService.findById(institutionId);
        institution.getTestGroups().add(testGroup);
        institutionService.update(institution);
        return testGroup;
    }

    public Test createTestInTestGroup(UUID testGroupId, Test test) {
        Optional<TestGroup> result = repository.findById(testGroupId);
        if (result.isEmpty()) {
            throw new NotFoundException("Test Group not found");
        }

        TestGroup testGroup = result.get();
        testGroup.getTests().add(test);
        repository.save(testGroup);
        return test;
    }
}
