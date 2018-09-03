package ke.co.coverapp.coverapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nick on 6/16/17.
 */

public class AssetTypes implements Parcelable {

    private String id;
    private String category_id;
    private String name;
    private String description;

    public AssetTypes(){
        // Required empty constructor
    }

    protected AssetTypes(Parcel in){
        id = in.readString();
        category_id = in.readString();
        name = in.readString();
        description = in.readString();
    }

    public static final Creator<AssetTypes> CREATOR = new Creator<AssetTypes>() {
        @Override
        public AssetTypes createFromParcel(Parcel parcel) {
            return new AssetTypes(parcel);
        }

        @Override
        public AssetTypes[] newArray(int i) {
            return new AssetTypes[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(category_id);
        parcel.writeString(name);
        parcel.writeString(description);
    }

    public String getId() {
        return id;
    }

    public String getCategoryId() {
        return category_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategoryId(String category_id) {
        this.category_id = category_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
