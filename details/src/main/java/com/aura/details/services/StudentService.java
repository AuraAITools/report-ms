package com.aura.details.services;

import com.aura.core.entities.students.Student;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.usecases.students.DeleteStudentUseCase;
import com.aura.core.usecases.students.StudentQueryUseCase;
import com.aura.core.usecases.students.UpdateStudentUseCase;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class StudentService {

    private static final Duration PROFILE_IMAGE_UPLOAD_TTL = Duration.ofMinutes(5);
    private static final Pattern PROFILE_IMAGE_KEY_PATTERN =
            Pattern.compile("^institutions/([^/]+)/students/([^/]+)/profile\\.(png|jpg|webp)$");

    private final StudentQueryUseCase studentQueryUseCase;
    private final UpdateStudentUseCase updateStudentUseCase;
    private final DeleteStudentUseCase deleteStudentUseCase;
    private final StudentRepository studentRepository;
    private final AssetUrlService assetUrlService;
    private final ProfileImageValidator profileImageValidator;

    public @Nullable Student findById(String id) {
        return studentQueryUseCase.findById(id);
    }

    public List<Student> findAllByTenantId(String tenantId) {
        return studentQueryUseCase.findAllByTenantId(tenantId);
    }

    @Transactional
    public Student updateById(String id,
                              @Nullable String name,
                              @Nullable String email,
                              @Nullable LocalDate dateOfBirth) {
        return updateStudentUseCase.update(id, name, email, dateOfBirth);
    }

    @Transactional
    public void deleteById(String id) {
        studentRepository.deleteAllStudentCourseLinksByStudentId(id);
        studentRepository.deleteAllStudentOutletLinksByStudentId(id);
        studentRepository.deleteAllStudentLessonLinksByStudentId(id);
        studentRepository.deleteAllSubjectStudentLinksByStudentId(id);
        studentRepository.deleteAllAccountStudentLinksByStudentId(id);
        deleteStudentUseCase.deleteById(id);
    }

    public PresignedProfileImageUpload requestProfileImageUpload(
            String institutionId, String studentId, String contentType) {
        if (!ProfileImageValidator.ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new ProfileImageValidator.ProfileImageRejectedException(
                    "content type '" + contentType + "' is not allowed; must be one of "
                            + ProfileImageValidator.ALLOWED_CONTENT_TYPES);
        }
        String ext = ProfileImageValidator.extensionFor(contentType);
        String fileKey = "institutions/" + institutionId + "/students/" + studentId + "/profile." + ext;
        String uploadUrl = assetUrlService.presignPut(fileKey, contentType, PROFILE_IMAGE_UPLOAD_TTL);
        return new PresignedProfileImageUpload(uploadUrl, fileKey);
    }

    @Transactional
    public Student confirmProfileImageUpload(String institutionId, String studentId, String fileKey) {
        Matcher matcher = PROFILE_IMAGE_KEY_PATTERN.matcher(fileKey);
        if (!matcher.matches()
                || !matcher.group(1).equals(institutionId)
                || !matcher.group(2).equals(studentId)) {
            throw new IllegalArgumentException("fileKey does not match expected scheme for this student: " + fileKey);
        }
        Student student = studentQueryUseCase.findById(studentId);
        if (student == null || !institutionId.equals(student.getTenantId())) {
            throw new IllegalArgumentException("student " + studentId + " not in institution " + institutionId);
        }
        if (!assetUrlService.objectExists(fileKey)) {
            throw new IllegalStateException("uploaded file not found at key " + fileKey);
        }
        String actualContentType = assetUrlService.getObjectContentType(fileKey);
        long sizeBytes = assetUrlService.getObjectSizeBytes(fileKey);
        byte[] bytes = assetUrlService.getObjectBytes(fileKey);
        try {
            profileImageValidator.validate(bytes, actualContentType, sizeBytes);
        } catch (ProfileImageValidator.ProfileImageRejectedException e) {
            assetUrlService.deleteObject(fileKey);
            throw e;
        }
        String previousKey = student.getProfileImageKey();
        if (previousKey != null && !previousKey.equals(fileKey)) {
            assetUrlService.deleteObject(previousKey);
        }
        studentRepository.updateProfileImageKeyById(studentId, fileKey);
        return studentQueryUseCase.findById(studentId);
    }

    public record PresignedProfileImageUpload(String uploadUrl, String fileKey) {
    }
}
