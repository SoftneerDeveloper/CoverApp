package ke.co.coverapp.coverapp.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

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
import ke.co.coverapp.coverapp.activities.MainActivity;
import ke.co.coverapp.coverapp.application.MyApplication;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.*;

import ke.co.coverapp.coverapp.callbacks.AssetCategoriesLoadedListener;
import ke.co.coverapp.coverapp.callbacks.AssetTypesLoadedListener;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleyMultipartRequest;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.AssetCategories;
import ke.co.coverapp.coverapp.pojo.AssetTypes;
import ke.co.coverapp.coverapp.pojo.Assets;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.tasks.TaskLoadAssetCategories;
import ke.co.coverapp.coverapp.tasks.TaskLoadAssetTypes;
import ke.co.coverapp.coverapp.utility.AppHelperUtil;
import ke.co.coverapp.coverapp.utility.ParseUtil;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

public class AddAssetsFragment extends Fragment implements View.OnClickListener, AssetTypesLoadedListener, AssetCategoriesLoadedListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ImageView image_three;//image_one, image_two,
    String image_three_string, selected_category, selected_type; //image_one_string, image_two_string,
    int request_code = 0;
    AppCompatButton image_three_camera, image_three_gallery;//image_one_camera, image_one_gallery, image_two_camera, image_two_gallery,
    TextInputEditText asset_name, asset_desc;//, serial_number;
    TextInputLayout layout_desc, layout_name;//layout_serial,
    private static String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());
    AppCompatButton save_asset;
    private ProgressDialog mProgressDialog;

    Spinner asset_category, asset_type;

    List<AssetCategories> list_asset_categories;
    List<AssetTypes> list_asset_types;
    List<AssetTypes> list_selected_asset_types;

    public AddAssetsFragment() {
        // Required empty public constructor
    }

    public static AddAssetsFragment newInstance(String param1, String param2) {
        AddAssetsFragment fragment = new AddAssetsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_assets, container, false);
//        image_one = (ImageView) view.findViewById(R.id.image_one);
//        image_two = (ImageView) view.findViewById(R.id.image_two);
        image_three = (ImageView) view.findViewById(R.id.image_three);
        save_asset = (AppCompatButton) view.findViewById(R.id.save_asset);
//        image_one_camera = (AppCompatButton) view.findViewById(R.id.image_one_camera);
//        image_one_gallery = (AppCompatButton) view.findViewById(R.id.image_one_gallery);
//        image_two_camera = (AppCompatButton) view.findViewById(R.id.image_two_camera);
//        image_two_gallery = (AppCompatButton) view.findViewById(R.id.image_two_gallery);
        image_three_camera = (AppCompatButton) view.findViewById(R.id.image_three_camera);
        image_three_gallery = (AppCompatButton) view.findViewById(R.id.image_three_gallery);

        asset_name = (TextInputEditText) view.findViewById(R.id.asset_name);
        asset_desc = (TextInputEditText) view.findViewById(R.id.asset_desc);
        // serial_number = (TextInputEditText) view.findViewById(R.id.serial_number);

        // layout_serial = (TextInputLayout) view.findViewById(R.id.layout_serial);
        layout_desc = (TextInputLayout) view.findViewById(R.id.layout_desc);
        layout_name = (TextInputLayout) view.findViewById(R.id.layout_name);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Saving asset");

//        image_one_camera.setOnClickListener(this);
//        image_one_gallery.setOnClickListener(this);
//        image_two_camera.setOnClickListener(this);
//        image_two_gallery.setOnClickListener(this);
        image_three_camera.setOnClickListener(this);
        image_three_gallery.setOnClickListener(this);
        save_asset.setOnClickListener(this);

        // Setting up spinners
        // 1. Asset Category Spinner

        // Load list of categories from the database
        list_asset_categories = MyApplication.getWritableDatabase().readAssetCategories();

        list_asset_types = MyApplication.getWritableDatabase().readAssetTypes();

        // if blank, trigger an AsyncTask to download activity list from the API
        if (list_asset_categories.isEmpty()) {
            new TaskLoadAssetCategories(AddAssetsFragment.this).execute();
        }

        if (list_asset_types.isEmpty()) {
            new TaskLoadAssetTypes(AddAssetsFragment.this).execute();
        }

        asset_category = (Spinner) view.findViewById(R.id.asset_category);
        ArrayAdapter<String> asset_category_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);

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
        asset_type = (Spinner) view.findViewById(R.id.asset_type);

        final ArrayAdapter<String> asset_type_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);

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

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
//            case R.id.image_one_camera:
//                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_ONE);
//                break;
//            case R.id.image_two_camera:
//                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_TWO);
//                break;
            case R.id.image_three_camera:
                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_THREE);
                break;
