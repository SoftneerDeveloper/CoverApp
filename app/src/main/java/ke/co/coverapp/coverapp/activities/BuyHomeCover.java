package ke.co.coverapp.coverapp.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.adapters.BuyCoverAssetListAdapter;
import ke.co.coverapp.coverapp.adapters.SelectAssetsListAdapter;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.AssetsLoadedListener;
import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.fragments.AddAssetHomeCover;
import ke.co.coverapp.coverapp.fragments.AddAssetsFragment;
import ke.co.coverapp.coverapp.fragments.AssetsViewFragment;
import ke.co.coverapp.coverapp.fragments.CoversFragment;
import ke.co.coverapp.coverapp.fragments.HomeCoverInfoViewFragment;
import ke.co.coverapp.coverapp.fragments.HomeFragment;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleyMultipartRequest;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Assets;
import ke.co.coverapp.coverapp.pojo.Balance;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.tasks.TaskLoadAssets;
import ke.co.coverapp.coverapp.tasks.TaskLoadBalance;
import ke.co.coverapp.coverapp.utility.AppHelperUtil;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.ACCESS_TOKEN;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.CURR;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.PRICE_HOME;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.WALLET_BALANCE;

public class BuyHomeCover extends AppCompatActivity implements AssetsLoadedListener, View.OnClickListener, AdapterView.OnItemSelectedListener, BalanceLoadedListener {
    private ArrayList<Assets> assetsList = new ArrayList<>();
    Spinner period_options_spinner, assets_spinner;//, partner_options_spinner;
    private static final String STATE_ACTIVITY = "state_activity";
    String payment_plan_selected, cover_type;
    int payment_plan = 0, check_asset_spinner = 0, automate_billing = 1;
    Float final_price, cover_type_cost = 0.F;
    String price_home = MyApplication.readFromPreferences(MyApplication.getAppContext(), PRICE_HOME, ValidationUtil.getDefaultCurr());
    String curr = MyApplication.readFromPreferences(MyApplication.getAppContext(), CURR, ValidationUtil.getDefaultCurr());
    String balance = MyApplication.readFromPreferences(MyApplication.getAppContext(), WALLET_BALANCE, ValidationUtil.getDefaultBalance());
    TextView home_cost, home_desc, home_title, textView2, textView10;
    Button create_asset, button_accept, view_assets, button_cancel;
    CheckBox buy_home_cover_tc;
    Context context;

    private ListView list_view_assets;
    private ProgressDialog mProgressDialog;
    private static String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listAssetItems = new ArrayList<String>();

    ArrayList<String> listAssetItemsId = new ArrayList<>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> assets_spinner_adapter;

    BuyCoverAssetListAdapter listAssetsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_home_cover);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        context = BuyHomeCover.this;

        // Load balance
        new TaskLoadBalance(this).execute();

        mProgressDialog = new ProgressDialog(BuyHomeCover.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Purchasing home cover...");

        Bundle bundle = getIntent().getExtras();
        cover_type_cost = bundle.getFloat("cost", 0);
        String title = bundle.getString("cover_type");
        cover_type = title;
        String desc = bundle.getString("message");

        home_cost = (TextView) findViewById(R.id.home_cost);
        home_title = (TextView) findViewById(R.id.home_title);
        home_title.setText(title);
        home_desc = (TextView) findViewById(R.id.home_desc);
        home_desc.setText(desc);

//        add_asset = (Button) findViewById(R.id.btn_add_asset);
//        add_asset.setOnClickListener(this);

        create_asset = (Button) findViewById(R.id.btn_create_asset);
        create_asset.setOnClickListener(this);

        view_assets = (Button) findViewById(R.id.btn_view_assets);
        view_assets.setOnClickListener(this);

        button_accept = (Button) findViewById(R.id.button_accept);
        button_accept.setOnClickListener(this);

        button_cancel = (Button) findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(this);

        buy_home_cover_tc = (CheckBox) findViewById(R.id.buy_home_cover_tc);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView10 = (TextView) findViewById(R.id.textView10);

        // Assets List
//        assets_list = (RecyclerView) findViewById(R.id.selectedAssetList);
//        asset_list_adapter = new SelectAssetsListAdapter(this, assetsListTwo);
//        assets_list.setAdapter(asset_list_adapter);

//        assets_list.setLayoutManager(new LinearLayoutManager(BuyHomeCover.this));

        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change, load the existing activity from a parcelable
            assetsList = savedInstanceState.getParcelableArrayList(STATE_ACTIVITY);
        } else {
            //if this fragment starts for the first time, load the list of activities from a database
            assetsList = MyApplication.getWritableDatabase().readAssets();
            //if the database is empty, trigger an AsyncTask to download activity list from the API
            if (assetsList.isEmpty()) {
                new TaskLoadAssets(this, 0).execute();
            } else {
                int last_id = Integer.valueOf(assetsList.get(assetsList.size() - 1).getUid());
                L.m("The last id " + assetsList.get(assetsList.size() - 1).getUid());
                new TaskLoadAssets(this, last_id).execute();
            }
        }

        // Update your Adapter to containing the retrieved activities
