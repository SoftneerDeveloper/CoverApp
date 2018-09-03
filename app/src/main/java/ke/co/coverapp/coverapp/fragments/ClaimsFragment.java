package ke.co.coverapp.coverapp.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.LegacyLifeClaims;
import ke.co.coverapp.coverapp.activities.MainActivity;
import ke.co.coverapp.coverapp.activities.ReportClaimsActivity;
import ke.co.coverapp.coverapp.activities.ThirdPartyMotorClaims;
import ke.co.coverapp.coverapp.activities.TravelInsuranceClaims;
import ke.co.coverapp.coverapp.adapters.ClaimsAdapter;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.ClaimsLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleyMultipartRequest;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Claims;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.tasks.TaskLoadClaims;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.ACCESS_TOKEN;


public class ClaimsFragment extends Fragment implements View.OnClickListener, ClaimsLoadedListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    Button btn_to_claim, ButtonClaimChosen;
    RadioButton radioButtonHomeContents, radioButtonMotor3rd, radioButtonLegacyLife, radioButtonTravelCover;
    RadioGroup radioGroup1;

    private LocationManager locationManager;

    private ClaimsAdapter claimsAdapter;
    private ArrayList<Claims> claimsList = new ArrayList<>();
    RecyclerView claimsRecyclerView;
    private ProgressDialog mProgressDialog;
    private static String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());
    LinearLayout claimsFragmentError, choose_claim_layout;

    private static final String STATE_ACTIVITY = "state_activity";

    public ClaimsFragment() {
        // Required empty public constructor
    }

    public static ClaimsFragment newInstance(String param1, String param2) {
        ClaimsFragment fragment = new ClaimsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_claims, container, false);
        btn_to_claim = (Button) view.findViewById(R.id.btn_to_claim);
        btn_to_claim.setOnClickListener(this);
        ButtonClaimChosen= (Button) view.findViewById(R.id.ButtonClaimChosen);
        ButtonClaimChosen.setOnClickListener(this);


        radioButtonHomeContents= (RadioButton) view.findViewById(R.id.radioButtonHomeContents);
        radioButtonMotor3rd= (RadioButton) view.findViewById(R.id.radioButtonMotor3rd);
        radioButtonLegacyLife= (RadioButton) view.findViewById(R.id.radioButtonLegacyLife);
        radioButtonTravelCover= (RadioButton) view.findViewById(R.id.radioButtonTravelCover);
        radioGroup1= (RadioGroup ) view.findViewById(R.id.radioGroup1);

        claimsFragmentError = (LinearLayout) view.findViewById(R.id.claimsFragmentError);

        // Load listener
        new TaskLoadClaims(this).execute();

        // Get claims
        claimsList = MyApplication.getWritableDatabase().readClaims();

        // Inflate the layout for this fragment
        claimsRecyclerView = (RecyclerView) view.findViewById(R.id.claimsRecyclerView);
        choose_claim_layout= (LinearLayout ) view.findViewById(R.id.choose_claim_layout);
        claimsAdapter = new ClaimsAdapter(getActivity(), claimsList);
        claimsRecyclerView.setAdapter(claimsAdapter);
        claimsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Checking covers...");

        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change, load the existing activity from a parcelable
            claimsList = savedInstanceState.getParcelableArrayList(STATE_ACTIVITY);

        } else {
            //if this fragment starts for the first time, load the list of activities from a database
            claimsList = MyApplication.getWritableDatabase().readClaims();
            //if the database is empty, trigger an AsyncTask to download activity list from the API
            if (claimsList.isEmpty()) {
                new TaskLoadClaims(this).execute();
                claimsList = MyApplication.getWritableDatabase().readClaims();
            }
        }

        // Show error or not
        if (claimsList != null && !claimsList.isEmpty()){
            claimsFragmentError.setVisibility(View.GONE);
        }

        // Update adapter with new covers list
        claimsAdapter.setClaimsList(claimsList);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
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
        if (id == R.id.action_settings) {
            ((MainActivity) getActivity()).displayView(R.id.nav_settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_to_claim:


                if (choose_claim_layout.getVisibility() == View.VISIBLE)
                {
                    choose_claim_layout.setVisibility(View.GONE);
                    btn_to_claim.setText("Report Claim");
                }
                else if (choose_claim_layout.getVisibility() == View.GONE)
                {
                    choose_claim_layout.setVisibility(View.VISIBLE);
                    btn_to_claim.setText("Cancel");
                }

                break;

            case R.id.ButtonClaimChosen:

                if (radioButtonHomeContents.isChecked())
                {  if(!isLocationEnabled()) {
                    showAlert();
                    //return isLocationEnabled();
                }
                else {

                    // Go to claim activity
                    L.m("Report claim button clicked");
                    checkClaimViability();
                }}
                else if(radioButtonMotor3rd.isChecked()){

                    startActivity(new Intent(MyApplication.getAppContext(), ThirdPartyMotorClaims.class));
                    Toast.makeText(getActivity(), "Third Party Motor",
                            Toast.LENGTH_LONG).show();

                }
                else if(radioButtonLegacyLife.isChecked()){

                    startActivity(new Intent(MyApplication.getAppContext(), LegacyLifeClaims.class));
                    Toast.makeText(getActivity(), "Legacy Life",
                            Toast.LENGTH_LONG).show();
                }
                else if(radioButtonTravelCover.isChecked()){

                    startActivity(new Intent(MyApplication.getAppContext(), TravelInsuranceClaims.class));
                    Toast.makeText(getActivity(), "Travel cover",
                            Toast.LENGTH_LONG).show();
                }
                else if (radioGroup1.getCheckedRadioButtonId() == -1)
                {
                    // no radio buttons are checked
                    Toast.makeText(getActivity(), "Select One!",
                            Toast.LENGTH_LONG).show();
                }
                else

                break;

            default:

                break;
        }

    }

    private void claimDialog(String message) {
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

    public void checkClaimViability()
    {
        // Check if user is allowed to make a claim
        mProgressDialog.show();
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, "http://api.coverappke.com/claim/check", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse asset) {
                // Take to next page
                L.m("Success - Check Claim Viability");
                Intent report_claim = new Intent(getActivity(), ReportClaimsActivity.class);
                getActivity().startActivity(report_claim);
                mProgressDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Show error dialog
                L.m("Error - Check Claim Viability");
                String message = "You are not allowed to make a claim yet. Please wait for the grace period to end. Contact support for more details.";

                // TODO: Return appropriate error e.g. network error etc
                NetworkResponse response = error.networkResponse;

                if (response != null) {

                    switch (response.statusCode) {
                        case 400:
                            message = "You are not allowed to make a claim yet. Please wait for the grace period to end. Contact support for more details.";
                            claimDialog(message);

                            break;

                        case 403:
                            message = "There was an error getting you claim details. Kindly retry.";
                            claimDialog(message);

                            break;

                        default:
                            VolleyCustomErrorHandler.errorMessage(error);
                            message = "An error occurred checking for your claim details. Kindly retry.";
                            claimDialog(message);

                            break;
                    }
                } else {
                    VolleyCustomErrorHandler.errorMessage(error);
                    message = "Check your internet connection and retry.";
                    claimDialog(message);

                }

//                mProgressDialog.hide();
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

    @Override
    public void onClaimsLoadedListener(ArrayList<Claims> claims) {

        if (claims != null) {
            if (!claims.isEmpty()) {
                L.m("Claims List: " + claims);
                claimsAdapter.addToClaimsList(claims);
                claimsAdapter.notifyDataSetChanged();

                // Remove error
                claimsFragmentError.setVisibility(View.GONE);

                L.t(getActivity(), "Claims loaded.");
            }
        }

    }

    //check if location is turned-on in current device
//    private boolean checkLocation() {
//        if(!isLocationEnabled())
//            showAlert();
//        return isLocationEnabled();
//    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    //end of check if location is turned-on in current device
}
