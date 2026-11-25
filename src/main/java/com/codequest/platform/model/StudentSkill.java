package com.codequest.platform.model;

import jakarta.persistence.*;

@Entity
@Table(name = "student_skills", uniqueConstraints =
       @UniqueConstraint(columnNames = {"student_id", "skill_id"}))
public class StudentSkill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne(optional = false) @JoinColumn(name = "skill_id")
    private Skill skill;
    @Column(nullable = false) private String proficiencyLevel;

    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }
    public String getProficiencyLevel() { return proficiencyLevel; }
    public void setProficiencyLevel(String proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }
}
