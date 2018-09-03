package ke.co.coverapp.coverapp.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.NotificationActivity;
import ke.co.coverapp.coverapp.adapters.AddAdultsAdapter;
import ke.co.coverapp.coverapp.adapters.AddChildrenAdapter;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.backgroundWorkers.BackgroundWorker2;
import ke.co.coverapp.coverapp.backgroundWorkers.BackgroundWorker3;
import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.fragments.MonthLongVehicleInsurance;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.tasks.TaskLoadBalance;
import ke.co.coverapp.coverapp.utility.ValidationUtil;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.PROF_PIC_URL;

public class IceaTravelCover extends AppCompatActivity implements View.OnClickListener {

    CardView personal_details_cardView, travel_details_cardView, quotation_cardView, next_of_kin_details, check_out_cardView, rest_of_travel_details_cardView, cardView_parents_details, cardView_children_details;
    Button button14, button13, buttonAddNextDetail, buttonNext_to_travelDetails, buttonNext_to_quotation, buttonNext_toNextOf_kin, buttonNext_to_CheckOut, button_continue_travel_details, button10, button12, add_next_child_traveller;
    Spinner spinner_reasonForTravel, spinner_countryOfDeparture, spinner_countryOfTravel, spinner_pricing, spinner_ratePlan;
    DatePickerDialog.OnDateSetListener datePickerAdultsDob, datePickerdepartureDate, datePickerReturningDate, datePicker_kids_dob;
    String stringDepartureDate, stringReturningDate, country_of_travel, reason_for_travel, country_of_departure, departure_date, returning_date, pricing_plan, rate_plan, kids_dob, adults_dob;
    EditText departure_date_EditText, returning_date_editText, AppCompatEditText_adults_dob;
    Calendar departureCalendar = Calendar.getInstance();
    Calendar returningmyCalendar = Calendar.getInstance();
    Calendar kids_dobCalendar = Calendar.getInstance();
    Calendar adultsDob_Calendar = Calendar.getInstance();
    TextInputEditText passportNumber_editText, nextOfKinRelationship_editText, nextOfKinPhone_editText, nextOfKinfullName_editText, childrenNumber_editText, adultsNumber_editText;
    TextInputLayout TextInputLayout_childName, TextInputLayoutChildDOB, passportNumber_TextInputLayout, nextOfKinRelationship_TextInputLayout, nextOfKinPhone_TextInputLayout, nextOfKinfullName_TextInputLayout, childrenNumber_TextInputLayout, adultsNumber_TextInputLayout ;
    TextInputEditText TextInputEdtText_childName, TextInputEditText_childDOB, textInputEditText_Name_adults, textInputEditText_IdNumber_adults, textInputEditText_passportNumber_adults, textInputEditText_email_adults;
    TextInputLayout textInputLayout_Name_adults, textInputLayout_IdNumber_adults, textInputLayout_passportNumber_adults, textInputLayout_email_adults;
    String adults_travellers_number, children_travellers_number, nextOfKin_name, nextOfKin_phoneNumber, nextOfKin_relationship;
    int adults_travellers_number2, children_travellers_number2;
    LinearLayout restOfTravelDetails_linearLayout;
    TextView textViewPriceQuotation, textView95, textView96, textViewShowParentDetails, textViewShowChildrenDetails, textViewShowTravelDetails, textViewShowMoreTravelDetails, textViewNextOfKinDetails, textViewFinalDetails;
    int counter = 0;
    int counter2 = 0;
    int counter3 = 0;
    int counter4 = 0;
    String confirmed_name = "";
    String confirmed_email = "";
    String confirmed_idNumber = "";
    String confirmed_passportNumber = "";
    String confirmed_adultDob="";
    String arrayAdultsEmpty = "yes";
    String arrayChildrenEmpty = "yes";
    String text_phone = MyApplication.readFromPreferences(MyApplication.getAppContext(), Keys.keys.PHONE_NUMBER, ValidationUtil.getDefault());
    String text_fname = MyApplication.readFromPreferences(MyApplication.getAppContext(), Keys.keys.FNAME, ValidationUtil.getDefault());
    String text_lname = MyApplication.readFromPreferences(MyApplication.getAppContext(), Keys.keys.LNAME, ValidationUtil.getDefault());
    String text_sname = MyApplication.readFromPreferences(MyApplication.getAppContext(), Keys.keys.SNAME, ValidationUtil.getDefault());
    String text_email = MyApplication.readFromPreferences(MyApplication.getAppContext(), Keys.keys.EMAIL, ValidationUtil.getDefault());
    String text_id_number = MyApplication.readFromPreferences(MyApplication.getAppContext(), Keys.keys.ID_NUMBER, ValidationUtil.getDefault());
    String text_dob = MyApplication.readFromPreferences(MyApplication.getAppContext(), Keys.keys.DOB, ValidationUtil.getDefault());
    String passportNumber;
    ArrayList <String> adultTravellersName;
    ArrayList <String> adultTravellersIdNumber;
    ArrayList <String> adultTravellersEmail;
    ArrayList <String> adultTravellersPassportNumber;
    ArrayList <String> adultTravellersDOB;
    Switch switch_adults_number, switch_children_number;
    ArrayList <String> childTravellersName;
    ArrayList <String> childTravellersIdDOB;
    String totalCostOfTravelInsurance = "100";
    private ProgressDialog mProgressDialog;
    CheckBox checkBoxIceaTravel;
    String dontGoBack="no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icea_travel_cover);

