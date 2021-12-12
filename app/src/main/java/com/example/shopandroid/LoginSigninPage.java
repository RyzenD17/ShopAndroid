package com.example.shopandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginSigninPage extends AppCompatActivity implements View.OnClickListener{

    EditText Login, Password;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Button btnLogin, btnSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signin_page);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnSignin = (Button) findViewById(R.id.btnSignin);
        btnSignin.setOnClickListener(this);

        Login = (EditText) findViewById(R.id.login);
        Password = (EditText) findViewById(R.id.password);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnLogin:
                Cursor logCursor = database.query(DBHelper.TABLE_USERS,null,null,null,null,null,null);
                boolean logged=false;
                if(logCursor.moveToFirst()) {
                    int usernameId = logCursor.getColumnIndex(DBHelper.KEY_LOGIN);
                    int passwordId = logCursor.getColumnIndex(DBHelper.KEY_PASSWORD);
                    int roleId = logCursor.getColumnIndex(DBHelper.KEY_ROLE);

                    do {
                        if (Login.getText().toString().equals(logCursor.getString(usernameId)) && (Password.getText().toString().equals(logCursor.getString(passwordId)))) {
                            if(logCursor.getInt(roleId)==1)
                            {
                                startActivity(new Intent(this,MainActivity.class));
                            }
                            else
                            {
                                Intent userintent = new Intent(this,ShopPage.class);
                                userintent.putExtra("justUser",true);
                                startActivity(userintent);
                            }
                            logged=true;
                            break;
                        }
                    }while (logCursor.moveToNext());
                }
                logCursor.close();
                if(!logged) Toast.makeText(this,"Пользователь не зарегестрирован",Toast.LENGTH_LONG).show();
                break;

            case R.id.btnSignin:
                Cursor signCursor = database.query(DBHelper.TABLE_USERS,null,null,null,null,null,null);
                boolean finded=false;
                if(signCursor.moveToFirst()){
                    int usernameIndex=signCursor.getColumnIndex(DBHelper.KEY_LOGIN);
                    do{
                        if(Login.getText().toString().equals(signCursor.getString(usernameIndex))){
                            Toast.makeText(this,"Введённы вами логин уже зарегестрирован",Toast.LENGTH_LONG).show();
                            finded=true;
                            break;
                        }
                    }while(signCursor.moveToNext());
                }
                if(!finded){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_LOGIN,Login.getText().toString());
                    contentValues.put(DBHelper.KEY_PASSWORD,Password.getText().toString());
                    contentValues.put(DBHelper.KEY_ROLE,2);
                    database.insert(DBHelper.TABLE_USERS,null,contentValues);
                    Toast.makeText(this,"Вы успешно зарегестрировались!",Toast.LENGTH_LONG).show();
                }
                signCursor.close();
                break;
        }

    }
}