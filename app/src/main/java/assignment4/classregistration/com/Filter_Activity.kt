package assignment4.classregistration.com

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*

import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import android.widget.TimePicker
import java.io.Serializable

/**
 * Created by schandiramani on 3/26/18.
 */
class Filter_Activity : AppCompatActivity() {

    lateinit var spinnerSubject : Spinner
    lateinit var spinnerLevel : Spinner
    private var btnApply: Button? = null
    lateinit var startTimePicker : TimePicker
    lateinit var endTimePicker : TimePicker
    val appContext: CertificateApplication
        get() = applicationContext as CertificateApplication

    companion object {
        val subjectSpinnerItems = arrayListOf<SubjectSpinnerItem>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        spinnerSubject = findViewById(R.id.spinnerSubject)
        val adapter = CustomAdapter(this, 0)
        adapter.getSubjects()

        spinnerLevel = findViewById(R.id.spinnerLevel)
        btnApply = findViewById<Button>(R.id.btnApply)

        fillLevelSpinner()

        btnApply!!.setOnClickListener {
            onFilterButtonClick()
        }

        startTimePicker = findViewById<TimePicker>(R.id.startTimePicker)   // initiate a time picker
        startTimePicker.setIs24HourView(true)

        endTimePicker = findViewById<TimePicker>(R.id.endTimePicker)   // initiate a time picker
        endTimePicker.setIs24HourView(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startTimePicker.setHour(0)
            startTimePicker.setMinute(0)
            endTimePicker.setHour(23)
            endTimePicker.setMinute(59)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, AddedCourse_Activity::class.java)
        startActivity(intent)
    }

    fun fillLevelSpinner(){
       // val mlevelList = arrayOf<String>("Select","Lower","Upper","Graduate")
        val mspinnerAdapter = ArrayAdapter.createFromResource(this,R.array.levelArray, android.R.layout.simple_spinner_item)
        mspinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerLevel.setAdapter(mspinnerAdapter)


        //  spinnerSubject!!.setOnItemSelectedListener(ListviewSelectedItemListener())
        spinnerLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Log.i("Level Selected",mlevelList[position])
            }
        }

    }

    fun onFilterButtonClick(){
        val filter_intent = Intent(this, AddCourse_Activity::class.java)
        val spinnerLevel = spinnerLevel.selectedItem.toString()

        val startTime  = (if (startTimePicker.hour == 0) "00" else startTimePicker.hour.toString()) + (if (startTimePicker.minute == 0) "00" else startTimePicker.minute.toString())
        val endTime    = (if (endTimePicker.hour == 0) "00" else endTimePicker.hour.toString()) + (if (endTimePicker.minute == 0) "00" else endTimePicker.minute.toString())

        val subjectList = arrayListOf<Subject>()

        (0 until subjectSpinnerItems.count())
                .filter { subjectSpinnerItems[it].mSelected }
                .mapTo(subjectList) { subjectSpinnerItems[it].mSubject }
        if(subjectList.count() == 0){
            (0 until subjectSpinnerItems.count()).mapTo(subjectList) { subjectSpinnerItems[it].mSubject }
        }

        val args = Bundle()
        args.putString("level",spinnerLevel)
        args.putString("start-time",startTime)
        args.putString("end-time",endTime)
        args.putSerializable("subjects", subjectList as Serializable)
        filter_intent.putExtra("bundle", args)
        startActivity(filter_intent)
    }

    inner private class CustomAdapter(context: Context, resource: Int) : ArrayAdapter<SubjectSpinnerItem>(context, resource) {


        private var isFromView = false
        private val mContext:Context = context

        override fun getDropDownView(position: Int, convertView: View?,
                                     parent: ViewGroup): View {
            var convertView = convertView

            val holder: ViewHolder
            if (convertView == null) {
                val layoutInflator = LayoutInflater.from(mContext)
                convertView = layoutInflator.inflate(R.layout.subject_spinner_item, null)
                holder = ViewHolder()
                holder.mTextView = convertView!!.findViewById<View>(R.id.text) as TextView

                holder.mCheckBox = convertView
                        .findViewById<View>(R.id.checkbox) as CheckBox
                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
            }

            holder.mTextView!!.setText(subjectSpinnerItems.get(position).mSubject.title)

            // To check weather checked event fire from getview() or user input
            isFromView = true
            holder.mCheckBox!!.isChecked = subjectSpinnerItems.get(position).mSelected
            isFromView = false
            holder.mCheckBox!!.visibility = View.VISIBLE
            holder.mCheckBox!!.tag = position
            holder.mCheckBox!!.setOnCheckedChangeListener { buttonView, isChecked ->
                val getPosition = buttonView.tag as Int
            }
            holder.mCheckBox!!.setOnClickListener(View.OnClickListener {
                subjectSpinnerItems.get(position).mSelected = !subjectSpinnerItems.get(position).mSelected
            })
            return convertView
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getCustomView(position, convertView, parent)
        }

        fun getCustomView(position: Int, convertView: View?,
                          parent: ViewGroup): View {
            var convertView = convertView

            val holder: ViewHolder
            if (convertView == null) {
                val layoutInflator = LayoutInflater.from(mContext)
                convertView = layoutInflator.inflate(R.layout.subject_spinner_item, null)
                holder = ViewHolder()
                holder.mTextView = convertView!!
                        .findViewById<View>(R.id.text) as TextView
                holder.mCheckBox = convertView
                        .findViewById<View>(R.id.checkbox) as CheckBox
                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
            }

            holder.mTextView!!.setText(subjectSpinnerItems.get(position).mSubject.title)

            // To check weather checked event fire from getview() or user input
            isFromView = true
            holder.mCheckBox!!.isChecked = subjectSpinnerItems.get(position).mSelected
            isFromView = false

            if (position == 0) {
                holder.mCheckBox!!.visibility = View.INVISIBLE
            } else {
                holder.mCheckBox!!.visibility = View.VISIBLE
            }
            holder.mCheckBox!!.tag = position
            holder.mCheckBox!!.setOnCheckedChangeListener { buttonView, isChecked ->
                val getPosition = buttonView.tag as Int
            }
            return convertView
        }

        public fun getSubjects() {
            val url = "https://bismarck.sdsu.edu/registration/subjectlist"
            val myStringRequest = object : JsonArrayRequest(Request.Method.GET, url, null,
                    Response.Listener { response ->
                        for (i in 0 until response.length()) {
                            val subject = Subject.getObjectFromJson(response.getJSONObject(i))
                            var subjectSpinnerItem = SubjectSpinnerItem(subject,false)
                            subjectSpinnerItems.add(subjectSpinnerItem)
                        }
                        this.addAll(subjectSpinnerItems)
                        spinnerSubject.adapter = this

                    },
                    Response.ErrorListener {
                        Toast.makeText(null, "Something is not right", Toast.LENGTH_SHORT)
                    }
            ) { }
            appContext.queue.add(myStringRequest)
            Log.i("message", subjectSpinnerItems.count().toString())

        }

        private inner class ViewHolder {
            var mTextView: TextView? = null
            var mCheckBox: CheckBox? = null
        }
    }

}

