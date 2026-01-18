package com.aura.tests.component.configs;

import com.aura.app.codegen.types.CreateInstitutionInput;
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
}
