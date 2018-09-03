package ke.co.coverapp.coverapp.tasks;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.callbacks.CoversLoadedListener;
import ke.co.coverapp.coverapp.network.VolleySingleton;
import ke.co.coverapp.coverapp.pojo.Covers;
import ke.co.coverapp.coverapp.utility.network.LoadUtil;

/**
 * Created by nick on 10/12/17.
 */

public class TaskLoadCovers extends AsyncTask<Void, Void, ArrayList<Covers>> {

    private CoversLoadedListener myComponent;
    private RequestQueue requestQueue;

    public TaskLoadCovers(CoversLoadedListener myComponent){
        this.myComponent = myComponent;
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    @Override
    protected ArrayList<Covers> doInBackground(Void... voids) {
        return LoadUtil.loadCovers(requestQueue);
    }

    @Override
    protected void onPostExecute(ArrayList<Covers> covers) {
        if (myComponent != null) {
            myComponent.onCoversLoadedListener(covers);
        }
    }
}
