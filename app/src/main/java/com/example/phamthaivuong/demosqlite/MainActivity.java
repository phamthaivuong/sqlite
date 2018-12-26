package com.example.phamthaivuong.demosqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.phamthaivuong.demosqlite.Adapter.AdapterNhanVien;
import com.example.phamthaivuong.demosqlite.DatabaseHepler.CreateDatabase;
import com.example.phamthaivuong.demosqlite.DatabaseHepler.Database;
import com.example.phamthaivuong.demosqlite.Model.NhanVien;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String DATABASE_NAME = "database";
    SQLiteDatabase database;
    ListView myListView;
    ArrayList<NhanVien> arrayList;
    AdapterNhanVien adapterNhanVien;
    Button btnThem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CreateDatabase createDatabase = new CreateDatabase(this);
        createDatabase.open();
        init();
        readData();
    }

    private void init() {
        myListView = (ListView)findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapterNhanVien = new AdapterNhanVien(this,arrayList);
        myListView.setAdapter(adapterNhanVien);
        btnThem = (Button)findViewById(R.id.buttonThem);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });
    }
    private void readData() {
        database = Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM NhanVien",null);
        if (cursor!=null){
            arrayList.clear();
            for (int i = 0; i< cursor.getCount();i++){
                cursor.moveToPosition(i);
                int id = cursor.getInt(0);
                String ten = cursor.getString(1);
                String sdt = cursor.getString(2);
                String email = cursor.getString(3);
                byte[] anh = cursor.getBlob(4);
                arrayList.add(new NhanVien(id,ten,sdt,email,anh));
            }
            adapterNhanVien.notifyDataSetChanged();
        }
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce){
        super.onBackPressed();
        return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        },2000);
    }
}
