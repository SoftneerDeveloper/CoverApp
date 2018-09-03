package ke.co.coverapp.coverapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.fragments.PhonesFragment;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleyMultipartRequest;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.utility.AppHelperUtil;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.ACCESS_TOKEN;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.CAMERA_REQUEST_ONE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.CAMERA_REQUEST_THREE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.GALLERY_REQUEST_ONE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.GALLERY_REQUEST_THREE;

public class AddAssetActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    AppCompatButton image_one_camera, image_one_gallery, save_asset;
    TextInputEditText asset_serial;
    TextView textView5;
    TextInputLayout layout_desc, layout_name;
    TextInputEditText asset_name, asset_desc;
    String category, type, image_one_string, im;
    ImageView image_one;
    CardView icons;
    public Bitmap bitmap;

    private ProgressDialog mProgressDialog;
    private static String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());
    LinearLayout addImeiCard;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_asset);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Get data from fragment
        Bundle bundle = getIntent().getExtras();
        category = bundle.getString("category");
        type = bundle.getString("type");

        L.m("Asset category: " + category + "; Asset type: " + type);

        // Initialize elements
        image_one_camera = (AppCompatButton) findViewById(R.id.image_one_camera);
        image_one_gallery = (AppCompatButton) findViewById(R.id.image_one_gallery);
        save_asset = (AppCompatButton) findViewById(R.id.save_asset);
        icons = (CardView) findViewById(R.id.icons);

        asset_serial = (TextInputEditText) findViewById(R.id.asset_serial);
        asset_serial.setHint("Serial No.");
        textView5 = (TextView) findViewById(R.id.textView5);
        layout_desc = (TextInputLayout) findViewById(R.id.layout_desc);
        layout_name = (TextInputLayout) findViewById(R.id.layout_name);
        asset_name = (TextInputEditText) findViewById(R.id.asset_name);
        asset_desc = (TextInputEditText) findViewById(R.id.asset_desc);
        image_one = (ImageView) findViewById(R.id.image_one);
