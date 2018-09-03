package ke.co.coverapp.coverapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.EditProfileActivity;
import ke.co.coverapp.coverapp.activities.MainActivity;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.utility.ValidationUtil;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.*;


public class ProfileFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    CircleImageView image_profile;
    TextView prof_kra, profileEmail, prof_occupation, prof_name, prof_phone, prof_id_number, prof_dob, prof_gender, prof_marital_status, prof_balance, prof_home_address;
    Button prof_edit;
    String phone = MyApplication.readFromPreferences(MyApplication.getAppContext(), PHONE_NUMBER, ValidationUtil.getDefault());
    String fname = MyApplication.readFromPreferences(MyApplication.getAppContext(), FNAME, ValidationUtil.getDefault());
    String lname = MyApplication.readFromPreferences(MyApplication.getAppContext(), LNAME, ValidationUtil.getDefault());
    String email = MyApplication.readFromPreferences(MyApplication.getAppContext(), EMAIL, ValidationUtil.getDefault());
    String id_number = MyApplication.readFromPreferences(MyApplication.getAppContext(), ID_NUMBER, ValidationUtil.getDefault());
    String dob = MyApplication.readFromPreferences(MyApplication.getAppContext(), DOB, ValidationUtil.getDefault());
    String gender = MyApplication.readFromPreferences(MyApplication.getAppContext(), GENDER, ValidationUtil.getDefault());
    String marital_status = MyApplication.readFromPreferences(MyApplication.getAppContext(), MARITAL_STATUS, ValidationUtil.getDefault());
    String occupation = MyApplication.readFromPreferences(MyApplication.getAppContext(), OCCUPATION, ValidationUtil.getDefault());
    String kra = MyApplication.readFromPreferences(MyApplication.getAppContext(), KRA, ValidationUtil.getDefault());
    String currency = MyApplication.readFromPreferences(MyApplication.getAppContext(), CURR, ValidationUtil.getDefault());
    String w_balance = MyApplication.readFromPreferences(MyApplication.getAppContext(), WALLET_BALANCE, ValidationUtil.getDefaultBalance());
    String prof_pic_url = MyApplication.readFromPreferences(MyApplication.getAppContext(), PROF_PIC_URL, ValidationUtil.getDefaultString());
    String home_address = MyApplication.readFromPreferences(MyApplication.getAppContext(), HOME_ADDRESS, ValidationUtil.getDefaultString());

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileEmail = (TextView) view.findViewById(R.id.profileEmail);
        prof_name = (TextView) view.findViewById(R.id.prof_name);
        prof_phone = (TextView) view.findViewById(R.id.prof_phone);
        prof_id_number = (TextView) view.findViewById(R.id.prof_id_number);
        prof_balance = (TextView) view.findViewById(R.id.prof_balance);
        prof_dob = (TextView) view.findViewById(R.id.prof_dob);
        prof_gender = (TextView) view.findViewById(R.id.prof_gender);
        prof_kra = (TextView) view.findViewById(R.id.prof_kra);
        prof_marital_status = (TextView) view.findViewById(R.id.prof_marital_status);
        prof_occupation = (TextView) view.findViewById(R.id.prof_occupation);
        prof_edit = (Button) view.findViewById(R.id.prof_edit);
        prof_edit.setOnClickListener(this);
        prof_home_address = (TextView) view.findViewById(R.id.prof_home_address);
        image_profile = (CircleImageView) view.findViewById(R.id.image_profile);

if (prof_pic_url !=null)
        Picasso.with(MyApplication.getAppContext())
                .load(prof_pic_url)
                .placeholder(R.drawable.side_icon)
                .error(R.drawable.side_icon)
                .into(image_profile);

        profileEmail.setText(email);
        prof_phone.setText("0"+ValidationUtil.validPhoneNumber(phone));
        prof_name.setText(fname + " " + lname);
        prof_balance.setText(currency+" "+w_balance);
        prof_id_number.setText(id_number);
        prof_dob.setText(dob);
        prof_gender.setText(gender.substring(0,1).toUpperCase() + gender.substring(1));
        prof_marital_status.setText(marital_status.substring(0,1).toUpperCase() + marital_status.substring(1));
        prof_occupation.setText(occupation);
        prof_kra.setText(kra);
        prof_home_address.setText(home_address);
        return  view;
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

    /**
     * Called when a view has been clicked.
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        //startActivity(new Intent(MyApplication.getAppContext(), EditProfileActivity.class));
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();

        String phone = MyApplication.readFromPreferences(MyApplication.getAppContext(), PHONE_NUMBER, ValidationUtil.getDefault());
        String fname = MyApplication.readFromPreferences(MyApplication.getAppContext(), FNAME, ValidationUtil.getDefault());
        String lname = MyApplication.readFromPreferences(MyApplication.getAppContext(), LNAME, ValidationUtil.getDefault());
        String email = MyApplication.readFromPreferences(MyApplication.getAppContext(), EMAIL, ValidationUtil.getDefault());
        String id_number = MyApplication.readFromPreferences(MyApplication.getAppContext(), ID_NUMBER, ValidationUtil.getDefault());
        String dob = MyApplication.readFromPreferences(MyApplication.getAppContext(), DOB, ValidationUtil.getDefault());
        String gender = MyApplication.readFromPreferences(MyApplication.getAppContext(), GENDER, ValidationUtil.getDefault());
        String marital_status = MyApplication.readFromPreferences(MyApplication.getAppContext(), MARITAL_STATUS, ValidationUtil.getDefault());
        String occupation = MyApplication.readFromPreferences(MyApplication.getAppContext(), OCCUPATION, ValidationUtil.getDefault());
        String kra = MyApplication.readFromPreferences(MyApplication.getAppContext(), KRA, ValidationUtil.getDefault());
        String currency = MyApplication.readFromPreferences(MyApplication.getAppContext(), CURR, ValidationUtil.getDefault());
        String w_balance = MyApplication.readFromPreferences(MyApplication.getAppContext(), WALLET_BALANCE, ValidationUtil.getDefaultBalance());
        String prof_pic_url = MyApplication.readFromPreferences(MyApplication.getAppContext(), PROF_PIC_URL, ValidationUtil.getDefaultString());
        String home_address = MyApplication.readFromPreferences(MyApplication.getAppContext(), HOME_ADDRESS, ValidationUtil.getDefaultString());


        Picasso.with(MyApplication.getAppContext())
                .load(prof_pic_url)
                .placeholder(R.drawable.side_icon)
                .error(R.drawable.side_icon)
                .into(image_profile);

        Picasso.with(MyApplication.getAppContext())
                .load(prof_pic_url)
                .placeholder(R.drawable.side_icon)
                .error(R.drawable.side_icon)
                .into(MainActivity.nav_prof_pic);

        profileEmail.setText(email);
        prof_phone.setText("0"+ValidationUtil.validPhoneNumber(phone));
        prof_name.setText(fname + " " + lname);
        prof_balance.setText(currency+" "+w_balance);
        prof_id_number.setText(id_number);
        prof_dob.setText(dob);
        prof_gender.setText(gender.substring(0,1).toUpperCase() + gender.substring(1));
        prof_marital_status.setText(marital_status.substring(0,1).toUpperCase() + marital_status.substring(1));
        prof_occupation.setText(occupation);
        prof_kra.setText(kra);
        prof_home_address.setText(home_address);
    }
}
