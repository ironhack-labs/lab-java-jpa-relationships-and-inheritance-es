package com.ironhack.jparelationships.nurse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Association {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /** Collections are LAZY by default for @OneToMany; kept that way on purpose. */
    @OneToMany(mappedBy = "association", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Division> divisions = new ArrayList<>();

    protected Association() {
        // required by JPA
    }

    public Association(String name) {
        this.name = name;
    }

    /** Keeps both sides of the relationship in sync. */
    public void addDivision(Division division) {
        divisions.add(division);
        division.setAssociation(this);
    }

    public void removeDivision(Division division) {
        divisions.remove(division);
        division.setAssociation(null);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Division> getDivisions() {
        return divisions;
    }

    @Override
    public String toString() {
        return "Association{id=" + id + ", name='" + name + "'}";
    }
}
