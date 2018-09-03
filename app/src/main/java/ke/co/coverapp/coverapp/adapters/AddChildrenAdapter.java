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

public class AddChildrenAdapter extends RecyclerView.Adapter<AddChildrenAdapter.ViewHolder> implements View.OnClickListener {

    private ArrayList<String> addchildrenDataset;
    private Context context;

    public AddChildrenAdapter(ArrayList<String> addchildrenDataset) {
        this.addchildrenDataset = addchildrenDataset;
    }

    @Override
    public AddChildrenAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_children_cardview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();

        return vh;
    }

    @Override
    public void onBindViewHolder(AddChildrenAdapter.ViewHolder holder, int position) {

        holder.textView96.setText(addchildrenDataset.get(position));

    }

    @Override
    public int getItemCount() {
        return addchildrenDataset.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView96;
        public ViewHolder(View itemView) {
            super(itemView);
            textView96 = itemView.findViewById(R.id.textView96);
        }
    }
}
