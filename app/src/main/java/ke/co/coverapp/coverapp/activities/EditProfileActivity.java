package ke.co.coverapp.coverapp.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.fragments.AddAssetHomeCover;
import ke.co.coverapp.coverapp.fragments.ProfileFragment;
import ke.co.coverapp.coverapp.fragments.TopUpFragment;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleyMultipartRequest;
import ke.co.coverapp.coverapp.network.VolleySingleton;

import static ke.co.coverapp.coverapp.pojo.Keys.keys;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.*;

import ke.co.coverapp.coverapp.utility.AppHelperUtil;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText kra, fname, lname, sname, phone, email, id_no, occupation, prof_edit_pass, prof_edit_pass_again;
    EditText dob, home_address;
    AppCompatButton update_profile, togglePasswordView, prof_pic_gallery, prof_pic_camera;
    LinearLayout changePassView, linearLayout_step_one, linearLayout_step_two, linearLayout_step_three, linearLayout_step_four;
    CardView cardView_step_one, cardView_step_two, cardView_step_three, cardView_step_four, cardView_step_five;
    Button backToStepFour, buttonCancelProfileUpdate, buttonNextToStepTwo, buttonBackToStepOne, buttonNextToStepThree, buttonBackToStepTwo, buttonNextToStepFour, buttonBackToStepThree, buttonNexttoStepFive;
    private boolean isPasswordReset = false;
    private boolean isPictureChanged = false;
    CircleImageView profile_image;

    //Uri to store the image uri

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    private Uri filePath1;
    private RadioGroup prof_edit_gender_group, prof_edit_marriage_group;
    String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.ACCESS_TOKEN, ValidationUtil.getDefault());

    private RadioButton radio_male, radio_female, radio_married, radio_single;
    String text_phone = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.PHONE_NUMBER, ValidationUtil.getDefault());
    String text_fname = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.FNAME, ValidationUtil.getDefault());
    String text_lname = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.LNAME, ValidationUtil.getDefault());
    String text_sname = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.SNAME, ValidationUtil.getDefault());
    String text_email = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.EMAIL, ValidationUtil.getDefault());
    String text_id_number = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.ID_NUMBER, ValidationUtil.getDefault());
    String text_dob = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.DOB, ValidationUtil.getDefault());
    String text_marriage = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.MARITAL_STATUS, ValidationUtil.getDefault());
    String text_gender = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.GENDER, keys.MALE);
    String text_occupation = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.OCCUPATION, ValidationUtil.getDefault());
    String text_kra = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.KRA, ValidationUtil.getDefault());
    String prof_pic_url = MyApplication.readFromPreferences(MyApplication.getAppContext(), PROF_PIC_URL, ValidationUtil.getDefaultString());
    String text_home_address = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.HOME_ADDRESS, ValidationUtil.getDefault());

    String new_gender, text_marital_status, yearOfBirth;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.progress_update_profile));

        changePassView = (LinearLayout) findViewById(R.id.changePassView);
        linearLayout_step_one = (LinearLayout) findViewById(R.id.linearLayout_step_one);
        linearLayout_step_two= (LinearLayout) findViewById(R.id.linearLayout_step_two);
        linearLayout_step_three= (LinearLayout) findViewById(R.id.linearLayout_step_three);
        linearLayout_step_four= (LinearLayout) findViewById(R.id.linearLayout_step_four);

        cardView_step_one = (CardView ) findViewById(R.id.cardView_step_one);
        cardView_step_two= (CardView ) findViewById(R.id.cardView_step_two);
        cardView_step_three= (CardView ) findViewById(R.id.cardView_step_three);
        cardView_step_four= (CardView ) findViewById(R.id.cardView_step_four);
        cardView_step_five= (CardView ) findViewById(R.id.cardView_step_five);

        prof_edit_gender_group = (RadioGroup) findViewById(R.id.prof_edit_gender_group);
        prof_edit_marriage_group = (RadioGroup) findViewById(R.id.prof_edit_marriage_group);
        radio_male = (RadioButton) findViewById(R.id.radio_male);
        radio_female = (RadioButton) findViewById(R.id.radio_female);
        radio_married = (RadioButton) findViewById(R.id.radio_married);
        radio_single = (RadioButton) findViewById(R.id.radio_single);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);

        buttonCancelProfileUpdate = (Button ) findViewById(R.id.buttonCancelProfileUpdate);
        buttonNextToStepTwo= (Button ) findViewById(R.id.buttonNextToStepTwo);
        buttonBackToStepOne= (Button ) findViewById(R.id.buttonBackToStepOne);
        buttonNextToStepThree= (Button ) findViewById(R.id.buttonNextToStepThree);
        buttonBackToStepTwo= (Button ) findViewById(R.id.buttonBackToStepTwo);
        buttonNextToStepFour= (Button ) findViewById(R.id.buttonNextToStepFour);
        buttonBackToStepThree= (Button ) findViewById(R.id.buttonBackToStepThree);
        buttonNexttoStepFive= (Button ) findViewById(R.id.buttonNexttoStepFive);
        backToStepFour= (Button ) findViewById(R.id.backToStepFour);

        buttonCancelProfileUpdate.setOnClickListener(this);
        buttonNextToStepTwo.setOnClickListener(this);
        buttonBackToStepOne.setOnClickListener(this);
        buttonNextToStepThree.setOnClickListener(this);
        buttonBackToStepTwo.setOnClickListener(this);
        buttonNextToStepFour.setOnClickListener(this);
        buttonBackToStepThree.setOnClickListener(this);
        buttonNexttoStepFive.setOnClickListener(this);
        backToStepFour.setOnClickListener(this);

        Picasso.with(MyApplication.getAppContext())
                .load(prof_pic_url)
                .placeholder(R.drawable.side_icon)
                .error(R.drawable.side_icon)
                .into(profile_image);

        fname = (TextInputEditText) findViewById(R.id.fname);
        lname = (TextInputEditText) findViewById(R.id.lname);
        sname = (TextInputEditText) findViewById(R.id.sname);
        phone = (TextInputEditText) findViewById(R.id.phone);
        email = (TextInputEditText) findViewById(R.id.email);
        id_no = (TextInputEditText) findViewById(R.id.id_no);
        occupation = (TextInputEditText) findViewById(R.id.occupation);
        kra = (TextInputEditText) findViewById(R.id.kra);

        prof_edit_pass = (TextInputEditText) findViewById(R.id.prof_edit_pass);
        prof_edit_pass_again = (TextInputEditText) findViewById(R.id.prof_edit_pass_again);
        dob = (EditText) findViewById(R.id.dob_text);
        home_address = (EditText) findViewById(R.id.home_address);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        // Show fragment on dob onclick
        dob.setOnClickListener(this);

        if (keys.MALE.toLowerCase().equals(text_gender.toLowerCase())) {

            radio_male.setChecked(true);

        } else if (keys.FEMALE.toLowerCase().equals(text_gender.toLowerCase())) {

            radio_female.setChecked(true);

        }

        if (keys.SINGLE.toLowerCase().equals(text_marriage.toLowerCase())) {

            radio_single.setChecked(true);

        } else if (keys.MARRIED.toLowerCase().equals(text_marriage.toLowerCase())) {

            radio_married.setChecked(true);

        }

        fname.setText(text_fname);
        lname.setText(text_lname);
        sname.setText(text_sname);
        phone.setText("0" + ValidationUtil.validPhoneNumber(text_phone));
        email.setText(text_email);
        id_no.setText(text_id_number);
        occupation.setText(text_occupation);
        kra.setText(text_kra);
        dob.setText(text_dob);
        home_address.setText(text_home_address);

        // Set defaults for gender and marital status
        new_gender = text_gender;
        text_marital_status = text_marriage;

        if(kra.getText().toString().trim().equals("Not Available"))
        {
            kra.setText("");
            kra.setHint(text_kra);
        }

        if (occupation.getText().toString().trim().equals("Not Available"))
        {
            occupation.setText("");
            occupation.setHint(text_occupation);
        }

        if (home_address.getText().toString().trim().equals("Not Available"))
        {
            home_address.setText("");
            home_address.setHint(text_home_address);
        }
