package ke.co.coverapp.coverapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ke.co.coverapp.coverapp.Model.HomeBundleCardItemModel;
import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.adapters.HomeBundleCardItemAdapter;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.AssetsLoadedListener;
import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.fragments.HomeCoverInfoViewFragment;
import ke.co.coverapp.coverapp.fragments.HomeCustomViewFragment;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.Assets;
import ke.co.coverapp.coverapp.pojo.Balance;
import ke.co.coverapp.coverapp.tasks.TaskLoadAssets;
import ke.co.coverapp.coverapp.tasks.TaskLoadBalance;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.WALLET_BALANCE;

public class HomeBundleActivity extends AppCompatActivity implements View.OnClickListener, BalanceLoadedListener, AssetsLoadedListener {

    private ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerView1;
    private  RecyclerView.LayoutManager mLayoutManager1;
    private RecyclerView.Adapter mAdapter1;
    private ArrayList<HomeBundleCardItemModel> mDataset1;
    LinearLayout simple_plan_linear, basic_plan_linear, full_plan_linear;
    //, swipeCardsViewLinearLayout;
    //TextView textViewCoverType;
    Context context;
    List<Assets> list_assets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_bundle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = HomeBundleActivity.this;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Saving home data...");
        mProgressDialog.setCancelable(false);

        simple_plan_linear = (LinearLayout) findViewById(R.id.simple_plan_linear);
        basic_plan_linear = (LinearLayout) findViewById(R.id.basic_plan_linear);
        full_plan_linear = (LinearLayout) findViewById(R.id.full_plan_linear);
       // textViewCoverType = (TextView ) findViewById(R.id.textViewCoverType);
       // swipeCardsViewLinearLayout = (LinearLayout) findViewById(R.id.swipeCardsViewLinearLayout);

        simple_plan_linear.setOnClickListener(this);
        basic_plan_linear.setOnClickListener(this);
        full_plan_linear.setOnClickListener(this);
       // swipeCardsViewLinearLayout.setOnClickListener(this);

        // Load assets
        list_assets = MyApplication.getWritableDatabase().readNonCoveredAssets();

        if (list_assets.isEmpty()) {
            // Make API request
            new TaskLoadAssets(this, 0).execute();
        }

        mDataset1 = new ArrayList<HomeBundleCardItemModel>();
        mDataset1.add(new HomeBundleCardItemModel("White","Cost - KES 100 per month","Your Cover- KES 50,000","What's covered - Electronics, home appliances and furniture + accidental injury to third parties and domestic workers up to KES 2,000,000"));
        mDataset1.add(new HomeBundleCardItemModel("Yellow","Cost - KES 350 per month","Your Cover- KES 150,000","What's covered - Electronics, home appliances and furniture + accidental injury to third parties and domestic workers up to KES 2,000,000"));
        mDataset1.add(new HomeBundleCardItemModel("Red","Cost - KES 950 per month","Your Cover- KES 450,000","What's covered - Electronics, home appliances and furniture + accidental injury to third parties and domestic workers up to KES 2,000,000"));

        mRecyclerView1 = (RecyclerView ) findViewById(R.id.recycler_view1);
        mRecyclerView1.setHasFixedSize(true);
        mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView1.setLayoutManager(mLayoutManager1);
        //LinearSnapHelper helper = new LinearSnapHelper();
        //helper.attachToRecyclerView(mRecyclerView1);
        mAdapter1 = new HomeBundleCardItemAdapter(mDataset1);
        mRecyclerView1.setAdapter(mAdapter1);

    }


    private void showDetailsDialog(String title, String desc, String home_meta, float price) {
        FragmentManager fm = getSupportFragmentManager();
        HomeCustomViewFragment homeCustomViewFragment = HomeCustomViewFragment.newInstance(title, desc, home_meta, price);
        homeCustomViewFragment.show(fm, "HomeCustomViewFragment");

        // New home cover fragment
    }

    public void showInfoViewDialog(String type, String desc, float price)  {
        try {
            //  Do database operation
            FragmentManager manager = getSupportFragmentManager();
            HomeCoverInfoViewFragment homeCoverInfoViewFragment = HomeCoverInfoViewFragment.newInstance(type, desc, price);
            homeCoverInfoViewFragment.show(manager, "HomeCoverInfoViewFragment");
        } catch (Exception e){
            try {
                throw new IOException(e.toString());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    private void assetDialog(String desc){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);


        builder.setMessage("You have selected the " + desc + ". Do you want to cover assets already added or would" +
                " you like to add a new assets?");
        builder.setPositiveButton("Add new", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
//                            context.startActivity(new Intent(context, RunTimeTopUpActivity.class));
//                            L.T(HomeBundleActivity.this, "Add new asset selected");
                            // Add new asset
                            context.startActivity(new Intent(context, AddNewAssetHomeCover.class));
                        }

                    }
        ).setNegativeButton("Select added", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.cancel();
//                 L.T(HomeBundleActivity.this, "Select asset from list");
                 context.startActivity(new Intent(context, SelectAssetHomeCover.class));
             }
        }).show();

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        String message, desc = "";
//        RelativeLayout layout_relative_recyclerView;
//        layout_relative_recyclerView = (RelativeLayout ) findViewById(R.id.layout_relative_recyclerView);
//        layout_relative_recyclerView.setOnClickListener(this);
       // String textViewCoverType2 = textViewCoverType.getText().toString();

        // TODO: Get maximum cover costs & monthly premiums dynamically. The table is available online.
        switch (v.getId()) {

            case R.id.simple_plan_linear:
                message = "Cost - KES 100 p.m. Your cover - KES 50,000.";
//                showDetailsDialog("White", message, message, 100);
                desc = "White plan with a monthly premium of KES 100";
//                buyCoverDialog("White", message, 100, desc);
//                assetDialog(message);
                showInfoViewDialog("White", message, 100);
                break;

            case R.id.basic_plan_linear:
                message = "Cost - KES 350 p.m. Maximum cover - KES 150,000.";
//                showDetailsDialog("Yellow", message, message, 300);
                desc = "Yellow plan with a monthly premium of KES 350";
//                buyCoverDialog("Yellow", message, 300, desc);
//                assetDialog(message);
                showInfoViewDialog( "Yellow", message, 350);
                break;

            case R.id.full_plan_linear:
                message = "Cost - KES 950 p.m. Maximum cover - KES 450,000.";
//                showDetailsDialog("Red", message, message, 900);
                desc = "Red plan with a monthly premium of KES 950";
//                buyCoverDialog("Red", message, 900, desc);
//                assetDialog(message);
                showInfoViewDialog( "Red", message, 950);
                break;

        }
    }

    public void buyCoverDialog(final String cover_type, final String message, final int cost, String desc){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Do you want to purchase the " + desc + "?");
        builder.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        // Starting activity
                        Intent buy_cover = new Intent(HomeBundleActivity.this, BuyHomeCover.class);
                        Bundle args = new Bundle();
                        args.putString("cover_type", cover_type);
                        args.putString("message", message);
                        args.putFloat("cost", cost);
                        buy_cover.putExtras(args);
                        startActivity(buy_cover);

                    }

                }
        ).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        }).show();
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

    @Override
    public void onAssetsLoaded(ArrayList<Assets> listFeatures) {
        // The assets are store in the database
        L.t(HomeBundleActivity.this, "Assets loaded.");
        list_assets = MyApplication.getWritableDatabase().readNonCoveredAssets();
    }
}
