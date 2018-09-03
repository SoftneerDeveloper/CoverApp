package ke.co.coverapp.coverapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.rilixtech.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ke.co.coverapp.coverapp.BuildConfig;
import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.PricesLoadedListener;
import ke.co.coverapp.coverapp.log.L;

import static ke.co.coverapp.coverapp.pojo.Keys.keys;

import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Prices;
import ke.co.coverapp.coverapp.tasks.TaskLoadPrices;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener , PricesLoadedListener {

    LinearLayout login_name_layout, terms_layout,register_layout,login_layout,login_layout_phone,layout_sms_code;
    TextInputLayout login_layout_id,login_layout_email2, login_layout_fname, login_layout_lname, login_layout_email,login_layout_password2, login_layout_password, login_layout_password_repeat;
    TextInputEditText login_id,sms_code_edt, login_fname, login_lname,login_email2, login_email, login_password,login_password2, login_password_repeat;
    CheckBox check_terms_conditions;
    TextView text_terms_conditions, divider,message,message2;
    Button btncontinue,regbutton2, regbutton3, regbutton4,regbutton5, verify,regbackbutton, regbackbutton2, regbackbutton3, regbackbutton4, regbackbutton5, regbackbutton6;
    AppCompatButton login_to_login_view, login_to_login_view_pass, login_but_signup, login_but_login, login_to_forgot_pass_view, login_to_signup_view, login_but_reset_pass;
    private ProgressDialog mProgressDialog;
    String email = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.EMAIL, ValidationUtil.getDefault());
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    private int RC_SIGN_IN;
//    public static final int RC_SIGN_IN = 001;
    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton loginButtonFacebook;
    ImageView imgSignGoogle;
    public static final String NullNotification="N/A";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private AppCompatEditText login_phone;
    CountryCodePicker ccp;

    private String verificationid,codeField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //intialized firebase auth
        mAuth = FirebaseAuth.getInstance();
        //facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        login_layout_fname = (TextInputLayout) findViewById(R.id.login_layout_fname);
        login_layout_lname = (TextInputLayout) findViewById(R.id.login_layout_lname);
        login_layout_email = (TextInputLayout) findViewById(R.id.login_layout_email);
        login_layout_email2 = (TextInputLayout) findViewById(R.id.login_layout_email2);
        login_layout_phone = (LinearLayout) findViewById(R.id.login_layout_phone);
        layout_sms_code = (LinearLayout) findViewById(R.id.layout_sms_code);
        login_layout_id = (TextInputLayout) findViewById(R.id.login_layout_id);
        login_layout_password = (TextInputLayout) findViewById(R.id.login_layout_password);
        login_layout_password2 = (TextInputLayout) findViewById(R.id.login_layout_password2);
        login_layout_password_repeat = (TextInputLayout) findViewById(R.id.login_layout_password_repeat);

        login_fname = (TextInputEditText) findViewById(R.id.login_fname);
        login_lname = (TextInputEditText) findViewById(R.id.login_lname);
        login_email = (TextInputEditText) findViewById(R.id.login_email);
        login_email2 = (TextInputEditText) findViewById(R.id.login_email2);
        sms_code_edt = (TextInputEditText) findViewById(R.id.sms_code_edt);
        login_phone = (AppCompatEditText) findViewById(R.id.login_phone);
        login_id = (TextInputEditText) findViewById(R.id.login_id);
        login_password2 = (TextInputEditText) findViewById(R.id.login_password2);
        login_password = (TextInputEditText) findViewById(R.id.login_password);
        login_password_repeat = (TextInputEditText) findViewById(R.id.login_password_repeat);
        message=(TextView) findViewById(R.id.message);
        message2=(TextView) findViewById(R.id.message2);
        if (!email.matches(ValidationUtil.getDefault())) {
            login_email.setText(email);
        }
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
         ccp.registerPhoneNumberTextView(login_phone);
         //ccp.isHintEnabled(false);
        login_to_login_view = (AppCompatButton) findViewById(R.id.login_to_login_view);
        login_but_signup = (AppCompatButton) findViewById(R.id.login_but_signup);
        login_to_login_view_pass = (AppCompatButton) findViewById(R.id.login_to_login_view_pass);
        login_but_login = (AppCompatButton) findViewById(R.id.login_but_login);
        login_to_forgot_pass_view = (AppCompatButton) findViewById(R.id.login_to_forgot_pass_view);
        login_to_signup_view = (AppCompatButton) findViewById(R.id.login_to_signup_view);
        login_but_reset_pass = (AppCompatButton) findViewById(R.id.login_but_reset_pass);

        btncontinue=(Button)findViewById(R.id.btncontinue);
        regbutton2=(Button)findViewById(R.id.regbutton2);
        regbutton3=(Button)findViewById(R.id.regbutton3);
        regbutton4=(Button)findViewById(R.id.regbutton4);
        regbutton5=(Button)findViewById(R.id.regbutton5);
        verify=(Button)findViewById(R.id.verify);


        regbackbutton2=(Button)findViewById(R.id.regbackbutton2);
        regbackbutton=(Button)findViewById(R.id.regbackbutton);
        regbackbutton3=(Button)findViewById(R.id.regbackbutton3);
        regbackbutton4=(Button)findViewById(R.id.regbackbutton4);
        regbackbutton5=(Button)findViewById(R.id.regbackbutton5);
        regbackbutton6=(Button)findViewById(R.id.regbackbutton6);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);

        login_to_login_view.setOnClickListener(this);
        login_but_signup.setOnClickListener(this);
        login_to_login_view_pass.setOnClickListener(this);
        login_but_login.setOnClickListener(this);
        login_to_forgot_pass_view.setOnClickListener(this);
        login_to_signup_view.setOnClickListener(this);
        login_but_reset_pass.setOnClickListener(this);

        btncontinue.setOnClickListener(this);
        regbutton2.setOnClickListener(this);
        regbutton3.setOnClickListener(this);
        regbutton4.setOnClickListener(this);
        regbutton5.setOnClickListener(this);
        verify.setOnClickListener(this);

        regbackbutton2.setOnClickListener(this);
        regbackbutton.setOnClickListener(this);
        regbackbutton3.setOnClickListener(this);
        regbackbutton4.setOnClickListener(this);
        regbackbutton5.setOnClickListener(this);
        regbackbutton6.setOnClickListener(this);

        terms_layout = (LinearLayout) findViewById(R.id.terms_layout);
        login_layout = (LinearLayout) findViewById(R.id.login_layout);
        register_layout = (LinearLayout) findViewById(R.id.register_layout);
        login_name_layout = (LinearLayout) findViewById(R.id.login_name_layout);

        check_terms_conditions = (CheckBox) findViewById(R.id.check_terms_conditions);

        if (check_terms_conditions != null) {
            check_terms_conditions.setChecked(false);
            check_terms_conditions.setOnClickListener(this);
        }
        text_terms_conditions = (TextView) findViewById(R.id.text_terms_conditions);
        divider = (TextView) findViewById(R.id.divider);
        text_terms_conditions.setText(Html.fromHtml("  <i>Terms and conditions</i>"));

        if (text_terms_conditions != null) {
            text_terms_conditions.setOnClickListener(this);
        }


        //Firebase global notification for all users
        //FirebaseMessaging.getInstance().subscribeToTopic("news");

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.defaults);
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        /*   cacheExpirationSeconds is set to cacheExpiration here, indicating that any previously
             fetched and cached config would be considered expired because it would have been fetched
             more than cacheExpiration seconds ago. Thus the next fetch would go to the server unless
             throttling is in progress. The default expiration duration is 43200 (12 hours).  */
        mFirebaseRemoteConfig.fetch(keys.REMOTE_CONFIG_3HRS)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.

                            mFirebaseRemoteConfig.activateFetched();
                            L.m("login " + mFirebaseRemoteConfig.getString("has_roadside"));
                        }
                    }
                });
        //Facebook Login
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //nextActivity(newProfile);
                L.m("onCurrentProfileChanged --Move to next");
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        loginButtonFacebook = (LoginButton) findViewById(R.id.login_button);
        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                //nextActivity(profile);
