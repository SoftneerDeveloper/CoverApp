package ke.co.coverapp.coverapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class ElectronicsFragment extends Fragment implements View.OnClickListener, AssetsLoadedListener {
    LinearLayout electronics_phone_linear, electronics_tablet_linear, electronics_laptop_linear, electronics_tv_linear, electronics_musicsystem_linear, electronics_hometheatre_linear, tablet_action_buttons, laptop_asset_buttons, tv_asset_buttons, music_system_asset_buttons, home_theatre_asset_buttons;

    TextView text_tablet_asset, text_laptop_asset, text_tv_asset, text_music_system_asset, text_home_theatre_asset;

    Button btn_view_home_theatre, btn_view_music_systems, btn_view_tv, btn_view_laptops, btn_view_tablets;

    ArrayList<Assets> assetsList = new ArrayList<>();

    List<Assets> tablets;
    List<Assets> laptop;
    List<Assets> tv;
    List<Assets> music_system;
    List<Assets> home_theatre;

    public ElectronicsFragment() {
        // Required empty public constructor
    }

    public static ElectronicsFragment newInstance() {
        ElectronicsFragment fragment = new ElectronicsFragment();
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

        View view =  inflater.inflate(R.layout.fragment_electronics, container, false);

        electronics_phone_linear = (LinearLayout) view.findViewById(R.id.electronics_phone_linear);
        electronics_tablet_linear = (LinearLayout) view.findViewById(R.id.electronics_tablet_linear);
        electronics_laptop_linear = (LinearLayout) view.findViewById(R.id.electronics_laptop_linear);
        electronics_tv_linear = (LinearLayout) view.findViewById(R.id.electronics_tv_linear);
        electronics_musicsystem_linear = (LinearLayout) view.findViewById(R.id.electronics_musicsystem_linear);
        electronics_hometheatre_linear = (LinearLayout) view.findViewById(R.id.electronics_hometheatre_linear);
        tablet_action_buttons = (LinearLayout) view.findViewById(R.id.tablet_action_buttons);
        laptop_asset_buttons = (LinearLayout) view.findViewById(R.id.laptop_asset_buttons);
        tv_asset_buttons = (LinearLayout) view.findViewById(R.id.tv_asset_buttons);
        music_system_asset_buttons = (LinearLayout) view.findViewById(R.id.music_system_asset_buttons);
        home_theatre_asset_buttons = (LinearLayout) view.findViewById(R.id.home_theatre_asset_buttons);

        text_tablet_asset = (TextView) view.findViewById(R.id.text_tablet_asset);
        text_laptop_asset = (TextView) view.findViewById(R.id.text_laptop_asset);
        text_tv_asset = (TextView) view.findViewById(R.id.text_tv_asset);
        text_music_system_asset = (TextView) view.findViewById(R.id.text_music_system_asset);
        text_home_theatre_asset = (TextView) view.findViewById(R.id.text_home_theatre_asset);

        btn_view_home_theatre = (Button) view.findViewById(R.id.btn_view_home_theatre);
        btn_view_music_systems = (Button) view.findViewById(R.id.btn_view_music_systems);
        btn_view_tv = (Button) view.findViewById(R.id.btn_view_tv);
        btn_view_laptops = (Button) view.findViewById(R.id.btn_view_laptops);
        btn_view_tablets = (Button) view.findViewById(R.id.btn_view_tablets);

        electronics_phone_linear.setOnClickListener(this);
        electronics_tablet_linear.setOnClickListener(this);
        electronics_laptop_linear.setOnClickListener(this);
        electronics_tv_linear.setOnClickListener(this);
        electronics_musicsystem_linear.setOnClickListener(this);
        electronics_hometheatre_linear.setOnClickListener(this);
        btn_view_home_theatre.setOnClickListener(this);
        btn_view_music_systems.setOnClickListener(this);
        btn_view_tv.setOnClickListener(this);
        btn_view_laptops.setOnClickListener(this);
        btn_view_tablets.setOnClickListener(this);

        // Load assets
        new TaskLoadAssets(this, 0).execute();

        assetsList = MyApplication.getWritableDatabase().readAssets();

        // Get assets
        tablets = MyApplication.getWritableDatabase().readAsset("2");
        laptop = MyApplication.getWritableDatabase().readAsset("3");
        tv = MyApplication.getWritableDatabase().readAsset("4");
        music_system = MyApplication.getWritableDatabase().readAsset("15");
        home_theatre = MyApplication.getWritableDatabase().readAsset("16");

        if (tablets.size() > 0) {
            // Show view assets button
            tablet_action_buttons.setVisibility(View.VISIBLE);

            electronics_tablet_linear.setClickable(false);

            text_tablet_asset.setText("Click below to manage all your tablet assets.");
        }

        if (laptop.size() > 0) {
            // Show view assets button
            laptop_asset_buttons.setVisibility(View.VISIBLE);

            electronics_laptop_linear.setClickable(false);

            text_laptop_asset.setText("Click below to manage all your laptop assets.");
        }

        if (tv.size() > 0) {
            // Show view assets button
            tv_asset_buttons.setVisibility(View.VISIBLE);

            electronics_tv_linear.setClickable(false);

            text_tv_asset.setText("Click below to manage all your TV assets.");
        }

        if (music_system.size() > 0) {
            // Show view assets button
            music_system_asset_buttons.setVisibility(View.VISIBLE);

            electronics_musicsystem_linear.setClickable(false);

            text_music_system_asset.setText("Click below to manage all your music system assets.");
        }

        if (home_theatre.size() > 0) {
            // Show view assets button
            home_theatre_asset_buttons.setVisibility(View.VISIBLE);

            electronics_hometheatre_linear.setClickable(false);

            text_home_theatre_asset.setText("Click below to manage all your home theatre assets.");
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
            case R.id.electronics_phone_linear:
                PhonesFragment phonesFragment =  new PhonesFragment();
                FragmentTransaction ft1 = this.getFragmentManager().beginTransaction();
                ft1.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                ft1.replace(R.id.content_frame, phonesFragment, "PhonesFragment");
                ft1.addToBackStack("PhonesFragment");
                ft1.commit();
                break;

            case R.id.electronics_tablet_linear:
                Bundle bundle = new Bundle();
                bundle.putString("category", "1");
                bundle.putString("type", "2");
                Intent intent = new Intent(getActivity(), AddAssetActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.electronics_laptop_linear:
                Bundle bundle_two = new Bundle();
                bundle_two.putString("category", "1");
                bundle_two.putString("type", "3");
                Intent intent_two = new Intent(getActivity(), AddAssetActivity.class);
                intent_two.putExtras(bundle_two);
                startActivity(intent_two);
                break;

            case R.id.electronics_tv_linear:
                Bundle bundle_three = new Bundle();
                bundle_three.putString("category", "1");
                bundle_three.putString("type", "4");
                Intent intent_three = new Intent(getActivity(), AddAssetActivity.class);
                intent_three.putExtras(bundle_three);
                startActivity(intent_three);
                break;

            case R.id.electronics_musicsystem_linear:
                Bundle bundle_four = new Bundle();
                bundle_four.putString("category", "1");
                bundle_four.putString("type", "15");
                Intent intent_four = new Intent(getActivity(), AddAssetActivity.class);
                intent_four.putExtras(bundle_four);
                startActivity(intent_four);
                break;

            case R.id.electronics_hometheatre_linear:
                Bundle bundle_five = new Bundle();
                bundle_five.putString("category", "1");
                bundle_five.putString("type", "16");
                Intent intent_five = new Intent(getActivity(), AddAssetActivity.class);
                intent_five.putExtras(bundle_five);
                startActivity(intent_five);
                break;

            case R.id.btn_view_home_theatre:
                Bundle bundle_curtains = new Bundle();
                bundle_curtains.putString("typeId", "16");
                Intent intent_curtains = new Intent(getActivity(), AssetsActivity.class);
                intent_curtains.putExtras(bundle_curtains);
                startActivity(intent_curtains);
                break;

            case R.id.btn_view_music_systems:
                Bundle bundle_music_systems = new Bundle();
                bundle_music_systems.putString("typeId", "15");
                Intent intent_music_systems = new Intent(getActivity(), AssetsActivity.class);
                intent_music_systems.putExtras(bundle_music_systems);
                startActivity(intent_music_systems);
                break;

            case R.id.btn_view_tv:
                Bundle bundle_tv = new Bundle();
                bundle_tv.putString("typeId", "4");
                Intent intent_tv = new Intent(getActivity(), AssetsActivity.class);
                intent_tv.putExtras(bundle_tv);
                startActivity(intent_tv);
                break;

            case R.id.btn_view_laptops:
                Bundle bundle_laptops = new Bundle();
                bundle_laptops.putString("typeId", "3");
                Intent intent_laptops = new Intent(getActivity(), AssetsActivity.class);
                intent_laptops.putExtras(bundle_laptops);
                startActivity(intent_laptops);
                break;

            case R.id.btn_view_tablets:
                Bundle bundle_tablets = new Bundle();
                bundle_tablets.putString("typeId", "2");
                Intent intent_tablets = new Intent(getActivity(), AssetsActivity.class);
                intent_tablets.putExtras(bundle_tablets);
                startActivity(intent_tablets);
                break;

            default:
                break;
        }

    }

    @Override
    public void onAssetsLoaded(ArrayList<Assets> newAssetsList) {
        // Restart fragment
        assetsList = MyApplication.getWritableDatabase().readAssets();

        if (newAssetsList != null) {
            if (!newAssetsList.isEmpty()) {
                L.m("assetsList: " + newAssetsList);
            }
        }
    }
}
