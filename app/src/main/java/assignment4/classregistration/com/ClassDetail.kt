package assignment4.classregistration.com

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject
import java.io.Serializable

/**
 * Created by schandiramani on 3/26/18.
 */
class ClassDetail : JsonProcessor, Serializable {

    var description : String = ""
    var department : String  = ""
    var suffix : String = ""
    var building : String = ""
    var startTime: String = ""
    var meetingType: String = ""
    var section: String = ""
    var endTime: String = ""
    var enrolled: String = ""
    var days: String = ""
    var prerequisite: String = ""
    var title: String = ""
    var id: String = ""
    var instructor: String = ""
    var schedule : String = "" //schedule#
    var units: String = ""
    var room: String = ""
    var waitlist: String = ""
    var seats: String = ""
    var fullTitle: String = ""
    var subject: String = ""
    var course: String = "" //course#

    constructor(title : String, id : String){
        this.title = title
        this.id = id
    }



    override fun getJsonObject(): JSONObject {
        val mJsonObj : JSONObject = JSONObject()
        mJsonObj.put("description", description)
        mJsonObj.put("department", department)
        mJsonObj.put("suffix", suffix)
        mJsonObj.put("building", building)
        mJsonObj.put("startTime", startTime)
        mJsonObj.put("meetingType",meetingType )
        mJsonObj.put("section", section)
        mJsonObj.put("endTime", endTime)
        mJsonObj.put("enrolled", enrolled)
        mJsonObj.put("days", days)
        mJsonObj.put("prerequisite", prerequisite)
        mJsonObj.put("title", title)
        mJsonObj.put("id", id)
        mJsonObj.put("instructor", instructor)
        mJsonObj.put("schedule#", schedule)
        mJsonObj.put("units", units)
        mJsonObj.put("room", room)
        mJsonObj.put("waitlist", waitlist)
        mJsonObj.put("seats", seats)
        mJsonObj.put("fullTitle", fullTitle)
        mJsonObj.put("subject", subject)
        mJsonObj.put("course#", course)

        return mJsonObj
    }

    companion object {
        fun getObjectFromJson(jsonObject: JSONObject): ClassDetail {
            var classDetail = ClassDetail(jsonObject["title"].toString(),jsonObject["id"].toString())

            classDetail.description =  if ( jsonObject.has("description")) jsonObject["description"].toString() else  ""

            classDetail.department = if ( jsonObject.has("department")) jsonObject["department"].toString() else  ""

            classDetail.suffix =  if ( jsonObject.has("suffix")) jsonObject["suffix"].toString() else  ""

            classDetail.building =  if ( jsonObject.has("building")) jsonObject["building"].toString() else  ""

            classDetail.startTime =  if ( jsonObject.has("startTime")) jsonObject["startTime"].toString() else  ""

            classDetail.meetingType =  if ( jsonObject.has("meetingType")) jsonObject["meetingType"].toString() else  ""

            classDetail.section =  if ( jsonObject.has("section")) jsonObject["section"].toString() else  ""

            classDetail.endTime =  if ( jsonObject.has("endTime")) jsonObject["endTime"].toString() else  ""

            classDetail.enrolled =  if ( jsonObject.has("enrolled")) jsonObject["enrolled"].toString() else  ""

            classDetail.days =  if ( jsonObject.has("days")) jsonObject["days"].toString() else  ""

            classDetail.prerequisite =  if ( jsonObject.has("prerequisite")) jsonObject["prerequisite"].toString() else  ""

            classDetail.instructor =  if ( jsonObject.has("instructor")) jsonObject["instructor"].toString() else  ""

            classDetail.schedule =  if ( jsonObject.has("schedule#")) jsonObject["schedule#"].toString() else  ""

            classDetail.units =  if ( jsonObject.has("units")) jsonObject["units"].toString() else  ""

            classDetail.room =  if ( jsonObject.has("room")) jsonObject["room"].toString() else  ""

            classDetail.waitlist =  if ( jsonObject.has("waitlist")) jsonObject["waitlist"].toString() else  ""

            classDetail.seats =  if ( jsonObject.has("seats")) jsonObject["seats"].toString() else  ""

            classDetail.fullTitle =  if ( jsonObject.has("fullTitle")) jsonObject["fullTitle"].toString() else  ""

            classDetail.subject =  if ( jsonObject.has("subject")) jsonObject["subject"].toString() else  ""

            classDetail.course =  if ( jsonObject.has("course#")) jsonObject["course#"].toString() else  ""

            return classDetail
        }
    }

}