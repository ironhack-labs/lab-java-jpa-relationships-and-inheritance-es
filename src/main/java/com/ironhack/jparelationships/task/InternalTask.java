package com.ironhack.jparelationships.task;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("INTERNAL")
public class InternalTask extends Task {

    protected InternalTask() {
        // required by JPA
    }

    public InternalTask(String title, LocalDate dueDate, boolean completed) {
        super(title, dueDate, completed);
    }
}
