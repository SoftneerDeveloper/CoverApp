package ke.co.coverapp.coverapp.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import ke.co.coverapp.coverapp.activities.ReportClaimsActivity;
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
import static ke.co.coverapp.coverapp.pojo.Keys.keys.CURR;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.EMAIL;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.FNAME;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.ID_NUMBER;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.PHONE_NUMBER;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.WALLET_BALANCE;

/**
 * Created by Clifford Owino on 9/14/2016.
 */

public class HomeFragment extends Fragment implements View.OnClickListener , BalanceLoadedListener {

    private ProgressDialog mProgressDialog;
  //  LinearLayout legacy_life_plan, emergency_bundle_linear, month_long_vehicle_insurance, custom_bundle_linear, vehicle_bundle_linear, home_bundle_linear, feedback_linear, coverapp_medical_cover, coverapp_travel_cover;
//     Button notifCount;
    private static String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());
    String fname = MyApplication.readFromPreferences(MyApplication.getAppContext(), FNAME, "CoverApp User");
    String phone = MyApplication.readFromPreferences(MyApplication.getAppContext(), PHONE_NUMBER, ValidationUtil.getDefault());
    String currency = MyApplication.readFromPreferences(MyApplication.getAppContext(), CURR, ValidationUtil.getDefault());
    String w_balance = MyApplication.readFromPreferences(MyApplication.getAppContext(), WALLET_BALANCE, ValidationUtil.getDefaultBalance());
    String email = MyApplication.readFromPreferences(MyApplication.getAppContext(), EMAIL, ValidationUtil.getDefault());
    String id_number = MyApplication.readFromPreferences(MyApplication.getAppContext(), ID_NUMBER, ValidationUtil.getDefault());
    private RelativeLayout claim,feedback,settings;
    private LinearLayout topup;
    private ImageView refresh,imgprofile;

    static int mNotifCount = 0;
    private CardView products,covers,profile,wallet;
    private TextView txtprofile,balance;
    private Animation animation;



    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        products=(CardView) view.findViewById(R.id.products);
        covers=(CardView) view.findViewById(R.id.covers);
        wallet=(CardView) view.findViewById(R.id.wallet);
        profile=(CardView) view.findViewById(R.id.profile);
        topup=(LinearLayout) view.findViewById(R.id.topup);
        refresh=(ImageView) view.findViewById(R.id.refresh);
        imgprofile=(ImageView) view.findViewById(R.id.imgprofile);

        txtprofile=(TextView) view.findViewById(R.id.txtprofile);
        balance=(TextView) view.findViewById(R.id.balance);

        claim=(RelativeLayout) view.findViewById(R.id.claim);
        feedback=(RelativeLayout) view.findViewById(R.id.feedback);
        settings=(RelativeLayout) view.findViewById(R.id.settings);

        balance.setText("KES " + w_balance);
        txtprofile.setText("Hey " + fname);

        products.setOnClickListener(this);
        covers.setOnClickListener(this);
        wallet.setOnClickListener(this);
        profile.setOnClickListener(this);
        claim.setOnClickListener(this);
        feedback.setOnClickListener(this);
        settings.setOnClickListener(this);
        refresh.setOnClickListener(this);
        imgprofile.setOnClickListener(this);
        topup.setOnClickListener(this);

         animation= new RotateAnimation(0.0f,360.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(500);

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
        FragmentTransaction t = this.getFragmentManager().beginTransaction();
        Fragment mFrag = null;
        String tag = "";
        switch (v.getId()) {
            case R.id.covers:
                // TODO: Reactivate when process is finalized
                mFrag= new CoversFragment();
                tag = "CoversFragment";
                break;

            case R.id.products:
                // TODO: Reactivate when process is finalized
                mFrag = new ProductsFragment();
                tag = "ProductsFragment";
                break;

            case R.id.profile:
                // TODO: Reactivate when process is finalized
                mFrag = new AssetsFragmentTwo();
                tag = "AssetsFragmentTwo";
                break;
            case  R.id.imgprofile:
                // TODO: Reactivate when process is finalized
                mFrag = new AssetsFragmentTwo();
                tag = "AssetsFragmentTwo";
                break;
            case R.id.wallet:
                // TODO: Reactivate when process is finalized
//                startActivity(new Intent(MyApplication.getAppContext(), .class));
//                L.t(getActivity(), "Coming soon");
                break;
            case R.id.claim:
                // TODO: Reactivate when process is finalized
                startActivity(new Intent(MyApplication.getAppContext(), ReportClaimsActivity.class));
//                L.t(getActivity(), "Coming soon");
                break;
            case R.id.settings:
                // TODO: Reactivate when process is finalized
                 mFrag = new SettingsFragment();
                tag = "SettingsFragment";
                break;
            case R.id.topup:
                // TODO: Check if a user has completed their profile first. If they have, allow them to proceed. Call the API.
                 mFrag = new TopUpFragment();
                tag = "TopUpFragment";
                mProgressDialog.hide();

                break;
            case R.id.feedback:
                startActivity(new Intent(MyApplication.getAppContext(), FeedbackActivity.class));
                break;
            case R.id.refresh:
                refresh.setAnimation(animation);
                refreshBalance();
                break;
        }

        if (mFrag != null) {
            t.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            t.replace(R.id.content_frame, mFrag, tag);
            t.addToBackStack(tag);
            t.commit();

            //setActionBarTitle(title);
        } else {
            L.T(MyApplication.getAppContext(), getString(R.string.under_maintenance));
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
    private void refreshBalance() {
        L.t(MyApplication.getAppContext(), "Refreshing balance");
        new TaskLoadBalance(this).execute();
        refresh.clearAnimation();
        Toast.makeText(getContext(),"Balance was updated ",Toast.LENGTH_LONG).show();

    }
}
