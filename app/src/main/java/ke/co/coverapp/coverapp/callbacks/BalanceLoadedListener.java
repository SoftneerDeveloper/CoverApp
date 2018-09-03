package ke.co.coverapp.coverapp.callbacks;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.pojo.Balance;

/**
 * Created by Clifford Owino on 2/9/2017.
 */

public interface BalanceLoadedListener {

    /**
     * Called when the account balance has been loaded
     * @param balanceData the balance in a single element arraylist
     */
    void onBalanceLoaded(ArrayList<Balance> balanceData);
}
