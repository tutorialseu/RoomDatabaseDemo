package eu.tutorials.roomdemo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Insert
    suspend fun insert(employeeEntity: EmployeeEntity)

    @Query("Select * from `employee-table`")
    fun fetchAllEmployee(): Flow<List<EmployeeEntity>>
}