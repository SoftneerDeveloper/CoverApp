package ke.co.coverapp.coverapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.R;

/**
 * Created by user001 on 19/03/2018.
 */

public class AddAdultsAdapter extends RecyclerView.Adapter<AddAdultsAdapter.ViewHolder> implements View.OnClickListener{

    private ArrayList<String> addadultsDataset;
    private Context context;

    public AddAdultsAdapter(ArrayList<String> addadultsDataset) {
        this.addadultsDataset = addadultsDataset;
    }

    @Override
    public AddAdultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_adults_cardview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(AddAdultsAdapter.ViewHolder holder, int position) {

        holder.textView95.setText(addadultsDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return addadultsDataset.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView95;
        public ViewHolder(View itemView) {
            super(itemView);
            textView95 = itemView.findViewById(R.id.textView95);
        }
    }
}
