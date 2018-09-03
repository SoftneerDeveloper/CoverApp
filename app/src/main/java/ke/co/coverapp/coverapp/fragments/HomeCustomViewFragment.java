package ke.co.coverapp.coverapp.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
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
import java.util.List;
import java.util.Map;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.AddNewAssetHomeCover;
import ke.co.coverapp.coverapp.activities.RunTimeTopUpActivity;
import ke.co.coverapp.coverapp.activities.SelectAssetHomeCover;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.AssetsLoadedListener;
import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Assets;
import ke.co.coverapp.coverapp.pojo.Balance;
import ke.co.coverapp.coverapp.tasks.TaskLoadAssets;
import ke.co.coverapp.coverapp.tasks.TaskLoadBalance;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.*;

/**
 * Created by Clifford Owino on 3/27/2017.
 */

public class HomeCustomViewFragment extends DialogFragment implements View.OnClickListener, BalanceLoadedListener {

    TextView home_title, home_desc, home_cost, home_meta, terms_home_cover;
    public static final String EXTRA_TIMEOUT = "timeout";
    Spinner period_options_spinner, partner_options_spinner, asset_options_spinner, asset_categories_spinner;
    Button button_cancel, button_accept;
    private ProgressDialog mProgressDialog;
    String payment_plan_selected, selected_asset;
    int automate_billing = 1;
    int payment_plan = 0;
    Float final_price =0.F;
    String price_home = MyApplication.readFromPreferences(MyApplication.getAppContext(), PRICE_HOME, ValidationUtil.getDefaultCurr());
    String curr = MyApplication.readFromPreferences(MyApplication.getAppContext(), CURR, ValidationUtil.getDefaultCurr());
    String balance = MyApplication.readFromPreferences(MyApplication.getAppContext(), WALLET_BALANCE, ValidationUtil.getDefaultBalance());
    Context context;
    String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());

    List<Assets> list_assets;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getActivity();

        return inflater.inflate(R.layout.home_custom_view_dialog, container);
    }

    public HomeCustomViewFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead
    }

    public static HomeCustomViewFragment newInstance(String title, String desc, String home_meta, float price) {
        HomeCustomViewFragment frag = new HomeCustomViewFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("desc", desc);
        args.putString("home_meta", home_meta);
        args.putFloat("unit_price", price);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        home_title = (TextView) view.findViewById(R.id.home_title);
        home_desc = (TextView) view.findViewById(R.id.home_desc);
//        home_meta = (TextView) view.findViewById(R.id.home_meta);
        home_cost = (TextView) view.findViewById(R.id.home_cost);
        button_cancel = (Button) view.findViewById(R.id.button_cancel);
        button_accept = (Button) view.findViewById(R.id.button_accept);
        button_cancel.setOnClickListener(this);
        button_accept.setOnClickListener(this);
        context = getActivity();

        // Dictate what the view looks like based on the type of home cover selected

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Acquiring cover...");
        mProgressDialog.setCancelable(false);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Home Cover");
        String desc = getArguments().getString("desc", "Home Cover");
//        String meta = getArguments().getString("home_meta", "Home Cover");

        terms_home_cover = (TextView) view.findViewById(R.id.terms_home_cover);
        terms_home_cover.setText(Html.fromHtml("  <i>Terms and conditions</i>"));

        if (terms_home_cover != null) {
            terms_home_cover.setOnClickListener(this);
        }

        home_title.setText(title);
        home_desc.setText(desc);
//        home_meta.setText(meta);
//        getDialog().setTitle(title);
        getDialog().setCancelable(false);

        period_options_spinner = (Spinner) view.findViewById(R.id.period_options_spinner);
        partner_options_spinner = (Spinner) view.findViewById(R.id.partner_options_spinner);
//        asset_options_spinner = (Spinner) view.findViewById(R.id.asset_options_spinner);
//        asset_categories_spinner = (Spinner) view.findViewById(R.id.asset_categories_spinner);

        ArrayAdapter<CharSequence> period_spinner_adaptor = ArrayAdapter
                .createFromResource(MyApplication.getAppContext(), R.array.period_options,
                        R.layout.spinner_item);

        ArrayAdapter<CharSequence> partner_spinner_adaptor = ArrayAdapter
                .createFromResource(MyApplication.getAppContext(), R.array.partner_options,
                        R.layout.spinner_item);

//        ArrayAdapter<CharSequence> asset_spinner_adaptor = ArrayAdapter
//                .createFromResource(MyApplication.getAppContext(), R.array.asset_options,
//                        R.layout.spinner_item);

//        ArrayAdapter<String> asset_spinner_adaptor = new ArrayAdapter<>(getActivity(), R.layout.spinner_item);

        // Get assets
//        list_assets = MyApplication.getWritableDatabase().readNonCoveredAssets();

//        if (list_assets.isEmpty()) {
//            new TaskLoadAssets(HomeCustomViewFragment.this, 0).execute();
//        }

//        L.m("My assets" + list_assets);

//        asset_spinner_adaptor.clear();

//        if (!list_assets.isEmpty()) {
//            // Add default selection
//            for (int i = 0; i < list_assets.size(); i++) {
//                Assets asset = list_assets.get(i);
//                asset_spinner_adaptor.add(asset.getName());
//            }
//        } else {
//            asset_spinner_adaptor.add("No assets found");
//        }

//        ArrayAdapter<CharSequence> asset_category_adaptor = ArrayAdapter
//                .createFromResource(MyApplication.getAppContext(), R.array.asset_categories,
//                        R.layout.spinner_item);

        period_spinner_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        partner_spinner_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        asset_spinner_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        asset_category_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        period_options_spinner.setAdapter(period_spinner_adaptor);

        // Disable partner spinner
        partner_options_spinner.setEnabled(false);
//        partner_options_spinner.setClickable(false);
        partner_options_spinner.setAdapter(partner_spinner_adaptor);
//        asset_options_spinner.setAdapter(asset_spinner_adaptor);
//        asset_categories_spinner.setAdapter(asset_category_adaptor);

        period_options_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    payment_plan_selected = "Monthly";
                    payment_plan = 1;
                    final_price = getArguments().getFloat("unit_price", 0);
                    home_cost.setText(curr+" "+final_price+" per month");
                } else if (position == 2) {
                    payment_plan_selected = "Quarterly";
                    payment_plan = 3;
                    final_price = getArguments().getFloat("unit_price", 0)*3;
                    home_cost.setText(curr+" "+final_price+" per quarter");
                } else if (position == 3) {
                    payment_plan = 6;
                    payment_plan_selected = "Bi-Annually";
                    final_price = getArguments().getFloat("unit_price", 0)*6;
                    home_cost.setText(curr+" "+final_price+" per half-year");

                } else if (position == 4) {
                    payment_plan = 12;
                    payment_plan_selected = "Annually";
                    final_price = getArguments().getFloat("unit_price", 0)*12;
                    home_cost.setText(curr+" "+final_price+" per year");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        // Get asset that was purchased
//        asset_options_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                // Get asset
//                    Assets asset = list_assets.get(i);
//                    selected_asset = asset.getId();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

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
//                createDialog();
                createDialogNew();
                break;

            case R.id.terms_home_cover:
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.site_url) + "/terms")));
                break;

        }

    }

    private void topUpDialog() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        if (Float.parseFloat(balance) <= final_price) {

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
            purchaseProcess();
        }
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

    private void createDialogNew() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("You have selected Partner 1 with the " + payment_plan_selected + " payment plan. Do you want to proceed?");

        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // buyCover();
