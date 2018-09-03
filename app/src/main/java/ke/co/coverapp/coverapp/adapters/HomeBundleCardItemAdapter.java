package ke.co.coverapp.coverapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import com.huxq17.swipecardsview.BaseCardAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ke.co.coverapp.coverapp.Model.HomeBundleCardItemModel;
import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.HomeBundleActivity;
import ke.co.coverapp.coverapp.activities.OpenHomeCoverInfoViewFragment;
import ke.co.coverapp.coverapp.activities.OpenHomeCoverInfoViewFragment2;
import ke.co.coverapp.coverapp.activities.OpenHomeCoverInfoViewFragment3;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.fragments.HomeCoverInfoViewFragment;
import ke.co.coverapp.coverapp.log.L;

import static android.app.PendingIntent.getActivity;

/**
 * Created by user001 on 13/02/2018.
 */

public class HomeBundleCardItemAdapter extends  RecyclerView.Adapter<HomeBundleCardItemAdapter.ViewHolder> {

    private ArrayList<HomeBundleCardItemModel> mDataset1;
    LinearLayout simple_plan_linear;
    ImageView coverTypeImageView;

    public HomeBundleCardItemAdapter(ArrayList<HomeBundleCardItemModel> mDataset1) {
        this.mDataset1 = mDataset1;
        this.context = context;
    }

    private Context context;

    @Override
    public HomeBundleCardItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.home_bundle_card_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        coverTypeImageView = v.findViewById(R.id.coverTypeImageView);
        return vh;
    }

    @Override
    public void onBindViewHolder(HomeBundleCardItemAdapter.ViewHolder holder, int position) {

        final HomeBundleCardItemModel homeBundleCardItemModel = mDataset1.get(position);

        holder.textViewCoverType.setText(homeBundleCardItemModel.getTextViewCoverType());
        holder.textViewCostPerMonth.setText(homeBundleCardItemModel.getTextViewCostPerMonth());
        holder.textViewYourCover.setText(homeBundleCardItemModel.getTextViewYourCover());
        holder.textViewDescription.setText(homeBundleCardItemModel.getTextViewDescription());
        String CoverTypeText = holder.textViewCoverType.getText().toString();


        if (CoverTypeText == "White"){
            holder.swipeCardsViewLinearLayout.setBackgroundColor(Color.WHITE);

            coverTypeImageView.setImageResource(R.drawable.whitecover);
        }
        if (CoverTypeText == "Yellow"){
            coverTypeImageView.setImageResource(R.drawable.yellowcover);
        }
        if (CoverTypeText == "Red"){
            coverTypeImageView.setImageResource(R.drawable.redcover);
        }
holder.swipeCardsViewLinearLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        TextView textViewCoverType =  v.findViewById(R.id.textViewCoverType);
        String textViewCoverType2 = textViewCoverType.getText().toString();

        if (textViewCoverType2 == "White"){

           Intent intent = new Intent(v.getContext(), OpenHomeCoverInfoViewFragment.class);
         // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           v.getContext().startActivity(intent);

        }
        if (textViewCoverType2 == "Yellow"){
            Intent intent = new Intent(v.getContext(), OpenHomeCoverInfoViewFragment2.class);
         //   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);
        }
        if (textViewCoverType2 == "Red"){
            Intent intent = new Intent(v.getContext(), OpenHomeCoverInfoViewFragment3.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);
        }
    }
});
    }

    @Override
    public int getItemCount() {
        return mDataset1.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        public  TextView textViewCoverType;
        public  TextView textViewCostPerMonth;
        public  TextView textViewYourCover;
        public  TextView textViewDescription;
        public  LinearLayout swipeCardsViewLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
             textViewCoverType =  itemView.findViewById(R.id.textViewCoverType);
             textViewCostPerMonth = itemView.findViewById(R.id.textViewCostPerMonth);
             textViewYourCover = itemView.findViewById(R.id.textViewYourCover);
             textViewDescription = itemView.findViewById(R.id.textViewDescription);
            swipeCardsViewLinearLayout = itemView.findViewById(R.id.swipeCardsViewLinearLayout);
        }
    }

}
