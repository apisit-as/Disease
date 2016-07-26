package disease.disease;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor c;
    int id;
    private ArrayAdapter adapter;
    private String check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        check  = getIntent().getExtras().getString("check");

        // Database
        db = this.openOrCreateDatabase("db_Disease",MODE_PRIVATE,null);
        String sql = ""
                + " CREATE TABLE IF NOT EXISTS db("
                + "   id INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + "   name VARCHAR," + " symptom VARCHAR,"
                + " regulation VARCHAR,"+ "   cause VARCHAR" + " )";
        db.execSQL(sql);
        bindData();
    }
    public void doAdd(View v){
        setContentView(R.layout.add);
    }
    public void doSave(View v){

        EditText txtName = (EditText) findViewById(R.id.editName);
        EditText txtSymptom = (EditText) findViewById(R.id.editSymptom);
        EditText txtRegulation = (EditText) findViewById(R.id.editRegulation);
        EditText txtCause = (EditText) findViewById(R.id.editCause);

        String name = txtName.getText().toString();
        String symptom = txtSymptom.getText().toString();
        String regulation = txtRegulation.getText().toString();
        String cause = txtCause.getText().toString();

        if(name.equals("") || symptom.equals("") || regulation.equals("")|| cause.equals("")){

        }else{
            String sql = "";
                sql = "INSERT INTO db VALUES(null, ':name', ':symptom', ':regulation', ':cause')";
                sql = sql.replace(":name",name);
                sql = sql.replace(":symptom",symptom);
                sql = sql.replace(":regulation",regulation);
                sql = sql.replace(":cause",cause);
                db.execSQL(sql);

            setContentView(R.layout.activity_main);
            bindData();
        }
    }
    private void bindData(){
        String sql = "SELECT * FROM db";
        c = db.rawQuery(sql, null);

        int item = android.R.layout.simple_list_item_1;
        ArrayList data = new ArrayList();

        while(c.moveToNext()){
            int index = c.getColumnIndex("name");
            data.add(c.getString(index));
        }

        adapter = new ArrayAdapter(this, item, data);

        ListView myList = (ListView) findViewById(R.id.listItem);
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int i, long l) {
                itemClick(i);
            }
        });
    }
    public void itemClick(int index){
          if(check.equals("1")){//ส่วนโชว์
              c.moveToPosition(index);
              id = c.getInt(c.getColumnIndex("id"));
              setContentView(R.layout.show_data);

              TextView name = (TextView)findViewById(R.id.textName);
              name.setText(c.getString(c.getColumnIndex("name")));
              TextView symptom = (TextView)findViewById(R.id.textSymptom);
              symptom.setText(c.getString(c.getColumnIndex("symptom")));
              TextView regulation = (TextView)findViewById(R.id.textRegulation);
              regulation.setText(c.getString(c.getColumnIndex("regulation")));
              TextView cause = (TextView)findViewById(R.id.textCause);
              cause.setText(c.getString(c.getColumnIndex("cause")));

          }else if(check.equals("2")){//ส่วน โชว์delete
              c.moveToPosition(index);
              id = c.getInt(c.getColumnIndex("id"));
              setContentView(R.layout.delete);

              TextView name = (TextView)findViewById(R.id.textShow);
              name.setText(c.getString(c.getColumnIndex("name")));
          }
    }
    public void doDelete(View v){
        db.delete("db", "id = " + id, null);
        doHome(v);
    }
    public void doHome(View v){
        setContentView(R.layout.activity_main);
        id = 0;
        bindData();
    }

    public void doBack(View v){
        setContentView(R.layout.show_list);
        bindData();
    }


}
