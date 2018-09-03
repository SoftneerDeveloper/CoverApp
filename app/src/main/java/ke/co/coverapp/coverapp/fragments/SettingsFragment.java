package ke.co.coverapp.coverapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.MainActivity;
import ke.co.coverapp.coverapp.application.MyApplication;
import static ke.co.coverapp.coverapp.pojo.Keys.keys;
import ke.co.coverapp.coverapp.utility.ValidationUtil;


public class SettingsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    SwitchCompat skip_getting_started, skip_login_screen;
    boolean is_skip = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.SKIP_GETTING_STARTED, false);
    boolean is_login = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.SKIP_LOGIN, false);


    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        skip_getting_started = (SwitchCompat) view.findViewById(R.id.skip_getting_started);
        skip_login_screen = (SwitchCompat) view.findViewById(R.id.skip_login_screen);
        skip_getting_started.setChecked(is_skip);
        skip_login_screen.setChecked(is_login);
        skip_getting_started.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean is_enabled) {
                if (is_enabled) {
                    is_enabled = true;
                }
                MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.SKIP_GETTING_STARTED, is_enabled);
            }
        });
        skip_login_screen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean is_enabled) {
                if (is_enabled) {
                    is_enabled = true;
                }
                MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.SKIP_LOGIN, is_enabled);
            }
        });
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
        }if (id == R.id.action_settings) {
//            ((MainActivity) getActivity()).createDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
