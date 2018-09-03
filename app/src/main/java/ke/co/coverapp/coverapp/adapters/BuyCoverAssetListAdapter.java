package ke.co.coverapp.coverapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.BuyHomeCover;
import ke.co.coverapp.coverapp.log.L;

import static android.R.id.list;

/**
 * Created by nick on 8/30/17.
 */

public class BuyCoverAssetListAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater;
    ArrayList<String> asset_id;
    ArrayList<String> asset_name;

    public BuyCoverAssetListAdapter(BuyHomeCover buyHomeCover, ArrayList<String> assetName, ArrayList<String> assetId) {
        context = buyHomeCover;
        asset_name = assetName;
        asset_id = assetId;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return asset_name.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder{
        TextView assetNameTextView;
        TextView assetIdTwoTextView;
        Button hc_btn_view;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_asset_list_two, null);
        holder.assetNameTextView = (TextView) rowView.findViewById(R.id.hc_asset_name);
        holder.assetIdTwoTextView = (TextView) rowView.findViewById(R.id.hc_asset_id_two);
        holder.hc_btn_view = (Button) rowView.findViewById(R.id.hc_btn_view);

        holder.assetIdTwoTextView.setText("" +  asset_id.get(position) + ". ");
        holder.assetNameTextView.setText(asset_name.get(position));

        holder.hc_btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                L.t(context, asset_name.get(position) + " removed");
                asset_id.remove(position);
                asset_name.remove(position);
                notifyDataSetChanged();
//                this.notify();
            }
        });

        // TODO: View asset on long click
//        rowView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                L.t(context, "You clicked asset ID: " + asset_id.get(position) + "; Asset name: " + asset_name.get(position));
//            }
//        });

        return rowView;
    }
}
