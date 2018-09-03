package ke.co.coverapp.coverapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.EditAssetActivity;
import ke.co.coverapp.coverapp.activities.EditVehicleActivity;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.Vehicle;

/**
 * Created by Clifford Owino on 3/24/2017.
 */

public class VehicleAdapter  extends RecyclerView.Adapter<VehicleAdapter.ViewHolderVehicle> {
    private LayoutInflater layoutInflater;
    private ArrayList<Vehicle> vehicleList = new ArrayList<>();
    private final OnVehicleClickListener listener;
    private Context context;

    public interface OnVehicleClickListener {
        /**
         * The method to bind the click event
         * @param vehicle The clicked item
         */
        void onItemClick(Vehicle vehicle);
    }

    public VehicleAdapter(Context context, ArrayList<Vehicle> vehicleList, OnVehicleClickListener listener ) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.listener = listener;
        this.vehicleList = vehicleList;
    }

    public void setVehiclesList(ArrayList<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
        notifyItemRangeChanged(0, vehicleList.size());

    }

    public void addToVehiclesList(ArrayList<Vehicle> newVehicleList) {

        int previousSize = vehicleList.size();
        vehicleList.clear(); // Clear previous list
        vehicleList.addAll(newVehicleList);// Confirm this method, may produce duplicates | Update vehicleList
        notifyItemRangeChanged(previousSize, newVehicleList.size());

    }

    @Override
    public ViewHolderVehicle onCreateViewHolder(ViewGroup parent, int viewType) {

        View layout = layoutInflater.inflate(R.layout.item_vehicle, parent, false);
        return new ViewHolderVehicle(layout);
    }


    @Override
    public void onBindViewHolder(ViewHolderVehicle holder, int position) {

        Vehicle current = vehicleList.get(position);

        holder.plate_number.setText(current.getPlate_first()+" "+current.getPlate_last());
        holder.vehicle_make.setText(current.getVehicle_make());
        holder.yom.setText(current.getYom());
        holder.mileage.setText(current.getOdometer());
        holder.vehicle_cc.setText(current.getEngine_capacity());
        holder.vehicle_value_before.setText(current.getCar_value_before());
        holder.vehicle_value_after.setText(current.getCar_value_after());
        holder.date_added.setText(current.getDate_added());
        holder.vehicle_covered.setText((current.getIs_covered().matches("N") ? "NO" : "YES"));

        Picasso.with(MyApplication.getAppContext())
                .load(current.getImage_one())
                .placeholder(R.drawable.side_icon)
                .error(R.drawable.side_icon)
                .resize(50,50)
                .centerCrop()
                .into(holder.image_one);

        Picasso.with(MyApplication.getAppContext())
                .load(current.getImage_two())
                .placeholder(R.drawable.side_icon)
                .error(R.drawable.side_icon)
                .resize(50,50)
                .centerCrop()
                .into(holder.image_two);

        Picasso.with(MyApplication.getAppContext())
                .load(current.getImage_three())
                .placeholder(R.drawable.side_icon)
                .error(R.drawable.side_icon)
                .resize(50,50)
                .centerCrop()
                .into(holder.image_three);

        Picasso.with(MyApplication.getAppContext())
                .load(current.getImage_four())
                .placeholder(R.drawable.side_icon)
                .error(R.drawable.side_icon)
                .resize(50,50)
                .centerCrop()
                .into(holder.image_four);

        holder.bind(current, listener );
    }

    @Override
    public int getItemCount() {
        if (vehicleList != null) {
            return vehicleList.size();
        }
        return 0;
    }

    public class ViewHolderVehicle extends RecyclerView.ViewHolder {

        private TextView plate_number, vehicle_make, yom, mileage, vehicle_cc, vehicle_value_before, vehicle_value_after, date_added, vehicle_covered;
        CircleImageView image_one, image_two, image_three, image_four;
        Button edit_vehicle;

        public ViewHolderVehicle(View itemView) {
            super(itemView);
            L.m("ViewHolderVehicle");

            plate_number = (TextView) itemView.findViewById(R.id.plate_number);
            vehicle_make = (TextView) itemView.findViewById(R.id.vehicle_make);
            yom = (TextView) itemView.findViewById(R.id.yom);
            mileage = (TextView) itemView.findViewById(R.id.mileage);
            vehicle_cc = (TextView) itemView.findViewById(R.id.vehicle_cc);
            vehicle_value_before = (TextView) itemView.findViewById(R.id.vehicle_value_before);
            vehicle_value_after = (TextView) itemView.findViewById(R.id.vehicle_value_after);
            date_added = (TextView) itemView.findViewById(R.id.date_added);
            vehicle_covered = (TextView) itemView.findViewById(R.id.vehicle_covered);
            image_one = (CircleImageView) itemView.findViewById(R.id.image_one);
            image_two = (CircleImageView) itemView.findViewById(R.id.image_two);
            image_three  = (CircleImageView) itemView.findViewById(R.id.image_three);
            image_four  = (CircleImageView) itemView.findViewById(R.id.image_four);
            edit_vehicle = (Button) itemView.findViewById(R.id.edit_vehicle);

            // onClick handler for 'edit vehicle'
            if (edit_vehicle != null) {
                edit_vehicle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MyApplication.getAppContext(), EditVehicleActivity.class);

                        if (image_one.getDrawable() instanceof BitmapDrawable && image_two.getDrawable() instanceof BitmapDrawable
                                && image_three.getDrawable() instanceof BitmapDrawable && image_four.getDrawable() instanceof BitmapDrawable) {

                            L.m("Edit vehicle button clicked");

                            context.startActivity(intent);

                        } else {
                            L.t(MyApplication.getAppContext(), "Kindly wait for the images to load");
                        }
                    }
                });
            }

        }

        public void bind(final Vehicle vehicle, final OnVehicleClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onItemClick(vehicle);

                }

            });

        }

    }
}