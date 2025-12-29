package com.aura.details.springdata.tables.lessons;

import com.aura.details.springdata.tables.base.TenantBaseTable;
import com.aura.details.springdata.tables.courses.CourseTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
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
@Table(name = "PriceRecords")
public class PriceRecordTable extends TenantBaseTable {

    @OneToOne(mappedBy = "priceRecord")
    @ToString.Exclude
    private CourseTable course;
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
