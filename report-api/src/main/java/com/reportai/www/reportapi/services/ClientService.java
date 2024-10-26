package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.dtos.mapper.SubjectMapper;
import com.reportai.www.reportapi.dtos.responses.LessonDto;
import com.reportai.www.reportapi.dtos.responses.SubjectDto;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.repositories.StudentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClientService {

    private StudentRepository studentRepository;
    private SubjectMapper subjectMapper;

    public ClientService(StudentRepository studentRepository, SubjectMapper subjectMapper) {
        this.studentRepository = studentRepository;
        this.subjectMapper = subjectMapper;
    }

    public List<SubjectDto> getSubjectsAndLessonsByStudentId(UUID studentId) throws Exception {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new Exception("Student not found"));

        List<Subject> subjects = new ArrayList<>(student.getSubjects());
        return subjectMapper.toDtoList(subjects);
    }


}
