package assignment4.classregistration.com

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import android.graphics.Typeface
import android.util.Log
import android.widget.TextView
import android.widget.BaseExpandableListAdapter
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable


class AddCourse_Activity: AppCompatActivity(){
    lateinit var expandableListView: ExpandableListView
    lateinit var level : String
    lateinit var startTime : String
    lateinit var endTime : String
    lateinit var subjects : ArrayList<Subject>
    var listDataChild = HashMap<Int, List<ClassDetail>>()

    val appContext: CertificateApplication
        get() = applicationContext as CertificateApplication


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        expandableListView = findViewById(R.id.expandableListView)

        //To get intent values
        val args = intent.getBundleExtra("bundle")
        level = args.getString("level").toLowerCase()
        subjects = args.getSerializable("subjects") as ArrayList<Subject>
        startTime = args.getString("start-time")
        endTime = args.getString("end-time")


        //New
        val adapter: ExpandableListAdapter = ExpandableListAdapter(this)
        for ( i in 0 until subjects.count()) {
            adapter.getClassIdList(subjects[i])
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, AddedCourse_Activity::class.java)
        startActivity(intent)
    }

    inner class ExpandableListAdapter( val _context: Context) : BaseExpandableListAdapter() {

        override fun getChild(groupPosition: Int, childPosititon: Int): Any {
            return listDataChild[subjects[groupPosition].id]!!.get(childPosititon)
        }

        override fun getChildId(groupPosition: Int, childPosition: Int): Long {
            return childPosition.toLong()
        }

        override fun getChildView(groupPosition: Int, childPosition: Int,
                                  isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val classDetail = getChild(groupPosition, childPosition) as ClassDetail

            if (convertView == null) {
                val infalInflater = this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                convertView = infalInflater.inflate(R.layout.expandable_child_layout, null)
            }

            val txtListChild = convertView!!.findViewById(R.id.txtListItem) as TextView
            txtListChild.text = classDetail.title

            val txtDuration = convertView!!.findViewById(R.id.txtDuration) as TextView
            txtDuration.text = "Schedule : " + classDetail.startTime + "-" + classDetail.endTime + if (classDetail.days != "") " (" + classDetail.days + ")" else ""

            val btnAddUpdate = convertView.findViewById(R.id.btnAdd) as Button
            btnAddUpdate.setOnClickListener {
                onAddUpdaterButtonClick(classDetail)
            }
            return convertView
        }

        private fun onAddUpdaterButtonClick(classDetail :ClassDetail){
            var intent = Intent(this._context, RegisterClass_Activity::class.java)
            val args = Bundle()
            args.putSerializable("classDetail", classDetail as Serializable)
            intent.putExtra("bundle", args)
            startActivity(intent)
        }

        override fun getChildrenCount(groupPosition: Int): Int {
            return listDataChild[subjects[groupPosition].id]!!.count()
        }

        override fun getGroup(groupPosition: Int): Any {
            return subjects[groupPosition]
        }

        override fun getGroupCount(): Int {
            return subjects.size
        }

        override fun getGroupId(groupPosition: Int): Long {
            return groupPosition.toLong()
        }

        override fun getGroupView(groupPosition: Int, isExpanded: Boolean,convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val headerTitle = getGroup(groupPosition) as Subject
            if (convertView == null) {
                val infalInflater = this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                convertView = infalInflater.inflate(R.layout.expandable_parent_layout, null)
            }

            val txtListHeader = convertView!!.findViewById<View>(R.id.txtListHeader) as TextView
            txtListHeader.setTypeface(null, Typeface.BOLD)
            txtListHeader.text = headerTitle.title

            return convertView
        }

        override fun hasStableIds(): Boolean {
            return false
        }

        override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
            return true
        }

        fun getClassIdList(subject : Subject) {
            val url = " https://bismarck.sdsu.edu/registration/classidslist"
            val mJsonObj = JSONObject()
            val array = JSONArray()
            array.put(subject.id)

            mJsonObj.put("subjectids", array)
            if(!level.equals("select", true)) {
                mJsonObj.put("level", level)
            }
            if (!startTime.equals("0000")) {
                mJsonObj.put("starttime", startTime)
            }
            if (!endTime.equals("2359")) {
                mJsonObj.put("endtime", endTime)
            }

            Log.i("Get Class Ids", mJsonObj.toString())
            val jsonObjectRequest = CustomJsonRequest(Request.Method.POST, url, mJsonObj,
                    Response.Listener { response ->
                        var res = response as JSONArray
                        var classIds = arrayListOf<Int>()
                        for (i in 0 until res.length()) {
                            classIds.add(res.getInt(i))
                        }
                        getClassDetail(subject, classIds)
                        expandableListView.setAdapter(this)
                    },
                    Response.ErrorListener {
                        Toast.makeText(null, "Something is not right", Toast.LENGTH_SHORT)
                    })
            appContext.queue.add(jsonObjectRequest)
        }

        fun getClassDetail(subject : Subject, classIds : ArrayList<Int>) {
            val mJsonObj = JSONObject()
            val array = JSONArray()
            for (i in 0 until classIds.count()){
                array.put(classIds.get(i))
            }
            mJsonObj.put("classids", array)
            val url = " https://bismarck.sdsu.edu/registration/classdetails"
            val myStringRequest = CustomJsonRequest(Request.Method.POST, url, mJsonObj,
                    Response.Listener { response ->

                        var classDetails = arrayListOf<ClassDetail>()
                        for (i in 0 until response.length()) {
                            val classDetail = ClassDetail.getObjectFromJson(response.getJSONObject(i))
                            classDetails.add(classDetail)
                        }
                        listDataChild[subject.id] = classDetails
                        subject.title = subject.title + " (" + classDetails.count().toString() + ")"
                        expandableListView.setAdapter(this)

                    },
                    Response.ErrorListener {
                        Toast.makeText(null, "Something is not right", Toast.LENGTH_SHORT)
                    }
            )
            appContext.queue.add(myStringRequest)
        }
    }
}