//                L.m(profile.toString());
                L.m("onSuccess 2nd --Move to next");
                L.t(getApplicationContext(), "Logging in...");
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        };
        loginButtonFacebook.setReadPermissions("user_friends");
        loginButtonFacebook.registerCallback(callbackManager, callback);
        //end of Facebook Login
        AppEventsLogger.activateApp(this);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
// Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, onConnectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

//        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
//        signInButton.setSize(SignInButton.SIZE_STANDARD);
//        signInButton.setScopes(gso.getScopeArray());

        imgSignGoogle = (ImageView) findViewById(R.id.imgSignGoogle);
        imgSignGoogle.setOnClickListener(this);

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        L.m("handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            L.t(MyApplication.getAppContext(), "" + acct.getDisplayName());
            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    //Facebook login button
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
            //nextActivity(profile);
            L.m("onSuccess --Move to next");
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException e) {
        }
    };


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.text_terms_conditions:
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.site_url) + "/terms")));
                break;

            case R.id.login_to_login_view:
                toLoginView();
                break;

            case R.id.login_but_signup:

//                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent);
                if (validateField(login_layout_fname, login_fname, 3, getString(R.string.invalid_fname))
                        && validateField(login_layout_lname, login_lname, 3, getString(R.string.invalid_lname))
                        && validatePhoneField()
                        && validateField(login_layout_password2, login_password2, 4, getString(R.string.invalid_password))
                        && validateField(login_layout_password, login_password, 4, getString(R.string.invalid_password))
                        && validateField(login_layout_password_repeat, login_password_repeat, 3, getString(R.string.invalid_password))
                        && validatePasswordMatch()
                        && validateField(login_layout_id, login_id, 6, getString(R.string.invalid_id_no))
                        && validateEmailField()) {
                    onRegister(v);
                }
                break;

            case R.id.login_to_login_view_pass:
                toLoginView();
                break;

            case R.id.login_but_login:
                if (validateEmailField2() && validateField(login_layout_password2, login_password2, 4, getString(R.string.invalid_password))) {
                    networkLogin();
                }

                //startActivity(new Intent(MyApplication.getAppContext(), MainActivity.class));

                break;

            case R.id.login_to_forgot_pass_view:
                toForgotPass();
                break;

            case R.id.login_to_signup_view:
                toSignUpView();
                break;

            case R.id.login_but_reset_pass:
                if (validateEmailField()) {
                    forgotPasswordReq();
                }
                break;
            //sign in with google on click
            case R.id.imgSignGoogle:
                signIn();
                break;

            case R.id.regbutton3://hide fname and lname and show email

                if (validateField(login_layout_fname, login_fname, 3, getString(R.string.invalid_fname)) && validateField(login_layout_lname, login_lname, 3, getString(R.string.invalid_lname)))
            {

                //hide
                btncontinue.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
//                login_layout_fname.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
//                login_layout_lname.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
                btncontinue.setVisibility(View.GONE);
                regbutton3.setVisibility(View.GONE);
                regbutton3.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));

                login_name_layout.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
                login_name_layout.setVisibility(View.GONE);
