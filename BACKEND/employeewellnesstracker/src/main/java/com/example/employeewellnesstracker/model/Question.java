package com.example.employeewellnesstracker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text; // question content

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    @JsonBackReference
    private Survey survey;
}
