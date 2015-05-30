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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Steps extends ActionBarActivity {

    private TextView mTextView1;
    private TextView mTextView2;
    private LinearLayout mLinearLayout;
    private DataStep step;
    private String id;
    private ArrayList<DataStep> listSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        id = (String)getIntent().getExtras().getSerializable("step");
        listSteps = (ArrayList<DataStep>)getIntent().getExtras().getSerializable("steps");

        mTextView1 = (TextView)findViewById(R.id.title);
        mTextView2 = (TextView)findViewById(R.id.question);

        int i = 0;
        boolean sw = false;
        while (i < listSteps.size() && !sw){
            DataStep ds = listSteps.get(i);
            if (ds.getStep_number().equals(id)){
                step = ds;
                sw = true;
            }
            i++;
        }

        mTextView1.setText("Paso "+ step.getStep_number());
        mTextView2.setText(step.getContent());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_steps, menu);
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


}
