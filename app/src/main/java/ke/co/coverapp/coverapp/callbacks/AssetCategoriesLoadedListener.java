package ke.co.coverapp.coverapp.callbacks;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.pojo.AssetCategories;

/**
 * Created by nick on 6/16/17.
 */

public interface AssetCategoriesLoadedListener {


    /**
     * Called when the asset categories have been received from the API
     * @param assetCategories the balance in a single element arraylist
     */
    void onAssetCategoriesLoaded(ArrayList<AssetCategories> assetCategories);
}
