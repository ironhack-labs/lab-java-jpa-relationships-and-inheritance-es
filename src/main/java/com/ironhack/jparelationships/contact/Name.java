package com.ironhack.jparelationships.contact;

import jakarta.persistence.Embeddable;

/**
 * Value object embedded in {@link Contact}. It has no table or id of its own:
 * its fields become columns of the {@code contact} table.
 */
@Embeddable
public class Name {

    private String salutation;
    private String firstName;
    private String middleName;
    private String lastName;

    protected Name() {
        // required by JPA
    }

    public Name(String salutation, String firstName, String middleName, String lastName) {
        this.salutation = salutation;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String part : new String[]{salutation, firstName, middleName, lastName}) {
            if (part != null && !part.isBlank()) {
                if (!sb.isEmpty()) {
                    sb.append(' ');
                }
                sb.append(part);
            }
        }
        return sb.toString();
    }
}
