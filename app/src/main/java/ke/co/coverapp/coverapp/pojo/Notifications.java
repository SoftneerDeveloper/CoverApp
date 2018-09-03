package ke.co.coverapp.coverapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Clifford Owino on 11/25/2016.
 */

public class Notifications implements Parcelable {

    private String notify_id;
    private String notify_body;
    private String notify_flag;

    public Notifications(){
        //required empty constructor
    }

    protected Notifications(Parcel in) {
        notify_id = in.readString();
        notify_body = in.readString();
        notify_flag = in.readString();
    }

    public static final Creator<Notifications> CREATOR = new Creator<Notifications>() {
        @Override
        public Notifications createFromParcel(Parcel in) {
            return new Notifications(in);
        }

        @Override
        public Notifications[] newArray(int size) {
            return new Notifications[size];
        }
    };

    public String getNotify_id() {
        return notify_id;
    }

    public String getNotify_body() {
        return notify_body;
    }

    public String getNotify_flag() {
        return notify_flag;
    }

    public void setNotify_id(String notify_id) {
        this.notify_id = notify_id;
    }

    public void setNotify_body(String notify_body) {
        this.notify_body = notify_body;
    }

    public void setNotify_flag(String notify_flag) {
        this.notify_flag = notify_flag;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(notify_id);
        dest.writeString(notify_body);
        dest.writeString(notify_flag);
    }
}
