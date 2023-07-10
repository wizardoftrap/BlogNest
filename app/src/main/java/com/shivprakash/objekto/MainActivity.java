package com.shivprakash.objekto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton add;
    FloatingActionButton signout;
    RecyclerView myRecyclerView;
    ArrayList<Blog> blogList;
    ArrayList<String> title, shortdscrpsn, Author, longdscrpsn, date;
    private DatabaseReference mDatabase;
    CustomAdaptor myCustomAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = findViewById(R.id.add);
        signout = findViewById(R.id.signout);
        myRecyclerView = findViewById(R.id.recyclerView);
        mDatabase = FirebaseDatabase.getInstance().getReference("Blogs");
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        blogList= new ArrayList<>();
        title = new ArrayList<>();
        shortdscrpsn = new ArrayList<>();
        longdscrpsn = new ArrayList<>();
        Author = new ArrayList<>();
        date = new ArrayList<>();
        myCustomAdapter = new CustomAdaptor(MainActivity.this, title, Author, shortdscrpsn, longdscrpsn, date);
        myRecyclerView.setAdapter(myCustomAdapter);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long dataSize = snapshot.getChildrenCount();
                List<DataSnapshot> children = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    children.add(childSnapshot);
                }
                for (int i = 0; i < dataSize; i++) {
                    title.add(children.get(i).child("title").getValue().toString());
                    Author.add(children.get(i).child("author").getValue().toString());
                    shortdscrpsn.add(children.get(i).child("shortDescription").getValue().toString());
                    longdscrpsn.add(children.get(i).child("longDescription").getValue().toString());
                    date.add(children.get(i).child("date").getValue().toString());
                }

                myCustomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, writeBlog.class);
                startActivity(intent);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
                Toast.makeText(MainActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}
