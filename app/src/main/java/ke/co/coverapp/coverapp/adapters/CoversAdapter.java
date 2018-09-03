package ke.co.coverapp.coverapp.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.ViewCoverActivity;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.Covers;

/**
 * Created by nick on 10/11/17.
 */

public class CoversAdapter extends RecyclerView.Adapter<CoversAdapter.ViewHolderCovers> {

    private LayoutInflater layoutInflater;
    private ArrayList<Covers> coversList;
    private Context context;
    String coverExpiryDate;

    public CoversAdapter(Context context, ArrayList<Covers> coversList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.coversList = coversList;

    }

    public void setCoversList(ArrayList<Covers> coversList) {
        this.coversList = coversList;
        notifyItemRangeChanged(0, coversList.size());
    }

    public void addToCoversList(ArrayList<Covers> newCoversList) {

        int previousSize = coversList.size();
        coversList.clear(); // Clear previous list
        coversList.addAll(newCoversList); // Confirm this method, may produce duplicates | Add new list
        notifyItemRangeChanged(previousSize, newCoversList.size());

    }

    @Override
    public ViewHolderCovers onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = layoutInflater.inflate(R.layout.item_cover, parent, false);
        return new ViewHolderCovers(layout);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolderCovers holder, int position) {
        Covers current = coversList.get(position);
        holder.coverType.setText(current.getCoverPackage().replace("_", " ").toUpperCase()); //+ " " + current.getPolicyNumber()
        holder.coverDesc.setText("Policy Number: " + current.getPolicyNumber());
        holder.coverId.setId(Integer.parseInt(current.getCoverId()));
        holder.tnxCode.setText("Transaction Code: " + current.getTnxCode());
        holder.purchaseDate.setText("Purchase Date: " + current.getCreatedAt());

        coverExpiryDate=current.getExpiryDate();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date strDate = null;
        try {
            strDate = sdf.parse(coverExpiryDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (System.currentTimeMillis() > strDate.getTime()) {
            holder.coverStatus.setText("Cover Status: Expired" );
            holder.coverStatus.setBackgroundColor(Color.parseColor("#EF4A23"));
           // holder.coverStatus.setBackground(context.getDrawable(R.drawable.custom_bg));
        }
        else if (System.currentTimeMillis() <= strDate.getTime())
        {
            holder.coverStatus.setBackgroundColor(Color.parseColor("#007a00"));
          //  holder.coverStatus.setBackground(context.getDrawable(R.drawable.green_bg));
            holder.coverStatus.setText("Cover Status: Active" );

        }
    }

    @Override
    public int getItemCount() {
        if (coversList != null) {
            return coversList.size();
        }
        return 0;
    }


    public class ViewHolderCovers extends RecyclerView.ViewHolder {

        private TextView coverType, coverDesc, tnxCode, purchaseDate, coverStatus;
        private LinearLayout coverId;
        Button viewCover;

        public ViewHolderCovers(View itemView) {
            super(itemView);

            coverType = (TextView) itemView.findViewById(R.id.coverType);
            coverDesc = (TextView) itemView.findViewById(R.id.coverDesc);
            coverStatus = (TextView) itemView.findViewById(R.id.coverStatus);
            coverId = (LinearLayout) itemView.findViewById(R.id.coverId);
            viewCover = (Button) itemView.findViewById(R.id.viewCover);
            tnxCode = (TextView) itemView.findViewById(R.id.tnxCode);
            purchaseDate = (TextView) itemView.findViewById(R.id.purchaseDate);

            // TODO: Handle onClick
            if (viewCover != null) {
                viewCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
//
//                        builder.setMessage("You will be able to view details of your cover once we do valuation of the assets under it. Thank you.");
//
//                        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//
//                        builder.show();

                        // Open Cover Details Activity
                        Intent openCoverDetails = new Intent(MyApplication.getAppContext(), ViewCoverActivity.class);
                        openCoverDetails.putExtra("coverId", coverId.getId());
                        openCoverDetails.putExtra("policyNumber", coversList.get(getAdapterPosition()).getPolicyNumber());
                        openCoverDetails.putExtra("tnxCode", coversList.get(getAdapterPosition()).getTnxCode());
                        openCoverDetails.putExtra("purchaseDate", coversList.get(getAdapterPosition()).getCreatedAt());
                        openCoverDetails.putExtra("paymentSchedule", coversList.get(getAdapterPosition()).getNumPayments());
                        openCoverDetails.putExtra("paymentAmount", coversList.get(getAdapterPosition()).getSignUpCost());
                        openCoverDetails.putExtra("coverType", coversList.get(getAdapterPosition()).getCoverPackage());
                        openCoverDetails.putExtra("expiryDate", coversList.get(getAdapterPosition()).getExpiryDate());
                        context.startActivity(openCoverDetails);

                    }
                });
            }
        }
    }
}
