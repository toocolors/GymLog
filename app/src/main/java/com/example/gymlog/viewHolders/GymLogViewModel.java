/**
 * Title GymLogViewModel.java
 * @author Noah deFer
 * Date: 4/6/2025
 * Description:
 */
package com.example.gymlog.viewHolders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gymlog.database.GymLogRepository;
import com.example.gymlog.database.entities.GymLog;

import java.util.List;

public class GymLogViewModel extends AndroidViewModel {

    // FIELDS

    private GymLogRepository repository;

//    private final LiveData<List<GymLog>> allLogsById;

    // CONSTRUCTOR

    /**
     * Initializes repository and allLogsById.
     * @param application Our application.
     */
    public GymLogViewModel(Application application) {
        super(application);
        repository = GymLogRepository.getRepository(application);
//        allLogsById = repository.getAllLogsByUserIdLiveData(userId);
    }

    // METHODS

    /**
     * Getter for getAllLogsById.
     * @return getAllLogsById.
     */
    public LiveData<List<GymLog>> getAllLogsById(int userId) {
        return repository.getAllLogsByUserIdLiveData(userId);
    }

    /**
     * Inserts a GymLog into the repository.
     * @param gymLog The GymLog to be inserted.
     */
    public void insert(GymLog gymLog) {
        repository.insertGymLog(gymLog);
    }
}
