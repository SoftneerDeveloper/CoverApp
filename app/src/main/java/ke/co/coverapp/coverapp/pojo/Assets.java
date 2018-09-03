package ke.co.coverapp.coverapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;


public class Assets implements Parcelable {

    private String id;
    private String uid;
    private String name;
    private String description;
    private String covered;
    private String image_one;
    private String image_two;
    private String image_three;
    private String category_id;
    private String type_id;

    // To be used to select assets to purchase cover for
    private String status;

    public Assets() {
        //required empty constructor
    }


    protected Assets(Parcel in) {
        id = in.readString();
        uid = in.readString();
        name = in.readString();
        description = in.readString();
        covered = in.readString();
        image_one = in.readString();
        image_two = in.readString();
        image_three = in.readString();
        status = in.readString();
        category_id = in.readString();
        type_id = in.readString();
    }

    public static final Creator<Assets> CREATOR = new Creator<Assets>() {
        @Override
        public Assets createFromParcel(Parcel in) {
            return new Assets(in);
        }

        @Override
        public Assets[] newArray(int size) {
            return new Assets[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(uid);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(covered);
        parcel.writeString(image_one);
        parcel.writeString(image_two);
        parcel.writeString(image_three);
        parcel.writeString(status);
        parcel.writeString(category_id);
        parcel.writeString(type_id);
    }

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCovered() { return covered; }

    public String getImage_one() {
        return image_one;
    }

    public String getImage_two() {
        return image_two;
    }

    public String getImage_three() {
        return image_three;
    }

    public String getStatus(){
        return status;
    }

    public String getCategoryId(){
        return category_id;
    }

    public String getTypeId(){
        return type_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCovered(String covered) { this.covered = covered; }

    public void setImage_one(String image_one) {
        this.image_one = image_one;
    }

    // Uncomment when a user can add more than one image to each asset
    public void setImage_two(String image_two) {
        this.image_two = image_two;
    }

    public void setImage_three(String image_three) {
        this.image_three = image_three;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCategoryId(String category_id) {
        this.category_id = category_id;
    }

    public void setTypeId(String type_id) {
        this.type_id = type_id;
    }
}
