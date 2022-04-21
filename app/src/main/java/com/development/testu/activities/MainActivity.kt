package com.development.testu.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.development.testu.Constants
import com.development.testu.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        btn_start.setOnClickListener{

            if(et_name.text?.isEmpty() == true){
                Toast.makeText(this,"Please enter your name", Toast.LENGTH_LONG).show()
            }else{
                val intent = Intent(this@MainActivity, TestingLocationListActivity::class.java)
                intent.putExtra(Constants.USER_NAME, et_name.text.toString())
                startActivity(intent)
                finish()



            }
        }
    }
}