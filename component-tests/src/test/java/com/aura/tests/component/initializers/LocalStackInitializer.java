package com.aura.tests.component.initializers;

import com.aura.tests.component.containers.localstack.LocalStackTestContainer;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
public class LocalStackInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        var container = LocalStackTestContainer.getSingleton();

        // DefaultCredentialsProvider reads these system properties (highest priority in the chain).
        // Setting them at initializer time means the app picks up LocalStack creds during startup.
        System.setProperty("aws.accessKeyId", LocalStackTestContainer.ACCESS_KEY);
        System.setProperty("aws.secretAccessKey", LocalStackTestContainer.SECRET_KEY);

        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "aws.region=" + container.getRegion(),
                "aws.s3.bucket-name=" + LocalStackTestContainer.BUCKET_NAME,
                "aws.s3.endpoint-override=" + container.getEndpoint().toString()
        );

        createBucket();
        log.info("LocalStack initialized at {}", container.getEndpoint());
    }

    private void createBucket() {
        try (S3Client s3 = S3Client.builder()
                .endpointOverride(LocalStackTestContainer.getSingleton().getEndpoint())
                .region(Region.of(LocalStackTestContainer.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(LocalStackTestContainer.ACCESS_KEY, LocalStackTestContainer.SECRET_KEY)))
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                .build()) {
            s3.createBucket(CreateBucketRequest.builder().bucket(LocalStackTestContainer.BUCKET_NAME).build());
        } catch (S3Exception e) {
            if (e.awsErrorDetails() != null && "BucketAlreadyOwnedByYou".equals(e.awsErrorDetails().errorCode())) {
                return;
            }
            throw e;
        }
    }
}
