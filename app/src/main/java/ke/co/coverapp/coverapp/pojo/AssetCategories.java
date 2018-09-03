package ke.co.coverapp.coverapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nick on 6/9/17.
 */

public class AssetCategories implements Parcelable {

    private String id;
    private String name;
    private String description;

    public AssetCategories() {
        // Required empty constructor
    }

    protected AssetCategories(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
    }

    public static final Creator<AssetCategories> CREATOR = new Creator<AssetCategories>() {
        @Override
        public AssetCategories createFromParcel(Parcel in) {
            return new AssetCategories(in);
        }

        @Override
        public AssetCategories[] newArray(int size) {
            return new AssetCategories[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
    }

    public String getId() {
        return id;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
