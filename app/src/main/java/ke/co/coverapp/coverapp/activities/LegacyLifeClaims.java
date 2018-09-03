package ke.co.coverapp.coverapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.tasks.TaskLoadBalance;
import ke.co.coverapp.coverapp.utility.ValidationUtil;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.CAMERA_REQUEST_ONE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.GALLERY_REQUEST_ONE;

public class LegacyLifeClaims extends AppCompatActivity implements View.OnClickListener{

    TextView textView99;
    Button buttonproceed, buttonScannedBurial_Permit, buttonImageBurial_Permit, buttonScannedclaimant_id, buttonImageclaimant_id, buttonScannedSurrender_Letter, buttonImageSurrender_Letter, button_Scanned_Claimant_Statement, buttonImage_Claimant_Statement, buttonScannedhospital_discharge_summary, buttonImagehospital_discharge_summary, buttonProceed6, buttonProceed5, buttonProceed4, buttonProceed3, buttonProceed2;
    LinearLayout layout_introductory_text, layout_Burial_Permit, layout_claimant_id, layout_Surrender_Letter, layout_Claimant_Statement, layout_hospital_discharge_summary;
    String imageViewBurial_Permit_string, imageViewclaimant_id_string, imageViewSurrender_Letter_string, imageView_Claimant_Statement_string, imageViewhospital_discharge_summary_string;
    ImageView imageViewBurial_Permit, imageViewclaimant_id, imageViewSurrender_Letter, imageView_Claimant_Statement, imageViewhospital_discharge_summary;
    public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legacy_life_claims);
        setTitle("Legacy Life Plan Claims");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        textView99=(TextView )findViewById(R.id.textView99);
        String silverIndividual = "<ul><li>1.\tOriginal Burial Permit</li><br>\n" +
                "<li>2.\tOriginal I.D. Card of Claimant (Claimant in this case likely the person listed to be the beneficiary)</li><br>\n" +
                "<li>3.\tOriginal Surrender Letter or I.D. Card of the deceased.</li><br>\n" +
                "<li>4.\tOriginal Policy Document</li><br>\n" +
                "<li>5.\tClaimant's statement duly signed by the claimant</li><br>\n" +
                "<li>6.\tHospital Discharge summary</li></ul><br>";
        textView99.setText(Html.fromHtml(silverIndividual));

        buttonproceed=(Button )findViewById(R.id.buttonproceed) ;
        buttonScannedBurial_Permit=(Button )findViewById(R.id.buttonScannedBurial_Permit) ;
        buttonImageBurial_Permit=(Button )findViewById(R.id.buttonImageBurial_Permit) ;
        buttonScannedclaimant_id=(Button )findViewById(R.id.buttonScannedclaimant_id) ;
        buttonImageclaimant_id=(Button )findViewById(R.id.buttonImageclaimant_id) ;
        buttonScannedSurrender_Letter=(Button )findViewById(R.id.buttonScannedSurrender_Letter) ;
        buttonImageSurrender_Letter=(Button )findViewById(R.id.buttonImageSurrender_Letter) ;
        button_Scanned_Claimant_Statement=(Button )findViewById(R.id.button_Scanned_Claimant_Statement) ;
        buttonImage_Claimant_Statement=(Button )findViewById(R.id.buttonImage_Claimant_Statement) ;
        buttonScannedhospital_discharge_summary=(Button )findViewById(R.id.buttonScannedhospital_discharge_summary) ;
        buttonImagehospital_discharge_summary=(Button )findViewById(R.id.buttonImagehospital_discharge_summary) ;
        buttonProceed6=(Button )findViewById(R.id.buttonProceed6) ;
        buttonProceed5=(Button )findViewById(R.id.buttonProceed5) ;
        buttonProceed4=(Button )findViewById(R.id.buttonProceed4) ;
        buttonProceed3=(Button )findViewById(R.id.buttonProceed3) ;
        buttonProceed2=(Button )findViewById(R.id.buttonProceed2) ;

        buttonproceed.setOnClickListener(this);
        buttonScannedBurial_Permit.setOnClickListener(this);
        buttonImageBurial_Permit.setOnClickListener(this);
        buttonScannedclaimant_id.setOnClickListener(this);
        buttonImageclaimant_id.setOnClickListener(this);
        buttonScannedSurrender_Letter.setOnClickListener(this);
        buttonImageSurrender_Letter.setOnClickListener(this);
        button_Scanned_Claimant_Statement.setOnClickListener(this);
        buttonImage_Claimant_Statement.setOnClickListener(this);
        buttonScannedhospital_discharge_summary.setOnClickListener(this);
        buttonImagehospital_discharge_summary.setOnClickListener(this);

        buttonProceed6.setOnClickListener(this);
        buttonProceed5.setOnClickListener(this);
        buttonProceed4.setOnClickListener(this);
        buttonProceed3.setOnClickListener(this);
        buttonProceed2.setOnClickListener(this);

        layout_introductory_text = (LinearLayout )findViewById(R.id.layout_introductory_text) ;
        layout_Burial_Permit= (LinearLayout )findViewById(R.id.layout_Burial_Permit) ;
        layout_claimant_id= (LinearLayout )findViewById(R.id.layout_claimant_id) ;
        layout_Surrender_Letter= (LinearLayout )findViewById(R.id.layout_Surrender_Letter) ;
        layout_Claimant_Statement= (LinearLayout )findViewById(R.id.layout_Claimant_Statement) ;
        layout_hospital_discharge_summary= (LinearLayout )findViewById(R.id.layout_hospital_discharge_summary) ;

        imageViewBurial_Permit= (ImageView ) findViewById(R.id.imageViewBurial_Permit) ;
        imageViewclaimant_id= (ImageView ) findViewById(R.id.imageViewclaimant_id) ;
        imageViewSurrender_Letter= (ImageView ) findViewById(R.id.imageViewSurrender_Letter) ;
        imageView_Claimant_Statement= (ImageView ) findViewById(R.id.imageView_Claimant_Statement) ;
        imageViewhospital_discharge_summary= (ImageView ) findViewById(R.id.imageViewhospital_discharge_summary) ;

    }

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
    public void openDocuments()
    {
        Intent getImage = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        Intent pickImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
        startActivityForResult(Intent.createChooser(getImage, getString(R.string.select_image)).putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickImage}), GALLERY_REQUEST_ONE);
    }
    public void openCamera()
    {
        startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_ONE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonproceed:

                //show
                layout_Burial_Permit.setVisibility(View.VISIBLE);
                //hide
                layout_introductory_text.setVisibility(View.GONE);
                break;

            case R.id.buttonScannedBurial_Permit:
                openDocuments();
                break;

            case R.id.buttonImageBurial_Permit:
                openCamera();
                break;

            case R.id.buttonScannedclaimant_id:
                openDocuments();
                break;

            case R.id.buttonImageclaimant_id:
                openCamera();
                break;

            case R.id.buttonScannedSurrender_Letter:
                openDocuments();
                break;

            case R.id.buttonImageSurrender_Letter:
                openCamera();
                break;

            case R.id.button_Scanned_Claimant_Statement:
                openDocuments();
                break;

            case R.id.buttonImage_Claimant_Statement:
                openCamera();
                break;

            case R.id.buttonScannedhospital_discharge_summary:
                openDocuments();
                break;

            case R.id.buttonImagehospital_discharge_summary:
                openCamera();
                break;

            case R.id.buttonProceed6:

                //show

                //hide

                break;

            case R.id.buttonProceed5:
                //show
layout_hospital_discharge_summary.setVisibility(View.VISIBLE);
                //hide
                layout_Claimant_Statement.setVisibility(View.GONE);
                break;

            case R.id.buttonProceed4:
                //show
                layout_Claimant_Statement.setVisibility(View.VISIBLE);
                //hide
                layout_Surrender_Letter.setVisibility(View.GONE);
                break;

            case R.id.buttonProceed3:
                //show
                layout_Surrender_Letter.setVisibility(View.VISIBLE);
                //hide
                layout_claimant_id.setVisibility(View.GONE);
                break;

            case R.id.buttonProceed2:

                //show
                layout_claimant_id.setVisibility(View.VISIBLE);
                //hide
                layout_Burial_Permit.setVisibility(View.GONE);
                break;
        }

    }
    public  void onBackPressed()
    {
        if (layout_hospital_discharge_summary.getVisibility() == View.VISIBLE)
        {

            //show
            layout_Claimant_Statement.setVisibility(View.VISIBLE);
            //hide
            layout_hospital_discharge_summary.setVisibility(View.GONE);
        }
        else if (layout_Claimant_Statement.getVisibility() == View.VISIBLE)
        {

            //show
            layout_Surrender_Letter.setVisibility(View.VISIBLE);
            //hide
            layout_Claimant_Statement.setVisibility(View.GONE);
        }
        else if (layout_Surrender_Letter.getVisibility() == View.VISIBLE)
        {

            //show
            layout_claimant_id.setVisibility(View.VISIBLE);
            //hide
            layout_Surrender_Letter.setVisibility(View.GONE);
        }
        else if (layout_claimant_id.getVisibility() == View.VISIBLE)
        {

            //show
            layout_Burial_Permit.setVisibility(View.VISIBLE);
            //hide
            layout_claimant_id.setVisibility(View.GONE);
        }
        else if (layout_Burial_Permit.getVisibility() == View.VISIBLE)
        {

            //show
            layout_introductory_text.setVisibility(View.VISIBLE);
            //hide
            layout_Burial_Permit.setVisibility(View.GONE);
        }
        else if (layout_introductory_text.getVisibility() == View.VISIBLE)
        {
            finish();
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && data != null) {

            if (requestCode == GALLERY_REQUEST_ONE ) {
                try {
                    // From the gallery
                    InputStream inputStream = MyApplication.getAppContext().getContentResolver().openInputStream(data.getData());

                    if (inputStream != null) {
                        L.m(inputStream.toString());
                        switch (requestCode) {
                            case GALLERY_REQUEST_ONE:

                                if (layout_hospital_discharge_summary.getVisibility() == View.VISIBLE)
                                {
                                    imageViewhospital_discharge_summary.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                    imageViewhospital_discharge_summary_string = inputStream.toString();
                                }
                                else if (layout_Claimant_Statement.getVisibility() == View.VISIBLE)
                                {
                                    imageView_Claimant_Statement.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                    imageView_Claimant_Statement_string = inputStream.toString();
                                }
                                else if (layout_Surrender_Letter.getVisibility() == View.VISIBLE)
                                {
                                    imageViewSurrender_Letter.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                    imageViewSurrender_Letter_string = inputStream.toString();
                                }
                                else if (layout_claimant_id.getVisibility() == View.VISIBLE)
                                {
                                    imageViewclaimant_id.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                    imageViewclaimant_id_string = inputStream.toString();
                                }
                                else if (layout_Burial_Permit.getVisibility() == View.VISIBLE)
                                {
                                    imageViewBurial_Permit.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                    imageViewBurial_Permit_string = inputStream.toString();
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

                        if (layout_hospital_discharge_summary.getVisibility() == View.VISIBLE)
                        {
                            imageViewhospital_discharge_summary.setImageBitmap(photo);
                            imageViewhospital_discharge_summary_string = ValidationUtil.getStringImage(photo);
                        }
                        else if (layout_Claimant_Statement.getVisibility() == View.VISIBLE)
                        {
                            imageView_Claimant_Statement.setImageBitmap(photo);
                            imageView_Claimant_Statement_string = ValidationUtil.getStringImage(photo);
                        }
                        else if (layout_Surrender_Letter.getVisibility() == View.VISIBLE)
                        {
                            imageViewSurrender_Letter.setImageBitmap(photo);
                            imageViewSurrender_Letter_string = ValidationUtil.getStringImage(photo);
                        }
                        else if (layout_claimant_id.getVisibility() == View.VISIBLE)
                        {
                            imageViewclaimant_id.setImageBitmap(photo);
                            imageViewclaimant_id_string = ValidationUtil.getStringImage(photo);
                        }
                        else if (layout_Burial_Permit.getVisibility() == View.VISIBLE)
                        {
                            imageViewBurial_Permit.setImageBitmap(photo);
                            imageViewBurial_Permit_string = ValidationUtil.getStringImage(photo);
                        }
                        break;
                }
            }
        }
    }
}
