package assignment4.classregistration.com

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import android.widget.TextView
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject


class AddedCourse_Activity : AppCompatActivity() {

    lateinit var btnAdd: Button
    lateinit var btnRemove: Button
    lateinit var btnReset: Button
    lateinit var courseListView : ListView
    lateinit var waitListView : ListView
    val appContext: CertificateApplication
        get() = applicationContext as CertificateApplication
    lateinit var redId : String
    lateinit var password : String
    private val addedClasses = arrayListOf<ClassDetail>()
    private val waitListClasses = arrayListOf<ClassDetail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_added_course)
        courseListView = findViewById(R.id.courseListView)
        waitListView = findViewById(R.id.waitListView)
        btnAdd = findViewById(R.id.btnAdd)
        btnReset = findViewById(R.id.btnReset)

        courseListView.isVerticalScrollBarEnabled = true
        waitListView.isVerticalScrollBarEnabled = true

        btnAdd.setOnClickListener {
            val intent = Intent(this, Filter_Activity::class.java)
            startActivity(intent)
        }

        btnReset.setOnClickListener {
            onResetButtonClick()
        }
//        redId = "000006577"
//        password = "abcdefghij"

        val sharedPreferences = getSharedPreferences("PersonalInformation", Context.MODE_PRIVATE)
        if (sharedPreferences.all.isNotEmpty()) {
            redId = (sharedPreferences.getString("redid", ""))
            password = (sharedPreferences.getString("password", ""))
        }


        CustomAdapter(this,false).getClasses()
        CustomAdapter(this,true).getClasses()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Main_Activity::class.java)
        startActivity(intent)
    }

    fun onResetButtonClick(){
        try {
            val url = "https://bismarck.sdsu.edu/registration/resetstudent?redid=" + redId + "&password=" + password
            val myStringRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener {
                        if (it.has("error")) {
                            Toast.makeText(this, it["error"].toString(), Toast.LENGTH_SHORT).show()
                        } else {
                            val intent = Intent(this, AddedCourse_Activity::class.java)
                            startActivity(intent)
                        }
                    }, Response.ErrorListener
            {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }) {}

            appContext.queue.add(myStringRequest)
        }
        catch (e: Exception) {
            Log.e("Exception", "Nework Issue", e)
        }
    }


    inner private class CustomAdapter(context:Context, isWaitListView: Boolean):BaseAdapter(){

        private val mContext:Context
        var isWaitListView : Boolean = false

        init {
            this.mContext = context
            this.isWaitListView = isWaitListView
        }

        //for rows in list
        override fun getCount(): Int {
            return if (isWaitListView) waitListClasses.count() else  addedClasses.count()
        }

        override fun getItem(position: Int): Any {
            return if (isWaitListView) waitListClasses[position] else addedClasses[position]
        }

        override fun getItemId(position: Int): Long {
             return position.toLong()
        }

        //used to render out each row
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflatter = LayoutInflater.from(mContext)
            val listRow = layoutInflatter.inflate(R.layout.listview_layout, parent, false)

            val txtCourseDetail = listRow.findViewById<TextView>(R.id.txtCourseDetail)
            val txtInstructor = listRow.findViewById<TextView>(R.id.txtInstructor)
            val txtTime = listRow.findViewById<TextView>(R.id.txtTime)
            val txtSeatsAvailable = listRow.findViewById<TextView>(R.id.txtSeatsAvailable)
            val txtWaitlist = listRow.findViewById<TextView>(R.id.txtWaitlist)
            val btnRemove = listRow.findViewById<Button>(R.id.btnRemove)

            val i = position + 1


            if (isWaitListView) {
                txtCourseDetail.text = "$i) Course: " + waitListClasses.get(position).subject + waitListClasses.get(position).course + " - " + waitListClasses.get(position).title
                txtInstructor.text = "Instructor: " + waitListClasses.get(position).instructor
                txtTime.text = "Schedule: " + waitListClasses.get(position).startTime + "-" + waitListClasses.get(position).endTime + if (waitListClasses.get(position).days != "") " (" + waitListClasses.get(position).days + ")" else ""
                txtSeatsAvailable.text = "Seats, Enrolled: " + waitListClasses.get(position).seats + ", " + waitListClasses.get(position).enrolled
                txtWaitlist.text = "WaitList: " + waitListClasses.get(position).waitlist
                btnRemove.setOnClickListener {
                    onRemoveButtonClick(waitListClasses.get(position), true)
                }
            } else {
                txtCourseDetail.text = "$i) Course: " + addedClasses.get(position).subject + addedClasses.get(position).course + " - " + addedClasses.get(position).title
                txtInstructor.text = "Instructor: " + addedClasses.get(position).instructor
                txtTime.text = "Schedule: " + addedClasses.get(position).startTime + "-" + addedClasses.get(position).endTime + if (addedClasses.get(position).days != "") " (" + addedClasses.get(position).days + ")" else ""
                txtSeatsAvailable.text = "Seats, Enrolled: " + addedClasses.get(position).seats + ", " + addedClasses.get(position).enrolled
                txtWaitlist.text = "WaitList: " + addedClasses.get(position).waitlist
                btnRemove.setOnClickListener {
                    onRemoveButtonClick(addedClasses.get(position), false)
                }
            }

            return listRow
        }

        fun getClasses() {
            val url = "https://bismarck.sdsu.edu/registration/studentclasses"
            val mJsonObject = JSONObject()
            mJsonObject.put("redid", redId)
            mJsonObject.put("password", password)
            val myStringRequest = object : JsonObjectRequest(Request.Method.POST, url, mJsonObject,
                    Response.Listener { response ->
                        if (isWaitListView) {
                            var waitListClassIds = arrayListOf<Int>()
                            val responseWaitListClassIds = response.getJSONArray("waitlist")
                            for (i in 0 until responseWaitListClassIds.length()) {
                                waitListClassIds.add(responseWaitListClassIds.getInt(i))
                            }
                            getClassDetail(waitListClassIds, waitListClasses, true)
                            waitListView.adapter = this
                        } else {
                            var addedClassIds = arrayListOf<Int>()
                            val responseAddedClassIds = response.getJSONArray("classes")
                            for (i in 0 until responseAddedClassIds.length()) {
                                addedClassIds.add(responseAddedClassIds.getInt(i))
                            }
                            getClassDetail(addedClassIds, addedClasses, false)
                            courseListView.adapter = this
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(null, "Something is not right", Toast.LENGTH_SHORT)
                    }
            ) {}
            appContext.queue.add(myStringRequest)
            Log.i("message", addedClasses.count().toString())
        }

        fun getClassDetail(classIds : ArrayList<Int>, result : ArrayList<ClassDetail>,isWaitListView:Boolean) {
            val mJsonObj = JSONObject()
            val array = JSONArray()
            for (i in 0 until classIds.count()) {
                array.put(classIds.get(i))
            }
            mJsonObj.put("classids", array)
            val url = "https://bismarck.sdsu.edu/registration/classdetails"
            val myStringRequest = CustomJsonRequest(Request.Method.POST, url, mJsonObj,
                    Response.Listener { response ->
                        for (i in 0 until response.length()) {
                            val classDetail = ClassDetail.getObjectFromJson(response.getJSONObject(i))
                            result.add(classDetail)
                        }
                        if(isWaitListView){
                            waitListView.setAdapter(this)
                        }
                        else
                        {
                            courseListView.setAdapter(this)
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(null, "Something is not right", Toast.LENGTH_SHORT)
                    }
            )
            appContext.queue.add(myStringRequest)
        }

        private fun onRemoveButtonClick(classDetail : ClassDetail, isWaitListView: Boolean){
            try {

                var unregisterClassUrl = "https://bismarck.sdsu.edu/registration/unregisterclass"
                var unwaitlistClassUrl = "https://bismarck.sdsu.edu/registration/unwaitlistclass"
                var queryString = "?redid=" + redId + "&password=" + password+"&courseid=" + classDetail.id.toInt()

                val url = if (isWaitListView) (unwaitlistClassUrl + queryString) else (unregisterClassUrl + queryString)
                val myStringRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                        Response.Listener {
                            if(it.has("error")){
                                Toast.makeText(mContext, it["error"].toString(), Toast.LENGTH_SHORT).show()
                            }
                            else{
                                val intent = Intent(mContext, AddedCourse_Activity::class.java)
                                startActivity(intent)
                            }
                        }, Response.ErrorListener
                {
                    Toast.makeText(mContext, it.toString(), Toast.LENGTH_SHORT).show()
                }) { }

                appContext.queue.add(myStringRequest)
            }
            catch (e: Exception) {
                Log.e("Exception", "Nework Issue", e)
            }
        }
    }


}
