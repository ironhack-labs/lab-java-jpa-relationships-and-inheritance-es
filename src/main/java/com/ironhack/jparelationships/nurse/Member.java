package com.ironhack.jparelationships.nurse;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    private LocalDate renewalDate;

    /**
     * Owning side of Division 1..N Member: the foreign key lives in the
     * {@code member} table, so no join table is needed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id")
    private Division division;

    protected Member() {
        // required by JPA
    }

    public Member(String name, MemberStatus status, LocalDate renewalDate) {
        this.name = name;
        this.status = status;
        this.renewalDate = renewalDate;
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

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public LocalDate getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(LocalDate renewalDate) {
        this.renewalDate = renewalDate;
    }

    public Division getDivision() {
        return division;
    }

    void setDivision(Division division) {
        this.division = division;
    }

    @Override
    public String toString() {
        return "Member{id=" + id + ", name='" + name + "', status=" + status + ", renewalDate=" + renewalDate + "}";
    }
}
