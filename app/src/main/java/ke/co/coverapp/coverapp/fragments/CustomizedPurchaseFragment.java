package ke.co.coverapp.coverapp.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.RunTimeTopUpActivity;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Balance;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.tasks.TaskLoadBalance;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.*;

/**
 * Created by Nick
 */
public class CustomizedPurchaseFragment extends DialogFragment implements View.OnClickListener, BalanceLoadedListener{

    Spinner period_options_spinner;
    Button button_cancel, button_accept;
    Context context;
    private ProgressDialog mProgressDialog;
    int payment_plan = 0;
    String payment_plan_selected, map;
    Float final_price = 0.F;
    TextView premium_cost;

    String curr = MyApplication.readFromPreferences(MyApplication.getAppContext(), CURR, ValidationUtil.getDefaultCurr());
    String balance = MyApplication.readFromPreferences(MyApplication.getAppContext(), WALLET_BALANCE, ValidationUtil.getDefaultBalance());
    String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());
    float charge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.customized_purchase_view_dialog, container);
    }

    public CustomizedPurchaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Getting parameters from parent activity
        charge = getArguments().getFloat("charge");
        map = getArguments().getString("map");

        button_cancel = (Button) view.findViewById(R.id.button_cancel);
        button_accept = (Button) view.findViewById(R.id.button_accept);
        premium_cost = (TextView) view.findViewById(R.id.premium_cost);

        button_cancel.setOnClickListener(this);
        button_accept.setOnClickListener(this);

        context = getActivity();
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Acquiring cover...");
        mProgressDialog.setCancelable(false);

        getDialog().setCancelable(false);

        period_options_spinner = (Spinner) view.findViewById(R.id.period_options_spinner);

        ArrayAdapter<CharSequence> period_spinner_adapter = ArrayAdapter
                .createFromResource(MyApplication.getAppContext(), R.array.period_options,
                        R.layout.spinner_item);

        period_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        period_options_spinner.setAdapter(period_spinner_adapter);

        period_options_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //  <item>Monthly</item>
                    // Default to the monthly payment plan
                    payment_plan_selected = "Monthly";
                    payment_plan = 1;
                    final_price = charge;
                    premium_cost.setText(curr + " : " + final_price + " per month");
                }else if (position == 1) {
                    payment_plan_selected = "Monthly";
                    payment_plan = 1;
                    final_price = charge;
                    premium_cost.setText(curr + " : " + final_price + " per month");
                } else if (position == 2) {
                    payment_plan_selected = "Quarterly";
                    payment_plan = 3;
                    final_price = charge * 3;
                    premium_cost.setText(curr + " : " + final_price + " per quarter");
                } else if (position == 3) {
                    payment_plan = 6;
                    payment_plan_selected = "Bi-Annually";
                    final_price = charge * 6;
                    premium_cost.setText(curr + " : " + final_price + " per half-year");

                } else if (position == 4) {
                    payment_plan = 12;
                    payment_plan_selected = "Annually";
                    final_price = charge * 12;
                    premium_cost.setText(curr + " : " + final_price + " per year");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_cancel:
                this.dismiss();
                break;

            case R.id.button_accept:
                this.dismiss();
                createDialog();
                break;
        }
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Your selected bundle costs  " + curr + " " + final_price);

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                // Recheck user balance
                balanceCheck();
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

    private void balanceCheck() {
        AlertDialog.Builder builderTwo = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        if (Float.parseFloat(balance) <= final_price) {
            builderTwo.setMessage("Your account balance is " + curr + " " + balance + ", you will need to top up to continue ");
            builderTwo.setPositiveButton("Top up", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    context.startActivity(new Intent(context, RunTimeTopUpActivity.class));
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).show();
        } else {
            // Proceed with purchase
            initiatePurchase();
        }
    }

    private void initiatePurchase() {
        mProgressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, "http://api.coverappke.com/bundle/purchase", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.m(response);

                // Display in long period of time
                // Toast.makeText(getApplicationContext(), "This a toast", Toast.LENGTH_LONG).show();

                new TaskLoadBalance(CustomizedPurchaseFragment.this).execute();
                JSONObject responseJson = null;

                try {
                    responseJson = new JSONObject(response);

                    if (ParseUtil.contains(responseJson, "text")) {
                        L.t(MyApplication.getAppContext(), responseJson.getString("text"));

                    } else {
                        L.t(MyApplication.getAppContext(), "You have successfully acquired the customized bundle, you will be contacted shortly");

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

                NetworkResponse response = error.networkResponse;

                L.m("Error");

                if (response != null && response.data != null) {
                    // Parsing the error
                    String json = "";
                    JSONObject obj;
                    switch (response.statusCode) {
                        case 400:
                            json = new String(response.data);
                            L.m(json);

                            try {
                                obj = new JSONObject(json);

                                if (ParseUtil.contains(obj, "error")) {
                                    L.t(MyApplication.getAppContext(), obj.getString("error"));
                                } else {
                                    L.t(MyApplication.getAppContext(), "Error acquiring bundle, please retry");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 403:
                            json = new String(response.data);
                            L.m(json);

                            try {
                                obj = new JSONObject(json);

                                if (ParseUtil.contains(obj, "error")) {
                                    L.t(MyApplication.getAppContext(), obj.getString("error"));
                                    L.m(obj.toString());
                                } else {
                                    L.t(MyApplication.getAppContext(), "Authentication error, please retry");
                                    L.m(obj.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

//                            L.t(MyApplication.getAppContext(), "An error occurred, please try again");
                            break;
                        default:
                            VolleyCustomErrorHandler.errorMessage(error);
                            break;
                    }
                } else {
                    VolleyCustomErrorHandler.errorMessage(error);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                parameters.put("package", "customized_cover");
                parameters.put("unit_code", "complete_plan");
                parameters.put("product", map);
                parameters.put("num_payments", Integer.toString(payment_plan));
                parameters.put("signup_cost", Float.toString(final_price));
//                map.put("automate_billing", String.valueOf(automate_billing));

                L.m(parameters.toString());
                return parameters;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                Keys.keys.MY_SOCKET_TIMEOUT_MS,
                Keys.keys.MY_DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(request);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onBalanceLoaded(ArrayList<Balance> balanceData) {
        if (balanceData.size() > 0) {
            Balance currentBalance = balanceData.get(0);
            MyApplication.saveToPreferences(MyApplication.getAppContext(), WALLET_BALANCE, currentBalance.getBalance());
            L.t(MyApplication.getAppContext(), "Balance updated successfully");
        }
    }
}
