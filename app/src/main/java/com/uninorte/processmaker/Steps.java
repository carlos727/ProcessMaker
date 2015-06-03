package com.uninorte.processmaker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.xml.datatype.Duration;

public class Steps extends ActionBarActivity {

    private TextView mTextView1;
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
        mLinearLayout = (LinearLayout)findViewById(R.id.content);

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
        String s = step.getContent();
        try {
            JSONObject content = new JSONObject(s);
            JSONArray fields = content.getJSONArray("Fields");
            JSONArray decisions = content.getJSONArray("Decisions");
            for (int j = 0; j < fields.length(); j++) {
                String caption = fields.getJSONObject(j).getString("caption");
                TextView question = new TextView(getApplicationContext());
                question.setText(caption);
                question.setGravity(Gravity.CENTER_HORIZONTAL);
                question.setTextSize(20);
                question.setPadding(5,70,5,15);

                mLinearLayout.addView(question);

                int type = Integer.parseInt(fields.getJSONObject(j).getString("field_type"));
                if (type == 0 || type == 1) {
                    JSONArray values = fields.getJSONObject(j).getJSONArray("possible_values");
                    String list_values[] = new String[values.length()];
                    //ArrayList<String> list_values = new ArrayList<>();
                    for (int k = 0; k < values.length(); k++){
                        list_values[k] = values.getString(k);
                    }

                    ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                            android.R.layout.simple_spinner_item, list_values);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    final Spinner answer = new Spinner(getApplicationContext());
                    answer.setAdapter(adapter);
                    answer.setBackgroundColor(getResources().getColor(R.color.blue_ligth));
                    answer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(getApplicationContext(), answer.getSelectedItem().toString() ,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            Toast.makeText(getApplicationContext(), answer.getSelectedItem().toString() ,Toast.LENGTH_SHORT).show();
                        }
                    });

                    mLinearLayout.addView(answer);
                }
                else {
                    if (type == 2){
                        EditText answer = new EditText(getApplicationContext());
                        answer.setGravity(Gravity.CENTER_HORIZONTAL);
                        answer.setInputType(InputType.TYPE_CLASS_NUMBER);
                        answer.setBackgroundColor(getResources().getColor(R.color.blue_ligth));
                        answer.setTextColor(getResources().getColor(R.color.white));
                        answer.setSelectAllOnFocus(true);
                        mLinearLayout.addView(answer);
                    }
                    else
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onBackPressed() {

    }
}
