package ke.co.coverapp.coverapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.MY_DEFAULT_MAX_RETRIES;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.MY_SOCKET_TIMEOUT_MS;

public class PostAlertActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout layout_message, layout_location;
    TextInputEditText message, location;
    AppCompatButton send_alert;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_alert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        layout_message = (TextInputLayout) findViewById(R.id.layout_message);
        layout_location = (TextInputLayout) findViewById(R.id.layout_location);
        message = (TextInputEditText) findViewById(R.id.message);
        location = (TextInputEditText) findViewById(R.id.location);
        send_alert = (AppCompatButton) findViewById(R.id.send_alert);
        send_alert.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Sending Alert To Community...");
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.send_alert:
                if (validateField(layout_message, message, 3) && validateField(layout_location, location, 4)){
                    createDialog();
                }
        }
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Post alert to coverApp community? ");

        builder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                initiatePostAlert();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



    private boolean validateField(TextInputLayout the_layout, TextInputEditText the_input, int required_length) {
        if (!ValidationUtil.hasValidContents(the_input) || the_input.getText().toString().trim().length()<required_length) {
            the_layout.setErrorEnabled(true);
            the_layout.setError(getString(R.string.invalid_content));
            requestFocus(the_input);

            return false;
        }
        the_layout.setErrorEnabled(false);
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void initiatePostAlert() {
        mProgressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, getString(R.string.baseUrl) + "/alert ", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.m(response);

                JSONObject responceJson = null;
                try {
                    responceJson = new JSONObject(response);
                    if (ParseUtil.contains(responceJson, "text")) {
                        L.t(MyApplication.getAppContext(), responceJson.getString("text"));

                    } else {
                        L.t(MyApplication.getAppContext(), "You have successfully posted your alert, Thank you");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mProgressDialog.hide();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.hide();

                    VolleyCustomErrorHandler.errorMessage(error);


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("message", message.getText().toString().trim());
                parameters.put("location", location.getText().toString().trim());
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                MY_DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(request);
    }


}
