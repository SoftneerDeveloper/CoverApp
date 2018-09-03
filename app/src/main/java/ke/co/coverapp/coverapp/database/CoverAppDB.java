package ke.co.coverapp.coverapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import ke.co.coverapp.coverapp.application.MyApplication;
import ke.co.coverapp.coverapp.log.L;
import ke.co.coverapp.coverapp.pojo.AssetCategories;
import ke.co.coverapp.coverapp.pojo.AssetTypes;
import ke.co.coverapp.coverapp.pojo.Assets;
import ke.co.coverapp.coverapp.pojo.Claims;
import ke.co.coverapp.coverapp.pojo.Covers;
import ke.co.coverapp.coverapp.pojo.Feature;
import static ke.co.coverapp.coverapp.pojo.Keys.keys;

import ke.co.coverapp.coverapp.pojo.Notifications;
import ke.co.coverapp.coverapp.pojo.Vehicle;
import ke.co.coverapp.coverapp.utility.ValidationUtil;

/**
 * Created by Clifford Owino on 9/14/2016.
 *
 * Edited by Nicholas Kimuli
 */
public class CoverAppDB {

    private SQLiteDatabase mDatabase;

    private static int FEATURE_TABLE = 1;
    public static int NOTIFY_TABLE = 2;
    public static int ASSETS_TABLE = 3;
    public static int VEHICLE_TABLE = 4;
    public static int ASSET_CATEGORIES_TABLE = 5;
    public static int ASSET_TYPES_TABLE = 6;
    public static int CLAIMS_TABLE = 7;
    public static int COVERS_TABLE = 8;

