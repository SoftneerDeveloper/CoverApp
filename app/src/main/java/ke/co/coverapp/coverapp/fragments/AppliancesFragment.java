package ke.co.coverapp.coverapp.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.AddAssetActivity;
import ke.co.coverapp.coverapp.activities.AssetsActivity;
import ke.co.coverapp.coverapp.activities.MainActivity;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.AssetsLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.Assets;
import ke.co.coverapp.coverapp.tasks.TaskLoadAssets;

/**
 * Created by nick on 8/14/17.
 */

public class AppliancesFragment extends Fragment implements View.OnClickListener, AssetsLoadedListener {
    LinearLayout appliances_cooker_linear, appliances_fridge_linear, appliances_microwave_linear, appliances_washing_machine_linear, fridge_asset_buttons, microwave_asset_buttons, washing_machine_asset_buttons, cooker_asset_buttons;

    TextView text_fridge_asset, text_microwave_assets, text_washing_machine_asset, text_view_cooker;
    Button btn_view_fridge, btn_view_microwave, btn_view_washing_machine, btn_view_cookers;

    List<Assets> fridge;
    List<Assets> microwave;
    List<Assets> washing_machine;
    List<Assets> cooker;

    public AppliancesFragment() {
        // Required empty public constructor
    }

    public static AppliancesFragment newInstance() {
        AppliancesFragment fragment = new AppliancesFragment();
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

        View view =  inflater.inflate(R.layout.fragment_appliances, container, false);

        // Reload assets
        new TaskLoadAssets(this, 0).execute();

        appliances_fridge_linear = (LinearLayout) view.findViewById(R.id.appliances_fridge_linear);
        appliances_microwave_linear = (LinearLayout) view.findViewById(R.id.appliances_microwave_linear);
        appliances_washing_machine_linear = (LinearLayout) view.findViewById(R.id.appliances_washing_machine_linear);
        appliances_cooker_linear = (LinearLayout) view.findViewById(R.id.appliances_cooker_linear);

        fridge_asset_buttons = (LinearLayout) view.findViewById(R.id.fridge_asset_buttons);
        microwave_asset_buttons = (LinearLayout) view.findViewById(R.id.microwave_asset_buttons);
        washing_machine_asset_buttons = (LinearLayout) view.findViewById(R.id.washing_machine_asset_buttons);
        cooker_asset_buttons = (LinearLayout) view.findViewById(R.id.cooker_asset_buttons);

        text_fridge_asset = (TextView) view.findViewById(R.id.text_fridge_asset);
        text_microwave_assets = (TextView) view.findViewById(R.id.text_microwave_assets);
        text_washing_machine_asset = (TextView) view.findViewById(R.id.text_washing_machine_asset);
        text_view_cooker = (TextView) view.findViewById(R.id.text_view_cooker);

        btn_view_fridge = (Button) view.findViewById(R.id.btn_view_fridge);
        btn_view_microwave = (Button) view.findViewById(R.id.btn_view_microwave);
        btn_view_washing_machine = (Button) view.findViewById(R.id.btn_view_washing_machine);
        btn_view_cookers = (Button) view.findViewById(R.id.btn_view_cookers);

        appliances_fridge_linear.setOnClickListener(this);
        appliances_microwave_linear.setOnClickListener(this);
        appliances_washing_machine_linear.setOnClickListener(this);
        appliances_cooker_linear.setOnClickListener(this);

        btn_view_fridge.setOnClickListener(this);
        btn_view_microwave.setOnClickListener(this);
        btn_view_washing_machine.setOnClickListener(this);
        btn_view_cookers.setOnClickListener(this);

        // Getting assets from database
        fridge = MyApplication.getWritableDatabase().readAsset("11");
        microwave = MyApplication.getWritableDatabase().readAsset("12");
        washing_machine = MyApplication.getWritableDatabase().readAsset("13");
        cooker = MyApplication.getWritableDatabase().readAsset("17");

        if (fridge.size() > 0) {
            // Show view assets button
            fridge_asset_buttons.setVisibility(View.VISIBLE);

            appliances_fridge_linear.setClickable(false);

            text_fridge_asset.setText("Click below to manage all your fridge assets.");
        }

        if (microwave.size() > 0) {
            // Show view assets button
            microwave_asset_buttons.setVisibility(View.VISIBLE);

            appliances_microwave_linear.setClickable(false);

            text_microwave_assets.setText("Click below to manage all your microwave assets.");
        }


        if (washing_machine.size() > 0) {
            // Show view assets button
            washing_machine_asset_buttons.setVisibility(View.VISIBLE);

            appliances_washing_machine_linear.setClickable(false);

            text_washing_machine_asset.setText("Click below to manage all your washing machine assets.");
        }


        if (cooker.size() > 0) {
            // Show view assets button
            cooker_asset_buttons.setVisibility(View.VISIBLE);

            appliances_cooker_linear.setClickable(false);

            text_view_cooker.setText("Click below to manage all your cooker assets.");
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
//        if (id == R.id.action_settings) {
//            ((MainActivity) getActivity()).displayView(R.id.nav_settings);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.appliances_fridge_linear:
                Bundle bundle = new Bundle();
                bundle.putString("category", "3");
                bundle.putString("type", "11");
                Intent intent = new Intent(getActivity(), AddAssetActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.appliances_microwave_linear:
                Bundle bundle_two = new Bundle();
                bundle_two.putString("category", "3");
                bundle_two.putString("type", "12");
                Intent intent_two = new Intent(getActivity(), AddAssetActivity.class);
                intent_two.putExtras(bundle_two);
                startActivity(intent_two);
                break;

            case R.id.appliances_washing_machine_linear:
                Bundle bundle_three = new Bundle();
                bundle_three.putString("category", "3");
                bundle_three.putString("type", "13");
                Intent intent_three = new Intent(getActivity(), AddAssetActivity.class);
                intent_three.putExtras(bundle_three);
                startActivity(intent_three);
                break;

            case R.id.appliances_cooker_linear:
                Bundle bundle_four = new Bundle();
                bundle_four.putString("category", "3");
                bundle_four.putString("type", "17");
                Intent intent_four = new Intent(getActivity(), AddAssetActivity.class);
                intent_four.putExtras(bundle_four);
                startActivity(intent_four);
                break;

            case R.id.btn_view_fridge:
                Bundle bundle_fridge = new Bundle();
                bundle_fridge.putString("typeId", "11");
                Intent intent_fridge = new Intent(getActivity(), AssetsActivity.class);
                intent_fridge.putExtras(bundle_fridge);
                startActivity(intent_fridge);
                break;

            case R.id.btn_view_microwave:
                Bundle bundle_microwave = new Bundle();
                bundle_microwave.putString("typeId", "12");
                Intent intent_microwave = new Intent(getActivity(), AssetsActivity.class);
                intent_microwave.putExtras(bundle_microwave);
                startActivity(intent_microwave);
                break;

            case R.id.btn_view_washing_machine:
                Bundle bundle_washing_machine = new Bundle();
                bundle_washing_machine.putString("typeId", "13");
                Intent intent_washing_machine = new Intent(getActivity(), AssetsActivity.class);
                intent_washing_machine.putExtras(bundle_washing_machine);
                startActivity(intent_washing_machine);
                break;

            case R.id.btn_view_cookers:
                Bundle bundle_cookers = new Bundle();
                bundle_cookers.putString("typeId", "17");
                Intent intent_cookers = new Intent(getActivity(), AssetsActivity.class);
                intent_cookers.putExtras(bundle_cookers);
                startActivity(intent_cookers);
                break;

            default:
                break;
        }
    }

    @Override
    public void onAssetsLoaded(ArrayList<Assets> assetsList) {
        // Restart fragment
//        getFragmentManager().beginTransaction().replace(R.id.content_frame, AppliancesFragment.newInstance()).commit();
    }
}
