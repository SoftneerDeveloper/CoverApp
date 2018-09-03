package ke.co.coverapp.coverapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.squareup.picasso.Picasso;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.BalanceLoadedListener;
import ke.co.coverapp.coverapp.callbacks.PricesLoadedListener;
import ke.co.coverapp.coverapp.fragments.AddAssetsFragment;
import ke.co.coverapp.coverapp.fragments.AssetsFragmentTwo;
import ke.co.coverapp.coverapp.fragments.ClaimsFragment;
import ke.co.coverapp.coverapp.fragments.CoversFragment;
import ke.co.coverapp.coverapp.fragments.HomeFragment;
import ke.co.coverapp.coverapp.fragments.ProfileFragment;
import ke.co.coverapp.coverapp.fragments.SettingsFragment;
import ke.co.coverapp.coverapp.fragments.TopUpFragment;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.Balance;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.pojo.Prices;
import ke.co.coverapp.coverapp.tasks.TaskLoadBalance;
import ke.co.coverapp.coverapp.tasks.TaskLoadPrices;
import ke.co.coverapp.coverapp.utility.ValidationUtil;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, PricesLoadedListener , BalanceLoadedListener {
    private String TITLE = " ";
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab_share, fab_tips_alerts, fab_claims;;//fab_experience_cover
    FloatingActionsMenu multiple_actions;
    private  View mShadowView;

    TextView nav_name, nav_email;
    public static CircleImageView nav_prof_pic;
    String token, title_two; // User agent firebase token
    String fname = MyApplication.readFromPreferences(MyApplication.getAppContext(), FNAME, "CoverApp User");
    String lname = MyApplication.readFromPreferences(MyApplication.getAppContext(), LNAME, ValidationUtil.getDefaultString());
    public static String email = MyApplication.readFromPreferences(MyApplication.getAppContext(), EMAIL, "support@coverapp.co.ke");
    String prof_pic_url = MyApplication.readFromPreferences(MyApplication.getAppContext(), PROF_PIC_URL, UPLOADS_FOLDER+"logo.png");
    public static String fragment_id = "0";
    public static String EditClaimActivity = "0";
    public static String ReportClaim = "0";
    public static final String NullNotification="N/A";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), "TRANSITION_NAME");

        new TaskLoadPrices(this).execute();

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.colorAccent));
        collapsingToolbarLayout.setStatusBarScrimColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);

//        fab_experience_cover = (FloatingActionButton) findViewById(R.id.fab_experience_cover);
        fab_share = (FloatingActionButton) findViewById(R.id.fab_share);
        fab_tips_alerts = (FloatingActionButton) findViewById(R.id.fab_tips_alerts);
        fab_claims = (FloatingActionButton) findViewById(R.id.fab_claims);
        multiple_actions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);


//        fab_experience_cover.setOnClickListener(this);
        fab_share.setOnClickListener(this);
        fab_tips_alerts.setOnClickListener(this);
        fab_claims.setOnClickListener(this);
       // mShadowView= findViewById(R.id.shadowView);
        //mShadowView.setVisibility(View.VISIBLE);
