package ke.co.coverapp.coverapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.AddAssetActivity;
import ke.co.coverapp.coverapp.activities.HomeBundleActivity;
import ke.co.coverapp.coverapp.adapters.CoversAdapter;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.CoversLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleyMultipartRequest;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Covers;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.tasks.TaskLoadCovers;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.ACCESS_TOKEN;

public class CoversFragment extends Fragment implements View.OnClickListener, CoversLoadedListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ProgressDialog mProgressDialog;
    private static String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());
    Button buyCoverFragmentCovers;

    private ArrayList<Covers> coversList = new ArrayList<>();
    private CoversAdapter coversAdapter;
    RecyclerView coversRecyclerView;
    LinearLayout coverFragmentError;

    private static final String STATE_ACTIVITY = "state_activity";

    public CoversFragment() {
        // Required empty public constructor
    }

    public static CoversFragment newInstance(String param1, String param2) {
        CoversFragment fragment = new CoversFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_covers, container, false);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Getting purchased covers...");

        buyCoverFragmentCovers = (Button) view.findViewById(R.id.buyCoverFragmentCovers);
        buyCoverFragmentCovers.setOnClickListener(this);

        coverFragmentError = (LinearLayout) view.findViewById(R.id.coverFragmentError);

        // Adapter & recyclerView
        coversRecyclerView = (RecyclerView) view.findViewById(R.id.coversRecyclerView);
        coversAdapter = new CoversAdapter(getActivity(), coversList);
        coversRecyclerView.setAdapter(coversAdapter);
        coversRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Load covers
        //TODO uncomment by joshua
       // new TaskLoadCovers(this).execute();

        // Storing covers in array list
        //coversList = MyApplication.getWritableDatabase().readCovers();

        if (coversList == null || coversList.isEmpty()) {
            new TaskLoadCovers(this).execute();
        }
       //TODO uncomment by joshua
        //coversList = MyApplication.getWritableDatabase().readCovers();

        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change, load the existing activity from a parcelable
            coversList = savedInstanceState.getParcelableArrayList(STATE_ACTIVITY);

        } else {
            //if this fragment starts for the first time, load the list of activities from a database
            coversList = MyApplication.getWritableDatabase().readCovers();
            //if the database is empty, trigger an AsyncTask to download activity list from the API
            if (coversList.isEmpty()) {
                new TaskLoadCovers(this).execute();
                coversList = MyApplication.getWritableDatabase().readCovers();
            }
        }

        // Show error or not
        if (coversList != null && !coversList.isEmpty()){
            coverFragmentError.setVisibility(View.GONE);
        }

        // Update adapter with new covers list
        coversAdapter.setCoversList(coversList);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void coverDialog(String message) {
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

    // Get covers purchased
    public void getCovers() {
        // Make API call to check for user profile details
        mProgressDialog.show();
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, "http://api.coverappke.com/get/claims", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse asset) {
                // Take to next page
                L.m("Successful retrieval of covers");

                //

                mProgressDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Show error dialog
                String message = "No covers tied to your account. Kindly purchase a cover.";
                L.m("Error Profile Details");

                // TODO: Return appropriate error e.g. network error etc
                NetworkResponse response = error.networkResponse;

                if (response != null) {

                    switch (response.statusCode) {
                        case 400:
                            message = "No covers tied to your account. Kindly purchase a cover.";
                            coverDialog(message);

                            break;

                        case 403:
                            message = "There was an error getting your covers. Kindly retry later. Contact support if this issue persists.";
                            coverDialog(message);

                            break;

                        default:
                            VolleyCustomErrorHandler.errorMessage(error);
                            message = "An error occurred while fetching your covers. Kindly retry later.";
                            coverDialog(message);

                            break;
                    }
                } else {
                    VolleyCustomErrorHandler.errorMessage(error);
                    message = "Please check your internet connection and retry.";
                    coverDialog(message);

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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buyCoverFragmentCovers:
                // Go to claim activity
                checkProfile();

                break;

            default:
                break;
        }
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
    public void onCoversLoadedListener(ArrayList<Covers> covers) {
        L.m("Covers loaded.");

        if (covers != null) {
            if (!covers.isEmpty()) {
                coversAdapter.addToCoversList(covers);
                coversAdapter.notifyDataSetChanged();

                // Remove error
                coverFragmentError.setVisibility(View.GONE);

                L.t(getActivity(), "Covers loaded.");
            }
        }
    }
}
