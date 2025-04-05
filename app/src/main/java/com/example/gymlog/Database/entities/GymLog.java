/**
 * Title: Gymlog.java
 * @author Noah deFer
 * Date: 4/5/2025
 * Description: A POGO that stores data entered by the user.
 */
package com.example.gymlog.Database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "gymLog")
public class GymLog {

    // Fields
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String exercise;
    private double weight;
    private int reps;
    private LocalDate date;

    // Constructor


    // Methods

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
