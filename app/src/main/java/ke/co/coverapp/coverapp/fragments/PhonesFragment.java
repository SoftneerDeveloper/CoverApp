package ke.co.coverapp.coverapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.AddAssetActivity;
import ke.co.coverapp.coverapp.activities.EditAssetActivity;
import ke.co.coverapp.coverapp.activities.MainActivity;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.callbacks.AssetsLoadedListener;
import ke.co.coverapp.coverapp.database.CoverAppDB;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleyMultipartRequest;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Assets;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.tasks.TaskLoadAssets;
import ke.co.coverapp.coverapp.utility.ValidationUtil;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.ACCESS_TOKEN;

/**
 * Created by nick on 8/23/17.
 */

public class PhonesFragment extends Fragment implements View.OnClickListener, AssetsLoadedListener{ //, AssetsLoadedListener

    LinearLayout phones_your_phone, phones_spouse_phone, your_phone_action_buttons, spouse_phone_action_buttons;
    List<Assets> your_phone;
    List<Assets> spouse_phone;
    Button btn_delete_spouse_phone, btn_edit_spouse_phone, btn_delete_your_phone, btn_edit_your_phone;
    TextView your_phone_desc_text, spouse_phone_desc_text;
    private static String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());

    public PhonesFragment() {
        // Required empty public constructor
    }

    public static PhonesFragment newInstance() {
        PhonesFragment fragment = new PhonesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_phones, container, false);

        phones_your_phone = (LinearLayout) view.findViewById(R.id.phones_your_phone);
        phones_spouse_phone = (LinearLayout) view.findViewById(R.id.phones_spouse_phone);

        phones_your_phone.setOnClickListener(this);
        phones_spouse_phone.setOnClickListener(this);

        spouse_phone_action_buttons = (LinearLayout) view.findViewById(R.id.spouse_phone_action_buttons);
        your_phone_action_buttons = (LinearLayout) view.findViewById(R.id.your_phone_action_buttons);

        your_phone = MyApplication.getWritableDatabase().readAsset("1");
        spouse_phone = MyApplication.getWritableDatabase().readAsset("14");

        btn_delete_spouse_phone = (Button) view.findViewById(R.id.btn_delete_spouse_phone);
        btn_edit_spouse_phone = (Button) view.findViewById(R.id.btn_edit_spouse_phone);
        btn_delete_your_phone = (Button) view.findViewById(R.id.btn_delete_your_phone);
        btn_edit_your_phone = (Button) view.findViewById(R.id.btn_edit_your_phone);

        your_phone_desc_text = (TextView) view.findViewById(R.id.your_phone_desc_text);
        spouse_phone_desc_text = (TextView) view.findViewById(R.id.spouse_phone_desc_text);

        btn_delete_spouse_phone.setOnClickListener(this);
        btn_edit_spouse_phone.setOnClickListener(this);
        btn_delete_your_phone.setOnClickListener(this);
        btn_edit_your_phone.setOnClickListener(this);

        if (your_phone.size() > 0) {
            // Show edit and view button
            your_phone_action_buttons.setVisibility(View.VISIBLE);

            // Set card not clickable false
            phones_your_phone.setClickable(false);

            // Change description text
            your_phone_desc_text.setText("Use the action buttons below to edit/view your saved phone.");
        }

        if (spouse_phone.size() > 0) {
            // Show edit and view button
            spouse_phone_action_buttons.setVisibility(View.VISIBLE);

            phones_spouse_phone.setClickable(false);

            // Change description text
            spouse_phone_desc_text.setText("Use the action buttons below to edit/view your spouse's saved phone.");
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ((MainActivity) getActivity()).createDialog();
            return true;
        }
        if (id == R.id.action_settings) {
            ((MainActivity) getActivity()).displayView(R.id.nav_settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.phones_your_phone:
                Bundle bundle = new Bundle();
                bundle.putString("category", "1");
                bundle.putString("type", "1");
                Intent intent = new Intent(getActivity(), AddAssetActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

                break;

            case R.id.phones_spouse_phone:
                Bundle bundle_two = new Bundle();
                bundle_two.putString("category", "1");
                bundle_two.putString("type", "14");
                Intent intent_two = new Intent(getActivity(), AddAssetActivity.class);
                intent_two.putExtras(bundle_two);
                startActivity(intent_two);

                break;

            case R.id.btn_delete_your_phone:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Warning")
                        .setMessage("Are you sure you want to delete this asset?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAsset(your_phone.get(0).getId());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();



                break;

            case R.id.btn_edit_your_phone:
                // Displaying edit assets activity
                Intent intentEditYourPhone = new Intent(MyApplication.getAppContext(), EditAssetActivity.class);
                // Passing variables
                intentEditYourPhone.putExtra("assetName", your_phone.get(0).getName());
                intentEditYourPhone.putExtra("assetDescription", your_phone.get(0).getDescription());
                intentEditYourPhone.putExtra("assetId", your_phone.get(0).getId());
                intentEditYourPhone.putExtra("assetImage", your_phone.get(0).getImage_one());
                intentEditYourPhone.putExtra("assetCategory", "1");
                intentEditYourPhone.putExtra("type", "1");
                getActivity().startActivity(intentEditYourPhone);


//                try {



//                    URL url = new URL(your_phone.get(0).getImage_one());
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                InputStream input = connection.getInputStream();
//                myBitmap = BitmapFactory.decodeStream(input);

//                    URL url = new URL(your_phone.get(0).getImage_one());
//                    Bitmap myBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//
//                    Bundle extras = new Bundle();
//                    extras.putParcelable("assetImage", myBitmap);
//                    intentEditYourPhone.putExtras(extras);
//
//                    Toast.makeText(getActivity(), ""+your_phone.get(0).getImage_one(), Toast.LENGTH_SHORT).show();



//                } catch (IOException e) {
//                    e.printStackTrace();
//                }



                break;

            case R.id.btn_delete_spouse_phone:

                new AlertDialog.Builder(getActivity())
                        .setTitle("Warning")
                        .setMessage("Are you sure you want to delete this asset?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAsset(spouse_phone.get(0).getId());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();


                break;

            case R.id.btn_edit_spouse_phone:
                // Displaying edit assets activity
                Intent intentEditSpousePhone = new Intent(MyApplication.getAppContext(), EditAssetActivity.class);

                // Passing variables
                intentEditSpousePhone.putExtra("assetName", spouse_phone.get(0).getName());
                intentEditSpousePhone.putExtra("assetDescription", spouse_phone.get(0).getDescription());
                intentEditSpousePhone.putExtra("assetId", spouse_phone.get(0).getId());
                intentEditSpousePhone.putExtra("assetImage", spouse_phone.get(0).getImage_one());
                intentEditSpousePhone.putExtra("assetCategory", "1");
                intentEditSpousePhone.putExtra("type", "14");

                getActivity().startActivity(intentEditSpousePhone);

                break;

            default:

                break;
        }

    }


    public void deleteAsset(final String assetId){
        // Delete asset process
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, "http://api.coverappke.com/assets/delete", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse asset) {
                // Take to next page
                L.m("Success - Check Claim Viability");
                L.T(getContext(), "Asset deleted.");

//                // Update list
                new TaskLoadAssets(PhonesFragment.this, 0).execute();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Show error dialog
                L.m("Error deleting asset");
                L.T(getContext(), "There was an error deleting the asset. Kindly try again.");


                // TODO: Return appropriate error e.g. network error etc
                NetworkResponse response = error.networkResponse;

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("assetId", assetId);
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

    public void selectViewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Do you want to view added phones or add a new one?");
        builder.setPositiveButton("View Asset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        L.t(getActivity(), "View asset clicked");
                    }

                }
        ).setNegativeButton("Add Asset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                L.t(getActivity(), "Add asset clicked");
            }
        }).show();
    }

    @Override
    public void onAssetsLoaded(ArrayList<Assets> listFeatures) {
        // Restart fragment
        //getFragmentManager().beginTransaction().replace(R.id.content_frame, PhonesFragment.newInstance()).commit();
        FragmentTransaction ftr = getFragmentManager().beginTransaction();
        ftr.detach(PhonesFragment.this).attach(PhonesFragment.this).commit();
    }
}
