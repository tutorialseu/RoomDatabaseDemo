package eu.tutorials.roomdemo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EmployeeEntity::class], version = 1)
abstract class EmployeeDatabase:RoomDatabase() {

    abstract fun employeeDao():EmployeeDao

    companion object {

        @Volatile
        private var INSTANCE:EmployeeDatabase?  = null

        fun getInstance(context: Context): EmployeeDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    EmployeeDatabase::class.java, "my-database"
                ).build()
            }
            return INSTANCE
        }
    }
}