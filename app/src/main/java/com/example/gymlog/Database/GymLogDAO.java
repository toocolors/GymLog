/**
 * Title: GymLogDAO.java
 * @author Noah deFer
 * Date: 4/5/2025
 * Description: DAO for application database.
 */
package com.example.gymlog.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gymlog.Database.entities.GymLog;

import java.util.List;

@Dao
public interface GymLogDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GymLog gymLog);

    @Query("SELECT * FROM " + GymLogDatabase.GYM_LOG_TABLE)
    List<GymLog> getAllRecords();

}
