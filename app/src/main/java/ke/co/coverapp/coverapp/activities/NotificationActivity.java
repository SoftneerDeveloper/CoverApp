package ke.co.coverapp.coverapp.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.adapters.NotificationsAdaptor;
import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.Notifications;

public class NotificationActivity extends AppCompatActivity {

    private ArrayList<Notifications> notificationsList = new ArrayList<>();
    private static final String STATE_NOTIFY = "state_notify";
    NotificationsAdaptor notificationsAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        RecyclerView notificationsRecyclerView = (RecyclerView) findViewById(R.id.notificationsRecyclerView);

        notificationsAdaptor = new NotificationsAdaptor(this, notificationsList);

        if (notificationsRecyclerView != null) {
            notificationsRecyclerView.setAdapter(notificationsAdaptor);
            notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        if (savedInstanceState != null) {
            //if this view starts after a rotation or configuration change, load the existing data from a parcelable
            notificationsList = savedInstanceState.getParcelableArrayList(STATE_NOTIFY);
        } else {
            //if this view starts for the first time, load the data from the database
            notificationsList = MyApplication.getWritableDatabase().readNotifications();

            if (notificationsList.isEmpty()) {
                //TODO: Notify user there are no notifications so far
                L.t(MyApplication.getAppContext(), "Nothing here for you today...");
            }
        }
        //update your Adapter to containing the retrieved activities
        notificationsAdaptor.setNotificationsList(notificationsList);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        if (fab != null) {
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
//            });
//        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save the data to a parcelable prior to rotation or configuration change
        outState.putParcelableArrayList(STATE_NOTIFY, notificationsList);

    }

}


