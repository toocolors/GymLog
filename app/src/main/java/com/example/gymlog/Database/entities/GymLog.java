/**
 * Title: Gymlog.java
 * @author Noah deFer
 * Date: 4/5/2025
 * Description: A POGO that stores data entered by the user.
 */
package com.example.gymlog.Database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.gymlog.Database.GymLogDatabase;

import java.time.LocalDate;
import java.util.Objects;

@Entity(tableName = GymLogDatabase.GYM_LOG_TABLE)
public class GymLog {

    // Fields
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String exercise;
    private double weight;
    private int reps;
    private LocalDate date;

    // Constructor
    public GymLog(String exercise, double weight, int reps) {
        this.exercise = exercise;
        this.weight = weight;
        this.reps = reps;
        date = LocalDate.now();
    }


    // Methods

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GymLog gymLog = (GymLog) o;
        return id == gymLog.id && Double.compare(weight, gymLog.weight) == 0 && reps == gymLog.reps && Objects.equals(exercise, gymLog.exercise) && Objects.equals(date, gymLog.date);
    }

    public int getId() {
        return id;
    }

    public String getExercise() {
        return exercise;
    }

    public double getWeight() {
        return weight;
    }

    public int getReps() {
        return reps;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exercise, weight, reps, date);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
