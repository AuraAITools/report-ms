package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TopicRepository extends JpaRepository<Topic, UUID> {
}
