package com.shivprakash.objekto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class writeBlog extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Blog blog;
    ArrayList<Blog> arrayListBlog;
    private DatabaseReference mDatabase;
    private EditText titleEditText;
    private EditText shortDescEditText;
    private EditText longDescEditText;
    private Button uploadButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_blog);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        titleEditText = findViewById(R.id.titleB);
        shortDescEditText = findViewById(R.id.shortDesc);
        longDescEditText = findViewById(R.id.longDesc);
        uploadButton = findViewById(R.id.upload);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String shortDesc = shortDescEditText.getText().toString();
                String longDesc = longDescEditText.getText().toString();
                if(title.equals("")||shortDesc.equals("")||longDesc.equals("")){
                    Toast.makeText(writeBlog.this, "All fields are necessary", Toast.LENGTH_SHORT).show();
                }
                else{
                addToDatabase(title,shortDesc,longDesc);
                String message = "Title: " + title + "\nShort Description: " + shortDesc + "\nLong Description: " + longDesc;
                Toast.makeText(writeBlog.this, message, Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(writeBlog.this,MainActivity.class);
                startActivity(intent);
            }}
        });
    }
    public  void  addToDatabase(String Title, String shortDescription, String longDescription) {
        String userName=mAuth.getCurrentUser().getEmail();
        String date = new SimpleDateFormat("dd-MMM-yyyy KK:mm:ss aaa", Locale.getDefault()).format(new Date());
        blog=new Blog(Title,shortDescription,longDescription,date,userName);
        mDatabase.child("Blogs").child(Title).setValue(blog);
    }
}