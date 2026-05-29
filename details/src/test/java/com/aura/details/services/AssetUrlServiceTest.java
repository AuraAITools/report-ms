package com.aura.details.services;

import java.net.URI;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.awscore.exception.AwsErrorDetails;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Asset URL service")
@ExtendWith(MockitoExtension.class)
class AssetUrlServiceTest {

    private static final String BUCKET = "aura-ap-southeast-dev";

    @Mock
    private S3Presigner s3Presigner;
    @Mock
    private S3Client s3Client;

    private AssetUrlService assetUrlService;

    @BeforeEach
    void setUp() {
        assetUrlService = new AssetUrlService(s3Presigner, s3Client, BUCKET);
    }

    @DisplayName("Should sign GET URLs with the configured bucket, requested key, and TTL")
    @Test
    void presignGet_signsWithBucketKeyAndTtl() throws Exception {
        String key = "institutions/inst-1/logo.png";
        Duration ttl = Duration.ofMinutes(15);
        PresignedGetObjectRequest signed = mockedSignedGet("https://s3.example/foo");
        ArgumentCaptor<GetObjectPresignRequest> captor = ArgumentCaptor.forClass(GetObjectPresignRequest.class);
        when(s3Presigner.presignGetObject(captor.capture())).thenReturn(signed);

        String url = assetUrlService.presignGet(key, ttl);

        assertThat(url).isEqualTo("https://s3.example/foo");
        GetObjectPresignRequest captured = captor.getValue();
        assertThat(captured.signatureDuration()).isEqualTo(ttl);
        GetObjectRequest getRequest = captured.getObjectRequest();
        assertThat(getRequest.bucket()).isEqualTo(BUCKET);
        assertThat(getRequest.key()).isEqualTo(key);
    }

    @DisplayName("Should sign PUT URLs with the configured bucket, key, content-type, and TTL")
    @Test
    void presignPut_signsWithBucketKeyContentTypeAndTtl() throws Exception {
        String key = "institutions/inst-1/accounts/acc-1/profile-abc.png";
        String contentType = "image/png";
        Duration ttl = Duration.ofMinutes(5);
        PresignedPutObjectRequest signed = mockedSignedPut("https://s3.example/upload");
        ArgumentCaptor<PutObjectPresignRequest> captor = ArgumentCaptor.forClass(PutObjectPresignRequest.class);
        when(s3Presigner.presignPutObject(captor.capture())).thenReturn(signed);

        String url = assetUrlService.presignPut(key, contentType, ttl);

        assertThat(url).isEqualTo("https://s3.example/upload");
        PutObjectPresignRequest captured = captor.getValue();
        assertThat(captured.signatureDuration()).isEqualTo(ttl);
        PutObjectRequest putRequest = captured.putObjectRequest();
        assertThat(putRequest.bucket()).isEqualTo(BUCKET);
        assertThat(putRequest.key()).isEqualTo(key);
        assertThat(putRequest.contentType()).isEqualTo(contentType);
    }

    @DisplayName("Should return true when HeadObject succeeds for the key")
    @Test
    void objectExists_returnsTrueOnSuccess() {
        when(s3Client.headObject(HeadObjectRequest.builder()
                .bucket(BUCKET).key("some/key").build()))
                .thenReturn(HeadObjectResponse.builder().build());

        assertThat(assetUrlService.objectExists("some/key")).isTrue();
    }

    @DisplayName("Should return false when HeadObject throws NoSuchKey")
    @Test
    void objectExists_returnsFalseOnNoSuchKey() {
        when(s3Client.headObject(HeadObjectRequest.builder()
                .bucket(BUCKET).key("missing").build()))
                .thenThrow(NoSuchKeyException.builder()
                        .awsErrorDetails(AwsErrorDetails.builder().errorCode("NoSuchKey").build())
                        .build());

        assertThat(assetUrlService.objectExists("missing")).isFalse();
    }

    private PresignedGetObjectRequest mockedSignedGet(String url) throws Exception {
        PresignedGetObjectRequest mock = org.mockito.Mockito.mock(PresignedGetObjectRequest.class);
        when(mock.url()).thenReturn(URI.create(url).toURL());
        return mock;
    }

    private PresignedPutObjectRequest mockedSignedPut(String url) throws Exception {
        PresignedPutObjectRequest mock = org.mockito.Mockito.mock(PresignedPutObjectRequest.class);
        when(mock.url()).thenReturn(URI.create(url).toURL());
        return mock;
    }
}
