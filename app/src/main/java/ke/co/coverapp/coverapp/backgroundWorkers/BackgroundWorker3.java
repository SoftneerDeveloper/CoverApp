package ke.co.coverapp.coverapp.backgroundWorkers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

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
import ke.co.coverapp.coverapp.activities.IceaTravelCover;
import ke.co.coverapp.coverapp.activities.LegacyLifePlan;
import ke.co.coverapp.coverapp.activities.RunTimeTopUpActivity;

public class BackgroundWorker3 extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog alertDialog;
    public BackgroundWorker3(Context ctx)
    {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        String login_url = "http://coverapp.co.ke/mpesaAPI.php";
        if (type.equals("iceatravel"))
        {

            try {
                String totalCostOfTravelInsurance = params[1];
                String text_phone = params[2];


                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection .setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("totalCostOfTravelInsurance", "UTF-8")+"="+URLEncoder.encode(totalCostOfTravelInsurance, "UTF-8")+"&"

                        + URLEncoder.encode("text_phone", "UTF-8")+"="+URLEncoder.encode(text_phone, "UTF-8");

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
        alertDialog.setTitle("Travel Cover");
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
            alertDialog.setButton(Dialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //context.startActivity(new Intent(context, RunTimeTopUpActivity.class));
                }
            });

            alertDialog.setMessage("Process Successful, Await response shortly");
            alertDialog.show();
         //   IceaTravelCover iceaTravelCover = new IceaTravelCover();
          //  iceaTravelCover.closeProgressDialog();

        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
