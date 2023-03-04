package eu.tutorials.roomdemo.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface EmployeeDao {

    @Insert
    suspend fun insert(employeeEntity: EmployeeEntity)
}