    private String cover_id = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.COVER_ID, ValidationUtil.getDefault());

    public CoverAppDB(Context context){
        mDatabase = new CoverAppDBHelper(context).getWritableDatabase();
    }

    private void truncateTable(int table){
        if (table == 1){
            mDatabase.delete(CoverAppDBHelper.FEATURES_TABLE_NAME , null, null);
        }else if (table == 2){
            mDatabase.delete(CoverAppDBHelper.NOTIFICATION_TABLE_NAME , null, null);
        }else if (table == 3){
            mDatabase.delete(CoverAppDBHelper.ASSETS_TABLE_NAME , null, null);
        }else if (table == 4){
            mDatabase.delete(CoverAppDBHelper.VEHICLE_TABLE_NAME, null, null);
        }else if (table == 5){
            mDatabase.delete(CoverAppDBHelper.ASSET_CATEGORIES_TABLE_NAME, null, null);
        } else if (table == 6){
            mDatabase.delete(CoverAppDBHelper.ASSET_TYPES_TABLE_NAME, null, null);
        } else if (table == 7) {
            mDatabase.delete(CoverAppDBHelper.CLAIMS_TABLE_NAME, null, null);
        } else {
            mDatabase.delete(CoverAppDBHelper.COVERS_TABLE_NAME, null, null);
        }
    }

    public int countTable(int table){

        String tableName ="";
        switch(table){

            case 1:
                tableName = CoverAppDBHelper.FEATURES_TABLE_NAME;
                break;
            case 2:
                tableName = CoverAppDBHelper.NOTIFICATION_TABLE_NAME;
                break;
            case 3:
                tableName = CoverAppDBHelper.ASSETS_TABLE_NAME;
                break;

            case 4:
                tableName = CoverAppDBHelper.VEHICLE_TABLE_NAME;
                break;

            case 5:
                tableName = CoverAppDBHelper.ASSET_CATEGORIES_TABLE_NAME;
                break;

            case 6:
                tableName = CoverAppDBHelper.ASSET_TYPES_TABLE_NAME;
                break;

            case 7:
                tableName = CoverAppDBHelper.CLAIMS_TABLE_NAME;
                break;

            case 8:
                tableName = CoverAppDBHelper.COVERS_TABLE_NAME;
                break;

        }

        Cursor cursor = mDatabase.rawQuery(CoverAppDBHelper.COUNT_TABLE_ROWS+" "+tableName,null);
        cursor.moveToFirst();
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public void insertFeatures(ArrayList<Feature> listFeatures, boolean clearPrevious) {
        if (clearPrevious) {
            truncateTable(FEATURE_TABLE);
        }

        //creating an sql prepared statement
        String sql = "INSERT INTO " + CoverAppDBHelper.FEATURES_TABLE_NAME + " VALUES (?,?,?,?,?,?);";
        //compiling the statement and starting a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listFeatures.size(); i++) {
            Feature currentFeature = listFeatures.get(i);
            statement.clearBindings();
            //for a given column index, bind the data to be put in that index
            statement.bindString(2, currentFeature.getName());
            statement.bindString(3, currentFeature.getDescription());
            statement.bindString(4, currentFeature.getPrice());
            statement.bindString(5, currentFeature.getParent());
            statement.bindString(6, cover_id);
            statement.execute();
        }
        //set the transaction as successful and end the transaction
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public void insertAssetTypes(ArrayList<AssetTypes> assetTypes, boolean clearPrevious) {
        if (clearPrevious) {
            truncateTable(ASSET_TYPES_TABLE);
        }

        String sql = "INSERT INTO " + CoverAppDBHelper.ASSET_TYPES_TABLE_NAME + " VALUES (?,?,?,?);";
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();

        for (int i = 0; i < assetTypes.size(); i++) {
            AssetTypes currentType = assetTypes.get(i);
            statement.clearBindings();
            statement.bindString(1, currentType.getId());
            statement.bindString(2, currentType.getCategoryId());
            statement.bindString(3, currentType.getName());
            statement.bindString(4, currentType.getDescription());
            statement.execute();
        }

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public void insertAssetCategories(ArrayList<AssetCategories> assetCategories, boolean clearPrevious) {
        if (clearPrevious) {
            truncateTable(ASSET_CATEGORIES_TABLE);
        }

        // Creating an sql prepared statement
        String sql = "INSERT INTO " + CoverAppDBHelper.ASSET_CATEGORIES_TABLE_NAME + " VALUES (?,?,?,?);";
        // Compiling the statement and starting a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();

        for (int i = 0; i <assetCategories.size(); i++) {
            AssetCategories currentCategory = assetCategories.get(i);
            statement.clearBindings();
            // For a given column index, bind the data to be put in that index
            statement.bindString(2, currentCategory.getId());
            statement.bindString(3, currentCategory.getDescription());
            statement.bindString(4, currentCategory.getName());
            statement.execute();
        }

        // Set the transaction as successful and end the transaction
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    /**
     * Get types belonging to a particular category
     *
     * @param category_id
     * @return
     */
    public ArrayList<AssetTypes> readParticularAssetTypes(String category_id) {
        ArrayList<AssetTypes> assetTypes = new ArrayList<>();

        String[] columns = {
                CoverAppDBHelper.ASSET_TYPES_ID,
                CoverAppDBHelper.ASSET_TYPES_CATEGORY_ID,
                CoverAppDBHelper.ASSET_TYPES_NAME,
                CoverAppDBHelper.ASSET_TYPES_DESCRIPTION
        };

        Cursor cursor = mDatabase.query(CoverAppDBHelper.ASSET_TYPES_TABLE_NAME, columns,
                CoverAppDBHelper.ASSET_TYPES_CATEGORY_ID + " = ? ",
                new String[]{category_id}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                AssetTypes assetType = new AssetTypes();
                assetType.setId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSET_TYPES_ID)));
                assetType.setCategoryId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSET_TYPES_CATEGORY_ID)));
                assetType.setName(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSET_TYPES_NAME)));
                assetType.setDescription(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSET_TYPES_DESCRIPTION)));

                assetTypes.add(assetType);
            }
            while (cursor.moveToNext());
            cursor.close();
        }

        L.m("DB method - get particular asset types: " + assetTypes.toString());

        return assetTypes;
    }

    public ArrayList<AssetTypes> readAssetTypes() {
        ArrayList<AssetTypes> assetTypes = new ArrayList<>();

        String[] columns = {
                CoverAppDBHelper.ASSET_TYPES_ID,
                CoverAppDBHelper.ASSET_TYPES_CATEGORY_ID,
                CoverAppDBHelper.ASSET_TYPES_NAME,
                CoverAppDBHelper.ASSET_TYPES_DESCRIPTION
        };

        Cursor cursor = mDatabase.query(CoverAppDBHelper.ASSET_TYPES_TABLE_NAME, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                AssetTypes assetType = new AssetTypes();
                assetType.setId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSET_TYPES_ID)));
                assetType.setCategoryId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSET_TYPES_CATEGORY_ID)));
                assetType.setName(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSET_TYPES_NAME)));
                assetType.setDescription(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSET_TYPES_DESCRIPTION)));

                assetTypes.add(assetType);
            }
            while (cursor.moveToNext());
            cursor.close();
        }

        return assetTypes;
    }

    public ArrayList<AssetCategories> readAssetCategories() {
        ArrayList<AssetCategories> assetCategories = new ArrayList<>();

        // Get a list of all columns
        String[] columns = {
                CoverAppDBHelper.ASSET_CATEGORY_ID,
                CoverAppDBHelper.ASSET_CATEGORY_CID,
                CoverAppDBHelper.ASSET_CATEGORY_NAME,
                CoverAppDBHelper.ASSET_CATEGORY_DESCRIPTION
        };

        Cursor cursor = mDatabase.query(CoverAppDBHelper.ASSET_CATEGORIES_TABLE_NAME, columns, null, null , null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Create a new assetCategory object and retrieve the data from the cursor to be stored in this object
                AssetCategories assetCategory = new AssetCategories();
                // Each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank assetCategory object to contain our data
                assetCategory.setId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSET_CATEGORY_CID)));
                assetCategory.setName(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSET_CATEGORY_NAME)));
                assetCategory.setDescription(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSET_CATEGORY_DESCRIPTION)));

                // Add category to the list we plan to return
                assetCategories.add(assetCategory);
            }
            while (cursor.moveToNext());
            cursor.close();
        }

        return assetCategories;
    }

    public ArrayList<Feature> readFeatures(String parent) {

        ArrayList<Feature> listFeatures = new ArrayList<>();
        //get a list of columns to be retrieved, we need all of them
        String[] columns = {
                CoverAppDBHelper.FEATURE_UID,
                CoverAppDBHelper.FEATURE_NAME,
                CoverAppDBHelper.FEATURE_DESCRIPTION,
                CoverAppDBHelper.FEATURE_PRICE,
                CoverAppDBHelper.FEATURE_PARENT//,
              //  CoverAppDBHelper.USER_ID_COLUMN//CoverAppDBHelper.USER_ID_COLUMN+"=? "+" AND "+
        };
        Cursor cursor = mDatabase.query(CoverAppDBHelper.FEATURES_TABLE_NAME, columns, CoverAppDBHelper.FEATURE_PARENT+" =? ",
                new String[]{ parent}, null, null, null);


        if (cursor != null && cursor.moveToFirst()) {
            do {
                //create a new feature object and retrieve the data from the cursor to be stored in this feature object
                Feature feature = new Feature();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank feature object to contain our data
                feature.setUid(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.FEATURE_UID)));
                feature.setName(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.FEATURE_NAME)));
                feature.setDescription(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.FEATURE_DESCRIPTION)));
                feature.setPrice(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.FEATURE_PRICE)));
                //add the biller to the list of feature objects which we plan to return
                listFeatures.add(feature);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return listFeatures;
    }

    public void insertNotification(ArrayList<Notifications> listNotifications, boolean clearPrevious) {
        if (clearPrevious) {
            truncateTable(NOTIFY_TABLE);
        }

        //creating an sql prepared statement
        String sql = "INSERT INTO " + CoverAppDBHelper.NOTIFICATION_TABLE_NAME + " VALUES (?,?,?,?);";
        //compiling the statement and starting a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listNotifications.size(); i++) {
            Notifications currentNotifications = listNotifications.get(i);
            statement.clearBindings();
            //for a given column index, bind the data to be put in that index
            statement.bindString(2, currentNotifications.getNotify_body());
            statement.bindString(3, currentNotifications.getNotify_flag());
            statement.bindString(4, cover_id);
            statement.execute();
        }
        //set the transaction as successful and end the transaction
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<Notifications> readNotifications() {
        ArrayList<Notifications> listNotifications = new ArrayList<>();
        //get a list of columns to be retrieved, we need all of them
        String[] columns = {
                CoverAppDBHelper.NOTIFICATION_UID,
                CoverAppDBHelper.NOTIFICATION_BODY,
                CoverAppDBHelper.NOTIFICATION_FLAG,
                CoverAppDBHelper.USER_ID_COLUMN
        };
        Cursor cursor = mDatabase.query(CoverAppDBHelper.NOTIFICATION_TABLE_NAME, columns, CoverAppDBHelper.USER_ID_COLUMN+"=?",
                new String[]{cover_id}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Notifications notifications = new Notifications();

                notifications.setNotify_body(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.NOTIFICATION_BODY)));
                notifications.setNotify_flag(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.NOTIFICATION_FLAG)));
                //add the notifications to the list of notifications objects which we plan to return
                listNotifications.add(notifications);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return listNotifications;
    }

    public void insertVehicle(ArrayList<Vehicle> listVehicles, boolean clearPrevious) {
        if (clearPrevious && listVehicles !=null) {
            truncateTable(VEHICLE_TABLE);
        }

        //creating an sql prepared statement
        String sql = "INSERT INTO " + CoverAppDBHelper.VEHICLE_TABLE_NAME + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        //compiling the statement and starting a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listVehicles.size(); i++) {
            Vehicle currentVehicle = listVehicles.get(i);
            statement.clearBindings();

            //for a given column index, bind the data to be put in that index
            statement.bindString(2, currentVehicle.getId());
            statement.bindString(3, currentVehicle.getVehicle_make());
            statement.bindString(4, currentVehicle.getPlate_first());
            statement.bindString(5, currentVehicle.getPlate_last());
            statement.bindString(6, currentVehicle.getYom());
            statement.bindString(7, currentVehicle.getOdometer());
            statement.bindString(8, currentVehicle.getEngine_capacity());
            statement.bindString(9, currentVehicle.getCar_value_before());
            statement.bindString(10, currentVehicle.getCar_value_after());
            statement.bindString(11, currentVehicle.getDate_added());
            statement.bindString(12, currentVehicle.getIs_covered());
            statement.bindString(13, currentVehicle.getImage_one());
            statement.bindString(14, currentVehicle.getImage_two());
            statement.bindString(15, currentVehicle.getImage_three());
            statement.bindString(16, currentVehicle.getImage_four());
            statement.execute();
        }
        //set the transaction as successful and end the transaction
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }


    public ArrayList<Vehicle> readVehicle() {

        ArrayList<Vehicle> listVehicles = new ArrayList<>();
        //get a list of columns to be retrieved, we need all of them
        String[] columns = {
                CoverAppDBHelper.VEHICLE_SERVER_ID,
                CoverAppDBHelper.VEHICLE_MAKE,
                CoverAppDBHelper.VEHICLE_PLATE_FIRST,
                CoverAppDBHelper.VEHICLE_PLATE_LAST,
                CoverAppDBHelper.VEHICLE_YOM,
                CoverAppDBHelper.VEHICLE_MILEAGE,
                CoverAppDBHelper.VEHICLE_CC,
                CoverAppDBHelper.VEHICLE_VALUE_BEFORE,
                CoverAppDBHelper.VEHICLE_VALUE_AFTER,
                CoverAppDBHelper.VEHICLE_DATE_ADDED,
                CoverAppDBHelper.VEHICLE_COVERED,
                CoverAppDBHelper.VEHICLE_IMAGE_ONE,
                CoverAppDBHelper.VEHICLE_IMAGE_TWO,
                CoverAppDBHelper.VEHICLE_IMAGE_THREE,
                CoverAppDBHelper.VEHICLE_IMAGE_FOUR

        };
        Cursor cursor = mDatabase.query(CoverAppDBHelper.VEHICLE_TABLE_NAME, columns, null, null, null, null, null);


        if (cursor != null && cursor.moveToFirst()) {
            do {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_SERVER_ID)));
                vehicle.setVehicle_make(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_MAKE)));
                vehicle.setPlate_first(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_PLATE_FIRST)));
                vehicle.setPlate_last(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_PLATE_LAST)));
                vehicle.setYom(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_YOM)));
                vehicle.setOdometer(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_MILEAGE)));
                vehicle.setEngine_capacity(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_CC)));
                vehicle.setCar_value_before(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_VALUE_BEFORE)));
                vehicle.setCar_value_after(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_VALUE_AFTER)));
                vehicle.setDate_added(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_DATE_ADDED)));
                vehicle.setIs_covered(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_COVERED)));
                vehicle.setImage_one(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_IMAGE_ONE)));
                vehicle.setImage_two(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_IMAGE_TWO)));
                vehicle.setImage_three(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_IMAGE_THREE)));
                vehicle.setImage_four(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_IMAGE_FOUR)));
                listVehicles.add(vehicle);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return listVehicles;
    }

    public ArrayList<Vehicle> readNonCoveredVehicle() {

        ArrayList<Vehicle> listVehicles = new ArrayList<>();
        //get a list of columns to be retrieved, we need all of them
        String[] columns = {
                CoverAppDBHelper.VEHICLE_SERVER_ID,
                CoverAppDBHelper.VEHICLE_MAKE,
                CoverAppDBHelper.VEHICLE_PLATE_FIRST,
                CoverAppDBHelper.VEHICLE_PLATE_LAST,
                CoverAppDBHelper.VEHICLE_YOM,
                CoverAppDBHelper.VEHICLE_MILEAGE,
                CoverAppDBHelper.VEHICLE_CC,
                CoverAppDBHelper.VEHICLE_VALUE_BEFORE,
                CoverAppDBHelper.VEHICLE_VALUE_AFTER,
                CoverAppDBHelper.VEHICLE_DATE_ADDED,
                CoverAppDBHelper.VEHICLE_COVERED,
                CoverAppDBHelper.VEHICLE_IMAGE_ONE,
                CoverAppDBHelper.VEHICLE_IMAGE_TWO,
                CoverAppDBHelper.VEHICLE_IMAGE_THREE,
                CoverAppDBHelper.VEHICLE_IMAGE_FOUR

        };
        Cursor cursor = mDatabase.query(CoverAppDBHelper.VEHICLE_TABLE_NAME, columns,
                CoverAppDBHelper.VEHICLE_COVERED+" = ? ",
                new String[]{"N"},
                null, null, null);


        if (cursor != null && cursor.moveToFirst()) {
            do {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_SERVER_ID)));
                vehicle.setVehicle_make(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_MAKE)));
                vehicle.setPlate_first(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_PLATE_FIRST)));
                vehicle.setPlate_last(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_PLATE_LAST)));
                vehicle.setYom(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_YOM)));
                vehicle.setOdometer(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_MILEAGE)));
                vehicle.setEngine_capacity(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_CC)));
                vehicle.setCar_value_before(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_VALUE_BEFORE)));
                vehicle.setCar_value_after(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_VALUE_AFTER)));
                vehicle.setDate_added(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_DATE_ADDED)));
                vehicle.setIs_covered(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_COVERED)));
                vehicle.setImage_one(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_IMAGE_ONE)));
                vehicle.setImage_two(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_IMAGE_TWO)));
                vehicle.setImage_three(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_IMAGE_THREE)));
                vehicle.setImage_four(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.VEHICLE_IMAGE_FOUR)));
                listVehicles.add(vehicle);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return listVehicles;
    }

    public void insertClaims(ArrayList<Claims> listClaims, boolean clearPrevious) {
        if (clearPrevious) {
            truncateTable(CLAIMS_TABLE);
        }

        L.m("Insert claims called. ListClaims size - " + listClaims.size());

        //creating an sql prepared statement
        String sql = "INSERT INTO " + CoverAppDBHelper.CLAIMS_TABLE_NAME + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";

        //compiling the statement and starting a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listClaims.size(); i++) {
            Claims currentClaim = listClaims.get(i);
            statement.clearBindings();
            //for a given column index, bind the data to be put in that index
            statement.bindString(2, currentClaim.getClaimId());
            statement.bindString(3, currentClaim.getUserId());
            statement.bindString(4, currentClaim.getAssetsClaimed());
            statement.bindString(5, currentClaim.getAccidentType());
            statement.bindString(6, currentClaim.getAccidentDate());
            statement.bindString(7, currentClaim.getAccidentLocation());
            statement.bindString(8, currentClaim.getPoliceAbstract());
            statement.bindString(9, currentClaim.getCurrentImage());
            statement.bindString(10, currentClaim.getState());
            statement.bindString(11, currentClaim.getStatus());
            statement.bindString(12, currentClaim.getCreatedAt());
            statement.bindString(13, currentClaim.getUpdatedAt());

            statement.execute();
        }

        //set the transaction as successful and end the transaction
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<Claims> readClaims() {

        ArrayList<Claims> listClaims = new ArrayList<>();
        //get a list of columns to be retrieved, we need all of them
        String[] columns = {
                CoverAppDBHelper.CLAIM_CLAIM_ID,
                CoverAppDBHelper.CLAIM_USER_ID,
                CoverAppDBHelper.CLAIM_ACCIDENT_TYPE,
                CoverAppDBHelper.CLAIM_ACCIDENT_DATE,
                CoverAppDBHelper.CLAIM_ACCIDENT_LOCATION,
                CoverAppDBHelper.CLAIM_ASSETS_CLAIMED,
                CoverAppDBHelper.CLAIM_POLICE_ABSTRACT,
                CoverAppDBHelper.CLAIM_CURRENT_IMAGE,
                CoverAppDBHelper.CLAIM_STATUS,
                CoverAppDBHelper.CLAIM_STATE,
                CoverAppDBHelper.CLAIM_CREATED_AT,
                CoverAppDBHelper.CLAIM_UPDATED_AT
        };

        Cursor cursor = mDatabase.query(CoverAppDBHelper.CLAIMS_TABLE_NAME, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            L.m("Cursor not null");
            do {
                Claims claims = new Claims();
                claims.setClaimId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_CLAIM_ID)));
                claims.setUserId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_USER_ID)));
                claims.setAccidentType(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_ACCIDENT_TYPE)));
                claims.setAccidentDate(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_ACCIDENT_DATE)));
                claims.setAccidentLocation(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_ACCIDENT_LOCATION)));
                claims.setAssetsClaimed(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_ASSETS_CLAIMED)));
                claims.setPoliceAbstract(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_POLICE_ABSTRACT)));
                claims.setCurrentImage(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_CURRENT_IMAGE)));
                claims.setStatus(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_STATUS)));
                claims.setState(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_STATE)));
                claims.setCreatedAt(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_CREATED_AT)));
                claims.setUpdatedAt(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_UPDATED_AT)));

                listClaims.add(claims);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return listClaims;
    }

    public ArrayList<Claims> readClaim(String claimId) {

        ArrayList<Claims> listClaims = new ArrayList<>();
        //get a list of columns to be retrieved, we need all of them
        String[] columns = {
                CoverAppDBHelper.CLAIM_CLAIM_ID,
                CoverAppDBHelper.CLAIM_USER_ID,
                CoverAppDBHelper.CLAIM_ACCIDENT_TYPE,
                CoverAppDBHelper.CLAIM_ACCIDENT_DATE,
                CoverAppDBHelper.CLAIM_ACCIDENT_LOCATION,
                CoverAppDBHelper.CLAIM_ASSETS_CLAIMED,
                CoverAppDBHelper.CLAIM_POLICE_ABSTRACT,
                CoverAppDBHelper.CLAIM_CURRENT_IMAGE,
                CoverAppDBHelper.CLAIM_STATUS,
                CoverAppDBHelper.CLAIM_STATE,
                CoverAppDBHelper.CLAIM_CREATED_AT,
                CoverAppDBHelper.CLAIM_UPDATED_AT
        };


        Cursor cursor = mDatabase.query(CoverAppDBHelper.CLAIMS_TABLE_NAME, columns,
                CoverAppDBHelper.CLAIM_CLAIM_ID+" = ? ",
                new String[]{claimId},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Claims claims = new Claims();
                claims.setClaimId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_CLAIM_ID)));
                claims.setUserId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_USER_ID)));
                claims.setAccidentType(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_ACCIDENT_TYPE)));
                claims.setAccidentDate(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_ACCIDENT_DATE)));
                claims.setAccidentLocation(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_ACCIDENT_LOCATION)));
                claims.setAssetsClaimed(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_ASSETS_CLAIMED)));
                claims.setPoliceAbstract(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_POLICE_ABSTRACT)));
                claims.setCurrentImage(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_CURRENT_IMAGE)));
                claims.setStatus(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_STATUS)));
                claims.setState(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_STATE)));
                claims.setCreatedAt(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_CREATED_AT)));
                claims.setUpdatedAt(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CLAIM_UPDATED_AT)));

                listClaims.add(claims);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return listClaims;
    }

    public void insertCovers(ArrayList<Covers> listCovers, boolean clearPrevious) {
        if (clearPrevious && listCovers != null) {
            truncateTable(COVERS_TABLE);
        }

        //creating an sql prepared statement
        String sql = "INSERT INTO " + CoverAppDBHelper.COVERS_TABLE_NAME + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        //compiling the statement and starting a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listCovers.size(); i++) {
            Covers currentCover = listCovers.get(i);
            statement.clearBindings();
            //for a given column index, bind the data to be put in that index
            statement.bindString(2, currentCover.getCoverId());
            statement.bindString(3, currentCover.getUserId());
            statement.bindString(4, currentCover.getCoverPackage());
            statement.bindString(5, currentCover.getUnitCode());
            statement.bindString(6, currentCover.getCoverProduct());
            statement.bindString(7, currentCover.getNumPayments());
            statement.bindString(8, currentCover.getSignUpCost());
            statement.bindString(9, currentCover.getCreatedAt());
            statement.bindString(10, currentCover.getUpdatedAt());
            statement.bindString(11, currentCover.getPolicyNumber());
            statement.bindString(12, currentCover.getReceiptNumber());
            statement.bindString(13, currentCover.getDrCrNumber());
            statement.bindString(14, currentCover.getTnxCode());
            statement.bindString(15, currentCover.getExpiryDate());

            statement.execute();
        }

        //set the transaction as successful and end the transaction
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<Covers> readCovers() {

        ArrayList<Covers> listCovers = new ArrayList<>();
        //get a list of columns to be retrieved, we need all of them
        String[] columns = {
                CoverAppDBHelper.COVER_COVER_ID,
                CoverAppDBHelper.COVER_USER_ID,
                CoverAppDBHelper.COVER_PACKAGE,
                CoverAppDBHelper.COVER_UNIT_CODE,
                CoverAppDBHelper.COVER_PRODUCT,
                CoverAppDBHelper.COVER_NUM_PAYMENTS,
                CoverAppDBHelper.COVER_SIGNUP_COST,
                CoverAppDBHelper.COVER_CREATED,
                CoverAppDBHelper.COVER_UPDATED_AT,
                CoverAppDBHelper.COVER_POLICY_NUMBER,
                CoverAppDBHelper.COVER_RECEIPT_NUMBER,
                CoverAppDBHelper.COVER_DRCR_NUMBER,
                CoverAppDBHelper.COVER_TNX_CODE,
                CoverAppDBHelper.COVER_EXPIRY_DATE
        };

        Cursor cursor = mDatabase.query(CoverAppDBHelper.COVERS_TABLE_NAME, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            L.m("Cursor not null");
            do {
                Covers covers = new Covers();
                covers.setCoverId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_COVER_ID)));
                covers.setUserId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_USER_ID)));
                covers.setUnitCode(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_UNIT_CODE)));
                covers.setSignUpCost(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_SIGNUP_COST)));
                covers.setReceiptNumber(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_RECEIPT_NUMBER)));
                covers.setPolicyNumber(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_POLICY_NUMBER)));
                covers.setCoverPackage(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_PACKAGE)));
                covers.setCoverProduct(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_PRODUCT)));
                covers.setCreatedAt(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_CREATED)));
                covers.setUpdatedAt(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_UPDATED_AT)));
                covers.setNumPayments(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_NUM_PAYMENTS)));
                covers.setDrCrNumber(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_DRCR_NUMBER)));
                covers.setTnxCode(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_TNX_CODE)));
                covers.setExpiryDate(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_EXPIRY_DATE)));

                listCovers.add(covers);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return listCovers;
    }

    public ArrayList<Covers> readCover(String coverId) {

        ArrayList<Covers> listCovers = new ArrayList<>();
        //get a list of columns to be retrieved, we need all of them
        String[] columns = {
                CoverAppDBHelper.COVER_COVER_ID,
                CoverAppDBHelper.COVER_USER_ID,
                CoverAppDBHelper.COVER_PACKAGE,
                CoverAppDBHelper.COVER_UNIT_CODE,
                CoverAppDBHelper.COVER_PRODUCT,
                CoverAppDBHelper.COVER_NUM_PAYMENTS,
                CoverAppDBHelper.COVER_SIGNUP_COST,
                CoverAppDBHelper.COVER_CREATED,
                CoverAppDBHelper.COVER_UPDATED_AT,
                CoverAppDBHelper.COVER_POLICY_NUMBER,
                CoverAppDBHelper.COVER_RECEIPT_NUMBER,
                CoverAppDBHelper.COVER_DRCR_NUMBER,
                CoverAppDBHelper.COVER_TNX_CODE,
                CoverAppDBHelper.COVER_EXPIRY_DATE
        };

        Cursor cursor = mDatabase.query(CoverAppDBHelper.COVERS_TABLE_NAME, columns,
                CoverAppDBHelper.COVER_COVER_ID+" = ? ",
                new String[]{coverId},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            L.m("Cursor not null");
            do {
                Covers covers = new Covers();
                covers.setCoverId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_COVER_ID)));
                covers.setUserId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_USER_ID)));
                covers.setUnitCode(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_UNIT_CODE)));
                covers.setSignUpCost(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_SIGNUP_COST)));
                covers.setReceiptNumber(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_RECEIPT_NUMBER)));
                covers.setPolicyNumber(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_POLICY_NUMBER)));
                covers.setCoverPackage(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_PACKAGE)));
                covers.setCoverProduct(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_PRODUCT)));
                covers.setCreatedAt(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_CREATED)));
                covers.setUpdatedAt(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_UPDATED_AT)));
                covers.setNumPayments(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_NUM_PAYMENTS)));
                covers.setDrCrNumber(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_DRCR_NUMBER)));
                covers.setTnxCode(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_TNX_CODE)));
                covers.setExpiryDate(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.COVER_EXPIRY_DATE)));

                listCovers.add(covers);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return listCovers;
    }

    public void insertAssets(ArrayList<Assets> listAssets, boolean clearPrevious) {
        if (clearPrevious) {
            truncateTable(ASSETS_TABLE);
        }

        //creating an sql prepared statement
        String sql = "INSERT INTO " + CoverAppDBHelper.ASSETS_TABLE_NAME + " VALUES (?,?,?,?,?,?,?,?);";
        //compiling the statement and starting a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listAssets.size(); i++) {
            Assets currentAsset = listAssets.get(i);
            statement.clearBindings();
            //for a given column index, bind the data to be put in that index
            statement.bindString(2, currentAsset.getId());
            statement.bindString(3, currentAsset.getName());
            statement.bindString(4, currentAsset.getDescription());
            statement.bindString(5, currentAsset.getCovered());
            statement.bindString(6, currentAsset.getImage_one());
            statement.bindString(7, currentAsset.getCategoryId());
            statement.bindString(8, currentAsset.getTypeId());

            // Uncomment lines below when you figure out how to allow users to input more than one image for each asset
            // Also uncomment lines at ParseAssets method in ParseUtil.java

//            statement.bindString(6, currentAsset.getImage_two());
//            statement.bindString(7, currentAsset.getImage_three());

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<Assets> readAssets() {

        ArrayList<Assets> listAssets = new ArrayList<>();
        //get a list of columns to be retrieved, we need all of them
        String[] columns = {
                CoverAppDBHelper.ASSET_ID,
                CoverAppDBHelper.ASSETS_UID,
                CoverAppDBHelper.ASSETS_NAME,
                CoverAppDBHelper.ASSETS_DESCRIPTION,
                CoverAppDBHelper.ASSETS_COVERED,
                CoverAppDBHelper.ASSETS_IMAGE_ONE,
                CoverAppDBHelper.CATEGORY_ID,
                CoverAppDBHelper.TYPE_ID
//                CoverAppDBHelper.ASSETS_IMAGE_TWO,
//                CoverAppDBHelper.ASSETS_IMAGE_THREE
        };

        // TODO: Get only uncovered assets
        Cursor cursor = mDatabase.query(CoverAppDBHelper.ASSETS_TABLE_NAME, columns, null, null, null, null, null);


        if (cursor != null && cursor.moveToFirst()) {
            do {
                Assets assets = new Assets();
                assets.setId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSET_ID)));
                assets.setUid(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_UID)));
                assets.setName(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_NAME)));
                assets.setDescription(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_DESCRIPTION)));
                assets.setCovered(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_COVERED)));
                assets.setImage_one(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_IMAGE_ONE)));
                assets.setCategoryId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CATEGORY_ID)));
                assets.setTypeId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.TYPE_ID)));

                // Uncomment when a user can add more than one image to each asset


