/**
 * Title: Gymlog.java
 * @author Noah deFer
 * Date: 4/5/2025
 * Description: A POGO that stores data entered by the user.
 */
package com.example.gymlog.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.gymlog.database.GymLogDatabase;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(tableName = GymLogDatabase.GYM_LOG_TABLE)
public class GymLog {

    // Fields
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String exercise;
    private double weight;
    private int reps;
    private LocalDateTime date;
    private int userId;

    // Constructor
    public GymLog(String exercise, double weight, int reps, int userId) {
        this.exercise = exercise;
        this.weight = weight;
        this.reps = reps;
        this.userId = userId;
        date = LocalDateTime.now();
    }


    // Methods

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GymLog gymLog = (GymLog) o;
        return id == gymLog.id && Double.compare(weight, gymLog.weight) == 0 && reps == gymLog.reps && userId == gymLog.userId && Objects.equals(exercise, gymLog.exercise) && Objects.equals(date, gymLog.date);
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

    public LocalDateTime getDate() {
        return date;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exercise, weight, reps, date, userId);
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

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public String toString() {
        return exercise + '\n' +
                "weight: " + weight + '\n' +
                "reps: " + reps + '\n' +
                "date: " + date.toString() + '\n' +
                "=-=-=-=-=-=-=-=-=-\n";
    }
}
