package com.aura.details.spicedb;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpiceDbCredentials {
    private String target;       // e.g., "localhost:50051"
    private String presharedKey;
    private boolean plaintext;   // true for local dev, false for production
}
