package com.ironhack.jparelationships.contact;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String company;

    private String title;

    @Embedded
    private Name name;

    protected Contact() {
        // required by JPA
    }

    public Contact(Name name, String title, String company) {
        this.name = name;
        this.title = title;
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Contact{id=" + id + ", name='" + name + "', title='" + title + "', company='" + company + "'}";
    }
}