//        image_one.setImageResource(R.drawable.action_icon);
//        image_one_string = "phone.png";
        addImeiCard = (LinearLayout) findViewById(R.id.addImeiCard);

        image_one_camera.setOnClickListener(this);
        image_one_gallery.setOnClickListener(this);
        save_asset.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(AddAssetActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Saving asset");

        // Show view according to type
        if(type.matches("1")) {
            // Do not show add from camera button
            image_one_camera.setVisibility(View.GONE);
            image_one_gallery.setVisibility(View.GONE);
            icons.setVisibility(View.GONE);

            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.side_icon);

            image_one.setImageBitmap(bitmap);

            image_one_string = ("photo.png");

        }

        if(category.matches("2")){
            // Do not show the serial field
            textView5.setVisibility(View.GONE);
            asset_serial.setVisibility(View.GONE);
            asset_serial.setText("Na");
        }

        //change heading from serial no. to imei no.
        if(type.matches("1") || type.matches("14"))
            {
                textView5.setText("IMEI No.");
                asset_serial.setHint("IMEI No.");
            }
        // Show IMEI for phone(only user's and !user's spouse)
        if(type.matches("1")){ //|| type.matches("14")
            //get phone imei number

           loadIMEI();
            //addImeiCard.setVisibility(View.VISIBLE);

        }
    }

    /**
     * Called when the 'loadIMEI' function is triggered.
     */
    public void loadIMEI() {
        // Check if the READ_PHONE_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_PHONE_STATE permission has not been granted.
            requestReadPhoneStatePermission();
        } else {
            // READ_PHONE_STATE permission is already been granted.
            doPermissionGrantedStuffs();
        }
    }

    /**
     * Requests the READ_PHONE_STATE permission.
     * If the permission has been denied previously, a dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(AddAssetActivity.this)
                    .setTitle("Permission Request")
                    .setMessage("Allow Cover app to read phone state")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(AddAssetActivity.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                        }
                    })
                    //.setIcon(R.drawable.onlinlinew_warning_sign)
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            // Received permission result for READ_PHONE_STATE permission.est.");
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // READ_PHONE_STATE permission has been granted, proceed with displaying IMEI Number
                //alertAlert(getString(R.string.permission_available_read_phone_state));
                doPermissionGrantedStuffs();
            } else {
                alertAlert("permissions not granted read phone state");
                addImeiCard.setVisibility(View.VISIBLE);
            }
        }
    }

    private void alertAlert(String msg) {
        new AlertDialog.Builder(AddAssetActivity.this)
                .setTitle("Permission Request")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do somthing here
                    }
                })
                //.setIcon(R.drawable.onlinlinew_warning_sign)
                .show();
    }


    public void doPermissionGrantedStuffs() {
        //Have an  object of TelephonyManager
        TelephonyManager tm =(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //Get IMEI Number of Phone  //////////////// for this example i only need the IMEI
        String IMEINumber=tm.getDeviceId();

        // Now read the desired content to a textview.
        addImeiCard.setVisibility(View.GONE);
        asset_serial.setText(IMEINumber);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_one_camera:
                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_ONE);
                break;

            case R.id.image_one_gallery:

                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                Intent pickImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
                startActivityForResult(Intent.createChooser(getImage, getString(R.string.select_image)).putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickImage}), GALLERY_REQUEST_ONE);

                break;

            case R.id.save_asset:

                if (validateField(layout_name, asset_name, 2) && validateField(layout_desc, asset_desc, 5)) {


//                    if (asset_serial.getText().length() < 2)
//                    {
//                        asset_serial.setError("please provide Serial");
//                    }
                  if (image_one_string != null && asset_serial.getText().length() > 1) {

                      if (type.matches("1"))
                      {
                          createDialogPhone();
                      }else {
                          createDialog();
                      }
                        return;
                    }

                    L.T(MyApplication.getAppContext(), getString(R.string.no_image_error));
                    asset_serial.setError("please provide Serial");
                }

                break;

            default:
                break;
        }
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
                                image_one.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                image_one_string = inputStream.toString();

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
                        image_one.setImageBitmap(photo);
                        image_one_string = ValidationUtil.getStringImage(photo);
                        break;

                }
            }
        }
    }

    private boolean validateField(TextInputLayout the_layout, TextInputEditText the_input, int required_length) {
        if (!ValidationUtil.hasValidContents(the_input) || the_input.getText().toString().trim().length() < required_length) {
            the_layout.setErrorEnabled(true);
            the_layout.setError("Invalid value");
            requestFocus(the_input);

            return false;
        }
        the_layout.setErrorEnabled(false);
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            AddAssetActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddAssetActivity.this, R.style.AppCompatAlertDialogStyle);


        builder.setMessage("Are you sure you want to save this data? ");

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                saveAsset();
//                L.t(AddAssetActivity.this, "Asset successfully saved");
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

    private void createDialogPhone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddAssetActivity.this, R.style.AppCompatAlertDialogStyle);


        builder.setMessage("Are you sure you want to save this data? ");

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                saveAssetPhone();
//                L.t(AddAssetActivity.this, "Asset successfully saved");
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

    private void saveAsset() {

//        if(type.matches("1"))
//        {
//            Toast.makeText(this, "image 1", Toast.LENGTH_SHORT).show();
//            image_one = (image_one.getDrawable());
//            image_one_string = ("photo.png");
//        }

        mProgressDialog.show();
        L.m("Saving asset...");

        final VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, getString(R.string.baseUrl) + "/asset", new Response.Listener<NetworkResponse>() {


            @Override
            public void onResponse(NetworkResponse asset) {
                L.m(asset.toString());
                L.t(MyApplication.getAppContext(), "Asset saved successful");
                mProgressDialog.hide();
                asset_serial.setText("");
                asset_name.setText("");
                asset_desc.setText("");


                startActivity(new Intent(MyApplication.getAppContext(), MainActivity.class));
                MainActivity.fragment_id = "1";

//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                //ft.replace(R.id.content_edit_asset_two, new PhonesFragment(), getString(R.string.app_name));
//                //ft.commit();
//               // Toast.makeText(AddAssetActivity.this, "should change", Toast.LENGTH_SHORT).show();
//                ft.replace(R.id.content, PhonesFragment.newInstance()).commit();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.hide();
                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {

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
                                if (ParseUtil.contains(obj, "text")) {

                                    L.t(MyApplication.getAppContext(), obj.getString("text"));
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
                parameters.put("asset_name", asset_name.getText().toString().trim());
                parameters.put("asset_desc", asset_desc.getText().toString().trim());
                parameters.put("asset_serial", asset_serial.getText().toString().trim());
                parameters.put("asset_category", category);
                parameters.put("asset_type", type);
                L.m(parameters.toString());

                return parameters;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                    params.put("image_asset", new DataPart("image_asset", AppHelperUtil.getFileDataFromDrawable(AddAssetActivity.this.getBaseContext(), image_one.getDrawable()), "image/jpeg"));

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

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //TODO get the image bitmap to solve the error
    private void saveAssetPhone() {

        mProgressDialog.show();
        L.m("Saving asset...");

        final VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, getString(R.string.baseUrl) + "/asset", new Response.Listener<NetworkResponse>() {


            @Override
            public void onResponse(NetworkResponse asset) {
                L.m(asset.toString());
                L.t(MyApplication.getAppContext(), "Asset saved successful");
                mProgressDialog.hide();
                asset_serial.setText("");
                asset_name.setText("");
                asset_desc.setText("");

                startActivity(new Intent(MyApplication.getAppContext(), MainActivity.class));
                MainActivity.fragment_id = "1";

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.hide();
                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {

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
                                if (ParseUtil.contains(obj, "text")) {

                                    L.t(MyApplication.getAppContext(), obj.getString("text"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            //VolleyCustomErrorHandler.errorMessage(error);
                            startActivity(new Intent(MyApplication.getAppContext(), MainActivity.class));
                            MainActivity.fragment_id = "1";

                            break;
                    }
                } else {
                    VolleyCustomErrorHandler.errorMessage(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String image = getStringImage(bitmap);

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("asset_name", asset_name.getText().toString().trim());
                parameters.put("asset_desc", asset_desc.getText().toString().trim());
                parameters.put("asset_serial", asset_serial.getText().toString().trim());
                parameters.put("asset_category", category);
                parameters.put("asset_type", type);
                parameters.put("image_asset", image);
                L.m(parameters.toString());

                return parameters;
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