//                login_layout_fname.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
//                login_layout_fname.setVisibility(View.GONE);

                //show
                login_layout_email.setErrorEnabled(false);
                login_layout_email.setVisibility(View.VISIBLE);
                login_layout_email.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
                regbutton4.setVisibility(View.VISIBLE);
                regbutton4.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
                regbackbutton3.setVisibility(View.VISIBLE);
                regbackbutton3.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
            }
                break;

            case R.id.regbutton4://hide email and show phone

                if (validateEmailField()) {
                    //hide
                    regbutton4.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
                    regbutton4.setVisibility(View.GONE);
                    regbackbutton3.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
                    regbackbutton3.setVisibility(View.GONE);
                    login_layout_email.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
                    login_layout_email.setVisibility(View.GONE);

                    //show
                    login_layout_id.setVisibility(View.VISIBLE);
//                    login_phone.setErrorEnabled(false);
                    login_layout_id.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
                    regbutton5.setVisibility(View.VISIBLE);
                    regbutton5.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
                    regbackbutton4.setVisibility(View.VISIBLE);
                    regbackbutton4.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
                }

                break;

            case R.id.btncontinue://hide phone and show id number
//                if (!validatePhoneNumberAndCode()) {
//                    return;
//                }


                if (validatePhoneField() ) {
                    Toast.makeText(LoginActivity.this,ccp.getFullNumberWithPlus(),Toast.LENGTH_LONG).show();
                    startPhoneNumberVerification(ccp.getFullNumberWithPlus());
                    //hide
                    //login_layout_phone.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
                    login_layout_phone.setVisibility(View.GONE);
                   // regbutton4.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
                    regbutton4.setVisibility(View.GONE);
                 //   regbackbutton3.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
                    regbackbutton3.setVisibility(View.GONE);

                    //show
                    //login_layout_id.setVisibility(View.VISIBLE);
                    //login_layout_id.setErrorEnabled(false);
                    //login_layout_id.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));


                }
