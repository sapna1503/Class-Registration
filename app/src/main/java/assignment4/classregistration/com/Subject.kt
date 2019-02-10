package assignment4.classregistration.com

import org.json.JSONObject
import java.io.Serializable

/**
 * Created by schandiramani on 3/25/18.
 */
public class Subject : JsonProcessor, Serializable {
    var title: String = ""
    var id: Int = 0
    var college: String = ""
    var classes: Int = 0


    constructor(title: String, id: Int, college: String, classes: Int) {
        this.title = title
        this.id = id
        this.college = college
        this.classes = classes

    }

    override fun getJsonObject() : JSONObject {
        val mJsonObj : JSONObject = JSONObject()
        mJsonObj.put("title", title)
        mJsonObj.put("id", id)
        mJsonObj.put("college", college)
        mJsonObj.put("classes", classes)
        return mJsonObj
    }

    companion object {
        fun getObjectFromJson(jsonObject: JSONObject): Subject {
            val title: String = jsonObject["title"].toString()
            val id: Int = Integer.parseInt(jsonObject["id"].toString())
            val college: String = jsonObject["college"].toString()
            val classes: Int = Integer.parseInt(jsonObject["classes"].toString())
            val subject: Subject = Subject(title, id, college, classes)
            return subject
        }
    }

}