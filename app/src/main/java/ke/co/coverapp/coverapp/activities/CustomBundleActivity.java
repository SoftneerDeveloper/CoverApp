package ke.co.coverapp.coverapp.activities;

import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

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
import ke.co.coverapp.coverapp.application.MyApplication;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.*;

import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.fragments.CustomizedPurchaseFragment;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Balance;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.LoadUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

public class CustomBundleActivity extends AppCompatActivity implements View.OnClickListener, BalanceLoadedListener {

    Button purchase;
    private ProgressDialog mProgressDialog;
    CheckBox check_roadside, check_home, check_ride_delivery, check_ambulance, check_security, check_fire, check_water;
    float charge = 0.0f;
    TextView price_roadside, price_home, price_ride_delivery, price_ambulance, price_security, price_fire, price_water;

    String price_roadside_figure = MyApplication.readFromPreferences(MyApplication.getAppContext(), PRICE_ROADSIDE, ValidationUtil.getDefaultPrice());
    String price_home_figure = MyApplication.readFromPreferences(MyApplication.getAppContext(), PRICE_HOME, ValidationUtil.getDefaultPrice());
    String price_ride_delivery_figure = MyApplication.readFromPreferences(MyApplication.getAppContext(), PRICE_RIDE_DELIVERY, ValidationUtil.getDefaultPrice());
    String price_ambulance_figure = MyApplication.readFromPreferences(MyApplication.getAppContext(), PRICE_AMBULANCE, ValidationUtil.getDefaultPrice());
    String price_security_figure = MyApplication.readFromPreferences(MyApplication.getAppContext(), PRICE_SECURITY, ValidationUtil.getDefaultPrice());
    String price_fire_figure = MyApplication.readFromPreferences(MyApplication.getAppContext(), PRICE_FIRE, ValidationUtil.getDefaultPrice());
    String price_water_figure = MyApplication.readFromPreferences(MyApplication.getAppContext(), PRICE_WATER, ValidationUtil.getDefaultPrice());
    String price_minimum = MyApplication.readFromPreferences(MyApplication.getAppContext(), PRICE_MINIMUM_CUSTOM, ValidationUtil.getDefaultPrice());
    String currency = MyApplication.readFromPreferences(MyApplication.getAppContext(), CURR, ValidationUtil.getDefaultCurr());
    String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());

    Map<String, String> map = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_bundle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        purchase = (Button) findViewById(R.id.purchase);

        check_roadside = (CheckBox) findViewById(R.id.check_roadside);
        check_home = (CheckBox) findViewById(R.id.check_home);
        check_ride_delivery = (CheckBox) findViewById(R.id.check_ride_delivery);
        check_ambulance = (CheckBox) findViewById(R.id.check_ambulance);
        check_security = (CheckBox) findViewById(R.id.check_security);
        check_fire = (CheckBox) findViewById(R.id.check_fire);
        check_water = (CheckBox) findViewById(R.id.check_water);

        price_roadside = (TextView) findViewById(R.id.price_roadside);
        price_home = (TextView) findViewById(R.id.price_home);
        price_ride_delivery = (TextView) findViewById(R.id.price_ride_delivery);
        price_ambulance = (TextView) findViewById(R.id.price_ambulance);
        price_security = (TextView) findViewById(R.id.price_security);
        price_fire = (TextView) findViewById(R.id.price_fire);
        price_water = (TextView) findViewById(R.id.price_water);

        price_roadside.setText(currency + " : " + price_roadside_figure);
        price_home.setText(currency + " : " + price_home_figure);
        price_ride_delivery.setText(currency + " : " + price_ride_delivery_figure);
        price_ambulance.setText(currency + " : " + price_ambulance_figure);
        price_security.setText(currency + " : " + price_security_figure);
        price_fire.setText(currency + " : " + price_fire_figure);
        price_water.setText(currency + " : " + price_water_figure);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.progress_get_custom));

        if (purchase != null) {
            purchase.setOnClickListener(this);
        }

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        map.clear();
        charge = 0;
        if (check_roadside.isChecked()) {
            if (!map.containsKey(PRICE_ROADSIDE)) {
                map.put(PRICE_ROADSIDE, price_roadside_figure);
            }
        } else {
            if (map.containsKey(PRICE_ROADSIDE)) {
                map.remove(PRICE_ROADSIDE);
            }
        }
        if (check_home.isChecked()) {
            if (!map.containsKey(PRICE_HOME)) {
                map.put(PRICE_HOME, price_home_figure);
            }
        } else {
            if (map.containsKey(PRICE_HOME)) {
                map.remove(PRICE_HOME);
            }
        }
        if (check_ride_delivery.isChecked()) {
            if (!map.containsKey(PRICE_RIDE_DELIVERY)) {
                map.put(PRICE_RIDE_DELIVERY, price_ride_delivery_figure);
            }
        } else {
            if (map.containsKey(PRICE_RIDE_DELIVERY)) {
                map.remove(PRICE_RIDE_DELIVERY);
            }
        }
        if (check_ambulance.isChecked()) {
            if (!map.containsKey(PRICE_AMBULANCE)) {
                map.put(PRICE_AMBULANCE, price_ambulance_figure);
            }
        } else {
            if (map.containsKey(PRICE_AMBULANCE)) {
                map.remove(PRICE_AMBULANCE);
            }
        }
        if (check_security.isChecked()) {
            if (!map.containsKey(PRICE_SECURITY)) {
                map.put(PRICE_SECURITY, price_security_figure);
            }
        } else {
            if (map.containsKey(PRICE_SECURITY)) {
                map.remove(PRICE_SECURITY);
            }
        }
        if (check_fire.isChecked()) {
            if (!map.containsKey(PRICE_FIRE)) {
                map.put(PRICE_FIRE, price_fire_figure);
            }
        } else {
            if (map.containsKey(PRICE_FIRE)) {
                map.remove(PRICE_FIRE);
            }
        }
        if (check_water.isChecked()) {
            if (!map.containsKey(PRICE_WATER)) {
                map.put(PRICE_WATER, price_water_figure);
            }
        } else {
            if (map.containsKey(PRICE_WATER)) {
                map.remove(PRICE_WATER);
            }
        }

        for (String price : map.values()) {
            charge += Float.valueOf(price);
        }

        L.m("price_minimum " + "test data");
        L.m("Charge " + charge);

        if (Integer.valueOf(price_minimum) > charge) {
            minimumErrorDialog();
        } else {
            //System.out.println(charge);
            confirmPurchaseDialog(charge + "");
        }
    }

    public void minimumErrorDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("The minimum price allowable for this package is " + currency + " " + price_minimum);

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();

    }

    public void confirmPurchaseDialog(final String total_price) {
        L.m(map.toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("The total price of the cover per month is " + currency + " " + total_price);

        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                // Select purchase plan i.e monthly etc
                // Call function to load fragment
                // Check for a user's wallet balance and compare it to the cost of the cover
                checkBalance();
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

    private void checkBalance() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        String balance = MyApplication.readFromPreferences(MyApplication.getAppContext(), WALLET_BALANCE, ValidationUtil.getDefaultBalance());

        if (Float.parseFloat(balance) <= charge) {
            builder.setMessage("Your account balance is " + currency + " " + balance + ", you will need to top up to continue");
            builder.setPositiveButton("Top up", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    startActivity(new Intent(MyApplication.getAppContext(), RunTimeTopUpActivity.class));
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).show();
        } else {
            initiatePurchaseProcess();
        }
    }

    private void initiatePurchaseProcess() {
        FragmentManager fm = getSupportFragmentManager();
        CustomizedPurchaseFragment customizedPurchaseFragment = new CustomizedPurchaseFragment();

        // Set data to pass
        Bundle data = new Bundle();
        data.putFloat("charge", charge);
        data.putString("map", map.keySet().toString());

        customizedPurchaseFragment.setArguments(data);
        customizedPurchaseFragment.show(fm, "CustomizedPurchaseFragment");
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
