/**
 * Title: LoginActivity.java
 * @author Noah deFer
 * Date: 4/5/2025
 * Description: Java class for LoginActivity.
 */
package com.example.gymlog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.gymlog.database.GymLogRepository;
import com.example.gymlog.database.entities.User;
import com.example.gymlog.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    // INSTANCE FIELDS
    // The Binding variable for LoginActivity.
    private ActivityLoginBinding binding;

    // The repository.
    private GymLogRepository repository;

    /**
     * Called when LoginActivity starts.
     * Gets repository, and sets OnClickListener for loginButton.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get repository.
        repository = GymLogRepository.getRepository(getApplication());

        // Set onClickListener for loginButton.
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });
    }

    /**
     * The intent factory for LoginActivity.
     * @param context The application context.
     * @return The LoginActivity Intent.
     */
    static Intent loginIntentFactory(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    /**
     * Creates a toast and displays it to the screen.
     * @param message The message to be displayed.
     */
    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Compares the entered username and password with the repository.
     * Shows a toast if the username or password are invalid.
     * Starts MainActivity if the username and password are valid.
     */
    private void verifyUser() {
        // Get entered username
        String username = binding.userNameLoginEditText.getText().toString();

        // Check if a username has been entered.
        if(username.isEmpty()) {
            toastMaker("Username may not be blank.");
            return;
        }

        // Set get user from repository using LiveData
        LiveData<User> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            /*
             * If the user exists:
             *      Start MainActivity if it is valid.
             *      Show toast if it is NOT valid.
             * Or show toast if the user does NOT exist.
             */
            if(user != null) {
                String password = binding.passwordLoginEditText.getText().toString();
                if(password.equals(user.getPassword())) {
                    startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext(),
                            user.getId()));
                } else {
                    toastMaker("Invalid password");
                    binding.passwordLoginEditText.setSelection(0);
                }
            } else {
                toastMaker(String.format("%s is not a valid username.", username));
                binding.userNameLoginEditText.setSelection(0);
            }
        });
    }


}