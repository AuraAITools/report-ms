//package com.reportai.www.reportapi.api.v1;
//
//import com.reportai.www.reportapi.api.commons.SimpleCRUDController;
//import com.reportai.www.reportapi.entities.Course;
//import com.reportai.www.reportapi.services.commons.CRUDService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/v1/courses")
//@Slf4j
//public class CourseController extends SimpleCRUDController<Course, UUID> {
//
//    private final RegistrationService registrationService;
//
//    public CourseController(CRUDService<Course, UUID> service, RegistrationService registrationService) {
//        super(service);
//        this.registrationService = registrationService;
//    }
//
//    @PatchMapping("/{course_id}/students/{student_id}/register")
//    public ResponseEntity<Void> registerStudentToCourse(@RequestParam(name = "course_id") UUID courseId, @RequestParam(name = "student_id") UUID studentId) {
//        registrationService.linkStudentToCourse(studentId,courseId);
//        return ResponseEntity.ok(null);
//    }
//
//    @PatchMapping("/{course_id}/educators/{educator_id}/register")
//    public ResponseEntity<Void> registerEducatorToCourse(@RequestParam(name = "course_id") UUID courseId, @RequestParam(name = "educator_id") UUID educatorId) {
//        registrationService.linkEducatorToCourse(educatorId,courseId);
//        return ResponseEntity.ok(null);
//    }
//
//    @PatchMapping("/{course_id}/subjects/{subject_id}/register")
//    public ResponseEntity<Void> registerSubjectToClass(@RequestParam(name = "course_id") UUID courseId, @RequestParam(name = "subject_id") UUID educatorId) {
//        registrationService.linkSubjectToCourse(educatorId,courseId);
//        return ResponseEntity.ok(null);
//    }
//}
