package com.example.librarymanage.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.MainActivity;
import com.example.librarymanage.R;
import com.example.librarymanage.data.DataBook;

public class dangnhap extends AppCompatActivity {

    private SQLiteDatabase db;
    EditText edtUser, edtPass;
    Button btnLogin;
    TextView txtReg;
    DataBook dbBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layoutdangnhap);
        //Khởi tạo CSDL
        dbBook = new DataBook(this);
        dbBook.getWritableDatabase();
        init();
        initListener();
        txtReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dangnhap.this, dangki.class);
                startActivity(intent);
            }
        });
    }
    private void init(){
        edtUser = findViewById(R.id.edt_username);
        edtPass = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btnLogin);
        txtReg = findViewById(R.id.txtsignup);
    }
    private int isUser(String user, String pass){
        try{
            DataBook helper = new DataBook(this);
            db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from User where username=? and password=?", new String[]{user,pass});
            if(cursor.moveToFirst()){
                return cursor.getInt(0);
            }
        }catch (Exception ex){
            Toast.makeText(this, "Lỗi hệ thống ", Toast.LENGTH_LONG).show();
        }
        return -1;
    }


    private void initListener(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUser.getText().toString();
                String pass = edtPass.getText().toString();
                if(username.equals("")|| pass.equals(""))
                    Toast.makeText(dangnhap.this,"Cần điền đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
                else{
                    int user_id = isUser(username,pass);
                    if(user_id != -1) {
                        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("user_id",user_id);
                        editor.apply();

                        Toast.makeText(getApplication(), "Mật khẩu hợp lệ", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(dangnhap.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(getApplication(), "Mật khẩu không hợp lệ", Toast.LENGTH_LONG).show();
                        edtUser.requestFocus();
                    }
                }
            }
        });
    }
}