/**
 * Title: GymLog MainActivity
 * @author Noah deFer
 * Date: 4/5/2025
 * Description: The MainActivity for the GymLog application.
 */

package com.example.gymlog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.gymlog.database.GymLogRepository;
import com.example.gymlog.database.entities.GymLog;
import com.example.gymlog.database.entities.User;
import com.example.gymlog.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // STATIC FIELDS
    private static final String MAIN_ACTIVITY_USER_ID = "com.example.gymlog.MAIN_ACTIVITY_USER_ID";

    private static final String SAVED_INSTANCE_STATE_USERID_KEY =
            "com.example.gymlog.SAVED_INSTANCE_STATE_USERID_KEY";

    // SharedPreference Strings
    static final String SHARED_PREFERENCE_USERID_KEY =
            "com.example.gymlog.SHARED_PREFERENCE_USERID_KEY";
    static final String SHARED_PREFERENCE_USERID_VALUE =
            "com.example.gymlog.SHARED_PREFERENCE_USERID_VALUE";

    // The identifier used for Logcat.
    public static final String TAG = "NSD_GYMLOG";

    // The default value for loggedInUserId when no user is logged in.
    private static final int LOGGED_OUT = -1;

    // INSTANCE FIELDS

    // The Binding object for MainActivity.
    private ActivityMainBinding binding;

    // The repository.
    private GymLogRepository repository;

    // The String entered in exerciseInputEditText
    String mExercise = "";

    // The double entered in weightInputEditText
    double mWeight = 0.0;

    // The int entered in repInputEditText
    int mReps = 0;

    // The ID of the logged in user. Defaults to LOGGED_OUT (-1) if no user is logged in.
    private int loggedInUserId = LOGGED_OUT;

    // The data of the logged in user.
    private User user;

    // Methods

    /**
     * Called when the activity is starting.
     * Gets the repository.
     * Checks if user is logged in, starts the LoginActivity if not.
     * Sets onClickListeners to logButton and exerciseInputTextEdit.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get repository instance, BEFORE calling loginUser!
        repository = GymLogRepository.getRepository(getApplication());

        // Handle login
        loginUser(savedInstanceState);
        if(loggedInUserId == LOGGED_OUT){
            Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
            startActivity(intent);
        }

        // Add scrolling to logDisplayTextView
        binding.logDisplayTextView.setMovementMethod(new ScrollingMovementMethod());

        // Update display
        updateDisplay();

        // Wire button
        binding.logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformationFromDisplay();
                insertGymLogRecord();
                updateDisplay();
            }
        });

        // Set UpdateDisplay Shortcut
        binding.exerciseInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDisplay();
            }
        });
    }

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
     * Inserts a new GymLog into the repository if mExercise is not empty.
     */
    private void insertGymLogRecord() {
        if(mExercise.isEmpty()) {
            return;
        }
        GymLog log = new GymLog(mExercise, mWeight, mReps, loggedInUserId);
        repository.insertGymLog(log);
    }

    /**
     * Intent factory for MainActivity.
     * Stores the userId entered on the login page.
     * @param context The application context.
     * @param userId The userId entered on the login page.
     * @return The MainActivity Intent.
     */
    static Intent mainActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
        return intent;
    }

    /**
     * Attempts to get user and user id from the application shared preferences, instance state,
     *      intent extras, and user repository.
     * loggedInUserId is set to LOGGED_OUT by default if no user id is found.
     * Starts LoginActivity if a user id is found but no valid user.
     */
    private void loginUser(Bundle savedInstanceState) {
        // Get SharedPreferences.
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(SHARED_PREFERENCE_USERID_KEY, Context.MODE_PRIVATE);

        // Check shared preference for logged in user.
        if(sharedPreferences.contains(SHARED_PREFERENCE_USERID_VALUE)) {
            loggedInUserId = sharedPreferences.getInt(SHARED_PREFERENCE_USERID_VALUE, LOGGED_OUT);
        }

        // Check instance state for user id.
        if(loggedInUserId == LOGGED_OUT && savedInstanceState != null
                && savedInstanceState.containsKey(SHARED_PREFERENCE_USERID_KEY)) {
            loggedInUserId = savedInstanceState.getInt(SHARED_PREFERENCE_USERID_KEY, LOGGED_OUT);
        }

        // Check intent for user id.
        if(loggedInUserId == LOGGED_OUT) {
            loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);
        }

        // Check if loggedUserId is -1.
        if(loggedInUserId == LOGGED_OUT) {
            return;
        }

        // Check repository for logged in user.
        LiveData<User> userObserver = repository.getUserByUserId(loggedInUserId);
        userObserver.observe(this, user -> {
            this.user = user;
            if(this.user != null) {
                invalidateOptionsMenu();
            } else {
//                TODO: Verify if this was an issue...
//                logout();
            }
        });
    }

    /**
     * Sets the application shared preference for the logged in user to LOGGED_OUT.
     * Starts the LoginActivity.
     */
    private void logout() {
        // Set user preference to LOGGED_OUT.
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(SHARED_PREFERENCE_USERID_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(SHARED_PREFERENCE_USERID_KEY, LOGGED_OUT);
        sharedPrefEditor.apply();

        // Start LoginActivity
        getIntent().putExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    /**
     * Called before MainActivity is killed.
     * Attempts to save user id preference.
     * @param outState Bundle in which to place your saved state.
     *
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save user id preference.
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserId);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_USERID_KEY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(SHARED_PREFERENCE_USERID_KEY, loggedInUserId);
        sharedPrefEditor.apply();
    }

    /**
     * Called when a menu is created.
     * @param menu The options menu in which you place your items.
     * @return ???
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    /**
     * Called before the menu is displayed.
     * Displays the logoutMenuItem, and assigns an onClickListener to it.
     * @param menu The options menu as last shown or first initialized by
     *             onCreateOptionsMenu().
     * @return true = menu displayed, false = menu not displayed.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        item.setVisible(true);
        if(user == null) {
            return false;
        }
        item.setTitle(user.getUsername());
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                showLogoutDialog();
                return false;
            }
        });
        return true;
    }

    /**
     * Called when logoutMenuItem is clicked.
     * Displays an alert message to the user.
     * User clicks 'Logout': logout() is called.
     * User clicks 'Cancel': alert message is dismissed.
     */
    private void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Logout?");

        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertBuilder.create().show();
    }

    /**
     * Constructs String using mExercise, mWeight, mReps, and current text of logDisplayTextView.
     * Sets text of logDisplayTextView to constructed String.
     */
    private void updateDisplay() {
        ArrayList<GymLog> allLogs = repository.getAllLogs();
        if(allLogs.isEmpty()) {
            binding.logDisplayTextView.setText(R.string.nothing_to_show_time_to_hit_the_gym);
        }
        StringBuilder sb = new StringBuilder();
        for(GymLog log : allLogs) {
            sb.append(log);
        }
        binding.logDisplayTextView.setText(sb.toString());
    }

}