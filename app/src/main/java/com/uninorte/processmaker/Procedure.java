package com.uninorte.processmaker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Procedure extends ActionBarActivity {

    private static String url = "https://dynamicformapi.herokuapp.com/steps/by_procedure/";
    private ProgressDialog pDialog;
    private TextView mTextView1;
    private TextView mTextView2;
    private Button mButton;
    ArrayList<DataStep> listSteps;
    JSONArray steps = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedure);

        listSteps = new ArrayList<>();

        mTextView1 = (TextView)findViewById(R.id.title);
        mTextView2 = (TextView)findViewById(R.id.description);
        mButton = (Button)findViewById(R.id.button);

        DataProcedure dp = (DataProcedure)getIntent().getExtras().getSerializable("procedure");
        mTextView1.setText(dp.getName());
        mTextView2.setText(dp.getDescription());

        url = url + dp.getProcedure_id() + ".json";

        if (isNetworkAvailable())
            new GetData().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_procedure, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void requestData(View view) {

        if (isNetworkAvailable() && !listSteps.isEmpty()) {
            Intent intent = new Intent(Procedure.this, Steps.class);
            intent.putExtra("steps",listSteps);
            intent.putExtra("step","1");
            startActivity(intent);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isNetworkAvaible = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isNetworkAvaible = true;
            Toast.makeText(this, "Network is available ", Toast.LENGTH_LONG) .show();
        } else {
            Toast.makeText(this, "Network isn't available ", Toast.LENGTH_LONG) .show();
        }
        return isNetworkAvaible;
    }

    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Procedure.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            listSteps.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    steps = new JSONArray(jsonStr);

                    for (int i = 0; i < steps.length(); i++){
                        JSONObject j = steps.getJSONObject(i);

                        DataStep ds = new DataStep();
                        ds.setProcedure_id(j.getString("procedure_id"));
                        ds.setStep_number(j.getString("step_id"));
                        ds.setContent(/*j.getString("content")*/"something");
                        listSteps.add(ds);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
}
