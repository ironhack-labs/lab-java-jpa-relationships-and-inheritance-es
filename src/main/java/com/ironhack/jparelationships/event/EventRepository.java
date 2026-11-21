package com.ironhack.jparelationships.event;

import org.springframework.data.jpa.repository.JpaRepository;

/** Polymorphic: returns conferences and exhibitions alike. */
public interface EventRepository extends JpaRepository<Event, Long> {
}
