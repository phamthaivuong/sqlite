package com.example.phamthaivuong.demosqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.phamthaivuong.demosqlite.DatabaseHepler.Database;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddActivity extends AppCompatActivity {
    private String DATABASE_NAME = "database";

    final int RESQUEST_TAKE_PHOTO = 123;
    final int REQUESRT_CHOOSE_PHOTO = 321;

    ImageView imgHinh;
    Button btnChup, btnChon,btnLuu,btnQuayLai;
    EditText edtTen,edtSdt,editEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        init();
        addEvents();
    }

    private void init() {
        imgHinh = (ImageView)findViewById(R.id.imageViewHinh);
        btnChup = (Button)findViewById(R.id.buttonChup);
        btnChon = (Button)findViewById(R.id.buttonChonhinh);
        btnLuu = (Button)findViewById(R.id.buttonLuu);
        btnQuayLai = (Button)findViewById(R.id.buttonQuaylai);
        edtTen = (EditText)findViewById(R.id.editTextTen);
        edtSdt = (EditText)findViewById(R.id.editTextSdt);
        editEmail = (EditText)findViewById(R.id.editTextEmail);
    }
    private void addEvents() {
        btnChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
        btnChup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePickre();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancal();
            }
        });
    }

    private void cancal() {
        finish();
    }

    private void insert() {
        String ten = edtTen.getText().toString();
        String sdt = edtSdt.getText().toString();
        String email = editEmail.getText().toString();
        byte[] anh = getByteArrayFromImageView(imgHinh);

        ContentValues contentValues = new ContentValues();
        contentValues.put("Ten",ten);
        contentValues.put("SDT",sdt);
        contentValues.put("Email",email);
        contentValues.put("Anh",anh);

        SQLiteDatabase database = Database.initDatabase(this,DATABASE_NAME);
        database.insert("NhanVien",null,contentValues);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private byte[] getByteArrayFromImageView(ImageView imgv) {
        BitmapDrawable drawable = (BitmapDrawable)imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,stream );
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //Chup hinh
    private void takePickre(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,RESQUEST_TAKE_PHOTO);
    }

    //Chon Hình
    private void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,REQUESRT_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == REQUESRT_CHOOSE_PHOTO){ //Chon hinh
              try {
                  Uri imgUri = data.getData();
                  InputStream is = getContentResolver().openInputStream(imgUri);
                  Bitmap bitmap = BitmapFactory.decodeStream(is);
                  imgHinh.setImageBitmap(bitmap);
              } catch (FileNotFoundException e) {
                  e.printStackTrace();
              }
            }
            else if (requestCode == RESQUEST_TAKE_PHOTO){
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                imgHinh.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
