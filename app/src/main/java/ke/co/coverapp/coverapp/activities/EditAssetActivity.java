package ke.co.coverapp.coverapp.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.List;
import java.util.Map;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.AssetCategoriesLoadedListener;
import ke.co.coverapp.coverapp.callbacks.AssetTypesLoadedListener;
import ke.co.coverapp.coverapp.fragments.AssetsViewFragment;
import ke.co.coverapp.coverapp.fragments.PhonesFragment;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleyMultipartRequest;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.AssetCategories;
import ke.co.coverapp.coverapp.pojo.AssetTypes;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.tasks.TaskLoadAssetCategories;
import ke.co.coverapp.coverapp.tasks.TaskLoadAssetTypes;
import ke.co.coverapp.coverapp.utility.AppHelperUtil;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.ACCESS_TOKEN;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.CAMERA_REQUEST_FIVE;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.GALLERY_REQUEST_FIVE;

public class EditAssetActivity extends AppCompatActivity implements View.OnClickListener, AssetCategoriesLoadedListener, AssetTypesLoadedListener {
    TextInputEditText name;
    TextInputEditText desc;
    ImageView image;
    Spinner asset_category, asset_type;
    LinearLayout edit_icons;

    String image_string, selected_category, selected_type;
    int assetId;

    Button update_asset_button, open_gallery, take_photo;

    List<AssetCategories> list_asset_categories;
    List<AssetTypes> list_asset_types;
    List<AssetTypes> list_selected_asset_types;

    private ProgressDialog mProgressDialog;
    private static String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_asset);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
            this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        // Getting data passed from intent
        Bundle extras = getIntent().getExtras();
        assetId = extras.getInt("assetId");
        L.m("Asset ID: " + assetId);
        String assetName = extras.getString("assetName");
        String assetDescription = extras.getString("assetDescription");
        String assetCategory = extras.getString("assetCategory");
        String type = extras.getString("type");

        Bitmap assetImage = (Bitmap) extras.getParcelable("assetImage");

        if (assetImage != null) {
            // Get image from the database
            L.m("Trying to get image");

        }

        // Fill asset details in activity
        name = (TextInputEditText) findViewById(R.id.edit_asset_name);
        name.setText(assetName);
        edit_icons = (LinearLayout) findViewById(R.id.edit_icons);

        desc = (TextInputEditText) findViewById(R.id.edit_asset_description);
        desc.setText(assetDescription);
        image = (ImageView) findViewById(R.id.edit_asset_image);

        if(assetCategory != null) {
            byte[] byteArray = extras.getByteArray("assetImage");
//            Bitmap bm = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
            String assetImage1 = extras.getString("assetImage");


            //image.setImageBitmap(bm);
            Picasso.with(MyApplication.getAppContext())
                    .load(assetImage1)
                    .placeholder(R.drawable.side_icon)
                    .error(R.drawable.side_icon)
                    .resize(100,100)
                    .centerCrop()
                    .into(image);
        }else {

            image.setImageBitmap(assetImage);
        }


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Updating asset");

        // On Click
        update_asset_button = (Button) findViewById(R.id.update_asset_button);
        if (update_asset_button != null) {
            update_asset_button.setOnClickListener(this);
        }

        open_gallery = (Button) findViewById(R.id.open_gallery_edit_asset);
        if (open_gallery != null) {
            open_gallery.setOnClickListener(this);
        }

        take_photo = (Button) findViewById(R.id.take_photo_edit_asset);
        if (take_photo != null) {
            take_photo.setOnClickListener(this);
        }

        // Setting up spinners
        // 1. Asset Category Spinner

        // Load list of categories from the database
        list_asset_categories = MyApplication.getWritableDatabase().readAssetCategories();

        list_asset_types = MyApplication.getWritableDatabase().readAssetTypes();

        // if blank, trigger an AsyncTask to download activity list from the API
        if (list_asset_categories.isEmpty()) {
            new TaskLoadAssetCategories(this).execute();
        }

        if (list_asset_types.isEmpty()) {
            new TaskLoadAssetTypes(this).execute();
        }

        asset_category = (Spinner) findViewById(R.id.asset_category);
        ArrayAdapter<String> asset_category_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);

        if (!list_asset_categories.isEmpty()) {
            for (int i = 0; i < list_asset_categories.size(); i++) {
                AssetCategories category = list_asset_categories.get(i);
                asset_category_adapter.add(category.getName());
            }
        } else {
            asset_category_adapter.add("No categories found");
        }

        asset_category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        asset_category.setAdapter(asset_category_adapter);

        // 2. Asset Type Spinner
        asset_type = (Spinner) findViewById(R.id.asset_type);

        final ArrayAdapter<String> asset_type_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);

        // Handling category spinner selections
        asset_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AssetCategories assetCategory = list_asset_categories.get(position);
                selected_category = assetCategory.getId();

                // Get types belonging to selected category
                list_selected_asset_types = MyApplication.getWritableDatabase().readParticularAssetTypes(selected_category);

                asset_type_adapter.clear();

                // Populate asset type spinner
                if (list_selected_asset_types != null) { //  || !list_selected_asset_types.isEmpty()
                    for (int i = 0; i < list_selected_asset_types.size(); i++) {
                        AssetTypes type = list_selected_asset_types.get(i);
                        asset_type_adapter.add(type.getName());
                    }
                } else {
                    asset_type_adapter.add("No types found");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Prevent asset for being edited
                L.m("Please select a category");
            }
        });

        asset_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        asset_type.setAdapter(asset_type_adapter);

        // 2. Handling asset type selection
        asset_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Get selected type
                AssetTypes assetType = list_selected_asset_types.get(i);
                selected_type = assetType.getId();

                L.m("This is the selected type: " + assetType.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                L.m("No type was selected");
            }
        });

        if (assetCategory.matches("1") && type.matches("1"))
        {
            Toast.makeText(this, "cat: "+assetCategory, Toast.LENGTH_SHORT).show();
            edit_icons.setVisibility(View.GONE);
        }
