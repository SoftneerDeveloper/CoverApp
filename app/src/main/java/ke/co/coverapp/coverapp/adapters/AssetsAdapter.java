package ke.co.coverapp.coverapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.EditAssetActivity;
import ke.co.coverapp.coverapp.activities.MainActivity;
import ke.co.coverapp.coverapp.activities.ReportClaimsActivity;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleyMultipartRequest;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Assets;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.utility.ValidationUtil;
import ke.co.coverapp.coverapp.utility.network.VolleyCustomErrorHandler;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.ACCESS_TOKEN;

/**
 * Created by Clifford Owino on 3/17/2017.
 */

public class AssetsAdapter extends RecyclerView.Adapter<AssetsAdapter.ViewHolderAssets> {
    private LayoutInflater layoutInflater;
    private ArrayList<Assets> assetsList = new ArrayList<>();
    private Context context;
    int id_position;
    private static String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());

    public AssetsAdapter(Context context, ArrayList<Assets> assetsList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.assetsList = assetsList;
        L.m("Binding Adaptor constructor AssetsAdapter");

    }

    public void setAssetsList(ArrayList<Assets> assetsList) {
        this.assetsList = assetsList;
        notifyItemRangeChanged(0, assetsList.size());

    }

    public void addToAssetsList(ArrayList<Assets> newAssetsList) {

        int previousSize = assetsList.size();
        assetsList.clear(); // Clear previous list
        assetsList.addAll(newAssetsList); // Confirm this method, may produce duplicates | Add new list
        notifyItemRangeChanged(previousSize, newAssetsList.size());

    }

    @Override
    public ViewHolderAssets onCreateViewHolder(ViewGroup parent, int viewType) {

        View layout = layoutInflater.inflate(R.layout.item_asset, parent, false);
        return new ViewHolderAssets(layout);
    }


    @Override
    public void onBindViewHolder(ViewHolderAssets holder, int position) {
        id_position = position;

        Assets current = assetsList.get(position);

        holder.name.setText(current.getName());

        // Set asset ID
        int hello = Integer.parseInt(current.getId());

        holder.id.setId(hello);

        holder.description.setText(current.getDescription());
        Picasso.with(MyApplication.getAppContext())
                .load(current.getImage_one())
                .placeholder(R.drawable.side_icon)
                .error(R.drawable.side_icon)
                .resize(100,100)
                .centerCrop()
                .into(holder.image_one);

        //Toast.makeText(context, ""+current.getImage_one() +" : "+id_position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        if (assetsList != null) {
            return assetsList.size();
        }
        return 0;
    }

    /*
    Displays the assets a user has saved
     */
    public class ViewHolderAssets extends RecyclerView.ViewHolder {

        private TextView name, description;
        private ImageView image_one;//, image_two, image_three;
        private LinearLayout id;
        Button edit, delete;

        public ViewHolderAssets(View itemView) {
            super(itemView);
            L.m("ViewHolderAssets");

            name = (TextView) itemView.findViewById(R.id.name);
            description = (TextView) itemView.findViewById(R.id.description);
            image_one  = (ImageView) itemView.findViewById(R.id.image_one);
            id = (LinearLayout) itemView.findViewById(R.id.asset_id);

            // Uncomment when a user can add more than one image for each asset

//            image_two  = (ImageView) itemView.findViewById(R.id.image_two);
//            image_three  = (ImageView) itemView.findViewById(R.id.image_three);

            // Set intent for delete button
            delete = (Button) itemView.findViewById(R.id.delete_asset);
            if (delete != null) {
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //warning b4 you delete asset
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

                        builder.setMessage("You are about to delete asset "+name.getText().toString());

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // Delete asset process
                                VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, "http://api.coverappke.com/assets/delete", new Response.Listener<NetworkResponse>() {
                                    @Override
                                    public void onResponse(NetworkResponse asset) {
                                        // Take to next page
                                        L.m("Success - Check Claim Viability");
                                        L.T(context, "Asset deleted.");
                                        //Toast.makeText(context, "Asset deleted", Toast.LENGTH_LONG).show();

                                        // Update list
                                        assetsList.remove(getAdapterPosition());
                                        notifyItemRemoved(getAdapterPosition());
                                        notifyDataSetChanged();

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // Show error dialog
                                        L.m("Error deleting asset");
                                        L.T(context, "There was an error deleting the asset. Kindly try again.");


                                        // TODO: Return appropriate error e.g. network error etc
                                        NetworkResponse response = error.networkResponse;

                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> parameters = new HashMap<String, String>();
                                        parameters.put("assetId", Integer.toString(id.getId()));
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
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();

                    }
                });
            }

            // Set intent for edit button
            edit = (Button) itemView.findViewById(R.id.edit_asset);
            if (edit != null) {
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Displaying edit assets activity
//                        String s = (name.getText().toString());
//                        String d = description.getText().toString();
//                        Bitmap image=((BitmapDrawable)image_one.getDrawable()).getBitmap();

                       //  Toast.makeText(context, "name: "+get, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MyApplication.getAppContext(), EditAssetActivity.class);

                        // Passing variables
                        intent.putExtra("assetName", name.getText().toString());
                        intent.putExtra("assetDescription", description.getText().toString());
                        intent.putExtra("assetId", id.getId());
                        //TODO pick the right category and type
                        intent.putExtra("assetCategory", "0");
                        intent.putExtra("type", "0");
                        image_one.buildDrawingCache();
                        Bitmap image= image_one.getDrawingCache();
                        Bundle extras = new Bundle();
                        extras.putParcelable("assetImage", image);

                        // Getting bitmap
                        if(image_one.getDrawable() instanceof BitmapDrawable) {
//                            Bitmap bm = ((BitmapDrawable) image_one.getDrawable()).getBitmap();
//
//                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                            byte[] byteArray = stream.toByteArray();

                           // intent.putExtra("assetImage", assetsList.get(id_position).getImage_one());


                            //intent.putExtra("assetImage", image);



                            //context.startActivity(intent);
                            intent.putExtras(extras);

                            context.startActivity(intent);


                        } else {
                            L.t(MyApplication.getAppContext(), "Please wait for the image to load");
                        }
                    }
                });
            }

        }
    }
}

