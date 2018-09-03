package ke.co.coverapp.coverapp.utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

import static ke.co.coverapp.coverapp.pojo.Keys.keys.*;
import static ke.co.coverapp.coverapp.pojo.Keys.keys.DATA;


/**
 * Created by Clifford Owino on 9/14/2016.
 */
public class ParseUtil {
    public static boolean contains(JSONObject jsonObject, String key) {
        return jsonObject != null && !key.matches("false") && jsonObject.has(key) && !jsonObject.isNull(key);
    }

    public static boolean parseLoginJSON(String response) {

        L.m(response);

        JSONObject loginJson = null;

        try {
            loginJson = new JSONObject(response);

            if (ParseUtil.contains(loginJson, ACCESS_TOKEN)) {
                MyApplication.saveToPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, loginJson.getString(ACCESS_TOKEN));//
            } else {
                MyApplication.saveToPreferences(MyApplication.getAppContext(), ACCESS_TOKEN, ValidationUtil.getDefaultString());
            }

            if (ParseUtil.contains(loginJson, IMAGE)) {

                L.m("Image retrieved!");

                // Check if there is image data
                if (!loginJson.getString(IMAGE).matches("false")) {

                    JSONObject image_data = new JSONObject(loginJson.getString(IMAGE));

                    if (ParseUtil.contains(image_data, NAME)) {
                        L.m("Image name received!");

                        MyApplication.saveToPreferences(MyApplication.getAppContext(), PROF_PIC_URL,
                                UPLOADS_FOLDER + image_data.getString(NAME));
                    } else {
                        L.m("No image name received!");
                        MyApplication.saveToPreferences(MyApplication.getAppContext(), PROF_PIC_URL,
                                UPLOADS_FOLDER + "logo.png");
                    }

                } else {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), PROF_PIC_URL,
                            UPLOADS_FOLDER + "logo.png");
                }

            } else{

                L.m("Image not retrieved!");

                MyApplication.saveToPreferences(MyApplication.getAppContext(), PROF_PIC_URL,
                        UPLOADS_FOLDER + "logo.png");
            }

            L.m("Try run!");

            if (ParseUtil.contains(loginJson, USER_DETAILS)) {

                L.m("Hello - user details");

                JSONObject user_details = new JSONObject(loginJson.getString(USER_DETAILS));
                L.m(user_details.toString());

                if (ParseUtil.contains(user_details, FNAME)) {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), FNAME, user_details.getString(FNAME));
                } else {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), FNAME, ValidationUtil.getDefaultString());
                }

                if (ParseUtil.contains(user_details, LNAME)) {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), LNAME, user_details.getString(LNAME));
                } else {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), LNAME, ValidationUtil.getDefaultString());
                }

                if (ParseUtil.contains(user_details, SNAME)) {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), SNAME, user_details.getString(SNAME));
                } else {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), SNAME, ValidationUtil.getDefaultString());
                }

                if (ParseUtil.contains(user_details, EMAIL)) {
                    if (ValidationUtil.hasValidEmail(user_details.getString(EMAIL))) {

                        MyApplication.saveToPreferences(MyApplication.getAppContext(), EMAIL, user_details.getString(EMAIL));
                    } else {
                        MyApplication.saveToPreferences(MyApplication.getAppContext(), EMAIL, ValidationUtil.getDefault());
                    }
                } else {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), EMAIL, ValidationUtil.getDefault());
                }

                if (ParseUtil.contains(user_details, PHONE_NUMBER)) {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), PHONE_NUMBER, user_details.getString(PHONE_NUMBER));
                } else {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), PHONE_NUMBER, ValidationUtil.getDefaultPhone());
                }


                if (ParseUtil.contains(user_details, ID_NUMBER)) {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), ID_NUMBER, user_details.getString(ID_NUMBER));
                } else {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), ID_NUMBER, ValidationUtil.getDefault());
                }

                if (ParseUtil.contains(user_details, DOB)) {
                    L.m("This is the user's DOB: " + user_details.getString(DOB));
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), DOB, user_details.getString(DOB));
                } else {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), DOB, ValidationUtil.getDefault());
                }

                if (ParseUtil.contains(user_details, GENDER)) {
                    if(user_details.getString(GENDER).equals(FEMALE)) {
                        L.m("Gender: Female");
                        MyApplication.saveToPreferences(MyApplication.getAppContext(), GENDER, FEMALE);
                    }else if (user_details.getString(GENDER).equals(MALE)){
                        L.m("Gender: Male");
                        MyApplication.saveToPreferences(MyApplication.getAppContext(), GENDER, MALE);
                    }else{
                        L.m("Gender: None");
                        MyApplication.saveToPreferences(MyApplication.getAppContext(), GENDER, ValidationUtil.getDefault());
                    }
                } else {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), GENDER, ValidationUtil.getDefault());
                }

                if (ParseUtil.contains(user_details, MARITAL_STATUS)) {
                    if(user_details.getString(MARITAL_STATUS).equals(SINGLE)) {
                        MyApplication.saveToPreferences(MyApplication.getAppContext(), MARITAL_STATUS, SINGLE);
                        L.m("Marital status: Single");
                    }else if (user_details.getString(MARITAL_STATUS).equals(MARRIED)) {
                        MyApplication.saveToPreferences(MyApplication.getAppContext(), MARITAL_STATUS, MARRIED);
                        L.m("Marital status: Married");
                    }else{
                        MyApplication.saveToPreferences(MyApplication.getAppContext(), MARITAL_STATUS, ValidationUtil.getDefault());
                        L.m("Marital status: None");
                    }
                } else {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), MARITAL_STATUS, ValidationUtil.getDefault());
                }

                if (ParseUtil.contains(user_details, DOB)) {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), DOB, user_details.getString(DOB));
                } else {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), DOB, ValidationUtil.getDefault());
                }

                if (ParseUtil.contains(user_details, HOME_ADDRESS)) {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), HOME_ADDRESS, user_details.getString(HOME_ADDRESS));
                } else {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), HOME_ADDRESS, ValidationUtil.getDefault());
                }

                if (ParseUtil.contains(user_details, OCCUPATION)) {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), OCCUPATION, user_details.getString(OCCUPATION));
                } else {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), OCCUPATION, ValidationUtil.getDefault());
                }

                if (ParseUtil.contains(user_details, KRA)) {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), KRA, user_details.getString(KRA));
                } else {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), KRA, ValidationUtil.getDefault());
                }

                if (ParseUtil.contains(user_details, CURR)) {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), CURR, user_details.getString(CURR));
                } else {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), CURR, ValidationUtil.getDefaultCurr());
                }

                if (ParseUtil.contains(user_details, WALLET_BALANCE)) {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), WALLET_BALANCE, user_details.getString(WALLET_BALANCE));
                } else {
                    MyApplication.saveToPreferences(MyApplication.getAppContext(), WALLET_BALANCE, ValidationUtil.getDefaultBalance());
                }

                L.m("True returned");

                return true;

            }

            L.m("True returned");

            return true;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        L.m("False returned");

        return false;
    }

    public static ArrayList<Feature> parseFeatures(JSONObject response) {

        ArrayList<Feature> listFeatures = new ArrayList<>();
        JSONArray feature_array;


        if (ParseUtil.contains(response, FEATURES)) {
            String name = ValidationUtil.getDefault();
            String description = ValidationUtil.getDefault();
            String price = ValidationUtil.getDefault();
            String parent = ValidationUtil.getDefault();

            try {
                feature_array = response.getJSONArray(FEATURES);

                for (int i = 0; i < feature_array.length(); i++) {
                    JSONObject jsonRowObj = feature_array.getJSONObject(i);


                    if (ParseUtil.contains(jsonRowObj, NAME)) {
                        name = jsonRowObj.getString(NAME);
                    }
                    if (ParseUtil.contains(jsonRowObj, DESCRIPTION)) {
                        description = jsonRowObj.getString(DESCRIPTION);
                    }
                    if (ParseUtil.contains(jsonRowObj, PRICE)) {
                        price = jsonRowObj.getString(PRICE);
                    }
                    if (ParseUtil.contains(jsonRowObj, PARENT)) {
                        parent = jsonRowObj.getString(PARENT);
                    }
                    Feature feature = new Feature();

                    feature.setName(name);
                    feature.setDescription(description);
                    feature.setPrice(price);
                    feature.setParent(parent);

                    listFeatures.add(feature);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return listFeatures;
    }

    public static ArrayList<Balance> parseBalance(JSONObject response) {
        ArrayList<Balance> balanceData = new ArrayList<>();
        if (contains(response, BALANCE)) {
            try {
                L.m("This is the balance: " + response.getString(BALANCE));
                MyApplication.saveToPreferences(MyApplication.getAppContext(), WALLET_BALANCE, response.getString(BALANCE));
                String balance = null;
                balance = response.getString(BALANCE);
                Balance data = new Balance();
                data.setBalance(balance);
                balanceData.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return balanceData;
    }

    public static ArrayList<Prices> parsePrices(JSONArray response) {

        ArrayList<Prices> listPrices = new ArrayList<>();
        if (response == null) {
            return listPrices;
        }

        String[] keys_names = {PRICE_AMBULANCE, PRICE_EMERGENCY, PRICE_FIRE, PRICE_HOME, PRICE_MINIMUM_CUSTOM,
                PRICE_RIDE_DELIVERY, PRICE_ROADSIDE, PRICE_SECURITY, PRICE_WATER};
        try {

            for (int key = 0; key < keys_names.length; key++) {
                for (int j = 0; j < response.length(); j++) {

                    JSONObject price_object = response.getJSONObject(j);

                    if (ParseUtil.contains(price_object, PRICE_KEY) && ParseUtil.contains(price_object, VALUE)) {

                        if (price_object.getString(PRICE_KEY).matches(keys_names[key])) {
                            L.m(key + " " + price_object.getString(VALUE) + " is price");
                            MyApplication.saveToPreferences(MyApplication.getAppContext(), keys_names[key], price_object.getString(VALUE));
                        }
                    }

                    Prices price = new Prices();
                    price.setTitle(keys_names[key]);
                    price.setValue(price_object.getString(VALUE));
                    listPrices.add(price);


                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listPrices;
    }

    public static ArrayList<AssetTypes> parseAssetTypes(JSONArray response) {
        ArrayList<AssetTypes> assetTypes = new ArrayList<>();

        if (response == null) {
            return assetTypes;
        }

        String id = ValidationUtil.getDefaultString();
        String category_id = ValidationUtil.getDefaultString();
        String name = ValidationUtil.getDefaultString();
        String description = ValidationUtil.getDefaultString();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonRowObj = response.getJSONObject(i);

                if (ParseUtil.contains(jsonRowObj, ID)) {
                    id = Integer.toString(jsonRowObj.getInt(ID));
                }
                if (ParseUtil.contains(jsonRowObj, CATEGORY_ID)) {
                    category_id = Integer.toString(jsonRowObj.getInt(CATEGORY_ID));
                }
                if (ParseUtil.contains(jsonRowObj, NAME)) {
                    name = jsonRowObj.getString(NAME);
                }
                if (ParseUtil.contains(jsonRowObj, DESCRIPTION)) {
                    description = jsonRowObj.getString(DESCRIPTION);
                }

                AssetTypes assetType = new AssetTypes();

                assetType.setId(id);
                assetType.setCategoryId(category_id);
                assetType.setName(name);
                assetType.setDescription(description);

                assetTypes.add(assetType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return assetTypes;
    }

    public static ArrayList<AssetCategories> parseAssetCategories(JSONArray response) {
        ArrayList<AssetCategories> assetCategories = new ArrayList<>();

        if (response == null) {
            return assetCategories;
        }

        String name = ValidationUtil.getDefaultString();
        String description = ValidationUtil.getDefaultString();
        String cid = ValidationUtil.getDefaultString();

        try {

            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonRowObj = response.getJSONObject(i);

                if (ParseUtil.contains(jsonRowObj, NAME)) {
                    name = jsonRowObj.getString(NAME);
                }
                if (ParseUtil.contains(jsonRowObj, DESCRIPTION)) {
                    description = jsonRowObj.getString(DESCRIPTION);
                }
                if (ParseUtil.contains(jsonRowObj, ID)) {
                    cid = Integer.toString(jsonRowObj.getInt(ID));
                }

                AssetCategories assetCategory = new AssetCategories();

                assetCategory.setName(name);
                assetCategory.setDescription(description);
                assetCategory.setId(cid);

                assetCategories.add(assetCategory);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return assetCategories;
    }

    public static ArrayList<Assets> parseAssets(JSONArray response) {
        ArrayList<Assets> listAssets = new ArrayList<>();
        JSONArray assets_array;

        if (response == null) {
            return listAssets;
        }

        L.m(response.toString());

        String id = "";
        String name = ValidationUtil.getDefault();
        String description = ValidationUtil.getDefault();
        String covered = ValidationUtil.getDefault();
        String image_one = UPLOADS_FOLDER + "logo.png";
        String image_two = UPLOADS_FOLDER + "logo.png";
        String image_three = UPLOADS_FOLDER + "logo.png";
        String input="";
        String category_id = "";
        String type_id = "";

        try {

            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonRowObj = response.getJSONObject(i);

                if (ParseUtil.contains(jsonRowObj, ASSET_ID)) {
                    id = jsonRowObj.getString(ASSET_ID);
                }

                if (ParseUtil.contains(jsonRowObj, NAME)) {
                    name = jsonRowObj.getString(NAME);
                }

                if (ParseUtil.contains(jsonRowObj, COVERED)) {
                    covered = jsonRowObj.getString(COVERED);
                }

                if (ParseUtil.contains(jsonRowObj, DESCRIPTION)) {
                    description = jsonRowObj.getString(DESCRIPTION);
                }

                if (ParseUtil.contains(jsonRowObj, IMAGES)) {

                    JSONArray images_array = jsonRowObj.getJSONArray(IMAGES);
                    if (images_array != null)
                    {
                        for (int j = 0; j < images_array.length(); j++)
                        {
                            JSONObject image_object = images_array.getJSONObject(j);
                            if (ParseUtil.contains(image_object, NAME))
                            {
                                // Uncomment if each asset has multiple images

                                // input+= image_object.getString(NAME)+",";

                                input = image_object.getString(NAME);
                                L.m(image_object.getString(NAME));
                            }
                        }
                    }
                }

                if (ParseUtil.contains(jsonRowObj, CATEGORY_ID)) {
                    category_id = jsonRowObj.getString(CATEGORY_ID);
                }

                if (ParseUtil.contains(jsonRowObj, TYPE_ID)) {
                    type_id = jsonRowObj.getString(TYPE_ID);
                }

                // input = input.substring(0,input.length()-1);
                // String[] store_values = input.split(",");

                Assets assets = new Assets();

                assets.setId(id);
                assets.setName(name);
                assets.setDescription(description);
                assets.setCovered(covered);
                assets.setImage_one(UPLOADS_FOLDER + (input != null ? input: "logo.png"));
                assets.setCategoryId(category_id);
                assets.setTypeId(type_id);

                L.m("Get asset image: " + assets.getImage_one());

                // Loop if each asset has multiple images

                // assets.setImage_one(UPLOADS_FOLDER + (store_values[0] != null ? store_values[0]: "logo.png"));
                // L.m("Images array: " + (store_values[0] != null ? store_values[0]: "logo.png"));

                // Change by Nick - Uncomment when a user is able to add  more than one image for their assets
                // Also uncomment code at insertAsset method in CoverAppDB.java

                // assets.setImage_two(UPLOADS_FOLDER + (store_values[1] != null ? store_values[1]: "logo.png"));
                // assets.setImage_three(UPLOADS_FOLDER + (store_values[2] != null ? store_values[2]: "logo.png"));

                listAssets.add(assets);

            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
//        if (ParseUtil.contains(response, ASSETS)) {
//            String name = ValidationUtil.getDefault();
//            String description = ValidationUtil.getDefault();
//            String image_one = UPLOADS_FOLDER + "logo.png";
//            String image_two = UPLOADS_FOLDER + "logo.png";
//            String image_three = UPLOADS_FOLDER + "logo.png";
//            String input="";
//
//            try {
//                assets_array = (JSONArray) response.getJSONArray(ASSETS);
//
//                for (int i = 0; i < assets_array.length(); i++) {
//                    JSONObject jsonRowObj = assets_array.getJSONObject(i);
//
//
//                    if (ParseUtil.contains(jsonRowObj, NAME)) {
//                        name = jsonRowObj.getString(NAME);
//                    }
//                    if (ParseUtil.contains(jsonRowObj, DESCRIPTION)) {
//                        description = jsonRowObj.getString(DESCRIPTION);
//                    }
//
//                    if (ParseUtil.contains(jsonRowObj, IMAGES)) {
//
//                        JSONArray images_array = jsonRowObj.getJSONArray(IMAGES);
//                        if (images_array != null)
//                        {
//                            for (int j = 0; j < images_array.length(); j++)
//                            {
//                                JSONObject image_object = images_array.getJSONObject(j);
//                                if (ParseUtil.contains(image_object, NAME))
//                                {
//                                    input+= image_object.getString(NAME)+",";
//                                    L.m(image_object.getString(NAME));
//                                }
//                            }
//                        }
//                    }
//                    input = input.substring(0,input.length()-1);
//                    String[] store_values = input.split(",");
//                    L.m(input);
//                    Assets assets = new Assets();
//
//                    assets.setName(name);
//                    assets.setDescription(description);
//                    assets.setImage_one(UPLOADS_FOLDER + (store_values[0] !=null ? store_values[0]: "logo.png"));
//                    assets.setImage_two(UPLOADS_FOLDER + (store_values[1] !=null ? store_values[1]: "logo.png"));
//                    assets.setImage_three(UPLOADS_FOLDER + (store_values[2] !=null ? store_values[2]: "logo.png"));
//                    listAssets.add(assets);
//                }
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

        return listAssets;
    }

    public static ArrayList<Covers> parseCovers(JSONObject response) {
        ArrayList<Covers> listCovers = new ArrayList<>();
        JSONArray coversArray;

        if (response == null) {
            return listCovers;
        }

        L.m("Claims response: " + response.toString());

        String coverId = "";
        String userId = "";
        String policyNumber = "";
        String receiptNumber = "";
        String coverPackage = "";
        String coverProduct = "";
        String unitCode = "";
        String createdAt = "";
        String numPayments = "";
        String signUpCost = "";
        String updatedAt = "";
        String drCrNumber = "";
        String tnxCode = "";
        String expiryDate = "";

        if (ParseUtil.contains(response, DATA)) {

            try {
                coversArray = response.getJSONArray(DATA);

                if (coversArray != null) {
                    // Parsing array of objects
                    L.m("Cover array length: " + coversArray.length());

                    for (int i = 0; i < coversArray.length(); i++) {
                        JSONObject jsonRowObj = coversArray.getJSONObject(i);

                        if (ParseUtil.contains(jsonRowObj, ID)) {
                            coverId = jsonRowObj.getString(ID);
                        }

                        if (ParseUtil.contains(jsonRowObj, USER_ID)) {
                            userId = jsonRowObj.getString(USER_ID);
                        }

                        if (ParseUtil.contains(jsonRowObj, PACKAGE)) {
                            coverPackage = jsonRowObj.getString(PACKAGE);
                        }

                        if (ParseUtil.contains(jsonRowObj, UNIT_CODE)) {
                            unitCode = jsonRowObj.getString(UNIT_CODE);
                        }

                        if (ParseUtil.contains(jsonRowObj, PRODUCT)) {
                            coverProduct = jsonRowObj.getString(PRODUCT);
                        }

                        if (ParseUtil.contains(jsonRowObj, NUM_PAYMENTS)) {
                            numPayments = jsonRowObj.getString(NUM_PAYMENTS);
                        }

                        if (ParseUtil.contains(jsonRowObj, SIGNUP_COST)) {
                            signUpCost = jsonRowObj.getString(SIGNUP_COST);
                        }

                        if (ParseUtil.contains(jsonRowObj, CREATED)) {
                            createdAt = jsonRowObj.getString(CREATED);
                        }

                        if (ParseUtil.contains(jsonRowObj, UPDATED_AT)) {
                            updatedAt = jsonRowObj.getString(UPDATED_AT);
                        }

                        if (ParseUtil.contains(jsonRowObj, POLICY_NUMBER)) {
                            policyNumber = jsonRowObj.getString(POLICY_NUMBER);
                        }

                        if (ParseUtil.contains(jsonRowObj, RECEIPT_NUMBER)) {
                            receiptNumber = jsonRowObj.getString(RECEIPT_NUMBER);
                        }

                        if (ParseUtil.contains(jsonRowObj, DRCR_NUMBER)) {
                            drCrNumber = jsonRowObj.getString(DRCR_NUMBER);
                        }

                        if (ParseUtil.contains(jsonRowObj, TNX_CODE)) {
                            tnxCode = jsonRowObj.getString(TNX_CODE);
                        }

                        if (ParseUtil.contains(jsonRowObj, EXPIRY_DATE)) {
                            expiryDate = jsonRowObj.getString(EXPIRY_DATE);
                        }

                        Covers covers = new Covers();

                        covers.setCoverId(coverId);
                        covers.setUserId(userId);
                        covers.setUpdatedAt(updatedAt);
                        covers.setCreatedAt(createdAt);
                        covers.setCoverPackage(coverPackage);
                        covers.setCoverProduct(coverProduct);
                        covers.setNumPayments(numPayments);
                        covers.setPolicyNumber(policyNumber);
                        covers.setReceiptNumber(receiptNumber);
                        covers.setSignUpCost(signUpCost);
                        covers.setUnitCode(unitCode);
                        covers.setDrCrNumber(drCrNumber);
                        covers.setTnxCode(tnxCode);
                        covers.setExpiryDate(expiryDate);

                        listCovers.add(covers);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return listCovers;
    }

    public static ArrayList<Claims> parseClaims(JSONObject response) {
        ArrayList<Claims> listClaims = new ArrayList<>();
        JSONArray claims_array;

        if (response == null) {
            return listClaims;
        }

        L.m("Claims response: " + response.toString());

        String claim_id = "";
        String user_id = "";
        String assets_claimed = "";
        String accident_type = "";
        String accident_date = "";
        String accident_location = "";
        String status = "";
        String[] images = new String[2];
        String created_at = "";
        String updated_at = "";
        String state = "";

        if (ParseUtil.contains(response, DATA)) {

            try {
                claims_array = response.getJSONArray(DATA);

                if (claims_array != null) {

                    for (int i = 0; i < claims_array.length(); i++) {
                        JSONObject jsonRowObj = claims_array.getJSONObject(i);

                        if (ParseUtil.contains(jsonRowObj, CLAIM_ID)) {
                            claim_id = jsonRowObj.getString(CLAIM_ID);
                            L.m("claim_id: " + claim_id);
                        }

                        if (ParseUtil.contains(jsonRowObj, USER_ID)) {
                            user_id = jsonRowObj.getString(USER_ID);
                            L.m("user_id: " + user_id);
                        }

                        if (ParseUtil.contains(jsonRowObj, ASSETS_CLAIMED)) {
                            assets_claimed = jsonRowObj.getString(ASSETS_CLAIMED);
                            L.m("assets_claimed: " + assets_claimed);
                        }

                        if (ParseUtil.contains(jsonRowObj, ACCIDENT_TYPE)) {
                            accident_type = jsonRowObj.getString(ACCIDENT_TYPE);
                            L.m("accident_type: " + accident_type);
                        }

                        if (ParseUtil.contains(jsonRowObj, ACCIDENT_DATE)) {
                            accident_date = jsonRowObj.getString(ACCIDENT_DATE);
                            L.m("accident_date: " + accident_date);
                        }

                        if (ParseUtil.contains(jsonRowObj, ACCIDENT_LOCATION)) {
                            accident_location = jsonRowObj.getString(ACCIDENT_LOCATION);
                            L.m("accident_location: " + accident_location);
                        }

                        if (ParseUtil.contains(jsonRowObj, STATUS)) {
                            status = jsonRowObj.getString(STATUS);
                            L.m("status: " + status);
                        }

                        if (ParseUtil.contains(jsonRowObj, STATE)) {
                            state = jsonRowObj.getString(STATE);
                            L.m("state: " + state);
                        }

                        if (ParseUtil.contains(jsonRowObj, CREATED_AT)) {
                            created_at = jsonRowObj.getString(CREATED_AT);
                            L.m("created_at: " + created_at);
                        }

                        if (ParseUtil.contains(jsonRowObj, UPDATED_AT)) {
                            updated_at = jsonRowObj.getString(UPDATED_AT);
                            L.m("Updated at: " + updated_at);
                        }

                        if (ParseUtil.contains(jsonRowObj, IMAGES)) {

                            JSONArray images_array = jsonRowObj.getJSONArray(IMAGES);

                            L.m("Images length: " + images_array.length());

                            if (images_array != null && images_array.length() != 0) {
                                //                        for (int j = 0; j < images_array.length(); j++)
                                // TODO: Get a better way to capture both images
                                for (int j = 0; j < 2; j++) // Get the two images per claim
                                {
                                    JSONObject image_object = images_array.getJSONObject(j);
                                    if (ParseUtil.contains(image_object, NAME)) {
                                        // Insert into array
                                        images[j] = image_object.getString(NAME);
                                    }
                                }
                            }
                        }

                        L.m("Adding claim process");
                        Claims claim = new Claims();

                        claim.setClaimId(claim_id);
                        claim.setUserId(user_id);
                        claim.setAssetsClaimed(assets_claimed);
                        claim.setAccidentDate(accident_date);
                        claim.setAccidentType(accident_type);
                        claim.setAccidentLocation(accident_location);
                        claim.setCurrentImage(UPLOADS_FOLDER + (images[0] != null ? images[0] : "logo.png"));
                        claim.setPoliceAbstract(UPLOADS_FOLDER + (images[1] != null ? images[1] : "logo.png"));
                        claim.setState(state);
                        claim.setStatus(status);
                        claim.setCreatedAt(created_at);
                        claim.setUpdatedAt(updated_at);

                        listClaims.add(claim);

                        L.m("Done adding claim process");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return listClaims;
    }

    public static ArrayList<Vehicle> parseVehicle(JSONArray response) {

        ArrayList<Vehicle> listVehicle = new ArrayList<>();

        if (response == null) {
            return listVehicle;
        }

        L.m(response.toString());

        String vehicle_make = ValidationUtil.getDefault();
        String yom = ValidationUtil.getDefault();
        String plate_first = ValidationUtil.getDefault();
        String plate_last = ValidationUtil.getDefault();
        String odometer = ValidationUtil.getDefault();
        String engine_capacity = ValidationUtil.getDefault();
        String vehicle_id = ValidationUtil.getDefault();
        String value_before = ValidationUtil.getDefault();
        String value_after = ValidationUtil.getDefault();
        String date_aaded = ValidationUtil.getDefault();
        String car_use = ValidationUtil.getDefault();
        String valued = ValidationUtil.getDefault();
        String covered = ValidationUtil.getDefault();
        String image_one = UPLOADS_FOLDER + "logo.png";
        String image_two = UPLOADS_FOLDER + "logo.png";
        String image_three = UPLOADS_FOLDER + "logo.png";
        String image_four = UPLOADS_FOLDER + "logo.png";
//        String input="";

        try {

            for (int i = 0; i < response.length(); i++) {

                String input="";

                JSONObject jsonRowObj = response.getJSONObject(i);

                if (ParseUtil.contains(jsonRowObj, VEHICLE_MAKE)) {
                    vehicle_make = jsonRowObj.getString(VEHICLE_MAKE);
                }
                if (ParseUtil.contains(jsonRowObj, VEHICLE_ID)) {
                    vehicle_id = jsonRowObj.getString(VEHICLE_ID);
                }
                if (ParseUtil.contains(jsonRowObj, YOM)) {
                    yom = jsonRowObj.getString(YOM);
                }
                if (ParseUtil.contains(jsonRowObj, PLATE_FIRST)) {
                    plate_first = jsonRowObj.getString(PLATE_FIRST);
                }
                if (ParseUtil.contains(jsonRowObj, PLATE_LAST)) {
                    plate_last = jsonRowObj.getString(PLATE_LAST);
                }
                if (ParseUtil.contains(jsonRowObj, COVERED)) {
                    covered = jsonRowObj.getString(COVERED);
                }


                if (ParseUtil.contains(jsonRowObj, ODOMETER)) {
                    odometer = jsonRowObj.getString(ODOMETER);
                }
                if (ParseUtil.contains(jsonRowObj, ENGINE_CAPACITY)) {
                    engine_capacity = jsonRowObj.getString(ENGINE_CAPACITY);
                }

                if (ParseUtil.contains(jsonRowObj, "created")) {
                    date_aaded = new JSONObject(jsonRowObj.getString("created")).getString(VEHICLE_DATE_ADDED);
                }


                if (ParseUtil.contains(jsonRowObj, VALUE_BEFORE)) {
                    value_before = jsonRowObj.getString(VALUE_BEFORE);
                }
                if (ParseUtil.contains(jsonRowObj, VALUE_AFTER)) {
                    value_after = jsonRowObj.getString(VALUE_AFTER);
                }
                if (ParseUtil.contains(jsonRowObj, CAR_USE)) {
                    car_use = jsonRowObj.getString(CAR_USE);
                }

                if (ParseUtil.contains(jsonRowObj, IMAGES)) {

                    JSONArray images_array = jsonRowObj.getJSONArray(IMAGES);
                    if (images_array != null)
                    {
                        for (int j = 0; j < images_array.length(); j++)
                        {
                            JSONObject image_object = images_array.getJSONObject(j);
                            if (ParseUtil.contains(image_object, NAME))
                            {
                                input += image_object.getString(NAME)+",";
                                L.m(image_object.getString(NAME));
                            }
                        }
                    }
                }
                Vehicle vehicle = new Vehicle();

                if (input.length()>3){
                    input = input.substring(0,input.length()-1);
                    String[] store_values = input.split(",");
                    L.m("Vehicle INPUT: " + input);
                    vehicle.setImage_one(UPLOADS_FOLDER + (null != store_values[0] ? store_values[0]: "logo.png"));
                    vehicle.setImage_two(UPLOADS_FOLDER + (null != store_values[1] ? store_values[1]: "logo.png"));
                    vehicle.setImage_three(UPLOADS_FOLDER + (null != store_values[2] ? store_values[2]: "logo.png"));
                    vehicle.setImage_four(UPLOADS_FOLDER + (null != store_values[3] ? store_values[3]: "logo.png"));

                }

                L.m("Image one: " + vehicle.getImage_one());
                L.m("Image two: " + vehicle.getImage_two());
                L.m("Image three: " + vehicle.getImage_three());
                L.m("Image four: " + vehicle.getImage_four());

                vehicle.setCar_value_before(value_before);
                vehicle.setCar_value_after(value_after);
                vehicle.setDate_added(date_aaded);
                vehicle.setEngine_capacity(engine_capacity);
                vehicle.setId(vehicle_id);
                vehicle.setOdometer(odometer);
                vehicle.setPlate_first(plate_first);
                vehicle.setPlate_last(plate_last);
                vehicle.setVehicle_make(vehicle_make);
                vehicle.setIs_covered(covered);
                vehicle.setYom(yom);

                listVehicle.add(vehicle);
            }


        } catch (JSONException e) {
            e.printStackTrace();

        }

        return listVehicle;
    }

}
