package ke.co.coverapp.coverapp.callbacks;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.pojo.Assets;

/**
 * Created by Clifford Owino on 3/17/2017.
 */

public interface AssetsLoadedListener {

    void onAssetsLoaded(ArrayList<Assets> listFeatures);
}
