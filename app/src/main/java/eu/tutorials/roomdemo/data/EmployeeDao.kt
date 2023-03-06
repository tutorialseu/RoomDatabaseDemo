package eu.tutorials.roomdemo.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {
    @Insert
    suspend fun insert(employeeEntity: EmployeeEntity)
    @Query("Select * from `employee-table`")
    fun fetchAllEmployee(): Flow<List<EmployeeEntity>>
    @Query("Select * from `employee-table` where id=:id")
    fun fetchEmployeeById(id:Int):Flow<EmployeeEntity>
    @Update
    suspend fun update(employeeEntity: EmployeeEntity)
    @Delete
    suspend fun delete(employeeEntity: EmployeeEntity)
}