//        else {
//            Toast.makeText(this, "cat: "+assetCategory, Toast.LENGTH_SHORT).show();
//            edit_icons.setVisibility(View.VISIBLE);
//        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_gallery_edit_asset:
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
                startActivityForResult(Intent.createChooser(getIntent, getString(R.string.select_image)).putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent}), GALLERY_REQUEST_FIVE);

                // Display image details
                // Call function to handle this

                break;
            case R.id.take_photo_edit_asset:
                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_FIVE);

                // Display image details
                // Call function to handle this

                break;
            case R.id.update_asset_button:
                if (validateField(name, 2, "Too short") && validateField(desc, 5, "Too short")){
                    // Check if all the data is okay

                    // Check if the image has been changed or not - if image_string == null, image was not changed else, it was

                    // Create confirmation dialog
                    createDialog();
                }
                break;
        }
    }

    /**
     * Set image after selection from gallery or photo taken from the camera
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {

            if (requestCode == GALLERY_REQUEST_FIVE) {
                try {
                    // Image from gallery
                    InputStream inputStream = MyApplication.getAppContext().getContentResolver().openInputStream(data.getData());
                    if (inputStream != null) {
                        L.m(inputStream.toString());

                        switch (requestCode) {
                            case GALLERY_REQUEST_FIVE:
                                image.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                image_string = inputStream.toString();
                                break;
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                switch (requestCode) {
                    case CAMERA_REQUEST_FIVE:
                        image.setImageBitmap(photo);
                        image_string = ValidationUtil.getStringImage(photo);

                        Uri new_df = data.getData();
                        L.m("Image data: " + new_df);
                        break;
                }
            }
        }
    }

    /**
     * Validate input text field
     * @param the_input
     * @param required_length
     * @param message
     * @return
     */
    private boolean validateField(TextInputEditText the_input, int required_length, String message) {
        if (!ValidationUtil.hasValidContents(the_input) || the_input.getText().toString().trim().length() < required_length) {
            the_input.setError(message);
            requestFocus(the_input);

            return false;
        }

        return true;
    }

    /**
     * Request focus
     * @param view
     */
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * Trigger update function
     */
    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Are you sure you want to update this asset? ");

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                updateAsset();
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

    private void updateAsset() {
        mProgressDialog.show();
        L.m("Updating asset...");

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, getString(R.string.baseUrl) + "/assets/update/" + assetId, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse update) {
                L.m(update.data.toString());
                L.t(MyApplication.getAppContext(), "Asset updated");
                mProgressDialog.hide();

                Fragment fragment= new Fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_edit_asset, fragment); // fragmen container id in first parameter is the  container(Main layout id) of Activity
                transaction.commit();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.hide();
                NetworkResponse response = error.networkResponse;

                L.m("Hello");

                if (response != null && response.data != null) {
                    L.m(new String(response.data));
                    switch (response.statusCode) {
                        case 400:
                            JSONObject obj;
                            String json = new String(response.data);
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
                parameters.put("asset_name", name.getText().toString().trim());
                parameters.put("asset_desc", desc.getText().toString().trim());
                // Include asset type and category in the update?
                parameters.put("asset_category", selected_category);
                parameters.put("asset_type", selected_type);
                L.m(parameters.toString());
                L.m("asset_id: " + assetId);
                return parameters;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("asset_image", new DataPart("asset_image", AppHelperUtil.getFileDataFromDrawable(EditAssetActivity.this.getBaseContext(), image.getDrawable()), "image/jpeg"));
                L.m(params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
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

    @Override
    protected void onPause() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }

    @Override
    public void onAssetCategoriesLoaded(ArrayList<AssetCategories> assetCategories) {
        L.m(assetCategories.toString());
        list_asset_categories = MyApplication.getWritableDatabase().readAssetCategories();
    }

    @Override
    public void onAssetTypesLoaded(ArrayList<AssetTypes> assetTypes) {
        list_asset_types = MyApplication.getWritableDatabase().readAssetTypes();
    }
}
