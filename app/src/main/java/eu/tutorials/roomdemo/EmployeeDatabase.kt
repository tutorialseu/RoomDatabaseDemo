package eu.tutorials.roomdemo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** Todo 6
 * Create a database that stores employee information.
 * And a global method to get access to the database.
 */
@Database(entities = [EmployeeEntity::class],version = 2)
abstract class EmployeeDatabase:RoomDatabase() {

    /**
     * Connects the database to the DAO.
     */
    abstract fun employeeDao():EmployeeDao

        /**
         * Define a companion object, this allows us to add functions on the EmployeeDatabase class.
         *
         * For example, classes can call `EMPLOYEEDatabase.getInstance(context)` to instantiate
         * a new EmployeeDatabase.
         */
        companion object {
            /**
             * INSTANCE will keep a reference to any database returned via getInstance.
             *
             * This will help us avoid repeatedly initializing the database, which is expensive.
             *
             *  The value of a volatile variable will never be cached, and all writes and
             *  reads will be done to and from the main memory. It means that changes made by one
             *  thread to shared data are visible to other threads.
             */
            @Volatile
            private var INSTANCE: EmployeeDatabase? = null

            /**
             * Helper function to get the database.
             *
             * If a database has already been retrieved, the previous database will be returned.
             * Otherwise, create a new database.
             *
             * This function is threadsafe, and callers should cache the result for multiple database
             * calls to avoid overhead.
             *
             * This is an example of a simple Singleton pattern that takes another Singleton as an
             * argument in Kotlin.
             *
             * To learn more about Singleton read the wikipedia article:
             * https://en.wikipedia.org/wiki/Singleton_pattern
             *
             * @param context The application context Singleton, used to get access to the filesystem.
             */
            fun getInstance(context: Context): EmployeeDatabase {
                // Multiple threads can ask for the database at the same time, ensure we only initialize
                // it once by using synchronized. Only one thread may enter a synchronized block at a
                // time.
                synchronized(this) {

                    // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                    // Smart cast is only available to local variables.
                    var instance = INSTANCE

                    // If instance is `null` make a new database instance.
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            EmployeeDatabase::class.java,
                            "employee_database"
                        )
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this lesson. You can learn more about
                            // migration with Room in this blog post:
                            // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                            .fallbackToDestructiveMigration()
                            .build()
                        // Assign INSTANCE to the newly created database.
                        INSTANCE = instance
                    }

                    // Return instance; smart cast to be non-null.
                    return instance
                }
            }
        }

}