package com.reportai.www.reportapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public class BaseEntitySupport {


        @jakarta.persistence.Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID Id;

        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "modified_at")
        private LocalDateTime modifiedAt;

        @PrePersist
        protected void onCreate() {
            LocalDateTime now = LocalDateTime.now();
            this.createdAt = now;
            this.modifiedAt = now;
        }

        @PreUpdate
        protected void onUpdate() {
            this.modifiedAt = LocalDateTime.now();
        }

}
