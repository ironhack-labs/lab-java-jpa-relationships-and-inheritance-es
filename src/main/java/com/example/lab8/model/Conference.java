package com.example.lab8.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Conference extends Event {

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Speaker> speakers;
}
