package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {
}
