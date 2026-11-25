package com.codequest.platform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "internship_applications",
       uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "internship_id"}))
public class InternshipApplication {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne(optional = false) @JoinColumn(name = "internship_id")
    private Internship internship;
    private LocalDateTime appliedAt = LocalDateTime.now();
    @Column(nullable = false) private String status = "Applied";

    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Internship getInternship() { return internship; }
    public void setInternship(Internship internship) { this.internship = internship; }
    public LocalDateTime getAppliedAt() { return appliedAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
