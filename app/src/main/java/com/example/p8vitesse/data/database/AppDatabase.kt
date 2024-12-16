package com.example.p8vitesse.data.database

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.p8vitesse.R
import com.example.p8vitesse.data.converter.Converter
import com.example.p8vitesse.data.dao.CandidatDtoDao
import com.example.p8vitesse.data.entity.CandidatDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Database(entities = [CandidatDto::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)  // Register the ImageConverter
abstract class AppDatabase : RoomDatabase() {

    // DAOs to access the database tables
    abstract fun candidatDtoDao(): CandidatDtoDao

    // Callback to handle initial database population when it is created
    private class AppDatabaseCallback(
        private val scope: CoroutineScope,
        private val context: Context
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.e("AppDatabase", "Database created, populating database...")

            INSTANCE?.let { database ->
                scope.launch {
                    // Populate the database with initial data, passing the context
                    populateDatabase(database.candidatDtoDao(), context)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Get the singleton instance of the database
        fun getDatabase(context: Context, coroutineScope: CoroutineScope): AppDatabase {
            Log.e("AppDatabase", "Getting database instance")
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "VitessDB"  // Name of the database
                )
                    .addCallback(AppDatabaseCallback(coroutineScope, context))  // Pass context to callback
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Function to populate the database with initial data
        suspend fun populateDatabase(candidatDtoDao: CandidatDtoDao, context: Context) {
            // Clear existing data to ensure a fresh start
            candidatDtoDao.deleteAllCandidates()

            // Log before inserting
            Log.e("AppDatabase", "Starting database population")

            // Create a sample Bitmap (this would be an actual image in a real app)
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.rounded_background)

            // Convert the Bitmap to a Base64 string using the ImageConverter
            val imageConverter = Converter()
            val profilePictureBase64 = imageConverter.fromBitmap(bitmap)

            // Insert candidates
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                // Insert candidates with profile pictures
                candidatDtoDao.insertCandidat(
                    CandidatDto(
                        name = "Jin", surname = "Zhao", phone = "12345", email = "jin@icloud.com",
                        birthdate = dateFormat.parse("1990-01-01"),
                        desiredSalary = 1200.0, note = "Learning programming", isFav = false,
                        profilePicture = profilePictureBase64
                    )
                )
                candidatDtoDao.insertCandidat(
                    CandidatDto(
                        name = "Alice", surname = "Pio", phone = "67890", email = "alice@icloud.com",
                        birthdate = dateFormat.parse("1990-01-01"),
                        desiredSalary = 2400.0, note = "Learning programming", isFav = false,
                        profilePicture = profilePictureBase64
                    )
                )
                candidatDtoDao.insertCandidat(
                    CandidatDto(
                        name = "Lane", surname = "Yu", phone = "11111", email = "lane@icloud.com",
                        birthdate = dateFormat.parse("1990-01-01"),
                        desiredSalary = 3000.0, note = "Learning programming", isFav = true,
                        profilePicture = profilePictureBase64
                    )
                )
                candidatDtoDao.insertCandidat(
                    CandidatDto(
                        name = "Lolo", surname = "Li", phone = "000000", email = "lolo@icloud.com",
                        birthdate = dateFormat.parse("1990-01-01"),
                        desiredSalary = 1300.0, note = "Learning programming", isFav = true,
                        profilePicture = profilePictureBase64
                    )
                )

                // Log after inserting
                val allCandidats = candidatDtoDao.getAllCandidats()
                Log.e("AppDatabase", "Number of candidates inserted: ${allCandidats}")

            } catch (e: Exception) {
                Log.e("AppDatabase", "Error inserting candidates", e)
            }
        }
    }
}
