package eu.tutorials.roomdemo

import androidx.room.*
import kotlinx.coroutines.flow.Flow
//Todo 1: create the dao interface
@Dao
interface EmployeeDao {

     // Todo 2: create a suspend insert function for saving an entry

    @Insert
    suspend fun insert(employeeEntity: EmployeeEntity)

    // Todo 3: create a suspend update function for updating an existing entry
    @Update
    suspend fun update(employeeEntity: EmployeeEntity)

    // Todo 4: create a suspend delete function for deleting an existing entry
    @Delete
    suspend fun delete(employeeEntity: EmployeeEntity)
    // Todo 5: create a function to read all employee, this returns a Flow
    @Query("Select * from `employee-table`")
    fun fetchAllEmployee():Flow<List<EmployeeEntity>>

    // Todo 5: create a function to read one employee, this returns a Flow
    @Query("Select * from `employee-table` where id=:id")
    fun fetchEmployeeById(id:Int):Flow<EmployeeEntity>
}