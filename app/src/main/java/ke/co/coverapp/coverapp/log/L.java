package ke.co.coverapp.coverapp.log;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Clifford Owino on 9/14/2016.
 */
public class L {
    public static void m(String message){
        Log.d("coverApp", "" + message);
    }

    public static void t(Context context, String message){
        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
    }
    public static void T(Context context, String message){
        Toast.makeText(context, ""+message, Toast.LENGTH_LONG).show();
    }
}
