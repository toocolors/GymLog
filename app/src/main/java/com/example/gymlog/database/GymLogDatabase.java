/**
 * Title: GymLogDatabase.java
 * @author Noah deFer
 * Date: 4/5/2025
 * Description: Database object for application database.
 */
package com.example.gymlog.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.gymlog.database.entities.GymLog;
import com.example.gymlog.MainActivity;
import com.example.gymlog.database.entities.User;
import com.example.gymlog.database.typeConverters.LocalDateTypeConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TypeConverters(LocalDateTypeConverter.class)
@Database(entities = {GymLog.class, User.class}, version = 3, exportSchema = false)
public abstract class GymLogDatabase extends RoomDatabase {

    // FIELDS
    // ID for the user table.
    public static final String USER_TABLE = "user_table";

    // ID for the database.
    private static final String DATABASE_NAME = "GymLog_database";

    // ID for the GymLog table.
    public static final String GYM_LOG_TABLE = "gymLogTable";

    // This GymLogDatabase instance.
    public static volatile GymLogDatabase INSTANCE;

    // The number of threads used by the ExecutorService
    private static final int NUMBER_OF_THREADS = 4;

    // The ExecutorService instance.
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // METHODS

    /**
     * Returns a GymLogDatabase instance.
     * Creates a new instance only if one doesn't exist.
     * @param context The application context.
     * @return A GymLogDatabase instance.
     */
    static GymLogDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (GymLogDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            GymLogDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Adds two default users to the user table.
     * User 1: username = admin1, password = admin1
     * User 2: username = testuser1, password = testuser1
     */
    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i(MainActivity.TAG, "DATABASE CREATED!");
            databaseWriteExecutor.execute(() -> {
                UserDAO dao = INSTANCE.userDao();
                dao.deleteAll();
                User admin = new User("admin1", "admin1");
                admin.setAdmin(true);
                dao.insert(admin);

                User testUser1 = new User("testuser1", "testuser1");
                dao.insert(testUser1);
            });
        }
    };

    public abstract GymLogDAO gymLogDAO();

    public abstract UserDAO userDao();
}
