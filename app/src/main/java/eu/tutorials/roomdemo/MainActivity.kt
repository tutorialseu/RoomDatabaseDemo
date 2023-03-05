package eu.tutorials.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import eu.tutorials.roomdemo.data.EmployeeDao
import eu.tutorials.roomdemo.data.EmployeeEntity
import eu.tutorials.roomdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val employeeDao = (application as EmployeeApp).db?.employeeDao()
        binding.btnAdd.setOnClickListener {
            employeeDao?.let {
                    dao -> addRecord(dao)
            }
        }

        lifecycleScope.launch {
            employeeDao?.fetchAllEmployee()?.collect {
                Log.d("employee", "$it")
            }
        }
    }

    private fun addRecord(employeeDao: EmployeeDao) {
        val name = binding.etName.text.toString()
        val email = binding.etEmailId.text.toString()
        if (name.isNotEmpty() && email.isNotEmpty()) {
            lifecycleScope.launch {
                employeeDao.insert(EmployeeEntity(name = name, email = email))
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                binding.etName.text?.clear()
                binding.etEmailId.text?.clear()
            }
        }else {
            Toast.makeText(
                applicationContext,
                "Name or Email cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}