//                assets.setImage_two(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_IMAGE_TWO)));
//                assets.setImage_three(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_IMAGE_THREE)));

                listAssets.add(assets);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return listAssets;
    }
//    public List<Assets> getAllAssets() {
//        List<Assets> listassets = new ArrayList<>();
//
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + CoverAppDBHelper.ASSETS_TABLE_NAME + " ORDER BY " +
//                CoverAppDBHelper.ASSET_ID + " DESC";
//
////        SQLiteDatabase db = this.;
//        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Assets assets = new Assets();
//                assets.setId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSET_ID)));
//                assets.setUid(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_UID)));
//                assets.setName(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_NAME)));
//                assets.setDescription(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_DESCRIPTION)));
//                assets.setCovered(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_COVERED)));
//                assets.setImage_one(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_IMAGE_ONE)));
//                assets.setCategoryId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CATEGORY_ID)));
//                assets.setTypeId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.TYPE_ID)));
//
//                listassets.add(assets);
//            } while (cursor.moveToNext());
//        }
//
//        // close db connection
//        mDatabase.close();
//
//        // return assets list
//        return listassets;
//    }

    public ArrayList<Assets> readNonCoveredAssets() {

        ArrayList<Assets> listAssets = new ArrayList<>();

        // Get a list of columns to be retrieved
        String[] columns = {
                CoverAppDBHelper.ASSET_ID,
                CoverAppDBHelper.ASSETS_UID,
                CoverAppDBHelper.ASSETS_NAME,
                CoverAppDBHelper.ASSETS_DESCRIPTION,
                CoverAppDBHelper.ASSETS_COVERED,
                CoverAppDBHelper.ASSETS_IMAGE_ONE,
                CoverAppDBHelper.CATEGORY_ID,
                CoverAppDBHelper.TYPE_ID
        };
        Cursor cursor = mDatabase.query(CoverAppDBHelper.ASSETS_TABLE_NAME, columns,
                CoverAppDBHelper.ASSETS_COVERED+" = ? ",
                new String[]{"N"},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Assets assets = new Assets();
                assets.setId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSET_ID)));
                assets.setUid(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_UID)));
                assets.setName(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_NAME)));
                assets.setDescription(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_DESCRIPTION)));
                assets.setCovered(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_COVERED)));
                assets.setImage_one(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_IMAGE_ONE)));
                assets.setCategoryId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CATEGORY_ID)));
                assets.setTypeId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.TYPE_ID)));

                listAssets.add(assets);
            }

            while (cursor.moveToNext());
            cursor.close();
        }

        return listAssets;
    }

    public ArrayList<Assets> readAsset(String type_id) {

        ArrayList<Assets> listAssets = new ArrayList<>();
        //get a list of columns to be retrieved, we need all of them
        String[] columns = {
                CoverAppDBHelper.ASSET_ID,
                CoverAppDBHelper.ASSETS_UID,
                CoverAppDBHelper.ASSETS_NAME,
                CoverAppDBHelper.ASSETS_DESCRIPTION,
                CoverAppDBHelper.ASSETS_COVERED,
                CoverAppDBHelper.ASSETS_IMAGE_ONE,
                CoverAppDBHelper.CATEGORY_ID,
                CoverAppDBHelper.TYPE_ID
        };

        Cursor cursor = mDatabase.query(CoverAppDBHelper.ASSETS_TABLE_NAME, columns,
                CoverAppDBHelper.TYPE_ID+" = ? ",
                new String[]{type_id},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Assets assets = new Assets();
                assets.setId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSET_ID)));
                assets.setUid(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_UID)));
                assets.setName(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_NAME)));
                assets.setDescription(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_DESCRIPTION)));
                assets.setCovered(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_COVERED)));
                assets.setImage_one(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.ASSETS_IMAGE_ONE)));
                assets.setCategoryId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.CATEGORY_ID)));
                assets.setTypeId(cursor.getString(cursor.getColumnIndex(CoverAppDBHelper.TYPE_ID)));

                listAssets.add(assets);
            }
            while (cursor.moveToNext());
            cursor.close();
        }

        return listAssets;
    }

    public boolean editAssets(String id, String uid, String name, String desc, String image_one, String image_two , String image_three) {

        ContentValues args = new ContentValues();
        args.put(CoverAppDBHelper.ASSET_ID, id);
        args.put(CoverAppDBHelper.ASSETS_NAME, name);
        args.put(CoverAppDBHelper.ASSETS_DESCRIPTION, desc);
        args.put(CoverAppDBHelper.ASSETS_IMAGE_ONE, image_one);

        return mDatabase.update(CoverAppDBHelper.ASSETS_NAME,args, CoverAppDBHelper.ASSETS_UID+" = "+uid, null ) > 0;
    }

    public void deleteAll() {
        mDatabase.execSQL(CoverAppDBHelper.TRUNCATE_TABLE_ASSETS);
        mDatabase.execSQL(CoverAppDBHelper.TRUNCATE_TABLE_NOTIFICATION);
        mDatabase.execSQL(CoverAppDBHelper.TRUNCATE_TABLE_VEHICLE);
        mDatabase.execSQL(CoverAppDBHelper.TRUNCATE_TABLE_FEATURES);
        mDatabase.execSQL(CoverAppDBHelper.TRUNCATE_TABLE_CLAIMS);
        mDatabase.execSQL(CoverAppDBHelper.TRUNCATE_TABLE_COVERS);
    }

    private static class CoverAppDBHelper extends SQLiteOpenHelper {

        private static final String USER_ID_COLUMN               =               "CoverAppID";

        //Features
        private static final String FEATURES_TABLE_NAME          =               "FEATURES";
        private static final String FEATURE_UID                  =               "feature_id";
        private static final String FEATURE_NAME                 =               "feature_name";
        private static final String FEATURE_DESCRIPTION          =               "feature_desc";
        private static final String FEATURE_PRICE                =               "feature_price";
        private static final String FEATURE_PARENT               =               "feature_parent";
        private static final String CREATE_TABLE_FEATURES = "CREATE TABLE " + FEATURES_TABLE_NAME + "("
                + FEATURE_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FEATURE_NAME + " VARCHAR(60),"
                + FEATURE_DESCRIPTION + " TEXT,"
                + FEATURE_PRICE + " VARCHAR(15),"
                + FEATURE_PARENT + " VARCHAR(5),"
                + USER_ID_COLUMN + " VARCHAR(15)"
                +");";
        private static final String DROP_TABLE_FEATURES = "DROP TABLE IF EXISTS " + FEATURES_TABLE_NAME + ";";
        private static final String TRUNCATE_TABLE_FEATURES = "DELETE FROM " + FEATURES_TABLE_NAME + ";";

        //Notifications
        private static final String NOTIFICATION_TABLE_NAME           =               "NOTIFICATION";
        private static final String NOTIFICATION_UID                  =               "notification_id";
        private static final String NOTIFICATION_BODY                 =               "notification_body";
        private static final String NOTIFICATION_FLAG                 =               "notification_flag";
        private static final String CREATE_TABLE_NOTIFICATION = "CREATE TABLE " + NOTIFICATION_TABLE_NAME + "("
                + NOTIFICATION_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NOTIFICATION_BODY + " VARCHAR(40),"
                + NOTIFICATION_FLAG + " VARCHAR(10),"
                + USER_ID_COLUMN+ " VARCHAR(15)"
                +");";

        private static final String DROP_TABLE_NOTIFICATION = " DROP TABLE IF EXISTS " + NOTIFICATION_TABLE_NAME + ";";
        private static final String TRUNCATE_TABLE_NOTIFICATION = "DELETE FROM " + NOTIFICATION_TABLE_NAME + ";";

        //Assets
        private static final String ASSETS_TABLE_NAME          =               "ASSETS";
        private static final String ASSET_ID                   =               "asset_id";
        private static final String ASSETS_UID                 =               "assets_id";
        private static final String ASSETS_NAME                =               "assets_name";
        private static final String ASSETS_DESCRIPTION         =               "assets_desc";
        private static final String ASSETS_COVERED             =               "assets_covered";
        private static final String ASSETS_IMAGE_ONE           =               "assets_image_one";
        //        private static final String ASSETS_IMAGE_TWO           =               "assets_image_two";
//        private static final String ASSETS_IMAGE_THREE         =               "assets_image_three";
        private static final String CATEGORY_ID                 =               "category_id";
        private static final String TYPE_ID                    =                "type_id";
        private static final String CREATE_TABLE_ASSETS = "CREATE TABLE " + ASSETS_TABLE_NAME + "("
                + ASSETS_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ASSET_ID + " INTEGER(11),"
                + ASSETS_NAME + " VARCHAR(60),"
                + ASSETS_DESCRIPTION + " TEXT,"
                + ASSETS_COVERED + " VARCHAR(5),"
                + ASSETS_IMAGE_ONE + " VARCHAR(120),"
//                + ASSETS_IMAGE_TWO + " VARCHAR(120),"
//                + ASSETS_IMAGE_THREE + " VARCHAR(120)"
                + CATEGORY_ID + " INTEGER(11),"
                + TYPE_ID + " INTEGER(11)"
                +");";
        private static final String DROP_TABLE_ASSETS = "DROP TABLE IF EXISTS " + ASSETS_TABLE_NAME + ";";
        private static final String TRUNCATE_TABLE_ASSETS = "DELETE FROM " + ASSETS_TABLE_NAME + ";";

        //Vehicle
        private static final String VEHICLE_TABLE_NAME          =               "VEHICLE";
        private static final String VEHICLE_UID                 =               "vehicle_id";
        private static final String VEHICLE_MAKE                =               "vehicle_make";
        private static final String VEHICLE_SERVER_ID           =               "vehicle_server_id";
        private static final String VEHICLE_PLATE_FIRST         =               "vehicle_plate_first";
        private static final String VEHICLE_PLATE_LAST          =               "vehicle_plate_last";
        private static final String VEHICLE_YOM                 =               "vehicle_yom";
        private static final String VEHICLE_MILEAGE             =               "vehicle_mileage";
        private static final String VEHICLE_CC                  =               "vehicle_cc";
        private static final String VEHICLE_VALUE_BEFORE        =               "vehicle_value_before";
        private static final String VEHICLE_VALUE_AFTER         =               "vehicle_value_after";
        private static final String VEHICLE_DATE_ADDED          =               "vehicle_date_added";
        private static final String VEHICLE_COVERED             =               "vehicle_covered";
        private static final String VEHICLE_IMAGE_ONE           =               "vehicle_image_one";
        private static final String VEHICLE_IMAGE_TWO           =               "vehicle_image_two";
        private static final String VEHICLE_IMAGE_THREE         =               "vehicle_image_three";
        private static final String VEHICLE_IMAGE_FOUR          =               "vehicle_image_four";
        private static final String CREATE_TABLE_VEHICLE = "CREATE TABLE " + VEHICLE_TABLE_NAME + "("
                + VEHICLE_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VEHICLE_SERVER_ID + "  VARCHAR(5),"
                + VEHICLE_MAKE + " VARCHAR(60),"
                + VEHICLE_PLATE_FIRST + "  VARCHAR(5),"
                + VEHICLE_PLATE_LAST + " VARCHAR(5),"
                + VEHICLE_YOM + " VARCHAR(5),"
                + VEHICLE_MILEAGE + " VARCHAR(10),"
                + VEHICLE_CC + " VARCHAR(10),"
                + VEHICLE_VALUE_BEFORE + " VARCHAR(10),"
                + VEHICLE_VALUE_AFTER + " VARCHAR(10),"
                + VEHICLE_DATE_ADDED + " VARCHAR(25),"
                + VEHICLE_COVERED + " VARCHAR(5),"
                + VEHICLE_IMAGE_ONE + " VARCHAR(120),"
                + VEHICLE_IMAGE_TWO + " VARCHAR(120),"
                + VEHICLE_IMAGE_THREE + " VARCHAR(120),"
                + VEHICLE_IMAGE_FOUR + " VARCHAR(120)"
                +");";
        private static final String DROP_TABLE_VEHICLE = "DROP TABLE IF EXISTS " + VEHICLE_TABLE_NAME + ";";
        private static final String TRUNCATE_TABLE_VEHICLE = "DELETE FROM " + VEHICLE_TABLE_NAME + ";";

        // Asset Categories
        private static final String ASSET_CATEGORIES_TABLE_NAME =               "ASSET_CATEGORIES";
        private static final String ASSET_CATEGORY_ID           =               "category_id";
        private static final String ASSET_CATEGORY_CID          =               "cid";
        private static final String ASSET_CATEGORY_NAME         =               "category_name";
        private static final String ASSET_CATEGORY_DESCRIPTION  =               "category_description";
        // TODO: Add created and updated at times
        // Creating table
        private static final String CREATE_TABLE_ASSET_CATEGORIES = "CREATE TABLE " + ASSET_CATEGORIES_TABLE_NAME + "("
                + ASSET_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ASSET_CATEGORY_CID + " VARCHAR(11),"
                + ASSET_CATEGORY_DESCRIPTION + " TEXT,"
                + ASSET_CATEGORY_NAME + " VARCHAR(60)"
                + ");";
        private static final String DROP_TABLE_ASSET_CATEGORIES = "DROP TABLE IF EXISTS " + ASSET_CATEGORIES_TABLE_NAME + ";";
        private static final String TRUNCATE_TABLE_ASSET_CATEGORIES = "DELETE FROM " + ASSET_CATEGORIES_TABLE_NAME + ";";

        // Asset Types
        private static final String ASSET_TYPES_TABLE_NAME            =               "ASSET_TYPES";
        private static final String ASSET_TYPES_ID                    =               "type_id";
        private static final String ASSET_TYPES_CATEGORY_ID           =               "category_id";
        private static final String ASSET_TYPES_NAME                  =               "type_name";
        private static final String ASSET_TYPES_DESCRIPTION           =               "type_description";
        // TODO: Add created and updated at times
        // Create table
        private static final String CREATE_TABLE_ASSET_TYPES = "CREATE TABLE " + ASSET_TYPES_TABLE_NAME + "("
                + ASSET_TYPES_ID + " VARCHAR(11), "
                + ASSET_TYPES_CATEGORY_ID + " VARCHAR(11),"
                + ASSET_TYPES_NAME + " VARCHAR(60),"
                + ASSET_TYPES_DESCRIPTION + " VARCHAR(200)"
                + ");";
        private static final String DROP_TABLE_ASSET_TYPES = "DROP TABLE IF EXISTS " + ASSET_TYPES_TABLE_NAME + ";";
        private static final String TRUNCATE_TABLE_ASSET_TYPES = "DELETE FROM " + ASSET_TYPES_TABLE_NAME + ";";

        //Claims
        private static final String CLAIMS_TABLE_NAME          =               "CLAIMS";
        private static final String CLAIM_ID                   =               "id";
        private static final String CLAIM_CLAIM_ID             =               "claim_id";
        private static final String CLAIM_USER_ID              =               "user_id";
        private static final String CLAIM_ASSETS_CLAIMED       =               "assets_claimed";
        private static final String CLAIM_ACCIDENT_TYPE        =               "accident_type";
        private static final String CLAIM_ACCIDENT_DATE        =               "accident_date";
        private static final String CLAIM_ACCIDENT_LOCATION    =               "accident_location";
        private static final String CLAIM_STATE                =               "state";
        private static final String CLAIM_POLICE_ABSTRACT      =               "police_abstract";
        private static final String CLAIM_CURRENT_IMAGE        =               "current_image";
        private static final String CLAIM_CREATED_AT           =                "created_at";
        private static final String CLAIM_UPDATED_AT           =                "updated_at";
        private static final String CLAIM_STATUS               =                "status";
        private static final String CREATE_TABLE_CLAIMS = "CREATE TABLE " + CLAIMS_TABLE_NAME + "("
                + CLAIM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CLAIM_CLAIM_ID + " INTEGER(11),"
                + CLAIM_USER_ID + " INTEGER(11),"
                + CLAIM_ASSETS_CLAIMED + " VARCHAR(120) DEFAULT NULL,"
                + CLAIM_ACCIDENT_TYPE + " VARCHAR(120) DEFAULT NULL,"
                + CLAIM_ACCIDENT_DATE + " VARCHAR(60) DEFAULT NULL,"
                + CLAIM_ACCIDENT_LOCATION + " VARCHAR(120) DEFAULT NULL,"
                + CLAIM_POLICE_ABSTRACT + " VARCHAR(120) DEFAULT NULL,"
                + CLAIM_CURRENT_IMAGE + " VARCHAR(120) DEFAULT NULL,"
                + CLAIM_STATE + " VARCHAR(11) DEFAULT NULL,"
                + CLAIM_STATUS + " VARCHAR(60) DEFAULT NULL,"
                + CLAIM_CREATED_AT + " VARCHAR(60) DEFAULT NULL,"
                + CLAIM_UPDATED_AT + " VARCHAR(60) DEFAULT NULL"
                +");";
        private static final String DROP_TABLE_CLAIMS = "DROP TABLE IF EXISTS " + CLAIMS_TABLE_NAME + ";";
        private static final String TRUNCATE_TABLE_CLAIMS = "DELETE FROM " + CLAIMS_TABLE_NAME + ";";

        // Covers
        private static final String COVERS_TABLE_NAME          =               "COVERS";
        private static final String COVER_ID                   =               "id";
        private static final String COVER_COVER_ID             =               "cover_id";
        private static final String COVER_USER_ID              =               "user_id";
        private static final String COVER_PACKAGE       =               "package";
        private static final String COVER_UNIT_CODE        =               "unit_code";
        private static final String COVER_PRODUCT        =               "product";
        private static final String COVER_NUM_PAYMENTS    =               "num_payments";
        private static final String COVER_SIGNUP_COST                =               "signup_cost";
        private static final String COVER_CREATED      =               "created";
        private static final String COVER_UPDATED_AT        =               "updated_at";
        private static final String COVER_POLICY_NUMBER           =               "policy_number";
        private static final String COVER_RECEIPT_NUMBER           =               "receipt_number";
        private static final String COVER_DRCR_NUMBER           =               "drcr_number";
        private static final String COVER_TNX_CODE           =               "tnx_code";
        private static final String COVER_EXPIRY_DATE           =               "expiry_date";
        private static final String CREATE_TABLE_COVERS = "CREATE TABLE " + COVERS_TABLE_NAME + "("
                + COVER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COVER_COVER_ID + " INTEGER(11),"
                + COVER_USER_ID + " INTEGER(11),"
                + COVER_PACKAGE + " VARCHAR(120) DEFAULT NULL,"
                + COVER_UNIT_CODE + " VARCHAR(120) DEFAULT NULL,"
                + COVER_PRODUCT + " TEXT,"
                + COVER_NUM_PAYMENTS + " VARCHAR(120) DEFAULT NULL,"
                + COVER_SIGNUP_COST + " VARCHAR(120) DEFAULT NULL,"
                + COVER_POLICY_NUMBER + " VARCHAR(120) DEFAULT NULL,"
                + COVER_RECEIPT_NUMBER + " VARCHAR(120) DEFAULT NULL,"
                + COVER_CREATED + " VARCHAR(60) DEFAULT NULL,"
                + COVER_UPDATED_AT + " VARCHAR(60) DEFAULT NULL,"
                + COVER_DRCR_NUMBER + " VARCHAR(120) DEFAULT NULL,"
                + COVER_TNX_CODE + " VARCHAR(120) DEFAULT NULL,"
                + COVER_EXPIRY_DATE + " VARCHAR(120) DEFAULT NULL"
                +");";
        private static final String DROP_TABLE_COVERS = "DROP TABLE IF EXISTS " + COVERS_TABLE_NAME + ";";
        private static final String TRUNCATE_TABLE_COVERS = "DELETE FROM " + COVERS_TABLE_NAME + ";";


        private static final String DATABASE_NAME                     =               "CoverAppDB";
        private static final String COUNT_TABLE_ROWS                  =               "SELECT COUNT(*) FROM ";
        private static final int DATABASE_VERSION                     =                20;
        private Context mContext;

        CoverAppDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_FEATURES);
                db.execSQL(CREATE_TABLE_NOTIFICATION);
                db.execSQL(CREATE_TABLE_ASSETS);
                db.execSQL(CREATE_TABLE_VEHICLE);
                db.execSQL(CREATE_TABLE_ASSET_CATEGORIES);
                db.execSQL(CREATE_TABLE_ASSET_TYPES);
                db.execSQL(CREATE_TABLE_CLAIMS);
                db.execSQL(CREATE_TABLE_COVERS);
            } catch (SQLException e) {
                L.m("SQLException onCreate " + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE_FEATURES);
                db.execSQL(DROP_TABLE_NOTIFICATION);
                db.execSQL(DROP_TABLE_ASSETS);
                db.execSQL(DROP_TABLE_VEHICLE);
                db.execSQL(DROP_TABLE_ASSET_CATEGORIES);
                db.execSQL(DROP_TABLE_ASSET_TYPES);
                db.execSQL(DROP_TABLE_CLAIMS);
                db.execSQL(DROP_TABLE_COVERS);
                onCreate(db);

            } catch (SQLException e) {
                L.m("SQLException onUpgrade " + e);
            }
        }

    }
}
