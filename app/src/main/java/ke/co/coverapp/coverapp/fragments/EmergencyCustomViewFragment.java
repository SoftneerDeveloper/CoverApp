package ke.co.coverapp.coverapp.fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ke.co.coverapp.coverapp.R;

/**
 * Created by Clifford Owino on 3/27/2017.
 */

public class EmergencyCustomViewFragment extends DialogFragment implements View.OnClickListener{

    TextView emergency_title, emergency_desc,  emergency_meta;
    Button button_cancel;
    ImageView product_icon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.emergency_custom_view_dialog, container);
    }

    public EmergencyCustomViewFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead
    }

    public static EmergencyCustomViewFragment newInstance(int icon,  String title, String desc, String home_meta) {
        EmergencyCustomViewFragment frag = new EmergencyCustomViewFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("desc", desc);
        args.putString("home_meta", home_meta);
        args.putInt("drawable", icon);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        emergency_title = (TextView) view.findViewById(R.id.emergency_title);
        emergency_desc = (TextView) view.findViewById(R.id.emergency_desc);
        emergency_meta = (TextView) view.findViewById(R.id.emergency_meta);
        button_cancel = (Button) view.findViewById(R.id.button_cancel);
        product_icon = (ImageView) view.findViewById(R.id.product_icon);
        button_cancel.setOnClickListener(this);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Emergency Cover");
        String desc = getArguments().getString("desc", "Emergency Cover");
        String meta = getArguments().getString("home_meta", "Emergency Cover");
        int icon = getArguments().getInt("drawable", R.drawable.alarm);


        emergency_title.setText(title);
        emergency_desc.setText(desc);
        emergency_meta.setText(meta);
        product_icon.setImageResource(icon);
//        getDialog().setTitle(title);
        getDialog().setCancelable(false);


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onClick(View view) {
        this.dismiss();
    }
}


