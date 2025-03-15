package com.reportai.www.reportapi.services.students;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.personas.StudentClientPersona;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.StudentClientPersonaRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import com.reportai.www.reportapi.services.levels.LevelsService;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentsService implements BaseServiceTemplate<Student, UUID> {
    private final StudentRepository studentRepository;

    private final InstitutionsService institutionsService;

    private final LevelsService levelsService;

    private final SubjectsService subjectsService;

    private final StudentClientPersonaRepository studentClientPersonaRepository;

    @Autowired
    public StudentsService(StudentRepository studentRepository, InstitutionsService institutionsService, LevelsService levelsService, SubjectsService subjectsService, StudentClientPersonaRepository studentClientPersonaRepository) {
        this.studentRepository = studentRepository;
        this.institutionsService = institutionsService;
        this.levelsService = levelsService;
        this.subjectsService = subjectsService;
        this.studentClientPersonaRepository = studentClientPersonaRepository;
    }

    @Override
    public JpaRepository<Student, UUID> getRepository() {
        return this.studentRepository;
    }

    public List<Student> getAllStudentsInInstitution(@NonNull UUID institutionId) {
        Institution institution = institutionsService.findById(institutionId);
        return institution.getStudents();
    }

    @Transactional
    public Student create(@NonNull Student student) {
        return this.studentRepository.save(student);
    }

    @Transactional
    public Student addLevel(@NonNull UUID studentId, @NonNull UUID levelId) {
        Student student = findById(studentId);
        Level level = levelsService.findById(levelId);
        student.addLevel(level);
        return studentRepository.save(student);
    }

    @Transactional
    public Student addInstitution(@NonNull UUID studentId, @NonNull UUID institutionId) {
        Student student = findById(studentId);
        Institution institution = institutionsService.findById(institutionId);
        student.addInstitution(institution);
        return studentRepository.save(student);
    }

    @Transactional
    public Student addStudentClientPersona(@NonNull UUID studentId, @NonNull UUID studentClientPersonaId) {
        Student student = findById(studentId);
        StudentClientPersona studentClientPersona = studentClientPersonaRepository.findById(studentClientPersonaId).orElseThrow(() -> new ResourceNotFoundException("no student client persona found"));
        student.addStudentClientPersona(studentClientPersona);
        return studentRepository.save(student);
    }

}
