package com.development.testu.activities


import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.development.testu.Constants
import com.development.testu.R
import com.development.testu.database.DatabaseHandler
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        btn_start.setOnClickListener{

            if(et_name.text?.isEmpty() == true || et_password.text?.isEmpty() == true){
                Toast.makeText(this,"Please enter text in each field", Toast.LENGTH_LONG).show()
            }else{
                if(dbHandler.checkUserName(et_name.text.toString())){
                        try {
                            if(dbHandler.checkUserNamePassword(et_name.text.toString(),et_password.text.toString())){
                                val intent = Intent(this@MainActivity, TestingLocationListActivity::class.java)

                                startActivity(intent)
                                finish()
                            }else{
                                Toast.makeText(this,"You entered the wrong login information. Try again.", Toast.LENGTH_LONG).show()
                            }

                        }catch (e: SQLiteException) {
                            e.printStackTrace()
                        }
                    }

            }
        }
        btn_register.setOnClickListener{
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}