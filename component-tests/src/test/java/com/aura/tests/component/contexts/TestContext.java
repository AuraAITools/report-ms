package com.aura.tests.component.contexts;

import com.aura.app.codegen.types.LessonResponse;
import com.aura.app.codegen.types.SubjectResponse;
import com.aura.app.codegen.types.SubjectResponseWithTopics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.graphql.client.HttpSyncGraphQlClient;

@Data
public class TestContext {

    private HttpSyncGraphQlClient graphQLClient;

    // IDs for chaining scenarios
    private String institutionId;
    private String outletId;
    private String subjectId;
    private String topicId;
    private List<String> topicIds = new ArrayList<>();
    private String courseId;
    private String lessonId;
    private String lessonPlanId;
    private String accountId;
    private String educatorId;
    private String studentId;
    private String materialId;
    private String materialUploadUrl;
    private String materialFileKey;

    // Last responses for @Then assertion steps
    private SubjectResponse lastSubjectResponse;
    private SubjectResponseWithTopics lastSubjectTopicResponse;
    private LessonResponse lastLessonResponse;
    private Map<String, Object> lastLessonPlanResponseMap;
    private boolean lastQuerySucceeded;

    // Error tracking for unhappy path @Then steps
    private boolean requestFailed;
    private String lastErrorMessage;
    private int lastHttpStatus;

    public TestContext(HttpSyncGraphQlClient graphQLClient) {
        this.graphQLClient = graphQLClient;
    }
}
