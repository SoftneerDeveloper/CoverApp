package ke.co.coverapp.coverapp.callbacks;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.pojo.AssetTypes;

/**
 * Created by nick on 6/16/17.
 */

public interface AssetTypesLoadedListener {
    void onAssetTypesLoaded(ArrayList<AssetTypes> assetTypes);
}
