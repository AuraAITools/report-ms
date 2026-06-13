package com.aura.tests.component.stepdefs;

import com.aura.tests.component.contexts.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.FieldAccessException;
import org.springframework.web.client.HttpServerErrorException;

import static com.aura.tests.component.utils.FileUtils.findGraphqlQueryFromFiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class OutletRoomManagementStepDefs {

    private final TestContext testContext;

    private List<Map<String, Object>> lastOutletRoomList;
    private Map<String, Object> lastOutletRoomResponse;
    private Boolean lastDeleteOutletRoomResult;

    @When("user lists outlet rooms in the outlet")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void userListsOutletRoomsInOutlet() {
        String query = findGraphqlQueryFromFiles("get-all-outlet-rooms-in-outlet.graphql");
        List<Map> raw = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "outletId", testContext.getOutletId()))
                .retrieveSync("getAllOutletRoomsInOutlet")
                .toEntityList(Map.class);
        lastOutletRoomList = raw.stream().map(m -> (Map<String, Object>) m).toList();
        assertThat(lastOutletRoomList).isNotNull();
    }

    @When("user lists outlet rooms in the institution")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void userListsOutletRoomsInInstitution() {
        String query = findGraphqlQueryFromFiles("get-all-outlet-rooms-in-institution.graphql");
        List<Map> raw = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("institutionId", testContext.getInstitutionId()))
                .retrieveSync("getAllOutletRoomsInInstitution")
                .toEntityList(Map.class);
        lastOutletRoomList = raw.stream().map(m -> (Map<String, Object>) m).toList();
        assertThat(lastOutletRoomList).isNotNull();
    }

    @When("user retrieves outlet rooms by ids of {string}")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void userRetrievesOutletRoomsByIds(String commaSeparatedNames) {
        List<String> ids = Arrays.stream(commaSeparatedNames.split(","))
                .map(String::trim)
                .map(testContext::getOutletRoomIdByName)
                .toList();
        assertThat(ids).as("looked-up outlet room ids").doesNotContainNull();
        String query = findGraphqlQueryFromFiles("get-all-outlet-rooms-by-ids.graphql");
        List<Map> raw = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of(
                        "institutionId", testContext.getInstitutionId(),
                        "ids", ids))
                .retrieveSync("getAllOutletRoomsByIds")
                .toEntityList(Map.class);
        lastOutletRoomList = raw.stream().map(m -> (Map<String, Object>) m).toList();
        assertThat(lastOutletRoomList).isNotNull();
    }

    @When("user updates outlet room {string} with")
    @SuppressWarnings("unchecked")
    public void userUpdatesOutletRoom(String roomName, DataTable dataTable) {
        String id = testContext.getOutletRoomIdByName(roomName);
        assertThat(id).as("outlet room id for '%s'", roomName).isNotNull();
        Map<String, String> row = dataTable.asMaps().get(0);

        Map<String, Object> input = new HashMap<>();
        input.put("id", id);
        input.put("institutionId", testContext.getInstitutionId());
        input.put("name", row.get("name"));
        input.put("size", Integer.parseInt(row.get("size")));
        input.put("type", row.get("type"));
        input.put("description", row.get("description"));

        String query = findGraphqlQueryFromFiles("update-outlet-room.graphql");
        lastOutletRoomResponse = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("updateOutletRoom")
                .toEntity(Map.class);
        assertThat(lastOutletRoomResponse).isNotNull();
    }

    @When("user deletes outlet room {string}")
    public void userDeletesOutletRoom(String roomName) {
        String id = testContext.getOutletRoomIdByName(roomName);
        assertThat(id).as("outlet room id for '%s'", roomName).isNotNull();
        Map<String, Object> input = Map.of(
                "id", id,
                "institutionId", testContext.getInstitutionId());

        String query = findGraphqlQueryFromFiles("delete-outlet-room-by-id.graphql");
        lastDeleteOutletRoomResult = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("input", input))
                .retrieveSync("deleteOutletRoomById")
                .toEntity(Boolean.class);
    }

    @When("user tries to update an outlet room with a random id")
    public void userTriesToUpdateOutletRoomWithRandomId() {
        Map<String, Object> input = new HashMap<>();
        input.put("id", UUID.randomUUID().toString());
        input.put("institutionId", testContext.getInstitutionId());
        input.put("name", "Ghost Room");
        input.put("size", 10);
        input.put("type", "PHYSICAL");
        input.put("description", "ignored");

        String query = findGraphqlQueryFromFiles("update-outlet-room.graphql");
        try {
            testContext.getGraphQLClient()
                    .mutate().build()
                    .document(query)
                    .variables(Map.of("input", input))
                    .retrieveSync("updateOutletRoom")
                    .toEntity(Map.class);
            testContext.setRequestFailed(false);
        } catch (FieldAccessException e) {
            handleFieldAccessException(e);
        } catch (HttpServerErrorException e) {
            testContext.setRequestFailed(true);
            testContext.setLastHttpStatus(e.getStatusCode().value());
            testContext.setLastErrorMessage(e.getMessage());
        }
    }

    @Then("the outlet room list should contain the room {string}")
    public void outletRoomListContains(String name) {
        assertThat(lastOutletRoomList).isNotNull();
        assertThat(lastOutletRoomList).extracting(m -> m.get("name")).contains(name);
    }

    @Then("the outlet room list should have {int} entries")
    public void outletRoomListSize(int expected) {
        assertThat(lastOutletRoomList).isNotNull().hasSize(expected);
    }

    @Then("the outlet room should be updated successfully with name {string} and size {int}")
    public void outletRoomUpdated(String expectedName, int expectedSize) {
        assertThat(lastOutletRoomResponse).isNotNull();
        assertThat(lastOutletRoomResponse.get("name")).isEqualTo(expectedName);
        assertThat(((Number) lastOutletRoomResponse.get("size")).intValue()).isEqualTo(expectedSize);
    }

    @Then("the outlet room delete should succeed")
    public void outletRoomDeleteSucceeded() {
        assertThat(lastDeleteOutletRoomResult).isTrue();
    }

    @Then("listing outlet rooms in the institution should return {int} entries")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void listingOutletRoomsInInstitutionShouldReturn(int expected) {
        String query = findGraphqlQueryFromFiles("get-all-outlet-rooms-in-institution.graphql");
        List<Map> raw = testContext.getGraphQLClient()
                .mutate().build()
                .document(query)
                .variables(Map.of("institutionId", testContext.getInstitutionId()))
                .retrieveSync("getAllOutletRoomsInInstitution")
                .toEntityList(Map.class);
        List<Map<String, Object>> rooms = new ArrayList<>();
        for (Map m : raw) {
            rooms.add((Map<String, Object>) m);
        }
        assertThat(rooms).hasSize(expected);
    }

    private void handleFieldAccessException(FieldAccessException e) {
        testContext.setRequestFailed(true);
        testContext.setLastErrorMessage(e.getMessage());
        var errors = e.getResponse().getErrors();
        if (!errors.isEmpty()) {
            var ext = errors.getFirst().getExtensions();
            if (ext != null && ext.containsKey("error_code")) {
                testContext.setLastErrorMessage(ext.get("error_code").toString());
            }
        }
    }
}
