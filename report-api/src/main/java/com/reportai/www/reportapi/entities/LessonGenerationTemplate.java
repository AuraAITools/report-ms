package com.reportai.www.reportapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * A lesson generation template contains information to generate a sequence of lessons
 * based on a day (i.e.) monday, and the week number
 * i.e. generate a monday lesson on week 2 on the course that have 4 lessons biweekly
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LessonGenerationTemplates")
public class LessonGenerationTemplate extends BaseEntity {

    private DayOfWeek dayOfWeek; // which day the lesson falls on

    private Integer weekNumber; // which week the lesson falls on

    private LocalTime startTime;

    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    private String tenantId;
}
