/**
 * Title: GymLogDatabase.java
 * @author Noah deFer
 * Date: 4/5/2025
 * Description: DAO for GymLog database.
 */
package com.example.gymlog.Database;

import androidx.room.Database;

import com.example.gymlog.Database.entities.GymLog;

@Database(entities = {GymLog.class}, version= 1, exportSchema = false)
public class GymLogDatabase {
}
