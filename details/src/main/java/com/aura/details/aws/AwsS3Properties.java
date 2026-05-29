package com.aura.details.aws;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AwsS3Properties {
    private String bucketName;
    /**
     * Optional. Set in tests to point at a LocalStack endpoint; leave blank in production.
     */
    private String endpointOverride;
}
