package ke.co.coverapp.coverapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nick on 10/11/17.
 */

public class Covers implements Parcelable {

    private String coverId;
    private String userId;
    private String policyNumber;
    private String receiptNumber;
    private String coverPackage;
    private String coverProduct;
    private String unitCode;
    private String createdAt;
    private String updatedAt;
    private String numPayments;
    private String signUpCost;
    private String drCrNumber;
    private String tnxCode;
    private String expiryDate;

    public Covers() {
        //required empty constructor
    }

    protected Covers(Parcel in) {
        coverId = in.readString();
        userId = in.readString();
        policyNumber = in.readString();
        receiptNumber = in.readString();
        coverPackage = in.readString();
        coverProduct = in.readString();
        unitCode = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        numPayments = in.readString();
        signUpCost = in.readString();
        drCrNumber = in.readString();
        tnxCode = in.readString();
        expiryDate = in.readString();
    }

    public static final Creator<Covers> CREATOR = new Creator<Covers>() {
        @Override
        public Covers createFromParcel(Parcel in) {
            return new Covers(in);
        }

        @Override
        public Covers[] newArray(int size) {
            return new Covers[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(coverId);
        parcel.writeString(userId);
        parcel.writeString(policyNumber);
        parcel.writeString(receiptNumber);
        parcel.writeString(coverPackage);
        parcel.writeString(coverProduct);
        parcel.writeString(unitCode);
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
        parcel.writeString(numPayments);
        parcel.writeString(signUpCost);
        parcel.writeString(drCrNumber);
        parcel.writeString(tnxCode);
        parcel.writeString(expiryDate);
    }

    public String getCoverId() {
        return coverId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public String getCoverPackage() {
        return coverPackage;
    }

    public String getCoverProduct() {
        return coverProduct;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getNumPayments() {
        return numPayments;
    }

    public String getSignUpCost() {
        return signUpCost;
    }

    public String getDrCrNumber() {
        return drCrNumber;
    }

    public String getTnxCode() {
        return tnxCode;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setCoverId(String coverId) {
        this.coverId = coverId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public void setCoverPackage(String coverPackage) {
        this.coverPackage = coverPackage;
    }

    public void setCoverProduct(String coverProduct) {
        this.coverProduct = coverProduct;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setNumPayments(String numPayments) {
        this.numPayments = numPayments;
    }

    public void setSignUpCost(String signUpCost) {
        this.signUpCost = signUpCost;
    }

    public void setDrCrNumber(String drCrNumber) {
        this.drCrNumber = drCrNumber;
    }

    public void setTnxCode(String tnxCode) {
        this.tnxCode = tnxCode;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
