package com.reportai.www.reportapi.api.v1.levels;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.StudentResponseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.api.v1.levels.requests.CreateLevelsRequestDTO;
import com.reportai.www.reportapi.api.v1.levels.requests.UpdateLevelRequestDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.ExpandedLevelsResponseDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.LevelsResponseDTO;
import com.reportai.www.reportapi.api.v1.subjects.responses.SubjectResponseDTO;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.attachments.LevelEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.SubjectLevelAttachment;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.entities.educators.Educator;
import com.reportai.www.reportapi.mappers.CourseMappers;
import com.reportai.www.reportapi.mappers.EducatorMappers;
import com.reportai.www.reportapi.mappers.LevelMappers;
import com.reportai.www.reportapi.mappers.StudentMappers;
import com.reportai.www.reportapi.mappers.SubjectMappers;
import com.reportai.www.reportapi.services.levels.LevelsService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.reportai.www.reportapi.mappers.LevelMappers.convert;
import static com.reportai.www.reportapi.mappers.LevelMappers.convertExpanded;

@Tag(name = "Levels APIs", description = "APIs for managing a Levels resource")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class LevelsController {

    private final LevelsService levelsService;

    private final OutletsService outletsService;

    private final StudentsService studentsService;

    @Autowired
    public LevelsController(LevelsService levelsService, OutletsService outletsService, StudentsService studentsService) {
        this.levelsService = levelsService;
        this.outletsService = outletsService;
        this.studentsService = studentsService;
    }

    @Operation(summary = "create a level for a institution", description = "create an level for a institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/levels")
    @HasResourcePermission(permission = "'institutions::' + #id + '::levels:create'")
    @Transactional
    public ResponseEntity<LevelsResponseDTO> createLevelForInstitution(@PathVariable UUID id, @Valid @RequestBody CreateLevelsRequestDTO createLevelsRequestDTO) {
        Level createdLevel = levelsService.createLevelForInstitution(id, convert(id, createLevelsRequestDTO), createLevelsRequestDTO.getSubjects());
        return new ResponseEntity<>(convert(createdLevel), HttpStatus.OK);
    }

    @Operation(summary = "update a level for a institution", description = "update an level for a institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/institutions/{id}/levels/{level_id}")
    @HasResourcePermission(permission = "'institutions::' + #id + '::levels:update'")
    @Transactional
    public ResponseEntity<ExpandedLevelsResponseDTO> updateLevelForInstitution(@PathVariable UUID id, @PathVariable(name = "level_id") UUID levelId, @Valid @RequestBody UpdateLevelRequestDTO updateLevelRequestDTO) {
        Level createdLevel = levelsService.update(levelId, updateLevelRequestDTO);
        return new ResponseEntity<>(convertExpanded(createdLevel), HttpStatus.OK);
    }

    @Operation(summary = "get all levels for a institution", description = "get all levels for a institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/levels")
    @HasResourcePermission(permission = "'institutions::' + #id + '::levels:read'")
    public ResponseEntity<List<LevelsResponseDTO>> getAllLevelsForInstitution(@PathVariable UUID id) {
        Collection<Level> levels = levelsService.getAllLevelsForInstitution();
        List<LevelsResponseDTO> levelsDTO = levels
                .stream()
                .map(LevelMappers::convert)
                .toList();
        return new ResponseEntity<>(levelsDTO, HttpStatus.OK);
    }

    /**
     * @param id
     * @return
     */
    @Operation(summary = "get all expanded levels for a institution", description = "get all expanded levels for a institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/levels/expand")
    @HasResourcePermission(permission = "'institutions::' + #id + '::levels:read'")
    public ResponseEntity<List<ExpandedLevelsResponseDTO>> getAllExpandedLevelsForInstitution(@PathVariable UUID id) {
        Collection<Level> levels = levelsService.getAllLevelsForInstitution();
        List<ExpandedLevelsResponseDTO> levelsDTO = levels
                .stream()
                .map(LevelMappers::convertExpanded)
                .toList();
        return new ResponseEntity<>(levelsDTO, HttpStatus.OK);
    }

    // TODO: get all students of level in an outlet
    @Operation(summary = "get all students of a level of a outlet", description = "get all levels for a institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/outlets/{outlet_id}/levels/{level_id}/students")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::students:read'")
    public ResponseEntity<List<StudentResponseDTO>> getAllStudentsOfLevelInOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "level_id") UUID levelId) {
        Collection<Student> students = levelsService.findById(levelId)
                .getStudents();
        return new ResponseEntity<>(students.stream().map(StudentMappers::convert).toList(), HttpStatus.OK);
    }

    // TODO: get all courses of level
    @Operation(summary = "get all courses of a level of a outlet", description = "get all levels for a institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/outlets/{outlet_id}/levels/{level_id}/courses")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses:read'")
    public ResponseEntity<List<CourseResponseDTO>> getAllCoursesOfLevelInOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "level_id") UUID levelId) {
        List<Course> courses = levelsService.findById(levelId)
                .getCourses()
                .stream()
                .filter(course -> course
                        .getOutlet().getId().equals(outletId)).toList();
        return new ResponseEntity<>(courses.stream().map(CourseMappers::convert).toList(), HttpStatus.OK);
    }

    // TODO: get all educators of level
    @Operation(summary = "get all educators of a level of a outlet", description = "get all levels for a institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/outlets/{outlet_id}/levels/{level_id}/educators")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::educators:read'")
    public ResponseEntity<List<EducatorResponseDTO>> getAllEducatorsOfLevelInOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "level_id") UUID levelId) {
        List<Educator> educators = levelsService.findById(levelId)
                .getLevelEducatorAttachments()
                .stream()
                .map(LevelEducatorAttachment::getEducator)
                .toList();
        return new ResponseEntity<>(educators.stream().map(EducatorMappers::convert).toList(), HttpStatus.OK);
    }

    // TODO: get all subjects of level
    @Operation(summary = "get all subjects of a level in an institution", description = "get all subjects for a level in an institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/levels/{level_id}/subjects")
    @HasResourcePermission(permission = "'institutions::' + #id + '::subjects:read'")
    public ResponseEntity<List<SubjectResponseDTO>> getAllSubjectsOfLevelInInstitution(@PathVariable UUID id, @PathVariable(name = "level_id") UUID levelId) {
        List<Subject> subjects = levelsService.findById(levelId)
                .getSubjectLevelAttachments()
                .stream()
                .map(SubjectLevelAttachment::getSubject)
                .toList();
        return new ResponseEntity<>(subjects.stream().map(SubjectMappers::convert).toList(), HttpStatus.OK);
    }

    // attach a subject to a level
    @Operation(summary = "attach a subject to a level in an institution", description = "Attach a subject to a level in the institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/institutions/{id}/levels/{level_id}/subjects/{subject_id}")
    @HasResourcePermission(permission = "'institutions::' + #id + '::subjects:attach'")
    public ResponseEntity<List<SubjectResponseDTO>> attachSubjectToLevelInInstitution(@PathVariable UUID id, @PathVariable(name = "level_id") UUID levelId, @PathVariable(name = "subject_id") UUID subjectId) {
        Subject subject = levelsService.addSubject(levelId, subjectId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
