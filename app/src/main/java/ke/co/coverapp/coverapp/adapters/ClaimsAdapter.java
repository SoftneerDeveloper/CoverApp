package ke.co.coverapp.coverapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.EditClaimActivity;
import ke.co.coverapp.coverapp.activities.ReportClaimsActivity;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.network.VolleyMultipartRequest;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Claims;
import ke.co.coverapp.coverapp.pojo.Keys;
import ke.co.coverapp.coverapp.utility.ValidationUtil;

import static ke.co.coverapp.coverapp.pojo.Keys.keys.ACCESS_TOKEN;

/**
 * Created by nick on 9/8/17.
 */

public class ClaimsAdapter extends RecyclerView.Adapter<ClaimsAdapter.ViewHolderClaims> {
    private LayoutInflater layoutInflater;
    private ArrayList<Claims> claimsList = new ArrayList<>();
    private Context context;
    int id_position;

    private static String token = MyApplication.readFromPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefault());

    public ClaimsAdapter(Context context, ArrayList<Claims> claimsList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.claimsList = claimsList;
        L.m("Binding Adaptor constructor ClaimsAdapter");

    }

    public void setClaimsList(ArrayList<Claims> claimsList) {
        this.claimsList = claimsList;
        notifyItemRangeChanged(0, claimsList.size());

    }

    public void addToClaimsList(ArrayList<Claims> newClaimsList) {

        int previousSize = claimsList.size();
        claimsList.clear(); // Clear previous list
        claimsList.addAll(newClaimsList); // Confirm this method, may produce duplicates | Add new list
        notifyItemRangeChanged(previousSize, newClaimsList.size());

    }

    @Override
    public ClaimsAdapter.ViewHolderClaims onCreateViewHolder(ViewGroup parent, int viewType) {

        View layout = layoutInflater.inflate(R.layout.item_claim, parent, false);
        return new ClaimsAdapter.ViewHolderClaims(layout);
    }


    @Override
    public void onBindViewHolder(ClaimsAdapter.ViewHolderClaims holder, int position) {

        Claims current = claimsList.get(position);

        holder.title.setText("CLAIM " + current.getAccidentDate() + "-" + current.getClaimId());

        int hello = Integer.parseInt(current.getClaimId());
        //asset_name = current.getAssetsClaimed();

        holder.id.setId(hello);
        holder.asset_name = current.getAccidentType();
        holder.accidentType.setText("Accident Type: " + current.getAccidentType());
        holder.accidentLoc.setText("Accident Location: " + current.getAccidentLocation());
        holder.accidentDate.setText("Accident Date: " + current.getAccidentDate());

    }

    @Override
    public int getItemCount() {
        if (claimsList != null) {
            return claimsList.size();
        }
        return 0;
    }

    /*
    Displays the assets a user has saved
     */
    public class ViewHolderClaims extends RecyclerView.ViewHolder {

        private TextView title, accidentType, accidentLoc, accidentDate;
        private LinearLayout id;
        Button edit, delete;
        String asset_name;

        public ViewHolderClaims(View itemView) {
            super(itemView);
            L.m("ViewHolderClaims");

            title = (TextView) itemView.findViewById(R.id.claim_title);
            accidentType = (TextView) itemView.findViewById(R.id.accidentType);
            accidentLoc = (TextView) itemView.findViewById(R.id.accidentLoc);
            accidentDate = (TextView) itemView.findViewById(R.id.accidentDate);
            id = (LinearLayout) itemView.findViewById(R.id.claim_id);

            // Set intent for delete button
            delete = (Button) itemView.findViewById(R.id.delete_claim);
            if (delete != null) {
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

                        builder.setMessage("are sure you want to delete this claim?");

                        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Delete claim process
                                VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, "http://api.coverappke.com/claims/delete", new Response.Listener<NetworkResponse>() {
                                    @Override
                                    public void onResponse(NetworkResponse asset) {
                                        // Take to next page
                                        L.m("Success - Claim deleted.");
                                        L.T(context, "Claim deleted.");

                                        // Update list
                                        claimsList.remove(getAdapterPosition());
                                        notifyItemRemoved(getAdapterPosition());
                                        notifyDataSetChanged();

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // Show error dialog
                                        L.m("Error deleting claim.");
                                        L.T(context, "There was an error deleting the claim. Kindly try again.");


                                        // TODO: Return appropriate error e.g. network error etc
                                        NetworkResponse response = error.networkResponse;

                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> parameters = new HashMap<String, String>();
                                        parameters.put("claimId", Integer.toString(id.getId()));
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

                                dialog.dismiss();
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
            edit = (Button) itemView.findViewById(R.id.edit_claim);
            if (edit != null) {
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent editClaimIntent = new Intent(MyApplication.getAppContext(), EditClaimActivity.class);

                        // Passing variables
                        editClaimIntent.putExtra("claimId", id.getId());
                        editClaimIntent.putExtra("claimName", asset_name);

//                        L.m("Claim ID (Intent): " + id.getId());
//                        editClaimIntent.putExtra("id", id.getId());

                        context.startActivity(editClaimIntent);
                    }
                });
            }

        }
    }
}
