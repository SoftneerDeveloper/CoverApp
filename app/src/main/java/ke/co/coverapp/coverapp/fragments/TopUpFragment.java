package ke.co.coverapp.coverapp.fragments;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.MainActivity;
import ke.co.coverapp.coverapp.application.MyApplication;

import static android.content.Context.CLIPBOARD_SERVICE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.*;

import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Balance;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.tasks.TaskLoadBalance;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;


public class TopUpFragment extends Fragment implements View.OnClickListener, BalanceLoadedListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView airtel_8, mpesa_6;
    private CardView airtelCard,mpesaCard;
    private Button airtelGetTID, moreOptions, refresh_balance;
    private String mParam1;
    private LinearLayout buttonLayout;
    private String mParam2, confirmationDatastring, datastring, hashedString, sid, tid;
    private ProgressDialog mProgressDialog;
    private String country_code = "KE";
    private String vid = "coverappltd";
    TextView top_up_balance;
    ImageButton mpesa, visa, airtel;
    String oid = "OiD";
    String cbk = "";
    String mpesa_user = "";
    String amount_provided = "";
    String phone = MyApplication.readFromPreferences(MyApplication.getAppContext(), PHONE_NUMBER, ValidationUtil.getDefault());
    String currency = MyApplication.readFromPreferences(MyApplication.getAppContext(), CURR, ValidationUtil.getDefault());
    String w_balance = MyApplication.readFromPreferences(MyApplication.getAppContext(), WALLET_BALANCE, ValidationUtil.getDefaultBalance());
    String email = MyApplication.readFromPreferences(MyApplication.getAppContext(), EMAIL, ValidationUtil.getDefault());
    String id_number = MyApplication.readFromPreferences(MyApplication.getAppContext(), ID_NUMBER, ValidationUtil.getDefault());
    private static String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());
    //Remote configs
    boolean fromMpesa = true;
    boolean has_mpesa = true;//FirebaseRemoteConfig.getInstance().getBoolean(TOPUP_MPESA);
    boolean has_airtel = true;//FirebaseRemoteConfig.getInstance().getBoolean(TOPUP_AIRTEL);
    boolean has_visa = false;//FirebaseRemoteConfig.getInstance().getBoolean(TOPUP_VISA);



    public TopUpFragment() {
        // Required empty public constructor
    }

    public static TopUpFragment newInstance(String param1, String param2) {
        TopUpFragment fragment = new TopUpFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topup, container, false);
        top_up_balance = (TextView) view.findViewById(R.id.top_up_balance);
        mpesa = (ImageButton) view.findViewById(R.id.mpesa);
        visa = (ImageButton) view.findViewById(R.id.visa);
        airtel = (ImageButton) view.findViewById(R.id.airtel);
        buttonLayout = (LinearLayout) view.findViewById(R.id.buttonLayout);
        airtelCard = (CardView) view.findViewById(R.id.airtelCard);
        mpesaCard = (CardView) view.findViewById(R.id.mpesaCard);
        airtelGetTID = (Button) view.findViewById(R.id.airtelGetTID);
        moreOptions = (Button) view.findViewById(R.id.moreOptions);
        refresh_balance = (Button) view.findViewById(R.id.refresh_balance);
        airtel_8 = (TextView) view.findViewById(R.id.airtel_8);
        cbk = getString(R.string.baseUrl) + "/topup";
        oid= ValidationUtil.randomString(5);
        L.m("oid "+oid);

        mpesa.setOnClickListener(this);
        refresh_balance.setOnClickListener(this);
        visa.setOnClickListener(this);
        airtel.setOnClickListener(this);
        airtelGetTID.setOnClickListener(this);
        airtelGetTID.setVisibility(View.GONE);
        moreOptions.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);

        top_up_balance.setText("Balance : " + currency + " " + w_balance);

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

                case R.id.mpesa:
                if (has_mpesa) {
                    fromMpesa = true;
                    createMpesaDialog();
                } else {
                    L.t(MyApplication.getAppContext(), getString(R.string.under_maintenance));
                }
                break;

            case R.id.refresh_balance:
                refreshBalance();
                break;

            case R.id.airtel:
                if (has_airtel) {
                    createInputDialog();
                    fromMpesa = false;
                } else {
                    L.t(MyApplication.getAppContext(), getString(R.string.under_maintenance));
                }

                break;

            case R.id.airtelGetTID:
                createInputDialog();
                buttonLayout.setVisibility(View.VISIBLE);
                airtelCard.setVisibility(View.GONE);
                moreOptions.setVisibility(View.GONE);
                break;

            case R.id.moreOptions:
                buttonLayout.setVisibility(View.VISIBLE);
                airtelCard.setVisibility(View.GONE);
                mpesaCard.setVisibility(View.GONE);
                moreOptions.setVisibility(View.GONE);
                break;

            case R.id.visa:
                if (has_visa) {
//                    ((HomeActivity) getActivity()).displayView(R.id.visa);
                } else {
                    L.t(MyApplication.getAppContext(), getString(R.string.under_maintenance));
                }
                break;
        }
    }

    private void createMpesaDialog() {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.top_up_mpesa_prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);//
        alertDialogBuilder.setView(promptsView);

        final EditText amount = (EditText) promptsView.findViewById(R.id.amount);
        final EditText number = (EditText) promptsView.findViewById(R.id.number);
        number.setText("0" + ValidationUtil.validPhoneNumber(phone));

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Initiate Payment",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mpesa_user = number.getText().toString();
                                amount_provided = amount.getText().toString();
                                datastring = "1" + oid + oid + amount_provided + "0" + ValidationUtil.validPhoneNumber(phone) + email + vid + currency + id_number + amount_provided + "p3p42" + cbk;

                                getEncoding(datastring);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void createInputDialog() {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.top_up_amount_prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        alertDialogBuilder.setView(promptsView);

        final EditText amount = (EditText) promptsView.findViewById(R.id.amount);
        final TextInputLayout layout_amount = (TextInputLayout) promptsView.findViewById(R.id.layout_amount);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Proceed",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,int id) {

                                if (ValidationUtil.hasValidContents(amount)){
                                    amount_provided = amount.getText().toString();
                                    datastring = "1" + oid + oid + amount_provided + "0" + ValidationUtil.validPhoneNumber(phone) + email + vid + currency + id_number + amount_provided + "p3p42" + cbk;

                                    getEncoding(datastring);
                                    dialog.cancel();
                                }else{
                                    layout_amount.setError(getString(R.string.invalid_amount));
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.dismiss();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void getEncoding(final String to_be_encoded) {

        mProgressDialog.setMessage("Encrypting data...");
        mProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, "https://ipayafrica.com/hash/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                L.m(response);
                JSONObject encryptData = null;
                mProgressDialog.hide();

                try {
                    encryptData = new JSONObject(response);

                    if (ParseUtil.contains(encryptData, "hash")) {

                        hashedString = encryptData.getString("hash");
                        L.m(hashedString);
                        getSID(hashedString);//Secondary call to get the transaction SID

                    } else {
                        L.t(MyApplication.getAppContext(), "Data encryption failed");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mProgressDialog.hide();
                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    L.m(new String(response.data));

                    VolleyCustomErrorHandler.errorMessage(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("algo", "sha256");
                parameters.put("data", datastring);
                parameters.put("key", SECURITY_KEY);//
                L.m("datastring " + datastring);
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                MY_DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(request);


    }
// Switch up. Nuhaffi feel block. Always uh look pon sky fi know how limitless Me can be.
    //I am the sky
    //Can I get a hell yeah!
    private void getSID(final String hash) {
        mProgressDialog.setMessage("Generating code...");
        mProgressDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, "https://api.ipayafrica.com/payments/v2/transact", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                L.m(response);
                JSONObject getResponse = null;
                JSONObject getSIDString = null;
                mProgressDialog.hide();

                try {
                    getResponse = new JSONObject(response);

                    if (ParseUtil.contains(getResponse, DATA)) {

                        getSIDString = new JSONObject(getResponse.getString(DATA));

                        if (fromMpesa) {

                            if (ParseUtil.contains(getSIDString, "sid")) {
                                sid = getSIDString.getString(SID);
                                tid = getSIDString.getString(ACCOUNT);

                                hashForMpesa(sid, tid);
                                //to start manual payment
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);

                                builder.setMessage("I did not get a  prompt on my phone. Take me to the previous MPESA payment method");

                                builder.setPositiveButton("Initiate Manual", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        displayTID(tid);
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        builder.show();
                                    }
                                }, 6000);

                            }
                        } else {

                            if (ParseUtil.contains(getSIDString, ACCOUNT)) {
                                displayTID(getSIDString.getString(ACCOUNT));
                            }
                        }
                    } else {
                        L.t(MyApplication.getAppContext(), "Funds transfer failed");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mProgressDialog.hide();
                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    L.m(new String(response.data));//------------------------------------------------

                    VolleyCustomErrorHandler.errorMessage(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("curr", currency);
                parameters.put("amount", amount_provided);
                parameters.put("tel", "0" + ValidationUtil.validPhoneNumber(phone));
                parameters.put("eml", email);
                parameters.put("hash", hash);
                parameters.put("oid", oid);
                parameters.put("inv", oid);
                parameters.put("vid", vid);
                parameters.put("p1", id_number);
                parameters.put("p2", amount_provided);
                parameters.put("p3", "p3");
                parameters.put("p4", "p4");
                parameters.put("cst", "2");
                parameters.put("crl", "2");
                parameters.put("live", "1");
                parameters.put("autopay", "1");
                parameters.put("cbk", cbk);
                L.m("the map " + parameters);
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                MY_DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(request);

    }

    private void hashForMpesa(final String the_sid, final String the_tid){
        final String mpesa_hash = mpesa_user+vid+the_sid;
        mProgressDialog.setMessage("Initiating M-Pesa request");
        mProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, "https://ipayafrica.com/hash/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                L.m(response);
                JSONObject encryptData = null;

                try {
                    encryptData = new JSONObject(response);

                    if (ParseUtil.contains(encryptData, "hash")) {

                        hashedString = encryptData.getString("hash");
                        L.m(hashedString);

                       makeMpesaPushRequest(the_sid, hashedString, the_tid);

                    } else {
                        L.t(MyApplication.getAppContext(), "Data encryption failed");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mProgressDialog.hide();
                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    L.m(new String(response.data));

                    VolleyCustomErrorHandler.errorMessage(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("algo", "sha256");
                parameters.put("data", mpesa_hash);
                parameters.put("key", SECURITY_KEY);
                return parameters;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                MY_DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(request);

    }

    private void makeMpesaPushRequest(final String the_sid, final String hashedString, final String the_tid) {

        StringRequest request = new StringRequest(Request.Method.POST, "https://api.ipayafrica.com/payments/v2/transact/push/mpesa", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               // Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                L.m(response);
                JSONObject data = null;
                mProgressDialog.hide();

                try {
                    data = new JSONObject(response);

                    if (ParseUtil.contains(data, "text")) {

                        L.T(MyApplication.getAppContext(), data.getString("text"));

                    } else {
                        L.t(MyApplication.getAppContext(), "An error occurred, please retry");

                        //to start manual payment
                        displayTID(the_tid);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mProgressDialog.hide();
                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    L.m(new String(response.data));

                    VolleyCustomErrorHandler.errorMessage(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("sid", the_sid);
                parameters.put("vid", vid);
                parameters.put("phone", mpesa_user);
                parameters.put("hash", hashedString);
                L.m(parameters.toString());
                return parameters;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                MY_DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(request);
    }

    public void displayTID(final String tid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Your Unique transaction code is " + tid);

        builder.setPositiveButton("Copy Code", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                copyToClipBoard(tid);
            }
        });

        builder.setNegativeButton("Get Via SMS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                getViaText(tid);
            }
        });

        builder.show();
    }

    private void copyToClipBoard(String tid) {
        ClipboardManager clipboard = (ClipboardManager) MyApplication.getAppContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("CoverApp", tid);
        clipboard.setPrimaryClip(clip);
        L.t(MyApplication.getAppContext(), "Code copied to clipboard");

        if (fromMpesa){
                moreOptions.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.GONE);
                mpesaCard.setVisibility(View.VISIBLE);
        }else{
            moreOptions.setVisibility(View.VISIBLE);
            buttonLayout.setVisibility(View.GONE);
            airtelCard.setVisibility(View.VISIBLE);
        }
    }

    private void getViaText(final String tid) {

        mProgressDialog.setMessage("Sending SMS...");
        mProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, getString(R.string.baseUrl) + "/sendText/ipay ", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.m(response);
                mProgressDialog.hide();
                L.t(MyApplication.getAppContext(), "A Text message has been sent to your phone number");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.hide();
                L.t(MyApplication.getAppContext(), "A Text message has been sent to your phone number");
                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    //parsing the error
                    String json = "";
                    JSONObject obj;
                    switch (response.statusCode) {
                        case 400:
                            json = new String(response.data);
                            L.m(json);

                            try {
                                obj = new JSONObject(json);

                                if (ParseUtil.contains(obj, Keys.keys.ERROR)) {
                                 //   L.t(MyApplication.getAppContext(), obj.getString(Keys.keys.ERROR));
                                } else {
                                 //   L.t(MyApplication.getAppContext(), "An error occurred, retry");
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
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("tid", tid);
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                Keys.keys.MY_DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(request);

        if (fromMpesa){
            moreOptions.setVisibility(View.VISIBLE);
            buttonLayout.setVisibility(View.GONE);
            mpesaCard.setVisibility(View.VISIBLE);
        }else{
            moreOptions.setVisibility(View.VISIBLE);
            buttonLayout.setVisibility(View.GONE);
            airtelCard.setVisibility(View.VISIBLE);
        }
    }

    private void refreshBalance() {
        L.t(MyApplication.getAppContext(), "Refreshing balance");
        new TaskLoadBalance(this).execute();
    }

    /**
     *
     * ed when the account balance has been loaded
     *
     * @param balanceData the balance in a single element arraylist
     */
    @Override
    public void onBalanceLoaded(ArrayList<Balance> balanceData) {
        if(balanceData.size() > 0) {
            Balance currentBalance = balanceData.get(0);
            L.m("The BALANCE: " + currentBalance);
            L.t(MyApplication.getAppContext(), "Balance updated successfully");
            //should update the preference at this point
            MyApplication.saveToPreferences(MyApplication.getAppContext(), Keys.keys.WALLET_BALANCE, currentBalance.getBalance());

            top_up_balance.setText("Balance : " + currency + " " + currentBalance.getBalance());

            getView().invalidate();
        } else {
            // If the array is empty
            L.t(MyApplication.getAppContext(), "No change");
        }
    }
}
