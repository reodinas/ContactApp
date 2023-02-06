package com.reodinas2.contactapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.reodinas2.contactapp.adapter.ContactAdapter;
import com.reodinas2.contactapp.data.DatabaseHandler;
import com.reodinas2.contactapp.model.Contact;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;

    // 리사이클러 뷰를 사용할 때!
    // RecyclerView, Adapter, ArrayList를 쌍으로 적어라!
    RecyclerView recyclerView;
    ContactAdapter adapter;
    ArrayList<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);

        // 리사이클러 뷰를 화면에 연결하고,
        // 쌍으로 같이 다니는 코드도 작성
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        // 버튼을 누르면 연락처 추가 액티비티로 이동
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        // DB로부터 데이터를 가져와서,
        // 리사이클러 뷰에 표시하자.
        DatabaseHandler db = new DatabaseHandler(MainActivity.this);
        contactList = db.getAllContacts();
        db.close();

        // 어댑터를 만든다.
        adapter = new ContactAdapter(MainActivity.this, contactList);
        // 어댑터를 리사이클러 뷰에 셋팅!!!
        recyclerView.setAdapter(adapter);
    }
}