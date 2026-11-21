package com.ironhack.jparelationships.event;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for {@link Conference} and {@link Exhibition}.
 * <p>
 * JOINED inheritance: the shared columns live once in {@code event}, and each
 * subclass gets its own table keyed by the same id. Conference needs its own
 * relationship (speakers), so a JOINED schema keeps that join table pointing
 * at {@code conference} instead of at every event. Being abstract, there is no
 * such thing as a plain Event row.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    // "date" is a keyword in several SQL dialects, so the column gets a safer name.
    @Column(name = "event_date")
    private LocalDate date;

    /** Duration in days. */
    private Integer duration;

    private String location;

    /**
     * A guest's RSVP status belongs to one specific invitation, so each Guest
     * row is tied to a single event: one-to-many with the FK in {@code guest}.
     */
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Guest> guests = new ArrayList<>();

    protected Event() {
        // required by JPA
    }

    protected Event(String title, LocalDate date, Integer duration, String location) {
        this.title = title;
        this.date = date;
        this.duration = duration;
        this.location = location;
    }

    /** Keeps both sides of the relationship in sync. */
    public void addGuest(Guest guest) {
        guests.add(guest);
        guest.setEvent(this);
    }

    public void removeGuest(Guest guest) {
        guests.remove(guest);
        guest.setEvent(null);
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", title='" + title + "', date=" + date
                + ", duration=" + duration + ", location='" + location + "'}";
    }
}