//        Toast.makeText(this, ""+profile_image.getDrawable(), Toast.LENGTH_SHORT).show();


        update_profile = (AppCompatButton) findViewById(R.id.update_profile);
        if (update_profile != null) {
            update_profile.setOnClickListener(this);
        }
        togglePasswordView = (AppCompatButton) findViewById(R.id.togglePasswordView);
        if (togglePasswordView != null) {
            togglePasswordView.setOnClickListener(this);
        }

        prof_pic_gallery = (AppCompatButton) findViewById(R.id.prof_pic_gallery);
        if (prof_pic_gallery != null) {
            prof_pic_gallery.setOnClickListener(this);
        }

        prof_pic_camera = (AppCompatButton) findViewById(R.id.prof_pic_camera);
        if (prof_pic_camera != null) {
            prof_pic_camera.setOnClickListener(this);
        }

        //get place
//        PlaceAutocompleteFragment places= (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//
//                Toast.makeText(getApplicationContext(),place.getName(),Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(Status status) {
//
//                Toast.makeText(getApplicationContext(),status.toString(),Toast.LENGTH_SHORT).show();
//
//            }
//        });


    }

    private boolean validateEmailField() {
        if (!ValidationUtil.hasValidContents(email) || !ValidationUtil.hasValidEmail(email)) {
            email.setError(getString(R.string.invalid_email));
            requestFocus(email);

            return false;
        }
        return true;
    }

    private boolean validatePhoneField() {
        if (!ValidationUtil.isValidPhoneNumber(phone)) {
            phone.setError(getString(R.string.invalid_phone_number));
            requestFocus(phone);
            return false;
        }
        return true;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_male:
                if (checked)
                    new_gender = keys.MALE;
                break;
            case R.id.radio_female:
                if (checked)
                    new_gender = keys.FEMALE;
                break;
        }
    }

    public void onMarriageRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_married:
                if (checked)
                    text_marital_status = keys.MARRIED;
                break;
            case R.id.radio_single:
                if (checked)
                    text_marital_status = keys.SINGLE;
                break;
        }
    }

    private boolean validateField(TextInputEditText the_input, int required_length, String message) {
        if (!ValidationUtil.hasValidContents(the_input) || the_input.getText().toString().trim().length() < required_length
                ) {

            the_input.setError(message);
            requestFocus(the_input);

            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String yearFormat = "yyyy";
        SimpleDateFormat sdfYear = new SimpleDateFormat(yearFormat, Locale.US);
        yearOfBirth = sdfYear.format(myCalendar.getTime());
        dob.setText(sdf.format(myCalendar.getTime()));
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.dob_text:
                // Show datePickerFragment
                new DatePickerDialog(EditProfileActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT,  date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


                break;

            case R.id.togglePasswordView:
                togglePasswordView.setVisibility(View.GONE);
                changePassView.setVisibility(View.VISIBLE);
                isPasswordReset = true;
                break;

            case R.id.update_profile:
//                if (profile_image.getDrawable().equals(null))
//                {
//
//                }not working
                if (validateField(id_no, 6, getString(R.string.invalid_id_no))
                        && validateField(sname, 3, getString(R.string.invalid_name))
                        && validateEmailField()
                        && validateField(fname, 3, getString(R.string.invalid_name))
                        && validateField(lname, 3, getString(R.string.invalid_name))
                        && validatePhoneField()
                        && validateField(occupation, 3, getString(R.string.invalid_occupation))
                        && validateField(kra, 3, getString(R.string.invalid_kra))) {

                    if (isPasswordReset && !ValidationUtil.textMatch(prof_edit_pass, prof_edit_pass_again)) {
                        prof_edit_pass_again.setError(getString(R.string.invalid_pass_rest));
                        return;
                    }

                    // Validate KRA PIN
                    if(kra.getText().length() != 11)
                    {
                        kra.setError("PIN has to be 11 digits long.");
                    }
                    else
                    {
                        //The first and last digits have to be letters
                        Integer pin_length = kra.getText().length();
                        if (!Character.isLetter(kra.getText().charAt(0)) || !Character.isLetter(kra.getText().charAt(pin_length - 1))) {
                            kra.setError("First and last characters have to be letters.");
                        }
                    }

                    // TODO: Validate date.
                    // TODO: Come up with a better way to do this. Use a function to do the validation like above.
                    // If the difference between the current date and the DOB selected is less than 18 years, generate error
                    // Get diff btw YOB and current year
                    if (yearOfBirth != null) {
                        int YOB = Integer.parseInt(yearOfBirth);

                        int yearDiff = Calendar.getInstance().get(Calendar.YEAR) - YOB;

                        if(yearDiff <= 18) {
                            L.m("Current year: " + Calendar.getInstance().get(Calendar.YEAR) + "; YOB: " + YOB + "; Diff: " + yearDiff);
                            dob.setError("You must be older than 18.");
                        } else {
                            if (dob.getError() != null) {
                                // Remove error
                                dob.setError(null);
                            }
                        }
                    }

                    // Check kra field error
                    if (kra.getError() != null) {
                        L.T(EditProfileActivity.this, "Please enter a valid KRA PIN.");
                    } else {

                        if(dob.getError() != null) {
                            L.t(EditProfileActivity.this, "You must be over 18.");
                        } else {
                            networkUpdate();
                        }
                    }

                } else {
                    L.m("Validation not passed therefore network update not called.");
                }
                break;

            case R.id.prof_pic_camera:
                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_ONE);
                break;

            case R.id.prof_pic_gallery:
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
                startActivityForResult(Intent.createChooser(getIntent, getString(R.string.select_image)).putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent}), GALLERY_REQUEST_ONE);

                break;

            case R.id.buttonCancelProfileUpdate:

                finish();
                break;

            case R.id.buttonNextToStepTwo:
                //hide
                cardView_step_one.setVisibility(View.GONE);
                linearLayout_step_one.setVisibility(View.GONE);


                //show
                cardView_step_two.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_right));
                cardView_step_two.setVisibility(View.VISIBLE);
                linearLayout_step_two.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_right));
                linearLayout_step_two.setVisibility(View.VISIBLE);

                break;

            case R.id.buttonBackToStepOne:

                //hide
                cardView_step_two.setVisibility(View.GONE);
                linearLayout_step_two.setVisibility(View.GONE);


                //show
                cardView_step_one.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_left));
                cardView_step_one.setVisibility(View.VISIBLE);
                linearLayout_step_one.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_left));
                linearLayout_step_one.setVisibility(View.VISIBLE);

                break;

            case R.id.buttonNextToStepThree:

                //hide
                cardView_step_two.setVisibility(View.GONE);
                linearLayout_step_two.setVisibility(View.GONE);


                //show
                cardView_step_three.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_right));
                cardView_step_three.setVisibility(View.VISIBLE);
                linearLayout_step_three.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_right));
                linearLayout_step_three.setVisibility(View.VISIBLE);

                break;

            case R.id.buttonBackToStepTwo:

                //hide
                cardView_step_three.setVisibility(View.GONE);
                linearLayout_step_three.setVisibility(View.GONE);


                //show
                cardView_step_two.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_left));
                cardView_step_two.setVisibility(View.VISIBLE);
                linearLayout_step_two.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_left));
                linearLayout_step_two.setVisibility(View.VISIBLE);

                break;

            case R.id.buttonNextToStepFour:

                //hide
                cardView_step_three.setVisibility(View.GONE);
                linearLayout_step_three.setVisibility(View.GONE);


                //show
                cardView_step_four.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_right));
                cardView_step_four.setVisibility(View.VISIBLE);
                linearLayout_step_four.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_right));
                linearLayout_step_four.setVisibility(View.VISIBLE);
                break;

            case R.id.buttonBackToStepThree:
                //hide
                cardView_step_four.setVisibility(View.GONE);
                linearLayout_step_four.setVisibility(View.GONE);


                //show
                cardView_step_three.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_left));
                cardView_step_three.setVisibility(View.VISIBLE);
                linearLayout_step_three.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_left));
                linearLayout_step_three.setVisibility(View.VISIBLE);
                break;

            case R.id.buttonNexttoStepFive:

                //hide
                cardView_step_four.setVisibility(View.GONE);
                linearLayout_step_four.setVisibility(View.GONE);

                //show
                cardView_step_five.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_right));
                cardView_step_five.setVisibility(View.VISIBLE);
               togglePasswordView.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_right));
                togglePasswordView.setVisibility(View.VISIBLE);
                update_profile.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_right));
                update_profile.setVisibility(View.VISIBLE);
                backToStepFour.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_right));
                backToStepFour.setVisibility(View.VISIBLE);

                break;

            case R.id.backToStepFour:

                //hide
                cardView_step_five.setVisibility(View.GONE);
                //togglePasswordView.setVisibility(View.GONE);
               // update_profile.setVisibility(View.GONE);
                backToStepFour.setVisibility(View.GONE);

                //show
                cardView_step_four.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_left));
                cardView_step_four.setVisibility(View.VISIBLE);
                linearLayout_step_four.startAnimation(AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.enter_from_left));
                linearLayout_step_four.setVisibility(View.VISIBLE);

                break;

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK && data != null) { //  && data.getData() != null

            if (requestCode == GALLERY_REQUEST_ONE) {
                L.m("Gallery");
                try {
                    //from the gallery
                    InputStream inputStream = null;
                    inputStream = MyApplication.getAppContext().getContentResolver().openInputStream(data.getData());
                    if (inputStream != null) {
                        profile_image.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                        filePath1 = data.getData();
                        isPictureChanged = true;
                        //  Toast.makeText(this, ""+filePath1.toString().trim(), Toast.LENGTH_SHORT).show();
                        //  MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.PROF_PIC_URL, filePath1.toString().trim());
                    }
                } catch (Exception e) {
                    L.t(MyApplication.getAppContext(), "Something went wrong attaching your photo");
                    e.printStackTrace();

                }

            } else if (requestCode == CAMERA_REQUEST_ONE){

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                try {
                    //               photo = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    profile_image.setImageBitmap(photo);
                    filePath1 = data.getData();
                    isPictureChanged = true;

                    L.m("Hello" + data.getData());

                        //    MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.PROF_PIC_URL, filePath1.toString().trim());


                } catch (Exception e) {
                    L.t(MyApplication.getAppContext(), "Something went wrong attaching your photo");
                    e.printStackTrace();
                }
            }
            else{
                L.m("Else image");
                L.t(MyApplication.getAppContext(), "Something went wrong attaching your photo");
            }
        }
    }

    private void networkUpdate() {

        L.m("Text marriage: " + text_marriage + "; Text marital status: " + text_marital_status);
        L.m("Text gender: " + text_gender + "; New gender: " + text_marital_status);

        mProgressDialog.show();
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, getString(R.string.baseUrl) + "/profile", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse profile) {
                L.m("Status code: " + profile.statusCode);
                L.m("Data: " + profile.data.toString());

                MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.PHONE_NUMBER, "254" + ValidationUtil.validPhoneNumber(phone));
                MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.FNAME, fname.getText().toString().trim());
                MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.LNAME, lname.getText().toString().trim());
                MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.SNAME, sname.getText().toString().trim());
                MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.EMAIL, email.getText().toString().trim());
                MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.ID_NUMBER, id_no.getText().toString().trim());
                MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.GENDER, new_gender);
                MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.MARITAL_STATUS, text_marital_status);
                MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.OCCUPATION, occupation.getText().toString().trim());
                MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.KRA, kra.getText().toString().trim());
                MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.DOB, dob.getText().toString().trim());
                MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.HOME_ADDRESS, home_address.getText().toString().trim());

                try
                {
                    //statements that may cause an exception
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.PROF_PIC_URL, filePath1.toString().trim());
                }
                catch (Exception e)
                {
                    //error handling code
                    L.t(MyApplication.getAppContext(), "Profile update successful");
                }



                L.t(MyApplication.getAppContext(), "Profile update successful");
