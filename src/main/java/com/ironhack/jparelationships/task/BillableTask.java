package com.ironhack.jparelationships.task;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("BILLABLE")
public class BillableTask extends Task {

    @Column(precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    protected BillableTask() {
        // required by JPA
    }

    public BillableTask(String title, LocalDate dueDate, boolean completed, BigDecimal hourlyRate) {
        super(title, dueDate, completed);
        this.hourlyRate = hourlyRate;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public String toString() {
        String base = super.toString();
        return base.substring(0, base.length() - 1) + ", hourlyRate=" + hourlyRate + "}";
    }
}
