package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Schools", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_school_name",
                columnNames = {"tenant_id", "name"}
        )
})
public class School extends TenantAwareBaseEntity {
    public enum SchoolCategory {
        PRIMARY("Primary School"),
        SECONDARY("Secondary School"),
        JUNIOR_COLLEGE("Junior College"),
        POLYTECHNIC("Polytechnic"),
        TECHNICAL("Technical Education Institution"),
        INTERNATIONAL("International School"),
        ARTS("Arts Institution"),
        UNIVERSITY("University");

        private final String displayName;

        SchoolCategory(String displayName) {
            this.displayName = displayName;
        }

        /**
         * Get the display name of the school category.
         *
         * @return the display name
         */
        public String getDisplayName() {
            return displayName;
        }
    }

    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SchoolCategory schoolCategory;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
    @ToString.Exclude
    @Builder.Default
    private Set<Student> students = new HashSet<>();
}
