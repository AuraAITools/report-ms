package com.reportai.www.reportapi.entities.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuditableEntityWithGeneratedId extends BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @CreatedBy
    private String createdBy;

    @Column(insertable = false)
    @LastModifiedDate
    private Instant updatedAt;

    @LastModifiedBy
    private String updatedBy;


}
