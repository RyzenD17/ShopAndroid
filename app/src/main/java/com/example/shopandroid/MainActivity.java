package com.example.shopandroid;


import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnClear,btnToDB,btnToStore;
    EditText name, cost;


    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        btnToDB=(Button) findViewById(R.id.btnToDB);
        btnToDB.setOnClickListener(this);

        btnToStore=(Button) findViewById(R.id.btnToStore);
        btnToStore.setOnClickListener(this);



        name = (EditText) findViewById(R.id.name);
        cost = (EditText) findViewById(R.id.cost);


        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        updateTable();
    }


    public void updateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int costIndex = cursor.getColumnIndex(DBHelper.KEY_COST);

            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();
            do {
                TableRow dbOutputRow = new TableRow(this);

                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight = 1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                dbOutputRow.addView(outputID);

                TextView outputName = new TextView(this);
                params.weight = 3.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(nameIndex));
                dbOutputRow.addView(outputName);

                TextView outputCost = new TextView(this);
                params.weight = 3.0f;
                outputCost.setLayoutParams(params);
                outputCost.setText(cursor.getString(costIndex));
                dbOutputRow.addView(outputCost);

                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight = 1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("??????????????");
                deleteBtn.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(deleteBtn);

                dbOutput.addView(dbOutputRow);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnAdd:
                String sname = name.getText().toString();
                String scost = cost.getText().toString();

                contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_NAME, sname);
                contentValues.put(DBHelper.KEY_COST, scost);

                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                updateTable();
                name.setText("");
                cost.setText("");
                break;

            case R.id.btnClear:
                database.delete(DBHelper.TABLE_CONTACTS, null, null);
                TableLayout dbOutput = findViewById(R.id.dbOutput);
                dbOutput.removeAllViews();
                updateTable();
                break;

            case R.id.btnToStore:
                Intent intent1 =new Intent(this,ShopPage.class);
                intent1.putExtra("justUser",false);
                startActivity(intent1);

                break;



            default:
                Button button1 = (Button) v;
                if (button1.getText() == "??????????????") {
                    View outputDBRow = (View) v.getParent();
                    ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                    outputDB.removeView(outputDBRow);
                    outputDB.invalidate();

                    database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + " = ?", new String[]{String.valueOf((v.getId()))});

                    Cursor cursorUpdater = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                    if (cursorUpdater.moveToFirst()) {
                        int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID);
                        int nameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_NAME);
                        int costIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_COST);
                        int realID = 1;
                        do {
                            if (cursorUpdater.getInt(idIndex) > realID) {
                                contentValues.put(DBHelper.KEY_ID, realID);
                                contentValues.put(DBHelper.KEY_NAME, cursorUpdater.getString(nameIndex));
                                contentValues.put(DBHelper.KEY_COST, cursorUpdater.getString(costIndex));
                                database.replace(DBHelper.TABLE_CONTACTS, null, contentValues);
                            }
                            realID++;

                        } while (cursorUpdater.moveToNext());
                        if (cursorUpdater.moveToLast() && v.getId() != realID) {
                            database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                        }
                        updateTable();
                    }
                }

        }
    }
}
