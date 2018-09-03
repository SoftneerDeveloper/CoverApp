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
import java.util.List;
import java.util.Map;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.RunTimeTopUpActivity;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Balance;
import ke.co.coverapp.coverapp.pojo.Vehicle;
import ke.co.coverapp.coverapp.tasks.TaskLoadBalance;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.ACCESS_TOKEN;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.CURR;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.MY_DEFAULT_MAX_RETRIES;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.MY_SOCKET_TIMEOUT_MS;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.WALLET_BALANCE;

/**
 * Created by Clifford Owino on 4/6/2017.
 */

public class VehiclePurchaseFragment extends DialogFragment implements View.OnClickListener, BalanceLoadedListener{

    Spinner saved_cars, partners, payment_plan;
    private ProgressDialog mProgressDialog;
    Button button_purchase, button_cancel;
    TextView vehicle_cost, partner_data;
    List<Vehicle> list_cars;
    String car_selected, partner_selected, payment_plan_selected;
    int automate_billing = 1;
    Context context;
    int payment_plans = 0;
    int partner_id = 0;
    double low_percentage = 8.0;
    double high_percentage = 10.0;
    Float car_value = 0.F;
    String current_id ;
    String curr = MyApplication.readFromPreferences(MyApplication.getAppContext(), CURR, ValidationUtil.getDefaultCurr());
    String balance = MyApplication.readFromPreferences(MyApplication.getAppContext(), WALLET_BALANCE, ValidationUtil.getDefaultBalance());

