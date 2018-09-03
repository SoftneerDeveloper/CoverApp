package ke.co.coverapp.coverapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleyMultipartRequest;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.utility.AppHelperUtil;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.*;

public class AddVehicleActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout layout_vehicle_make, layout_yom, layout_plate_first, layout_plate_last, layout_odometer, layout_engine_capacity, layout_car_value;
    TextInputEditText vehicle_make, yom, plate_first, plate_last, odometer, engine_capacity, car_value;
    ImageView image_one, image_two, image_three, image_four;
    ProgressBar loading_spinner;
    private ProgressDialog mProgressDialog;
    AppCompatButton purchase;
    private static String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), Keys.keys.ACCESS_TOKEN, ValidationUtil.getDefault());
    AppCompatButton image_one_camera, image_one_gallery, image_two_camera, image_two_gallery, image_three_camera, image_three_gallery, image_four_camera, image_four_gallery;
    String balance = MyApplication.readFromPreferences(MyApplication.getAppContext(), WALLET_BALANCE, ValidationUtil.getDefaultBalance());
    String curr = MyApplication.readFromPreferences(MyApplication.getAppContext(), CURR, ValidationUtil.getDefaultCurr());
    int lower = 0;
    int higher = 0;
    //Uri to store the image uri
    private Uri filePath1;
    private Uri filePath2;
    private Uri filePath3;
    private Uri filePath4;
    //Bitmap to get image from gallery
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        layout_vehicle_make = (TextInputLayout) findViewById(R.id.layout_vehicle_make);
        layout_yom = (TextInputLayout) findViewById(R.id.layout_yom);
        layout_plate_first = (TextInputLayout) findViewById(R.id.layout_plate_first);
        layout_plate_last = (TextInputLayout) findViewById(R.id.layout_plate_last);
        layout_odometer = (TextInputLayout) findViewById(R.id.layout_odometer);
        layout_engine_capacity = (TextInputLayout) findViewById(R.id.layout_engine_capacity);
        layout_car_value = (TextInputLayout) findViewById(R.id.layout_car_value);

        vehicle_make = (TextInputEditText) findViewById(R.id.vehicle_make);
        yom = (TextInputEditText) findViewById(R.id.yom);
        plate_first = (TextInputEditText) findViewById(R.id.plate_first);
        plate_last = (TextInputEditText) findViewById(R.id.plate_last);
        odometer = (TextInputEditText) findViewById(R.id.odometer);
        engine_capacity = (TextInputEditText) findViewById(R.id.engine_capacity);
        car_value = (TextInputEditText) findViewById(R.id.car_value);

        vehicle_make.setText(MyApplication.readFromPreferences(MyApplication.getAppContext(), VEHICLE_MAKE, ValidationUtil.getDefaultString()));
        yom.setText(MyApplication.readFromPreferences(MyApplication.getAppContext(), YOM, ValidationUtil.getDefaultString()));
        plate_first.setText(MyApplication.readFromPreferences(MyApplication.getAppContext(), PLATE_FIRST, ValidationUtil.getDefaultString()));
        plate_last.setText(MyApplication.readFromPreferences(MyApplication.getAppContext(), PLATE_LAST, ValidationUtil.getDefaultString()));
        odometer.setText(MyApplication.readFromPreferences(MyApplication.getAppContext(), ODOMETER, ValidationUtil.getDefaultString()));
        engine_capacity.setText(MyApplication.readFromPreferences(MyApplication.getAppContext(), ENGINE_CAPACITY, ValidationUtil.getDefaultString()));
        car_value.setText(MyApplication.readFromPreferences(MyApplication.getAppContext(), CAR_VALUE, ValidationUtil.getDefaultString()));


        image_one = (ImageView) findViewById(R.id.image_one);
        image_two = (ImageView) findViewById(R.id.image_two);
        image_three = (ImageView) findViewById(R.id.image_three);
        image_four = (ImageView) findViewById(R.id.image_four);
        purchase = (AppCompatButton) findViewById(R.id.purchase);
        image_one_camera = (AppCompatButton) findViewById(R.id.image_one_camera);
        image_one_gallery = (AppCompatButton) findViewById(R.id.image_one_gallery);
        image_two_camera = (AppCompatButton) findViewById(R.id.image_two_camera);
        image_two_gallery = (AppCompatButton) findViewById(R.id.image_two_gallery);
        image_three_camera = (AppCompatButton) findViewById(R.id.image_three_camera);
        image_three_gallery = (AppCompatButton) findViewById(R.id.image_three_gallery);
        image_four_camera = (AppCompatButton) findViewById(R.id.image_four_camera);
        image_four_gallery = (AppCompatButton) findViewById(R.id.image_four_gallery);

        image_one_camera.setOnClickListener(this);
        image_one_gallery.setOnClickListener(this);
        image_two_camera.setOnClickListener(this);
        image_two_gallery.setOnClickListener(this);
        image_three_camera.setOnClickListener(this);
        image_three_gallery.setOnClickListener(this);
        image_four_camera.setOnClickListener(this);
        image_four_gallery.setOnClickListener(this);
        purchase.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Saving vehicle data...");
        mProgressDialog.setCancelable(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_one_camera:
                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_ONE);
                break;
            case R.id.image_two_camera:
                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_TWO);
                break;
            case R.id.image_three_camera:
                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_THREE);
                break;
            case R.id.image_four_camera:
                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_FOUR);
                break;
            case R.id.image_one_gallery:
                Intent getIntent1 = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                Intent pickIntent1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
                startActivityForResult(Intent.createChooser(getIntent1, getString(R.string.select_image)).putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent1}), GALLERY_REQUEST_ONE);
                break;

            case R.id.image_two_gallery:
                Intent getIntent2 = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                Intent pickIntent2 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
                startActivityForResult(Intent.createChooser(getIntent2, getString(R.string.select_image)).putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent2}), GALLERY_REQUEST_TWO);
                break;

            case R.id.image_three_gallery:
                Intent getIntent3 = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                Intent pickIntent3 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
                startActivityForResult(Intent.createChooser(getIntent3, getString(R.string.select_image)).putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent3}), GALLERY_REQUEST_THREE);
                break;

            case R.id.image_four_gallery:
                Intent getIntent4 = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                Intent pickIntent4 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
                startActivityForResult(Intent.createChooser(getIntent4, getString(R.string.select_image)).putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent4}), GALLERY_REQUEST_FOUR);
                break;

            case R.id.purchase:
                if (validateField(layout_vehicle_make, vehicle_make, 5) && validateField(layout_yom, yom, 4)
                        && validateField(layout_plate_first, plate_first, 3) && validateField(layout_plate_last, plate_last, 4)
                        && validateField(layout_odometer, odometer, 2) && validateField(layout_engine_capacity, engine_capacity, 4)
                        && validateField(layout_car_value, car_value, 4)) {
                    buyVehicle();
                }
                break;
        }
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        lower = (int) (Integer.valueOf(car_value.getText().toString()) * 0.04);
        higher = (int) (Integer.valueOf(car_value.getText().toString()) * 0.08);

        builder.setMessage("Your cost will be between " + curr + " " + String.valueOf(lower) + " and " + curr + " " + String.valueOf(higher));

        builder.setPositiveButton("Purchase", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                initiatePurchase();
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

    private boolean validateField(TextInputLayout the_layout, TextInputEditText the_input, int required_length) {
        if (!ValidationUtil.hasValidContents(the_input) || the_input.getText().toString().trim().length() < required_length) {
            the_layout.setErrorEnabled(true);
            the_layout.setError(getString(R.string.invalid_value));
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

    private void initiatePurchase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        L.m("balance " + balance);
        L.m("higher " + higher);

        if (Float.parseFloat(balance) <= higher) {
            L.m("Low balance");
            builder.setMessage("Your account balance is  " + curr + " " + balance + ", you will need to top up to continue ");
            builder.setPositiveButton("Top Up", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Bundle topup = new Bundle();
                            topup.putBoolean(IS_TOPUP, true);
                            startActivity(new Intent(MyApplication.getAppContext(), MainActivity.class).putExtras(topup));
                        }

                    }
            ).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        } else {
            if (filePath1 != null && filePath2 != null && filePath3 != null && filePath4 != null) {
                buyVehicle();
//                uploadMultipart();
                return;
            }
            L.T(MyApplication.getAppContext(), getString(R.string.no_image_error));
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            if (requestCode == GALLERY_REQUEST_ONE || requestCode == GALLERY_REQUEST_TWO
                    || requestCode == GALLERY_REQUEST_THREE || requestCode == GALLERY_REQUEST_FOUR) {
                try {
                    //from the gallery
                    InputStream inputStream = MyApplication.getAppContext().getContentResolver().openInputStream(data.getData());
                    if (inputStream != null) {
                        L.m(inputStream.toString());
                        switch (requestCode) {
                            case GALLERY_REQUEST_ONE:
                                image_one.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                filePath1 = data.getData();
                                break;
                            case GALLERY_REQUEST_TWO:
                                image_two.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                filePath2 = data.getData();
                                break;
                            case GALLERY_REQUEST_THREE:
                                image_three.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                filePath3 = data.getData();
                                break;
                            case GALLERY_REQUEST_FOUR:
                                image_four.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                filePath4 = data.getData();
                                break;
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Bitmap photo;
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
                try {
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());


                    switch (requestCode) {
                        case CAMERA_REQUEST_ONE:
                            image_one.setImageBitmap(photo);
                            filePath1 = data.getData();
                            break;
                        case CAMERA_REQUEST_TWO:
                            image_two.setImageBitmap(photo);
                            filePath2 = data.getData();
                            break;
                        case CAMERA_REQUEST_THREE:
                            image_three.setImageBitmap(photo);
                            filePath3 = data.getData();
                            break;
                        case CAMERA_REQUEST_FOUR:
                            image_four.setImageBitmap(photo);
                            filePath4 = data.getData();
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void buyVehicle() {
        L.m("Saving vehicle...");
        mProgressDialog.show();
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, getString(R.string.baseUrl) + "/vehicle ", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse  response) {
                //Should be set to "" when saving is successfull
                MyApplication.saveToPreferences(MyApplication.getAppContext(), VEHICLE_MAKE, "");
                MyApplication.saveToPreferences(MyApplication.getAppContext(), CAR_VALUE, "");
                MyApplication.saveToPreferences(MyApplication.getAppContext(), ENGINE_CAPACITY, "");
                MyApplication.saveToPreferences(MyApplication.getAppContext(), ODOMETER, "");
                MyApplication.saveToPreferences(MyApplication.getAppContext(), PLATE_LAST, "");
                MyApplication.saveToPreferences(MyApplication.getAppContext(), PLATE_FIRST, "");
                MyApplication.saveToPreferences(MyApplication.getAppContext(), YOM, "");
                mProgressDialog.hide();
                vehicle_make.setText("");
                yom.setText("");
                plate_first.setText("");
                plate_last.setText("");
                odometer.setText("");
                engine_capacity.setText("");
                car_value.setText("");
                L.m("Vehicle saved successfully");

                String resultResponse = new String(response.data);
                try {
                    JSONObject result = new JSONObject(resultResponse);
//                    String status = result.getString("status");
//                    String message = result.getString("message");

//                    if (status.equals(Constant.REQUEST_SUCCESS)) {
//                        // tell everybody you have succed upload image and post strings
//                        Log.i("Messsage", message);
//                    } else {
//                        Log.i("Unexpected", message);
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                MyApplication.saveToPreferences(MyApplication.getAppContext(), VEHICLE_MAKE, vehicle_make.getText().toString().trim());
                MyApplication.saveToPreferences(MyApplication.getAppContext(), CAR_VALUE, car_value.getText().toString().trim());
                MyApplication.saveToPreferences(MyApplication.getAppContext(), ENGINE_CAPACITY, engine_capacity.getText().toString().trim());
                MyApplication.saveToPreferences(MyApplication.getAppContext(), ODOMETER, odometer.getText().toString().trim());
                MyApplication.saveToPreferences(MyApplication.getAppContext(), PLATE_LAST, plate_last.getText().toString().trim());
                MyApplication.saveToPreferences(MyApplication.getAppContext(), PLATE_FIRST, plate_first.getText().toString().trim());
                MyApplication.saveToPreferences(MyApplication.getAppContext(), YOM, yom.getText().toString().trim());

                mProgressDialog.hide();

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    //parsing the error
                    String json = "";
                    JSONObject obj;
                    L.m(new String(response.data));
                    switch (response.statusCode) {
                        case 400:
                            json = new String(response.data);
                            try {
                                obj = new JSONObject(json);
                                if (ParseUtil.contains(obj, "error")) {
                                    L.t(MyApplication.getAppContext(), obj.getString("error"));
                                } else {
                                    L.t(MyApplication.getAppContext(), "Something went wrong, please retry");
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                String text_id_number = MyApplication.readFromPreferences(MyApplication.getAppContext(), ID_NUMBER, ValidationUtil.getDefault());

                parameters.put("vehicle_make", vehicle_make.getText().toString().trim());
                parameters.put("yom", yom.getText().toString().trim());
                parameters.put("plate_first", plate_first.getText().toString().trim());
                parameters.put("plate_last", plate_last.getText().toString().trim());
                parameters.put("odometer", odometer.getText().toString().trim());
                parameters.put("engine_capacity", engine_capacity.getText().toString().trim());
                parameters.put("car_value", car_value.getText().toString().trim());
                parameters.put("id_number", text_id_number);
                parameters.put("car_use", "Private");

                return parameters;

            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could find file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("image_one", new DataPart("image_one", AppHelperUtil.getFileDataFromDrawable(getBaseContext(), image_one.getDrawable()), "image/jpeg"));
                params.put("image_two", new DataPart("image_two", AppHelperUtil.getFileDataFromDrawable(getBaseContext(), image_two.getDrawable()), "image/jpeg"));
                params.put("image_three", new DataPart("image_three", AppHelperUtil.getFileDataFromDrawable(getBaseContext(), image_three.getDrawable()), "image/jpeg"));
                params.put("image_four", new DataPart("image_four", AppHelperUtil.getFileDataFromDrawable(getBaseContext(), image_four.getDrawable()), "image/jpeg"));

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                Keys.keys.MY_SOCKET_TIMEOUT_MS,
                Keys.keys.MY_DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(request);
    }


    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                L.t(MyApplication.getAppContext(), "Permission granted now you can read the storage");
            } else {
                //Displaying another toast if permission is not granted
                L.t(MyApplication.getAppContext(), "Oops you just denied the permission");
            }
        }
    }


}

