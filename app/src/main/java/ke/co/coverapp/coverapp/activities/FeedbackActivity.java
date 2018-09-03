package ke.co.coverapp.coverapp.activities;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.log.L;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout help_skype, help_email, help_whatsapp,help_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        help_skype        =       (RelativeLayout) findViewById(R.id.help_skype);
        help_email        =       (RelativeLayout) findViewById(R.id.help_email);
        help_call         =       (RelativeLayout) findViewById(R.id.help_call);
        help_whatsapp = (RelativeLayout) findViewById(R.id.help_whatsapp);

        help_call.setOnClickListener(this);
        help_email.setOnClickListener(this);
        help_skype.setOnClickListener(this);
        help_whatsapp.setOnClickListener(this);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.help_call:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + getString(R.string.support_phone_number)));
                startActivity(intent);
                break;
            case R.id.help_email:

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", getString(R.string.support_email), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "CoverApp Assistance ");
                startActivity(Intent.createChooser(emailIntent, getString(R.string.chooser_email_title)));
                break;
            case R.id.help_skype:

                initiateSkypeUri(MyApplication.getAppContext(), "skype:live:info_759282?chat");

                break;

            case R.id.help_whatsapp:
                Intent sendIntent = new Intent();
                sendIntent.setComponent(new  ComponentName("com.whatsapp","com.whatsapp.Conversation"));
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("254740130001")+"@s.whatsapp.net");
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendIntent);
                } else {
                    getWhatsappFromPlayStore(FeedbackActivity.this);
                }
                break;

            default:
                break;

        }

    }

    /**
     * Initiate the actions encoded in the specified URI.
     */
    public void initiateSkypeUri(Context myContext, String mySkypeUri) {

        // Make sure the Skype for Android client is installed.
        if (!isSkypeClientInstalled(myContext)) {
            L.T(MyApplication.getAppContext(), "You don't have skype installed");
            goToMarket(myContext);
            return;
        }

        // Create the Intent from our Skype URI.
        Uri skypeUri = Uri.parse(mySkypeUri);
        Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);

        // Restrict the Intent to being handled by the Skype for Android client only.
        myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Initiate the Intent. It should never fail because you've already established the
        // presence of its handler (although there is an extremely minute window where that
        // handler can go away).
        myContext.startActivity(myIntent);

        return;
    }

    /**
     * Determine whether the Skype for Android client is installed on this device.
     *
     * @param myContext The requesting view
     * @return
     */
    public boolean isSkypeClientInstalled(Context myContext) {
        PackageManager myPackageMgr = myContext.getPackageManager();
        try {
            myPackageMgr.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            return (false);
        }
        return (true);
    }

    /**
     * Install the Skype client through the market: URI scheme.
     *
     * @param myContext
     */
    public void goToMarket(Context myContext) {
        try {
            Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
            Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myContext.startActivity(myIntent);
        }catch (ActivityNotFoundException e){
            L.t(MyApplication.getAppContext(),"Unable to get application from the market");

        }
    }

    // Get whatsapp from the market
    public void getWhatsappFromPlayStore(Context myContext) {
        try {
            Uri marketUri = Uri.parse("market://details?id=com.whatsapp");
            Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myContext.startActivity(myIntent);
        }catch (ActivityNotFoundException e){
            L.t(MyApplication.getAppContext(),"Unable to get application from the market");

        }

    }
}
