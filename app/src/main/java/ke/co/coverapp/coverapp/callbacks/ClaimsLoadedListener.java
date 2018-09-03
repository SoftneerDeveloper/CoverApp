package ke.co.coverapp.coverapp.callbacks;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.pojo.Claims;

/**
 * Created by nick on 9/19/17.
 */

public interface ClaimsLoadedListener {

    /**
     * Called when the claims have been received from the API
     * @param claims the balance in a single element arraylist
     */
    void onClaimsLoadedListener(ArrayList<Claims> claims);

}