break;
            case R.id.regbutton2://hide phone and show id number

                if (validatePhoneField() ) {
//                    Toast.makeText(LoginActivity.this,ccp.getFullNumberWithPlus(),Toast.LENGTH_LONG).show();
//                    startPhoneNumberVerification(ccp.getFullNumberWithPlus());
                    //hide
                    //login_layout_phone.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
                    login_layout_phone.setVisibility(View.GONE);
                    // regbutton4.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
                    regbutton4.setVisibility(View.GONE);
                    //   regbackbutton3.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
                    regbackbutton3.setVisibility(View.GONE);

                    //show
                    //login_layout_id.setVisibility(View.VISIBLE);
                    //login_layout_id.setErrorEnabled(false);
                    //login_layout_id.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));


                }

            break;
            case  R.id.verify:
                if (!validateSMSCode()) {
                    return;
                }

                verifyPhoneNumberWithCode(verificationid, sms_code_edt.getText().toString());
                break;
            case R.id.regbutton5://hide id number and show passwords and terms and complete registration

                if (validateField(login_layout_id, login_id, 6, getString(R.string.invalid_id_no)) ) {
                    //hide
                    login_layout_id.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
                    login_layout_id.setVisibility(View.GONE);
                    regbutton5.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
                    regbutton5.setVisibility(View.GONE);
                    regbackbutton4.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
                    regbackbutton4.setVisibility(View.GONE);

                    //show
                    login_layout_password.setVisibility(View.VISIBLE);
                    login_layout_password.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
                    login_layout_password.setErrorEnabled(false);
                    login_layout_password_repeat.setVisibility(View.VISIBLE);
                    login_layout_password_repeat.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
                    login_layout_password_repeat.setErrorEnabled(false);
                    login_but_signup.setVisibility(View.VISIBLE);
                    login_but_signup.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
                    regbackbutton5.setVisibility(View.VISIBLE);
                    regbackbutton5.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
                    terms_layout.setVisibility(View.VISIBLE);
                    terms_layout.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
                }
                break;
            case R.id.regbackbutton://hide email view and show fname and lname

                //hide
                 regbutton3.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
                regbutton3.setVisibility(View.GONE);
                regbackbutton.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
                regbackbutton.setVisibility(View.GONE);
                login_name_layout.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
                login_name_layout.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);

                //show
                //login_to_login_view.setVisibility(View.VISIBLE);
                //login_to_login_view_pass.setVisibility(View.GONE);
                login_but_login.setVisibility(View.GONE);
                login_to_forgot_pass_view.setVisibility(View.GONE);
                login_to_signup_view.setVisibility(View.GONE);
                login_but_reset_pass.setVisibility(View.GONE);


                login_layout_fname.setErrorEnabled(false);
                btncontinue.setVisibility(View.VISIBLE);
                btncontinue.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_left));
                login_layout_phone.setVisibility(View.VISIBLE);
                login_layout_phone.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_left));

                break;
//            case R.id.regbackbutton2://hide email view and show fname and lname
//
//            //hide
//                regbutton3.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
//                regbutton3.setVisibility(View.GONE);
//                regbackbutton2.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
//                regbackbutton2.setVisibility(View.GONE);
//                login_layout_email.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
//                login_layout_email.setVisibility(View.GONE);
//
//
////                regbackbutton.setVisibility(View.VISIBLE);
////                regbackbutton.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_left));
//                regbutton3.setVisibility(View.VISIBLE);
//                regbutton3.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
//                login_name_layout.setVisibility(View.VISIBLE);
//                login_name_layout.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_left));
//
//                break;

            case R.id.regbackbutton3://hide phone and show email

                //hide
                login_layout_email.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
                login_layout_email.setVisibility(View.GONE);
                regbutton4.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
                regbutton4.setVisibility(View.GONE);
                regbackbutton3.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
                regbackbutton3.setVisibility(View.GONE);

                //show
                //login_layout_email.setErrorEnabled(false);
                login_name_layout.setVisibility(View.VISIBLE);
                login_name_layout.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_left));
                regbutton3.setVisibility(View.VISIBLE);
                regbutton3.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_left));
