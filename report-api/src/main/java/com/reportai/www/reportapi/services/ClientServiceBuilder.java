package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.dtos.mapper.SubjectMapper;
import com.reportai.www.reportapi.repositories.StudentRepository;

public class ClientServiceBuilder {

    private StudentRepository studentRepository;
    private SubjectMapper subjectMapper;

    public ClientServiceBuilder setStudentRepository(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        return this;
    }

    public ClientServiceBuilder setSubjectMapper(SubjectMapper subjectMapper) {
        this.subjectMapper = subjectMapper;
        return this;
    }

    public ClientService createClientService() {
        return new ClientService(studentRepository, subjectMapper);
    }
}