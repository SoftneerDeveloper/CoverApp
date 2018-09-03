package ke.co.coverapp.coverapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.backgroundWorkers.BackgroundWorker2;
import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.tasks.TaskLoadBalance;
import ke.co.coverapp.coverapp.utility.ValidationUtil;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.CAMERA_REQUEST_ONE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.GALLERY_REQUEST_ONE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.VALUE;

public class LegacyLifePlan extends AppCompatActivity implements View.OnClickListener{

    CardView cardView_beneficiary, spouse_and_kids_info2, cardview_one, cardview_two, cardview_three, cardview_four, cardview_five, cardview_six, cardview_seven, cardview_final, add_parents_cardView, spouse_and_kids_info, cardView_kids1, cardView_kids2, cardView_kids3, cardView_kids4, cardView_kids5, cardView_kids6, cardView_parent1, cardView_parent2, cardView_parent3, cardView_parent4;
    Button button8, nextToLayout_parentRecycler, nextTocardview_final, button4, buttonSelectPlan, button3, button6, button5, button7, button9, button11, button_upload_photo1, button_upload_photo2, button_upload_photo3, button_upload_photo4, button_upload_photo5, button_upload_photo6, button_open_camera1, button_open_camera2,button_open_camera3,button_open_camera4,button_open_camera5, button_open_camera6, button12;
    Spinner spinner2, spinner_relationship1, spinner_relationship2, spinner_relationship3, spinner_relationship4, spinner3;
    String packageName, paymentDuration, packageLevel, parents_number, kids_number, yearOfBirth, parentCoverAdded, spouseAdded, kidsAdded;
    int parents_number2, kids_number2;
    String price;
    String  priceParent;
    String parentPackageLevel;
    Integer price2;
    Integer parentTotal;
    Integer legacyPremium;
    Integer priceParent2;
    String childs_name1, childs_name2, childs_name3, childs_name4, childs_name5, childs_name6;
    String kids_dob1, kids_dob2, kids_dob3, kids_dob4, kids_dob5, kids_dob6;
    Spinner spinner_kids_gender1, spinner_kids_gender2, spinner_kids_gender3, spinner_kids_gender4, spinner_kids_gender5, spinner_kids_gender6;
    String kids_gender1, kids_gender2, kids_gender3, kids_gender4, kids_gender5, kids_gender6;
    TextView textView75, textView66, textView67, textView68, textView62, textView72, textView74, textView123, textView73,textView107, textView108, textView109, textView110,textView80, TextViewDOB, textView81, textView82;
    TextView textView78, textView83, textView84;
    LinearLayout layout_kidsHorizontalScroll, add_parent_layout, Layout_step_onelabel, layout_parentsNum, layout_parentRecycler, layout_horizontal_scroll1;
    EditText  spouse_dob, kids_date_of_birth1, kids_date_of_birth2, kids_date_of_birth3, kids_date_of_birth4, kids_date_of_birth5, kids_date_of_birth6, pd_date_of_birth1, pd_date_of_birth2, pd_date_of_birth3, pd_date_of_birth4;
    TableLayout benefits_tableLayout;
    TextInputEditText editText_parents_num, kids_number_TextInputEditText, TextInputEditText_name1, TextInputEditText_name2, TextInputEditText_name3, TextInputEditText_name4, TextInputEditText_name5, TextInputEditText_name6, pd_name_editText1, pd_name_editText2, pd_name_editText3, pd_name_editText4;
    TextInputLayout layout_editText_parents_num, TextInputLayout_Name, TextInputLayout_IdNumber, TextInputLayout_PhoneNumber, kids_number_TextInputLayout, TextInputLayout_name1, TextInputLayout_name2, TextInputLayout_name3, TextInputLayout_name4, TextInputLayout_name5, TextInputLayout_name6, pd_name_layout1, pd_name_layout2, pd_name_layout3, pd_name_layout4;
    TextInputLayout pd_id_layout1, pd_id_layout2, pd_id_layout3, pd_id_layout4, pd_phone_layout1, pd_phone_layout2, pd_phone_layout3, pd_phone_layout4;
    TextInputEditText TextInputEditText_Name, TextInputEditText_IdNumber, TextInputEditText_PhoneNumber;
    TextInputEditText pd_id_editText1, pd_id_editText2, pd_id_editText3, pd_id_editText4, pd_phone_EditText1, pd_phone_EditText2, pd_phone_EditText3, pd_phone_EditText4;
    Calendar myCalendarLegacy = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener legacyDate;
    Switch switchSpouse, switchKids, switch_ParentsCover;
    ImageView imageView_birth_cert1, imageView_birth_cert2, imageView_birth_cert3, imageView_birth_cert4, imageView_birth_cert5, imageView_birth_cert6;
    String dob_editText, cover_amount, imageView_birth_cert1_string, imageView_birth_cert2_string, imageView_birth_cert3_string, imageView_birth_cert4_string, imageView_birth_cert5_string, imageView_birth_cert6_string, buttonUpload, buttonCamera;
    String parentRelationShip1, parentRelationShip2, parentRelationShip3, parentRelationShip4;
    String parentName1, parentName2, parentName3, parentName4;
    String parent_DOB1, parent_DOB2, parent_DOB3, parent_DOB4;
    String parentIdNumber1, parentIdNumber2, parentIdNumber3, parentIdNumber4;
    String parentPhoneNumber1, parentPhoneNumber2, parentPhoneNumber3, parentPhoneNumber4;
    public Bitmap bitmap;


    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legacy_life_plan);
        setTitle(getResources().getText(R.string.legacyplan));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        parents_number2=0;
        kids_number2=0;
        parentCoverAdded="no";
        paymentDuration="";
        spouseAdded="no";
        kidsAdded="no";

        benefits_tableLayout=(TableLayout )findViewById(R.id.benefits_tableLayout);

        switchSpouse=(Switch ) findViewById(R.id.switchSpouse);
        switchSpouse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    TextInputLayout_Name.setVisibility(View.VISIBLE);
                    TextInputLayout_IdNumber.setVisibility(View.VISIBLE);
                    TextInputLayout_PhoneNumber.setVisibility(View.VISIBLE);
                    TextViewDOB.setVisibility(View.VISIBLE);
                    spouse_dob.setVisibility(View.VISIBLE);
                    spouseAdded="yes";
                }
                else {
                    TextInputLayout_Name.setVisibility(View.GONE);
                    TextInputLayout_IdNumber.setVisibility(View.GONE);
                    TextInputLayout_PhoneNumber.setVisibility(View.GONE);
                    TextViewDOB.setVisibility(View.GONE);
                    spouse_dob.setVisibility(View.GONE);
                    spouseAdded="no";
                }
            }
        });

        switchKids=(Switch ) findViewById(R.id.switchKids);
        switchKids.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    kids_number_TextInputLayout.setVisibility(View.VISIBLE);
                    button9.setVisibility(View.VISIBLE);
                    kidsAdded="yes" ;
                }
                else {
                    kids_number_TextInputLayout.setVisibility(View.GONE);
                    button9.setVisibility(View.VISIBLE);
                    kidsAdded="no" ;
                }
            }
        });

            switch_ParentsCover =(Switch)findViewById(R.id.switch_ParentsCover);

        switch_ParentsCover.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    textView62.setVisibility(View.VISIBLE);
                    editText_parents_num.setVisibility(View.VISIBLE);
                    textView82.setVisibility(View.VISIBLE);
                    spinner3.setVisibility(View.VISIBLE);
                    layout_editText_parents_num.setVisibility(View.VISIBLE);
                    parentCoverAdded="yes";
                }
                else if(!isChecked)
                {
                    textView62.setVisibility(View.GONE);
                    editText_parents_num.setVisibility(View.GONE);
                    textView82.setVisibility(View.GONE);
                    spinner3.setVisibility(View.GONE);
                    layout_editText_parents_num.setVisibility(View.GONE);
                    parentCoverAdded="no";
                }
            }
        });

        spinner_kids_gender1=(Spinner ) findViewById(R.id.spinner_kids_gender1);
        spinner_kids_gender2=(Spinner ) findViewById(R.id.spinner_kids_gender2);
        spinner_kids_gender3=(Spinner ) findViewById(R.id.spinner_kids_gender3);
        spinner_kids_gender4=(Spinner ) findViewById(R.id.spinner_kids_gender4);
        spinner_kids_gender5=(Spinner ) findViewById(R.id.spinner_kids_gender5);
        spinner_kids_gender6=(Spinner ) findViewById(R.id.spinner_kids_gender6);

        cardView_kids1=(CardView )findViewById(R.id.cardView_kids1);
        cardView_kids2=(CardView )findViewById(R.id.cardView_kids2);
        cardView_kids3=(CardView )findViewById(R.id.cardView_kids3);
        cardView_kids4=(CardView )findViewById(R.id.cardView_kids4);
        cardView_kids5=(CardView )findViewById(R.id.cardView_kids5);
        cardView_kids6=(CardView )findViewById(R.id.cardView_kids6);

        cardView_parent1=(CardView )findViewById(R.id.cardView_parent1);
        cardView_parent2=(CardView )findViewById(R.id.cardView_parent2);
        cardView_parent3=(CardView )findViewById(R.id.cardView_parent3);
        cardView_parent4=(CardView )findViewById(R.id.cardView_parent4);

        TextInputLayout_name1= (TextInputLayout)findViewById(R.id.TextInputLayout_name1);
        layout_editText_parents_num= (TextInputLayout)findViewById(R.id.layout_editText_parents_num);

        TextInputLayout_name2= (TextInputLayout)findViewById(R.id.TextInputLayout_name2);
        TextInputLayout_name3= (TextInputLayout)findViewById(R.id.TextInputLayout_name3);
        TextInputLayout_name4= (TextInputLayout)findViewById(R.id.TextInputLayout_name4);
        TextInputLayout_name5= (TextInputLayout)findViewById(R.id.TextInputLayout_name5);
        TextInputLayout_name6= (TextInputLayout)findViewById(R.id.TextInputLayout_name6);

        pd_name_layout1= (TextInputLayout)findViewById(R.id.pd_name_layout1);
        pd_name_layout2= (TextInputLayout)findViewById(R.id.pd_name_layout2);
        pd_name_layout3= (TextInputLayout)findViewById(R.id.pd_name_layout3);
        pd_name_layout4= (TextInputLayout)findViewById(R.id.pd_name_layout4);

        pd_id_layout1= (TextInputLayout)findViewById(R.id.pd_id_layout1);
        pd_id_layout2= (TextInputLayout)findViewById(R.id.pd_id_layout2);
        pd_id_layout3= (TextInputLayout)findViewById(R.id.pd_id_layout3);
        pd_id_layout4= (TextInputLayout)findViewById(R.id.pd_id_layout4);

        pd_phone_layout1= (TextInputLayout)findViewById(R.id.pd_phone_layout1);
        pd_phone_layout2= (TextInputLayout)findViewById(R.id.pd_phone_layout2);
        pd_phone_layout3= (TextInputLayout)findViewById(R.id.pd_phone_layout3);
        pd_phone_layout4= (TextInputLayout)findViewById(R.id.pd_phone_layout4);

        TextInputEditText_name1= (TextInputEditText)findViewById(R.id.TextInputEditText_name1);
        TextInputEditText_name2= (TextInputEditText)findViewById(R.id.TextInputEditText_name2);
        TextInputEditText_name3= (TextInputEditText)findViewById(R.id.TextInputEditText_name3);
        TextInputEditText_name4= (TextInputEditText)findViewById(R.id.TextInputEditText_name4);
        TextInputEditText_name5= (TextInputEditText)findViewById(R.id.TextInputEditText_name5);
        TextInputEditText_name6= (TextInputEditText)findViewById(R.id.TextInputEditText_name6);

        pd_name_editText1= (TextInputEditText)findViewById(R.id.pd_name_editText1);
        pd_name_editText2= (TextInputEditText)findViewById(R.id.pd_name_editText2);
        pd_name_editText3= (TextInputEditText)findViewById(R.id.pd_name_editText3);
        pd_name_editText4= (TextInputEditText)findViewById(R.id.pd_name_editText4);

        pd_id_editText1= (TextInputEditText)findViewById(R.id.pd_id_editText1);
        pd_id_editText2= (TextInputEditText)findViewById(R.id.pd_id_editText2);
        pd_id_editText3= (TextInputEditText)findViewById(R.id.pd_id_editText3);
        pd_id_editText4= (TextInputEditText)findViewById(R.id.pd_id_editText4);

        pd_phone_EditText1= (TextInputEditText)findViewById(R.id.pd_phone_EditText1);
        pd_phone_EditText2= (TextInputEditText)findViewById(R.id.pd_phone_EditText2);
        pd_phone_EditText3= (TextInputEditText)findViewById(R.id.pd_phone_EditText3);
        pd_phone_EditText4= (TextInputEditText)findViewById(R.id.pd_phone_EditText4);

        kids_date_of_birth1= (EditText)findViewById(R.id.kids_date_of_birth1);
        kids_date_of_birth2= (EditText)findViewById(R.id.kids_date_of_birth2);
        kids_date_of_birth3= (EditText)findViewById(R.id.kids_date_of_birth3);
        kids_date_of_birth4= (EditText)findViewById(R.id.kids_date_of_birth4);
        kids_date_of_birth5= (EditText)findViewById(R.id.kids_date_of_birth5);
        kids_date_of_birth6= (EditText)findViewById(R.id.kids_date_of_birth6);

        pd_date_of_birth1= (EditText)findViewById(R.id.pd_date_of_birth1);
        pd_date_of_birth2= (EditText)findViewById(R.id.pd_date_of_birth2);
        pd_date_of_birth3= (EditText)findViewById(R.id.pd_date_of_birth3);
        pd_date_of_birth4= (EditText)findViewById(R.id.pd_date_of_birth4);

        kids_date_of_birth1.setOnClickListener(this);
        kids_date_of_birth2.setOnClickListener(this);
        kids_date_of_birth3.setOnClickListener(this);
        kids_date_of_birth4.setOnClickListener(this);
        kids_date_of_birth5.setOnClickListener(this);
        kids_date_of_birth6.setOnClickListener(this);

        pd_date_of_birth1 .setOnClickListener(this);
        pd_date_of_birth2.setOnClickListener(this);
        pd_date_of_birth3.setOnClickListener(this);
        pd_date_of_birth4.setOnClickListener(this);

        button_upload_photo1= (Button)findViewById(R.id.button_upload_photo1);
        button_upload_photo2= (Button)findViewById(R.id.button_upload_photo2);
        button_upload_photo3= (Button)findViewById(R.id.button_upload_photo3);
        button_upload_photo4= (Button)findViewById(R.id.button_upload_photo4);
        button_upload_photo5= (Button)findViewById(R.id.button_upload_photo5);
        button_upload_photo6= (Button)findViewById(R.id.button_upload_photo6);

        button_open_camera1= (Button)findViewById(R.id.button_open_camera1);
        button_open_camera2= (Button)findViewById(R.id.button_open_camera2);
        button_open_camera3= (Button)findViewById(R.id.button_open_camera3);
        button_open_camera4= (Button)findViewById(R.id.button_open_camera4);
        button_open_camera5= (Button)findViewById(R.id.button_open_camera5);
        button_open_camera6= (Button)findViewById(R.id.button_open_camera6);

        button_upload_photo1.setOnClickListener(this);
        button_upload_photo2.setOnClickListener(this);
        button_upload_photo3.setOnClickListener(this);
        button_upload_photo4.setOnClickListener(this);
        button_upload_photo5.setOnClickListener(this);
        button_upload_photo6.setOnClickListener(this);

        button_open_camera1.setOnClickListener(this);
        button_open_camera2.setOnClickListener(this);
        button_open_camera3.setOnClickListener(this);
        button_open_camera4.setOnClickListener(this);
        button_open_camera5.setOnClickListener(this);
        button_open_camera6.setOnClickListener(this);

        imageView_birth_cert1= (ImageView)findViewById(R.id.imageView_birth_cert1);
        imageView_birth_cert2= (ImageView)findViewById(R.id.imageView_birth_cert2);
        imageView_birth_cert3= (ImageView)findViewById(R.id.imageView_birth_cert3);
        imageView_birth_cert4= (ImageView)findViewById(R.id.imageView_birth_cert4);
        imageView_birth_cert5= (ImageView)findViewById(R.id.imageView_birth_cert5);
        imageView_birth_cert6= (ImageView)findViewById(R.id.imageView_birth_cert6);

        spinner_relationship1= (Spinner)findViewById(R.id.spinner_relationship1);
        spinner_relationship2= (Spinner)findViewById(R.id.spinner_relationship2);
        spinner_relationship3= (Spinner)findViewById(R.id.spinner_relationship3);
        spinner_relationship4 = (Spinner)findViewById(R.id.spinner_relationship4);
        spinner3 = (Spinner)findViewById(R.id.spinner3);

        cardview_one = (CardView)findViewById(R.id.cardview_one);
        cardview_two = (CardView)findViewById(R.id.cardview_two);
        cardview_three = (CardView)findViewById(R.id.cardview_three);
        cardview_four = (CardView)findViewById(R.id.cardview_four);
        cardview_five = (CardView)findViewById(R.id.cardview_five);
        cardview_six = (CardView)findViewById(R.id.cardview_six);
        cardview_seven  = (CardView)findViewById(R.id.cardview_seven);
        cardView_beneficiary  = (CardView)findViewById(R.id.cardView_beneficiary);
        cardview_final =  (CardView)findViewById(R.id.cardview_final);

        spouse_and_kids_info2 =  (CardView)findViewById(R.id.spouse_and_kids_info2);
        add_parents_cardView =  (CardView)findViewById(R.id.add_parents_cardView);
        spouse_and_kids_info =  (CardView)findViewById(R.id.spouse_and_kids_info);

        buttonSelectPlan = (Button ) findViewById(R.id.buttonSelectPlan);
        button3 = (Button ) findViewById(R.id.button3);
        button6 = (Button ) findViewById(R.id.button6);
        button5 = (Button ) findViewById(R.id.button5);
        button8 = (Button ) findViewById(R.id.button8);
        button7 = (Button ) findViewById(R.id.button7);
        button9 = (Button ) findViewById(R.id.button9);
        button11 = (Button ) findViewById(R.id.button11);
         button4 = (Button ) findViewById(R.id.button4);
        nextToLayout_parentRecycler= (Button ) findViewById(R.id.nextToLayout_parentRecycler);
        nextTocardview_final= (Button ) findViewById(R.id.nextTocardview_final);

        textView75 = (TextView )findViewById(R.id.textView75);
        textView66 = (TextView )findViewById(R.id.textView66);
        textView67 = (TextView )findViewById(R.id.textView67);
        textView68 = (TextView )findViewById(R.id.textView68);
        textView62 = (TextView )findViewById(R.id.textView62);
        textView72 = (TextView )findViewById(R.id.textView72);
        textView74 = (TextView )findViewById(R.id.textView74);
        textView80 = (TextView )findViewById(R.id.textView80);
        textView81 = (TextView )findViewById(R.id.textView81);
        textView62 = (TextView )findViewById(R.id.textView62);
        textView82 = (TextView )findViewById(R.id.textView82);

        textView123= (TextView )findViewById(R.id.textView123);
        textView107= (TextView )findViewById(R.id.textView107);
        textView108= (TextView )findViewById(R.id.textView108);
        textView109= (TextView )findViewById(R.id.textView109);
        textView110= (TextView )findViewById(R.id.textView110);
        TextViewDOB= (TextView )findViewById(R.id.TextViewDOB);

        textView73= (TextView )findViewById(R.id.textView73);
        textView78= (TextView )findViewById(R.id.textView78);
        textView83= (TextView )findViewById(R.id.textView83);
        textView84= (TextView )findViewById(R.id.textView84);

        kids_number_TextInputEditText = (TextInputEditText ) findViewById(R.id.kids_number_TextInputEditText);

        add_parent_layout=(LinearLayout)findViewById(R.id.add_parent_layout);
        Layout_step_onelabel=(LinearLayout)findViewById(R.id.Layout_step_onelabel);
        layout_parentRecycler=(LinearLayout)findViewById(R.id.layout_parentRecycler);

        String benefitsInfo = "<b>Benefits:</b>";
        textView74.setText(Html.fromHtml(benefitsInfo));

        String stringText1 = "Accidental Loss <br>of Life Benefit ";
        textView123.setText(Html.fromHtml(stringText1));

        String stringText2 = "Accidental Impairment<br> Benefit ";
        textView107.setText(Html.fromHtml(stringText2));

        String stringText3 = "Ksh. 50,000 or Ksh. 200,000<br> depending on severity ";
        textView108.setText(Html.fromHtml(stringText3));

        String stringText4 = "Ksh. 100,000 or Ksh. 200,000<br> depending on severity";
        textView109.setText(Html.fromHtml(stringText4));

        String stringText5 = "Ksh. 250,000 or Ksh. 500,000<br> depending on severity";
        textView110.setText(Html.fromHtml(stringText5));

        editText_parents_num = (TextInputEditText ) findViewById(R.id.editText_parents_num);
        spouse_dob = (EditText)findViewById(R.id.spouse_dob);
        spouse_dob.setOnClickListener(this);

        spinner2 = (Spinner)findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (spinner2.getSelectedItem().toString().equalsIgnoreCase("")){
                    button3.setVisibility(View.GONE);
                }
                else
                {
                    button3.setVisibility(View.VISIBLE);
                    paymentDuration = spinner2.getSelectedItem().toString();
                    showPricingDetails();
                }
                //  L.t(LegacyLifePlan.this, "Something has been selected on the spinner");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                // L.t(LegacyLifePlan.this, "Nothing has been selected on the spinner");
            }
        });

        cardview_one.setOnClickListener(this);
        cardview_two.setOnClickListener(this);
        cardview_three.setOnClickListener(this);
        cardview_four.setOnClickListener(this);
        cardview_five.setOnClickListener(this);
        cardview_six.setOnClickListener(this);
        cardview_seven.setOnClickListener(this);

        buttonSelectPlan.setOnClickListener(this);
        button3.setOnClickListener(this);
        button6.setOnClickListener(this);
        button5.setOnClickListener(this);
        button8.setOnClickListener(this);
        button7.setOnClickListener(this);
        button9.setOnClickListener(this);
        button11.setOnClickListener(this);
        button4.setOnClickListener(this);
        nextTocardview_final.setOnClickListener(this);
        nextToLayout_parentRecycler.setOnClickListener(this);

        layout_kidsHorizontalScroll=(LinearLayout)findViewById(R.id.layout_kidsHorizontalScroll);

        TextInputLayout_Name = (TextInputLayout ) findViewById(R.id.TextInputLayout_Name);
        TextInputLayout_IdNumber = (TextInputLayout ) findViewById(R.id.TextInputLayout_IdNumber);
        TextInputLayout_PhoneNumber = (TextInputLayout ) findViewById(R.id.TextInputLayout_PhoneNumber);
        kids_number_TextInputLayout= (TextInputLayout ) findViewById(R.id.kids_number_TextInputLayout);

        TextInputEditText_Name = (TextInputEditText )findViewById(R.id.TextInputEditText_Name);
        TextInputEditText_IdNumber = (TextInputEditText )findViewById(R.id.TextInputEditText_IdNumber);
        TextInputEditText_PhoneNumber = (TextInputEditText )findViewById(R.id.TextInputEditText_PhoneNumber);

        legacyDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendarLegacy.set(Calendar.YEAR, year);
                myCalendarLegacy.set(Calendar.MONTH, monthOfYear);
                myCalendarLegacy.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_upload_photo1:
                buttonUpload="button_upload_photo1";
                openDocuments();
                break;

            case R.id.button_upload_photo2:
                buttonUpload="button_upload_photo2";
                openDocuments();
                break;

            case R.id.button_upload_photo3:
                buttonUpload="button_upload_photo3";
                openDocuments();
                break;

            case R.id.button_upload_photo4:
                buttonUpload="button_upload_photo4";
                openDocuments();
                break;

            case R.id.button_upload_photo5:
                buttonUpload="button_upload_photo5";
                openDocuments();
                break;

            case R.id.button_upload_photo6:
                buttonUpload="button_upload_photo6";
                openDocuments();
                break;

            case R.id.button_open_camera1:
                buttonCamera="button_open_camera1";
                openCamera();
                break;

            case R.id.button_open_camera2:
                buttonCamera="button_open_camera2";
                openCamera();
                break;

            case R.id.button_open_camera3:
                buttonCamera="button_open_camera3";
                openCamera();
                break;

            case R.id.button_open_camera4:
                buttonCamera="button_open_camera4";
                openCamera();
                break;

            case R.id.button_open_camera5:
                buttonCamera="button_open_camera5";
                openCamera();
                break;

            case R.id.button_open_camera6:
                buttonCamera="button_open_camera6";
                openCamera();
                break;

            case R.id.button11:

                if (switchSpouse.isChecked())
                {
                    if (validateField(TextInputEditText_Name, 3, getString(R.string.invalid_name))
                            &&  validateField(TextInputEditText_IdNumber, 6, getString(R.string.invalid_id_no))
                            && validatePhoneField())
                    {
                        if (spouse_dob.getText().length() > 0)
                        {
                            spouse_dob.setError(null);
                            spouse_and_kids_info2.setVisibility(View.VISIBLE);
                            spouse_and_kids_info.setVisibility(View.GONE);
                        }
                        else
                        {
                            spouse_dob.setError("Cannot be blank!");
                        }
                    }
                }
                else
                {
                    spouse_and_kids_info2.setVisibility(View.VISIBLE);
                    spouse_and_kids_info.setVisibility(View.GONE);
                }
                break;

            case R.id.spouse_dob:
                // Show datePickerFragment
                dob_editText="dob_spouse";
               getDate();
                break;

            case R.id.button9:

                if (switchSpouse.isChecked())
                {
                    if (validateField(TextInputEditText_Name, 3, getString(R.string.invalid_name))
                            &&  validateField(TextInputEditText_IdNumber, 6, getString(R.string.invalid_id_no))
                            && validatePhoneField())
                    {
                        if (spouse_dob.getText().length() > 0)
                        {
                            spouse_dob.setError(null);
                            String string_kids_num = kids_number_TextInputEditText.getText().toString();

                            String kids_number3 = kids_number_TextInputEditText.getText().toString();
                            if (TextUtils.isEmpty(kids_number3))
                            {
                                kids_number_TextInputEditText.setError("Kids: Between 1 to 6");
                            }
                            else if (string_kids_num.equalsIgnoreCase("1") ||
                                    string_kids_num.equalsIgnoreCase("2") ||
                                    string_kids_num.equalsIgnoreCase("3") ||
                                    string_kids_num.equalsIgnoreCase("4") ||
                                    string_kids_num.equalsIgnoreCase("5") ||
                                    string_kids_num.equalsIgnoreCase("6"))
                            {
                                kids_number_TextInputEditText.setError(null);

                                    kids_number = kids_number_TextInputEditText.getText().toString();
                                    kids_number2 =Integer.parseInt(kids_number);

                                spouse_and_kids_info2.setVisibility(View.GONE);
                                add_parents_cardView.setVisibility(View.VISIBLE);

                            }
                            else
                            {
                                kids_number_TextInputEditText.setError("Kids: Between 1 to 6");
                            }
                        }
                        else{
                            spouse_dob.setError("Cannot be empty!");
                        }
                    }
                }
                else
                {
                        spouse_and_kids_info2.setVisibility(View.GONE);
                        add_parents_cardView.setVisibility(View.VISIBLE);
                }

                break;




            case R.id.cardview_one:

                //show cardview4-6
                cardview_four.setVisibility(View.VISIBLE);
                cardview_four.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_right));
                cardview_five.setVisibility(View.VISIBLE);
                cardview_five.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_right2));
                cardview_six.setVisibility(View.VISIBLE);
                cardview_six.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_right3));

                //hide cardview1-3
                cardview_one.setVisibility(View.GONE);
                cardview_two.setVisibility(View.GONE);
                //  cardview_three.setVisibility(View.GONE);
                //add_parents_cardView.setVisibility(View.GONE);
                Layout_step_onelabel.setVisibility(View.GONE);

                packageName="individual";

                String silverIndividual = "Cover: Kshs <b>100,000</b><br>Monthly Premium: Kshs <b>400</b><br>Annual Premium: Kshs <b>4,325</b>";
                textView66.setText(Html.fromHtml(silverIndividual));
                String goldIndividual = "Cover: Kshs <b>200,000</b><br>Monthly Premium: Kshs <b>800</b><br>Annual Premium: Kshs <b>8650</b>";
                textView67.setText(Html.fromHtml(goldIndividual));
                //Silver
                String platinumIndividual = "Cover: Kshs <b>500,000</b><br>Monthly Premium: Kshs <b>2,000</b><br>Annual Premium: Kshs <b>21,625</b>";
                textView68.setText(Html.fromHtml(platinumIndividual));
                break;

            case R.id.button7:
                if (switch_ParentsCover.isChecked()==(true))
                {
                    String string_parents_num = editText_parents_num.getText().toString();
                    if (string_parents_num.equalsIgnoreCase("1") ||
                            string_parents_num.equalsIgnoreCase("2") ||
                            string_parents_num.equalsIgnoreCase("3") ||
                            string_parents_num.equalsIgnoreCase("4") )
                    {
                        editText_parents_num.setError(null);
                        parents_number = editText_parents_num.getText().toString();
                        if (TextUtils.isEmpty(parents_number))
                        {
                            //nothing happens
                        }
                        else
                        {
                            parents_number = editText_parents_num.getText().toString();
                            parents_number2=Integer.parseInt(parents_number);
                            clickbutton7();

                            if (paymentDuration.equalsIgnoreCase(""))
                            {

                            }
                            else
                            {
                                showPricingDetails();
                            }
                        }
                    }
                    else{
                        // L.t(this,"Parents: Between 1 to 4");
                        editText_parents_num.setError("Parents: Between 1 to 4");
                    }
                }
                else  if (switch_ParentsCover.isChecked()==(false))
                {
                    clickbutton7();
                }
                break;
            case R.id.button6:
                //show select payment period
                cardview_seven.setVisibility(View.VISIBLE);
                cardview_seven.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_right));
                spouse_and_kids_info.setVisibility(View.GONE);
                add_parents_cardView.setVisibility(View.GONE);

                //hide cardview4-6
                cardview_four.setVisibility(View.GONE);
                cardview_five.setVisibility(View.GONE);
                cardview_six.setVisibility(View.GONE);
                parentCoverAdded="no";
                break;

            case R.id.button5:
                editText_parents_num.setVisibility(View.VISIBLE);
                textView62.setVisibility(View.VISIBLE);
                button6.setVisibility(View.GONE);
                button5.setVisibility(View.GONE);
                parentCoverAdded="yes";
                break;

            case R.id.cardview_two:
                cardview_four.setVisibility(View.VISIBLE);
                cardview_four.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_right));
                cardview_five.setVisibility(View.VISIBLE);
                cardview_five.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_right2));
                cardview_six.setVisibility(View.VISIBLE);
                cardview_six.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_right3));
                cardview_one.setVisibility(View.GONE);
                cardview_two.setVisibility(View.GONE);
                Layout_step_onelabel.setVisibility(View.GONE);

                //Silver
                String silverIndividual2 = "Cover: Kshs <b>100,000</b><br>Monthly Premium: Kshs <b>680</b><br>Annual Premium: Kshs <b>7,325</b>";
                textView66.setText(Html.fromHtml(silverIndividual2));
                //Silver
                String goldIndividual2 = "Cover: Kshs <b>200,000</b><br>Monthly Premium: Kshs <b>1,360</b><br>Annual Premium: Kshs <b>14,650</b>";
                textView67.setText(Html.fromHtml(goldIndividual2));
                //Silver
                String platinumIndividual2 = "Cover: Kshs <b>500,000</b><br>Monthly Premium: Kshs <b>3,400</b><br>Annual Premium: Kshs <b>36,625</b>";
                textView68.setText(Html.fromHtml(platinumIndividual2));

                packageName="family";
                break;

            case R.id.cardview_four:

                toStepThree();

                packageLevel="Silver";
                break;

            case R.id.cardview_five:
                toStepThree();
                packageLevel="Gold";
                break;

            case R.id.cardview_six:
                toStepThree();
                packageLevel="Platinum";
                break;

            case R.id.buttonSelectPlan:
                //Hide button
                buttonSelectPlan.setVisibility(View.GONE);
                textView74.setVisibility(View.GONE);
                benefits_tableLayout.setVisibility(View.GONE);

                //show cardview1-3
                Layout_step_onelabel.setVisibility(View.VISIBLE);
                Layout_step_onelabel.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_right));
                cardview_one.setVisibility(View.VISIBLE);
                cardview_one.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_right));
                cardview_two.setVisibility(View.VISIBLE);
                cardview_two.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_right2));
                break;

            case R.id.button3:
                paymentDuration = spinner2.getSelectedItem().toString();

                if (switchKids.isChecked()==(false) &&
                        switch_ParentsCover.isChecked()==(false))
                {
                    cardview_seven.setVisibility(View.GONE);
                    cardView_beneficiary.setVisibility(View.VISIBLE);
                }
                else if (switchKids.isChecked()==(true))
                {
                    if (switchKids.isChecked()) {
                        layout_kidsHorizontalScroll.setVisibility(View.VISIBLE);
                        showKidsRecycler();
                    }
                    else {
                        layout_kidsHorizontalScroll.setVisibility(View.GONE);
                    }
                }
                cardview_seven.setVisibility(View.GONE);
                break;

            case R.id.nextToLayout_parentRecycler:
                if (cardView_kids1.getVisibility()==(View.VISIBLE))
                {
                    if (validateField(TextInputEditText_name1, 3, getString(R.string.invalid_name)))
                    {
                        if (kids_date_of_birth1.getText().length() > 0)
                        {
                            TextInputEditText_name1.setError(null);
                            kids_date_of_birth1.setError(null);
                            showParentsRecycler2();
                        }
                        else
                        {
                            kids_date_of_birth1.setError("Cannot be blank!");
                        }
                    }
                    else
                    {
                        TextInputEditText_name1.setError("Cannot be blank!!");
                    }
                }

                if (cardView_kids2.getVisibility()==(View.VISIBLE))
                {
                    if (validateField(TextInputEditText_name2, 3, getString(R.string.invalid_name)))
                    {
                        if (kids_date_of_birth2.getText().length() > 0)
                        {
                            TextInputEditText_name2.setError(null);
                            kids_date_of_birth2.setError(null);
                            showParentsRecycler2();
                        }
                        else
                        {
                            kids_date_of_birth2.setError("Cannot be blank!");
                        }
                    }
                    else
                    {
                        TextInputEditText_name2.setError("Cannot be blank!!");
                    }
                }
                if (cardView_kids3.getVisibility()==(View.VISIBLE))
                {
                    if (validateField(TextInputEditText_name3, 3, getString(R.string.invalid_name)))
                    {
                        if (kids_date_of_birth3.getText().length() > 0)
                        {
                            TextInputEditText_name3.setError(null);
                            kids_date_of_birth3.setError(null);
                            showParentsRecycler2();
                        }
                        else
                        {
                            kids_date_of_birth3.setError("Cannot be blank!");
                        }
                    }
                    else
                    {
                        TextInputEditText_name3.setError("Cannot be blank!!");
                    }
                }
                if (cardView_kids4.getVisibility()==(View.VISIBLE))
                {
                    if (validateField(TextInputEditText_name4, 3, getString(R.string.invalid_name)))
                    {
                        if (kids_date_of_birth4.getText().length() > 0)
                        {
                            TextInputEditText_name4.setError(null);
                            kids_date_of_birth4.setError(null);
                            showParentsRecycler2();
                        }
                        else
                        {
                            kids_date_of_birth4.setError("Cannot be blank!");
                        }
                    }
                    else
                    {
                        TextInputEditText_name4.setError("Cannot be blank!!");
                    }
                }
                if (cardView_kids5.getVisibility()==(View.VISIBLE))
                {
                    if (validateField(TextInputEditText_name5, 3, getString(R.string.invalid_name)))
                    {
                        if (kids_date_of_birth5.getText().length() > 0)
                        {
                            TextInputEditText_name5.setError(null);
                            kids_date_of_birth5.setError(null);
                            showParentsRecycler2();
                        }
                        else
                        {
                            kids_date_of_birth5.setError("Cannot be blank!");
                        }
                    }
                    else
                    {
                        TextInputEditText_name5.setError("Cannot be blank!!");
                    }
                }
                if (cardView_kids6.getVisibility()==(View.VISIBLE))
                {
                    if (validateField(TextInputEditText_name6, 3, getString(R.string.invalid_name)))
                    {
                        if (kids_date_of_birth6.getText().length() > 0)
                        {
                            TextInputEditText_name6.setError(null);
                            kids_date_of_birth6.setError(null);
                            showParentsRecycler2();
                        }
                        else
                        {
                            kids_date_of_birth6.setError("Cannot be blank!");
                        }
                    }
                    else
                    {
                        TextInputEditText_name6.setError("Cannot be blank!!");
                    }
                }
                if(
                        cardView_kids1.getVisibility()==(View.GONE) &&
                                cardView_kids2.getVisibility()==(View.GONE) &&
                                cardView_kids3.getVisibility()==(View.GONE) &&
                                cardView_kids4.getVisibility()==(View.GONE) &&
                                cardView_kids5.getVisibility()==(View.GONE) &&
                                cardView_kids6.getVisibility()==(View.GONE)
                        )
                {
                    showParentsRecycler2();
                }
                break;

            case R.id.nextTocardview_final:
             cardView_beneficiary.setVisibility(View.VISIBLE);
                layout_parentRecycler.setVisibility(View.GONE);
                break;

            case R.id.button4:
               sendDataToServer();
                break;

            case R.id.kids_date_of_birth1:
                dob_editText="kids_date_of_birth1";
                getDate();
                break;

            case R.id.kids_date_of_birth2:
                dob_editText="kids_date_of_birth2";
                getDate();
                break;

            case R.id.kids_date_of_birth3:
                dob_editText="kids_date_of_birth3";
                getDate();
                break;

            case R.id.kids_date_of_birth4:
                dob_editText="kids_date_of_birth4";
                getDate();
                break;

            case R.id.kids_date_of_birth5:
                dob_editText="kids_date_of_birth5";
                getDate();
                break;

            case R.id.kids_date_of_birth6:
                dob_editText="kids_date_of_birth6";
                getDate();
                break;

            case R.id.pd_date_of_birth1:
                dob_editText="pd_date_of_birth1";
                getDate();
                break;

            case R.id.pd_date_of_birth2:
                dob_editText="pd_date_of_birth2";
                getDate();
                break;

            case R.id.pd_date_of_birth3:
                dob_editText="pd_date_of_birth3";
                getDate();
                break;

            case R.id.pd_date_of_birth4:
                getDate();
                dob_editText="pd_date_of_birth4";
                break;

            case R.id.button8:
                cardview_final.setVisibility(View.VISIBLE);
                cardView_beneficiary.setVisibility(View.GONE);
                break;
        }
    }

    private void AlgorithmOne() {

        if (spinner2.getSelectedItem().toString().equalsIgnoreCase("")){
            button3.setVisibility(View.GONE);
        }
        else
        {
            button3.setVisibility(View.VISIBLE);
        }

        if (switchKids.isChecked() ||  parentCoverAdded.equalsIgnoreCase("yes"))
        {
            if (switchKids.isChecked() &&  parentCoverAdded.equalsIgnoreCase("no"))
            {
                button3.setText("Fill in your Kids info and Check Out");
            }
            else  if ( parentCoverAdded.equalsIgnoreCase("yes") && switchKids.isChecked()==(false))
            {
                button3.setText("Fill in your Parents info and Check Out");
            }
            else  if ( parentCoverAdded.equalsIgnoreCase("yes") && switchKids.isChecked())
            {
                button3.setText("Fill in your Kids and Parents info, then Check Out");
            }
        }
    }

    private void showPricingDetails() {

        Map<String, Integer> map = new HashMap<>();
        map.put("individualMonthlySilver", 400);
        map.put("individualQuarterlySilver", 1140);
        map.put("individualBiAnnuallySilver", 2280);
        map.put("individualAnnuallySilver", 4325);

        map.put("individualMonthlyGold", 800);
        map.put("individualQuarterlyGold", 2280);
        map.put("individualBiAnnuallyGold", 4560);
        map.put("individualAnnuallyGold", 8650);

        map.put("individualMonthlyPlatinum", 2000);
        map.put("individualQuarterlyPlatinum", 5700);
        map.put("individualBiAnnuallyPlatinum", 11400);
        map.put("individualAnnuallyPlatinum", 21625);

        map.put("familyMonthlySilver", 680);
        map.put("familyQuarterlySilver", 1938);
        map.put("familyBiAnnuallySilver", 3876);
        map.put("familyAnnuallySilver", 7325);

        map.put("familyMonthlyGold", 1360);
        map.put("familyQuarterlyGold", 3876);
        map.put("familyBiAnnuallyGold", 7752);
        map.put("familyAnnuallyGold", 14650);

        map.put("familyMonthlyPlatinum", 3400);
        map.put("familyQuarterlyPlatinum", 9690);
        map.put("familyBiAnnuallyPlatinum", 19380);
        map.put("familyAnnuallyPlatinum", 36625);

        price=packageName+paymentDuration+packageLevel;
        parentPackageLevel=spinner3.getSelectedItem().toString();
        price2 = map.get(price);

        if(switch_ParentsCover.isChecked()==(true))
        {
            someMethod();

        }
        else if (switch_ParentsCover.isChecked()==(false))
        {
            someMethod2();
        }

        String stringText1 = "You have Selected the <b>"+packageName+" "+packageLevel+"</b><br> package. You have chosen to pay premiums <b>"+paymentDuration+"</b><br>The premiums calculator figures your premium amount is : <b>Kshs. "+price2+"</b>.";
        textView75.setVisibility(View.VISIBLE);
        textView75.setText(Html.fromHtml(stringText1));

        if (spouseAdded.equalsIgnoreCase("yes"))
        {
            String stringText2 = "You have added a spouse.";
            textView78.setVisibility(View.VISIBLE);
            textView78.setText(Html.fromHtml(stringText2));
        }
        else if (spouseAdded.equalsIgnoreCase("no"))
        {
           textView78.setText("");
           textView78.setVisibility(View.GONE);
        }

        if (kidsAdded.equalsIgnoreCase("yes"))
        {
            String stringText4 = "<br>You have added "+kids_number2+" children to your cover";
            textView83.setVisibility(View.VISIBLE);
            textView83.setText(Html.fromHtml(stringText4));
        }
        else if(kidsAdded.equalsIgnoreCase("no"))
        {
                 textView83.setText("");
                 textView83.setVisibility(View.GONE);
        }

        if (parentCoverAdded.equalsIgnoreCase("yes"))
        {
            String stringText6 = "You have added the Parent Cover for " +parents_number2+"  parents</b><br>The premiums calculator figures your premium amount is : <b>Kshs. "+parentTotal+"</b>, The total cost is: Kshs. "+legacyPremium+"";
            textView84.setVisibility(View.VISIBLE);
            textView84.setText(Html.fromHtml(stringText6));
        }
        else if  (parentCoverAdded.equalsIgnoreCase("no"))
        {
              textView84.setText("");
              textView84.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            //new MainActivity().createDialog();
            return true;
        }

        if (id == R.id.action_refresh) {
            L.t(MyApplication.getAppContext(), "Refreshing balance");
            new TaskLoadBalance(( BalanceLoadedListener ) this).execute();
            return true;
        }

        if (id == R.id.action_settings) {
            //  new MainActivity().displayView(R.id.nav_settings);
            return true;
        }

        if (id == R.id.action_notification) {
            startActivity(new Intent(MyApplication.getAppContext(), NotificationActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public  void onBackPressed() {
        if (buttonSelectPlan.getVisibility() == View.VISIBLE) {
            finish();
        }

        else if (cardview_one.getVisibility() == View.VISIBLE || cardview_two.getVisibility() == View.VISIBLE) {
            //Hide all cardviews
            cardview_one.setVisibility(View.GONE);
            Layout_step_onelabel.setVisibility(View.GONE);
            cardview_two.setVisibility(View.GONE);
            //cardview_three.setVisibility(View.GONE);
            cardview_four.setVisibility(View.GONE);
            cardview_five.setVisibility(View.GONE);
            cardview_six.setVisibility(View.GONE);
            cardview_seven.setVisibility(View.GONE);
            add_parents_cardView.setVisibility(View.GONE);
          //  button7.setVisibility(View.GONE);
            spouse_and_kids_info.setVisibility(View.GONE);

            //Show buttonSelectPlan
            buttonSelectPlan.setVisibility(View.VISIBLE);
            textView74.setVisibility(View.VISIBLE);
            benefits_tableLayout.setVisibility(View.VISIBLE);
        }

        else if (cardview_four.getVisibility() == View.VISIBLE) {
            //Hide all cardviews
            cardview_four.setVisibility(View.GONE);
            cardview_five.setVisibility(View.GONE);
            cardview_six.setVisibility(View.GONE);
            cardview_seven.setVisibility(View.GONE);
            add_parents_cardView.setVisibility(View.GONE);
          //  button7.setVisibility(View.GONE);

            //Show step one
            Layout_step_onelabel.setVisibility(View.VISIBLE);
            Layout_step_onelabel.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_left));
            cardview_one.setVisibility(View.VISIBLE);
            cardview_one.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_left));
            cardview_two.setVisibility(View.VISIBLE);
            cardview_two.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_left2));
            //  cardview_three.setVisibility(View.VISIBLE);
            //  cardview_three.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_left3));
        }

        else if (cardview_seven.getVisibility() == View.VISIBLE) {
          add_parents_cardView.setVisibility(View.VISIBLE);
            cardview_seven.setVisibility(View.GONE);
        }

        else if (cardview_final.getVisibility() == View.VISIBLE) {
            cardview_final.setVisibility(View.GONE);
            cardView_beneficiary.setVisibility(View.VISIBLE);
        }

        else if (add_parents_cardView.getVisibility() == View.VISIBLE) {

            if (packageName.equalsIgnoreCase("individual"))
            {
             showStepTwo();
             add_parents_cardView.setVisibility(View.GONE);
            }
            else if (packageName.equalsIgnoreCase("family"))
            {
                spouse_and_kids_info2.setVisibility(View.VISIBLE);
                add_parents_cardView.setVisibility(View.GONE);
            }
        }
        else if (layout_parentRecycler.getVisibility() == View.VISIBLE) {

            layout_parentRecycler.setVisibility(View.GONE);
            layout_kidsHorizontalScroll.setVisibility(View.VISIBLE);


        }

        else if (layout_kidsHorizontalScroll.getVisibility() == View.VISIBLE) {

            cardview_seven.setVisibility(View.VISIBLE);
            layout_kidsHorizontalScroll.setVisibility(View.GONE);
        }

        else if (spouse_and_kids_info.getVisibility() == View.VISIBLE) {

            showStepTwo();
            spouse_and_kids_info.setVisibility(View.GONE);

        }

        else if (spouse_and_kids_info2.getVisibility() == View.VISIBLE) {

            spouse_and_kids_info.setVisibility(View.VISIBLE);
            spouse_and_kids_info2.setVisibility(View.GONE);
        }

        else if (cardView_beneficiary.getVisibility() == View.VISIBLE) {

            if (switch_ParentsCover.isChecked()==(false) &&
                    switchKids.isChecked()==(false))
            {
                cardView_beneficiary.setVisibility(View.GONE);
                cardview_seven.setVisibility(View.VISIBLE);
            }
            else if (switch_ParentsCover.isChecked()==(false) &&
                    switchKids.isChecked()==(true))
            {
                layout_kidsHorizontalScroll.setVisibility(View.VISIBLE);
                cardView_beneficiary.setVisibility(View.GONE);
            }
            else if (switch_ParentsCover.getVisibility()==(View.VISIBLE))
            {
                layout_parentRecycler.setVisibility(View.VISIBLE);
                cardView_beneficiary.setVisibility(View.GONE);
            }
        }

    }

        public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

        @SuppressLint("RestrictedApi")
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {


                if (requestCode ==7777 && resultCode == RESULT_OK)
                {
                    Bitmap photo = (Bitmap)data.getExtras().get("data");
                    //ImageView imageView_birth_cert = (ImageView)findViewById(R.id.imageView_birth_cert);
                    imageView_birth_cert1.setImageBitmap(photo);
                }


            if (resultCode == Activity.RESULT_OK && data != null) {

                if (requestCode == GALLERY_REQUEST_ONE ) {
                    try {
                        // From the gallery
                        InputStream inputStream = MyApplication.getAppContext().getContentResolver().openInputStream(data.getData());

                        if (inputStream != null) {
                            L.m(inputStream.toString());
                            switch (requestCode) {
                                case GALLERY_REQUEST_ONE:

                                    if (buttonUpload.equalsIgnoreCase("button_upload_photo1")){
                                            imageView_birth_cert1.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                            imageView_birth_cert1_string = inputStream.toString();
                                        }

                                    if (buttonUpload.equalsIgnoreCase("button_upload_photo2")){
                                        imageView_birth_cert2.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                        imageView_birth_cert2_string = inputStream.toString();
                                    }

                                    if (buttonUpload.equalsIgnoreCase("button_upload_photo3")){
                                        imageView_birth_cert3.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                        imageView_birth_cert3_string = inputStream.toString();
                                    }

                                    if (buttonUpload.equalsIgnoreCase("button_upload_photo4")){
                                        imageView_birth_cert4.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                        imageView_birth_cert4_string = inputStream.toString();
                                    }

                                    if (buttonUpload.equalsIgnoreCase("button_upload_photo5")){
                                        imageView_birth_cert5.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                        imageView_birth_cert5_string = inputStream.toString();
                                    }

                                    if (buttonUpload.equalsIgnoreCase("button_upload_photo6")){
                                        imageView_birth_cert6.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                        imageView_birth_cert6_string = inputStream.toString();
                                    }

                                    break;
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                } else {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");

                    switch (requestCode) {
                        case CAMERA_REQUEST_ONE:


                            if (buttonCamera.equalsIgnoreCase("button_open_camera1")){
                                imageView_birth_cert1.setImageBitmap(photo);
                                imageView_birth_cert1_string =ValidationUtil.getStringImage(photo);
                            }

                            if (buttonCamera.equalsIgnoreCase("button_open_camera2")){
                                imageView_birth_cert2.setImageBitmap(photo);
                                imageView_birth_cert2_string =ValidationUtil.getStringImage(photo);
                            }
                            if (buttonCamera.equalsIgnoreCase("button_open_camera3")){
                                imageView_birth_cert3.setImageBitmap(photo);
                                imageView_birth_cert3_string =ValidationUtil.getStringImage(photo);
                            }
                            if (buttonCamera.equalsIgnoreCase("button_open_camera4")){
                                imageView_birth_cert4.setImageBitmap(photo);
                                imageView_birth_cert4_string =ValidationUtil.getStringImage(photo);
                            }
                            if (buttonCamera.equalsIgnoreCase("button_open_camera5")){
                                imageView_birth_cert5.setImageBitmap(photo);
                                imageView_birth_cert5_string =ValidationUtil.getStringImage(photo);
                            }
                            if (buttonCamera.equalsIgnoreCase("button_open_camera6")){
                                imageView_birth_cert6.setImageBitmap(photo);
                                imageView_birth_cert6_string =ValidationUtil.getStringImage(photo);
                            }

                            break;

                    }
                }
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

        private boolean validatePhoneField() {
        if (!ValidationUtil.isValidPhoneNumber(TextInputEditText_PhoneNumber)) {
            TextInputEditText_PhoneNumber.setError(getString(R.string.invalid_phone_number));
            requestFocus(TextInputEditText_PhoneNumber);
            return false;
        }
        return true;
    }

        private void updateLabel() {
            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
            String yearFormat = "yyyy";
            SimpleDateFormat sdfYear = new SimpleDateFormat(yearFormat, Locale.UK);
            yearOfBirth = sdfYear.format(myCalendarLegacy.getTime());

            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int chosenYear = Integer.parseInt(yearOfBirth);

            if (currentYear < chosenYear)
            {
                L.t(this, "Please provide a date that isn't in the future");
            }
            else if (currentYear > chosenYear)
            {
                if (dob_editText.equalsIgnoreCase("dob_spouse")) {
                    myCalendarLegacy.add(Calendar.YEAR, 64);
                } else if (dob_editText.equalsIgnoreCase("kids_date_of_birth1") ||
                        dob_editText.equalsIgnoreCase("kids_date_of_birth2") ||
                        dob_editText.equalsIgnoreCase("kids_date_of_birth3") ||
                        dob_editText.equalsIgnoreCase("kids_date_of_birth4") ||
                        dob_editText.equalsIgnoreCase("kids_date_of_birth5") ||
                        dob_editText.equalsIgnoreCase("kids_date_of_birth6")
                        ) {
                    myCalendarLegacy.add(Calendar.YEAR, 25);
                } else if (dob_editText.equalsIgnoreCase("pd_date_of_birth4") ||
                        dob_editText.equalsIgnoreCase("pd_date_of_birth3") ||
                        dob_editText.equalsIgnoreCase("pd_date_of_birth2") ||
                        dob_editText.equalsIgnoreCase("pd_date_of_birth1")
                        ) {
                    myCalendarLegacy.add(Calendar.YEAR, 75);
                }

                Date tarehe = myCalendarLegacy.getTime();
                Date hivisasa = new Date();

                if (dob_editText.equalsIgnoreCase("dob_spouse")) {
                    if (tarehe.before(hivisasa)) {
                        //Too old for this policy
                        spouse_dob.setText(null);
                        spouse_dob.setError("");
                        L.t(this, "Your spouse is too old for this policy");
                    } else if (tarehe.after(hivisasa) || (tarehe.equals(hivisasa))) {
                        //Within Age Limit
                        myCalendarLegacy.add(Calendar.YEAR, -64);
                        spouse_dob.setError(null);
                        spouse_dob.setText(sdf.format(myCalendarLegacy.getTime()));
                        //L.t(this, hivisasa.toString());
                    }
                } else if (dob_editText.equalsIgnoreCase("kids_date_of_birth1")) {
                    if (tarehe.before(hivisasa)) {
                        //Too old for this policy
                        kids_date_of_birth1.setText(null);
                        kids_date_of_birth1.setError("");
                        L.t(this, "Your child is too old for this policy");
                    } else if (tarehe.after(hivisasa) || (tarehe.equals(hivisasa))) {
                        //Within Age Limit
                        myCalendarLegacy.add(Calendar.YEAR, -25);
                        kids_date_of_birth1.setError(null);
                        kids_date_of_birth1.setText(sdf.format(myCalendarLegacy.getTime()));
                        //  L.t(this, hivisasa.toString());
                    }
                } else if (dob_editText.equalsIgnoreCase("kids_date_of_birth2")) {
                    if (tarehe.before(hivisasa)) {
                        //Too old for this policy
                        kids_date_of_birth2.setText(null);
                        kids_date_of_birth2.setError("");
                        L.t(this, "Your child is too old for this policy");
                    } else if (tarehe.after(hivisasa) || (tarehe.equals(hivisasa))) {
                        //Within Age Limit
                        myCalendarLegacy.add(Calendar.YEAR, -25);
                        kids_date_of_birth2.setError(null);
                        kids_date_of_birth2.setText(sdf.format(myCalendarLegacy.getTime()));
                        //L.t(this, hivisasa.toString());
                    }
                } else if (dob_editText.equalsIgnoreCase("kids_date_of_birth3")) {
                    if (tarehe.before(hivisasa)) {
                        //Too old for this policy
                        kids_date_of_birth3.setText(null);
                        kids_date_of_birth3.setError("");
                        L.t(this, "Your child is too old for this policy");
                    } else if (tarehe.after(hivisasa) || (tarehe.equals(hivisasa))) {
                        //Within Age Limit
                        myCalendarLegacy.add(Calendar.YEAR, -25);
                        kids_date_of_birth3.setError(null);
                        kids_date_of_birth3.setText(sdf.format(myCalendarLegacy.getTime()));
                        // L.t(this, hivisasa.toString());
                    }
                } else if (dob_editText.equalsIgnoreCase("kids_date_of_birth4")) {
                    if (tarehe.before(hivisasa)) {
                        //Too old for this policy
                        kids_date_of_birth4.setText(null);
                        kids_date_of_birth4.setError("");
                        L.t(this, "Your child is too old for this policy");
                    } else if (tarehe.after(hivisasa) || (tarehe.equals(hivisasa))) {
                        //Within Age Limit
                        myCalendarLegacy.add(Calendar.YEAR, -25);
                        kids_date_of_birth4.setError(null);
                        kids_date_of_birth4.setText(sdf.format(myCalendarLegacy.getTime()));
                        //L.t(this, hivisasa.toString());
                    }
                } else if (dob_editText.equalsIgnoreCase("kids_date_of_birth5")) {
                    if (tarehe.before(hivisasa)) {
                        //Too old for this policy
                        kids_date_of_birth5.setText(null);
                        kids_date_of_birth5.setError("");
                        L.t(this, "Your child is too old for this policy");
                    } else if (tarehe.after(hivisasa) || (tarehe.equals(hivisasa))) {
                        //Within Age Limit
                        myCalendarLegacy.add(Calendar.YEAR, -25);
                        kids_date_of_birth5.setError(null);
                        kids_date_of_birth5.setText(sdf.format(myCalendarLegacy.getTime()));
                        //L.t(this, hivisasa.toString());
                    }
                } else if (dob_editText.equalsIgnoreCase("kids_date_of_birth6")) {
                    if (tarehe.before(hivisasa)) {
                        //Too old for this policy
                        kids_date_of_birth6.setText(null);
                        kids_date_of_birth6.setError("");
                        L.t(this, "Your child is too old for this policy");
                    } else if (tarehe.after(hivisasa) || (tarehe.equals(hivisasa))) {
                        //Within Age Limit
                        myCalendarLegacy.add(Calendar.YEAR, -25);
                        kids_date_of_birth6.setError(null);
                        kids_date_of_birth6.setText(sdf.format(myCalendarLegacy.getTime()));
                        //L.t(this, hivisasa.toString());
                    }
                } else if (dob_editText.equalsIgnoreCase("pd_date_of_birth4")) {
                    if (tarehe.before(hivisasa)) {
                        //Too old for this policy
                        pd_date_of_birth4.setText(null);
                        pd_date_of_birth4.setError("");
                        L.t(this, "Your parent is too old for this policy");
                    } else if (tarehe.after(hivisasa) || (tarehe.equals(hivisasa))) {
                        //Within Age Limit
                        myCalendarLegacy.add(Calendar.YEAR, -75);
                        pd_date_of_birth4.setError(null);
                        pd_date_of_birth4.setText(sdf.format(myCalendarLegacy.getTime()));
                        //L.t(this, hivisasa.toString());
                    }
                } else if (dob_editText.equalsIgnoreCase("pd_date_of_birth3")) {
                    if (tarehe.before(hivisasa)) {
                        //Too old for this policy
                        pd_date_of_birth3.setText(null);
                        pd_date_of_birth3.setError("");
                        L.t(this, "Your parent is too old for this policy");
                    } else if (tarehe.after(hivisasa) || (tarehe.equals(hivisasa))) {
                        //Within Age Limit
                        myCalendarLegacy.add(Calendar.YEAR, -75);
                        pd_date_of_birth3.setError(null);
                        pd_date_of_birth3.setText(sdf.format(myCalendarLegacy.getTime()));
                        //L.t(this, hivisasa.toString());
                    }
                } else if (dob_editText.equalsIgnoreCase("pd_date_of_birth2")) {
                    if (tarehe.before(hivisasa)) {
                        //Too old for this policy
                        pd_date_of_birth2.setText(null);
                        pd_date_of_birth2.setError("");
                        L.t(this, "Your parent is too old for this policy");
                    } else if (tarehe.after(hivisasa) || (tarehe.equals(hivisasa))) {
                        //Within Age Limit
                        myCalendarLegacy.add(Calendar.YEAR, -75);
                        pd_date_of_birth2.setError(null);
                        pd_date_of_birth2.setText(sdf.format(myCalendarLegacy.getTime()));
                        //L.t(this, hivisasa.toString());
                    }
                } else if (dob_editText.equalsIgnoreCase("pd_date_of_birth1")) {
                    if (tarehe.before(hivisasa)) {
                        //Too old for this policy
                        pd_date_of_birth1.setText(null);
                        pd_date_of_birth1.setError("");
                        L.t(this, "Your parent is too old for this policy");
                    } else if (tarehe.after(hivisasa) || (tarehe.equals(hivisasa))) {
                        //Within Age Limit
                        myCalendarLegacy.add(Calendar.YEAR, -75);
                        pd_date_of_birth1.setError(null);
                        pd_date_of_birth1.setText(sdf.format(myCalendarLegacy.getTime()));
                        //L.t(this, hivisasa.toString());
                    }
                }
            }
        }

        public void toStepThree() {
            if (packageName.equalsIgnoreCase("individual"))
            {
                add_parents_cardView.setVisibility(View.VISIBLE);
                switch_ParentsCover.setVisibility(View.VISIBLE);
              //  button7.setVisibility(View.VISIBLE);
            }
            else if (packageName.equalsIgnoreCase("family")) {
                //Show Children Info
                spouse_and_kids_info.setVisibility(View.VISIBLE);
                //button7.setVisibility(View.VISIBLE);

                if (button11.getVisibility()==View.VISIBLE)
                {
               //     button10.setVisibility(View.GONE);
                }
                if (button11.getVisibility()==View.GONE)
                {
               //     button10.setVisibility(View.VISIBLE);
                }
                //add_parents_cardView.setVisibility(View.VISIBLE);
                //switch_ParentsCover.setVisibility(View.VISIBLE);

            }
            //hide cardview4-6
            cardview_four.setVisibility(View.GONE);
            cardview_five.setVisibility(View.GONE);
            cardview_six.setVisibility(View.GONE);
        }

        public void showStepTwo() {
            //Show step two
            cardview_four.setVisibility(View.VISIBLE);
            cardview_four.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_left));
            cardview_five.setVisibility(View.VISIBLE);
            cardview_five.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_left2));
            cardview_six.setVisibility(View.VISIBLE);
            cardview_six.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_left3));
        }

        public void kidsCardViewAlgo() {
            if (cardView_kids1.getVisibility()==(View.GONE) &&
                    cardView_kids2.getVisibility()==(View.GONE) &&
                    cardView_kids3.getVisibility()==(View.GONE) &&
                    cardView_kids4.getVisibility()==(View.GONE) &&
                    cardView_kids5.getVisibility()==(View.GONE) &&
                    cardView_kids6.getVisibility()==(View.GONE)
                    )
            {
                cardView_kids1.setVisibility(View.VISIBLE);
            }
            else if (cardView_kids1.getVisibility()==(View.VISIBLE) &&
                    cardView_kids2.getVisibility()==(View.GONE) &&
                    cardView_kids3.getVisibility()==(View.GONE) &&
                    cardView_kids4.getVisibility()==(View.GONE) &&
                    cardView_kids5.getVisibility()==(View.GONE) &&
                    cardView_kids6.getVisibility()==(View.GONE)
                    )
            {
                cardView_kids2.setVisibility(View.VISIBLE);
            }
            else if (cardView_kids1.getVisibility()==(View.VISIBLE) &&
                    cardView_kids2.getVisibility()==(View.VISIBLE) &&
                    cardView_kids3.getVisibility()==(View.GONE) &&
                    cardView_kids4.getVisibility()==(View.GONE) &&
                    cardView_kids5.getVisibility()==(View.GONE) &&
                    cardView_kids6.getVisibility()==(View.GONE)
                    )
            {
                cardView_kids3.setVisibility(View.VISIBLE);
            }
            else if (cardView_kids1.getVisibility()==(View.VISIBLE) &&
                    cardView_kids2.getVisibility()==(View.VISIBLE) &&
                    cardView_kids3.getVisibility()==(View.VISIBLE) &&
                    cardView_kids4.getVisibility()==(View.GONE) &&
                    cardView_kids5.getVisibility()==(View.GONE) &&
                    cardView_kids6.getVisibility()==(View.GONE)
                    )
            {
                cardView_kids4.setVisibility(View.VISIBLE);
            }
            else if (cardView_kids1.getVisibility()==(View.VISIBLE) &&
                    cardView_kids2.getVisibility()==(View.VISIBLE) &&
                    cardView_kids3.getVisibility()==(View.VISIBLE) &&
                    cardView_kids4.getVisibility()==(View.VISIBLE) &&
                    cardView_kids5.getVisibility()==(View.GONE) &&
                    cardView_kids6.getVisibility()==(View.GONE)
                    )
            {
                cardView_kids5.setVisibility(View.VISIBLE);
            }
            else if (cardView_kids1.getVisibility()==(View.VISIBLE) &&
                    cardView_kids2.getVisibility()==(View.VISIBLE) &&
                    cardView_kids3.getVisibility()==(View.VISIBLE) &&
                    cardView_kids4.getVisibility()==(View.VISIBLE) &&
                    cardView_kids5.getVisibility()==(View.VISIBLE) &&
                    cardView_kids6.getVisibility()==(View.GONE)
                    )
            {
                cardView_kids6.setVisibility(View.VISIBLE);
            }

        }

        public void parentsCardViewAlgo() {
            if (cardView_parent1.getVisibility()==(View.GONE) &&
                    cardView_parent2.getVisibility()==(View.GONE) &&
                    cardView_parent3.getVisibility()==(View.GONE) &&
                    cardView_parent4.getVisibility()==(View.GONE)
                    )
            {
                cardView_parent1.setVisibility(View.VISIBLE);
            }
            else if (cardView_parent1.getVisibility()==(View.VISIBLE) &&
                    cardView_parent2.getVisibility()==(View.GONE) &&
                    cardView_parent3.getVisibility()==(View.GONE) &&
                    cardView_parent4.getVisibility()==(View.GONE)
                    )
            {
                cardView_parent2.setVisibility(View.VISIBLE);
            }
            else if (cardView_parent1.getVisibility()==(View.VISIBLE) &&
                    cardView_parent2.getVisibility()==(View.VISIBLE) &&
                    cardView_parent3.getVisibility()==(View.GONE) &&
                    cardView_parent4.getVisibility()==(View.GONE)
                    )
            {
                cardView_parent3.setVisibility(View.VISIBLE);
            }
            else if (cardView_parent1.getVisibility()==(View.VISIBLE) &&
                    cardView_parent2.getVisibility()==(View.VISIBLE) &&
                    cardView_parent3.getVisibility()==(View.VISIBLE) &&
                    cardView_parent4.getVisibility()==(View.GONE)
                    )
            {
                cardView_parent4.setVisibility(View.VISIBLE);
            }
        }

        public void showKidsRecycler() {
            kids_number = kids_number_TextInputEditText.getText().toString();
            kids_number2 =Integer.parseInt(kids_number);

            for (int incr = 0; incr < kids_number2 ; incr++) {
                kidsCardViewAlgo();
            }
        }

        public void showParentsRecycler() {
            parents_number = editText_parents_num.getText().toString();
            parents_number2=Integer.parseInt(parents_number);

            for (int increment = 0; increment < parents_number2 ; increment++) {
                parentsCardViewAlgo();
            }
        }

    public void openDocuments() {
        Intent getImage = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        Intent pickImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
        startActivityForResult(Intent.createChooser(getImage, getString(R.string.select_image)).putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickImage}), GALLERY_REQUEST_ONE);
    }

    public void openCamera() {
        startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_ONE);
    }

