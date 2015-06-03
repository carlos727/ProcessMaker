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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Procedures extends ActionBarActivity {

    private ProgressDialog pDialog;
    private static String url = "https://dynamicformapi.herokuapp.com/procedures/by_group/";
    private Button mButton;
    private DataProcedure procedure = null;
    private ListView mListView;
    public TextView mTextView;
    JSONArray procedures = null;
    ArrayList<DataProcedure> listProcedures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedures);

        DataCategory dc = (DataCategory)getIntent().getExtras().getSerializable("category");

        listProcedures = new ArrayList<>();
        mButton = (Button)findViewById(R.id.button);

        mListView = (ListView) findViewById(R.id.list);
        mListView.setFocusable(false);
        mListView.setFocusableInTouchMode(false);
        mListView.setClickable(false);

        mTextView = (TextView)findViewById(R.id.title);
        mTextView.setText(dc.getName() + "'s Procedures");

        url = url + dc.getGroup_id() + ".json";

        if (isNetworkAvailable())
            new GetData().execute();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {
                procedure = listProcedures.get(position);
                Intent intent = new Intent(Procedures.this,Procedure.class);
                intent.putExtra("procedure", procedure);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_procedures, menu);
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

    @Override
    public void onBackPressed() {

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

    public void requestData(View view) {
        if (isNetworkAvailable())
            new GetData().execute();
    }



    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Procedures.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            listProcedures.clear();
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
                    procedures = new JSONArray(jsonStr);

                    for (int i = 0; i < procedures.length(); i++){
                        JSONObject j = procedures.getJSONObject(i);

                        DataProcedure dp = new DataProcedure();
                        dp.setProcedure_id(j.getString("procedure_id"));
                        dp.setGroup_id(j.getString("group_id"));
                        dp.setName(j.getString("name"));
                        dp.setDescription(j.getString("description"));
                        listProcedures.add(dp);
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

            ArrayList<String> values = new ArrayList<>();
            for (int i = 0; i < listProcedures.size(); i++){
                DataProcedure dp = listProcedures.get(i);
                values.add(dp.getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Procedures.this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);

            mListView.setAdapter(adapter);
        }

    }
}
