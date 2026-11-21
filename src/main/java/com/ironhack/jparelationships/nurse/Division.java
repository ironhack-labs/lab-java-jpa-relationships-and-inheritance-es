package com.ironhack.jparelationships.nurse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Division {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String district;

    /**
     * Each division has exactly one president, and a member presides over at
     * most one division, so this is a one-to-one stored as
     * {@code division.president_id}. The president is also one of the
     * division's members, so persisting the members list already saves it.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "president_id", unique = true)
    private Member president;

    @OneToMany(mappedBy = "division", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "association_id")
    private Association association;

    protected Division() {
        // required by JPA
    }

    public Division(String name, String district) {
        this.name = name;
        this.district = district;
    }

    /** Keeps both sides of the relationship in sync. */
    public void addMember(Member member) {
        members.add(member);
        member.setDivision(this);
    }

    public void removeMember(Member member) {
        members.remove(member);
        member.setDivision(null);
        if (member.equals(president)) {
            president = null;
        }
    }

    /** The president must belong to this division; adds them if needed. */
    public void setPresident(Member president) {
        if (president != null && !members.contains(president)) {
            addMember(president);
        }
        this.president = president;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Member getPresident() {
        return president;
    }

    public List<Member> getMembers() {
        return members;
    }

    public Association getAssociation() {
        return association;
    }

    void setAssociation(Association association) {
        this.association = association;
    }

    @Override
    public String toString() {
        return "Division{id=" + id + ", name='" + name + "', district='" + district + "'}";
    }
}
