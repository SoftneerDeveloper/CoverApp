package ke.co.coverapp.coverapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import ke.co.coverapp.coverapp.fragments.HomeCoverInfoViewFragment;

/**
 * Created by user001 on 21/02/2018.
 */

public class OpenHomeCoverInfoViewFragment3 extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            //   getSupportFragmentManager().beginTransaction()
            //       .add(android.R.id.content, new HomeCustomViewFragment()).commit();
            String message, desc = "";
            message = "Cost - KES 950 p.m. Maximum cover - KES 450,000.";
//                showDetailsDialog("Red", message, message, 900);
            desc = "Red plan with a monthly premium of KES 950";
//                buyCoverDialog("Red", message, 900, desc);
//                assetDialog(message);
            FragmentManager manager = getSupportFragmentManager();
            HomeCoverInfoViewFragment homeCoverInfoViewFragment = HomeCoverInfoViewFragment.newInstance("Red", message, 950);
            homeCoverInfoViewFragment.show(manager, "HomeCoverInfoViewFragment");

        }
    }
}
