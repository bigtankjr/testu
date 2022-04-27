package com.development.testu.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.development.testu.Constants
import com.development.testu.R
import com.development.testu.adapters.TestingLocationsAdapter
import com.development.testu.database.DatabaseHandler
import com.development.testu.models.TestingLocationModel
import com.development.testu.utils.SwipeToDeleteCallback
import com.development.testu.utils.SwipeToEditCallback
import kotlinx.android.synthetic.main.activity_testing_location_list.*


class TestingLocationListActivity : AppCompatActivity() {


    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        var userId: String = intent.getStringExtra(Constants.USER_ID).toString()
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_testing_location_list)


        // Setting an click event for Fab Button and calling the AddTestingLocationActivity.
        fabAddTestingLocation.setOnClickListener {

            val intent = Intent(this@TestingLocationListActivity, AddTestingLocationActivity::class.java)
            intent.putExtra(Constants.USER_ID, userId)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }

        getTestingLocationsListFromLocalDB()
    }

    // Call Back method  to get the Message form other Activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var userId: String = intent.getStringExtra(Constants.USER_ID).toString()
        // check if the request code is same as what is passed  here it is 'ADD_PLACE_ACTIVITY_REQUEST_CODE'
        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getTestingLocationsListFromLocalDB()
            }else{
                Log.e("Activity", "Cancelled or Back Pressed")
            }
        }
    }

    /**
     * A function to get the list of Testing Location from local database.
     */
    private fun getTestingLocationsListFromLocalDB() {

        val dbHandler = DatabaseHandler(this)

        val getTestingLocationsList = dbHandler.getTestingLocationsList()


        if (getTestingLocationsList!!.size > 0) {
            rv_testing_locations_list.visibility = View.VISIBLE
            tv_no_records_available.visibility = View.GONE
            if (getTestingLocationsList != null) {
                setupTestingLocationsRecyclerView(getTestingLocationsList)
            }
        } else {
            rv_testing_locations_list.visibility = View.GONE
            tv_no_records_available.visibility = View.VISIBLE
        }
    }

    /**
     * A function to populate the recyclerview to the UI.
     */
    private fun setupTestingLocationsRecyclerView(testingLocationsList: ArrayList<TestingLocationModel>) {

        rv_testing_locations_list.layoutManager = LinearLayoutManager(this)
        rv_testing_locations_list.setHasFixedSize(true)

        val placesAdapter = TestingLocationsAdapter(this, testingLocationsList)
        rv_testing_locations_list.adapter = placesAdapter

        placesAdapter.setOnClickListener(object :
            TestingLocationsAdapter.OnClickListener {
            override fun onClick(position: Int, model: TestingLocationModel) {
                val intent = Intent(this@TestingLocationListActivity, TestingLocationDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model) // Passing the complete serializable data class to the detail activity using intent.
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_testing_locations_list.adapter as TestingLocationsAdapter
                adapter.notifyEditItem(
                    this@TestingLocationListActivity,
                    viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQUEST_CODE
                )
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_testing_locations_list)

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_testing_locations_list.adapter as TestingLocationsAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                var userId: String = intent.getStringExtra(Constants.USER_ID).toString()
                getTestingLocationsListFromLocalDB() // Gets the latest list from the local database after item being delete from it.
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_testing_locations_list)
    }

    companion object{
        private const val ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        internal const val EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}