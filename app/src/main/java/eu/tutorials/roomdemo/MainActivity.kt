package eu.tutorials.roomdemo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import eu.tutorials.roomdemo.data.EmployeeDao
import eu.tutorials.roomdemo.data.EmployeeEntity
import eu.tutorials.roomdemo.databinding.ActivityMainBinding
import eu.tutorials.roomdemo.databinding.DialogUpdateBinding
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
            employeeDao?.let { dao ->
                addRecord(dao)
            }
        }

        lifecycleScope.launch {
            employeeDao?.fetchAllEmployee()?.collect {
                Log.d("employee", "$it")
                setupListOfDataIntoRecyclerView(it.toMutableList(), employeeDao)
            }
        }
    }


    private fun setupListOfDataIntoRecyclerView(
        employeesList: MutableList<EmployeeEntity>, employeeDao: EmployeeDao
    ) {

        if (employeesList.isNotEmpty()) {
            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = ItemAdapter(employeesList, updateListener = {
                updateRecordDialog(it, employeeDao)
            }) { deleteId ->
                lifecycleScope.launch {
                    employeeDao.fetchEmployeeById(deleteId).collect {
                        if (it != null) {
                            deleteRecord(deleteId, employeeDao, it.name)
                        }
                    }
                }
            }
            // Set the LayoutManager that this RecyclerView will use.
            binding.rvItemsList.layoutManager = LinearLayoutManager(this)
            // adapter instance is set to the recyclerview to inflate the items.
            binding.rvItemsList.adapter = itemAdapter
            binding.rvItemsList.visibility = View.VISIBLE
            binding.tvNoRecordsAvailable.visibility = View.GONE
        } else {

            binding.rvItemsList.visibility = View.GONE
            binding.tvNoRecordsAvailable.visibility = View.VISIBLE
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
        } else {
            Toast.makeText(
                applicationContext,
                "Name or Email cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun updateRecordDialog(id: Int, employeeDao: EmployeeDao) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        val binding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        lifecycleScope.launch {
            employeeDao.fetchEmployeeById(id).collect {
                binding.etUpdateName.setText(it.name)
                binding.etUpdateEmailId.setText(it.email)
            }
        }

        binding.tvUpdate.setOnClickListener {
            val name = binding.etUpdateName.text.toString()
            val email = binding.etUpdateEmailId.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                lifecycleScope.launch {
                    employeeDao.update(EmployeeEntity(id, name, email))
                    Toast.makeText(applicationContext, "Record Updated.", Toast.LENGTH_LONG)
                        .show()
                    updateDialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Name or Email cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        updateDialog.show()
    }

    fun deleteRecord(id: Int, employeeDao: EmployeeDao, name: String) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set message for alert dialog
        builder.setMessage("Are you sure you wants to delete $name.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                employeeDao.delete(EmployeeEntity(id))
                Toast.makeText(
                    applicationContext,
                    "Record deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()
                dialogInterface.dismiss() // Dialog will be dismissed
            }
        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }
}