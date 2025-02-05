package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.accounts.requests.CreateClientRequestDTO;
import com.reportai.www.reportapi.entities.Client;
import java.util.UUID;

public class ClientMappers {
    private ClientMappers() {
    }

    public static Client convert(CreateClientRequestDTO createClientRequestDTO, UUID id) {
        return Client
                .builder()
                .relationship(createClientRequestDTO.getRelationship())
                .tenantId(id.toString())
                .build();
    }
}
