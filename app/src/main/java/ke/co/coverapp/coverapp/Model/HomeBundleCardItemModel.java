package ke.co.coverapp.coverapp.Model;

/**
 * Created by user001 on 13/02/2018.
 */

public class HomeBundleCardItemModel {

    public String textViewCoverType, textViewCostPerMonth, textViewYourCover, textViewDescription;

    public HomeBundleCardItemModel(String textViewCoverType, String textViewCostPerMonth, String textViewYourCover, String textViewDescription) {
        this.textViewCoverType = textViewCoverType;
        this.textViewCostPerMonth = textViewCostPerMonth;
        this.textViewYourCover = textViewYourCover;
        this.textViewDescription = textViewDescription;
    }

    public String getTextViewCoverType() {
        return textViewCoverType;
    }

    public void setTextViewCoverType(String textViewCoverType) {
        this.textViewCoverType = textViewCoverType;
    }

    public String getTextViewCostPerMonth() {
        return textViewCostPerMonth;
    }

    public void setTextViewCostPerMonth(String textViewCostPerMonth) {
        this.textViewCostPerMonth = textViewCostPerMonth;
    }

    public String getTextViewYourCover() {
        return textViewYourCover;
    }

    public void setTextViewYourCover(String textViewYourCover) {
        this.textViewYourCover = textViewYourCover;
    }

    public String getTextViewDescription() {
        return textViewDescription;
    }

    public void setTextViewDescription(String textViewDescription) {
        this.textViewDescription = textViewDescription;
    }
}
