package ke.co.coverapp.coverapp.fragments;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.app.DatePickerDialog;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.NotificationActivity;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.backgroundWorkers.BackGroundWorker;
import ke.co.coverapp.coverapp.callbacks.AssetsLoadedListener;
import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.Assets;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.tasks.TaskLoadAssets;
import ke.co.coverapp.coverapp.tasks.TaskLoadBalance;
import ke.co.coverapp.coverapp.utility.ValidationUtil;

public class MonthLongVehicleInsurance extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layout_delivery_address, layout_step_one, layout_step_two, layout_step_three, layout_step_four, layout_step_five, layout_final_step;
    TextInputEditText editTextName, editTextIdNumber, editTextEmail, editTextPhone, editTextDeliveryAddress, editTextRegistrationPlate, editTextMake, editTextModel;
    TextInputLayout textInputLayout_name, textInputLayout_number, textInputLayout_email, textInputLayout_phone, textIputLayout_make, textIputLayout_model, textIputLayout_registrationPlate, textInputLayout_delivery_address;
    Button buttonCancelDelivery, buttonOkDeliveryAddress, buttonBackToStepFour, buttonBackToStepOneFromThree, buttonCoverMyself, buttonOtherPersonCover, buttonbackToStepOne, buttonNextToStepThree, buttonBackToStepTwo, buttonNextToStepFour, buttonBackToStepThree, buttonNextToStepFive, buttonPickUp, buttonDeliver, buttonCancelLastStep, buttonBuy;
    DatePickerDialog.OnDateSetListener datePickerCoverStart2;
    Calendar myCalendar = Calendar.getInstance();
    EditText datePickerCoverStart;
    String cover_for_who2, pickup_vs_delivery2, coverStartDate;
    Context context;
    List<Assets> list_assets;
    TextView textView76;
    Spinner commOrPriv_spinner;
    CheckBox checkBoxTCs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_long_vehicle_insurance);
        setTitle(getResources().getText(R.string.tpmotor));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        checkBoxTCs= (CheckBox ) findViewById(R.id.checkBoxTCs);

        datePickerCoverStart = (EditText) findViewById(R.id.datePickerCoverStart);
        datePickerCoverStart.setOnClickListener(this);

        layout_step_one = (LinearLayout)findViewById(R.id.layout_step_one);
        layout_step_two= (LinearLayout)findViewById(R.id.layout_step_two);
        layout_step_three= (LinearLayout)findViewById(R.id.layout_step_three);
        layout_step_four= (LinearLayout)findViewById(R.id.layout_step_four);
        layout_step_five= (LinearLayout)findViewById(R.id.layout_step_five);
        layout_final_step= (LinearLayout)findViewById(R.id.layout_final_step);
        layout_delivery_address= (LinearLayout)findViewById(R.id.layout_delivery_address);

        commOrPriv_spinner = (Spinner)findViewById(R.id.commOrPriv_spinner);
        commOrPriv_spinner.setFocusable(true);
        commOrPriv_spinner.setFocusableInTouchMode(true);

        editTextName = (TextInputEditText)findViewById(R.id.editTextName);
        editTextIdNumber= (TextInputEditText)findViewById(R.id.editTextIdNumber);
        editTextEmail= (TextInputEditText)findViewById(R.id.editTextEmail);
        editTextPhone= (TextInputEditText)findViewById(R.id.editTextPhone);
        editTextMake= (TextInputEditText)findViewById(R.id.editTextMake);
        editTextModel= (TextInputEditText)findViewById(R.id.editTextModel);
        editTextRegistrationPlate=(TextInputEditText )findViewById(R.id.editTextRegistrationPlate);
        editTextDeliveryAddress=(TextInputEditText )findViewById(R.id.editTextDeliveryAddress);

        textView76=(TextView ) findViewById(R.id.textView76);
        String payForDelivery = "Please Note you will bear the cost of delivery.<br><i>All pick ups are done at Sydlink offices. Their contact info is listed below:<br>\n" +
                "NJENGI HOUSE ,<br>\n" +
                "TOM MBOYA STREET,<br>\n" +
                "Nairobi, Kenya<br>\n" +
                "Telephone: 0723423798<br>\n";
        textView76.setText(Html.fromHtml(payForDelivery));

        textInputLayout_name = (TextInputLayout) findViewById(R.id.textInputLayout_name);
        textInputLayout_number= (TextInputLayout) findViewById(R.id.textInputLayout_number);
        textInputLayout_email= (TextInputLayout) findViewById(R.id.textInputLayout_email);
        textInputLayout_phone= (TextInputLayout) findViewById(R.id.textInputLayout_phone);

        textIputLayout_make = (TextInputLayout) findViewById(R.id.textIputLayout_make);
        textIputLayout_model= (TextInputLayout) findViewById(R.id.textIputLayout_model);
        textIputLayout_registrationPlate= (TextInputLayout) findViewById(R.id.textIputLayout_registrationPlate);
        textInputLayout_delivery_address= (TextInputLayout) findViewById(R.id.textInputLayout_delivery_address);

        buttonCoverMyself = (Button)findViewById(R.id.buttonCoverMyself);
        buttonOtherPersonCover= (Button)findViewById(R.id.buttonOtherPersonCover);
        buttonbackToStepOne= (Button)findViewById(R.id.buttonbackToStepOne);
        buttonNextToStepThree= (Button)findViewById(R.id.buttonNextToStepThree);
        buttonBackToStepTwo= (Button)findViewById(R.id.buttonBackToStepTwo);
        buttonNextToStepFour= (Button)findViewById(R.id.buttonNextToStepFour);
        buttonBackToStepThree= (Button)findViewById(R.id.buttonBackToStepThree);
        buttonNextToStepFive= (Button)findViewById(R.id.buttonNextToStepFive);
        buttonPickUp= (Button)findViewById(R.id.buttonPickUp);
        buttonDeliver= (Button)findViewById(R.id.buttonDeliver);
        buttonCancelLastStep= (Button)findViewById(R.id.buttonCancelLastStep);
        buttonBuy= (Button)findViewById(R.id.buttonBuy);
        buttonBackToStepOneFromThree = (Button )findViewById(R.id.buttonBackToStepOneFromThree);
        buttonBackToStepFour= (Button)findViewById(R.id.buttonBackToStepFour);
        buttonCancelDelivery= (Button)findViewById(R.id.buttonCancelDelivery);
        buttonOkDeliveryAddress= (Button)findViewById(R.id.buttonOkDeliveryAddress);

        list_assets = MyApplication.getWritableDatabase().readNonCoveredAssets();
        if (list_assets.isEmpty()) {
            // Make API request
//            new TaskLoadAssets(( AssetsLoadedListener ) this, 0).execute();
        }

        buttonCoverMyself.setOnClickListener(this);
        buttonOtherPersonCover.setOnClickListener(this);
        buttonbackToStepOne.setOnClickListener(this);
        buttonNextToStepThree.setOnClickListener(this);
        buttonBackToStepTwo.setOnClickListener(this);
        buttonNextToStepFour.setOnClickListener(this);
        buttonBackToStepThree.setOnClickListener(this);
        buttonNextToStepFive.setOnClickListener(this);
        buttonPickUp.setOnClickListener(this);
        buttonDeliver.setOnClickListener(this);
        buttonCancelLastStep.setOnClickListener(this);
        buttonBuy.setOnClickListener(this);
        buttonBackToStepOneFromThree.setOnClickListener(this);
        buttonBackToStepFour.setOnClickListener(this);
        buttonCancelDelivery.setOnClickListener(this);
        buttonOkDeliveryAddress.setOnClickListener(this);

        datePickerCoverStart2 = new DatePickerDialog.OnDateSetListener() {

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

        datePickerCoverStart.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonCoverMyself:

                //hide
                layout_step_one.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_left));
                layout_step_one.setVisibility(View.GONE);
                buttonBackToStepTwo.setVisibility(View.GONE);

                //show
                layout_step_three.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_right));
                layout_step_three.setVisibility(View.VISIBLE);
                textIputLayout_make.setErrorEnabled(false);
                textIputLayout_model.setErrorEnabled(false);
                textIputLayout_registrationPlate.setErrorEnabled(false);

                //Declare Variable cover_for_who
                cover_for_who2 = "self";

                break;

            case R.id.buttonOtherPersonCover:

                //hide
                layout_step_one.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_left));
                layout_step_one.setVisibility(View.GONE);
                buttonBackToStepOneFromThree.setVisibility(View.GONE);

                //show
                layout_step_two.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_right));
                layout_step_two.setVisibility(View.VISIBLE);
                textInputLayout_name.setErrorEnabled(false);
                textInputLayout_number.setErrorEnabled(false);
                textInputLayout_email.setErrorEnabled(false);
                textInputLayout_phone.setErrorEnabled(false);

                //Declare Variable cover_for_who
                cover_for_who2 = "other";

                break;

            case R.id.buttonbackToStepOne:

                //hide
                layout_step_two.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_right));
                layout_step_two.setVisibility(View.GONE);

                //show
                layout_step_one.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_left));
                layout_step_one.setVisibility(View.VISIBLE);
                buttonBackToStepOneFromThree.setVisibility(View.VISIBLE);

                break;

            case R.id.buttonNextToStepThree:

                if (validateField(textInputLayout_name, editTextName, 3, "Invalid Name")
                        &&validateField(textInputLayout_number, editTextIdNumber, 6, getString(R.string.invalid_id_no))
                &&  validateEmailField() && validatePhoneField())
                {
                    //hide
                    layout_step_two.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_left));
                    layout_step_two.setVisibility(View.GONE);

                    //show
                    layout_step_three.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_right));
                    layout_step_three.setVisibility(View.VISIBLE);
                    textIputLayout_make.setErrorEnabled(false);
                    textIputLayout_model.setErrorEnabled(false);
                    textIputLayout_registrationPlate.setErrorEnabled(false);

                }

                break;

            case R.id.buttonBackToStepTwo:

                //hide
                layout_step_three.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_right));
                layout_step_three.setVisibility(View.GONE);

                //show
                layout_step_two.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_left));
                layout_step_two.setVisibility(View.VISIBLE);
                textInputLayout_name.setErrorEnabled(false);
                textInputLayout_number.setErrorEnabled(false);
                textInputLayout_email.setErrorEnabled(false);
                textInputLayout_phone.setErrorEnabled(false);

                break;

            case R.id.buttonBackToStepOneFromThree:

                //hide
                layout_step_three.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_right));
                layout_step_three.setVisibility(View.GONE);

                //show
                layout_step_one.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_left));
                layout_step_one.setVisibility(View.VISIBLE);
                buttonBackToStepTwo.setVisibility(View.VISIBLE);

                break;

            case R.id.buttonNextToStepFour:
                if (validateField(textIputLayout_make, editTextMake, 3, getString(R.string.invalid_make))
                        && validateField(textIputLayout_model, editTextModel, 3, getString(R.string.invalid_model))) {

                    // Validate Registration Plate
                    if(editTextRegistrationPlate.getText().length() <= 0)
                    {
                        editTextRegistrationPlate.setError("A Number plate  cannot be blank.");
                    }
                    else
                    {
                        //The first three and last digits have to be letters
                     //   Integer pin_length = editTextRegistrationPlate.getText().length();
                        //    if (!Character.isLetter(editTextRegistrationPlate.getText().charAt(2)) || !Character.isLetter(editTextRegistrationPlate.getText().charAt(pin_length - 1)))
                        //    {
                        //          editTextRegistrationPlate.setError("First three and last characters have to be letters.");
                        //      }
                        //     else
                        //    {
                            if (commOrPriv_spinner.getSelectedItem().toString().equalsIgnoreCase(""))
                            {
                                TextView errorText = (TextView)commOrPriv_spinner.getSelectedView();
                                errorText.setError("");
                                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                errorText.setText("Nothing has been selected!");//changes the selected item text to this
                                commOrPriv_spinner.requestFocus();
                                //((TextView)commOrPriv_spinner.SelectedView).Error = "Your Error Text";
                                //((TextView)commOrPriv_spinner.getChildAt(0)).setError("Cannot be blank!");
                                //L.t(this, "Cannot be blank!");
                            }
                            else
                            {
                                //hide
                                layout_step_three.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_left));
                                layout_step_three.setVisibility(View.GONE);

                                //show
                                layout_step_four.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_right));
                                layout_step_four.setVisibility(View.VISIBLE);
                                textIputLayout_make.setErrorEnabled(false);
                                textIputLayout_model.setErrorEnabled(false);
                                textInputLayout_delivery_address.setErrorEnabled(false);
                                //datePickerCoverStart.performClick();
                            }
                        //  }
                    }
                }
                break;

            case R.id.buttonBackToStepThree:

                //hide
                layout_step_four.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_right));
                layout_step_four.setVisibility(View.GONE);

                //show
                layout_step_three.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_left));
                layout_step_three.setVisibility(View.VISIBLE);
                textIputLayout_make.setErrorEnabled(false);
                textIputLayout_model.setErrorEnabled(false);
                textIputLayout_registrationPlate.setErrorEnabled(false);

                break;

            case R.id.buttonNextToStepFive:

                    //hide
                    layout_step_four.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_left));
                    layout_step_four.setVisibility(View.GONE);

                    //show
                    layout_step_five.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_right));
                    layout_step_five.setVisibility(View.VISIBLE);

                break;

            case R.id.buttonPickUp:

                //hide
                layout_step_five.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_left));
                layout_step_five.setVisibility(View.GONE);

                //show
                layout_final_step.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_right));
                layout_final_step.setVisibility(View.VISIBLE);

                pickup_vs_delivery2 = "pick_up";

                break;

            case R.id.buttonDeliver:

                //hide
                buttonPickUp.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_left));
                buttonPickUp.setVisibility(View.GONE);
                buttonBackToStepFour.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_left));
                buttonBackToStepFour.setVisibility(View.GONE);
                buttonDeliver.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_left));
                buttonDeliver.setVisibility(View.GONE);

                //show
                layout_delivery_address.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_right));
                layout_delivery_address.setVisibility(View.VISIBLE);
                textInputLayout_delivery_address.setErrorEnabled(false);

                pickup_vs_delivery2 = "deliver";

                break;
            case R.id.buttonOkDeliveryAddress:

                if (validateField(textInputLayout_delivery_address, editTextDeliveryAddress, 3, "Invalid address"))
            {

                //hide
                layout_step_five.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_left));
                layout_step_five.setVisibility(View.GONE);

                //show
                layout_final_step.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_right));
                layout_final_step.setVisibility(View.VISIBLE);
                textInputLayout_delivery_address.setErrorEnabled(false);
                pickup_vs_delivery2 = "deliver";
            }
                break;

            case R.id.buttonCancelDelivery:

                //hide
                layout_delivery_address.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_right));
                layout_delivery_address.setVisibility(View.GONE);

                //show
                buttonPickUp.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_left));
                buttonPickUp.setVisibility(View.VISIBLE);
                buttonBackToStepFour.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_left));
                buttonBackToStepFour.setVisibility(View.VISIBLE);
                buttonDeliver.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_left));
                buttonDeliver.setVisibility(View.VISIBLE);

                break;

            case R.id.buttonCancelLastStep:

                //hide
                layout_final_step.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_right));
                layout_final_step.setVisibility(View.GONE);
                layout_delivery_address.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_right));
                layout_delivery_address.setVisibility(View.GONE);

                //show
                layout_step_five.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_left));
                layout_step_five.setVisibility(View.VISIBLE);
                buttonPickUp.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_left));
                buttonPickUp.setVisibility(View.VISIBLE);
                buttonBackToStepFour.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_left));
                buttonBackToStepFour.setVisibility(View.VISIBLE);
                buttonDeliver.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_left));
                buttonDeliver.setVisibility(View.VISIBLE);

                break;

            case R.id.buttonBackToStepFour:

                //hide
                layout_step_five.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_right));
                layout_step_five.setVisibility(View.GONE);
                layout_delivery_address.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.exit_to_right));
                layout_delivery_address.setVisibility(View.GONE);

                //show
                layout_step_four.startAnimation(AnimationUtils.loadAnimation(MonthLongVehicleInsurance.this, R.anim.enter_from_left));
                layout_step_four.setVisibility(View.VISIBLE);
                textIputLayout_make.setErrorEnabled(false);
                textIputLayout_model.setErrorEnabled(false);
                textInputLayout_delivery_address.setErrorEnabled(false);

                break;

            case R.id.datePickerCoverStart:
                // Show datePickerFragment
                new DatePickerDialog(MonthLongVehicleInsurance.this, android.app.AlertDialog.THEME_HOLO_LIGHT,  datePickerCoverStart2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.buttonBuy:

                if (checkBoxTCs.isChecked()) {

                    String cover_for_who, name, id_number, email, phone, make, model, reg_plates, cover_start_date, pickup_vs_delivery, delivery_address, vehicle_type;
                    checkBoxTCs.setError(null);
                    String text_id_number = MyApplication.readFromPreferences(MyApplication.getAppContext(), Keys.keys.ID_NUMBER, ValidationUtil.getDefault());

                    cover_for_who = cover_for_who2;
                    pickup_vs_delivery = pickup_vs_delivery2;
                    name = editTextName.getText().toString();
                    id_number = editTextIdNumber.getText().toString();
                    email = editTextEmail.getText().toString();
                    phone = editTextPhone.getText().toString();
                    make = editTextMake.getText().toString();
                    model = editTextModel.getText().toString();
                    reg_plates = editTextRegistrationPlate.getText().toString();
                    cover_start_date = datePickerCoverStart.getText().toString();
                    pickup_vs_delivery = pickup_vs_delivery2.toString();
                    delivery_address = editTextDeliveryAddress.getText().toString();
                    String type = "monthLongVehicleCover";
                    vehicle_type = commOrPriv_spinner.getSelectedItem().toString();

                    ConnectivityManager connectivityManager = ( ConnectivityManager ) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        //we are connected to a network
                        BackGroundWorker backGroundWorker = new BackGroundWorker(this);
                        backGroundWorker.execute(type, cover_for_who, name, id_number, email, phone, vehicle_type, make, model, reg_plates, cover_start_date, pickup_vs_delivery, delivery_address, text_id_number);
                    } else {
                        L.T(MyApplication.getAppContext(), getString(R.string.no_network_connection));
                    }
                }
                else
                    {
                        checkBoxTCs.setError("Agree to Terms and Conditions");
                        L.t(this,"Agree to Terms and Conditions");
                    }
                break;
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

    private boolean validateEmailField() {
        if (!ValidationUtil.hasValidContents(editTextEmail) || !ValidationUtil.hasValidEmail(editTextEmail)) {
            textInputLayout_email.setErrorEnabled(true);
            editTextEmail.setError(getString(R.string.invalid_email));
            requestFocus(editTextEmail);

            return false;
        }
        textInputLayout_email.setErrorEnabled(false);
        return true;
    }

    private boolean validatePhoneField() {
        if (!ValidationUtil.isValidPhoneNumber(editTextPhone)) {
            textInputLayout_phone.setErrorEnabled(true);
            textInputLayout_phone.setError(getString(R.string.invalid_phone_number));
            requestFocus(editTextPhone);

            return false;
        }
        textInputLayout_phone.setErrorEnabled(false);
        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String yearFormat = "yyyy";
        SimpleDateFormat sdfYear = new SimpleDateFormat(yearFormat, Locale.US);
        coverStartDate = sdfYear.format(myCalendar.getTime());
        datePickerCoverStart.setText(sdf.format(myCalendar.getTime()));
    }

    public void onBackPressed()
    {
if (layout_step_one.getVisibility() == View.VISIBLE)
{
finish();
}
else if (layout_step_two.getVisibility() == View.VISIBLE)
{
buttonbackToStepOne.performClick();
}
else if (layout_step_three.getVisibility() == View.VISIBLE)
{
    if (buttonBackToStepOneFromThree.getVisibility() == View.VISIBLE)
    {
        buttonBackToStepOneFromThree.performClick();
    }
    else if (buttonBackToStepTwo.getVisibility() == View.VISIBLE)
    {
        buttonBackToStepTwo.performClick();
    }

}
else if (layout_step_four.getVisibility() == View.VISIBLE)
{
    buttonBackToStepThree.performClick();
}
else if (layout_step_five.getVisibility() == View.VISIBLE)
{
    buttonBackToStepFour.performClick();
}
else if (layout_delivery_address.getVisibility() == View.VISIBLE)
{
buttonCancelDelivery.performClick();
}
else if (layout_final_step.getVisibility() == View.VISIBLE)
{
buttonCancelLastStep.performClick();
}
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}


