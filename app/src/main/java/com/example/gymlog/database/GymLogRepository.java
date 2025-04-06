/**
 * Title: GymLogRepository.java
 * @author Noah deFer
 * Date: 4/5/2025
 * Description: Repository class for application database.
 */
package com.example.gymlog.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.gymlog.database.entities.GymLog;
import com.example.gymlog.MainActivity;
import com.example.gymlog.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GymLogRepository {
    // Fields

    // Stores the GymLogDAO instance.
    private final GymLogDAO gymLogDAO;

    // Stores the UserDAO instance.
    private final UserDAO userDAO;
    private ArrayList<GymLog> allLogs;

    // Stores the repository instance.
    private static GymLogRepository repository;

    // Constructors
    private GymLogRepository(Application application) {
        GymLogDatabase db = GymLogDatabase.getDatabase(application);
        this.gymLogDAO = db.gymLogDAO();
        this.userDAO = db.userDao();
        this.allLogs = (ArrayList<GymLog>) this.gymLogDAO.getAllRecords();
    }

    /**
     * Returns an instance of GymLogRepository.
     * @param application
     * @return The repository.
     */
    // Methods
    public static GymLogRepository getRepository(Application application) {
        Future<GymLogRepository> future = GymLogDatabase.databaseWriteExecutor.submit(
                new Callable<GymLogRepository>() {
                    @Override
                    public GymLogRepository call() throws Exception {
                        repository =  new GymLogRepository(application);
                        return repository;
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(MainActivity.TAG, "Problem getting GymLogRepository, thread error.");
        }
        return null;
    }

    /**
     * Returns an ArrayList containing all GymLog logs in the repository.
     * @return An ArrayList of GymLogs.
     */
    public ArrayList<GymLog> getAllLogs() {
        Future<ArrayList<GymLog>> future = GymLogDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<GymLog>>() {
                    @Override
                    public ArrayList<GymLog> call() throws Exception {
                        return (ArrayList<GymLog>) gymLogDAO.getAllRecords();
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when getting all GymLogs in the repository");
        }
        return null;

    }

    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }

    public LiveData<User> getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }

    public void insertGymLog(GymLog gymLog) {
        GymLogDatabase.databaseWriteExecutor.execute(() -> {
            gymLogDAO.insert(gymLog);
        });
    }

    public void insertGymLog(User... user) {
        GymLogDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.insert(user);
        });
    }

    /**
     * Returns an ArrayList containing all GymLog logs with the passed in userId.
     * @param userId Used to determine which GymLog logs to get.
     * @return The ArrayList of GymLogs.
     */
    @Deprecated
    public ArrayList<GymLog> getAllLogsByUserId(int userId) {
        Future<ArrayList<GymLog>> future = GymLogDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<GymLog>>() {
                    @Override
                    public ArrayList<GymLog> call() throws Exception {
                        return (ArrayList<GymLog>) gymLogDAO.getRecordsByUserId(userId);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when getting all GymLogs in the repository");
        }
        return null;
    }

    public LiveData<List<GymLog>> getAllLogsByUserIdLiveData(int userId) {
        return gymLogDAO.getRecordsByUserIdLiveData(userId);
    }
}