//                priceCheck();
                assetDialog();
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

    private void assetDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);


        builder.setMessage("Do you want to cover an asset already added or would" +
                " you like to add a new asset?"); // You have selected the " + desc + ".
        builder.setPositiveButton("Add new", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        // Add new asset
                        Intent intent_one = new Intent(context, AddNewAssetHomeCover.class);
                        intent_one.putExtra("payment_plan", payment_plan_selected);
                        //TODO: Get selected partner
                        context.startActivity(intent_one);
//                        context.startActivity(new Intent(context, AddNewAssetHomeCover.class));
                    }

                }
        ).setNegativeButton("Select added", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                Intent intent_two = new Intent(context, SelectAssetHomeCover.class);
                intent_two.putExtra("payment_plan", payment_plan_selected);
                //TODO: Get selected partner
                context.startActivity(intent_two);
//                context.startActivity(new Intent(context, SelectAssetHomeCover.class));

            }
        }).show();
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
        StringRequest request = new StringRequest(Request.Method.POST, "http://api.coverappke.com/bundle/purchase ", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.m(response);
                // Update wallet balance
                new TaskLoadBalance(HomeCustomViewFragment.this).execute();
//                new TaskLoadAssets(HomeCustomViewFragment.this, 0).execute();
                JSONObject responceJson = null;
                try {
                    responceJson = new JSONObject(response);
                    if (ParseUtil.contains(responceJson, "text")) {
                        L.t(MyApplication.getAppContext(), responceJson.getString("text"));

                    } else {
                        L.t(MyApplication.getAppContext(), "You have successfully acquired the Home cover, you will be contacted shortly");

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


                if (response != null && response.data != null) {
                    L.m(new String(response.data));
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

                map.put("package", "home_cover");
                map.put("unit_code", "complete_plan");
                map.put("product", "{home_cover}");
                map.put("num_payments", payment_plan+"");
                map.put("signup_cost", price_home+"");

                // Asset details
                map.put("asset_id", selected_asset);

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

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
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

//    @Override
//    public void onAssetsLoaded(ArrayList<Assets> listFeatures) {
//        list_assets = MyApplication.getWritableDatabase().readNonCoveredAssets();
//
//        L.t(MyApplication.getAppContext(), "Asset list updated");
//    }
}
