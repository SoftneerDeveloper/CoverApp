package ke.co.coverapp.coverapp.tasks;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.callbacks.ClaimsLoadedListener;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Claims;
import ke.co.coverapp.coverapp.utility.network.LoadUtil;

/**
 * Created by nick on 9/20/17.
 */

public class TaskLoadClaims extends AsyncTask<Void, Void, ArrayList<Claims>> {

    private ClaimsLoadedListener myComponent;
    private RequestQueue requestQueue;

    public TaskLoadClaims(ClaimsLoadedListener myComponent){
        this.myComponent = myComponent;
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    @Override
    protected ArrayList<Claims> doInBackground(Void... voids) {
        return LoadUtil.loadClaims(requestQueue);
    }

    @Override
    protected void onPostExecute(ArrayList<Claims> claims) {
        if (myComponent != null) {
            myComponent.onClaimsLoadedListener(claims);
        }
    }
}
