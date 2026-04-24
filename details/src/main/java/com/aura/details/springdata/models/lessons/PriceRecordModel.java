package com.aura.details.springdata.models.lessons;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.courses.CourseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Audited
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "PriceRecords")
public class PriceRecordModel extends TenantBaseModel {

    @OneToOne(mappedBy = "priceRecord")
    @ToString.Exclude
    private CourseModel course;
    @Column(nullable = false)
    private Double price;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FREQUENCY frequency;

    public enum FREQUENCY {
        MONTHLY,
        WEEKLY,
        PER_LESSON,
        TOTAL
    }


}
