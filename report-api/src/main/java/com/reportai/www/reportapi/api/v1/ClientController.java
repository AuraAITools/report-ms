package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.dtos.responses.SubjectDto;
import com.reportai.www.reportapi.services.ClientService;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("v1")
@RestController
@Slf4j
public class ClientController {
    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/client/{id}/subjects")
    public ResponseEntity<List<SubjectDto>> getSubjectsAndLessonsByStudentId(@PathVariable UUID studentId)
        throws Exception {
        List<SubjectDto> subjectDtos = clientService.getSubjectsAndLessonsByStudentId(studentId);
        return ResponseEntity.ok(subjectDtos);
    }
}
