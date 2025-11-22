package com.academiadev.domain.entities;

public class Enrollment {
    private Student student;
    private Course course;
    private double progress;
    private boolean active;
    
    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.progress = 0.0;
        this.active = true;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public Course getCourse() {
        return course;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public double getProgress() {
        return progress;
    }
    
    public void setProgress(double progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progresso deve estar entre 0 e 100");
        }
        this.progress = progress;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void cancel() {
        this.active = false;
    }
    
    @Override
    public String toString() {
        return "Enrollment{" +
                "student=" + student.getEmail() +
                ", course=" + course.getTitle() +
                ", progress=" + progress + "%" +
                ", active=" + active +
                '}';
    }
}

