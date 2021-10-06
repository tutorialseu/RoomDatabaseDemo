package eu.tutorials.roomdemo

import android.app.Application
//Todo 7: create the application class and initialize the database
class EmployeeApp:Application() {

    val db by lazy {
        EmployeeDatabase.getInstance(this)
    }

}