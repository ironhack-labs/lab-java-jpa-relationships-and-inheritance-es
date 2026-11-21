package com.ironhack.jparelationships.task;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

import java.time.LocalDate;

/**
 * Base class for {@link BillableTask} and {@link InternalTask}.
 * <p>
 * SINGLE_TABLE inheritance: the subclasses differ by one nullable column
 * ({@code hourly_rate}), so one {@code task} table with a {@code task_type}
 * discriminator avoids joins and costs almost nothing in empty columns.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "task_type")
public abstract class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDate dueDate;

    private boolean completed;

    protected Task() {
        // required by JPA
    }

    protected Task(String title, LocalDate dueDate, boolean completed) {
        this.title = title;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", title='" + title + "', dueDate=" + dueDate
                + ", completed=" + completed + "}";
    }
}
