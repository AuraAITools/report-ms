package com.aura.details.services;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Slf4j
@RequiredArgsConstructor
public class AssetUrlService {

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    private final String bucketName;

    public String presignGet(String objectKey, Duration ttl) {
        GetObjectRequest getObject = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
        GetObjectPresignRequest presign = GetObjectPresignRequest.builder()
                .signatureDuration(ttl)
                .getObjectRequest(getObject)
                .build();
        String url = s3Presigner.presignGetObject(presign).url().toString();
        log.debug("Presigned GET url for key={} ttl={}", objectKey, ttl);
        return url;
    }

    public String presignPut(String objectKey, String contentType, Duration ttl) {
        PutObjectRequest putObject = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(contentType)
                .build();
        PutObjectPresignRequest presign = PutObjectPresignRequest.builder()
                .signatureDuration(ttl)
                .putObjectRequest(putObject)
                .build();
        String url = s3Presigner.presignPutObject(presign).url().toString();
        log.debug("Presigned PUT url for key={} contentType={} ttl={}", objectKey, contentType, ttl);
        return url;
    }

    /**
     * Returns true if the object exists at the given key in the configured bucket.
     * Used to verify a client-confirmed upload actually completed before persisting
     * the key alongside a domain entity.
     */
    public boolean objectExists(String objectKey) {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    public byte[] getObjectBytes(String objectKey) {
        return s3Client.getObjectAsBytes(GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build())
                .asByteArray();
    }

    public long getObjectSizeBytes(String objectKey) {
        return s3Client.headObject(HeadObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build())
                .contentLength();
    }

    public String getObjectContentType(String objectKey) {
        return s3Client.headObject(HeadObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build())
                .contentType();
    }

    public void deleteObject(String objectKey) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build());
        log.debug("Deleted S3 object key={}", objectKey);
    }
}