    String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());

    Float final_price = 0.F;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.vehicle_custom_view_dialog, container);
    }

    public VehiclePurchaseFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead
    }

    public static VehiclePurchaseFragment newInstance() {
        VehiclePurchaseFragment frag = new VehiclePurchaseFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        saved_cars = (Spinner) view.findViewById(R.id.saved_cars);
        partners = (Spinner) view.findViewById(R.id.partners);
        payment_plan = (Spinner) view.findViewById(R.id.payment_plan);
        button_purchase = (Button) view.findViewById(R.id.button_purchase);
        button_cancel = (Button) view.findViewById(R.id.button_cancel);
        vehicle_cost = (TextView) view.findViewById(R.id.vehicle_cost);
        partner_data = (TextView) view.findViewById(R.id.partner_data);
        button_purchase.setOnClickListener(this);
        button_cancel.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Acquiring cover...");
        mProgressDialog.setCancelable(false);
        context = getActivity();
        getDialog().setCancelable(false);



        // creating adapters for the spinners and adding the data for each

        ArrayAdapter<CharSequence> period_spinner_adaptor = ArrayAdapter
                .createFromResource(MyApplication.getAppContext(), R.array.period_options,
                        R.layout.spinner_item);

        ArrayAdapter<CharSequence> partner_spinner_adaptor = ArrayAdapter
                .createFromResource(MyApplication.getAppContext(), R.array.partner_options,
                        R.layout.spinner_item);

        ArrayAdapter<String> cars_spinner_adaptor = new ArrayAdapter<>(getActivity(),  R.layout.spinner_item);

        list_cars = MyApplication.getWritableDatabase().readNonCoveredVehicle();

        if (!list_cars.isEmpty()) {

            for (int i = 0; i < list_cars.size(); i++) {
                Vehicle vehicle = list_cars.get(i);
                cars_spinner_adaptor.add(vehicle.getVehicle_make() + "(" + vehicle.getPlate_first()+""+ vehicle.getPlate_last() + ")");
//                car_value = Float.valueOf(vehicle.getCar_value());
            }
        }else{
            saved_cars.setVisibility(View.GONE);
            button_purchase.setVisibility(View.GONE);
            partners.setVisibility(View.GONE);
            payment_plan.setVisibility(View.GONE);
            vehicle_cost.setText("You either do not have vehicles saved within CoverApp or All your vehicles have been covered.");
        }

        //set the theme for the spinners

        period_spinner_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        partner_spinner_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cars_spinner_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //assigning adapters for the spinners

        partners.setAdapter(partner_spinner_adaptor);

        saved_cars.setAdapter(cars_spinner_adaptor);

        payment_plan.setAdapter(period_spinner_adaptor);


        //setting selection listeners for the spinners

        payment_plan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:

                        break;

                    case 1:
                        payment_plans = 1;
                        payment_plan_selected = "Monthly";
                        final_price = Float.valueOf(5);
                        vehicle_cost.setText(curr + " : 5 per month");
                        break;

                    case 2:
                        payment_plan_selected = "Quarterly";
                        payment_plans = 3;
                        final_price = Float.valueOf(15);
                        vehicle_cost.setText(curr + " : 15 per quarter");
                        break;

                    case 3:
                        payment_plans = 6;
                        payment_plan_selected = "Bi-Annually";
                        final_price = Float.valueOf(30);
    //                    final_price = Float.parseFloat(price_emergency) * 6;
                        vehicle_cost.setText(curr + " : 30 per half-year");
                        break;

                    case 4:
                        payment_plans = 12;
                        payment_plan_selected = "Annually";
                        final_price = Float.valueOf(60);
    //                    final_price = Float.parseFloat(price_emergency) * 12;
                        vehicle_cost.setText(curr + " : 60 per year");
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        saved_cars.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               Vehicle vehicle =  list_cars.get(position);
                L.m("vehicle "+ vehicle.getVehicle_make());
                current_id = vehicle.getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        partners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Hack
                partner_id = position;
                switch (position){
                    case 0:

                        break;

                    case 1:
                        payment_plans = 1;
                        final_price = Float.valueOf(5);
                        vehicle_cost.setText(curr + " : 5 per month");
                        break;

                    case 2:
                        payment_plan_selected = "Quarterly";
                        payment_plans = 3;
                        final_price = Float.valueOf(15);
                        vehicle_cost.setText(curr + " : 15 per quarter");
                        break;

                    case 3:
                        payment_plans = 6;
                        payment_plan_selected = "Bi-Annually";
                        final_price = Float.valueOf(30);
                        //                    final_price = Float.parseFloat(price_emergency) * 6;
                        vehicle_cost.setText(curr + " : 30 per half-year");
                        break;

                    case 4:
                        payment_plans = 12;
                        payment_plan_selected = "Annually";
                        final_price = Float.valueOf(60);
                        //                    final_price = Float.parseFloat(price_emergency) * 12;
                        vehicle_cost.setText(curr + " : 60 per year");
                        break;

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
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_cancel:
                this.dismiss();
                break;

            case R.id.button_purchase:
                this.dismiss();
                createDialog();
                break;

        }

    }

    private void topUpDialog() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

            builder2.setMessage("Your account balance is  " + curr + " " + balance + ", you will need to top up to continue ");
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

    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Your selected bundle costs  " + curr + " " + final_price+ " and your account balance ");

        builder.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
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
        StringRequest request = new StringRequest(Request.Method.POST,  "http://api.coverappke.com/purchase/vehicle", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.m(response);
                new TaskLoadBalance(VehiclePurchaseFragment.this).execute();
                JSONObject responceJson = null;
                String text = "";
                try {
                    responceJson = new JSONObject(response);

                    if (ParseUtil.contains(responceJson, "text")) {
                        text= responceJson.getString("text");

                    } else {

                        text= "You have successfully acquired a Vehicle cover, you will be contacted shortly";

                    }
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
                    //parsing the error
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

                map.put("vehicle_id", current_id);
                map.put("partner_id", partner_id+"");
                map.put("num_payments", payment_plans+"");
                map.put("signup_cost", final_price+"");
                map.put("automate_billing", String.valueOf(automate_billing));
                L.m(map.toString());
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

    /**
     * Called when the account balance has been loaded
     *
     * @param balanceData the balance in a single element arraylist
     */
    @Override
    public void onBalanceLoaded(ArrayList<Balance> balanceData) {
        Balance currentBalance = balanceData.get(0);
        MyApplication.saveToPreferences(MyApplication.getAppContext(), WALLET_BALANCE, currentBalance.getBalance());
    }
}


