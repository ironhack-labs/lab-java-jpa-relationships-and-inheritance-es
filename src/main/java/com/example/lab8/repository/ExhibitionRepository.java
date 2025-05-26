package com.example.lab8.repository;

import com.example.lab8.model.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitionRepository extends JpaRepository<Exhibition,Long> {
}
