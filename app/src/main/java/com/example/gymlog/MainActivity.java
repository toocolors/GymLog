/**
 * Title: GymLog MainActivity
 *
 * @author Noah deFer
 * Date: 4/5/2025
 * Description: The MainActivity for the GymLog application.
 */

package com.example.gymlog;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymlog.database.GymLogRepository;
import com.example.gymlog.database.entities.GymLog;
import com.example.gymlog.databinding.ActivityMainBinding;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Fields

    private ActivityMainBinding binding;

    private GymLogRepository repository;

    public static final String TAG = "NSD_GYMLOG";
    String mExercise = "";
    double mWeight = 0.0;
    int mReps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get repository instance
        repository = GymLogRepository.getRepository(getApplication());

        // Add scrolling to logDisplayTextView
        binding.logDisplayTextView.setMovementMethod(new ScrollingMovementMethod());

        // Wire button
        binding.logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformationFromDisplay();
                insertGymLogRecord();
                updateDisplay();
            }
        });
    }

    // Methods

    /**
     * Reads values from exercise, weight, and reps edit text fields.
     * Stores those values in mExercise, mWeight, and mReps respectively.
     */
    private void getInformationFromDisplay() {
        // Get mExercise
        mExercise = binding.exerciseInputEditText.getText().toString();

        // Get mWeight
        try {
            mWeight = Double.parseDouble(binding.weightInputEditText.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG, "Error reading value from weightEditText");
        }

        // Get mReps
        try {
            mReps = Integer.parseInt(binding.repInputEditText.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG, "Error reading value from repEditText");
        }

    }

    /**
     * Inserts a new GymLog into the repository.
     */
    private void insertGymLogRecord() {
        GymLog log = new GymLog(mExercise, mWeight, mReps);
        repository.insertGymLog(log);
    }

    /**
     * Constructs String using mExercise, mWeight, mReps, and current text of logDisplayTextView.
     * Sets text of logDisplayTextView to constructed String.
     */
    private void updateDisplay() {
        String currentInfo = binding.logDisplayTextView.getText().toString();
        Log.d(TAG, "current info: " + currentInfo);
        String newDisplay = String.format(Locale.US,
                "Exercise:%s%nWeight:%.2f%nReps:%d%n=-=-=-=-=%n%s",
                mExercise, mWeight, mReps, currentInfo);
        binding.logDisplayTextView.setText(newDisplay);
    }

}