public void getDate() {
    // Show datePickerFragment
    new DatePickerDialog(LegacyLifePlan.this, AlertDialog.THEME_HOLO_LIGHT,  legacyDate, myCalendarLegacy
            .get(Calendar.YEAR), myCalendarLegacy.get(Calendar.MONTH),
            myCalendarLegacy.get(Calendar.DAY_OF_MONTH)).show();
}

public void sendDataToServer() {
    childs_name1 = TextInputEditText_name1.getText().toString();
    childs_name2 = TextInputEditText_name2.getText().toString();
    childs_name3 = TextInputEditText_name3.getText().toString();
    childs_name4 = TextInputEditText_name4.getText().toString();
    childs_name5 = TextInputEditText_name5.getText().toString();
    childs_name6 = TextInputEditText_name6.getText().toString();

    kids_dob1 = kids_date_of_birth1.getText().toString();
    kids_dob2 = kids_date_of_birth2.getText().toString();
    kids_dob3 = kids_date_of_birth3.getText().toString();
    kids_dob4 = kids_date_of_birth4.getText().toString();
    kids_dob5 = kids_date_of_birth5.getText().toString();
    kids_dob6 = kids_date_of_birth6.getText().toString();

    kids_gender1 = spinner_kids_gender1.getSelectedItem().toString();
    kids_gender2 = spinner_kids_gender2.getSelectedItem().toString();
    kids_gender3 = spinner_kids_gender3.getSelectedItem().toString();
    kids_gender4 = spinner_kids_gender4.getSelectedItem().toString();
    kids_gender5 = spinner_kids_gender5.getSelectedItem().toString();
    kids_gender6 = spinner_kids_gender6.getSelectedItem().toString();

     parentRelationShip1 = spinner_relationship1.getSelectedItem().toString();
     parentRelationShip2 = spinner_relationship2.getSelectedItem().toString();
    parentRelationShip3 = spinner_relationship3.getSelectedItem().toString();
    parentRelationShip4 = spinner_relationship4.getSelectedItem().toString();

     parentName1 = pd_name_editText1.getText().toString();
     parentName2 = pd_name_editText2.getText().toString();
    parentName3 = pd_name_editText3.getText().toString();
    parentName4 = pd_name_editText4.getText().toString();

     parent_DOB1 = pd_date_of_birth1.getText().toString();
     parent_DOB2 = pd_date_of_birth2.getText().toString();
    parent_DOB3 = pd_date_of_birth3.getText().toString();
    parent_DOB4 = pd_date_of_birth4.getText().toString();

     parentIdNumber1 = pd_id_editText1.getText().toString();
     parentIdNumber2 = pd_id_editText2.getText().toString();
    parentIdNumber3 = pd_id_editText3.getText().toString();
    parentIdNumber4 = pd_id_editText4.getText().toString();

     parentPhoneNumber1 = pd_phone_EditText1.getText().toString();
     parentPhoneNumber2 = pd_phone_EditText2.getText().toString();
    parentPhoneNumber3 = pd_phone_EditText3.getText().toString();
    parentPhoneNumber4 = pd_phone_EditText4.getText().toString();

    String type = "legacy";
    cover_amount = legacyPremium.toString();
    String id_numberMain = MyApplication.readFromPreferences(MyApplication.getAppContext(), Keys.keys.ID_NUMBER, ValidationUtil.getDefault());
    ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
        //we are connected to a network
        BackgroundWorker2 backGroundWorker2 = new BackgroundWorker2(this);
        backGroundWorker2.execute(type, cover_amount, id_numberMain, packageName, packageLevel, paymentDuration);
    }
    else
    {
        L.T(MyApplication.getAppContext(), getString(R.string.no_network_connection));
    }
}

