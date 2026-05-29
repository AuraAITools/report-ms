package com.aura.tests.component.configs;

import com.aura.app.codegen.types.CreateAccountInput;
import com.aura.app.codegen.types.CreateInstitutionInput;
import com.aura.app.codegen.types.CreateOutletInput;
import com.aura.app.codegen.types.RELATIONSHIP;
import io.cucumber.java.DataTableType;
import java.util.Map;

public class DataTableTypeConverterConfig {

    @DataTableType
    public CreateInstitutionInput institutionInput(Map<String, String> entry) {
        return CreateInstitutionInput.newBuilder()
                .name(entry.get("name"))
                .email(entry.get("email"))
                .contactNumber(entry.get("contactNumber"))
                .address(entry.get("address"))
                .uen(entry.get("uen"))
                .build();
    }

    @DataTableType
    public CreateOutletInput outletInput(Map<String, String> entry) {
        return CreateOutletInput.newBuilder()
                .name(entry.get("name"))
                .address(entry.get("address"))
                .postalCode(Integer.parseInt(entry.get("postalCode")))
                .contactNumber(Integer.parseInt(entry.get("contactNumber")))
                .description(entry.get("description"))
                .email(entry.get("email"))
                .build();
    }

    @DataTableType
    public CreateAccountInput accountInput(Map<String, String> entry) {
        return CreateAccountInput.newBuilder()
                .email(entry.get("email"))
                .firstName(entry.get("firstName"))
                .lastName(entry.get("lastName"))
                .contact(Integer.parseInt(entry.get("contact")))
                .build();
    }

}
