package assignment4.classregistration.com

import org.json.JSONObject



/**
 * Created by schandiramani on 3/22/18.
 */

public class Student : JsonProcessor {
    var firstName: String = ""
    var lastName: String = ""
    var redId: String = ""
    var email: String = ""
    var password: String = ""

    constructor(firstName: String, lastName: String, redId: String, email: String, password: String) {
        this.firstName = firstName
        this.lastName = lastName
        this.redId = redId
        this.email = email
        this.password = password
    }

    override fun getJsonObject() : JSONObject {
        val mJsonObj : JSONObject = JSONObject()
        mJsonObj.put("firstname", firstName)
        mJsonObj.put("lastname", lastName)
        mJsonObj.put("redid", redId)
        mJsonObj.put("password", password)
        mJsonObj.put("email", email)
        return mJsonObj
    }
}
