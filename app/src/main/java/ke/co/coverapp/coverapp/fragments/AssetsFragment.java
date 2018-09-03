package ke.co.coverapp.coverapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.MainActivity;
import ke.co.coverapp.coverapp.adapters.AssetsAdapter;
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
import ke.co.coverapp.coverapp.utility.SortUtil;

public class AssetsFragment extends Fragment implements View.OnClickListener, AssetsLoadedListener, AssetCategoriesLoadedListener, AssetTypesLoadedListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ArrayList<Assets> assetsList = new ArrayList<>();
    private AssetsAdapter assetsAdapter;
    Button button_to_edit_asset, button_to_add_asset;
    private static final String STATE_ACTIVITY = "state_activity";
    RecyclerView assetsRecyclerView;
    private SortUtil mSorter = new SortUtil();
    Spinner assets_spinner;

    List<AssetCategories> list_asset_categories;
    List<AssetTypes> list_asset_types;

    public AssetsFragment() {
        // Required empty public constructor
    }

    public static AssetsFragment newInstance(String param1, String param2) {
        AssetsFragment fragment = new AssetsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_assets, container, false);
//        button_to_edit_asset = (Button) view.findViewById(R.id.button_to_edit_asset) ;
        button_to_add_asset  = (Button) view.findViewById(R.id.button_to_add_asset) ;
//        button_to_edit_asset.setOnClickListener(this);
        button_to_add_asset.setOnClickListener(this);

        assetsRecyclerView = (RecyclerView) view.findViewById(R.id.assetsRecyclerView);

        assetsAdapter = new AssetsAdapter(getActivity(), assetsList);

        assetsRecyclerView.setAdapter(assetsAdapter);

        assetsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
            new TaskLoadAssetCategories(AssetsFragment.this).execute();
        }

        // Load asset types
        list_asset_types = MyApplication.getWritableDatabase().readAssetTypes();

        if (list_asset_types.isEmpty()) {
            new TaskLoadAssetTypes(AssetsFragment.this).execute();
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


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_to_add_asset:
                ((MainActivity) getActivity()).displayView(R.id.button_to_add_asset);
                break;
//            case R.id.button_to_edit_asset:
//                ((MainActivity) getActivity()).displayView(R.id.button_to_edit_asset);
//                break;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save the activity list to a parcelable prior to rotation or configuration change
        outState.putParcelableArrayList(STATE_ACTIVITY, assetsList);
    }


    /**
     * Shows assets a user has saved
     */
    @Override
    public void onAssetsLoaded(ArrayList<Assets> assetsList) {

        if (assetsList != null) {
            if (!assetsList.isEmpty()) {
                L.m("assetsList: " + assetsList);
                assetsAdapter.addToAssetsList(assetsList);
                assetsAdapter.notifyDataSetChanged();
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
