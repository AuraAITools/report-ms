package com.reportai.www.reportapi.services.students;

import com.reportai.www.reportapi.api.v1.students.requests.UpdateStudentRequestDTO;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.repositories.StudentClientPersonaRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.courses.CoursesService;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import com.reportai.www.reportapi.services.levels.LevelsService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentsService implements BaseServiceTemplate<Student, UUID> {
    private final StudentRepository studentRepository;

    private final InstitutionsService institutionsService;

    private final LevelsService levelsService;

    private final SubjectsService subjectsService;

    private final StudentClientPersonaRepository studentClientPersonaRepository;

    private final ModelMapper modelMapper;

    private final CoursesService coursesService;

    private final OutletsService outletsService;

    @Autowired
    public StudentsService(StudentRepository studentRepository, InstitutionsService institutionsService, LevelsService levelsService, SubjectsService subjectsService, StudentClientPersonaRepository studentClientPersonaRepository, @Lazy CoursesService coursesService, @Lazy OutletsService outletsService) {
        this.studentRepository = studentRepository;
        this.institutionsService = institutionsService;
        this.levelsService = levelsService;
        this.subjectsService = subjectsService;
        this.studentClientPersonaRepository = studentClientPersonaRepository;
        this.coursesService = coursesService;
        this.outletsService = outletsService;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);// skip null values
        Converter<List<UUID>, List<Course>> courseIdsToCoursesConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }
            List<UUID> courseIds = context.getSource();
            return courseIds.stream()
                    .map(coursesService::findById)
                    .collect(Collectors.toList());
        };

        Converter<UUID, Level> levelIdToLevelConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }
            UUID levelId = context.getSource();
            return levelsService.findById(levelId);
        };

        Converter<List<UUID>, List<Outlet>> outletIdsToOutletsConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }
            List<UUID> outletIds = context.getSource();
            return outletIds.stream()
                    .map(outletsService::findById)
                    .collect(Collectors.toList());
        };

        this.modelMapper
                .typeMap(UpdateStudentRequestDTO.class, Student.class)
//                .addMappings(mapper -> {
//                    mapper.using(courseIdsToCoursesConverter)
//                            .map(UpdateStudentRequestDTO::getCourseIds, Student::setCourses);
//                })
                .addMappings(mapper -> {
                    mapper.using(levelIdToLevelConverter)
                            .map(UpdateStudentRequestDTO::getLevelId, Student::setLevel);
                });
//                .addMappings(mapper -> {
//                    mapper.using(outletIdsToOutletsConverter)
//                            .map(UpdateStudentRequestDTO::getOutletIds, Student::setOutlets);
//                });
    }

    @Override
    public JpaRepository<Student, UUID> getRepository() {
        return this.studentRepository;
    }

    public Collection<Student> getAllStudentsInInstitution(@NonNull UUID institutionId) {
        return studentRepository.findAll();
    }

    @Transactional
    public Student addLevel(@NonNull UUID studentId, @NonNull UUID levelId) {
        Student student = findById(studentId);
        Level level = levelsService.findById(levelId);
        student.addLevel(level);
        return studentRepository.save(student);
    }

    @Transactional
    public Student update(UUID studentId, UpdateStudentRequestDTO updates) {
        Student student = findById(studentId);
        modelMapper.map(updates, student);
        return student;
    }

}
