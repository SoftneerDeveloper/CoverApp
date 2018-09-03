package ke.co.coverapp.coverapp.callbacks;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.pojo.Covers;

/**
 * Created by nick on 10/12/17.
 */

public interface CoversLoadedListener {

    /**
     * Called when the claims have been received from the API
     * @param covers the balance in a single element arraylist
     */
    void onCoversLoadedListener(ArrayList<Covers> covers);
}
