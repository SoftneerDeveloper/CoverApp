package ke.co.coverapp.coverapp.callbacks;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.pojo.Feature;

/**
 * Created by Clifford Owino on 11/16/2016.
 */

public interface FeaturesLoadedListener {

    /**
     *  Called when the features have been successfully loaded from the API
     * @param listFeatures | The features loaded from the API
     */
    public void onFeaturesLoaded(ArrayList<Feature> listFeatures);
}


