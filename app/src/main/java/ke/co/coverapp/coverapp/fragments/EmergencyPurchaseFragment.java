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
import ke.co.coverapp.coverapp.activities.PaymentNotificationActivity;
import ke.co.coverapp.coverapp.activities.RunTimeTopUpActivity;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Balance;
import ke.co.coverapp.coverapp.tasks.TaskLoadBalance;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.*;

/**
 * Created by Clifford Owino on 4/3/2017.
 */

public class EmergencyPurchaseFragment extends DialogFragment implements View.OnClickListener, BalanceLoadedListener {

    Spinner period_options_spinner;
    Spinner partner_options_spinner;
    Button button_cancel, button_accept;
    private ProgressDialog mProgressDialog;
    String payment_plan_selected;
    int automate_billing = 1;
    TextView premium_cost;
    int payment_plan = 0;
    Context context;
    Float final_price = 0.F;
    static String price_emergency = MyApplication.readFromPreferences(MyApplication.getAppContext(), PRICE_EMERGENCY, ValidationUtil.getDefaultPrice());
    String curr = MyApplication.readFromPreferences(MyApplication.getAppContext(), CURR, ValidationUtil.getDefaultCurr());
    String balance = MyApplication.readFromPreferences(MyApplication.getAppContext(), WALLET_BALANCE, ValidationUtil.getDefaultBalance());

    String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.emergency_purchase_view_dialog, container);
    }

    public EmergencyPurchaseFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button_cancel = (Button) view.findViewById(R.id.button_cancel);
        button_accept = (Button) view.findViewById(R.id.button_accept);
        premium_cost = (TextView) view.findViewById(R.id.premium_cost);
        button_cancel.setOnClickListener(this);
        button_accept.setOnClickListener(this);

        context = getActivity();
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Acquiring cover...");
        mProgressDialog.setCancelable(false);

        getDialog().setCancelable(false);

        period_options_spinner = (Spinner) view.findViewById(R.id.period_options_spinner);

        ArrayAdapter<CharSequence> period_spinner_adaptor = ArrayAdapter
                .createFromResource(MyApplication.getAppContext(), R.array.period_options,
                        R.layout.spinner_item);

        period_spinner_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        period_options_spinner.setAdapter(period_spinner_adaptor);

        period_options_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //  <item>Monthly</item>
                    payment_plan = 0;
                    final_price = 0.0F;
                    premium_cost.setText(curr + " : 0.00 per month");
                }else if (position == 1) {
                    payment_plan_selected = "Monthly";
                    payment_plan = 1;
                    final_price = Float.parseFloat(price_emergency);
                    premium_cost.setText(curr + " : " + final_price + " per month");
                } else if (position == 2) {
                    payment_plan_selected = "Quarterly";
                    payment_plan = 3;
                    final_price = Float.parseFloat(price_emergency) * 3;
                    premium_cost.setText(curr + " : " + final_price + " per quarter");
                } else if (position == 3) {
                    payment_plan = 6;
                    payment_plan_selected = "Bi-Annually";
                    final_price = Float.parseFloat(price_emergency) * 6;
                    premium_cost.setText(curr + " : " + final_price + " per half-year");

                } else if (position == 4) {
                    payment_plan = 12;
                    payment_plan_selected = "Annually";
                    final_price = Float.parseFloat(price_emergency) * 12;
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

    private void topUpDialog() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        if (Float.parseFloat(balance) <= final_price) {

            builder2.setMessage("Your account balance is " + curr + " " + balance + ", you will need to top up to continue ");
            builder2.setPositiveButton("Top Up", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            context.startActivity(new Intent(context, RunTimeTopUpActivity.class));
                        }

                    }
            ).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        } else {
            purchaseProcess();
        }
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Your selected bundle costs  " + curr + " " + final_price);

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                // buyCover();
                priceCheck();
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

    private void priceCheck(){
        if (Float.parseFloat(balance) <= final_price) {
            topUpDialog();
            return;
        }
        billingRequest();
    }


    private void billingRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Automate billing of  " + curr + " " + final_price+ " "+payment_plan_selected+" from your wallet?");

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                automate_billing = 1;
                purchaseProcess();
            }
        });

        builder.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                automate_billing = 0;
                purchaseProcess();
            }
        });

        builder.show();
    }

    private void purchaseProcess() {
        mProgressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,  "http://api.coverappke.com/bundle/purchase", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.m(response);
                new TaskLoadBalance(EmergencyPurchaseFragment.this).execute();
                JSONObject responceJson = null;
                String text = "";
                try {
                    responceJson = new JSONObject(response);

                    if (ParseUtil.contains(responceJson, "text")) {
                        text= responceJson.getString("text");

                    } else {
                        text= "You have successfully acquired the emergency cover, you will be contacted shortly";

                    }
//                    context.startActivity(new Intent(MyApplication.getAppContext(), PaymentNotificationActivity.class));
                    L.t(MyApplication.getAppContext(), text);

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

                if (response != null && response.data != null) {
                    // Parsing the error
                    String json = "";
                    JSONObject obj;
                    switch (response.statusCode) {
                        case 400:
                            json = new String(response.data);

                            try {
                                obj = new JSONObject(json);

                                if (ParseUtil.contains(obj, "error")) {
                                    L.t(MyApplication.getAppContext(), obj.getString("error"));
                                } else {
                                    L.t(MyApplication.getAppContext(), "Something went wrong, please retry");
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
                Map<String, String> map = new HashMap<String, String>();

                map.put("package", "emergency_cover");
                map.put("unit_code", "complete_plan");
                map.put("product", "{roadside, home, ride_and_delivery, ambulance_rescue, security_backup, fire_rescue, water}");
                map.put("num_payments", String.valueOf(payment_plan));
                map.put("signup_cost", price_emergency);
                map.put("automate_billing", String.valueOf(automate_billing));
                return map;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                MY_DEFAULT_MAX_RETRIES,
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

    /**
     * Called when the account balance has been loaded
     *
     * @param balanceData the balance in a single element arraylist
     */
    @Override
    public void onBalanceLoaded(ArrayList<Balance> balanceData) {
        if (balanceData.size() > 0) {

            Balance currentBalance = balanceData.get(0);
            MyApplication.saveToPreferences(MyApplication.getAppContext(), WALLET_BALANCE, currentBalance.getBalance());
            L.t(MyApplication.getAppContext(), "Balance updated successfully");
        }
    }
}

