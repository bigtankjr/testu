package com.development.testu.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.development.testu.R
import com.development.testu.database.DatabaseHandler
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.et_name
import kotlinx.android.synthetic.main.activity_register.et_password

class RegisterActivity : AppCompatActivity() {

    private val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        btn_signup.setOnClickListener{
            if(et_name.text?.isEmpty() == true || et_password.text?.isEmpty() == true || et_confirm_password.text?.isEmpty() == true){
                Toast.makeText(this,"Please enter text in each field", Toast.LENGTH_LONG).show()
            }else{
                try {
                    if(et_password.text.contentEquals(et_confirm_password.text.toString())){
                        dbHandler.insertData(et_name.text.toString().trim(),et_password.text.toString().trim())
                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this,"Passwords do not match.", Toast.LENGTH_LONG).show()
                    }
                }catch(e:Exception){
                    e.printStackTrace()
                }

            }
        }

    }
}