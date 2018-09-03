package ke.co.coverapp.coverapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import ke.co.coverapp.coverapp.fragments.HomeCoverInfoViewFragment;

/**
 * Created by user001 on 21/02/2018.
 */

public class OpenHomeCoverInfoViewFragment2 extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            //   getSupportFragmentManager().beginTransaction()
            //       .add(android.R.id.content, new HomeCustomViewFragment()).commit();
            String message, desc = "";
            message = "Cost - KES 350 p.m. Maximum cover - KES 150,000.";
//                showDetailsDialog("Yellow", message, message, 300);
            desc = "Yellow plan with a monthly premium of KES 350";
//                buyCoverDialog("Yellow", message, 300, desc);
//                assetDialog(message);

            FragmentManager manager = getSupportFragmentManager();
            HomeCoverInfoViewFragment homeCoverInfoViewFragment = HomeCoverInfoViewFragment.newInstance("Yellow", message, 350);
            homeCoverInfoViewFragment.show(manager, "HomeCoverInfoViewFragment");

        }
    }
}
