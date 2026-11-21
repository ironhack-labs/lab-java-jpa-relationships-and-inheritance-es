package com.ironhack.jparelationships.task;

import org.springframework.data.jpa.repository.JpaRepository;

/** Polymorphic: returns billable and internal tasks alike. */
public interface TaskRepository extends JpaRepository<Task, Long> {
}
