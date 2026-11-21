package com.ironhack.jparelationships.event;

import jakarta.persistence.Entity;

import java.time.LocalDate;

/** An event with no extra fields; its table only holds the id shared with {@code event}. */
@Entity
public class Exhibition extends Event {

    protected Exhibition() {
        // required by JPA
    }

    public Exhibition(String title, LocalDate date, Integer duration, String location) {
        super(title, date, duration, location);
    }
}
