package ke.co.coverapp.coverapp.utility.network;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.log.L;

/**
 * Created by Clifford Owino on 9/14/2016.
 */
public class VolleyCustomErrorHandler {

    public static void errorMessage(VolleyError error){

        if(error instanceof TimeoutError){

            L.T(MyApplication.getAppContext(), "Your request has timed out. Please try again.");
            L.m("Request Timeout Error");

        } else if(error instanceof NoConnectionError){

            L.t(MyApplication.getAppContext(), "No connection currently. Check your data/wifi connection and retry.");
            L.m("No Connection Error");


        }else if(error instanceof NetworkError){

            L.t(MyApplication.getAppContext(), "Network error. Please try again.");
            L.m("Network Error");

        }else if(error instanceof ServerError){

            L.t(MyApplication.getAppContext(), "Error from our servers. Don't worry, retry after a few minutes.");
            L.m("Server Error");

        }else if(error instanceof ParseError){

            L.t(MyApplication.getAppContext(), "Parsing error encountered. Don't worry, retry after a few minutes.");
            L.m("Data Parse Error");

        }

    }
}
