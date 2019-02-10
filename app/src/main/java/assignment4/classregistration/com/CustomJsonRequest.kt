package assignment4.classregistration.com

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;





class CustomJsonRequest(method: Int, url: String, private val mRequestObject: JSONObject?, private val mResponseListener: Response.Listener<JSONArray>, errorListener: Response.ErrorListener)
    : JsonRequest<JSONArray>(method, url, mRequestObject?.toString(), mResponseListener, errorListener) {

    override fun deliverResponse(response: JSONArray) {
        mResponseListener.onResponse(response)
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONArray> {
        try {
            val json = String(response.data)

            //val json = String(response.data, HttpHeaderParser.parseCharset(response.headers))
            try {
                return Response.success(JSONArray(json),
                        HttpHeaderParser.parseCacheHeaders(response))
            } catch (e: JSONException) {
                return Response.error(ParseError(e))
            }

        } catch (e: UnsupportedEncodingException) {
            return Response.error(ParseError(e))
        }

    }
}