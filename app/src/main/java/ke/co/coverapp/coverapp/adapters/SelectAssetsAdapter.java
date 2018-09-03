package ke.co.coverapp.coverapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.Assets;

/**
 * Created by intrepid on 7/16/17.
 */

public class SelectAssetsAdapter extends RecyclerView.Adapter<SelectAssetsAdapter.ViewHolderAssets>{
    private LayoutInflater layoutInflater;
    private ArrayList<Assets> assetsList = new ArrayList<>();
    private Context context;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    public SelectAssetsAdapter(Context context, ArrayList<Assets> assetsList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.assetsList = assetsList;
        onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Assets asset =  (Assets) compoundButton.getTag();

                if (isChecked) {
                    // Status set
                    asset.setStatus("Checked");

                    L.m(asset.getName() + " has been checked");
                } else {
                    asset.setStatus("Unchecked");
                    L.m(asset.getName() + " has been unchecked");
                }
            }
        };
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
    public SelectAssetsAdapter.ViewHolderAssets onCreateViewHolder(ViewGroup parent, int viewType) {

        View layout = layoutInflater.inflate(R.layout.item_home_cover_asset_select, parent, false);
        return new SelectAssetsAdapter.ViewHolderAssets(layout);
    }

    @Override
    public void onBindViewHolder(SelectAssetsAdapter.ViewHolderAssets holder, int position) {
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

        if(current.getStatus() != null) {
            L.m("This is the status: " + current.getStatus() + " for asset - " + current.getName());
            if (current.getStatus().equals("Checked")) {
                holder.checkbox.setChecked(true);
            } else {
                holder.checkbox.setChecked(false);
            }
        } else {
            L.m("Null found - getStatus() - for asset: " + current.getName());
            holder.checkbox.setChecked(false);
        }

        holder.checkbox.setTag(current);
        holder.checkbox.setOnCheckedChangeListener(onCheckedChangeListener);

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
    public class ViewHolderAssets extends RecyclerView.ViewHolder
    {

        private TextView name, description;
        private ImageView image_one;
        private LinearLayout id;

        CheckBox checkbox;

        public ViewHolderAssets(View itemView) {
            super(itemView);
            L.m("ViewHolderAssets");

            name = (TextView) itemView.findViewById(R.id.name);
            description = (TextView) itemView.findViewById(R.id.description);
            image_one  = (ImageView) itemView.findViewById(R.id.image_one);
            id = (LinearLayout) itemView.findViewById(R.id.asset_id);

            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);

            // Push into array every selected asset

        }
    }
}
