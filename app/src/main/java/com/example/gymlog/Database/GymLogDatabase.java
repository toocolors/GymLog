/**
 * Title: GymLogDatabase.java
 * @author Noah deFer
 * Date: 4/5/2025
 * Description: Database object for application database.
 */
package com.example.gymlog.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.gymlog.Database.entities.GymLog;

@Database(entities = {GymLog.class}, version= 1, exportSchema = false)
public abstract class GymLogDatabase extends RoomDatabase {

}