//        asset_list_adapter.setAssetsList(assetsListTwo);

        period_options_spinner = (Spinner) findViewById(R.id.period_options_spinner);
//        partner_options_spinner = (Spinner) findViewById(R.id.partner_options_spinner);

//        assets_spinner = (Spinner) findViewById(R.id.assets_spinner);

        ArrayAdapter<CharSequence> period_spinner_adaptor = ArrayAdapter
                .createFromResource(this, R.array.period_options,
                        android.R.layout.simple_spinner_item);

//        ArrayAdapter<CharSequence> partner_spinner_adaptor = ArrayAdapter
//                .createFromResource(MyApplication.getAppContext(), R.array.partner_options,
//                        R.layout.spinner_item);

//        assets_spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);

        if (!assetsList.isEmpty()) {
            for (int i = 0; i < assetsList.size(); i++) {
                Assets assets = assetsList.get(i);
//                assets_spinner_adapter.add(assets.getName());
            }
        } else {
//            assets_spinner_adapter.add("No assets found");
        }

        period_spinner_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        partner_spinner_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


//        assets_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        period_options_spinner.setAdapter(period_spinner_adaptor);
//        partner_options_spinner.setAdapter(partner_spinner_adaptor);


//        assets_spinner.setAdapter(assets_spinner_adapter);


//        list_view_assets = (ListView) findViewById(R.id.list_view_assets);

//        listAssetsAdapter =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listAssetItems);
//        list_view_assets.setAdapter(listAssetsAdapter);

        listAssetsAdapter = new BuyCoverAssetListAdapter(this, listAssetItems, listAssetItemsId);
//        list_view_assets.setAdapter(listAssetsAdapter);

//        assets_spinner.setOnItemSelectedListener(this);

        period_options_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    payment_plan_selected = "Monthly";
                    payment_plan = 1;
                    final_price = cover_type_cost;
                    home_cost.setText(curr + " " + final_price + " per month");
                }
