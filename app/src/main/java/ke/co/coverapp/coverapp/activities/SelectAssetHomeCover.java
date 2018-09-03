package ke.co.coverapp.coverapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.adapters.SelectAssetsAdapter;
import ke.co.coverapp.coverapp.adapters.SelectAssetsListAdapter;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.AssetCategoriesLoadedListener;
import ke.co.coverapp.coverapp.callbacks.AssetTypesLoadedListener;
import ke.co.coverapp.coverapp.callbacks.AssetsLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.AssetCategories;
import ke.co.coverapp.coverapp.pojo.AssetTypes;
import ke.co.coverapp.coverapp.pojo.Assets;
import ke.co.coverapp.coverapp.tasks.TaskLoadAssetCategories;
import ke.co.coverapp.coverapp.tasks.TaskLoadAssetTypes;
import ke.co.coverapp.coverapp.tasks.TaskLoadAssets;

public class SelectAssetHomeCover extends AppCompatActivity implements View.OnClickListener, AssetsLoadedListener, AssetCategoriesLoadedListener, AssetTypesLoadedListener{
    private ArrayList<Assets> assetsList = new ArrayList<>();
    private SelectAssetsAdapter assetsAdapter;
    private static final String STATE_ACTIVITY = "state_activity";
    RecyclerView assetsRecyclerViewTwo;
    String payment_plan;

    Context context;

    Button buy_cover;

    List<AssetCategories> list_asset_categories;
    List<AssetTypes> list_asset_types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_asset_home_cover);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        context = SelectAssetHomeCover.this;

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            payment_plan = extras.getString("payment_plan");
            L.m("Payment plan selected: " + payment_plan);
        }

        assetsRecyclerViewTwo = (RecyclerView) findViewById(R.id.assetsRecyclerViewTwo);

        assetsAdapter = new SelectAssetsAdapter(SelectAssetHomeCover.this, assetsList);

        assetsRecyclerViewTwo.setAdapter(assetsAdapter);

        assetsRecyclerViewTwo.setLayoutManager(new LinearLayoutManager(SelectAssetHomeCover.this));

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
                L.m("the last id " + assetsList.get(assetsList.size() - 1).getUid());
                new TaskLoadAssets(this, last_id).execute();
            }
        }
        // Update your Adapter to containing the retrieved activities
        assetsAdapter.setAssetsList(assetsList);

        // Load asset categories
        list_asset_categories = MyApplication.getWritableDatabase().readAssetCategories();

        if (list_asset_categories.isEmpty()) {
            new TaskLoadAssetCategories(SelectAssetHomeCover.this).execute();
        }

        // Load asset types
        list_asset_types = MyApplication.getWritableDatabase().readAssetTypes();

        if (list_asset_types.isEmpty()) {
            new TaskLoadAssetTypes(SelectAssetHomeCover.this).execute();
        }

        buy_cover = (Button) findViewById(R.id.button_cover);

        if (buy_cover != null) {
            buy_cover.setOnClickListener(this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_bundle_asset_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handling item menu selection
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent addAsset = new Intent(SelectAssetHomeCover.this, AddNewAssetHomeCover.class);
                addAsset.putExtra("payment_plan", payment_plan);
                startActivity(addAsset);
                return true;

            case android.R.id.home:
                this.finish();
                return true;

            default:
                return true;
        }
    }

    @Override
    public void onClick(View v) {
        // Process items selected
        // Show monthly premiums

        // Accept
//        finalDialog();
        confirmDialog();
    }

    private void confirmDialog() {

        L.m("PAYMENT PLAN: " + payment_plan);

        String items_string = "";

        for (int j = 0; j < assetsList.size(); j++) {
            if(assetsList.get(j).getStatus() != null) {
                if (assetsList.get(j).getStatus().equals("Checked")) {
                    if (items_string == "") {
                        items_string = assetsList.get(j).getName();
                    } else {
                        items_string += ", " + assetsList.get(j).getName();
                    }
                }
            }
        }

        if (items_string != "") {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

            builder.setMessage("You have selected " + items_string + ". Would you like to add another asset?");

            builder.setPositiveButton("Add new", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    // Open add asset activity
                    Intent addNewIntent = new Intent(SelectAssetHomeCover.this, AddNewAssetHomeCover.class);
                    addNewIntent.putExtra("payment_plan", payment_plan);
                    startActivity(addNewIntent);
                }
            });

            builder.setNegativeButton("Buy cover", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                    // Proceed with purchase process
                    finalDialog();
                }
            });

            builder.show();
        } else {
            L.t(context, "Please select at least one asset");
        }
    }

    private void finalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Your selected bundle costs  " + "KES" + " " + "900");

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // buyCover();

                // Check if there is money in the wallet
//                priceCheck();

                // Show successful dialog
                billingRequest();
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

    private void billingRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Automate billing of " + "KES" + " " + "900" + " " + "Monthly" + " from your wallet?");

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
//                automate_billing = 1;
                purchaseProcess();
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
//                automate_billing = 0;
//                purchaseProcess();
            }
        });

        builder.show();
    }

    private void purchaseProcess(){
        L.T(SelectAssetHomeCover.this, "Home cover successfully purchased! Check you email for confirmation");

        // Redirect to Home Bundle Activity
        context.startActivity(new Intent(context, HomeBundleActivity.class));
    }

    @Override
    public void onAssetsLoaded(ArrayList<Assets> assetsList) {

        if (assetsList != null) {
            if (!assetsList.isEmpty()) {
                L.m("assetsList: " + assetsList);
                assetsAdapter.addToAssetsList(assetsList);
            }
        }

    }

    @Override
    public void onAssetCategoriesLoaded(ArrayList<AssetCategories> assetCategories) {
        list_asset_categories = MyApplication.getWritableDatabase().readAssetCategories();
    }

    @Override
    public void onAssetTypesLoaded(ArrayList<AssetTypes> assetTypes) {
        list_asset_types = MyApplication.getWritableDatabase().readAssetTypes();
    }
}
