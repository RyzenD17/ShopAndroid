package com.example.shopandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ShopPage extends AppCompatActivity implements View.OnClickListener{


    Button btnCheckout,btnToDB;
    TextView basket;
    Toast toast;
    float TotalCost = 0;
    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_page);

        Bundle arg = getIntent().getExtras();
        boolean justUser = arg.getBoolean("justUser",false);

        btnCheckout = (Button) findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(this);

        btnToDB=(Button) findViewById(R.id.btnToDB);
        btnToDB.setOnClickListener(this);

        if(justUser)
        {
            btnToDB.setVisibility(View.INVISIBLE);
        }
        else
        {
            btnToDB.setOnClickListener(this);
        }
        basket = (TextView) findViewById(R.id.basket);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        updateTable();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnToDB:
                Intent intent2 = new Intent(this,MainActivity.class);
                startActivity(intent2);
                break;

            case R.id.btnCheckout:
                toast = Toast.makeText(getApplicationContext(),"Сумма заказа - "+ basket.getText(), Toast.LENGTH_LONG);
                toast.show();
                TotalCost=0;
                basket.setText(TotalCost+" Руб");
                break;

            default:
                int id = v.getId();
                Cursor cursorAdd = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                cursorAdd.moveToPosition(id-1);
                int id1 = cursorAdd.getColumnIndex(DBHelper.KEY_COST);
                TotalCost+=Float.valueOf(cursorAdd.getString(id1));
                basket.setText(TotalCost+" Руб");
                cursorAdd.close();
                updateTable();
                break;

        }

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

                Button AddBasketBtn = new Button(this);
                AddBasketBtn.setOnClickListener(this);
                params.weight = 1.0f;
                AddBasketBtn.setLayoutParams(params);
                AddBasketBtn.setText("Добавить");
                AddBasketBtn.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(AddBasketBtn);

                dbOutput.addView(dbOutputRow);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

}
