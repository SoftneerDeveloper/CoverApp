package ke.co.coverapp.coverapp.callbacks;


import java.util.ArrayList;

import ke.co.coverapp.coverapp.pojo.Prices;

/**
 * Created by Clifford Owino on 12/7/2016.
 */

public interface PricesLoadedListener {

    /**
     *  Called when the prices have been successfully loaded from the API
     */
    public void onPricesLoaded(ArrayList<Prices> listPrices);
}
