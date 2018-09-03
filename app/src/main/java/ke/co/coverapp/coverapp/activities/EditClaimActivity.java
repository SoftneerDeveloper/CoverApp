package ke.co.coverapp.coverapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleyMultipartRequest;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Claims;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.utility.AppHelperUtil;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.ACCESS_TOKEN;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.CAMERA_REQUEST_ONE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.CAMERA_REQUEST_TWO;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.GALLERY_REQUEST_ONE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.GALLERY_REQUEST_TWO;

public class EditClaimActivity extends AppCompatActivity {
    int claimId,cause_position;
    Spinner damage_cause;
    CheckBox terms;
    String asset_type,current_image_string,police_abstract_string;
    LinearLayout damaged_assets,select_list;
    TextView damaged_heading;
    CalendarView calendarView;
    EditText date_happened,location;
    ImageView current_image,police_abstract;
    Button claim_asset_img_gallery,claim_asset_img_camera,claim_police_abstract_gallery,claim_police_abstract_camera,submit_claim;
    ArrayList<Claims> claimsList = new ArrayList<>();

    private ProgressDialog mProgressDialog;
    private static String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_claim);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mProgressDialog = new ProgressDialog(EditClaimActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Saving updates");

    // Getting id
        Bundle extras = getIntent().getExtras();
        claimId = extras.getInt("claimId");
        asset_type = extras.getString("claimName");

        damaged_assets = (LinearLayout) findViewById(R.id.asset_damaged_layout);
        damaged_assets.setVisibility(View.VISIBLE);
        select_list = (LinearLayout) findViewById(R.id.claim_select_list);
        select_list.setVisibility(View.GONE);
        damaged_heading = (TextView) findViewById(R.id.asset_damaged);
        damaged_heading.setText("Current damage cause: "+asset_type);
        damage_cause = (Spinner) findViewById(R.id.covered_options_spinner);
        //set to spinner selected item
        if (asset_type.matches(""))
        {
            damage_cause.setSelection(1);
        }
        else if (asset_type.matches(""))
        {
            damage_cause.setSelection(2);
        } else if (asset_type.matches(""))
        {
            damage_cause.setSelection(3);
        } else if (asset_type.matches(""))
        {
            damage_cause.setSelection(4);
        } else if (asset_type.matches(""))
        {
            damage_cause.setSelection(5);
        } else if (asset_type.matches(""))
        {
            damage_cause.setSelection(6);
        }

        terms = (CheckBox) findViewById(R.id.terms);
        terms.setChecked(true);

        date_happened = (EditText) findViewById(R.id.claim_date);
        location = (EditText) findViewById(R.id.claim_location);
        current_image = (ImageView) findViewById(R.id.image_asset);
        police_abstract = (ImageView) findViewById(R.id.img_police_abstract);
        calendarView = (CalendarView) findViewById(R.id.calendarView1);
        claim_asset_img_gallery = (Button) findViewById(R.id.claim_asset_img_gallery);
        claim_asset_img_camera = (Button) findViewById(R.id.claim_asset_img_camera);
        claim_police_abstract_gallery = (Button) findViewById(R.id.claim_police_abstract_gallery);
        claim_police_abstract_camera = (Button) findViewById(R.id.claim_police_abstract_camera);
        submit_claim = (Button) findViewById(R.id.submit_claim);

        calendarView.setVisibility(View.GONE);
        date_happened.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.setVisibility(View.VISIBLE);
                date_happened.setVisibility(View.GONE);
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                //Toast.makeText(getApplicationContext(), ""+dayOfMonth+"/"+month+"/"+year, Toast.LENGTH_LONG).show();// TODO Auto-generated method stub
                date_happened.setText(+(month+1)+"/"+dayOfMonth+"/"+year);
                calendarView.setVisibility(View.GONE);
                date_happened.setVisibility(View.VISIBLE);
            }
        });

        // Get claim with that id
        claimsList = MyApplication.getWritableDatabase().readClaim(Integer.toString(claimId));

        // Get details of first item in the claim array
        if (claimsList != null) {
            if (claimsList.size() > 0) {
                Claims claim = claimsList.get(0);

                // Do everything else
                // Append data to fields that were initially saved
                L.m("Retrieved claim - ID: " + claim.getClaimId());
                L.m("Retrieved claim - Date: " + claim.getAccidentDate());
                L.m("Retrieved claim - Location: " + claim.getAccidentLocation());

        // Populating already submitted fields
                date_happened.setText(claim.getAccidentDate());
                location.setText(claim.getAccidentLocation());

                Picasso.with(this)
                        .load(claim.getCurrentImage())
                        .into(current_image);

                Picasso.with(this)
                        .load(claim.getPoliceAbstract())
                        .into(police_abstract);
            } else {
                // Return error
                L.t(EditClaimActivity.this, "There was trouble getting claim data. Please try again.");
            }
        } else {
            // Return error
            L.t(EditClaimActivity.this, "There was trouble getting claim data. Please try again.");
        }

        //set onclick listener
        claim_asset_img_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                Intent pickImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
                startActivityForResult(Intent.createChooser(getImage, getString(R.string.select_image)).putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickImage}), GALLERY_REQUEST_ONE);
            }
        });
        claim_asset_img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_ONE);
            }
        });
       claim_police_abstract_gallery.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent getImage = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
               Intent pickImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
               startActivityForResult(Intent.createChooser(getImage, getString(R.string.select_image)).putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickImage}), GALLERY_REQUEST_TWO);
           }
       });
        claim_police_abstract_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_TWO);
            }
        });

        getData();

        submit_claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //validation of fields then update
                if (date_happened.getText().toString().trim().equals(""))
                {
                    date_happened.setError("Please specify date");
                }
                else if (location.getText().toString().trim().equals("") )
                {
                   location.setError("Please specify location");
                }
                else if (cause_position == 0)
                {
                    damage_cause.requestFocus();
                    Toast.makeText(EditClaimActivity.this, "select accident cause", Toast.LENGTH_LONG).show();
                }
                else if (!terms.isChecked())
                {
                    L.t(EditClaimActivity.this, "Please read and accept the terms and conditions.");
                    terms.setError("Please read and accept the terms and conditions.");
                }
                else
                {
                    createDialog();
                }
            }
        });
    }

    //get image from galley/camera
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
                                current_image.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                current_image_string = inputStream.toString();
                                break;

                            case GALLERY_REQUEST_TWO:
                                police_abstract.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                police_abstract_string = inputStream.toString();
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
                        current_image.setImageBitmap(photo);
                        current_image_string = ValidationUtil.getStringImage(photo);
                        break;

                    case CAMERA_REQUEST_TWO:
                        police_abstract.setImageBitmap(photo);
                        police_abstract_string = ValidationUtil.getStringImage(photo);
                        break;

                    default:
                        break;

                }
            }
        }
    }

    //get data from fields to save to db
    private void getData()
    {
        damage_cause.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cause_position = position;

                    damage_cause.getSelectedItem().toString().trim();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //dialog
    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditClaimActivity.this, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Are you sure you want to save this data? ");

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                updateClaim();
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

    //make network request to update claim
    public void updateClaim() {

//        L.t(ReportClaimsActivity.this, "Your claim has been placed. It is currently being processed.");

        mProgressDialog.show();
        L.m("Place claim");

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, "http://api.coverappke.com/claims/update", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse asset) {
//                L.m(asset.toString());
                //Toast.makeText(EditClaimActivity.this, "response: "+asset, Toast.LENGTH_SHORT).show();
                L.t(MyApplication.getAppContext(), "Claim has been updated successful."); // TODO: Get message to display directly from the API?
                mProgressDialog.hide();

                startActivity(new Intent(MyApplication.getAppContext(), MainActivity.class));
                MainActivity.EditClaimActivity = "EditClaimActivity";

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.hide();

                L.m("Error: " + error.toString());

                NetworkResponse response = error.networkResponse;

                Toast.makeText(EditClaimActivity.this, ""+error, Toast.LENGTH_SHORT).show();

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
                                L.t(EditClaimActivity.this, "An error occurred. Please try again.");
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

                parameters.put("claim_id", String.valueOf(claimId));
                parameters.put("accident_type", damage_cause.getSelectedItem().toString().trim());
                parameters.put("accident_date", date_happened.getText().toString().trim());
                parameters.put("accident_location", location.getText().toString().trim()); L.m(parameters.toString());
                return parameters;
            }

//            @Override
//            protected Map<String, DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//
//                if (current_image_string != null) {
//                    params.put("current_image", new DataPart("current_image", AppHelperUtil.getFileDataFromDrawable(EditClaimActivity.this.getBaseContext(), current_image.getDrawable()), "image/jpeg"));
//                }
//
//                if (police_abstract_string != null) {
//                    params.put("police_abstract", new DataPart("police_abstract", AppHelperUtil.getFileDataFromDrawable(EditClaimActivity.this.getBaseContext(), police_abstract.getDrawable()), "image/jpeg"));
//                }
//
//                return params;
//            }

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
