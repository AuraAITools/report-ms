package com.aura.tests.component.contexts;

import com.aura.app.codegen.types.LessonResponse;
import com.aura.app.codegen.types.SubjectResponse;
import com.aura.app.codegen.types.SubjectResponseWithTopics;
import java.util.ArrayList;
import java.util.HashMap;
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
    private String lessonGroupId;
    private String lessonGroupPlanId;
    private String accountId;
    private String educatorId;
    private String studentId;
    private String materialId;
    private String materialUploadUrl;
    private String materialFileKey;
    private String institutionCreationRequestId;
    private String lessonQuizId;
    private Map<String, Object> lastLessonQuizResponseMap;

    // Last responses for @Then assertion steps
    private SubjectResponse lastSubjectResponse;
    private SubjectResponseWithTopics lastSubjectTopicResponse;
    private LessonResponse lastLessonResponse;
    private Map<String, Object> lastLessonGroupPlanResponseMap;
    private Map<String, Object> lastLessonGroupResponseMap;
    private Map<String, Object> lastResponseData;
    private boolean lastQuerySucceeded;

    // Error tracking for unhappy path @Then steps
    private boolean requestFailed;
    private String lastErrorMessage;
    private int lastHttpStatus;
    private Throwable lastException;

    // Name-keyed lookup maps for conflict test scenarios
    private final Map<String, String> lessonIdsByName = new HashMap<>();
    private final Map<String, String> outletRoomIdsByName = new HashMap<>();
    private final Map<String, String> courseIdsByName = new HashMap<>();
    private final Map<String, String> studentIdsByName = new HashMap<>();
    private final Map<String, String> educatorIdsByName = new HashMap<>();

    public TestContext(HttpSyncGraphQlClient graphQLClient) {
        this.graphQLClient = graphQLClient;
    }

    public void recordLesson(String name, String id) {
        lessonIdsByName.put(name, id);
    }

    public String getLessonIdByName(String name) {
        return lessonIdsByName.get(name);
    }

    public void recordOutletRoom(String name, String id) {
        outletRoomIdsByName.put(name, id);
    }

    public String getOutletRoomIdByName(String name) {
        return outletRoomIdsByName.get(name);
    }

    public void recordCourse(String name, String id) {
        courseIdsByName.put(name, id);
    }

    public String getCourseIdByName(String name) {
        return courseIdsByName.get(name);
    }

    public void recordStudent(String name, String id) {
        studentIdsByName.put(name, id);
    }

    public String getStudentIdByName(String name) {
        return studentIdsByName.get(name);
    }

    public void recordEducator(String name, String id) {
        educatorIdsByName.put(name, id);
    }

    public String getEducatorIdByName(String name) {
        return educatorIdsByName.get(name);
    }
}
