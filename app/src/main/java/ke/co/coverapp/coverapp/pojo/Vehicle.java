package ke.co.coverapp.coverapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Clifford Owino on 3/1/2017.
 */

public class Vehicle implements Parcelable {

    private String vehicle_make;
    private String yom;
    private String plate_first;
    private String plate_last;
    private String odometer;
    private String engine_capacity;
    private String car_value_before;
    private String car_value_after;
    private String id;
    private String date_added;
    private String image_one;
    private String image_two;
    private String image_three;
    private String image_four;
    private String is_covered;

    public Vehicle(){
        //required empty constructor
    }


    protected Vehicle(Parcel in) {
        vehicle_make = in.readString();
        yom = in.readString();
        plate_first = in.readString();
        plate_last = in.readString();
        odometer = in.readString();
        engine_capacity = in.readString();
        car_value_before = in.readString();
        car_value_after = in.readString();
        id = in.readString();
        date_added = in.readString();
        image_one = in.readString();
        image_two = in.readString();
        image_three = in.readString();
        image_four = in.readString();
        is_covered = in.readString();
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    public String getVehicle_make() {
        return vehicle_make;
    }

    public void setVehicle_make(String vehicle_make) {
        this.vehicle_make = vehicle_make;
    }

    public String getYom() {
        return yom;
    }

    public void setYom(String yom) {
        this.yom = yom;
    }

    public String getPlate_first() {
        return plate_first;
    }

    public void setPlate_first(String plate_first) {
        this.plate_first = plate_first;
    }

    public String getPlate_last() {
        return plate_last;
    }

    public void setPlate_last(String plate_last) {
        this.plate_last = plate_last;
    }

    public String getOdometer() {
        return odometer;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }

    public String getEngine_capacity() {
        return engine_capacity;
    }

    public void setEngine_capacity(String engine_capacity) {
        this.engine_capacity = engine_capacity;
    }

    public String getCar_value_before() {
        return car_value_before;
    }

    public void setCar_value_before(String car_value_before) {
        this.car_value_before = car_value_before;
    }

    public String getCar_value_after() {
        return car_value_after;
    }

    public void setCar_value_after(String car_value_after) {
        this.car_value_after = car_value_after;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getImage_one() {
        return image_one;
    }

    public void setImage_one(String image_one) {
        this.image_one = image_one;
    }

    public String getImage_two() {
        return image_two;
    }

    public void setImage_two(String image_two) {
        this.image_two = image_two;
    }

    public String getImage_three() {
        return image_three;
    }

    public void setImage_three(String image_three) {
        this.image_three = image_three;
    }

    public String getImage_four() {
        return image_four;
    }

    public void setImage_four(String image_four) {
        this.image_four = image_four;
    }

    public String getIs_covered() {
        return is_covered;
    }

    public void setIs_covered(String is_covered) {
        this.is_covered = is_covered;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(vehicle_make);
        parcel.writeString(yom);
        parcel.writeString(plate_first);
        parcel.writeString(plate_last);
        parcel.writeString(odometer);
        parcel.writeString(engine_capacity);
        parcel.writeString(car_value_before);
        parcel.writeString(car_value_after);
        parcel.writeString(id);
        parcel.writeString(date_added);
        parcel.writeString(image_one);
        parcel.writeString(image_two);
        parcel.writeString(image_three);
        parcel.writeString(image_four);
        parcel.writeString(is_covered);
    }
}
