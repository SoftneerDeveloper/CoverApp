package ke.co.coverapp.coverapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.fragments.EmergencyCustomViewFragment;
import ke.co.coverapp.coverapp.fragments.EmergencyPurchaseFragment;
import ke.co.coverapp.coverapp.log.L;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.*;

import ke.co.coverapp.coverapp.pojo.Balance;
import ke.co.coverapp.coverapp.tasks.TaskLoadBalance;
import ke.co.coverapp.coverapp.utility.ValidationUtil;


public class EmergencyBundleActivity extends AppCompatActivity implements View.OnClickListener, BalanceLoadedListener {

    ImageButton roadside_emergency, home_emergency, emergency_ride_delivery, ambulance_rescue, security_backup, fire_rescue, emergency_water;
    AppCompatButton purchase;
    static String price_emergency = MyApplication.readFromPreferences(MyApplication.getAppContext(), PRICE_EMERGENCY, ValidationUtil.getDefaultPrice());
    String curr = MyApplication.readFromPreferences(MyApplication.getAppContext(), CURR, ValidationUtil.getDefaultCurr());
    private ProgressDialog mProgressDialog;
    String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_bundle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        roadside_emergency = (ImageButton) findViewById(R.id.roadside_emergency);
        home_emergency = (ImageButton) findViewById(R.id.home_emergency);
        emergency_ride_delivery = (ImageButton) findViewById(R.id.emergency_ride_delivery);
        ambulance_rescue = (ImageButton) findViewById(R.id.ambulance_rescue);
        security_backup = (ImageButton) findViewById(R.id.security_backup);
        fire_rescue = (ImageButton) findViewById(R.id.fire_rescue);
        emergency_water = (ImageButton) findViewById(R.id.emergency_water);
        purchase = (AppCompatButton) findViewById(R.id.purchase);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Saving your request...");
        mProgressDialog.setCancelable(false);

        roadside_emergency.setOnClickListener(this);
        home_emergency.setOnClickListener(this);
        emergency_ride_delivery.setOnClickListener(this);
        ambulance_rescue.setOnClickListener(this);
        security_backup.setOnClickListener(this);
        fire_rescue.setOnClickListener(this);
        emergency_water.setOnClickListener(this);
        purchase.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        String message = "";
        String title = "";
        int icon = 0;
        switch (v.getId()) {

            case R.id.roadside_emergency:
                message = getString(R.string.text_roadside_emergency);
                title = getString(R.string.title_roadside_emergency);
                icon = R.drawable.roadside_emergency;
                showDetailsDialog(icon, title, message, message);
                break;

            case R.id.home_emergency:
                message = getString(R.string.text_home_emergency);
                title = getString(R.string.title_home_emergency);
                icon = R.drawable.home_emergency;
                showDetailsDialog(icon, title, message, message);
                break;

            case R.id.emergency_ride_delivery:
                message = getString(R.string.text_ride_delivery);
                title = getString(R.string.title_emergency_ride_delivery);
                icon = R.drawable.emergency_ride_delivery;
                showDetailsDialog(icon, title, message, message);
                break;
            case R.id.ambulance_rescue:
                message = getString(R.string.text_ambulance_rescue);
                title = getString(R.string.title_ambualance_rescue);
                icon = R.drawable.ambulance_rescue;
                showDetailsDialog(icon, title, message, message);
                break;
            case R.id.security_backup:
                message = getString(R.string.text_security_backup);
                title = getString(R.string.title_security_backup);
                icon = R.drawable.security_backup;
                showDetailsDialog(icon, title, message, message);
                break;
            case R.id.fire_rescue:
                message = getString(R.string.text_fire_rescue);
                title = getString(R.string.title_fire_rescue);
                icon = R.drawable.fire_rescue;
                showDetailsDialog(icon, title, message, message);
                break;
            case R.id.emergency_water:
                message = getString(R.string.text_emergency_water);
                title = getString(R.string.title_emergency_water);
                icon = R.drawable.waterbowser_icon;
                showDetailsDialog(icon, title, message, message);
                break;
            case R.id.purchase:
                createDialog();
                break;
        }
    }

    private void showDetailsDialog(int icon, String title, String desc, String meta) {
        FragmentManager fm = getSupportFragmentManager();
        EmergencyCustomViewFragment emergencyCustomViewFragment = EmergencyCustomViewFragment.newInstance(icon, title, desc, meta);
        emergencyCustomViewFragment.show(fm, "EmergencyCustomViewFragment");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            //new MainActivity().createDialog();
            return true;
        }

        if (id == R.id.action_refresh) {
            L.t(MyApplication.getAppContext(), "Refreshing balance");
            new TaskLoadBalance(this).execute();
            return true;
        }


        if (id == R.id.action_settings) {
            //  new MainActivity().displayView(R.id.nav_settings);
            return true;
        }

        if (id == R.id.action_notification) {
            startActivity(new Intent(MyApplication.getAppContext(), NotificationActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiatePurchase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        String balance = MyApplication.readFromPreferences(MyApplication.getAppContext(), WALLET_BALANCE, ValidationUtil.getDefaultBalance());

        if (Float.parseFloat(balance) <= Integer.valueOf(price_emergency)) {

            builder.setMessage("Your account balance is " + curr + " " + balance + ", you will need to top up to continue ");
            builder.setPositiveButton("Top Up", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            startActivity(new Intent(MyApplication.getAppContext(), RunTimeTopUpActivity.class));
                        }

                    }
            ).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        } else {
            initiatePurchaseDialog();
        }
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("The Emergency bundle costs  " + curr + " " + price_emergency + " per month");

        builder.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                // buyCover();
                initiatePurchase();
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

    private void initiatePurchaseDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EmergencyPurchaseFragment emergencyPurchaseFragment = new EmergencyPurchaseFragment();
        emergencyPurchaseFragment.show(fm, "EmergencyPurchaseFragment");
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
         //   L.t(MyApplication.getAppContext(), "Balance updated successfully");
        }
    }
}
