package ke.co.coverapp.coverapp.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatEditText;
import android.util.Base64;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.security.SecureRandom;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import ke.co.coverapp.coverapp.log.L;

/**
 * Created by Clifford Owino on 9/14/2016.
 */
public class ValidationUtil {

     private static final String AB = UUID.randomUUID().toString();
    private static String[] bannedChars = {";", " :", " ~", " `", " !", " % ", "^", " *", " >", " <", " &", "-"};
    private static SecureRandom rnd = new SecureRandom();


    /**
     * Random string generator
     * @param len | length of the random string
     * @return the generated string
     */
    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        String approved = sb.toString();

        for (int i = 0; i < bannedChars.length; i++) {
            if (approved.contains(bannedChars[i])) {
                approved = approved.replace(bannedChars[i],"A");
            }
        }
        return approved;
    }

    
    /**
     * to validate the input fields
     *
     * @param editText | Arbitrary type
     * @return boolean
     */
    public static boolean hasValidContents(EditText editText) {
        if (editText != null
                && editText.getText() != null
                && editText.getText().toString().trim().length() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Regex to validate the mobile number
     * mobile number should be of 7 digits length
     *
     * @param editText | The phone number field
     * @return boolean
     */
    public static boolean isValidPhoneNumber(AppCompatEditText editText) {

        if (!hasValidContents(editText)) {
            return false;
        }

        if (editText.getText() != null
                && editText.getText().toString().trim().length() > 7) {


            String regEx = "^[0-9]{10}$";
            return editText.getText().toString().matches(regEx);
        }
        return false;

    }

    /**
     * mobile number should be start with a 7
     *
     * @param phoneField | The phone number field
     * @return String
     */

    public static String validPhoneNumber(EditText phoneField) {
        String phone = phoneField.getText().toString().trim();
        try {
            if (phone.substring(0, 4).matches("2547")) {
                return phone.replaceFirst("2547", "7");
            } else if (phone.substring(0, 2).matches("07")) {
                return phone.replaceFirst("07", "7");
            }
        } catch (StringIndexOutOfBoundsException e) {
            L.m(e.getMessage());
        }


        return phone;//is valid phone number

    }

    public static String validPhoneNumber(String phoneNumber) {
        if (phoneNumber.substring(0, 4).matches("2547")) {
            return phoneNumber.replaceFirst("2547", "7");
        } else if (phoneNumber.substring(0, 2).matches("07")) {
            return phoneNumber.replaceFirst("07", "7");
        } else {
            return phoneNumber;//is valid phone number
        }
    }

    public static String random(int length) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(length);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    /**
     * @return string | the default string
     */
    public static String getDefault() {
        return "Not Available";
    }

    /**
     * @return string | the default wallet balance
     */
    public static String getDefaultBalance() {
        return "0.00";
    }

    /**
     * @return String | the default wallet balance
     */
    public static String getDefaultPrice() {
        return "0";
    }

    /**
     * @return string | the default string
     */
    public static String getDefaultCurr() {
        return "KES";
    }

    /**
     * @return string | the default string
     */
    public static String getDefaultString() {
        return "";
    }

    /**
     * @return string | the default phone mask
     */
    public static String getDefaultPhone() {
        return "0700000000";
    }


    /**
     * @param balance | the wallet balance
     * @return true if the balance is logically true
     */
    public static boolean isValidBalance(String balance) {
        if (Float.parseFloat(balance) < 1) {
            return false;
        }
        return true;
    }



    /**
     * @param prof_edit_email | Email text field
     * @return true if valid
     */
    public static boolean hasValidEmail(EditText prof_edit_email) {

        String email = prof_edit_email.getText().toString();

        if (!hasValidContents(prof_edit_email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * @param emailAdd | a string to be validated
     * @return true if valid email
     */
    public static boolean hasValidEmail(String emailAdd) {
L.m("android.util.Patterns.EMAIL_ADDRESS.matcher(emailAdd).matches() "+android.util.Patterns.EMAIL_ADDRESS.matcher(emailAdd).matches());
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailAdd).matches();
    }

    /**
     * @param one | password field
     * @param two |repeat password field
     * @return true if they match
     */
    public static boolean textMatch(EditText one, EditText two) {

        if (!hasValidContents(one) || !hasValidContents(two)) {
            return false;
        }

        if (!matches(one, two)) {
            return false;
        }
        return true;
    }

    /**
     * @param one | Text field to get value one
     * @param two |  Text field to get value two
     * @return true if contents match
     */
    private static boolean matches(EditText one, EditText two) {
        if (one.getText().toString().matches(two.getText().toString())) {
            return true;
        }

        return false;
    }

    public static boolean radioChecked(RadioGroup group) {

        if (group.getCheckedRadioButtonId() == -1) {
            return false;
        }
        return true;

    }
    /**
     * This method accepts a Bitmap image and converts it to a Base 64 encoded string
     * @param bmp A bitmap image
     * @return String encodedImage
     */
    public static String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return  Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    /**
     * This method accepts a Base 64 encoded string version of an image and converts it back to a bitmap image
     * @param encodedImage Base64 encoded string version of a Bitmap image
     * @return the decoded Bitmap
     */
    public static Bitmap getBitmapFromString(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
