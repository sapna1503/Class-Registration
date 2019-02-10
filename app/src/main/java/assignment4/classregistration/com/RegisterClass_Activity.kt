package assignment4.classregistration.com

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import android.text.method.ScrollingMovementMethod




class RegisterClass_Activity:AppCompatActivity() {
    lateinit var btnAdd: Button
    lateinit var btnWaitList: Button


    lateinit var txtDepartment: TextView
    lateinit var txtDescription: TextView
    lateinit var txtInstructor: TextView
    lateinit var txtTitle: TextView
    lateinit var txtBuilding: TextView
    lateinit var txtDays: TextView
    lateinit var txtStartTime: TextView
    lateinit var txtEndTime: TextView
    lateinit var txtSeats: TextView
    lateinit var txtEnrolled: TextView
    lateinit var txtWaitList: TextView
    lateinit var txtUnits: TextView

    lateinit var classDetails : ClassDetail

    lateinit var redId : String
    lateinit var password : String

    val appContext: CertificateApplication
        get() = applicationContext as CertificateApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_class)

        val args = intent.getBundleExtra("bundle")
        classDetails = args.getSerializable("classDetail") as ClassDetail

        btnAdd = findViewById(R.id.btnAdd)
        btnWaitList = findViewById(R.id.btnWaitList)

        if(classDetails.seats.toInt() - classDetails.enrolled.toInt() == 0){
            btnAdd.isEnabled = false
            btnWaitList.isEnabled = true
        }
        else {
            btnWaitList.isEnabled = false
            btnAdd.isEnabled = true
        }

        txtDepartment= findViewById(R.id.txtDepartment)
        txtDescription = findViewById(R.id.txtDescription)
        txtInstructor= findViewById(R.id.txtInstructor)
        txtTitle= findViewById(R.id.txtTitle)
        txtBuilding= findViewById(R.id.txtBuilding)
        txtDays= findViewById(R.id.txtDays)
        txtStartTime = findViewById(R.id.txtStartTime)
        txtEndTime = findViewById(R.id.txtEndTime)
        txtSeats= findViewById(R.id.txtSeats)
        txtEnrolled= findViewById(R.id.txtSeatsAvailable)
        txtWaitList= findViewById(R.id.txtWaitList)
        txtUnits = findViewById(R.id.txtUnits)

        txtDescription.setMovementMethod(ScrollingMovementMethod())

        txtDepartment.text = classDetails.department
        txtDescription.text = classDetails.description
        txtInstructor.text = classDetails.instructor
        txtTitle.text = classDetails.title
        txtBuilding.text = classDetails.building
        txtDays.text = classDetails.days
        txtStartTime.text = classDetails.startTime
        txtEndTime.text = classDetails.endTime
        txtSeats.text = classDetails.seats
        txtEnrolled.text = classDetails.enrolled
        txtWaitList.text = classDetails.waitlist
        txtUnits.text = classDetails.units

        val sharedPreferences = getSharedPreferences("PersonalInformation", Context.MODE_PRIVATE)
        if (sharedPreferences.all.isNotEmpty()) {
            redId = (sharedPreferences.getString("redid", ""))
            password = (sharedPreferences.getString("password", ""))
        }
////
//        redId = "000006577"
//        password = "abcdefghij"

        btnAdd.setOnClickListener {
            onRegisterButtonClick()
        }

        btnWaitList.setOnClickListener {
            onWaitListButtonClick()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Filter_Activity::class.java)
        startActivity(intent)
    }

    fun onRegisterButtonClick(){
        try {
            val mJsonObj = JSONObject()
            mJsonObj.put("redid", redId)
            mJsonObj.put("password", password)
            mJsonObj.put("courseid", classDetails.id.toInt())

            val url = "https://bismarck.sdsu.edu/registration/registerclass"
            val myStringRequest = object : JsonObjectRequest(Request.Method.POST, url, mJsonObj,
                    Response.Listener {
                        if(it.has("error")){
                            Toast.makeText(this, it["error"].toString(), Toast.LENGTH_SHORT).show()
                        }
                        else{
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
            Log.e("Exception", "Nework Issue", e)
        }
    }

    fun onWaitListButtonClick(){
        try {
            val mJsonObj = JSONObject()
            mJsonObj.put("redid", redId)
            mJsonObj.put("password", password)
            mJsonObj.put("courseid", classDetails.id.toInt())

            val url = "https://bismarck.sdsu.edu/registration/waitlistclass"
            val myStringRequest = object : JsonObjectRequest(Request.Method.POST, url, mJsonObj,
                    Response.Listener {
                        if(it.has("error")){
                            Toast.makeText(this, it["error"].toString(), Toast.LENGTH_SHORT).show()
                        }
                        else{
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
            Log.e("Exception", "Nework Issue", e)
        }
    }
}