//                else if (position == 2) {
//                    payment_plan_selected = "Quarterly";
//                    payment_plan = 3;
//                    final_price = cover_type_cost*3;
//                    home_cost.setText(curr + " " + final_price + " per quarter");
//                }
//                else if (position == 3) {
//                    payment_plan = 6;
//                    payment_plan_selected = "Bi-Annually";
//                    final_price = cover_type_cost*6;
//                    home_cost.setText(curr + " " + final_price + " per half-year");
//
//                }
                else if (position == 2) {
                    payment_plan = 12;
                    payment_plan_selected = "Annually";
                    final_price = cover_type_cost*12;
                    home_cost.setText(curr + " " + final_price + " per year");

                } else {
                    payment_plan = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    private void showViewAssetsFragment()  {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        AssetsViewFragment assetsViewFragment = AssetsViewFragment.newInstance();
        assetsViewFragment.show(manager, "AssetsViewFragment");
    }

    @Override
    public void onAssetsLoaded(ArrayList<Assets> assetsList) {
        L.t(BuyHomeCover.this, "Assets loaded.");

        if (assetsList != null) {
            if (!assetsList.isEmpty()) {
//                L.t(BuyHomeCover.this, "Assets loaded.");
                L.m("assetsList: " + assetsList);
//                asset_list_adapter.addToAssetsList(assetsList);
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_create_asset:
                // TODO: Create a dialog to do this.
                // Open add asset fragment
                L.t(BuyHomeCover.this, "Add a bed to your asset list. Go to 'My Assets' in the main menu to add a different asset type.");
                Bundle bundle_four = new Bundle();
                bundle_four.putString("category", "2");
                bundle_four.putString("type", "8");
                Intent addAsset = new Intent(BuyHomeCover.this, AddAssetActivity.class);
                addAsset.putExtras(bundle_four);
                startActivity(addAsset);

                break;

            case R.id.btn_view_assets:
                // TODO: Create a dialog to do this.
                // TODO: Show fragment of all assets
                showViewAssetsFragment();

                break;

            case R.id.button_cancel:

              finish();

                break;

            case R.id.button_accept:
                // Check if required items are filled
                if(buy_home_cover_tc.isChecked()) {
                    // Proceed
                    buy_home_cover_tc.setError(null);

                    // Check that at least one asset has been added to list
//                    if(listAssetItemsId.size() == 0) {
//                        // Show error
//                        L.t(BuyHomeCover.this, "Please select at least one asset to cover.");
//                        textView2.setError("Please select at least one asset to cover.");
//                        textView2.requestFocus();
//
//                    } else {
                        // Proceed
                        textView2.setError(null);

                        // Check that they've selected a payment plan
                        if (payment_plan == 0) {
                            // Show error
                            L.t(BuyHomeCover.this, "Please select a payment plan.");
                            textView10.setError("Please select a payment plan.");
                            textView10.requestFocus();


                        } else {
                            // Proceed to process payment
                            checkProfile2();
                            //textView10.setError(null);

                            //createDialog();
                        }

//                    }
                } else {
                    // Show error
                    L.t(BuyHomeCover.this, "Please read the terms and conditions.");
                    buy_home_cover_tc.setError("Please read the terms and conditions.");
                    buy_home_cover_tc.requestFocus();
                }

            default:
                break;
        }
    }
    private void profileDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

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
    public void checkProfile2(){
        // Make API call to check for user profile details
        mProgressDialog.show();
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, "http://api.coverappke.com/user/profile/check", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse asset) {
                // Take to next page
                textView10.setError(null);

                createDialog();
                L.m("Success Profile Details");
                //startActivity(new Intent(MyApplication.getAppContext(), HomeBundleActivity.class));
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
                            //message = "Oops! I need to know you better. Press 'Okay' to give me enough details about you. You are a few steps away from purchasing a cover.";
                            //profileDialog(message);

                            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
                            UserGuide dialog = new UserGuide();
                            dialog.show(manager, "Message");

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

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Your selected bundle costs  " + curr + " " + final_price);

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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
        if (Float.parseFloat(balance) < final_price) {
            topUpDialog();
            return;
        }

        billingRequest();
    }

    private void topUpDialog() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        if (Float.parseFloat(balance) < final_price) {

            builder2.setMessage("Your account balance is  " + curr + " " + balance + ", you will need to top up to continue ");
            builder2.setPositiveButton("Top Up", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            context.startActivity(new Intent(context, RunTimeTopUpActivity.class)); }

                    }
            ).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();

        } else {
            buyCover();

        }
    }


    private void billingRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Automate billing of  " + curr + " " + final_price+ " "+payment_plan_selected+" from your wallet?");

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                automate_billing = 1;
                buyCover();
            }
        });

        builder.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                automate_billing = 0;
                //buyCover();
            }
        });

        builder.show();
    }

    private void refreshBalance() {
        L.t(MyApplication.getAppContext(), "Refreshing balance");
        new TaskLoadBalance(this).execute();
    }

    public void buyCover() {
        // Get list of selected assets
//        L.m("Number of selected assets: " + listAssetItemsId.size());
        // Process cover
//        L.T(BuyHomeCover.this, "Home Content Cover has been purchased. Check your email for further details.");
//        L.t(ReportClaimsActivity.this, "Your claim has been placed. It is currently being processed.");
        mProgressDialog.show();
        L.m("Purchase home cover");

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, "http://api.coverappke.com/bundle/purchase", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse asset) {

                //new TaskLoadBalance(BuyHomeCover.this).execute();
                refreshBalance();

                L.m(asset.toString());
                L.t(MyApplication.getAppContext(), "Home Content Cover has been purchased. Check your email for further details."); // TODO: Get message to display directly from the API?

                startActivity(new Intent(BuyHomeCover.this, MainActivity.class));
                mProgressDialog.hide();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.hide();

                L.m("Error: " + error.toString());

                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
//                    L.t(ReportClaimsActivity.this, "An error occurred. Status code: " + response.statusCode);

                    L.m(new String(response.data));
                    switch (response.statusCode) {
                        case 400:
                            //parsing the error

                            JSONObject obj;
                            JSONArray message;
                            JSONObject jsonRowObj;
                            String json = new String(response.data);//string
                            L.m(new String(response.data));

                            try {
                                obj = new JSONObject(json);
                                if (ParseUtil.contains(obj, "message")) {

                                    L.t(MyApplication.getAppContext(), obj.getString("message"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                L.t(BuyHomeCover.this, "An error occurred. Please try again.");
                            }
                            break;

                        case 403:
                            L.m("User authorization error");
                            break;

                        default:
                            VolleyCustomErrorHandler.errorMessage(error);
                            break;
                    }
                } else {
                    L.m("Issa volley error: " + error.getClass());
                    VolleyCustomErrorHandler.errorMessage(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                parameters.put("package", "home_content"); // TODO: Change to home_cover
                parameters.put("unit_code", "pioneer_cover");
                parameters.put("product", "all_assets");
                parameters.put("cover_type", cover_type);
                parameters.put("num_payments", Integer.toString(payment_plan));
                parameters.put("signup_cost", Float.toString(final_price));
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
//            case R.id.assets_spinner:
////                L.m("Check asset spinner: " + check_asset_spinner);
//                if (++check_asset_spinner > 1) {
//                    L.m("Check asset spinner: " + check_asset_spinner);
//                    Assets asset = assetsList.get(position);
//
//                    // Check if the clicked asset is already in the list
//
//                    if (listAssetItemsId.contains(asset.getId())) {
//                        L.m("Asset is already added to the list");
//                        L.t(BuyHomeCover.this, asset.getName().toString() + " is already in the list");
//
//                    } else {
//                        L.m("Asset is not in the list");
//                        L.m("Added asset: " + asset.getName() + "; ID: " + asset.getId());
//                        L.m("Asset type: " + asset.getTypeId() + "; Asset category: " + asset.getCategoryId());
//                        L.t(BuyHomeCover.this, asset.getName() + " has been added to list");
//
//                        // Add asset to list
//                        listAssetItems.add(asset.getName());
//                        listAssetItemsId.add(asset.getId());
//                        listAssetsAdapter.notifyDataSetChanged();
//                    }
//
//                    // Print array items
////                    L.m("Selected assets: " + listAssetItems.toString());
//
//                    // Remove item from array
////                    assetsList.remove(asset.getId());
//
//                    // Remove item from spinner
////                    assets_spinner_adapter.remove(asset.getName());
//                }
//                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBalanceLoaded(ArrayList<Balance> balanceData) {
        if (balanceData.size() > 0) {

            Balance currentBalance = balanceData.get(0);
            MyApplication.saveToPreferences(MyApplication.getAppContext(), WALLET_BALANCE, currentBalance.getBalance());
        }
    }
}
