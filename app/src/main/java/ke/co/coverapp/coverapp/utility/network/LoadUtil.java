package ke.co.coverapp.coverapp.utility.network;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.AssetCategories;
import ke.co.coverapp.coverapp.pojo.AssetTypes;
import ke.co.coverapp.coverapp.pojo.Assets;
import ke.co.coverapp.coverapp.pojo.Balance;
import ke.co.coverapp.coverapp.pojo.Claims;
import ke.co.coverapp.coverapp.pojo.Covers;
import ke.co.coverapp.coverapp.pojo.Feature;
import ke.co.coverapp.coverapp.pojo.Prices;
import ke.co.coverapp.coverapp.pojo.Vehicle;
import ke.co.coverapp.coverapp.utility.ParseUtil;

/**
 * Created by Clifford Owino on 9/14/2016.
 */
public class LoadUtil {
    public static ArrayList<Feature> loadFeatures(RequestQueue requestQueue, int product) {
        JSONObject response = RequestUtil.requestFeatures(requestQueue, product);
        ArrayList<Feature> listFeatures = ParseUtil.parseFeatures(response);
        MyApplication.getWritableDatabase().insertFeatures(listFeatures, true);
        return listFeatures;
    }

    public static ArrayList<Assets> loadAssets(RequestQueue requestQueue, int last_id) {
        JSONArray response = RequestUtil.requestAssets(requestQueue, last_id);
        ArrayList<Assets> listAssets = ParseUtil.parseAssets(response);
        MyApplication.getWritableDatabase().insertAssets(listAssets, true);
        return listAssets;
    }

    public static ArrayList<Vehicle> loadVehicles(RequestQueue requestQueue) {
        JSONArray response = RequestUtil.requestVehicles(requestQueue);
        ArrayList<Vehicle> listVehicle = ParseUtil.parseVehicle(response);
        MyApplication.getWritableDatabase().insertVehicle(listVehicle, true);
        return listVehicle;
    }

    public static ArrayList<Prices> loadPrices(RequestQueue requestQueue) {
        JSONArray response = RequestUtil.requestPrices(requestQueue);
        ArrayList<Prices> listPrices = ParseUtil.parsePrices(response);
        return listPrices;
    }

    public static ArrayList<Balance> loadBalance (RequestQueue requestQueue) {
        JSONObject response = RequestUtil.requestBalance(requestQueue);
        ArrayList<Balance> balanceData = ParseUtil.parseBalance(response);
        return balanceData;
    }

    /**
     * Load asset categories & types
     *
     * Created by Nick
     */
    public static ArrayList<AssetCategories> loadAssetCategories (RequestQueue requestQueue) {
        JSONArray response = RequestUtil.requestAssetCategories(requestQueue);
        ArrayList<AssetCategories> assetCategories = ParseUtil.parseAssetCategories(response);

        // Write to database
        MyApplication.getWritableDatabase().insertAssetCategories(assetCategories, true);

        return assetCategories;
    }

    public static ArrayList<AssetTypes> loadAssetTypes (RequestQueue requestQueue) {
        JSONArray response = RequestUtil.requestAssetTypes(requestQueue);
        ArrayList<AssetTypes> assetTypes = ParseUtil.parseAssetTypes(response);

        // Write asset types to database
        MyApplication.getWritableDatabase().insertAssetTypes(assetTypes, true);

        return assetTypes;
    }

    public static ArrayList<Claims> loadClaims (RequestQueue requestQueue) {
        JSONObject response = RequestUtil.requestClaims(requestQueue);
        ArrayList<Claims> claims = ParseUtil.parseClaims(response);
        MyApplication.getWritableDatabase().insertClaims(claims, true);

        return claims;
    }

    public static ArrayList<Covers> loadCovers (RequestQueue requestQueue) {
        JSONObject response = RequestUtil.requestCovers(requestQueue);
        ArrayList<Covers> covers = ParseUtil.parseCovers(response);
        MyApplication.getWritableDatabase().insertCovers(covers, true);

        return covers;
    }

}