//                new MainActivity().displayView(R.id.nav_profile);
                mProgressDialog.hide();

                finish();

//                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
//                startActivity(intent);

//                finish();
//                overridePendingTransition(0, 0);
//                startActivity(getIntent());
//                overridePendingTransition(0, 0);


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.hide();

                NetworkResponse response = error.networkResponse;

                L.m("Error encountered updating profile");

                if (response != null && response.data != null) {
                    L.m("The error encountered is shown below: ");

                    L.m(new String(response.data));
                    switch (response.statusCode) {
                        case 400:
                            //parsing the error

                            JSONObject obj;
                            JSONArray message;
                            JSONObject jsonRowObj;
                            String json = new String(response.data);//string
                            L.m(new String(response.data));
                            try {
                                obj = new JSONObject(json);
                                if (ParseUtil.contains(obj, "text")) {

                                        L.t(MyApplication.getAppContext(), obj.getString("text"));
                                    }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            VolleyCustomErrorHandler.errorMessage(error);
                            break;
                    }
                } else {
                    L.m("Error could not be shown!");
                    VolleyCustomErrorHandler.errorMessage(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                if (!ValidationUtil.validPhoneNumber(phone.getText().toString().trim()).matches(ValidationUtil.validPhoneNumber(text_phone)))
                    parameters.put("phone", "254" + ValidationUtil.validPhoneNumber(phone.getText().toString().trim()));

                if (!text_fname.matches(fname.getText().toString().trim()))
                    parameters.put("fname", fname.getText().toString().trim());

                if (!text_lname.matches(lname.getText().toString().trim()))
                    parameters.put("lname", lname.getText().toString().trim());

                if (!text_sname.matches(sname.getText().toString().trim()))
                    parameters.put("sname", sname.getText().toString().trim());

                if (!text_email.matches(email.getText().toString().trim()))
                    parameters.put("email", email.getText().toString().trim());

                if (!text_id_number.matches(id_no.getText().toString().trim()))
                    parameters.put("id_no", text_id_number);

                if (!text_occupation.matches(occupation.getText().toString().trim()))
                    parameters.put("occupation", occupation.getText().toString().trim());

                if (!text_kra.matches(kra.getText().toString().trim()))
                    parameters.put("kra", kra.getText().toString().trim());

                if (!text_gender.matches(new_gender))
                parameters.put("gender", new_gender);

                if (!text_marriage.matches(text_marital_status))
                parameters.put("marital_status", text_marital_status);

                if (isPasswordReset) {
                    parameters.put("password", prof_edit_pass.getText().toString().trim());
                }

                if (!text_dob.matches(dob.getText().toString().trim()))
                    parameters.put("dob", dob.getText().toString().trim());

                if (!text_home_address.matches(home_address.getText().toString().trim()))
                    parameters.put("home_address", home_address.getText().toString().trim());

                L.m("Hello parameters shown below: ");
                L.m(parameters.toString());
                return parameters;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could find file base or direct access from real path
                // for now just get bitmap data from ImageView
                if (isPictureChanged) {
                    params.put("profile_image", new DataPart("image_one", AppHelperUtil.getFileDataFromDrawable(getBaseContext(), profile_image.getDrawable()), "image/jpeg"));
                }

                L.m(params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                keys.MY_SOCKET_TIMEOUT_MS,
                keys.MY_DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(request);
    }

}