//

        //Firebase
       // FirebaseMessaging.getInstance().subscribeToTopic("news");
      //  token = FirebaseInstanceId.getInstance().getToken();
      //  L.m("Firebase token " + token);

        if (savedInstanceState == null && getSupportActionBar() != null) {
            Intent in = getIntent();
            Bundle extras = in.getExtras();

            FragmentManager fm = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if (extras != null) {
                if (extras.containsKey(Keys.keys.IS_TOPUP)) {
                    if (extras.getBoolean(Keys.keys.IS_TOPUP, false)) {
                        ft.replace(R.id.content_frame, new TopUpFragment(), getString(R.string.fragment_tag_top_up));
                        ft.commit();
                        setActionBarTitle("My Wallet");
                    } else {
                        ft.replace(R.id.content_frame, new HomeFragment(), getString(R.string.fragment_tag_home));
                        ft.commit();
                        setActionBarTitle(getString(R.string.app_name));
                    }
                } else {
                    ft.replace(R.id.content_frame, new HomeFragment(), getString(R.string.fragment_tag_home));
                    ft.commit();
                    setActionBarTitle(getString(R.string.app_name));
                }
            } else {
                ft.replace(R.id.content_frame, new HomeFragment(), getString(R.string.fragment_tag_home));
                ft.commit();
                setActionBarTitle(getString(R.string.app_name));
            }

        } else if (savedInstanceState != null && getSupportActionBar() != null) {
            L.m("Hello" + savedInstanceState.getString(TITLE));
            setActionBarTitle(savedInstanceState.getString(TITLE));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        L.m("Profile Picture: " + prof_pic_url);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            View hView =  navigationView.getHeaderView(0);
            nav_prof_pic = (CircleImageView) hView.findViewById(R.id.nav_prof_pic);

            Picasso.with(MyApplication.getAppContext())
                    .load(prof_pic_url)
                    .placeholder(R.drawable.side_icon)
                    .error(R.drawable.side_icon)
                    .into(nav_prof_pic);

            nav_name = (TextView)hView.findViewById(R.id.nav_name);
            nav_name.setText("Hi "+fname+"!");
            nav_email = (TextView)hView.findViewById(R.id.nav_email);
            nav_email.setText(email);

        }

        //to load assets fragment
        if (fragment_id.toString().trim().equals("1")) {
            FragmentManager asset = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction assets = getSupportFragmentManager().beginTransaction();
            assets.replace(R.id.content_frame, new AssetsFragmentTwo(), getString(R.string.fragment_tag_top_up));
            assets.commit();
        }
        if (EditClaimActivity.toString().trim().equals("EditClaimActivity")) {
            FragmentManager asset = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction assets = getSupportFragmentManager().beginTransaction();
            assets.replace(R.id.content_frame, new ClaimsFragment(), getString(R.string.fragment_tag_top_up));
            assets.commit();
        }
        if (ReportClaim.toString().trim().equals("ReportClaim")) {
            FragmentManager asset = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction assets = getSupportFragmentManager().beginTransaction();
            assets.replace(R.id.content_frame, new ClaimsFragment(), getString(R.string.fragment_tag_top_up));
            assets.commit();
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
            createDialog();
            return true;
        }

        if (id == R.id.action_refresh) {
            L.t(MyApplication.getAppContext(), "Refreshing balance");
            new TaskLoadBalance(this).execute();
            return true;
        }

