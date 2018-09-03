package ke.co.coverapp.coverapp.services;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import static ke.co.coverapp.coverapp.pojo.Keys.keys;
import ke.co.coverapp.coverapp.utility.ValidationUtil;

/**
 * Created by Clifford Owino on 11/25/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    String access_token = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.ACCESS_TOKEN, ValidationUtil.getDefault());

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        L.m("Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persisting token to coverapp server.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        // Add custom implementation, as needed.
        StringRequest request = new StringRequest(Request.Method.POST, getString(R.string.baseUrl) + "/fbtoken", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("token", token);
                return parameters;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", "Bearer "+ access_token);
                return map;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                keys.MY_SOCKET_TIMEOUT_MS,
                keys.MY_DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(request);
    }
}
