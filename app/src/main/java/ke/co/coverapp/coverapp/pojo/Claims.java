package ke.co.coverapp.coverapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nick on 9/15/17.
 */

public class Claims implements Parcelable {

    private String claim_id;
    private String user_id;
    private String assets_claimed;
    private String accident_type;
    private String accident_date;
    private String accident_location;
    private String state;
    private String police_abstract;
    private String current_image;
    private String created_at;
    private String updated_at;
    private String status;

    public Claims() {
        //required empty constructor
    }


    protected Claims(Parcel in) {
        claim_id = in.readString();
        user_id = in.readString();
        assets_claimed = in.readString();
        accident_type = in.readString();
        accident_date = in.readString();
        accident_location = in.readString();
        state = in.readString();
        police_abstract = in.readString();
        status = in.readString();
        current_image = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static final Parcelable.Creator<Claims> CREATOR = new Parcelable.Creator<Claims>() {
        @Override
        public Claims createFromParcel(Parcel in) {
            return new Claims(in);
        }

        @Override
        public Claims[] newArray(int size) {
            return new Claims[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(claim_id);
        parcel.writeString(user_id);
        parcel.writeString(assets_claimed);
        parcel.writeString(accident_type);
        parcel.writeString(accident_date);
        parcel.writeString(accident_location);
        parcel.writeString(state);
        parcel.writeString(police_abstract);
        parcel.writeString(status);
        parcel.writeString(current_image);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
    }

    public String getClaimId() {
        return claim_id;
    }

    public String getUserId() {
        return user_id;
    }

    public String getAssetsClaimed() {
        return assets_claimed;
    }

    public String getAccidentType() {
        return accident_type;
    }

    public String getAccidentDate() { return accident_date; }

    public String getAccidentLocation() {
        return accident_location;
    }

    public String getState() {
        return state;
    }

    public String getPoliceAbstract() {
        return police_abstract;
    }

    public String getStatus(){
        return status;
    }

    public String getCurrentImage(){
        return current_image;
    }

    public String getCreatedAt(){
        return created_at;
    }

    public String getUpdatedAt(){
        return updated_at;
    }

    public void setClaimId(String claim_id) {
        this.claim_id = claim_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public void setAssetsClaimed(String assets_claimed) {
        this.assets_claimed = assets_claimed;
    }

    public void setAccidentType(String accident_type) {
        this.accident_type = accident_type;
    }

    public void setAccidentDate(String accident_date) { this.accident_date = accident_date; }

    public void setAccidentLocation(String accident_location) {
        this.accident_location = accident_location;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPoliceAbstract(String police_abstract) {
        this.police_abstract = police_abstract;
    }

    public void setCurrentImage(String current_image) {
        this.current_image = current_image;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdatedAt(String updated_at) {
        this.updated_at = updated_at;
    }
}
