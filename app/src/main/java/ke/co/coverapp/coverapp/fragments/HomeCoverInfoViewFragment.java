package ke.co.coverapp.coverapp.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.BuyHomeCover;
import ke.co.coverapp.coverapp.activities.HomeBundleActivity;
import ke.co.coverapp.coverapp.activities.OpenHomeCoverInfoViewFragment;
import ke.co.coverapp.coverapp.activities.OpenHomeCoverInfoViewFragment2;
import ke.co.coverapp.coverapp.activities.OpenHomeCoverInfoViewFragment3;

/**
 * Created by nick on 8/1/17.
 */

public class HomeCoverInfoViewFragment extends DialogFragment implements View.OnClickListener {
    String cover_type, cover_desc;
    float cover_price;
    Context context;
    TextView maximum_cover,home_title, home_desc, monthly_premium, title_electronics, title_appliances, title_furniture, your_phone_text, spouses_phone_text, tablet_text, laptop_text, tv_text, music_sys_text, home_theatre_text, fridge_text, microwave_text, cooker_text, washing_machine_text, sofa_sets_text, dining_set_text, carpets_text, beds_text, curtains_text, tv_units_text;
    Button button_cancel, btn_continue;
    LinearLayout color_linear;
    ImageView CoverTypeImageView2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getActivity();
        View view = inflater.inflate(R.layout.home_cover_info_view_dialog, container, false);

