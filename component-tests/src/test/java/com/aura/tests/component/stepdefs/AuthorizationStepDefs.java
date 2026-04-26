package com.aura.tests.component.stepdefs;

import com.authzed.api.v1.ReadSchemaRequest;
import com.authzed.api.v1.ReadSchemaResponse;
import com.authzed.api.v1.SchemaServiceGrpc;
import com.authzed.grpcutil.BearerToken;
import com.aura.tests.component.containers.spicedb.SpiceDbTestContainer;
import com.aura.tests.component.contexts.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationStepDefs {

    private final TestContext testContext;

    @Given("SpiceDB is set up with the authorization schema")
    public void spiceDbIsSetUp() {
        var container = SpiceDbTestContainer.getSingleton();
        assertThat(container.isRunning()).isTrue();
    }

    @Then("the schema should define the following resource types:")
    public void schemaShouldDefineResourceTypes(DataTable dataTable) {
        String target = SpiceDbTestContainer.getTarget();
        String key = SpiceDbTestContainer.getPresharedKey();
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
        try {
            SchemaServiceGrpc.SchemaServiceBlockingStub schemaService =
                    SchemaServiceGrpc.newBlockingStub(channel)
                            .withCallCredentials(new BearerToken(key));
            ReadSchemaResponse response = schemaService.readSchema(ReadSchemaRequest.getDefaultInstance());
            String schema = response.getSchemaText();

            for (Map<String, String> row : dataTable.asMaps()) {
                String type = row.get("type");
                assertThat(schema).as("Schema should define '%s'", type)
                        .contains("definition " + type);
            }
        } finally {
            channel.shutdownNow();
        }
    }
}
