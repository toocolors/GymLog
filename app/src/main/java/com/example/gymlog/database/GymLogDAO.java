/**
 * Title: GymLogDAO.java
 * @author Noah deFer
 * Date: 4/5/2025
 * Description: DAO for application database.
 */
package com.example.gymlog.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gymlog.database.entities.GymLog;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface GymLogDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GymLog gymLog);

    @Query("SELECT * FROM " + GymLogDatabase.GYM_LOG_TABLE + " ORDER BY date DESC")
    List<GymLog> getAllRecords();

    @Query("SELECT * FROM " + GymLogDatabase.GYM_LOG_TABLE + " WHERE userId = :userId ORDER BY date DESC")
    List<GymLog> getRecordsByUserId(int userId);

    @Query("SELECT * FROM " + GymLogDatabase.GYM_LOG_TABLE + " WHERE userId = :userId ORDER BY date DESC")
    LiveData<List<GymLog>> getRecordsByUserIdLiveData(int userId);
}