        return view;
    }

    public HomeCoverInfoViewFragment() {
    }

    public static HomeCoverInfoViewFragment newInstance(String type, String desc, float price) {
        HomeCoverInfoViewFragment fragment = new HomeCoverInfoViewFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("desc", desc);
        args.putFloat("price", price);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Write code to initialize dialog fragment here

        // Initializing layout elements

        // 0. Titles
        maximum_cover = (TextView) view.findViewById(R.id.maximum_cover);
        title_electronics = (TextView) view.findViewById(R.id.title_electronics);
        title_appliances = (TextView) view.findViewById(R.id.title_appliances);
        title_furniture = (TextView) view.findViewById(R.id.title_furniture);

        // 1. Items
        // 1. a. Electronics
        your_phone_text = (TextView) view.findViewById(R.id.your_phone_text);
        spouses_phone_text = (TextView) view.findViewById(R.id.spouses_phone_text);
        tablet_text = (TextView) view.findViewById(R.id.tablet_text);
        laptop_text = (TextView) view.findViewById(R.id.laptop_text);
        tv_text = (TextView) view.findViewById(R.id.tv_text);
        music_sys_text = (TextView) view.findViewById(R.id.music_sys_text);
        home_theatre_text = (TextView) view.findViewById(R.id.home_theatre_text);

        // 1. b Appliances
        fridge_text = (TextView) view.findViewById(R.id.fridge_text);
        microwave_text = (TextView) view.findViewById(R.id.microwave_text);
        cooker_text = (TextView) view.findViewById(R.id.cooker_text);
        washing_machine_text = (TextView) view.findViewById(R.id.washing_machine_text);

        // 1. c Furniture
        sofa_sets_text = (TextView) view.findViewById(R.id.sofa_sets_text);
        dining_set_text = (TextView) view.findViewById(R.id.dining_set_text);
        carpets_text = (TextView) view.findViewById(R.id.carpets_text);
        beds_text = (TextView) view.findViewById(R.id.beds_text);
        curtains_text = (TextView) view.findViewById(R.id.curtains_text);
        tv_units_text = (TextView) view.findViewById(R.id.tv_units_text);

        // 2. TextViews
        home_title = (TextView) view.findViewById(R.id.home_title);
        home_desc = (TextView) view.findViewById(R.id.home_desc);
        monthly_premium = (TextView) view.findViewById(R.id.monthly_premium);

        // 3. Buttons
        button_cancel = (Button) view.findViewById(R.id.button_cancel);
        btn_continue = (Button) view.findViewById(R.id.button_continue);

        color_linear = (LinearLayout ) view.findViewById(R.id.color_linear);
        CoverTypeImageView2 = (ImageView )view.findViewById(R.id.CoverTypeImageView2);

        button_cancel.setOnClickListener(this);
        btn_continue.setOnClickListener(this);

        // Get data from activity
        cover_type = getArguments().getString("type");
        cover_desc = getArguments().getString("desc");
        cover_price = getArguments().getFloat("price", 0);

        // Set values according to selection
        home_title.setText(cover_type);
        home_desc.setText(cover_desc);
        monthly_premium.setText("KES " + cover_price);



        // TODO: Make this dynamic. We already have all the data in the Online Database. If a change is made there, then it should reflect here as well.

        // Show the different prices for the different covers
        if (cover_type == "White") {
            //color_linear.setBackgroundColor(Color.parseColor("#ffffff"));
            home_desc.setTextColor(Color.parseColor("#000000"));
            CoverTypeImageView2.setImageResource(R.drawable.whitecover);
            // Titles
            maximum_cover.setText("KES 50,000");
            title_electronics.setText("Electronics (Max cover - KES 15,000)");
            title_appliances.setText("Appliances (Max cover - KES 15,000)");
            title_furniture.setText("Furniture (Max cover - KES 20,000)");

            // Show what's covered under that type
            // 1. Electronics
            your_phone_text.setText("Your Phone - up to KES 5,000");
            spouses_phone_text.setVisibility(View.GONE);
            tablet_text.setText("Tablet- up to KES 5,000");
            laptop_text.setText("Laptop - up to KES 10,000");
            tv_text.setText("TV - up to KES 5,000");
            music_sys_text.setText("Music system- up to KES 5,000");
            home_theatre_text.setText("Home theatre - up to KES 5,000");

            // 2. Appliances
            fridge_text.setText("Fridge - up to KES 10,000");
            microwave_text.setText("Microwave - up to KES 5,000");
            cooker_text.setText("Cooker - up to KES 5,000");
            washing_machine_text.setVisibility(View.GONE);

            // 3. Furniture
            sofa_sets_text.setText("Sofa sets - up to KES 5,000");
            dining_set_text.setText("Dining set - up to KES 5,000");
            carpets_text.setText("Carpets - up to KES 2,500");
            beds_text.setText("Beds - up to KES 5,000");
            curtains_text.setText("Curtains - up to KES 2,500");
            tv_units_text.setText("TV units - up to KES 5,000");

        } else if(cover_type == "Yellow") {
            //color_linear.setBackgroundColor(Color.parseColor("#FAA92F"));
            home_desc.setTextColor(Color.parseColor("#000000"));
            CoverTypeImageView2.setImageResource(R.drawable.yellowcover);
            // Titles
            maximum_cover.setText("KES 150,000");
            title_electronics.setText("Electronics (Max cover - KES 50,000)");
            title_appliances.setText("Appliances (Max cover - KES 50,000)");
            title_furniture.setText("Furniture - (Max cover - KES 50,000)");

            // Show what's covered under Yellow
            // 1. Electronics
            your_phone_text.setText("Your Phone - up to KES 10,000");
            spouses_phone_text.setVisibility(View.GONE);
            tablet_text.setText("Tablet- up to KES 10,000");
            laptop_text.setText("Laptop - up to KES 15,000");
            tv_text.setText("TV - up to KES 15,000");
            music_sys_text.setText("Music system- up to KES 15,000");
            home_theatre_text.setText("Home theatre - up to KES 15,000");

            // 2. Appliances
            fridge_text.setText("Fridge - up to KES 20,000");
            microwave_text.setText("Microwave - up to KES 10,000");
            cooker_text.setText("Cooker - up to KES 10,000");
            washing_machine_text.setText("Washing machine - up to KES 20,000");

            // 3. Furniture
            sofa_sets_text.setText("Sofa sets - up to KES 10,000");
            dining_set_text.setText("Dining set - up to KES 10,000");
            carpets_text.setText("Carpets - up to KES 5,000");
            beds_text.setText("Beds - up to KES 10,000");
            curtains_text.setText("Curtains - up to KES 5,000");
            tv_units_text.setText("TV units - up to KES 10,000");

        } else if(cover_type == "Red") {
            //color_linear.setBackgroundColor(Color.parseColor("#BD3227"));
            home_desc.setTextColor(Color.parseColor("#ffffff"));
            //home_title.setTextColor(Color.parseColor("#ffffff"));
            CoverTypeImageView2.setImageResource(R.drawable.redcover);
            // Titles
            maximum_cover.setText("KES 450,000");
            title_electronics.setText("Electronics (Max cover - KES 150,000)");
            title_appliances.setText("Appliances (Max cover - KES 150,000)");
            title_furniture.setText("Furniture - (Max cover - KES 150,000)");

            // Show what's covered under Red
            // 1. Electronics
            your_phone_text.setText("Your Phone - up to KES 20,000");
            spouses_phone_text.setText("Spouse's Phone - up to KES 20,000");
            tablet_text.setText("Tablet- up to KES 20,000");
            laptop_text.setText("Laptop - up to KES 30,000");
            tv_text.setText("TV - up to KES 30,000");
            music_sys_text.setText("Music system- up to KES 30,000");
            home_theatre_text.setText("Home theatre - up to KES 30,000");

            // 2. Appliances
            fridge_text.setText("Fridge - up to KES 50,000");
            microwave_text.setText("Microwave - up to KES 20,000");
            cooker_text.setText("Cooker - up to KES 40,000");
            washing_machine_text.setText("Washing machine - up to KES 40,000");

            // 3. Furniture
            sofa_sets_text.setText("Sofa sets - up to KES 40,000");
            dining_set_text.setText("Dining set - up to KES 40,000");
            carpets_text.setText("Carpets - up to KES 20,000");
            beds_text.setText("Beds - up to KES 40,000");
            curtains_text.setText("Curtains - up to KES 20,000");
            tv_units_text.setText("TV units - up to KES 20,000");

        } else {
            // If no known cover is passed
            // Show default prices
            // TODO: Add error message layout
        }

    }

    public void onBackPressed(View view) {

        Intent intent = new Intent(view.getContext(), HomeBundleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        view.getContext().startActivity(intent);

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
            case R.id.button_cancel:
                Intent intent = new Intent(view.getContext(), HomeBundleActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                view.getContext().startActivity(intent);
                this.dismiss();
                break;

            case R.id.button_continue:
                // Show activity
                Intent buy_cover = new Intent(getActivity(), BuyHomeCover.class);
                Bundle args = new Bundle();
                args.putString("cover_type", cover_type);
                args.putString("message", cover_desc);
                args.putFloat("cost", cover_price);
                buy_cover.putExtras(args);
                startActivity(buy_cover);
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
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode,
                                 android.view.KeyEvent event) {

                if ((keyCode ==  android.view.KeyEvent.KEYCODE_BACK))
                {
                    //This is the filter
                    if (event.getAction()!=KeyEvent.ACTION_DOWN)
                        return true;
                    else
                    {
                        Intent intent = new Intent(getContext(), HomeBundleActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getContext().startActivity(intent);
                        dismiss();
                        return true; // pretend we've processed it
                    }
                }
                else
                    return false; // pass on to be processed as normal
            }
        });
    }

    }

