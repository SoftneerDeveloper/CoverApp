package ke.co.coverapp.coverapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.adapters.AssetsAdapter;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.AssetsLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.Assets;

public class AssetsActivity extends AppCompatActivity implements AssetsLoadedListener {
    RecyclerView assetsRecyclerView;
    private ArrayList<Assets> assetsList = new ArrayList<>();
    private AssetsAdapter assetsAdapter;
    private String typeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets);

        // Set toolbar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        typeId = extras.getString("typeId");

        L.m("Type ID: " + typeId);

        // Getting assets
        assetsList = MyApplication.getWritableDatabase().readAsset(typeId);

        L.m("Asset List from DB: " + assetsList.toString());

        // Set and call adapter - Handle all user actions from here.

        assetsRecyclerView = (RecyclerView) findViewById(R.id.assetsRecyclerView);

        assetsAdapter = new AssetsAdapter(AssetsActivity.this, assetsList);

        assetsRecyclerView.setAdapter(assetsAdapter);

        assetsRecyclerView.setLayoutManager(new LinearLayoutManager(AssetsActivity.this));

    }


    @Override
    public void onAssetsLoaded(ArrayList<Assets> assetsList) {

        if (assetsList != null) {
            if (!assetsList.isEmpty()) {
                // Getting assets
                assetsList = MyApplication.getWritableDatabase().readAsset(typeId);
                assetsAdapter.addToAssetsList(assetsList);
                assetsAdapter.notifyDataSetChanged();
            }
        }

    }
}
