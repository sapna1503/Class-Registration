package assignment4.classregistration.com

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest


class Main_Activity : AppCompatActivity(){

    lateinit var etFirstName: EditText
    lateinit var etLastName: EditText
    lateinit var etRedId: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var btnAddStudent: Button

    companion object {
        private var existsInSharedPrefrences: Boolean = false
    }

    lateinit var sharedPreferences :SharedPreferences
    val appContext: CertificateApplication
        get() = applicationContext as CertificateApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etRedId = findViewById(R.id.etRedId)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnAddStudent = findViewById(R.id.btnAddStudent)
        existsInSharedPrefrences = fillInformationIfAvailable()
        btnAddStudent.setOnClickListener {
            onProceedButtonClick()
        }
    }

    private fun fillInformationIfAvailable() :Boolean{
        sharedPreferences = getSharedPreferences("PersonalInformation", Context.MODE_PRIVATE)
        if (sharedPreferences.all.isNotEmpty()) {
            etFirstName.setText(sharedPreferences.getString("firstname", ""))
            etLastName.setText(sharedPreferences.getString("lastname", ""))
            etRedId.setText(sharedPreferences.getString("redid", ""))
            etEmail.setText(sharedPreferences.getString("email", ""))
            etPassword.setText(sharedPreferences.getString("password", ""))
            return true
        }
        return false
    }

    private fun onProceedButtonClick(){
        if(existsInSharedPrefrences){
            val intent = Intent(this, AddedCourse_Activity::class.java)
            startActivity(intent)
        }
        else{
            if(isValidData()){
                addStudent()
            }
        }
    }

    private fun savePersonalInformation() {
        val sharedPreferences = getSharedPreferences("PersonalInformation", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("firstname", etFirstName.text.toString().trim())
        editor.putString("lastname", etLastName.text.toString().trim())
        editor.putString("redid", etRedId.text.toString().trim())
        editor.putString("email", etEmail.text.toString().trim())
        editor.putString("password", etPassword.text.toString().trim())
        editor.apply()
    }

    private fun isValidData():Boolean{
        if (etFirstName.text.toString().trim() == ""){
            Toast.makeText(this, "Enter First Name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (etLastName.text.toString().trim() == ""){
            Toast.makeText(this, "Enter Last Name", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun addStudent() {
        try {
            var student = Student(etFirstName.text.toString(), etLastName.text.toString(), etRedId.text.toString(), etEmail.text.toString(), etPassword.text.toString())
            val url = "https://bismarck.sdsu.edu/registration/addstudent"
            val myStringRequest = object : JsonObjectRequest(Request.Method.POST, url, student.getJsonObject(),
                    Response.Listener {
                        if(it.has("error")){
                            Toast.makeText(this, it["error"].toString(), Toast.LENGTH_SHORT).show()
                        }
                        else{
                            savePersonalInformation()
                            val intent = Intent(this, AddedCourse_Activity::class.java)
                            startActivity(intent)
                        }
                    }, Response.ErrorListener
                    {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    }) { }

            appContext.queue.add(myStringRequest)
        }
        catch (e: Exception) {
            Log.e("rew", "nework issue", e)
        }

    }
}
