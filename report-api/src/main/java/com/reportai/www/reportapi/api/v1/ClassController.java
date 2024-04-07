//package com.reportai.www.reportapi.api.v1;
//
//import com.reportai.www.reportapi.api.commons.SimpleCRUDController;
//import com.reportai.www.reportapi.entities.Class;
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
//@RequestMapping("/api/v1/classes")
//@Slf4j
//public class ClassController extends SimpleCRUDController<Class, UUID> {
//
//    private final RegistrationService registrationService;
//
//    public ClassController(CRUDService<Class, UUID> service, RegistrationService registrationService) {
//        super(service);
//        this.registrationService = registrationService;
//    }
//
//    @PatchMapping("/{class_id}/students/{student_id}/register")
//    public ResponseEntity<Void> registerStudentToClass(@RequestParam(name = "class_id") UUID classId, @RequestParam(name = "student_id") UUID studentId) {
//        registrationService.linkStudentToClass(studentId,classId);
//        return ResponseEntity.ok(null);
//    }
//
//    @PatchMapping("/{class_id}/educators/{educator_id}/register")
//    public ResponseEntity<Void> registerEducatorToClass(@RequestParam(name = "class_id") UUID classId, @RequestParam(name = "educator_id") UUID educatorId) {
//        registrationService.linkEducatorToClass(educatorId,classId);
//        return ResponseEntity.ok(null);
//    }
//
//    @PatchMapping("/{class_id}/subjects/{subject_id}/register")
//    public ResponseEntity<Void> registerSubjectToClass(@RequestParam(name = "class_id") UUID classId, @RequestParam(name = "subject_id") UUID educatorId) {
//        registrationService.linkSubjectToClass(educatorId,classId);
//        return ResponseEntity.ok(null);
//    }
//}
