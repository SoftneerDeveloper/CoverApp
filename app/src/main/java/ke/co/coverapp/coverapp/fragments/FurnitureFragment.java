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

public class FurnitureFragment extends Fragment implements View.OnClickListener, AssetsLoadedListener {
    LinearLayout furniture_sofa_set_linear, furniture_dining_set_linear, furniture_carpets_linear, furniture_beds_linear, furniture_curtains_linear, furniture_tv_units_linear, sofa_set_asset_buttons, dining_set_asset_buttons, carpet_asset_buttons, bed_asset_buttons, curtain_asset_buttons, tv_units_asset_buttons;

    TextView text_view_sofa_set, text_view_dining_set, text_view_carpets, text_view_beds, text_view_curtains, text_view_tv_units;

    Button btn_view_sofa_set, btn_view_dining_set, btn_view_carpets, btn_view_beds, btn_view_curtains, btn_view_tv_units;

    List<Assets> sofa_sets, dining_sets, carpets, beds, curtains, tv_units;

    public FurnitureFragment() {
        // Required empty public constructor
    }

    public static FurnitureFragment newInstance() {
        FurnitureFragment fragment = new FurnitureFragment();
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

        View view =  inflater.inflate(R.layout.fragment_furniture, container, false);

        // Reload assets
        new TaskLoadAssets(this, 0).execute();

        // Get assets
        sofa_sets = MyApplication.getWritableDatabase().readAsset("5");
        dining_sets = MyApplication.getWritableDatabase().readAsset("6");
        carpets = MyApplication.getWritableDatabase().readAsset("7");
        beds = MyApplication.getWritableDatabase().readAsset("8");
        curtains = MyApplication.getWritableDatabase().readAsset("9");
        tv_units = MyApplication.getWritableDatabase().readAsset("10");

        furniture_sofa_set_linear = (LinearLayout) view.findViewById(R.id.furniture_sofa_set_linear);
        furniture_dining_set_linear = (LinearLayout) view.findViewById(R.id.furniture_dining_set_linear);
        furniture_carpets_linear = (LinearLayout) view.findViewById(R.id.furniture_carpets_linear);
        furniture_beds_linear = (LinearLayout) view.findViewById(R.id.furniture_beds_linear);
        furniture_curtains_linear = (LinearLayout) view.findViewById(R.id.furniture_curtains_linear);
        furniture_tv_units_linear = (LinearLayout) view.findViewById(R.id.furniture_tv_units_linear);

        sofa_set_asset_buttons = (LinearLayout) view.findViewById(R.id.sofa_set_asset_buttons);
        dining_set_asset_buttons = (LinearLayout) view.findViewById(R.id.dining_set_asset_buttons);
        carpet_asset_buttons = (LinearLayout) view.findViewById(R.id.carpet_asset_buttons);
        bed_asset_buttons = (LinearLayout) view.findViewById(R.id.bed_asset_buttons);
        curtain_asset_buttons = (LinearLayout) view.findViewById(R.id.curtain_asset_buttons);
        tv_units_asset_buttons = (LinearLayout) view.findViewById(R.id.tv_units_asset_buttons);

        text_view_sofa_set = (TextView) view.findViewById(R.id.text_view_sofa_set);
        text_view_dining_set = (TextView) view.findViewById(R.id.text_view_dining_set);
        text_view_carpets = (TextView) view.findViewById(R.id.text_view_carpets);
        text_view_beds = (TextView) view.findViewById(R.id.text_view_beds);
        text_view_curtains = (TextView) view.findViewById(R.id.text_view_curtains);
        text_view_tv_units = (TextView) view.findViewById(R.id.text_view_tv_units);

        btn_view_sofa_set = (Button) view.findViewById(R.id.btn_view_sofa_set);
        btn_view_dining_set = (Button) view.findViewById(R.id.btn_view_dining_set);
        btn_view_carpets = (Button) view.findViewById(R.id.btn_view_carpets);
        btn_view_beds = (Button) view.findViewById(R.id.btn_view_beds);
        btn_view_curtains = (Button) view.findViewById(R.id.btn_view_curtains);
        btn_view_tv_units = (Button) view.findViewById(R.id.btn_view_tv_units);

        furniture_sofa_set_linear.setOnClickListener(this);
        furniture_dining_set_linear.setOnClickListener(this);
        furniture_carpets_linear.setOnClickListener(this);
        furniture_beds_linear.setOnClickListener(this);
        furniture_curtains_linear.setOnClickListener(this);
        furniture_tv_units_linear.setOnClickListener(this);

        btn_view_sofa_set.setOnClickListener(this);
        btn_view_dining_set.setOnClickListener(this);
        btn_view_carpets.setOnClickListener(this);
        btn_view_beds.setOnClickListener(this);
        btn_view_curtains.setOnClickListener(this);
        btn_view_tv_units.setOnClickListener(this);

        if (sofa_sets.size() > 0) {
            // Show view assets button
            sofa_set_asset_buttons.setVisibility(View.VISIBLE);

            furniture_sofa_set_linear.setClickable(false);

            text_view_sofa_set.setText("Click below to manage all your sofa set assets.");
        }

        if (dining_sets.size() > 0) {
            // Show view assets button
            dining_set_asset_buttons.setVisibility(View.VISIBLE);

            furniture_dining_set_linear.setClickable(false);

            text_view_dining_set.setText("Click below to manage all your sofa set assets.");
        }

        if (carpets.size() > 0) {
            // Show view assets button
            carpet_asset_buttons.setVisibility(View.VISIBLE);

            furniture_carpets_linear.setClickable(false);

            text_view_carpets.setText("Click below to manage all your sofa set assets.");
        }

        if (beds.size() > 0) {
            // Show view assets button
            bed_asset_buttons.setVisibility(View.VISIBLE);

            furniture_beds_linear.setClickable(false);

            text_view_beds.setText("Click below to manage all your sofa set assets.");
        }

        if (curtains.size() > 0) {
            // Show view assets button
            curtain_asset_buttons.setVisibility(View.VISIBLE);

            furniture_curtains_linear.setClickable(false);

            text_view_curtains.setText("Click below to manage all your sofa set assets.");
        }

        if (tv_units.size() > 0) {
            // Show view assets button
            tv_units_asset_buttons.setVisibility(View.VISIBLE);

            furniture_tv_units_linear.setClickable(false);

            text_view_tv_units.setText("Click below to manage all your sofa set assets.");
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
            case R.id.furniture_sofa_set_linear:
                Bundle bundle = new Bundle();
                bundle.putString("category", "2");
                bundle.putString("type", "5");
                Intent intent = new Intent(getActivity(), AddAssetActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.furniture_dining_set_linear:
                Bundle bundle_two = new Bundle();
                bundle_two.putString("category", "2");
                bundle_two.putString("type", "6");
                Intent intent_two = new Intent(getActivity(), AddAssetActivity.class);
                intent_two.putExtras(bundle_two);
                startActivity(intent_two);
                break;

            case R.id.furniture_carpets_linear:
                Bundle bundle_three = new Bundle();
                bundle_three.putString("category", "2");
                bundle_three.putString("type", "7");
                Intent intent_three = new Intent(getActivity(), AddAssetActivity.class);
                intent_three.putExtras(bundle_three);
                startActivity(intent_three);
                break;

            case R.id.furniture_beds_linear:
                Bundle bundle_four = new Bundle();
                bundle_four.putString("category", "2");
                bundle_four.putString("type", "8");
                Intent intent_four = new Intent(getActivity(), AddAssetActivity.class);
                intent_four.putExtras(bundle_four);
                startActivity(intent_four);
                break;

            case R.id.furniture_curtains_linear:
                Bundle bundle_five = new Bundle();
                bundle_five.putString("category", "2");
                bundle_five.putString("type", "9");
                Intent intent_five = new Intent(getActivity(), AddAssetActivity.class);
                intent_five.putExtras(bundle_five);
                startActivity(intent_five);
                break;

            case R.id.furniture_tv_units_linear:
                Bundle bundle_six = new Bundle();
                bundle_six.putString("category", "2");
                bundle_six.putString("type", "10");
                Intent intent_six = new Intent(getActivity(), AddAssetActivity.class);
                intent_six.putExtras(bundle_six);
                startActivity(intent_six);
                break;

            case R.id.btn_view_sofa_set:
                Bundle bundle_sofa_set = new Bundle();
                bundle_sofa_set.putString("typeId", "5");
                Intent intent_sofa_set = new Intent(getActivity(), AssetsActivity.class);
                intent_sofa_set.putExtras(bundle_sofa_set);
                startActivity(intent_sofa_set);
                break;

            case R.id.btn_view_dining_set:
                Bundle bundle_dining_sets = new Bundle();
                bundle_dining_sets.putString("typeId", "6");
                Intent intent_dining_sets = new Intent(getActivity(), AssetsActivity.class);
                intent_dining_sets.putExtras(bundle_dining_sets);
                startActivity(intent_dining_sets);
                break;

            case R.id.btn_view_carpets:
                Bundle bundle_carpets = new Bundle();
                bundle_carpets.putString("typeId", "7");
                Intent intent_carpets = new Intent(getActivity(), AssetsActivity.class);
                intent_carpets.putExtras(bundle_carpets);
                startActivity(intent_carpets);
                break;

            case R.id.btn_view_beds:
                Bundle bundle_beds = new Bundle();
                bundle_beds.putString("typeId", "8");
                Intent intent_beds = new Intent(getActivity(), AssetsActivity.class);
                intent_beds.putExtras(bundle_beds);
                startActivity(intent_beds);
                break;

            case R.id.btn_view_curtains:
                Bundle bundle_curtains = new Bundle();
                bundle_curtains.putString("typeId", "9");
                Intent intent_curtains = new Intent(getActivity(), AssetsActivity.class);
                intent_curtains.putExtras(bundle_curtains);
                startActivity(intent_curtains);
                break;

            case R.id.btn_view_tv_units:
                Bundle bundle_tv_units = new Bundle();
                bundle_tv_units.putString("typeId", "10");
                Intent intent_tv_units = new Intent(getActivity(), AssetsActivity.class);
                intent_tv_units.putExtras(bundle_tv_units);
                startActivity(intent_tv_units);
                break;

            default:
                break;
        }

    }

    @Override
    public void onAssetsLoaded(ArrayList<Assets> listFeatures) {
        // Do something with the newly loaded assets
        // Restart fragment
//        getFragmentManager().beginTransaction().replace(R.id.content_frame, FurnitureFragment.newInstance()).commit();
    }
}
