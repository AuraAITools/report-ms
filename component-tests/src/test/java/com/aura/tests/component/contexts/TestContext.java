package com.aura.tests.component.contexts;

import com.aura.app.codegen.types.LessonResponse;
import com.aura.app.codegen.types.SubjectResponse;
import com.aura.app.codegen.types.SubjectResponseWithTopics;
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
    private String courseId;
    private String accountId;

    // Last responses for @Then assertion steps
    private SubjectResponse lastSubjectResponse;
    private SubjectResponseWithTopics lastSubjectTopicResponse;
    private LessonResponse lastLessonResponse;
    private boolean lastQuerySucceeded;

    // Error tracking for unhappy path @Then steps
    private boolean requestFailed;
    private String lastErrorMessage;

    public TestContext(HttpSyncGraphQlClient graphQLClient) {
        this.graphQLClient = graphQLClient;
    }
}
