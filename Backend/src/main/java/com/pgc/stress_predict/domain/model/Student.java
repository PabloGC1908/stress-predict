package com.pgc.stress_predict.domain.models.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "department")
    private String department;

    @Column(name = "attendance_percentage")
    private Float attendancePercentage;

    @Column(name = "midterm_score")
    private Float midtermScore;

    @Column(name = "final_score")
    private Float finalScore;

    @Column(name = "assignments_avg")
    private Float assignmentsAvg;

    @Column(name = "quizzes_avg")
    private Float quizzesAvg;

    @Column(name = "participation_score")
    private Float participationScore;

    @Column(name = "projects_score")
    private Float projectsScore;

    @Column(name = "total_score")
    private Float totalScore;

    @Column(name = "grade")
    private String grade;

    @Column(name = "study_hours_per_week")
    private Float studyHoursPerWeek;

    @Column(name = "extracurricular_activities")
    private Boolean extracurricularActivities;

    @Column(name = "internet_access_at_home")
    private Boolean internetAccessAtHome;

    @Column(name = "parent_education_level")
    private String parentEducationLevel;

    @Column(name = "family_income_level")
    private String familyIncomeLevel;

    @Column(name = "stress_level")
    private Integer stressLevel;

    @Column(name = "sleep_hours_per_night")
    private Float sleepHoursPerNight;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}