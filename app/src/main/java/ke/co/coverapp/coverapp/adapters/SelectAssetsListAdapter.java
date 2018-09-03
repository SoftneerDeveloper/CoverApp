package ke.co.coverapp.coverapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.Assets;


public class SelectAssetsListAdapter extends RecyclerView.Adapter<SelectAssetsListAdapter.ViewHolderAssets> {
    private LayoutInflater layoutInflater;
    private ArrayList<Assets> assetsListTwo = new ArrayList<>();
    private Context context;

    public SelectAssetsListAdapter(Context context, ArrayList<Assets> assetsListTwo) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.assetsListTwo = assetsListTwo;
        L.m("Binding Adaptor constructor AssetsAdapter");

    }

    public void setAssetsList(ArrayList<Assets> assetsListTwo) {
        this.assetsListTwo = assetsListTwo;
        L.m("AssetListTwo size: " + assetsListTwo.size());
        notifyItemRangeChanged(0, assetsListTwo.size());

    }

    public void addToAssetsList(ArrayList<Assets> newAssetsList) {

        int previousSize = assetsListTwo.size();
        assetsListTwo.clear(); // Clear previous list
        assetsListTwo.addAll(newAssetsList); // Confirm this method, may produce duplicates | Add new list
        notifyItemRangeChanged(previousSize, newAssetsList.size());

    }

    @Override
    public SelectAssetsListAdapter.ViewHolderAssets onCreateViewHolder(ViewGroup parent, int viewType) {

        View layout = layoutInflater.inflate(R.layout.item_asset_list, parent, false);
        return new SelectAssetsListAdapter.ViewHolderAssets(layout);
    }


    @Override
    public void onBindViewHolder(SelectAssetsListAdapter.ViewHolderAssets holder, int position) {

        Assets current = assetsListTwo.get(position);

        holder.name.setText(current.getName());

        // Set asset ID
        int hello = Integer.parseInt(current.getId());

        holder.id.setId(hello);

    }

    @Override
    public int getItemCount() {
        if (assetsListTwo != null) {
            return assetsListTwo.size();
        }
        return 0;
    }

    /*
    Displays the assets a user has saved
     */
    public class ViewHolderAssets extends RecyclerView.ViewHolder {

        private TextView name;
        private GridLayout id;
        public ViewHolderAssets(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.hc_asset_name);
            id = (GridLayout) itemView.findViewById(R.id.hc_asset_id);

        }
    }
}

