package ke.co.coverapp.coverapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.Covers;

public class ViewCoverActivity extends AppCompatActivity {

    int coverId;
    String policyNumber, tnxCode, purchaseDate, expiryDate, paymentSchedule, paymentAmount, coverType, paymentPeriod;
    TextView policyNumberTextView, tnxCodeTextView, purchaseDateTextView, expiryDateTextView, paymentScheduleTextView, paymentAmountTextView, paymentAmountTitleTextView, coverTypeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cover);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Getting data passed from intent
        Bundle extras = getIntent().getExtras();
        coverId = extras.getInt("coverId");

        // Get all details from intent
        policyNumber = extras.getString("policyNumber");
        tnxCode = extras.getString("tnxCode");
        purchaseDate = extras.getString("purchaseDate");
        paymentSchedule = extras.getString("paymentSchedule");
        paymentAmount = extras.getString("paymentAmount");
        coverType = extras.getString("coverType");
        expiryDate = extras.getString("expiryDate");

        // Initializing fields
        policyNumberTextView = (TextView) findViewById(R.id.policyNumber);
        tnxCodeTextView = (TextView) findViewById(R.id.tnxCode);
        purchaseDateTextView = (TextView) findViewById(R.id.purchaseDate);
        expiryDateTextView = (TextView) findViewById(R.id.expiryDate);
        paymentScheduleTextView = (TextView) findViewById(R.id.paymentSchedule);
        paymentAmountTextView = (TextView) findViewById(R.id.paymentAmount);
        paymentAmountTitleTextView = (TextView) findViewById(R.id.paymentAmountTitle);
        coverTypeTextView = (TextView) findViewById(R.id.coverType);

        // Get payment amount title
        paymentPeriod = getPaymentPeriodString(paymentSchedule);

        // Get payment schedule
        paymentSchedule = paymentPeriod;

        // Set text values
        policyNumberTextView.setText(policyNumber);
        tnxCodeTextView.setText(tnxCode);
        purchaseDateTextView.setText(purchaseDate);
        expiryDateTextView.setText(expiryDate);
        paymentScheduleTextView.setText(paymentSchedule);
        paymentAmountTextView.setText("Ksh. " + paymentAmount);
        paymentAmountTitleTextView.setText(paymentPeriod + " Premiums");
        coverTypeTextView.setText(coverType.replace("_", " ").toUpperCase());

    }

    public String getPaymentPeriodString(String paymentPeriod) {
        String period = "";

        if(paymentPeriod.matches("1")) {
            period = "Monthly";

        } else if(paymentPeriod.matches("3")) {
            period = "Quarterly";

        } else if(paymentPeriod.matches("6")) {
            period = "Bi-annually";

        } else if(paymentPeriod.matches("12")) {
            period = "Annually";

        } else {
            period = "Monthly";

        }

        return period;
    }
}
