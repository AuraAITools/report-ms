package com.reportai.www.reportapi.services.students;

import com.reportai.www.reportapi.api.v1.accounts.requests.CreateStudentRequestDTO;
import com.reportai.www.reportapi.api.v1.students.requests.UpdateStudentRequestDTO;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.School;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.attachments.StudentOutletRegistration;
import com.reportai.www.reportapi.entities.helpers.Attachment;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.SchoolRepository;
import com.reportai.www.reportapi.repositories.StudentOutletRegistrationRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import com.reportai.www.reportapi.services.courses.CoursesService;
import com.reportai.www.reportapi.services.levels.LevelsService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentsService implements ISimpleRead<Student> {
    private final StudentRepository studentRepository;

    private final LevelsService levelsService;

    private final ModelMapper modelMapper;

    private final CoursesService coursesService;

    private final OutletsService outletsService;
    private final SchoolRepository schoolRepository;
    private final StudentOutletRegistrationRepository studentOutletRegistrationRepository;

    @Autowired
    public StudentsService(StudentRepository studentRepository, LevelsService levelsService, @Lazy CoursesService coursesService, @Lazy OutletsService outletsService,
                           SchoolRepository schoolRepository,
                           StudentOutletRegistrationRepository studentOutletRegistrationRepository,
                           ModelMapper modelMapper) {
        this.studentRepository = studentRepository;
        this.levelsService = levelsService;
        this.coursesService = coursesService;
        this.outletsService = outletsService;
        this.modelMapper = modelMapper;
        this.schoolRepository = schoolRepository;
        this.studentOutletRegistrationRepository = studentOutletRegistrationRepository;
    }


    @Override
    public JpaRepository<Student, UUID> getRepository() {
        return this.studentRepository;
    }

    public Collection<Student> getAllStudentsInInstitution(@NonNull UUID institutionId) {
        return studentRepository.findAll();
    }

    /**
     * creates a new student in an institution
     *
     * @param createStudentRequestDTO
     * @return
     */
    @Transactional
    public Student create(@NonNull CreateStudentRequestDTO createStudentRequestDTO) {
        Student student = modelMapper.map(createStudentRequestDTO, Student.class);
        studentRepository.saveAndFlush(student);
        Level level = levelsService.findById(createStudentRequestDTO.getLevelId());
        School school = schoolRepository.findById(createStudentRequestDTO.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("School with id %s not found", createStudentRequestDTO.getSchoolId())));
        Optional.ofNullable(createStudentRequestDTO.getOutletId())
                .ifPresent((outletId) -> addStudentToOutlet(student.getId(), outletId));

        Optional.ofNullable(createStudentRequestDTO.getCourseIds())
                .ifPresent((courseIds) -> courseIds.forEach(courseId -> coursesService.registerStudentsToCourse(courseId, List.of(student.getId()))));
        student.setLevel(level);
        student.setSchool(school);
        return studentRepository.save(student);
    }

    /**
     * adds a student to an outlet
     *
     * @param studentId
     * @param outletId
     * @return
     */
    @Transactional
    public StudentOutletRegistration addStudentToOutlet(UUID studentId, UUID outletId) {
        Student student = findById(studentId);
        Outlet outlet = outletsService.findById(outletId);
        StudentOutletRegistration studentOutletRegistration = Attachment.createAndSync(student, outlet, new StudentOutletRegistration());
        return studentOutletRegistrationRepository.saveAndFlush(studentOutletRegistration);
    }

    @Transactional
    public Student update(UUID studentId, UpdateStudentRequestDTO updates) {
        Student student = findById(studentId);
        modelMapper.map(updates, student);
        return student;
    }

}
