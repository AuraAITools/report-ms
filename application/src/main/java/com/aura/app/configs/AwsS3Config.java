package com.aura.app.configs;

import com.aura.details.aws.AwsS3Properties;
import com.aura.details.services.AssetUrlService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsS3Config {

    @Bean
    @ConfigurationProperties(prefix = "aws.s3")
    AwsS3Properties awsS3Properties() {
        return new AwsS3Properties();
    }

    @Bean(destroyMethod = "close")
    S3Presigner s3Presigner(@Value("${aws.region}") String region, AwsS3Properties properties) {
        S3Presigner.Builder builder = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build());
        if (properties.getEndpointOverride() != null && !properties.getEndpointOverride().isBlank()) {
            builder.endpointOverride(URI.create(properties.getEndpointOverride()));
        }
        return builder.build();
    }

    @Bean(destroyMethod = "close")
    S3Client s3Client(@Value("${aws.region}") String region, AwsS3Properties properties) {
        S3ClientBuilder builder = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build());
        if (properties.getEndpointOverride() != null && !properties.getEndpointOverride().isBlank()) {
            builder.endpointOverride(URI.create(properties.getEndpointOverride()));
        }
        return builder.build();
    }

    @Bean
    AssetUrlService assetUrlService(S3Presigner s3Presigner, S3Client s3Client, AwsS3Properties properties) {
        return new AssetUrlService(s3Presigner, s3Client, properties.getBucketName());
    }
}
