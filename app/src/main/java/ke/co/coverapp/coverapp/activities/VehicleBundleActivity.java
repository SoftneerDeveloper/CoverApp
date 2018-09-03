package ke.co.coverapp.coverapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.adapters.VehicleAdapter;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.callbacks.VehiclesLoadedListener;
import ke.co.coverapp.coverapp.fragments.VehiclePurchaseFragment;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Balance;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.pojo.Vehicle;
import ke.co.coverapp.coverapp.tasks.TaskLoadBalance;
import ke.co.coverapp.coverapp.tasks.TaskLoadVehicles;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.SortUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.CAMERA_REQUEST_FOUR;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.CAMERA_REQUEST_ONE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.CAMERA_REQUEST_THREE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.CAMERA_REQUEST_TWO;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.CAR_VALUE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.CURR;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.ENGINE_CAPACITY;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.GALLERY_REQUEST_FOUR;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.GALLERY_REQUEST_ONE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.GALLERY_REQUEST_THREE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.GALLERY_REQUEST_TWO;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.ID_NUMBER;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.IS_TOPUP;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.ODOMETER;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.PLATE_FIRST;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.PLATE_LAST;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.VEHICLE_MAKE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.WALLET_BALANCE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.YOM;

public class VehicleBundleActivity extends AppCompatActivity
        implements View.OnClickListener , VehiclesLoadedListener, VehicleAdapter.OnVehicleClickListener, BalanceLoadedListener{
    private ArrayList<Vehicle> vehicleList = new ArrayList<>();
    private VehicleAdapter vehicleAdapter;
    Button button_to_add_vehicle, button_to_buy_vehicle; // button_to_edit_vehicle,
    private static final String STATE_ACTIVITY = "state_activity";
    RecyclerView vehicleRecyclerView;
    private SortUtil mSorter = new SortUtil();
    Spinner assets_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor_bundle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

//        button_to_edit_vehicle = (Button) findViewById(R.id.button_to_edit_vehicle) ;
        button_to_add_vehicle  = (Button) findViewById(R.id.button_to_add_vehicle) ;
        button_to_buy_vehicle  = (Button) findViewById(R.id.button_to_buy_vehicle) ;
//        button_to_edit_vehicle.setOnClickListener(this);
        button_to_add_vehicle.setOnClickListener(this);
        button_to_buy_vehicle.setOnClickListener(this);

        vehicleRecyclerView = (RecyclerView) findViewById(R.id.vehicleRecyclerView);

        vehicleAdapter = new VehicleAdapter(this, vehicleList, this);

        vehicleRecyclerView.setAdapter(vehicleAdapter);

        vehicleRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        activity_spinner = (Spinner) view.findViewById(R.id.sort_activity_spinner);
//
//        ArrayAdapter<CharSequence> activity_spinner_adaptor = ArrayAdapter
//                .createFromResource(MyApplication.getAppContext(), R.array.sort_activity_options,
//                        android.R.layout.simple_spinner_item);
//
//        activity_spinner_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        activity_spinner.setAdapter(activity_spinner_adaptor);
//
//        activity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    onSortByDate();
//                } else if (position == 1) {
//                    onSortByAmount();
//                } else if (position == 2) {
//                    onSortByChannel();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });


        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change, load the existing activity from a parcelable
            vehicleList = savedInstanceState.getParcelableArrayList(STATE_ACTIVITY);
        } else {
            //if this fragment starts for the first time, load the list of activities from a database
            vehicleList = MyApplication.getWritableDatabase().readVehicle();
            new TaskLoadVehicles(this).execute();

        }
        //update your Adapter to containing the retrieved activities
        vehicleAdapter.setVehiclesList(vehicleList);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_to_buy_vehicle:
                FragmentManager fm = getSupportFragmentManager();
                VehiclePurchaseFragment vehiclePurchaseFragment = VehiclePurchaseFragment.newInstance();
                vehiclePurchaseFragment.show(fm, "VehiclePurchaseFragment");
                break;
            case R.id.button_to_add_vehicle:
                startActivity(new Intent(MyApplication.getAppContext(), AddVehicleActivity.class));
                break;
        }
}

    /**
     * Called when the vehicles have been successfully loaded from the API
     *
     * @param listVehicles | The vehicles loaded from the API
     */
    @Override
    public void onVehiclesLoaded(ArrayList<Vehicle> listVehicles) {
        //When data is added, create an arraylist.add(listVehicles) method to just push the new data
        if (listVehicles != null)
        {
            if (!listVehicles.isEmpty()) {
                vehicleAdapter.addToVehiclesList(listVehicles);
                vehicleAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * The method to bind the click event
     *
     * @param vehicle The clicked item
     */
    @Override
    public void onItemClick(Vehicle vehicle) {
        L.t(MyApplication.getAppContext(), vehicle.getEngine_capacity()+ " more text");
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