//        if (id == R.id.action_settings) {
//            displayView(R.id.nav_settings);
//            return true;
//        }

        if (id == R.id.action_notification) {
            startActivity(new Intent(MyApplication.getAppContext(), NotificationActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_claims:
                displayView(R.id.claim);
                break;

            case R.id.fab_share:
                shareAction();
                break;

            case R.id.fab_tips_alerts:
                startActivity(new Intent(MyApplication.getAppContext(), TipsAlertsActivity.class));
                break;
        }
    }

    public void setActionBarTitle(String actionTitle) {
        if (getSupportActionBar() != null) {
            L.m(actionTitle);
            collapsingToolbarLayout.setTitle(actionTitle);
        }
    }

    private void shareAction() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "No more paper work, great prices, insure the things that matter most to you - https://play.google.com/store/apps/details?id=ke.co.coverapp.coverapp&hl=en";
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Try coverApp");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        try {

            outState.putString(TITLE, collapsingToolbarLayout.getTitle().toString());

        } catch (NullPointerException ex) {
            L.m("Title threw an exception");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            displayView(R.id.nav_home);

        } else if (id == R.id.nav_profile) {

            displayView(R.id.nav_profile);

//        } else if (id == R.id.nav_assets) {
//
//            displayView(R.id.nav_assets);

        } else if (id == R.id.nav_topup) {

            displayView(R.id.nav_topup);
        } else if (id == R.id.nav_settings) {

            displayView(R.id.nav_settings);

        } else if (id == R.id.nav_share) {

            shareAction();

        } else if (id == R.id.nav_help) {

            startActivity(new Intent(MyApplication.getAppContext(), FeedbackActivity.class));

        } else if (id == R.id.nav_terms) {

            //startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.site_url) + "/terms-and-conditions")));

            FragmentManager manager = getSupportFragmentManager();
            TermsAndConditions dialog = new TermsAndConditions();
            dialog.show(manager, "Message");

        } else if (id == R.id.nav_privacy) {

            //startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.site_url) + "/privacy-policy")));

            FragmentManager manager = getSupportFragmentManager();
            PrivacyPolicy dialog = new PrivacyPolicy();
            dialog.show(manager, "Message");

        } else if (id == R.id.nav_about) {

            //startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.site_url) + "/about-us")));

            FragmentManager manager = getSupportFragmentManager();
            AboutUs dialog = new AboutUs();
            dialog.show(manager, "Message");

//        } else if (id == R.id.nav_claims) {
//
//            //multiple_actions.setVisibility(View.GONE);
//
//            displayView(R.id.nav_claims);

        } else if (id == R.id.nav_covers) {

            displayView(R.id.nav_covers);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Are you sure you want to logout?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                logout();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void logout() {
        // Clear database
        MyApplication.getWritableDatabase().deleteAll();
        MyApplication.saveToPreferences(MyApplication.getAppContext(), Keys.keys.SKIP_GETTING_STARTED, false);
        MyApplication.saveToPreferences(MyApplication.getAppContext(), Keys.keys.SKIP_LOGIN, false);

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void displayView(int viewId) {

        Fragment fragment = null;
        String title = "CoverApp";
        String tag = "";

        switch (viewId) {
//            case R.id.nav_assets:
//                fragment = new AssetsFragment();
//                title = "My Assets";
//                tag = getString(R.string.fragment_tag_assets);
//
//                break;

//            case R.id.nav_assets:
//                fragment = new AssetsFragmentTwo();
//                //title = "My Assets";
//                title = getString(R.string.title_my_assets);
//                tag = getString(R.string.fragment_tag_assets);
//                multiple_actions.setVisibility(View.VISIBLE);
//                break;

            case R.id.nav_home:
                fragment = new HomeFragment();
                title = getString(R.string.app_name);
                tag = getString(R.string.fragment_tag_home);//-----------to change--------------
                multiple_actions.setVisibility(View.VISIBLE);
                break;

            case R.id.button_to_add_asset:
                fragment = new AddAssetsFragment();
                title = getString(R.string.title_add_assets);
                tag = getString(R.string.fragment_tag_add_assets);

                break;

//            case R.id.button_to_edit_asset:
//                fragment = new EditAssetsFragment();
//                title = getString(R.string.title_edit_assets);
//                tag = getString(R.string.fragment_tag_edit_assets);
//
//                break;

            case R.id.nav_topup:
                fragment = new TopUpFragment();
                title = "My Wallet";
                tag = getString(R.string.fragment_tag_top_up);
                multiple_actions.setVisibility(View.VISIBLE);
                break;


            case R.id.nav_profile:
                fragment = new ProfileFragment();
                title = "My Profile";
                tag = "ProfileFragment";
                multiple_actions.setVisibility(View.VISIBLE);
                break;

//            case R.id.nav_claims:
//                fragment = new ClaimsFragment();
//                title = getString(R.string.title_claims);
//                tag = getString(R.string.fragment_tag_claims);
//                multiple_actions.setVisibility(View.GONE);
//                break;

            case R.id.nav_covers:
                fragment = new CoversFragment();
                title = "Covers";
                tag = "CoversFragment";
                multiple_actions.setVisibility(View.GONE);
                break;

            case R.id.nav_settings:
                fragment = new SettingsFragment();
                title = getString(R.string.title_settings);
                tag = getString(R.string.fragment_tag_settings);
                multiple_actions.setVisibility(View.VISIBLE);
                break;
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (currentFragment.getTag().equals(tag)) {

            if (drawer != null) {
                drawer.closeDrawer(GravityCompat.START);
            }
        } else {

            if (fragment != null) {

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                ft.replace(R.id.content_frame, fragment, tag);
                ft.addToBackStack(tag);
                ft.commit();

                setActionBarTitle(title);
            } else {
                L.T(MyApplication.getAppContext(), getString(R.string.under_maintenance));
            }
        }

        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * Called when the prices have been successfully loaded from the API
     *
     * @param listPrices
     */
    @Override
    public void onPricesLoaded(ArrayList<Prices> listPrices) {
//        L.t(MyApplication.getAppContext(), listPrices.toString());
    }

    /**
     * Called when the account balance has been loaded
     *
     * @param balanceData the balance in a single element arraylist
     */
    @Override
    public void onBalanceLoaded(ArrayList<Balance> balanceData) {
        if (balanceData.size() > 0) {

            Balance currentBalance = balanceData.get(0);
            MyApplication.saveToPreferences(MyApplication.getAppContext(), WALLET_BALANCE, currentBalance.getBalance());
            L.t(MyApplication.getAppContext(), "Balance updated successfully");
        }
    }

}
