package com.academiadev.domain.entities;

import com.academiadev.domain.enums.CourseStatus;
import com.academiadev.domain.enums.DifficultyLevel;

public class Course {
    private String title;
    private String description;
    private String instructorName;
    private int durationInHours;
    private DifficultyLevel difficultyLevel;
    private CourseStatus status;
    
    public Course(String title, String description, String instructorName, 
                  int durationInHours, DifficultyLevel difficultyLevel, CourseStatus status) {
        this.title = title;
        this.description = description;
        this.instructorName = instructorName;
        this.durationInHours = durationInHours;
        this.difficultyLevel = difficultyLevel;
        this.status = status;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getInstructorName() {
        return instructorName;
    }
    
    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }
    
    public int getDurationInHours() {
        return durationInHours;
    }
    
    public void setDurationInHours(int durationInHours) {
        this.durationInHours = durationInHours;
    }
    
    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }
    
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
    
    public CourseStatus getStatus() {
        return status;
    }
    
    public void setStatus(CourseStatus status) {
        this.status = status;
    }
    
    public boolean isActive() {
        return status == CourseStatus.ACTIVE;
    }
    
    @Override
    public String toString() {
        return "Course{" +
                "title='" + title + '\'' +
                ", instructorName='" + instructorName + '\'' +
                ", difficultyLevel=" + difficultyLevel +
                ", status=" + status +
                ", durationInHours=" + durationInHours +
                '}';
    }
}