//            case R.id.image_four_camera:
//                startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_FOUR);
//                break;
//            case R.id.image_one_gallery:
//                Intent getIntent1 = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
//                Intent pickIntent1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
//                startActivityForResult(Intent.createChooser(getIntent1, getString(R.string.select_image)).putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent1}), GALLERY_REQUEST_ONE);
//                break;
//
//            case R.id.image_two_gallery:
//                Intent getIntent2 = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
//                Intent pickIntent2 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
//                startActivityForResult(Intent.createChooser(getIntent2, getString(R.string.select_image)).putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent2}), GALLERY_REQUEST_TWO);
//                break;

            case R.id.image_three_gallery:
                Intent getIntent3 = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                Intent pickIntent3 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
                startActivityForResult(Intent.createChooser(getIntent3, getString(R.string.select_image)).putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent3}), GALLERY_REQUEST_THREE);
                break;

            case R.id.save_asset:
                if (validateField(layout_name, asset_name, 2) && validateField(layout_desc, asset_desc, 5)) {

                    if (image_three_string != null) {
                        createDialog();
                        return;
                    }
                    L.T(MyApplication.getAppContext(), getString(R.string.no_image_error));
                }


                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && data != null) {

            if (requestCode == GALLERY_REQUEST_THREE ) {
                try {
                    //from the gallery
                    InputStream inputStream = MyApplication.getAppContext().getContentResolver().openInputStream(data.getData());
                    if (inputStream != null) {
                        L.m(inputStream.toString());
                        switch (requestCode) {
//                            case GALLERY_REQUEST_ONE:
//                                image_one.setImageBitmap(BitmapFactory.decodeStream(inputStream));
//                                image_one_string = inputStream.toString();
//                                break;
//                            case GALLERY_REQUEST_TWO:
//                                image_two.setImageBitmap(BitmapFactory.decodeStream(inputStream));
//                                image_two_string = inputStream.toString();
//                                break;
                            case GALLERY_REQUEST_THREE:
                                image_three.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                                image_three_string = inputStream.toString();
                                break;

                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {

                Bitmap photo = (Bitmap) data.getExtras().get("data");

                switch (requestCode) {
//                    case CAMERA_REQUEST_ONE:
//                        image_one.setImageBitmap(photo);
//                        image_one_string = ValidationUtil.getStringImage(photo);
//                        break;
//                    case CAMERA_REQUEST_TWO:
//                        image_two.setImageBitmap(photo);
//                        image_two_string = ValidationUtil.getStringImage(photo);
//                        break;
                    case CAMERA_REQUEST_THREE:
                        image_three.setImageBitmap(photo);
                        image_three_string = ValidationUtil.getStringImage(photo);
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
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);


        builder.setMessage("Are you sure you want to save this data? ");

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                saveAsset();
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
        mProgressDialog.show();
//        L.t(MyApplication.getAppContext(), "Saving asset...");
        L.m("Saving asset...");

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, getString(R.string.baseUrl) + "/asset", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse asset) {
                L.m(asset.toString());
                L.t(MyApplication.getAppContext(), "Asset saved successful");
                mProgressDialog.hide();

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
                parameters.put("asset_category", selected_category);
                parameters.put("asset_type", selected_type);
                L.m(parameters.toString());
                return parameters;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
//                params.put("image_one", new DataPart("image_one", AppHelperUtil.getFileDataFromDrawable(getActivity().getBaseContext(), image_one.getDrawable()), "image/jpeg"));
//                params.put("image_two", new DataPart("image_two", AppHelperUtil.getFileDataFromDrawable(getActivity().getBaseContext(), image_two.getDrawable()), "image/jpeg"));
                params.put("image_asset", new DataPart("image_asset", AppHelperUtil.getFileDataFromDrawable(getActivity().getBaseContext(), image_three.getDrawable()), "image/jpeg"));

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

    @Override
    public void onAssetTypesLoaded(ArrayList<AssetTypes> assetTypes) {
        list_asset_types = MyApplication.getWritableDatabase().readAssetTypes();
    }

    @Override
    public void onAssetCategoriesLoaded(ArrayList<AssetCategories> assetCategories) {
        list_asset_categories = MyApplication.getWritableDatabase().readAssetCategories();
    }
}
