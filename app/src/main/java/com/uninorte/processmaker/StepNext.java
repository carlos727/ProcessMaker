package com.uninorte.processmaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StepNext extends ActionBarActivity {

    private TextView mTextView1;
    private LinearLayout mLinearLayout;
    private Button mButton;
    private DataStep step;
    private String id;
    private ArrayList<DataStep> listSteps;
    private ArrayList<DataField> listFields;
    private ArrayList<Branch> listBranchs;
    private ArrayList<DataBranch> listBranchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        id = (String)getIntent().getExtras().getSerializable("step");
        listSteps = (ArrayList<DataStep>)getIntent().getExtras().getSerializable("steps");
        listFields = new ArrayList<>();
        listBranchs = new ArrayList<>();
        listBranchInfo = new ArrayList<>();

        mTextView1 = (TextView)findViewById(R.id.title);
        mLinearLayout = (LinearLayout)findViewById(R.id.content);
        mButton = (Button)findViewById(R.id.button);

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

                String list_values[] = null;
                if (type == 0 || type == 1) {
                    JSONArray values = fields.getJSONObject(j).getJSONArray("possible_values");
                    list_values = new String[values.length()];
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
                            Toast.makeText(getApplicationContext(), answer.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
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
                        answer.setText("0");
                        answer.setGravity(Gravity.CENTER_HORIZONTAL);
                        answer.setInputType(InputType.TYPE_CLASS_NUMBER);
                        answer.setBackgroundColor(getResources().getColor(R.color.blue_ligth));
                        answer.setTextColor(getResources().getColor(R.color.white));
                        answer.setSelectAllOnFocus(true);
                        mLinearLayout.addView(answer);
                    }
                }

                DataField df = new DataField();
                df.setId(fields.getJSONObject(j).getString("id"));
                df.setCaption(caption);
                df.setType(type);
                df.setPossible_values(list_values);

                listFields.add(df);
            }

            for (int j = 0; j < decisions.length(); j++){
                String go_to = decisions.getJSONObject(j).getString("go_to_step");
                JSONArray branch = decisions.getJSONObject(j).getJSONArray("branch");

                Branch b = new Branch();
                b.setGo_to_step(go_to);
                b.setBranch(branch);
                listBranchs.add(b);
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

    public ArrayList<String> parameters (LinearLayout linearLayout){
        ArrayList<String> p = new ArrayList<>();
        int n = linearLayout.getChildCount();
        for (int i = 0; i < n; i++){
            if (linearLayout.getChildAt(i)instanceof Spinner){
                Spinner spinner = (Spinner)linearLayout.getChildAt(i);
                p.add(spinner.getSelectedItem().toString());
            }
            else {
                if (linearLayout.getChildAt(i)instanceof EditText){
                    EditText editText =(EditText)linearLayout.getChildAt(i);
                    p.add(editText.getText().toString());
                }
            }
        }
        return p;
    }

    public void nextStep(View view){

        if (listBranchs.size() == 1){
            Branch b = listBranchs.get(0);
            if (b.getBranch().length() > 0){
                ArrayList<String> parameters = parameters(mLinearLayout);

                JSONArray jsonArray = b.getBranch();
                for (int i = 0; i < jsonArray.length(); i++) {
                    DataBranch db = new DataBranch();

                    try {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        db.setField_id(jo.getInt("field_id"));
                        db.setType(jo.getString("comparison_type"));
                        db.setValue(jo.getString("value"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    listBranchInfo.add(db);
                }

                int i = 0;
                boolean sw = false;
                while (i < parameters.size() && !sw){
                    String parameter = parameters.get(i);

                    DataBranch db = listBranchInfo.get(i);
                    if (db.getType().equals("=")){
                        if (!db.getValue().equals(parameter)){
                            sw = true;
                        }
                    }
                    else {

                    }
                    i++;
                }

                if (!sw){
                    if (!b.getGo_to_step().equals("-1")) {
                        Intent intent = new Intent(StepNext.this, Steps.class);
                        intent.putExtra("steps", listSteps);
                        intent.putExtra("step", b.getGo_to_step());
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(StepNext.this, Congratulation.class);
                        startActivity(intent);
                    }
                }
                else {
                    Toast.makeText(this,"Don't have an asociated step",Toast.LENGTH_SHORT).show();
                }
            }
            else {
                if(b.getGo_to_step().equals("-1")){
                    Intent intent = new Intent(StepNext.this,Congratulation.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(StepNext.this,Steps.class);
                    intent.putExtra("steps",listSteps);
                    intent.putExtra("step",b.getGo_to_step());
                    startActivity(intent);
                }
            }
        }
        else {
            int i = 0;
            boolean sw = false;
            String step = "-1";
            ArrayList<String> parameters = parameters(mLinearLayout);
            while (i < listBranchs.size() && !sw){
                Branch b = listBranchs.get(i);

                JSONArray jsonArray = b.getBranch();
                for (int j = 0; j < jsonArray.length(); j++){
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        DataBranch db = new DataBranch();
                        db.setField_id(jsonObject.getInt("field_id"));
                        db.setType(jsonObject.getString("comparison_type"));
                        db.setValue(jsonObject.getString("value"));
                        listBranchInfo.add(db);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                int j = 0;
                boolean sv = false;
                while (j < parameters.size() && !sw){
                    String parameter = parameters.get(j);

                    DataBranch db = listBranchInfo.get(j);
                    if (db.getType().equals("=")){
                        if (!db.getValue().equals(parameter)){
                            sv = true;
                        }
                    }

                    i++;
                }

                if(!sv){
                    sw = true;
                    step = b.getGo_to_step();
                }
            }

            if (!sw){

            }
            else {
                Intent intent = new Intent(StepNext.this, Steps.class);
                intent.putExtra("steps",listSteps);
                intent.putExtra("step",step);
                startActivity(intent);
            }
        }
    }
}

