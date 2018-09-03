package ke.co.coverapp.coverapp.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ke.co.coverapp.coverapp.activities.IceaTravelCover;
import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.FeedbackActivity;
import ke.co.coverapp.coverapp.activities.HomeBundleActivity;
import ke.co.coverapp.coverapp.activities.LegacyLifePlan;
import ke.co.coverapp.coverapp.activities.MainActivity;
import ke.co.coverapp.coverapp.activities.NotificationActivity;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleyMultipartRequest;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Balance;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.tasks.TaskLoadBalance;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.ACCESS_TOKEN;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.WALLET_BALANCE;

/**
 * Created by Clifford Owino on 9/14/2016.
 */

public class ProductsFragment extends Fragment implements View.OnClickListener , BalanceLoadedListener {

    private ProgressDialog mProgressDialog;
    LinearLayout legacy_life_plan, emergency_bundle_linear, month_long_vehicle_insurance, custom_bundle_linear, vehicle_bundle_linear, home_bundle_linear, feedback_linear, coverapp_medical_cover, coverapp_travel_cover;
    //     Button notifCount;
    private static String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());

    static int mNotifCount = 0;

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        emergency_bundle_linear = (LinearLayout) view.findViewById(R.id.emergency_bundle_linear);
        custom_bundle_linear = (LinearLayout) view.findViewById(R.id.custom_bundle_linear);
        vehicle_bundle_linear = (LinearLayout) view.findViewById(R.id.vehicle_bundle_linear);
        home_bundle_linear = (LinearLayout) view.findViewById(R.id.home_bundle_linear);
        feedback_linear = (LinearLayout) view.findViewById(R.id.feedback_linear);
        coverapp_medical_cover= (LinearLayout) view.findViewById(R.id.coverapp_medical_cover);
        coverapp_travel_cover= (LinearLayout) view.findViewById(R.id.coverapp_travel_cover);
        month_long_vehicle_insurance= (LinearLayout) view.findViewById(R.id.month_long_vehicle_insurance);
        legacy_life_plan =  (LinearLayout) view.findViewById(R.id.legacy_life_plan);

        emergency_bundle_linear.setOnClickListener(this);
        custom_bundle_linear.setOnClickListener(this);
        vehicle_bundle_linear.setOnClickListener(this);
        home_bundle_linear.setOnClickListener(this);
        feedback_linear.setOnClickListener(this);
        coverapp_travel_cover.setOnClickListener(this);
        coverapp_medical_cover.setOnClickListener(this);
        month_long_vehicle_insurance.setOnClickListener(this);
        legacy_life_plan.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Checking profile details...");

        return view;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.emergency_bundle_linear:
                // TODO: Reactivate when process is finalized
//                startActivity(new Intent(MyApplication.getAppContext(), EmergencyBundleActivity.class));
                L.t(getActivity(), "Coming soon");
                break;

            case R.id.month_long_vehicle_insurance:
                // TODO: Reactivate when process is finalized
                startActivity(new Intent(MyApplication.getAppContext(), MonthLongVehicleInsurance.class));
                // L.t(getActivity(), "Coming soon");
                break;

            case R.id.legacy_life_plan:
                // TODO: Reactivate when process is finalized
                startActivity(new Intent(MyApplication.getAppContext(), LegacyLifePlan.class));
                // L.t(getActivity(), "Coming soon");
                break;

            case R.id.coverapp_medical_cover:
                // TODO: Reactivate when process is finalized
//                startActivity(new Intent(MyApplication.getAppContext(), EmergencyBundleActivity.class));
                L.t(getActivity(), "Coming soon");
                break;
            case R.id.coverapp_travel_cover:
                // TODO: Reactivate when process is finalized
                startActivity(new Intent(MyApplication.getAppContext(), IceaTravelCover.class));
//                L.t(getActivity(), "Coming soon");
                break;
            case R.id.custom_bundle_linear:
                // TODO: Reactivate when process is finalized
//                startActivity(new Intent(MyApplication.getAppContext(), CustomBundleActivity.class));
                L.t(getActivity(), "Coming soon");
                break;
            case R.id.vehicle_bundle_linear:
                // TODO: Reactivate when process is finalized
//                startActivity(new Intent(MyApplication.getAppContext(), VehicleBundleActivity.class));
                L.t(getActivity(), "Coming soon");
                break;
            case R.id.home_bundle_linear:
                // TODO: Check if a user has completed their profile first. If they have, allow them to proceed. Call the API.
//                if(checkProfile()) {
//                    // If true
//                    startActivity(new Intent(MyApplication.getAppContext(), HomeBundleActivity.class));
//                } else {
//                    // If false
////                    L.T(getContext(), "Please complete your profile first to proceed.");
//                    profileDialog();
//                }
                // checkProfile();
                startActivity(new Intent(MyApplication.getAppContext(), HomeBundleActivity.class));
                mProgressDialog.hide();

                break;
            case R.id.feedback_linear:
                startActivity(new Intent(MyApplication.getAppContext(), FeedbackActivity.class));
                break;
        }

    }

    private void profileDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);

        builder.setMessage(message);

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ((MainActivity) getActivity()).createDialog();
            return true;
        }

        if (id == R.id.action_refresh) {
            L.t(MyApplication.getAppContext(), "Refreshing balance");
            new TaskLoadBalance(this).execute();
            return true;
        }

        if (id == R.id.action_settings) {
            ((MainActivity) getActivity()).displayView(R.id.nav_settings);
            return true;
        }

        if (id == R.id.action_notification) {
            startActivity(new Intent(MyApplication.getAppContext(), NotificationActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkProfile(){
        // Make API call to check for user profile details
        mProgressDialog.show();
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, "http://api.coverappke.com/user/profile/check", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse asset) {
                // Take to next page
                L.m("Success Profile Details");
                startActivity(new Intent(MyApplication.getAppContext(), HomeBundleActivity.class));
                mProgressDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Show error dialog
                String message = "Please fill in the missing details of your profile first to proceed.";
                L.m("Error Profile Details");

                // TODO: Return appropriate error e.g. network error etc
                NetworkResponse response = error.networkResponse;

                if (response != null) {

                    switch (response.statusCode) {
                        case 400:
                            message = "Please fill in the missing details of your profile first to proceed.";
                            profileDialog(message);

                            break;

                        case 403:
                            message = "There was an error getting you profile details. Kindly retry.";
                            profileDialog(message);

                            break;

                        default:
                            VolleyCustomErrorHandler.errorMessage(error);
                            message = "An error occurred checking for your profile details. Kindly retry.";
                            profileDialog(message);

                            break;
                    }
                } else {
                    VolleyCustomErrorHandler.errorMessage(error);
                    message = "Check your internet connection and retry.";
                    profileDialog(message);

                }

                mProgressDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
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