//                regbackbutton2.setVisibility(View.VISIBLE);
//                regbackbutton2.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_left));

                break;

            case R.id.regbackbutton4://hide id number and show phone

                //hide
                login_layout_id.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
                login_layout_id.setVisibility(View.GONE);
                regbutton5.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
                regbutton5.setVisibility(View.GONE);
                regbackbutton4.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
                regbackbutton4.setVisibility(View.GONE);

                //show
                login_layout_email.setVisibility(View.VISIBLE);
//                login_phone.setErrorEnabled(false);
                login_layout_email.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_left));
                regbutton4.setVisibility(View.VISIBLE);
                regbutton4.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_left));
                regbackbutton3.setVisibility(View.VISIBLE);
                regbackbutton3.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_left));

                break;

            case R.id.regbackbutton5://hide passwords and show idnumber

                //hide
                login_layout_password.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
                login_layout_password.setVisibility(View.GONE);
                login_layout_password_repeat.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
                login_layout_password_repeat.setVisibility(View.GONE);
                login_but_signup.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
                login_but_signup.setVisibility(View.GONE);
                regbackbutton6.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
                regbackbutton6.setVisibility(View.GONE);
                terms_layout.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
                terms_layout.setVisibility(View.GONE);
                regbackbutton5.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_right));
                regbackbutton5.setVisibility(View.GONE);

                //show
                login_layout_id.setVisibility(View.VISIBLE);
                login_layout_id.setErrorEnabled(false);
                login_layout_id.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_left));
                regbutton5.setVisibility(View.VISIBLE);
                regbutton5.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_left));
                regbackbutton4.setVisibility(View.VISIBLE);
                regbackbutton4.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_left));
                login_id.setVisibility(View.VISIBLE);
                login_id.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_left));

                break;

        }

    }

    private void toLoginView() {
login_layout.setVisibility(View.VISIBLE);
register_layout.setVisibility(View.GONE);
        login_to_login_view.setVisibility(View.GONE);
     login_but_signup.setVisibility(View.GONE);
//            login_to_login_view_pass.setVisibility(View.GONE);
////        login_but_login.setVisibility(View.VISIBLE);
////        login_to_forgot_pass_view.setVisibility(View.VISIBLE);
////        login_to_signup_view.setVisibility(View.VISIBLE);
////        login_but_reset_pass.setVisibility(View.GONE);
////        terms_layout.setVisibility(View.GONE);
////        login_name_layout.setVisibility(View.GONE);
////        login_id.setVisibility(View.GONE);
////        divider.setVisibility(View.GONE);
//
//
//        loginButtonFacebook.setVisibility(View.GONE);
//        imgSignGoogle.setVisibility(View.GONE);
//
        //login_layout_email.setErrorEnabled(false);
        login_name_layout.setVisibility(View.GONE);

        login_layout_email.setVisibility(View.GONE);
        login_layout_phone.setVisibility(View.GONE);
//        login_layout_phone.setErrorEnabled(false);
        login_layout_password.setVisibility(View.GONE);
        login_layout_password.setErrorEnabled(false);
        login_layout_password_repeat.setVisibility(View.GONE);
        login_layout_password_repeat.setErrorEnabled(false);
        login_layout_fname.setErrorEnabled(false);
        login_layout_lname.setErrorEnabled(false);
        login_layout_id.setVisibility(View.GONE);
        login_layout_id.setErrorEnabled(false);
//
       btncontinue.setVisibility(View.GONE);
        regbutton3.setVisibility(View.GONE);
        regbutton2.setVisibility(View.GONE);
        regbutton4.setVisibility(View.GONE);
        regbutton5.setVisibility(View.GONE);

        regbackbutton2.setVisibility(View.GONE);
        regbackbutton3.setVisibility(View.GONE);
        regbackbutton4.setVisibility(View.GONE);
        regbackbutton5.setVisibility(View.GONE);
        regbackbutton6.setVisibility(View.GONE);
    }

    private void toSignUpView() {
        login_layout.setVisibility(View.GONE);
        login_layout.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
        register_layout.setVisibility(View.VISIBLE);
//        login_but_signup.setVisibility(View.VISIBLE);
        login_to_login_view.setVisibility(View.VISIBLE);
//        login_but_login.setVisibility(View.GONE);
//        login_to_forgot_pass_view.setVisibility(View.GONE);
//        login_to_signup_view.setVisibility(View.GONE);
//        login_but_reset_pass.setVisibility(View.GONE);
//        //terms_layout.setVisibility(View.VISIBLE);
//        login_name_layout.setVisibility(View.VISIBLE);
//        login_id.setVisibility(View.VISIBLE);
//        divider.setVisibility(View.VISIBLE);
//
//         loginButtonFacebook.setVisibility(View.VISIBLE);
//         imgSignGoogle.setVisibility(View.VISIBLE);
//
//        login_layout_email.setErrorEnabled(false);
//        login_layout_email.setVisibility(View.VISIBLE);
//        login_layout_email.setErrorEnabled(false);
////        login_layout_email.setVisibility(View.GONE);
//
////        login_layout_password.setVisibility(View.GONE);
////        login_layout_password_repeat.setVisibility(View.GONE);
        //login_layout_phone.setVisibility(View.VISIBLE);
        //login_layout_phone.setErrorEnabled(false);
        //login_layout_password.setVisibility(View.VISIBLE);
        //login_layout_password.setErrorEnabled(false);
        //login_layout_password_repeat.setVisibility(View.VISIBLE);
        //login_layout_password_repeat.setErrorEnabled(false);
        //login_layout_fname.setErrorEnabled(false);
        btncontinue.setVisibility(View.VISIBLE);
        btncontinue.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
        login_layout_phone.setVisibility(View.VISIBLE);
        login_layout_phone.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
//        login_layout_lname.setVisibility(View.VISIBLE);
//        login_layout_lname.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
//        login_layout_lname.setErrorEnabled(false);

       //login_layout_id.setVisibility(View.VISIBLE);
        //login_layout_id.setErrorEnabled(false);
    }

    private void toForgotPass() {
        login_to_login_view.setVisibility(View.GONE);
        login_but_signup.setVisibility(View.GONE);
        login_to_login_view_pass.setVisibility(View.VISIBLE);
        login_but_login.setVisibility(View.GONE);
        login_to_forgot_pass_view.setVisibility(View.GONE);
        login_to_signup_view.setVisibility(View.GONE);
        login_but_reset_pass.setVisibility(View.VISIBLE);
        terms_layout.setVisibility(View.GONE);
        login_name_layout.setVisibility(View.GONE);
        login_id.setVisibility(View.GONE);
        divider.setVisibility(View.GONE);

        loginButtonFacebook.setVisibility(View.GONE);
        imgSignGoogle.setVisibility(View.GONE);

        login_layout_email.setErrorEnabled(false);
        login_layout_email.setVisibility(View.VISIBLE);
        login_layout_phone.setVisibility(View.GONE);
//        login_layout_phone.setErrorEnabled(false);
        login_layout_password.setVisibility(View.GONE);
        login_layout_password.setErrorEnabled(false);
        login_layout_password_repeat.setVisibility(View.GONE);
        login_layout_password_repeat.setErrorEnabled(false);
        login_layout_fname.setErrorEnabled(false);
        login_layout_lname.setErrorEnabled(false);
        login_layout_id.setVisibility(View.GONE);
        login_layout_id.setErrorEnabled(false);
    }

    private void networkLogin() {
        mProgressDialog.setMessage(getString(R.string.progress_login));
        mProgressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, getString(R.string.baseUrl) + "/validate", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressDialog.dismiss();
                if (ParseUtil.parseLoginJSON(response)) {
                    L.m("Hello. Login successful!");


                   // login_email = (TextInputEditText) findViewById(R.id.login_email);
                    //String email = login_email.getText().toString();
                    //SharedPreferences sharedPreferences1 = getSharedPreferences("notificationz", MODE_PRIVATE);
                    //SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    //editor1.putString("email_id", email);
                    //editor1.commit();

                    //update the preferences to true here
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.SKIP_GETTING_STARTED, true);
                            MyApplication.saveToPreferences(MyApplication.getAppContext(), keys.SKIP_LOGIN, true);


                    startActivity(new Intent(MyApplication.getAppContext(), MainActivity.class));
                    finish();
//                    startActivity(new Intent(MyApplication.getAppContext(), IntroActivity.class));
                    new TaskLoadPrices(LoginActivity.this).execute();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.m("Login error");
                mProgressDialog.hide();
                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    //parsing the error
                    String json = "";
                    JSONObject obj;
                    switch (response.statusCode) {
                        case 400:
                            json = new String(response.data);
                            L.m(json);

                            try {
                                obj = new JSONObject(json);

                                if (ParseUtil.contains(obj, keys.ERROR)) {
                                    JSONObject getData = new JSONObject(obj.getString(keys.ERROR));

                                    if (ParseUtil.contains(getData, "message")) {
                                        L.t(MyApplication.getAppContext(),getData.getString("message"));
                                    }else{
                                        L.t(MyApplication.getAppContext(),"Invalid credentials");
                                    }
                                } else {
                                    L.t(MyApplication.getAppContext(), "Invalid credentials, check and retry");
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
                    VolleyCustomErrorHandler.errorMessage(error);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", login_email2.getText().toString().trim());
                parameters.put("password", login_password2.getText().toString().trim());
                return parameters;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                keys.MY_SOCKET_TIMEOUT_MS,
                keys.MY_DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(request);

    }

    private void forgotPasswordReq() {

        mProgressDialog.setMessage("Resetting Password...");
        mProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, getString(R.string.baseUrl) + "/forgotpassword ", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.m(response);

                JSONObject resetJson = null;
                try {
                    resetJson = new JSONObject(response);


                    if (ParseUtil.contains(resetJson, "text")) {

                        L.t(MyApplication.getAppContext(), resetJson.getString("text"));

                    } else {

                        L.t(MyApplication.getAppContext(), "Your new password has been sent to you via email");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //toggle view to login view and show the login view
                toLoginView();
                login_password.setVisibility(View.VISIBLE);
                login_but_reset_pass.setVisibility(View.GONE);
//                rememberedPassword.setVisibility(View.GONE);
                mProgressDialog.hide();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.hide();

                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    //parsing the error
                    String json = "";
                    JSONObject obj;
                    switch (response.statusCode) {
                        case 400:
                            json = new String(response.data);
                            L.m(json);

                            try {
                                obj = new JSONObject(json);

                                if (ParseUtil.contains(obj, keys.ERROR)) {
                                    L.t(MyApplication.getAppContext(), obj.getString(keys.ERROR));
                                } else {
                                    L.t(MyApplication.getAppContext(), "No such user, check and retry");
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
                    VolleyCustomErrorHandler.errorMessage(error);
                }

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", login_email.getText().toString().trim());
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                keys.MY_SOCKET_TIMEOUT_MS,
                keys.MY_DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(request);
    }

    private void onRegister(View view) {

        if (!check_terms_conditions.isChecked()) {

            L.t(MyApplication.getAppContext(), getString(R.string.terms_accept_reminder));

        } else {

            mProgressDialog.setMessage(getString(R.string.progress_registration));
            mProgressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, getString(R.string.baseUrl) + "/signup", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    L.m(response);

                    Toast.makeText(LoginActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                    //JSONObject encryptData = null;

//                    try {
//                        encryptData = new JSONObject(response);
//
//                        if (ParseUtil.contains(encryptData, "hash")) {
                    //L.m(response);

                    JSONObject registerJson = null;
                    try {
                        registerJson = new JSONObject(response);


                        if (ParseUtil.contains(registerJson, keys.DATA)) {
                            L.t(MyApplication.getAppContext(), registerJson.getString(keys.DATA));
                        }

                        else {
                            L.t(MyApplication.getAppContext(), getString(R.string.success_registration));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    toLoginView();
                    mProgressDialog.hide();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgressDialog.hide();


                    NetworkResponse response = error.networkResponse;
                    L.m(new String(response.data));

                    if (response != null && response.data != null) {
                        //parsing the error
                        String json = "";
                        JSONObject obj;

                        switch (response.statusCode) {
                            case 400:
                                json = new String(response.data);

                                L.m(json);



                                try {
                                    obj = new JSONObject(json);

                                   JSONArray jArray3 = obj.getJSONArray("error");
                                    for(int i = 0; i < jArray3 .length(); i++)
                                    {
                                        JSONObject object3 = jArray3.getJSONObject(i);
                                        String comp_id = object3.getString("name");
                                        String username = object3.getString("text");
                                        Toast.makeText(LoginActivity.this, ""+username, Toast.LENGTH_SHORT).show();

                                    }

                                    if (ParseUtil.contains(obj, keys.ERROR)) {
                                        L.t(MyApplication.getAppContext(), obj.getString("text"));
                                    }else {
                                       L.t(MyApplication.getAppContext(), "Registration failed, please retry");
                                    }

                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                VolleyCustomErrorHandler.errorMessage(error);
                                break;
                        }
                    } else {
                        VolleyCustomErrorHandler.errorMessage(error);
                    }

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("password", login_password.getText().toString().trim());
                    parameters.put("phone", "254" + ValidationUtil.validPhoneNumber(login_phone));
                    parameters.put("fname", login_fname.getText().toString().trim());
                    parameters.put("email", login_email.getText().toString().trim());
                    parameters.put("id_no", login_id.getText().toString().trim());
                    parameters.put("lname", login_lname.getText().toString().trim());
                    return parameters;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(
                    keys.MY_SOCKET_TIMEOUT_MS,
                    keys.MY_DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance().getRequestQueue().add(request);
        }
    }

    private boolean validatePasswordMatch() {

        if (!ValidationUtil.textMatch(login_password, login_password_repeat)) {

            login_layout_password_repeat.setErrorEnabled(true);
            login_layout_password_repeat.setError(getString(R.string.invalid_pass_match));

            return false;
        }
        login_layout_password_repeat.setErrorEnabled(false);
        return true;
    }

    private boolean validateEmailField() {
        if (!ValidationUtil.hasValidContents(login_email) || !ValidationUtil.hasValidEmail(login_email)) {
            login_layout_email.setErrorEnabled(true);
            login_layout_email.setError(getString(R.string.invalid_email));
            requestFocus(login_email);

            return false;
        }
        login_layout_email.setErrorEnabled(false);
        return true;
    }
    private boolean validateEmailField2() {
        if (!ValidationUtil.hasValidContents(login_email2) || !ValidationUtil.hasValidEmail(login_email2)) {
            login_layout_email2.setErrorEnabled(true);
            login_layout_email2.setError(getString(R.string.invalid_email));
            requestFocus(login_email2);

            return false;
        }
        login_layout_email2.setErrorEnabled(false);
        return true;
    }

    private boolean validatePhoneField() {
        if (!ValidationUtil.isValidPhoneNumber(login_phone)) {
//            login_phone.setErrorEnabled(true);
            login_phone.setError(getString(R.string.invalid_phone_number));
            requestFocus(login_phone);

            return false;
        }
//        login_phone.setErrorEnabled(false);
        return true;
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

    /**
     * Called when the prices have been successfully loaded from the API
     *
     * @param listPrices
     */
    @Override
    public void onPricesLoaded(ArrayList<Prices> listPrices) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                layout_sms_code.setVisibility(View.GONE);
                login_layout_phone.setVisibility(View.VISIBLE);
                btncontinue.setVisibility(View.VISIBLE);
                btncontinue.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
                //login_id.setVisibility(View.VISIBLE);
                layout_sms_code.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
                verify.setVisibility(View.GONE);
                verify.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                verificationid = s;
            }
        };
    }

    private void startPhoneNumberVerification(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        login_layout_phone.setVisibility(View.GONE);
        message.setVisibility(View.GONE);
        message2.setVisibility(View.VISIBLE);
        btncontinue.setVisibility(View.GONE);
        btncontinue.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
        layout_sms_code.setVisibility(View.VISIBLE);
        //login_id.setVisibility(View.VISIBLE);
        layout_sms_code.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
        verify.setVisibility(View.VISIBLE);
        verify.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
        //regbackbutton.setVisibility(View.VISIBLE);
        //regbackbutton.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            layout_sms_code.setVisibility(View.GONE);
                            message2.setVisibility(View.GONE);
                            login_layout_fname.setErrorEnabled(false);
                            verify.setVisibility(View.GONE);
                            verify.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.exit_to_left));
                            regbutton3.setVisibility(View.VISIBLE);
                            regbutton3.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
                            login_name_layout.setVisibility(View.VISIBLE);
                            login_name_layout.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right));
//                            login_layout_lname.setVisibility(View.VISIBLE);
//                            login_layout_lname.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_left));
                            login_layout_lname.setErrorEnabled(false);

                           // startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                                sms_code_edt.setError("Invalid code.");

                            }

                        }
                    }
                });
    }

//    private boolean validatePhoneNumberAndCode() {
//        String phoneNumber = login_phone.getText().toString();
//        if (TextUtils.isEmpty(phoneNumber)) {
//            login_phone.setError("Invalid phone number.");
//            return false;
//        }
//
//
//        return true;
   // }

    private boolean validateSMSCode(){
        String code = sms_code_edt.getText().toString();
        if (TextUtils.isEmpty(code)) {
            sms_code_edt.setError("Enter verification Code.");
            return false;
        }

        return true;
    }

}
