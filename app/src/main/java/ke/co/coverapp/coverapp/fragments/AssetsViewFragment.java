package ke.co.coverapp.coverapp.fragments;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.AddAssetActivity;
import ke.co.coverapp.coverapp.activities.BuyHomeCover;
import ke.co.coverapp.coverapp.adapters.AssetsAdapter;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.AssetsLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.Assets;
import ke.co.coverapp.coverapp.tasks.TaskLoadAssets;

/**
 * Created by nick on 10/11/17.
 */

public class AssetsViewFragment extends DialogFragment implements View.OnClickListener, AssetsLoadedListener{

    private AssetsAdapter assetsAdapter;
    private ArrayList<Assets> assetsList = new ArrayList<>();
    RecyclerView assetsFragmentRecyclerView;

    LinearLayout no_covers_error;
    Button addAssetsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.assets_custom_view_dialog, container, false);

        addAssetsButton = (Button) view.findViewById(R.id.addAssetsButton);
        addAssetsButton.setOnClickListener(this);

        no_covers_error = (LinearLayout) view.findViewById(R.id.no_covers_error);
        assetsFragmentRecyclerView = (RecyclerView) view.findViewById(R.id.assetsFragmentRecyclerView);

        // Load listener
        new TaskLoadAssets(this, 0).execute();

        // Get assets
        assetsList = MyApplication.getWritableDatabase().readAssets();

        if (assetsList.size() == 0) {
            // Show error message

            // Hide recycler view
        } else {
            // Show assets
            // Inflate the layout for this fragment
            assetsAdapter = new AssetsAdapter(getActivity(), assetsList);
            assetsFragmentRecyclerView.setAdapter(assetsAdapter);
            assetsFragmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            // Hide error message
            no_covers_error.setVisibility(View.GONE);
        }

        return view;
    }

    public AssetsViewFragment() {
    }

    public static AssetsViewFragment newInstance() {
        AssetsViewFragment fragment = new AssetsViewFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

            case R.id.addAssetsButton:
                // Go to claim activity
                L.t(getActivity(), "Add a bed to your asset list. Go to 'My Assets' in the main menu to add a different asset type.");
                Bundle bundle_four = new Bundle();
                bundle_four.putString("category", "2");
                bundle_four.putString("type", "8");
                Intent addAsset = new Intent(getActivity(), AddAssetActivity.class);
                addAsset.putExtras(bundle_four);
                startActivity(addAsset);

                break;

            default:

                break;
        }
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
    public void onAssetsLoaded(ArrayList<Assets> listFeatures) {

    }

    @Override
    public void onResume() {
        super.onResume();
        // Load listener
        new TaskLoadAssets(this, 0).execute();

        // Get assets
        assetsList = MyApplication.getWritableDatabase().readAssets();

        if (assetsList.size() == 0) {
            // Show error message

            // Hide recycler view
        } else {
            // Show assets
            // Inflate the layout for this fragment
            assetsAdapter = new AssetsAdapter(getActivity(), assetsList);
            assetsFragmentRecyclerView.setAdapter(assetsAdapter);
            assetsFragmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            // Hide error message
            no_covers_error.setVisibility(View.GONE);
        }
    }
}
