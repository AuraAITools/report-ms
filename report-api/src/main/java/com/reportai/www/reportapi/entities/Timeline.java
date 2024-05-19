package com.reportai.www.reportapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.isNull;

@Entity
@Table(name = "Timelines")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Timeline {

    public enum TYPE {
        ABSOLUTE,
        ONGOING
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @OneToMany(mappedBy = "timeline")
    private List<Course> courses;

    @ManyToOne
    private Institution institution;

    @JsonProperty("start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @JsonProperty("end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

//    @JsonIgnore
    @JsonProperty(access = Access.READ_ONLY)
    private TYPE type;

    @OneToMany(mappedBy = "timeline")
    private List<TestGroup> testGroups;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @PrePersist
    private void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.modifiedAt = now;

        // set type according to user input
        if (isNull(endDate)) {
            this.type = TYPE.ONGOING;
        } else {
            this.type = TYPE.ABSOLUTE;
        }
    }

    @PreUpdate
    private void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
}

