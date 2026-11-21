package com.ironhack.jparelationships.event;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Conference extends Event {

    /**
     * A conference has many speakers and a speaker can present at many
     * conferences, so this is many-to-many through the
     * {@code conference_speaker} join table. No cascade: speakers exist on
     * their own and are saved before being attached.
     */
    @ManyToMany
    @JoinTable(
            name = "conference_speaker",
            joinColumns = @JoinColumn(name = "conference_id"),
            inverseJoinColumns = @JoinColumn(name = "speaker_id")
    )
    private List<Speaker> speakers = new ArrayList<>();

    protected Conference() {
        // required by JPA
    }

    public Conference(String title, LocalDate date, Integer duration, String location) {
        super(title, date, duration, location);
    }

    public void addSpeaker(Speaker speaker) {
        speakers.add(speaker);
    }

    public void removeSpeaker(Speaker speaker) {
        speakers.remove(speaker);
    }

    public List<Speaker> getSpeakers() {
        return speakers;
    }
}
