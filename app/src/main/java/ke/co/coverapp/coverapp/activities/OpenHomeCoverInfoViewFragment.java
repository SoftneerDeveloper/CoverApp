package ke.co.coverapp.coverapp.activities;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.fragments.HomeCoverInfoViewFragment;
import ke.co.coverapp.coverapp.fragments.HomeCustomViewFragment;

/**
 * Created by user001 on 21/02/2018.
 */

public class OpenHomeCoverInfoViewFragment extends AppCompatActivity {

    Button button1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missing_profile_info);

        if (savedInstanceState == null){
         //   getSupportFragmentManager().beginTransaction()
             //       .add(android.R.id.content, new HomeCustomViewFragment()).commit();
            String message, desc = "";
            message = "Cost - KES 100 p.m. Your cover - KES 50,000.";
//                showDetailsDialog("White", message, message, 100);
            desc = "White plan with a monthly premium of KES 100";
//                buyCoverDialog("White", message, 100, desc);
//                assetDialog(message);
            FragmentManager manager = getSupportFragmentManager();
            HomeCoverInfoViewFragment homeCoverInfoViewFragment = HomeCoverInfoViewFragment.newInstance("White", message, 100);
            homeCoverInfoViewFragment.show(manager, "HomeCoverInfoViewFragment");

            button1 = (Button)findViewById(R.id.button2);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {

    }

}

