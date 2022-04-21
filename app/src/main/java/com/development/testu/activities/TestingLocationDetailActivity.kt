package com.development.testu.activities


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.development.testu.R
import com.development.testu.models.TestingLocationModel
import kotlinx.android.synthetic.main.activity_testing_location_detail.*

class TestingLocationDetailActivity : AppCompatActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_testing_location_detail)

        var testingLocationDetailModel: TestingLocationModel? = null

        if (intent.hasExtra(TestingLocationListActivity.EXTRA_PLACE_DETAILS)) {
            // get the Serializable data model class with the details in it
            testingLocationDetailModel =
                intent.getParcelableExtra(TestingLocationListActivity.EXTRA_PLACE_DETAILS) as TestingLocationModel?
        }

        if (testingLocationDetailModel != null) {

            setSupportActionBar(toolbar_testing_location_detail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = testingLocationDetailModel.title

            toolbar_testing_location_detail.setNavigationOnClickListener {
                onBackPressed()
            }

            iv_testing_location_image.setImageURI(Uri.parse(testingLocationDetailModel.image))
            tv_description.text = testingLocationDetailModel.description
            tv_location.text = testingLocationDetailModel.location
        }

        btn_view_on_map.setOnClickListener {
            val intent = Intent(this@TestingLocationDetailActivity, MapActivity::class.java)
            intent.putExtra(TestingLocationListActivity.EXTRA_PLACE_DETAILS, testingLocationDetailModel)
            startActivity(intent)
        }
    }
}