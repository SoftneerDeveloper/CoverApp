package ke.co.coverapp.coverapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.tasks.TaskLoadBalance;

/**
 * Created by Clifford Owino on 3/31/2017.
 */

public class TopUpRunTimeFragment extends DialogFragment implements View.OnClickListener{

//    TextView emergency_title, emergency_desc,  emergency_meta;
  //  Button button_cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_up_runtime, container);
    }

    public TopUpRunTimeFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead
    }

    public static TopUpRunTimeFragment newInstance( ) {
        TopUpRunTimeFragment frag = new TopUpRunTimeFragment();
//        Bundle args = new Bundle();
//        args.putString("title", title);
//        args.putString("desc", desc);
//        args.putString("home_meta", home_meta);
//        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
//        emergency_title = (TextView) view.findViewById(R.id.emergency_title);
//        emergency_desc = (TextView) view.findViewById(R.id.emergency_desc);
//        emergency_meta = (TextView) view.findViewById(R.id.emergency_meta);
 //     button_cancel = (Button) view.findViewById(R.id.button_cancel);
  //      button_cancel.setOnClickListener(this);
//        // Fetch arguments from bundle and set title
//        String title = getArguments().getString("title", "Emergency Cover");
//        String desc = getArguments().getString("desc", "Emergency Cover");
//        String meta = getArguments().getString("home_meta", "Emergency Cover");
//
//        emergency_title.setText(title);
//        emergency_desc.setText(desc);
//        emergency_meta.setText(meta);
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