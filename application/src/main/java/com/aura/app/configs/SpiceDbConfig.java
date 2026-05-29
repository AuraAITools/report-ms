package com.aura.app.configs;

import com.authzed.api.v1.PermissionsServiceGrpc;
import com.authzed.api.v1.SchemaServiceGrpc;
import com.authzed.grpcutil.BearerToken;
import com.aura.app.bootstrap.SystemRootRelationshipBootstrap;
import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.details.spicedb.SpiceDbCredentials;
import com.aura.details.spicedb.SpiceDbRelationshipService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpiceDbConfig {

    @Bean
    @ConfigurationProperties(prefix = "spicedb")
    SpiceDbCredentials spiceDbCredentials() {
        return new SpiceDbCredentials();
    }

    @Bean
    ManagedChannel spiceDbChannel(SpiceDbCredentials credentials) {
        var builder = ManagedChannelBuilder.forTarget(credentials.getTarget());
        if (credentials.isPlaintext()) {
            builder.usePlaintext();
        } else {
            builder.useTransportSecurity();
        }
        return builder.build();
    }

    @Bean
    PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsServiceStub(
            ManagedChannel spiceDbChannel, SpiceDbCredentials credentials) {
        return PermissionsServiceGrpc.newBlockingStub(spiceDbChannel)
                .withCallCredentials(new BearerToken(credentials.getPresharedKey()));
    }

    @Bean
    SchemaServiceGrpc.SchemaServiceBlockingStub schemaServiceStub(
            ManagedChannel spiceDbChannel, SpiceDbCredentials credentials) {
        return SchemaServiceGrpc.newBlockingStub(spiceDbChannel)
                .withCallCredentials(new BearerToken(credentials.getPresharedKey()));
    }

    @Bean
    SpiceDbRelationshipService spiceDbRelationshipService(
            PermissionsServiceGrpc.PermissionsServiceBlockingStub stub) {
        return new SpiceDbRelationshipService(stub);
    }

    @Bean
    SystemRootRelationshipBootstrap systemRootRelationshipBootstrap(
            RelationshipAuthorizationService relationshipService) {
        return new SystemRootRelationshipBootstrap(relationshipService);
    }
}
