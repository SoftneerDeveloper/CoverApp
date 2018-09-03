package ke.co.coverapp.coverapp.callbacks;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.pojo.Vehicle;

/**
 * Created by Clifford Owino on 3/24/2017.
 */

public interface VehiclesLoadedListener {

    /**
     *  Called when the vehicles have been successfully loaded from the API
     * @param listVehicles | The vehicles loaded from the API
     */
    public void onVehiclesLoaded(ArrayList<Vehicle> listVehicles);
}
