package com.davidwerfelmann.course_scheduler.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Section extends AbstractEntity {

    @NotNull
    @Min(value = 1, message="Section number must be at least 1.")
    @Max(value=99, message="Section number must be less than 100.")
    private int sectionNumber;

    @NotNull
    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;

    @NotNull
    @ManyToOne
    @JoinColumn(name="instructor_id")
    private Instructor instructor;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private LocalTime startTime;

    private LocalTime stopTime;

    private boolean isOnline = false;

    public Section() {}

    public Section(Course course, int sectionNumber, Instructor instructor, Location location, LocalTime startTime, LocalTime stopTime) {
        this.course = course;
        this.sectionNumber = sectionNumber;
        this.instructor = instructor;
        this.location = location;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.isOnline = false;
    }

    public Section(Course course, int sectionNumber, Instructor instructor, boolean isOnline) {
        this.course = course;
        this.sectionNumber = sectionNumber;
        this.instructor = instructor;
        this.isOnline = isOnline;
    }

    @AssertTrue(message = "Start time must be earlier than stop time.")
    public boolean isValidTimeRange() {
        // Null values are allowed for unscheduled times
        if (startTime == null || stopTime == null) {
            return true; // Skip validation if either time is null
        }
        return stopTime.isAfter(startTime);
    }

    @AssertTrue(message = "Both start and stop times must be set together or left blank.")
    public boolean isTimeConsistencyValid() {
        return (startTime == null && stopTime == null) || (startTime != null && stopTime != null);
    }

    @AssertTrue(message = "Online sections should not have a physical location.")
    public boolean isLocationValid() {
        return !isOnline || location == null;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public String getFormattedStartTime() {
        return startTime != null ? startTime.format(DateTimeFormatter.ofPattern("hh:mm a")) : "Not Scheduled";
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getStopTime() {
        return stopTime;
    }

    public String getFormattedStopTime() {
        return stopTime != null ? stopTime.format(DateTimeFormatter.ofPattern("hh:mm a")) : "Not Scheduled";
    }

    public void setStopTime(LocalTime stopTime) {
        this.stopTime = stopTime;
    }

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    @Override
    public String toString() {
        return course.getName() + " " + sectionNumber;
    }
}