        setTitle(getResources().getText(R.string.icea_travel_cover));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        spinner_pricing= (Spinner ) findViewById(R.id.spinner_pricing) ;
        spinner_pricing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (spinner_pricing.getSelectedItem().toString().equalsIgnoreCase("")){

                }
                else
                {
                    if (spinner_ratePlan.getSelectedItem().toString().equalsIgnoreCase("")){

                    }
                    else
                    {
                        //Code Here
                       checkSchengen();
                        L.t(IceaTravelCover.this, "2");
                    }
                }
                //  L.t(LegacyLifePlan.this, "Something has been selected on the spinner");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                // L.t(LegacyLifePlan.this, "Nothing has been selected on the spinner");
            }
        });

        spinner_ratePlan= (Spinner ) findViewById(R.id.spinner_ratePlan) ;
        spinner_ratePlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (spinner_ratePlan.getSelectedItem().toString().equalsIgnoreCase("")){

                }
                else
                {
                    if (spinner_pricing.getSelectedItem().toString().equalsIgnoreCase("")){

                    }
                    else
                    {
                        //Code Here
                        checkSchengen();
                        L.t(IceaTravelCover.this, "1");
                    }
                }
                //  L.t(LegacyLifePlan.this, "Something has been selected on the spinner");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                // L.t(LegacyLifePlan.this, "Nothing has been selected on the spinner");
            }
        });

        AppCompatEditText_adults_dob=(EditText )findViewById(R.id.AppCompatEditText_adults_dob);
        AppCompatEditText_adults_dob.setOnClickListener(this);
        textViewPriceQuotation=(TextView )findViewById(R.id.textViewPriceQuotation);
        button14 = (Button )findViewById(R.id.button14);
        button14.setOnClickListener(this);
        checkBoxIceaTravel = (CheckBox )findViewById(R.id.checkBoxIceaTravel);
        mProgressDialog = new ProgressDialog(IceaTravelCover.this);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage("Processing request...");
        textViewShowTravelDetails = (TextView ) findViewById(R.id.textViewShowTravelDetails);
        textViewShowMoreTravelDetails = (TextView ) findViewById(R.id.textViewShowMoreTravelDetails);
        textViewNextOfKinDetails = (TextView ) findViewById(R.id.textViewNextOfKinDetails);
        textViewFinalDetails = (TextView ) findViewById(R.id.textViewFinalDetails);
        add_next_child_traveller = (Button )findViewById(R.id.add_next_child_traveller);
        add_next_child_traveller.setOnClickListener(this);
        switch_adults_number = (Switch)findViewById(R.id.switch_adults_number);
        switch_children_number = (Switch)findViewById(R.id.switch_children_number);

        switch_adults_number.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    adultsNumber_TextInputLayout.setVisibility(View.VISIBLE);
                }
                else {
                    adultsNumber_TextInputLayout.setVisibility(View.GONE);
                }
            }
        });

        switch_children_number.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    childrenNumber_TextInputLayout.setVisibility(View.VISIBLE);
                }
                else {
                    childrenNumber_TextInputLayout.setVisibility(View.GONE);
                }
            }
        });

        textViewShowParentDetails=(TextView )findViewById(R.id.textViewShowParentDetails);
        textViewShowChildrenDetails=(TextView )findViewById(R.id.textViewShowChildrenDetails);

        adultTravellersName = new ArrayList<>();
        adultTravellersIdNumber = new ArrayList<>();
        adultTravellersEmail = new ArrayList<>();
        adultTravellersPassportNumber = new ArrayList<>();
        adultTravellersDOB = new ArrayList<>();

        childTravellersName = new ArrayList<>();
        childTravellersIdDOB = new ArrayList<>();

        restOfTravelDetails_linearLayout=(LinearLayout )findViewById(R.id.restOfTravelDetails_linearLayout);
        //addChildren_linearLayout=(LinearLayout )findViewById(R.id.addChildren_linearLayout);
        //children_number_linearLayout=(LinearLayout )findViewById(R.id.children_number_linearLayout);

        buttonAddNextDetail=(Button)findViewById(R.id.buttonAddNextDetail);
        buttonAddNextDetail.setOnClickListener(this);

        textView95=(TextView )findViewById(R.id.textView95);
        textView96=(TextView )findViewById(R.id.textView96);

        textInputLayout_Name_adults = (TextInputLayout )findViewById(R.id.textInputLayout_Name_adults) ;
        textInputLayout_IdNumber_adults = (TextInputLayout )findViewById(R.id.textInputLayout_IdNumber_adults) ;
        textInputLayout_passportNumber_adults = (TextInputLayout )findViewById(R.id.textInputLayout_passportNumber_adults) ;
        textInputLayout_email_adults = (TextInputLayout )findViewById(R.id.textInputLayout_email_adults) ;
        TextInputLayout_childName = (TextInputLayout )findViewById(R.id.TextInputLayout_childName) ;
        TextInputLayoutChildDOB = (TextInputLayout )findViewById(R.id.TextInputLayoutChildDOB) ;

        textInputEditText_Name_adults = (TextInputEditText )findViewById(R.id.textInputEditText_Name_adults) ;
        textInputEditText_IdNumber_adults = (TextInputEditText )findViewById(R.id.textInputEditText_IdNumber_adults) ;
        textInputEditText_passportNumber_adults = (TextInputEditText )findViewById(R.id.textInputEditText_passportNumber_adults) ;
        textInputEditText_email_adults = (TextInputEditText )findViewById(R.id.textInputEditText_email_adults) ;
        TextInputEdtText_childName = (TextInputEditText )findViewById(R.id.TextInputEdtText_childName) ;
        TextInputEditText_childDOB = (TextInputEditText )findViewById(R.id.TextInputEditText_childDOB) ;

        departure_date_EditText = (EditText ) findViewById(R.id.departure_date_EditText) ;
        returning_date_editText = (EditText ) findViewById(R.id.returning_date_editText) ;
        departure_date_EditText.setOnClickListener(this);
        returning_date_editText.setOnClickListener(this);

        passportNumber_editText = (TextInputEditText ) findViewById(R.id.passportNumber_editText) ;
        nextOfKinRelationship_editText = (TextInputEditText ) findViewById(R.id.nextOfKinRelationship_editText) ;
        nextOfKinPhone_editText = (TextInputEditText ) findViewById(R.id.nextOfKinPhone_editText) ;
        nextOfKinfullName_editText = (TextInputEditText ) findViewById(R.id.nextOfKinfullName_editText) ;
        childrenNumber_editText = (TextInputEditText ) findViewById(R.id.childrenNumber_editText) ;
        adultsNumber_editText = (TextInputEditText ) findViewById(R.id.adultsNumber_editText) ;

        passportNumber_TextInputLayout = (TextInputLayout ) findViewById(R.id.passportNumber_TextInputLayout) ;
        nextOfKinRelationship_TextInputLayout = (TextInputLayout ) findViewById(R.id.nextOfKinRelationship_TextInputLayout) ;
        nextOfKinPhone_TextInputLayout = (TextInputLayout ) findViewById(R.id.nextOfKinPhone_TextInputLayout) ;
        nextOfKinfullName_TextInputLayout = (TextInputLayout ) findViewById(R.id.nextOfKinfullName_TextInputLayout) ;
        childrenNumber_TextInputLayout = (TextInputLayout ) findViewById(R.id.childrenNumber_TextInputLayout) ;
        adultsNumber_TextInputLayout = (TextInputLayout ) findViewById(R.id.adultsNumber_TextInputLayout) ;

        personal_details_cardView = (CardView )findViewById(R.id.personal_details_cardView) ;
        travel_details_cardView = (CardView )findViewById(R.id.travel_details_cardView) ;
        quotation_cardView = (CardView )findViewById(R.id.quotation_cardView) ;
        next_of_kin_details = (CardView )findViewById(R.id.next_of_kin_details) ;
        check_out_cardView = (CardView )findViewById(R.id.check_out_cardView) ;
        rest_of_travel_details_cardView= (CardView )findViewById(R.id.rest_of_travel_details_cardView) ;
        cardView_parents_details= (CardView )findViewById(R.id.cardView_parents_details) ;
        cardView_children_details= (CardView )findViewById(R.id.cardView_children_details) ;

        buttonNext_to_travelDetails= (Button ) findViewById(R.id.buttonNext_to_travelDetails) ;
        buttonNext_to_quotation= (Button ) findViewById(R.id.buttonNext_to_quotation) ;
        buttonNext_toNextOf_kin= (Button ) findViewById(R.id.buttonNext_toNextOf_kin) ;
        buttonNext_to_CheckOut= (Button ) findViewById(R.id.buttonNext_to_CheckOut) ;
        button10= (Button ) findViewById(R.id.button10) ;
        button12= (Button ) findViewById(R.id.button12) ;
        button13= (Button ) findViewById(R.id.button13) ;

        button_continue_travel_details= (Button ) findViewById(R.id.button_continue_travel_details) ;

        spinner_reasonForTravel= (Spinner ) findViewById(R.id.spinner_reasonForTravel) ;
        spinner_countryOfDeparture= (Spinner ) findViewById(R.id.spinner_countryOfDeparture) ;
        spinner_countryOfDeparture.setSelection(93);
        spinner_countryOfTravel= (Spinner ) findViewById(R.id.spinner_countryOfTravel) ;

        buttonNext_to_travelDetails.setOnClickListener(this);
        buttonNext_to_quotation.setOnClickListener(this);
        buttonNext_toNextOf_kin.setOnClickListener(this);
        buttonNext_to_CheckOut.setOnClickListener(this);
        button_continue_travel_details.setOnClickListener(this);
        button10.setOnClickListener(this);
        button12.setOnClickListener(this);
        button13.setOnClickListener(this);
        TextInputEditText_childDOB.setOnClickListener(this);

        datePickerdepartureDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                departureCalendar.set(Calendar.YEAR, year);
                departureCalendar.set(Calendar.MONTH, monthOfYear);
                departureCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDeparture();
            }
        };

        departure_date_EditText.setOnClickListener(this);

        datePickerReturningDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                returningmyCalendar.set(Calendar.YEAR, year);
                returningmyCalendar.set(Calendar.MONTH, monthOfYear);
                returningmyCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelReturning();
            }
        };

        returning_date_editText.setOnClickListener(this);

        datePicker_kids_dob = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                kids_dobCalendar.set(Calendar.YEAR, year);
                kids_dobCalendar.set(Calendar.MONTH, monthOfYear);
                kids_dobCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelKidsDob();

            }
        };
        returning_date_editText.setOnClickListener(this);

        datePickerAdultsDob = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                adultsDob_Calendar.set(Calendar.YEAR, year);
                adultsDob_Calendar.set(Calendar.MONTH, monthOfYear);
                adultsDob_Calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelAdultsDob();
            }
        };

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public  void onBackPressed() {

        //one////////////////////////
        if (personal_details_cardView.getVisibility() == View.VISIBLE)
        {
            finish();
        }

        //two/////////////////////
        else   if (travel_details_cardView.getVisibility() == View.VISIBLE)
        {
            //hide
            travel_details_cardView.setVisibility(View.GONE);

            //show
            personal_details_cardView.setVisibility(View.VISIBLE);

        }

        //three/////////////////////
        else if (rest_of_travel_details_cardView.getVisibility()==View.VISIBLE)
        {
            //hide
            rest_of_travel_details_cardView.setVisibility(View.GONE);
            //show
            travel_details_cardView.setVisibility(View.VISIBLE);
        }

        //four/////////////////////////
        else   if (quotation_cardView.getVisibility() == View.VISIBLE)
        {
            //hide
            quotation_cardView.setVisibility(View.GONE);

            //show
            rest_of_travel_details_cardView.setVisibility(View.VISIBLE);

        }

        //five///////////////////////////
        else   if (next_of_kin_details.getVisibility() == View.VISIBLE)
        {
            //hide
            next_of_kin_details.setVisibility(View.GONE);

            //show
            quotation_cardView.setVisibility(View.VISIBLE);

        }

        //six///////////////////////////
        else   if (cardView_parents_details.getVisibility() == View.VISIBLE)
        {
         if (arrayAdultsEmpty.equalsIgnoreCase("yes") && switch_adults_number.isChecked()==(true))
         {
             AlertDialog.Builder builder;
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                 builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
             } else {
                 builder = new AlertDialog.Builder(this);
             }
             builder.setTitle("Delete entry")
                     .setMessage("Are you sure you want to go back and loose all the adult travellers details?")
                     .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                             // continue with delete
                             backAwayAdultsDetails();
                             adultTravellersName.clear();
                             adultTravellersIdNumber.clear();
                             adultTravellersEmail.clear();
                             adultTravellersPassportNumber.clear();
                             counter=0;
                             counter3=0;
                         }
                     })
                     .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                             // do nothing
                         }
                     })
                     .setIcon(android.R.drawable.ic_dialog_alert)
                     .show();
         }
         else
             {
                 backAwayAdultsDetails();
             }

        }

        //seven///////////////////////
        else   if (cardView_children_details.getVisibility() == View.VISIBLE)
        {
               if (arrayChildrenEmpty.equalsIgnoreCase("yes") && switch_children_number.isChecked()==(true))
               {
                   AlertDialog.Builder builder;
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                       builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                   } else {
                       builder = new AlertDialog.Builder(this);
                   }
                   builder.setTitle("Go Back?")
                           .setMessage("Are you sure you want to go back and loose all the children travellers details?")
                           .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   backAwayChildrenDetails();
                                   childTravellersName.clear();
                                   childTravellersIdDOB.clear();
                                   counter2=0;
                                   counter4=0;
                               }
                           })
                           .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   // do nothing
                               }
                           })
                           .setIcon(android.R.drawable.ic_dialog_alert)
                           .show();
               }
               else
                   {
                       backAwayChildrenDetails();
                   }
        }

        //eight//////////////////////
        else   if (check_out_cardView.getVisibility() == View.VISIBLE)
        {
            if (dontGoBack.equalsIgnoreCase("no"))
            {
                //hide
                check_out_cardView.setVisibility(View.GONE);

                //show
                if (switch_children_number.isChecked()==(false))
                {
                    if (switch_adults_number.isChecked()==(true))
                    {
                        cardView_parents_details.setVisibility(View.VISIBLE);
                        textView95.setText("Confirm your Details ");
                        textInputEditText_Name_adults. setText(confirmed_name);
                        textInputEditText_IdNumber_adults.setText(confirmed_idNumber);
                        textInputEditText_passportNumber_adults.setText(confirmed_passportNumber);
                        textInputEditText_email_adults.setText(confirmed_email);
                        AppCompatEditText_adults_dob.setText(confirmed_adultDob);

                        button12.setVisibility(View.GONE);
                        buttonAddNextDetail.setVisibility(View.VISIBLE);
                        //Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
                    }
                    else if (switch_adults_number.isChecked()==(false))
                    {
                        cardView_parents_details.setVisibility(View.VISIBLE);
                        textView95.setText("Confirm your Details ");
                        textInputEditText_Name_adults. setText(confirmed_name);
                        textInputEditText_IdNumber_adults.setText(confirmed_idNumber);
                        textInputEditText_passportNumber_adults.setText(confirmed_passportNumber);
                        textInputEditText_email_adults.setText(confirmed_email);
                        AppCompatEditText_adults_dob.setText(confirmed_adultDob);
                        // Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getApplicationContext(),"3",Toast.LENGTH_SHORT).show();
                }
                else if (switch_children_number.isChecked()==(true))
                {
                    cardView_children_details.setVisibility(View.VISIBLE);
                }
            }
            else
                {
                    finish();
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.departure_date_EditText:

                // Show datePickerFragment
                new DatePickerDialog(IceaTravelCover.this, android.app.AlertDialog.THEME_HOLO_LIGHT,  datePickerdepartureDate, departureCalendar
                        .get(Calendar.YEAR), departureCalendar.get(Calendar.MONTH),
                        departureCalendar.get(Calendar.DAY_OF_MONTH)).show();
             //   button_continue_travel_details.setVisibility(View.GONE);
                break;

            case R.id.returning_date_editText:

                // Show datePickerFragment
                new DatePickerDialog(IceaTravelCover.this, android.app.AlertDialog.THEME_HOLO_LIGHT,  datePickerReturningDate, returningmyCalendar
                        .get(Calendar.YEAR), returningmyCalendar.get(Calendar.MONTH),
                        returningmyCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.buttonNext_to_travelDetails:
                if (validateField(passportNumber_TextInputLayout, passportNumber_editText, 6, getString(R.string.invalid_passport_number))) {
                travel_details_cardView.setVisibility(View.VISIBLE);
                personal_details_cardView.setVisibility(View.GONE);
            }
                break;

            case R.id.button_continue_travel_details:

                if (switch_adults_number.isChecked()==(true))
                {
                    if (validateField(adultsNumber_TextInputLayout, adultsNumber_editText, 1, "Adults Number cannot be empty!"))
                    {
                        getAdultsNumber();
                        if ( adults_travellers_number2<1)
                        {
                            adultsNumber_TextInputLayout.setError("Not a valid Number, enter one or more!");
                        }
                        else if (adults_travellers_number2>=1)
                            {
                                adultsNumber_TextInputLayout.setError(null);
                            }
                    }
                }
                if (switch_children_number.isChecked()==(true))
                {
                    if (validateField(childrenNumber_TextInputLayout, childrenNumber_editText, 1, "Children's Number cannot be empty!"))
                    {
                        childsNumber();
                        if ( children_travellers_number2<1)
                        {
                            childrenNumber_TextInputLayout.setError("Not a valid Number, enter one or more!");
                        }
                        else if (children_travellers_number2>=1)
                        {
                            childrenNumber_TextInputLayout.setError(null);
                        }
                    }
                }
                 if ( adultsNumber_TextInputLayout.getError() == null
                        && childrenNumber_TextInputLayout.getError() == null)
                {
                   // L.t(this, "The program can read if there is an error on the TextInputLayout1");

                    //hide
                    travel_details_cardView.setVisibility(View.GONE);

                    //show
                    rest_of_travel_details_cardView.setVisibility(View.VISIBLE);
                }

                 if(switch_adults_number.isChecked()==(false) && switch_children_number.isChecked()==(false))
                {
                  //  Toast.makeText(getApplicationContext(),"The program can read if there is an error on the TextInputLayout2",Toast.LENGTH_SHORT).show();
                    //hide
                    travel_details_cardView.setVisibility(View.GONE);

                    //show
                    rest_of_travel_details_cardView.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.buttonNext_to_quotation:

                country_of_travel = spinner_countryOfTravel.getSelectedItem().toString();
                reason_for_travel = spinner_reasonForTravel.getSelectedItem().toString();
                country_of_departure = spinner_countryOfDeparture.getSelectedItem().toString();
                departure_date = departure_date_EditText.getText().toString();
                returning_date = returning_date_editText.getText().toString();

                String stringTextTravelDetails = ("Country of Travel: "+country_of_travel+"<br> Country of Departure: "+country_of_departure+"<br> Reason for travel: "+reason_for_travel+"<br> Departure Date: "+departure_date+"<br> Return Date: "+returning_date);
                textViewShowTravelDetails.setText(Html.fromHtml(stringTextTravelDetails));

                if (country_of_travel.equalsIgnoreCase(""))
                {
                    TextView errorText = (TextView)spinner_countryOfTravel.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Nothing has been selected!");//changes the selected item text to this
                }
                else if (reason_for_travel.equalsIgnoreCase(""))
                {
                    TextView errorText = (TextView)spinner_reasonForTravel.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Nothing has been selected!");//changes the selected item text to this
                }
                else if (country_of_departure.equalsIgnoreCase(""))
                {
                    TextView errorText = (TextView)spinner_countryOfDeparture.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Nothing has been selected!");//changes the selected item text to this
                }
                else if (departure_date.equalsIgnoreCase(""))
                {
                    departure_date_EditText.setError("Cannot be blank!");
                }
                else if (returning_date.equalsIgnoreCase(""))
                {
                    returning_date_editText.setError("Cannot be blank!");
                }
                else
                    {
                        departure_date_EditText.setError(null);
                        returning_date_editText.setError(null);

                        //hide
                        rest_of_travel_details_cardView.setVisibility(View.GONE);

                        //show
                        quotation_cardView.setVisibility(View.VISIBLE);
                    }
                break;

            case R.id.buttonNext_toNextOf_kin:

                pricing_plan = spinner_pricing.getSelectedItem().toString();
                rate_plan = spinner_ratePlan.getSelectedItem().toString();

                String stringTextMoreTravelDetails = ("Pricing plan: "+pricing_plan+"<br> Rate Plan: "+rate_plan);
                textViewShowMoreTravelDetails.setText(Html.fromHtml(stringTextMoreTravelDetails));

                if (pricing_plan.equalsIgnoreCase(""))
                {
                    TextView errorText = (TextView)spinner_pricing.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Nothing has been selected!");//changes the selected item text to this
                }
                else   if (rate_plan.equalsIgnoreCase(""))
                {
                    TextView errorText = (TextView)spinner_ratePlan.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Nothing has been selected!");//changes the selected item text to this
                }
                else
                    {
                        //hide
                        quotation_cardView.setVisibility(View.GONE);

                        //show
                        next_of_kin_details.setVisibility(View.VISIBLE);
                    }

                break;

            case R.id.buttonNext_to_CheckOut:

                if (arrayChildrenEmpty.equalsIgnoreCase("yes"))
                {
                    if (validateField(TextInputLayout_childName, TextInputEdtText_childName, 1, "Invalid Name, cannot be empty!")
                            && validateField(TextInputLayoutChildDOB, TextInputEditText_childDOB, 1, "Cannot be empty"))
                    {
                        childTravellersName.add(TextInputEdtText_childName.getText().toString());
                        childTravellersIdDOB.add(TextInputEditText_childDOB.getText().toString());

                        //hide
                        cardView_children_details.setVisibility(View.GONE);

                        //show
                        check_out_cardView.setVisibility(View.VISIBLE);

                        String stringText3 = ("Children Info:    "+childTravellersName+" "+childTravellersIdDOB);
                        textViewShowChildrenDetails.setText(Html.fromHtml(stringText3));
                        //String text4 = childTravellersName+" "+childTravellersIdDOB;
                        //textViewShowChildrenDetails.setText(text4);

                        arrayChildrenEmpty="no";

                    }
                }
                else
                    {
                        if (validateField(TextInputLayout_childName, TextInputEdtText_childName, 1, "Invalid Name, cannot be empty!")
                                && validateField(TextInputLayoutChildDOB, TextInputEditText_childDOB, 1, "Cannot be empty"))
                        {

                            //hide
                            cardView_children_details.setVisibility(View.GONE);

                            //show
                            check_out_cardView.setVisibility(View.VISIBLE);

                            String stringText3 = ("Children Info:    "+childTravellersName+" "+childTravellersIdDOB);
                            textViewShowChildrenDetails.setText(Html.fromHtml(stringText3));
                            //String text4 = childTravellersName+" "+childTravellersIdDOB;
                            //textViewShowChildrenDetails.setText(text4);

                        }
                    }

                break;


            case R.id.button10:

                if (switch_adults_number.isChecked()==(true))
                {
                   if (validateField(nextOfKinfullName_TextInputLayout, nextOfKinfullName_editText, 1, "Invalid Name!")
                           && validatePhoneField()
                           && validateField(nextOfKinRelationship_TextInputLayout, nextOfKinRelationship_editText, 1, "cannot be Empty!")
                           )
                   {
                       nextOfKinfullName_TextInputLayout.setError(null);
                       nextOfKinPhone_TextInputLayout.setError(null);
                       nextOfKinRelationship_TextInputLayout.setError(null);

                       //show
                       buttonAddNextDetail.setVisibility(View.VISIBLE);
                       cardView_parents_details.setVisibility(View.VISIBLE);
                       //hide
                       next_of_kin_details.setVisibility(View.GONE);
                       button12.setVisibility(View.GONE);

                       getAdultsNumber();
                       passportNumber=passportNumber_editText.getText().toString();
                       textView95.setText("Confirm your Details ");

                       loadConfirmDetails();
                   }
                }
                else if (switch_adults_number.isChecked()==(false))
                {
                    //show
                    cardView_parents_details.setVisibility(View.VISIBLE);

                    //hide
                    next_of_kin_details.setVisibility(View.GONE);

                    passportNumber=passportNumber_editText.getText().toString();

                    textView95.setText("Confirm your Details ");
                    loadConfirmDetails();

                    button12.setVisibility(View.VISIBLE);
                    buttonAddNextDetail.setVisibility(View.GONE);
                }
                getNextOfKinDetails();
                String stringTextNextofKinDetails = ("Next of Kin full names: "+nextOfKin_name+" <br> Next of Kin Phone Number: "+nextOfKin_phoneNumber+" <br> Next of Kin Relationship: "+nextOfKin_relationship);
                textViewNextOfKinDetails.setText(Html.fromHtml(stringTextNextofKinDetails));

                break;

            case R.id.buttonAddNextDetail:

             if (arrayAdultsEmpty.equalsIgnoreCase("yes"))
             {
                 if (counter==0)
                 {
                     if (validateField(textInputLayout_Name_adults, textInputEditText_Name_adults, 1, "Cannot be empty!")
                             && validateField(textInputLayout_IdNumber_adults, textInputEditText_IdNumber_adults, 6, "Invalid Id Number")
                             && validateField(textInputLayout_passportNumber_adults, textInputEditText_passportNumber_adults, 6, "Invalid Passport Number!")
                             && validateEmailField()){

                         removeError();

                         confirmed_name = textInputEditText_Name_adults.getText().toString();
                         confirmed_idNumber = textInputEditText_IdNumber_adults.getText().toString();
                         confirmed_email = textInputEditText_email_adults.getText().toString();
                         confirmed_passportNumber = textInputEditText_passportNumber_adults.getText().toString();
                         confirmed_adultDob = AppCompatEditText_adults_dob.getText().toString();
                         String stringTextFinalDetails = ("Total Cost: "+totalCostOfTravelInsurance+" <br> Applicant phone number: "+text_phone+" <br> Applicant Full Names: "+confirmed_name+"<br> Applicant Passport Number: "+confirmed_passportNumber+"<br> Applicant Id Number: "+confirmed_idNumber+"<br> Applicant Email Address"+confirmed_email);
                         textViewFinalDetails.setText(Html.fromHtml(stringTextFinalDetails));
                         textInputEditText_Name_adults. setText("");
                         textInputEditText_IdNumber_adults.setText("");
                         textInputEditText_email_adults.setText("");
                         textInputEditText_passportNumber_adults.setText("");
                         AppCompatEditText_adults_dob.setText("");
                         textView95.setText("");
                         textView95.setText("Details for adult No. "+(counter+1)+" travelling with you: ");

                         textInputEditText_Name_adults.requestFocus();
                         counter=counter+1;

                         if (counter==adults_travellers_number2)
                         {
                             button12.setVisibility(View.VISIBLE);
                             buttonAddNextDetail.setVisibility(View.GONE);
                         }
                     }
                 }

                 if (counter>=1 && counter<=adults_travellers_number2)
                 {
                     if (validateField(textInputLayout_Name_adults, textInputEditText_Name_adults, 1, "Cannot be empty!")
                             && validateField(textInputLayout_IdNumber_adults, textInputEditText_IdNumber_adults, 6, "Invalid Id Number")
                             && validateField(textInputLayout_passportNumber_adults, textInputEditText_passportNumber_adults, 6, "Invalid Passport Number!")
                             && validateEmailField()){

                         removeError();
                         addToAdultsArray();

                         textView95.setText("");
                         textView95.setText("Details for adult No. "+(counter+1)+" travelling with you: ");
                         textInputEditText_Name_adults. setText("");
                         textInputEditText_IdNumber_adults.setText("");
                         textInputEditText_email_adults.setText("");
                         textInputEditText_passportNumber_adults.setText("");
                         AppCompatEditText_adults_dob.setText("");

                         textInputEditText_Name_adults.requestFocus();
                         counter=counter+1;

                         if (counter==adults_travellers_number2)
                         {
                             button12.setVisibility(View.VISIBLE);
                             buttonAddNextDetail.setVisibility(View.GONE);
                         }
                     }
                 }

             }
             else
                 {

                     textView95.setText("");
                     textView95.setText("Details for adult No. "+(counter3+1)+" travelling with you: ");
                     textInputEditText_Name_adults.setText(adultTravellersName.get(counter3));
                     textInputEditText_IdNumber_adults.setText(adultTravellersIdNumber.get(counter3));
                     textInputEditText_email_adults.setText(adultTravellersEmail.get(counter3));
                     AppCompatEditText_adults_dob.setText(adultTravellersEmail.get(counter3));
                     textInputEditText_passportNumber_adults.setText(adultTravellersPassportNumber.get(counter3));

                     counter3 = counter3+1;

                     if (counter3==adults_travellers_number2)
                     {
                         button12.setVisibility(View.VISIBLE);
                         buttonAddNextDetail.setVisibility(View.GONE);
                     }
                 }
                break;

            case R.id.button12:

                if (arrayAdultsEmpty.equalsIgnoreCase("yes"))
                {
                    if (switch_adults_number.isChecked()==(false))
                    {
                        if (validateField(textInputLayout_Name_adults, textInputEditText_Name_adults, 1, "Cannot be empty!")
                                && validateField(textInputLayout_IdNumber_adults, textInputEditText_IdNumber_adults, 6, "Invalid Id Number")
                                && validateField(textInputLayout_passportNumber_adults, textInputEditText_passportNumber_adults, 6, "Invalid Passport Number!")
                                && validateEmailField())
                        {
                            confirmed_name =  textInputEditText_Name_adults.getText().toString();
                            confirmed_idNumber=  textInputEditText_IdNumber_adults.getText().toString();
                            confirmed_email=  textInputEditText_email_adults.getText().toString();
                            confirmed_adultDob = AppCompatEditText_adults_dob.getText().toString();
                            confirmed_passportNumber=  textInputEditText_passportNumber_adults.getText().toString();
                            String stringTextFinalDetails = ("Total Cost: "+totalCostOfTravelInsurance+" <br> Applicant phone number: "+text_phone+" <br> Applicant Full Names: "+confirmed_name+"<br> Applicant Passport Number: "+confirmed_passportNumber+"<br> Applicant Id Number: "+confirmed_idNumber+"<br> Applicant Email Address"+confirmed_email+"<br> Applicant Date of Birth"+confirmed_adultDob);
                            textViewFinalDetails.setText(Html.fromHtml(stringTextFinalDetails));
                            //Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
                            showChildrensDetails();
                        }
                    }
                    else if (switch_adults_number.isChecked()==(true))
                    {
                        if (validateField(textInputLayout_Name_adults, textInputEditText_Name_adults, 1, "Cannot be empty!")
                                && validateField(textInputLayout_IdNumber_adults, textInputEditText_IdNumber_adults, 6, "Invalid Id Number")
                                && validateField(textInputLayout_passportNumber_adults, textInputEditText_passportNumber_adults, 6, "Invalid Passport Number!")
                                && validateEmailField()){

                            removeError();
                            addToAdultsArray();

                            //Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_SHORT).show();
                            arrayAdultsEmpty="no";
                            showChildrensDetails();
                        }
                    }
                }

                else
                    {
                      if (arrayChildrenEmpty.equalsIgnoreCase("yes"))
                      {
                          showChildrensDetails();
                      }
                      else
                          {
                              TextInputEdtText_childName.setText(childTravellersName.get(counter4));
                              TextInputEditText_childDOB.setText(childTravellersIdDOB.get(counter4));
                              textView96.setText("");
                              textView96.setText("Details for child No. "+(counter4+1)+" travelling with you: ");

                              counter4 = counter4+1;

                              if (counter4==children_travellers_number2)
                              {
                                  add_next_child_traveller.setVisibility(View.GONE);
                                  buttonNext_to_CheckOut.setVisibility(View.VISIBLE);
                              }
                          }
                    }

                break;

            case R.id.add_next_child_traveller:

                if (arrayChildrenEmpty.equalsIgnoreCase("yes"))
                {
                    if (children_travellers_number2==1)
                    {
                        add_next_child_traveller.setVisibility(View.GONE);
                        buttonNext_to_CheckOut.setVisibility(View.VISIBLE);
                    }
                    if (counter2>=0 && counter2<=children_travellers_number2)
                    {
                        if (validateField(TextInputLayout_childName, TextInputEdtText_childName, 1, "Invalid Name, cannot be empty!")
                                && validateField(TextInputLayoutChildDOB, TextInputEditText_childDOB, 1, "Cannot be empty"))
                        {
                            childTravellersName.add(TextInputEdtText_childName.getText().toString());
                            childTravellersIdDOB.add(TextInputEditText_childDOB.getText().toString());

                            textView96.setText("");
                            textView96.setText("Details for child No. "+(counter2+1)+"  travelling with you: ");
                            TextInputEdtText_childName. setText("");
                            TextInputEditText_childDOB.setText("");

                            TextInputEdtText_childName.requestFocus();
                            counter2=counter2+1;

                            if (counter2==children_travellers_number2)
                            {
                                add_next_child_traveller.setVisibility(View.GONE);
                                buttonNext_to_CheckOut.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                else
                    {
                        TextInputEdtText_childName.setText(childTravellersName.get(counter4));
                        TextInputEditText_childDOB.setText(childTravellersIdDOB.get(counter4));
                        textView96.setText("");
                        textView96.setText("Details for child No. "+(counter4+1)+" travelling with you: ");

                        counter4 = counter4+1;

                        if (counter4==children_travellers_number2)
                        {
                            add_next_child_traveller.setVisibility(View.GONE);
                            buttonNext_to_CheckOut.setVisibility(View.VISIBLE);
                        }
                    }

                break;

            case R.id.TextInputEditText_childDOB:
              new DatePickerDialog(IceaTravelCover.this, android.app.AlertDialog.THEME_HOLO_LIGHT,  datePicker_kids_dob, kids_dobCalendar
                        .get(Calendar.YEAR), kids_dobCalendar.get(Calendar.MONTH),
                        kids_dobCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.button13:

                dontGoBack="yes";

                if (checkBoxIceaTravel.isChecked()==(true))
                {
                    button13.setVisibility(View.GONE);
                    button14.setVisibility(View.VISIBLE);
                    checkBoxIceaTravel.setError(null);
                    mProgressDialog.show();

                    Runnable progressRunnable = new Runnable() {

                        @Override
                        public void run() {
                            mProgressDialog.cancel();
                        }
                    };

                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable, 5000);

                    String type = "iceatravel";
                    //cover_amount = legacyPremium.toString();
                    //String id_numberMain = MyApplication.readFromPreferences(MyApplication.getAppContext(), Keys.keys.ID_NUMBER, ValidationUtil.getDefault());
                    ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        //we are connected to a network
                        BackgroundWorker3 backGroundWorker3 = new BackgroundWorker3(this);
                        backGroundWorker3.execute(type, totalCostOfTravelInsurance, text_phone);
                    }
                    else
                    {
                        L.T(MyApplication.getAppContext(), getString(R.string.no_network_connection));
                    }
                }
                else
                    {
                        checkBoxIceaTravel.setError("You have to agree to the Terms & Conditions");
                    }
                break;

            case R.id.button14:
                finish();
                break;

            case R.id.AppCompatEditText_adults_dob:
                // Show datePickerFragment
                new DatePickerDialog(IceaTravelCover.this, android.app.AlertDialog.THEME_HOLO_LIGHT,  datePickerAdultsDob, adultsDob_Calendar
                        .get(Calendar.YEAR), returningmyCalendar.get(Calendar.MONTH),
                        adultsDob_Calendar.get(Calendar.DAY_OF_MONTH)).show();

                break;


        }
    }

    private void removeError() {
        textInputLayout_Name_adults.setError(null);
        textInputLayout_IdNumber_adults.setError(null);
        textInputLayout_passportNumber_adults.setError(null);
        textInputLayout_email_adults.setError(null);
    }

    private void updateLabelAdultsDob() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String yearFormat = "yyyy";
        SimpleDateFormat sdfYear = new SimpleDateFormat(yearFormat, Locale.US);
        adults_dob = sdfYear.format(adultsDob_Calendar.getTime());


        Date tarehe = adultsDob_Calendar.getTime();
        Date hivisasa = new Date();

        if (tarehe.before(hivisasa))
        {
            AppCompatEditText_adults_dob.setError(null);
            AppCompatEditText_adults_dob.setText(sdf.format(adultsDob_Calendar.getTime()));
        }        else
        {
            AppCompatEditText_adults_dob.setError("Error");
            L.t(this, "Please provide a date that isn't in the future");
        }
    }

    private void updateLabelDeparture() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String yearFormat = "yyyy";
        SimpleDateFormat sdfYear = new SimpleDateFormat(yearFormat, Locale.US);
        stringDepartureDate = sdfYear.format(departureCalendar.getTime());

        Date tareheOfDeparture = departureCalendar.getTime();
        Date hivisasa = new Date();

        if (tareheOfDeparture.before(hivisasa))
        {
            departure_date_EditText.setError("Error");
            L.t(this, "Please provide a date that isn't in the past");
        }
        else
        {
            if(returning_date_editText.getText().toString().equalsIgnoreCase(""))
            {
                departure_date_EditText.setError(null);
                departure_date_EditText.setText(sdf.format(departureCalendar.getTime()));
            }
            else
                {
                    Date tareheOfReturn = returningmyCalendar.getTime();

                    if (tareheOfReturn.before(tareheOfDeparture))
                    {
                        departure_date_EditText.setError("Error");
                        L.t(this, "Return Date cannot be before Departure Date");
                    }
                    else if (tareheOfReturn.after(tareheOfDeparture))
                        {
                            departure_date_EditText.setError(null);
                            departure_date_EditText.setText(sdf.format(departureCalendar.getTime()));
                        }

                }

        }
    }

    private void updateLabelReturning() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String yearFormat = "yyyy";
        SimpleDateFormat sdfYear = new SimpleDateFormat(yearFormat, Locale.US);
        stringReturningDate = sdfYear.format(returningmyCalendar.getTime());


        Date tareheOfReturn = returningmyCalendar.getTime();
        Date hivisasa2 = new Date();

        if (tareheOfReturn.before(hivisasa2))
        {
            returning_date_editText.setError("Error");
            L.t(this, "Please provide a date that isn't in the past");
        }
        else
        {
            if (departure_date_EditText.getText().toString().equalsIgnoreCase(""))
            {
                returning_date_editText.setError(null);
                returning_date_editText.setText(sdf.format(returningmyCalendar.getTime()));
            }
            else
                {
                    Date tareheOfDeparture = departureCalendar.getTime();

                    if (tareheOfDeparture.after(tareheOfReturn))
                    {
                        returning_date_editText.setError("Error");
                        L.t(this, "Return Date cannot be before Departure Date");
                    }
                    else if (tareheOfDeparture.before(tareheOfReturn))
                    {
                        returning_date_editText.setError(null);
                        returning_date_editText.setText(sdf.format(returningmyCalendar.getTime()));
                    }
                }


        }
    }

    private void updateLabelKidsDob() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String yearFormat = "yyyy";
        SimpleDateFormat sdfYear = new SimpleDateFormat(yearFormat, Locale.US);
        kids_dob = sdfYear.format(kids_dobCalendar.getTime());


        Date tarehe3 = kids_dobCalendar.getTime();
        Date hivisasa3 = new Date();

        if (tarehe3.before(hivisasa3))
        {
            kids_dobCalendar.add(Calendar.YEAR, 18);
            Date tareheAdd18 = kids_dobCalendar.getTime();
            if (tareheAdd18.before(hivisasa3) || (tareheAdd18.equals(hivisasa3)))
            {
                kids_dobCalendar.add(Calendar.YEAR, -18);
                TextInputEditText_childDOB.setError(null);
                TextInputEditText_childDOB.setText(sdf.format(kids_dobCalendar.getTime()));
            }
            else
                {
                    TextInputEditText_childDOB.setError("Error");
                    L.t(this, "This Child is not below 18yrs");
                }
        }
        else
        {
            TextInputEditText_childDOB.setError("Error");
            L.t(this, "Please provide a date that isn't in the future");
        }
    }

    private boolean validateField(TextInputLayout the_layout, TextInputEditText the_input, int required_length, String message) {
        if (!ValidationUtil.hasValidContents(the_input) || the_input.getText().toString().trim().length() < required_length) {
            the_layout.setErrorEnabled(true);
            the_layout.setError(message);
            requestFocus(the_input);

            return false;
        }
        the_layout.setErrorEnabled(false);
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validatePhoneField() {
        if (!ValidationUtil.isValidPhoneNumber(nextOfKinPhone_editText)) {
            nextOfKinPhone_TextInputLayout.setErrorEnabled(true);
            nextOfKinPhone_editText.setError(getString(R.string.invalid_phone_number));
            requestFocus(nextOfKinPhone_editText);

            return false;
        }
        nextOfKinPhone_TextInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean validateEmailField() {
        if (!ValidationUtil.hasValidContents(textInputEditText_email_adults) || !ValidationUtil.hasValidEmail(textInputEditText_email_adults)) {
            textInputEditText_email_adults.setError(getString(R.string.invalid_email));
            requestFocus(textInputEditText_email_adults);

            return false;
        }
        return true;
    }

    public void getAdultsNumber() {
        adults_travellers_number = adultsNumber_editText.getText().toString();
        adults_travellers_number2 =Integer.parseInt(adults_travellers_number);
    }

    public void childsNumber() {
        children_travellers_number = childrenNumber_editText.getText().toString();
        children_travellers_number2 = Integer.parseInt(children_travellers_number);
    }

    public void showChildrensDetails() {
        if (switch_children_number.isChecked()==(true))
        {
            childsNumber();

            textView96.setText("Enter the details of child number: "+(counter2+1));
            add_next_child_traveller.setVisibility(View.VISIBLE);
            buttonNext_to_CheckOut.setVisibility(View.GONE);

            //hide
            cardView_parents_details.setVisibility(View.GONE);
            //show
            cardView_children_details.setVisibility(View.VISIBLE);

            if (children_travellers_number2==1)
            {
                add_next_child_traveller.setVisibility(View.GONE);
                buttonNext_to_CheckOut.setVisibility(View.VISIBLE);
            }

            counter2=counter2+1;
        }
        else if (switch_children_number.isChecked()==(false))
        {
            //show
            check_out_cardView.setVisibility(View.VISIBLE);
            //hide
            cardView_parents_details.setVisibility(View.GONE);
        }

        String stringText1 = ("Adults Info:    "+adultTravellersName+" "+adultTravellersIdNumber+" "+adultTravellersEmail+" "+adultTravellersPassportNumber+" ");
        textViewShowParentDetails.setText(Html.fromHtml(stringText1));
        // String text2 = adultTravellersName+" "+adultTravellersIdNumber+" "+adultTravellersEmail+" "+adultTravellersPassportNumber;
        //textViewShowParentDetails.setText(text2);
    }

    public void loadConfirmDetails(){
        if (confirmed_name.equalsIgnoreCase("")) {
            textInputEditText_Name_adults.setText(text_fname+" "+text_lname+" "+text_sname);
            textInputEditText_IdNumber_adults.setText(text_id_number);
            textInputEditText_passportNumber_adults.setText(passportNumber);
            textInputEditText_email_adults.setText(text_email);
            AppCompatEditText_adults_dob.setText(text_dob);
          //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
            }
        else {
            textInputEditText_Name_adults.setText(confirmed_name);
            textInputEditText_IdNumber_adults.setText(confirmed_idNumber);
            textInputEditText_passportNumber_adults.setText(confirmed_passportNumber);
            textInputEditText_email_adults.setText(confirmed_email);
            AppCompatEditText_adults_dob.setText(text_dob);
          //  Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_SHORT).show();
           }
    }

    public void backAwayChildrenDetails() {
        //hide
        cardView_children_details.setVisibility(View.GONE);
        //show
        cardView_parents_details.setVisibility(View.VISIBLE);

        textView95.setText("Confirm your Details ");
        textInputEditText_Name_adults. setText(confirmed_name);
        textInputEditText_IdNumber_adults.setText(confirmed_idNumber);
        textInputEditText_passportNumber_adults.setText(confirmed_passportNumber);
        textInputEditText_email_adults.setText(confirmed_email);
        AppCompatEditText_adults_dob.setText(confirmed_adultDob);

        button12.setVisibility(View.GONE);
        buttonAddNextDetail.setVisibility(View.VISIBLE);
        counter2 = 0;
        counter4=0;
    }

    public void backAwayAdultsDetails(){   //hide
        cardView_parents_details.setVisibility(View.GONE);

        //show
        next_of_kin_details.setVisibility(View.VISIBLE);

        counter=0;
        counter3=0;}

        public void addToAdultsArray(){
        adultTravellersName.add(textInputEditText_Name_adults.getText().toString());
            adultTravellersIdNumber.add(textInputEditText_IdNumber_adults.getText().toString());
            adultTravellersEmail.add(textInputEditText_email_adults.getText().toString());
            adultTravellersDOB.add(AppCompatEditText_adults_dob.getText().toString());
            adultTravellersPassportNumber.add(textInputEditText_passportNumber_adults.getText().toString());}

            public void  getNextOfKinDetails() {
                nextOfKin_name = nextOfKinfullName_editText.getText().toString();
                nextOfKin_phoneNumber = nextOfKinPhone_editText.getText().toString();
                nextOfKin_relationship=nextOfKinRelationship_editText.getText().toString();
            }

            public void calculatePriceOfCover () {

                pricing_plan = spinner_pricing.getSelectedItem().toString();
                rate_plan = spinner_ratePlan.getSelectedItem().toString();

                long diff = returningmyCalendar.getTimeInMillis() - departureCalendar.getTimeInMillis();
                float dayCount = (float) diff / (24 * 60 * 60 * 1000);



                String stringTextTravelDetails2 = ("Number of Adults Travelling: "+adults_travellers_number2+"<br>"+
                "Number of Children Travelling: "+children_travellers_number2+"<br><hr>"+

                "Country of travel: "+country_of_travel+"<br>"+
                "Country of Departure: "+country_of_departure+"<br><hr>"+

                "Returning Date: "+returning_date+"<br>"+
                "Departure Date: "+departure_date+"<br><hr>"+
                        "Number of Days: "+dayCount+"<br><hr>"+

                "Pricing Plan: "+pricing_plan+"<br>"+
                "Rate Plan: "+rate_plan+"<br><hr>");

              textViewPriceQuotation.setText(Html.fromHtml(stringTextTravelDetails2));
            }

            public void checkSchengen(){ if (
                    (spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Austria")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Belgium")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Czech Republic")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Denmark")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Estonia")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Finland")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("France")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Germany")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Greece")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Hungary")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Iceland")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Italy")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Latvia")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Lithuania")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Liechtenstein")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Luxembourg")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Malta")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Netherlands")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Norway")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Poland")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Portugal")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Slovakia")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Slovenia")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Spain")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Sweden")
                            || spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Switzerland")) &&
                            (spinner_ratePlan.getSelectedItem().toString().equalsIgnoreCase("EUROPE"))
                    )
            {
                calculatePriceOfCover();
            }
            else if (
                    (!spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Austria")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Belgium")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Czech Republic")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Denmark")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Estonia")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Finland")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("France")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Germany")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Greece")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Hungary")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Iceland")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Italy")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Latvia")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Lithuania")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Liechtenstein")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Luxembourg")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Malta")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Netherlands")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Norway")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Poland")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Portugal")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Slovakia")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Slovenia")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Spain")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Sweden")
                            || !spinner_countryOfTravel.getSelectedItem().toString().equalsIgnoreCase("Switzerland"))
                            && (spinner_ratePlan.getSelectedItem().toString().equalsIgnoreCase("EUROPE"))
                    )
            {
                String selectedCountry = spinner_countryOfTravel.getSelectedItem().toString();
                TextView errorText = (TextView)spinner_ratePlan.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText(selectedCountry+" is not in Europe (Schengen)");
            }
            else{calculatePriceOfCover();}
            }
            //public void closeProgressDialog(){mProgressDialog.hide();}
}




















