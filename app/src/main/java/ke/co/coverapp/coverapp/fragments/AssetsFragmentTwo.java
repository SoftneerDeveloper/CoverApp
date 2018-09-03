package ke.co.coverapp.coverapp.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.MainActivity;
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

/**
 * Created by nick on 8/7/17.
 */

public class AssetsFragmentTwo extends Fragment implements View.OnClickListener, AssetsLoadedListener, AssetTypesLoadedListener, AssetCategoriesLoadedListener {
    LinearLayout assets_electronics_linear, assets_appliances_linear, assets_furniture_linear;
    private ArrayList<Assets> assetsList = new ArrayList<>();
    List<AssetCategories> list_asset_categories;
    List<AssetTypes> list_asset_types;
    Button viewAllAssetsBtn;

    public AssetsFragmentTwo() {
        // Required empty public constructor
    }

    public static AssetsFragmentTwo newInstance() {
        AssetsFragmentTwo fragment = new AssetsFragmentTwo();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_assets_two, container, false);

        // Load assets
        new TaskLoadAssets(this, 0).execute();

        assets_electronics_linear = (LinearLayout) view.findViewById(R.id.assets_electronics_linear);
        assets_appliances_linear = (LinearLayout) view.findViewById(R.id.assets_appliances_linear);
        assets_furniture_linear = (LinearLayout) view.findViewById(R.id.assets_furniture_linear);

        viewAllAssetsBtn = (Button) view.findViewById(R.id.viewAllAssetsBtn);

        assets_electronics_linear.setOnClickListener(this);
        assets_appliances_linear.setOnClickListener(this);
        assets_furniture_linear.setOnClickListener(this);

        viewAllAssetsBtn.setOnClickListener(this);

        //if this fragment starts for the first time, load the list of activities from a database
        assetsList = MyApplication.getWritableDatabase().readAssets();
        //if the database is empty, trigger an AsyncTask to download activity list from the API
        if (assetsList.isEmpty()) {
            new TaskLoadAssets(this, 0).execute();
        }

        // Load asset categories
        list_asset_categories = MyApplication.getWritableDatabase().readAssetCategories();

        if (list_asset_categories.isEmpty()) {
            new TaskLoadAssetCategories(AssetsFragmentTwo.this).execute();
        }

        // Load asset types
        list_asset_types = MyApplication.getWritableDatabase().readAssetTypes();

        if (list_asset_types.isEmpty()) {
            new TaskLoadAssetTypes(AssetsFragmentTwo.this).execute();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ((MainActivity) getActivity()).createDialog();
            return true;
        }
        if (id == R.id.action_settings) {
            ((MainActivity) getActivity()).displayView(R.id.nav_settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showViewAssetsFragment()  {
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        AssetsViewFragment assetsViewFragment = AssetsViewFragment.newInstance();
        assetsViewFragment.show(manager, "AssetsViewFragment");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.assets_electronics_linear:
                ElectronicsFragment electronicsFragment =  new ElectronicsFragment();
                FragmentTransaction ft = this.getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                ft.replace(R.id.content_frame, electronicsFragment, "ElectronicsFragment");
                ft.addToBackStack("ElectronicsFragment");
                ft.commit();

                break;

            case R.id.assets_appliances_linear:
                AppliancesFragment appliancesFragment =  new AppliancesFragment();
                FragmentTransaction ft1 = this.getFragmentManager().beginTransaction();
                ft1.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                ft1.replace(R.id.content_frame, appliancesFragment, "AppliancesFragment");
                ft1.addToBackStack("AppliancesFragment");
                ft1.commit();

                break;

            case R.id.assets_furniture_linear:
                FurnitureFragment furnitureFragment =  new FurnitureFragment();
                FragmentTransaction ft2 = this.getFragmentManager().beginTransaction();
                ft2.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                ft2.replace(R.id.content_frame, furnitureFragment, "FurnitureFragment");
                ft2.addToBackStack("FurnitureFragment");
                ft2.commit();

                break;

            case R.id.viewAllAssetsBtn:
                // TODO: Pass relevant message to display on the fragment description?
                showViewAssetsFragment();

                break;

            default:
                break;
        }
    }

    @Override
    public void onAssetsLoaded(ArrayList<Assets> newAssetsList) {
//        L.t(getActivity(), "Assets loaded.");
        assetsList = MyApplication.getWritableDatabase().readAssets();
    }

    @Override
    public void onAssetTypesLoaded(ArrayList<AssetTypes> assetTypes) {
        list_asset_types = MyApplication.getWritableDatabase().readAssetTypes();
    }

    @Override
    public void onAssetCategoriesLoaded(ArrayList<AssetCategories> assetCategories) {
        list_asset_categories = MyApplication.getWritableDatabase().readAssetCategories();
    }
}