public void showParentsRecycler2() {
    if (switch_ParentsCover.isChecked()==(true))
    {
        layout_parentRecycler.setVisibility(View.VISIBLE);
        cardView_parent1.setVisibility(View.GONE);
        cardView_parent2.setVisibility(View.GONE);
        cardView_parent3.setVisibility(View.GONE);
        cardView_parent4.setVisibility(View.GONE);
        layout_kidsHorizontalScroll.setVisibility(View.GONE);
        showParentsRecycler();
    }
    else
    {
        layout_kidsHorizontalScroll.setVisibility(View.GONE);
        cardview_final.setVisibility(View.VISIBLE);
    }
}

public  void  clickbutton7() {
    if (switchSpouse.isChecked())
    {
        if (validateField(TextInputEditText_Name, 3, getString(R.string.invalid_name))
                &&  validateField(TextInputEditText_IdNumber, 6, getString(R.string.invalid_id_no))
                && validatePhoneField())
        {
            if (spouse_dob.getText().length() > 0)
            {
                //textView81.setVisibility(View.VISIBLE);
                spouse_dob.setError(null);
                cardview_seven.setVisibility(View.VISIBLE);
                cardview_seven.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_right));
                add_parents_cardView.setVisibility(View.GONE);
                // switchKids.setVisibility(View.VISIBLE);
                //    button10.setVisibility(View.VISIBLE);
                // button11.setVisibility(View.GONE);
                //switchSpouse.setClickable(false);

                AlgorithmOne();
            }
            else
            {
                spouse_dob.setError("Cannot be blank!");
            }
        }
    }

    else{
        AlgorithmOne();
        cardview_seven.setVisibility(View.VISIBLE);
        cardview_seven.startAnimation(AnimationUtils.loadAnimation(LegacyLifePlan.this, R.anim.enter_from_right));
        add_parents_cardView.setVisibility(View.GONE);
    }
}

public void someMethod() {
    Map<String, Integer> map = new HashMap<>();
    map.put("parentMonthlySilver", 260);
    map.put("parentQuarterlySilver", 741);
    map.put("parentBiAnnuallySilver", 1482);
    map.put("parentAnnuallySilver", 2810);

    map.put("parentMonthlyGold", 520);
    map.put("parentQuarterlyGold", 1482);
    map.put("parentBiAnnuallyGold", 2964);
    map.put("parentAnnuallyGold", 5620);

    map.put("parentMonthlyPlatinum", 1300);
    map.put("parentQuarterlyPlatinum", 3705);
    map.put("parentBiAnnuallyPlatinum", 7410);
    map.put("parentAnnuallyPlatinum", 14040);

    priceParent="parent"+paymentDuration+parentPackageLevel;
    priceParent2=map.get(priceParent);
    parentTotal = priceParent2 * parents_number2;
    legacyPremium = parentTotal+price2;
}

    public void someMethod2() {

        legacyPremium = price2;
    }

}

