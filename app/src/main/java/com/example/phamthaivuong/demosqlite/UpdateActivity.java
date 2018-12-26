package com.example.phamthaivuong.demosqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

import com.example.phamthaivuong.demosqlite.DatabaseHepler.Database;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateActivity extends AppCompatActivity {

    private String DATABASE_NAME = "database";
    final int RESQUEST_TAKE_PHOTO = 123;
    final int RESQUERT_CHOOSE_PHOTO = 321;
    int id = -1;
    ImageView imgHinh;
    Button btnChup,btnChon,btnLuu,btnQuayLai;
    EditText edtTen,edtSdt,edtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        init();
        initUI();
        addEvents();

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
                TakePickre();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                Toast.makeText(UpdateActivity.this, "Cập nhật thành công ", Toast.LENGTH_SHORT).show();
            }
        });
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cancel();
            }
        });
    }

    private void Cancel() {
        finish();
    }

    private void update() {
        String ten = edtTen.getText().toString();
        String sdt = edtSdt.getText().toString();
        String email = edtEmail.getText().toString();
        byte[] anh = getByteArrayFromImageView(imgHinh);

        ContentValues contentValues = new ContentValues();
        contentValues.put("Ten",ten);
        contentValues.put("SDT",sdt);
        contentValues.put("Email",email);
        contentValues.put("Anh",anh);

        SQLiteDatabase database = Database.initDatabase(this,DATABASE_NAME);
        database.update("NhanVien",contentValues,"Id = ?",new String[]{id+""});
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private byte[] getByteArrayFromImageView(ImageView imgv) {
        BitmapDrawable drawable = (BitmapDrawable)imgHinh.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void init() {
        imgHinh = (ImageView)findViewById(R.id.imageViewHinh);
        btnChup = (Button)findViewById(R.id.buttonChup);
        btnChon = (Button)findViewById(R.id.buttonChonhinh);
        btnLuu = (Button)findViewById(R.id.buttonLuu);
        btnQuayLai = (Button)findViewById(R.id.buttonQuaylai);
        edtTen = (EditText)findViewById(R.id.editTextTen);
        edtSdt = (EditText)findViewById(R.id.editTextSdt);
        edtEmail = (EditText)findViewById(R.id.editTextEmail);
    }

    private void initUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("ID",-1);
        SQLiteDatabase database = Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM NhanVien WHERE Id = ?",new String[]{id + ""});
        cursor.moveToFirst();
        String ten = cursor.getString(1);
        String sdt = cursor.getString(2);
        String email = cursor.getString(3);
        byte[] anh = cursor.getBlob(4);

        Bitmap bitmap = BitmapFactory.decodeByteArray(anh,0,anh.length);
        imgHinh.setImageBitmap(bitmap);
        edtSdt.setText(sdt);
        edtTen.setText(ten);
        edtEmail.setText(email);
    }
    private void TakePickre(){ // chup hinh
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,RESQUEST_TAKE_PHOTO);
    }
    private void choosePhoto(){ //Chon hinh
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,RESQUERT_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == RESQUERT_CHOOSE_PHOTO){ //Chon hinh
                try
                {
                    Uri imgUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imgUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgHinh.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }else if (requestCode == RESQUEST_TAKE_PHOTO){
                Bitmap bitmap = (Bitmap)data.getExtras().get("Data");
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
