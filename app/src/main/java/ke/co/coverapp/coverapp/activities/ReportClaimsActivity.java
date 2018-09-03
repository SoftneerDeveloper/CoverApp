package ke.co.coverapp.coverapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.AssetsLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleyMultipartRequest;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Assets;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.tasks.TaskLoadAssets;
import ke.co.coverapp.coverapp.utility.AppHelperUtil;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.ACCESS_TOKEN;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.CAMERA_REQUEST_ONE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.CAMERA_REQUEST_TWO;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.GALLERY_REQUEST_ONE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.GALLERY_REQUEST_TWO;

public class ReportClaimsActivity extends AppCompatActivity implements AssetsLoadedListener, View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener  {
    private ArrayList<Assets> assetsList = new ArrayList<>();
    private static final String STATE_ACTIVITY = "state_activity";
    Spinner assets_list_claims, covered_options_spinner;
    ArrayAdapter<String> assets_spinner_adapter;
    EditText claim_date, claim_location;
    DatePickerDialog.OnDateSetListener date;
    //String [] listItems;
    String[] mStringArray;
    private ArrayList<Assets> listItems = new ArrayList<>();
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    //MultiSelectSpinner mySpinner1;

    private static final String TAG = "ReportClaims";
    private TextView mLatitudeTextView, textView101;
    private TextView mLongitudeTextView;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationManager locationManager;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    static final Integer LOCATION = 0x1;


    Calendar myCalendar = Calendar.getInstance();
    Button claim_police_abstract_gallery, claim_police_abstract_camera, claim_asset_img_camera, claim_asset_img_gallery, submit_claim;
    CheckBox claimsTC;
    TextView selectRiskText;

    ImageView image_asset, img_police_abstract;
    String image_asset_string, image_police_abstract_string, selectedRisk, selectedAsset;

