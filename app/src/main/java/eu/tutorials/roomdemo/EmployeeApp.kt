package eu.tutorials.roomdemo

import android.app.Application
import eu.tutorials.roomdemo.data.EmployeeDatabase

class EmployeeApp:Application() {
    val db by lazy {
        EmployeeDatabase.getInstance(this)
    }
}