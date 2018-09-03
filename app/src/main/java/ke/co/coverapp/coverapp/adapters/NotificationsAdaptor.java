package ke.co.coverapp.coverapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.log.L;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.*;
import ke.co.coverapp.coverapp.pojo.Notifications;

/**
 * Created by Clifford Owino on 1/6/2017.
 */

public class NotificationsAdaptor extends RecyclerView.Adapter<NotificationsAdaptor.ViewHolderNotifications>{
    private LayoutInflater layoutInflater;
    private ArrayList<Notifications> notificationsList = new ArrayList<>();

    public NotificationsAdaptor(Context context, ArrayList<Notifications> notificationsList){
        layoutInflater = LayoutInflater.from(context);
        this.notificationsList=notificationsList;
        L.m("Binding Adaptor constructor NotificationsAdaptor");


    }

    public void setNotificationsList(ArrayList<Notifications> notificationsList){
        this.notificationsList=notificationsList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderNotifications onCreateViewHolder(ViewGroup parent, int viewType) {

        View layout =  layoutInflater.inflate(R.layout.item_notification, parent, false);
        return  new ViewHolderNotifications(layout);
    }


    @Override
    public void onBindViewHolder(ViewHolderNotifications holder, int position) {

        Notifications current = notificationsList.get(position);

        String state = ((current.getNotify_flag().matches(NEWS)) ? NEWS : NOTIFICATION);

        holder.notification_status.setText(state);
        holder.notification_body.setText(current.getNotify_body());
        L.m(current.getNotify_body());

    }

    @Override
    public int getItemCount() {
        if (notificationsList != null){
            return notificationsList.size();
        }
        return 0;
    }

    static class ViewHolderNotifications extends RecyclerView.ViewHolder {


        private TextView notification_status;
        private  TextView notification_body;

        public ViewHolderNotifications(View itemView) {
            super(itemView);
            L.m("ViewHolderNotifications");

            notification_status = (TextView) itemView.findViewById(R.id.notification_status);
            notification_body = (TextView) itemView.findViewById(R.id.notification_body);

        }
    }
}
