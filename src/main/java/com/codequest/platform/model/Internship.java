package com.codequest.platform.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "internships")
public class Internship {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "posted_by")
    private Student postedBy;
    @Column(nullable = false) private String company;
    private String location;
    @Column(nullable = false) private String title;
    @Column(length = 2000) private String description;
    private String requiredSkill;
    private Integer durationMonths;
    private Double stipend;
    private LocalDate lastDate;
    @Column(nullable = false) private boolean open = true;

    public Long getId() { return id; }
    public Student getPostedBy() { return postedBy; }
    public void setPostedBy(Student postedBy) { this.postedBy = postedBy; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRequiredSkill() { return requiredSkill; }
    public void setRequiredSkill(String requiredSkill) { this.requiredSkill = requiredSkill; }
    public Integer getDurationMonths() { return durationMonths; }
    public void setDurationMonths(Integer durationMonths) { this.durationMonths = durationMonths; }
    public Double getStipend() { return stipend; }
    public void setStipend(Double stipend) { this.stipend = stipend; }
    public LocalDate getLastDate() { return lastDate; }
    public void setLastDate(LocalDate lastDate) { this.lastDate = lastDate; }
    public boolean isOpen() { return open; }
    public void setOpen(boolean open) { this.open = open; }
}
