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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Categories extends ActionBarActivity {

    private ProgressDialog pDialog;
    private static String url = "https://dynamicformapi.herokuapp.com/groups.json";
    private Button mButton;
    private ListView mListView;
    JSONArray categories = null;
    ArrayList<DataCategory> listCategories;
    private DataCategory category = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        listCategories = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.list);
        mButton = (Button)findViewById(R.id.button);

        mListView.setFocusable(false);
        mListView.setFocusableInTouchMode(false);
        mListView.setClickable(false);

        if (isNetworkAvailable())
            new GetData().execute();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {
                category  = listCategories.get(position);
                Intent intent = new Intent(Categories.this,Procedures.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categories, menu);
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

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isNetworkAvaible = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isNetworkAvaible = true;
            Toast.makeText(this, "Network is available ", Toast.LENGTH_SHORT) .show();
        } else {
            Toast.makeText(this, "Network isn't available ", Toast.LENGTH_SHORT) .show();
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
            pDialog = new ProgressDialog(Categories.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            listCategories.clear();
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
                    categories = new JSONArray(jsonStr);

                    for (int i = 0; i < categories.length(); i++){
                        JSONObject j = categories.getJSONObject(i);

                        DataCategory dc = new DataCategory();
                        dc.setGroup_id(j.getString("group_id"));
                        dc.setName(j.getString("name"));
                        listCategories.add(dc);
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
            for (int i = 0; i < listCategories.size(); i++){
                DataCategory dc = listCategories.get(i);
                values.add(dc.getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Categories.this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);

            mListView.setAdapter(adapter);
        }

    }
}
