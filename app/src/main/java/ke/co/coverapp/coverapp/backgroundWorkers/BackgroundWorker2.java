package ke.co.coverapp.coverapp.backgroundWorkers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.ContextThemeWrapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.activities.LegacyLifePlan;
import ke.co.coverapp.coverapp.activities.RunTimeTopUpActivity;

public class BackgroundWorker2 extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog alertDialog;
    public BackgroundWorker2 (Context ctx)
    {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        String login_url = "http://coverapp.co.ke/LegacyLifePlan.php";
        if (type.equals("legacy"))
        {

            try {
                String cover_amount = params[1];
                String id_numberMain = params[2];//user_id

                String id_number = params[3];
                String email = params[4];
                String phone = params[5];
                String vehicle_type = params[6];
                String make = params[7];
                String model = params[8];
                String reg_plates = params[9];
                String cover_start_date = params[10];
                String pickup_vs_delivery = params[11];
                String delivery_address = params[12];
                String text_id_number = params[13];



                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection .setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("cover_amount", "UTF-8")+"="+URLEncoder.encode(cover_amount, "UTF-8")+"&"


                        +URLEncoder.encode("id_numberMain", "UTF-8")+"="+URLEncoder.encode(id_numberMain, "UTF-8")+"&"
                        +URLEncoder.encode("id_number", "UTF-8")+"="+URLEncoder.encode(id_number, "UTF-8")+"&"
                        +URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8")+"&"
                        +URLEncoder.encode("phone", "UTF-8")+"="+URLEncoder.encode(phone, "UTF-8")+"&"
                        +URLEncoder.encode("vehicle_type", "UTF-8")+"="+URLEncoder.encode(vehicle_type, "UTF-8")+"&"
                        +URLEncoder.encode("make", "UTF-8")+"="+URLEncoder.encode(make, "UTF-8")+"&"
                        +URLEncoder.encode("model", "UTF-8")+"="+URLEncoder.encode(model, "UTF-8")+"&"
                        +URLEncoder.encode("reg_plates", "UTF-8")+"="+URLEncoder.encode(reg_plates, "UTF-8")+"&"
                        +URLEncoder.encode("cover_start_date", "UTF-8")+"="+URLEncoder.encode(cover_start_date, "UTF-8")+"&"
                        +URLEncoder.encode("pickup_vs_delivery", "UTF-8")+"="+URLEncoder.encode(pickup_vs_delivery, "UTF-8")+"&"
                        +URLEncoder.encode("delivery_address", "UTF-8")+"="+URLEncoder.encode(delivery_address, "UTF-8")+"&"


                        + URLEncoder.encode("id_numberMain", "UTF-8")+"="+URLEncoder.encode(id_numberMain, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine() )!= null)
                {
                    result += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogCustom).create();
        //AlertDialog.Builder alertDialog = new AlertDialog.Builder(
               // new ContextThemeWrapper(context, android.R.style.Theme_Dialog));
        alertDialog.setTitle("Legacy Life Plan");
    }

    @Override
    protected void onPostExecute(String result) {

        if (result.toString().equalsIgnoreCase("success")) {
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((LegacyLifePlan)context).finish();
                }
            });
            alertDialog.setMessage("You have successfully purchased this product, check your email for more information");
            alertDialog.show();
        }
        else
        {
            alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Top Up", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    context.startActivity(new Intent(context, RunTimeTopUpActivity.class));
                }
            });
            alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });
            alertDialog.setMessage(result);
            alertDialog.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
