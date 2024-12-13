package com.example.p8vitesse.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.p8vitesse.data.converter.Converter
import com.example.p8vitesse.data.dao.CandidatDtoDao
import com.example.p8vitesse.data.entity.CandidatDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Database(entities = [CandidatDto::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)  // Register the TypeConverters
abstract class AppDatabase : RoomDatabase() {

    // DAOs to access the database tables
    abstract fun candidatDtoDao(): CandidatDtoDao

    // Callback to handle initial database population when it is created
    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.e("AppDatabase", "Database created, populating database...") // Log here

            INSTANCE?.let { database ->
                scope.launch {
                    // Populate the database with initial data
                    populateDatabase(database.candidatDtoDao())

                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Get the singleton instance of the database
        fun getDatabase(context: Context, coroutineScope: CoroutineScope): AppDatabase {

            Log.e("AppDatabase", "Getting database instance") // Add this log to check if it's being called

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "VitessDB"  // Name of the database
                )
                    .addCallback(AppDatabaseCallback(coroutineScope))  // Adding the callback for initial population
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Function to populate the database with initial data
        suspend fun populateDatabase(candidatDtoDao: CandidatDtoDao) {
            // Clear existing data to ensure a fresh start
            candidatDtoDao.deleteAllCandidates()

            // Log before inserting
            Log.e("AppDatabase", "Starting database population")

            // Insert candidates
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                candidatDtoDao.insertCandidat(
                    CandidatDto(
                        name = "Jin", surname = "Zhao", phone = "", email = "jin@icloud.com",
                        birthdate = dateFormat.parse("1990-01-01"),
                        desiredSalary = 1200.0, note = "Learning programming", isFav = false
                    )
                )
                candidatDtoDao.insertCandidat(
                    CandidatDto(
                        name = "Alice", surname = "Pio", phone = "", email = "alice@icloud.com",
                        birthdate = dateFormat.parse("1990-01-01"),
                        desiredSalary = 2400.0, note = "Learning programming", isFav = false
                    )
                )
                candidatDtoDao.insertCandidat(
                    CandidatDto(
                        name = "Lane", surname = "Yu", phone = "", email = "lane@icloud.com",
                        birthdate = dateFormat.parse("1990-01-01"),
                        desiredSalary = 3000.0, note = "Learning programming", isFav = true
                    )
                )
                candidatDtoDao.insertCandidat(
                    CandidatDto(
                        name = "Lolo", surname = "Li", phone = "", email = "lolo@icloud.com",
                        birthdate = dateFormat.parse("1990-01-01"),
                        desiredSalary = 1300.0, note = "Learning programming", isFav = true
                    )
                )

                // Log after inserting
                val allCandidats = candidatDtoDao.getAllCandidats()
                Log.e(
                    "AppDatabase",
                    "Number of candidates inserted: ${allCandidats.size}"
                )

            } catch (e: Exception) {
                Log.e("AppDatabase", "Error inserting candidates", e)
            }
        }
    }
}