    private ProgressDialog mProgressDialog;
    private static String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_claims);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //mySpinner1 = (MultiSelectSpinner) findViewById(R.id.mySpinner1);



        claimsTC = (CheckBox) findViewById(R.id.claimsTC);
        selectRiskText = (TextView) findViewById(R.id.selectRiskText);

        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change, load the existing activity from a parcelable
            assetsList = savedInstanceState.getParcelableArrayList(STATE_ACTIVITY);
        } else {
            //if this fragment starts for the first time, load the list of activities from a database
            assetsList = MyApplication.getWritableDatabase().readAssets();
            //if the database is empty, trigger an AsyncTask to download activity list from the API
            if (assetsList.isEmpty()) {
                new TaskLoadAssets(this, 0).execute();
            } else {
                int last_id = Integer.valueOf(assetsList.get(assetsList.size() - 1).getUid());
                new TaskLoadAssets(this, last_id).execute();
            }
        }

        assets_list_claims = (Spinner) findViewById(R.id.assets_list_claims);

        assets_spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);

        if (!assetsList.isEmpty()) {
            for (int i = 0; i < assetsList.size(); i++) {
                Assets assets = assetsList.get(i);
                assets_spinner_adapter.add(assets.getName());
            }
        } else {
            assets_spinner_adapter.add("No assets found");
        }

        assets_spinner_adapter.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);
        assets_list_claims.setAdapter(assets_spinner_adapter);

        // Get selected asset
        assets_list_claims.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (!assetsList.isEmpty()) {
                    Assets asset = assetsList.get(position);
                    selectedAsset = asset.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                L.m("No asset was selected.");
            }
        });

        // Risks adapter & spinner
        covered_options_spinner = (Spinner) findViewById(R.id.covered_options_spinner);
        ArrayAdapter<CharSequence> covered_options_adapter = ArrayAdapter.createFromResource(this, R.array.what_is_covered, android.R.layout.simple_spinner_item);

        covered_options_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        covered_options_spinner.setAdapter(covered_options_adapter);

        // Getting selected risk
        covered_options_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position == 1) {
                    selectedRisk = "Theft";

                } else if(position == 2) {
                    selectedRisk = "Accidental and malicious damage";

                } else if(position == 3) {
                    selectedRisk = "Fire (Smoke and flames)";

                } else if(position == 4) {
                    selectedRisk = "Flood and water damage";

                } else if(position == 5) {
                    selectedRisk  = "Natural disasters";

                } else if(position == 6) {
                    selectedRisk = "Riot and strike";

                } else {
                    selectedRisk = "";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        claim_date = (EditText) findViewById(R.id.claim_date);
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
        claim_date.setOnClickListener(this);

        claim_police_abstract_gallery = (Button) findViewById(R.id.claim_police_abstract_gallery);
        claim_police_abstract_camera = (Button) findViewById(R.id.claim_police_abstract_camera);
        claim_asset_img_camera = (Button) findViewById(R.id.claim_asset_img_camera);
        claim_asset_img_gallery = (Button) findViewById(R.id.claim_asset_img_gallery);

        claim_police_abstract_gallery.setOnClickListener(this);
        claim_police_abstract_camera.setOnClickListener(this);
        claim_asset_img_camera.setOnClickListener(this);
        claim_asset_img_gallery.setOnClickListener(this);

        image_asset = (ImageView) findViewById(R.id.image_asset);
        img_police_abstract = (ImageView) findViewById(R.id.img_police_abstract);

        submit_claim = (Button) findViewById(R.id.submit_claim);
        submit_claim.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(ReportClaimsActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Saving claim");

        claim_location = (EditText) findViewById(R.id.claim_location);

        //get current location
        mLatitudeTextView = (TextView) findViewById((R.id.latitude_textview));
        mLongitudeTextView = (TextView) findViewById((R.id.longitude_textview));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        //Toast.makeText(this, "loading location", Toast.LENGTH_SHORT).show();
        checkLocation();


        textView101= (TextView ) findViewById(R.id.textView101);
        //listItems=getResources().getStringArray(R.array.asset_list);
       // listItems =  MyApplication.getWritableDatabase().readAssets();



        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change, load the existing activity from a parcelable
            listItems = savedInstanceState.getParcelableArrayList(STATE_ACTIVITY);
        } else {
            //if this fragment starts for the first time, load the list of activities from a database
            listItems = MyApplication.getWritableDatabase().readAssets();
            //if the database is empty, trigger an AsyncTask to download activity list from the API
            if (listItems.isEmpty()) {
                new TaskLoadAssets(this, 0).execute();
            } else {
                int last_id = Integer.valueOf(listItems.get(listItems.size() - 1).getUid());
                new TaskLoadAssets(this, last_id).execute();
            }
        }



        checkedItems= new boolean[listItems.size()];
        //int last_id = Integer.valueOf(assetsList.get(assetsList.size() - 1).getUid());
        textView101.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ReportClaimsActivity.this);
                mBuilder.setTitle("Pick Assets");



                mStringArray = new String[listItems.size()];
                mStringArray = listItems.toArray(mStringArray);



                mBuilder.setMultiChoiceItems(mStringArray,  checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {

                        if (isChecked){
                            if (!mUserItems.contains(position)){
                                mUserItems.add(position);
                            }
                        }
                        else if (mUserItems.contains(position)){
                            mUserItems.remove(mUserItems.indexOf(position));
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        String item = "";
                        for (int i = 0; i<mUserItems.size(); i++){
                            item = item+mStringArray[mUserItems.get(i)];
                            if (i !=    mUserItems.size() -1){
                                item = item +", ";
                            }
                        }
                        textView101.setText(item);
                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for (int i=0; i<checkedItems.length; i++){
                            checkedItems[i] = false;
                            mUserItems.clear();
                            textView101.setText("");
                        }
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }



    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        claim_date.setText(sdf.format(myCalendar.getTime()));
    }

    //get location
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
//               public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                      int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
            //Toast.makeText(this, "permissions", Toast.LENGTH_SHORT).show();
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
          //  Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //Toast.makeText(this, "toasting...", Toast.LENGTH_SHORT).show();
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
        mLongitudeTextView.setText(String.valueOf(location.getLongitude() ));
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//
//        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
//
//            Toast.makeText(this, "permissions not granted", Toast.LENGTH_SHORT).show();
//        }
//
//    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        } else {
           // Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            Intent report_claim = new Intent(this, ReportClaimsActivity.class);
            startActivity(report_claim);
        }else{
            Intent report_claim = new Intent(this, MainActivity.class);
            startActivity(report_claim);
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    //end of get location

    @Override
    public void onAssetsLoaded(ArrayList<Assets> assetsList) {

        if (assetsList != null) {
            if (!assetsList.isEmpty()) {
                L.t(ReportClaimsActivity.this, "Assets loaded.");
                L.m("assetsList: " + assetsList);
//                asset_list_adapter.addToAssetsList(assetsList);
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.claim_date:
                // Show datePickerFragment
                new DatePickerDialog(ReportClaimsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.claim_asset_img_camera:
                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_ONE);
                break;

            case R.id.claim_asset_img_gallery:
                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                Intent pickImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
                startActivityForResult(Intent.createChooser(getImage, getString(R.string.select_image)).putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickImage}), GALLERY_REQUEST_ONE);
                break;

            case R.id.claim_police_abstract_camera:
                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_TWO);
                break;

            case R.id.claim_police_abstract_gallery:
                Intent getAbstract = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                Intent pickAbstract = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
                startActivityForResult(Intent.createChooser(getAbstract, getString(R.string.select_image)).putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickAbstract}), GALLERY_REQUEST_TWO);
                break;

            case R.id.submit_claim:
                // TODO: Carry out validation? Let API handle it?
                // 1. Check that a risk has been selected
                if (claimsTC.isChecked()) {
                    // Proceed
                    claimsTC.setError(null);

                    // 2. Check that a risk has been selected
                    if (selectedRisk == "") {
                        // Show error
                        L.t(ReportClaimsActivity.this, "Please select what happened.");
                        selectRiskText.setError("Please select what happened.");
                        selectRiskText.requestFocus();

                    } else {
                        selectRiskText.setError(null);

                        // 3. Check date when it happened
                        if (claim_date.getText().toString().trim().length() < 1) {
                            L.t(ReportClaimsActivity.this, "Please select the date when it happened.");
                            claim_date.setError("Please select the date when it happened.");
                            claim_date.requestFocus();

                        } else {
                            claim_date.setError(null);

                            // 4. Check where it happened
                            if (claim_location.getText().toString().trim().length() < 1) {
                                L.t(ReportClaimsActivity.this, "Please enter the location where it happened.");
                                claim_location.setError("Please enter the location where it happened.");
                                claim_location.requestFocus();

                            } else {
                                claim_location.setError(null);

                                // 5. Check that all required images are added
                                if (image_asset_string == null || image_police_abstract_string == null) {
                                    L.t(ReportClaimsActivity.this, "Please add all the images required.");
                                    image_asset.requestFocus();

                                } else {
                                    // TODO: Post to API
                                    createDialog();

                                }
                            }
                        }

                    }

                } else {
                    // Show error
                   L.t(ReportClaimsActivity.this, "Please read and accept the terms and conditions.");
                    claimsTC.setError("Please read the terms and conditions.");
                    claimsTC.requestFocus();

                }
                break;

            default:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && data != null) {

            if (requestCode == GALLERY_REQUEST_ONE || requestCode == GALLERY_REQUEST_TWO ) {
                try {
                    // From the gallery
                    InputStream inputStream = MyApplication.getAppContext().getContentResolver().openInputStream(data.getData());
                    if (inputStream != null) {
                        L.m(inputStream.toString());
                        switch (requestCode) {
                            case GALLERY_REQUEST_ONE:
                                image_asset.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                image_asset_string = inputStream.toString();
                                break;

                            case GALLERY_REQUEST_TWO:
                                img_police_abstract.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                image_police_abstract_string = inputStream.toString();
                                break;

                            default:
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
                        image_asset.setImageBitmap(photo);
                        image_asset_string = ValidationUtil.getStringImage(photo);
                        break;

                    case CAMERA_REQUEST_TWO:
                        img_police_abstract.setImageBitmap(photo);
                        image_police_abstract_string = ValidationUtil.getStringImage(photo);
                        break;

                    default:
                        break;

                }
            }
        }
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReportClaimsActivity.this, R.style.AppCompatAlertDialogStyle);


        builder.setMessage("Are you sure you want to save this data? ");

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                placeClaim();
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

    public void placeClaim() {

//        L.t(ReportClaimsActivity.this, "Your claim has been placed. It is currently being processed.");
        mProgressDialog.show();
        L.m("Place claim");

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, "http://api.coverapp.co.ke/claims", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse asset) {
//                L.m(asset.toString());
                //Toast.makeText(ReportClaimsActivity.this, "response: "+asset, Toast.LENGTH_SHORT).show();
                L.t(MyApplication.getAppContext(), "Claim has been saved successful."); // TODO: Get message to display directly from the API?
                mProgressDialog.hide();
                startActivity(new Intent(MyApplication.getAppContext(), MainActivity.class));
                MainActivity.ReportClaim = "ReportClaim";

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.hide();

                L.m("Error: " + error.toString());

                //Toast.makeText(ReportClaimsActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();

                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
//                    L.t(ReportClaimsActivity.this, "An error occurred. Status code: " + response.statusCode);

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
                                if (ParseUtil.contains(obj, "message")) {

                                    L.t(MyApplication.getAppContext(), obj.getString("message"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                L.t(ReportClaimsActivity.this, "An error occurred. Please try again.");
                            }
                            break;

                        case 403:
                            L.m("User authorization error");
                            break;

                        default:
                            VolleyCustomErrorHandler.errorMessage(error);
                            break;
                    }
                } else {
                    L.m("Issa volley error: " + error.getClass());
                    VolleyCustomErrorHandler.errorMessage(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("assets_claimed", selectedAsset);
                parameters.put("accident_type", selectedRisk);
                parameters.put("accident_date", claim_date.getText().toString());
                parameters.put("accident_location", claim_location.getText().toString());
                parameters.put("status", "COMPLETED_PENDING");
                parameters.put("state", "1");
                parameters.put("geo_location", mLatitudeTextView.getText().toString().trim()+" , "+mLongitudeTextView.getText().toString().trim());
                L.m(parameters.toString());
                return parameters;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                if (image_asset_string != null) {
                    params.put("current_image", new DataPart("image_asset", AppHelperUtil.getFileDataFromDrawable(ReportClaimsActivity.this.getBaseContext(), image_asset.getDrawable()), "image/jpeg"));
                }

                if (image_police_abstract_string != null) {
                    params.put("police_abstract", new DataPart("police_abstract", AppHelperUtil.getFileDataFromDrawable(ReportClaimsActivity.this.getBaseContext(), img_police_abstract.getDrawable()), "image/jpeg"));
                }

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
                Keys.keys.MY_SOCKET_TIMEOUT_MS,
                Keys.keys.MY_DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(request);
    }


}
