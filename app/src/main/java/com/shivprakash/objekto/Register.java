package com.shivprakash.objekto;


import android.content.ContentValues;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class Register extends AppCompatActivity {
    Button register,toLogin;
    EditText email,dob,name;
    User user;
    RequestQueue requestQueueE;
    String status="error";
    String emlR,passR,dobR,nameR;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = findViewById(R.id.register);
        toLogin = findViewById(R.id.toLogin);
        email= findViewById(R.id.email);
        name=findViewById(R.id.name);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        dob=findViewById(R.id.dob);
        requestQueueE = Volley.newRequestQueue(Register.this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emlR = email.getText().toString();
                dobR = dob.getText().toString();
                passR ="vjdhwgdjqwvhjdvhfit76274ted748fgb";
                nameR = name.getText().toString();

                if(emlR.equals("")||dobR.equals("")||nameR.equals("") ){
                    Toast.makeText(Register.this, "All fields are necessary to register", Toast.LENGTH_SHORT).show();
                }
                else{
                    emailValidation(emlR);
                    if (status.equals("valid")) {
                        //Signup
                        mAuth.createUserWithEmailAndPassword(emlR, passR).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mAuth.sendPasswordResetEmail(emlR).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful())
                                            {
                                                // if isSuccessful then done message will be shown
                                                // and you can change the password
                                            }
                                            else {
                                                Toast.makeText(Register.this,cutter(task.getException().toString()),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(Register.this,"Error:Failed",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    Toast.makeText(Register.this, "Please reset your password", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Register.this, Login.class);
                                    startActivity(intent);
                                    //database entry
                                    addToDatabase(nameR,emlR,dobR);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, cutter(task.getException().toString()), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(Register.this, "Invalid email Please try again", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.setText("");
                name.setText("");
                dob.setText("");
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });
    }
    public String cutter(String exception){
        char [] error = exception.toCharArray();
        int j = 0;
        for (int i = 0; i <exception.length(); i++) {
            if(error[i]==':'){
                j=i;
                break;
            }
        }
        return exception.substring(j+1);
    }
    public String emailUser(String email){
        char [] chars =email.toCharArray();
        int j = 0;
        for (int i = 0; i <email.length(); i++) {
            if(chars[i]=='@'){
                j=i;
                break;
            }
        }
        return email.substring(0,j);
    }
    public String emailDomain(String email){
        char [] chars =email.toCharArray();
        int j = 0;
        for (int i = 0; i <email.length(); i++) {
            if(chars[i]=='@'){
                j=i;
                break;
            }
        }
        return email.substring(j+1);
    }
    public void emailValidation(String email){
        String userE=emailUser(email);
        String domainE=emailDomain(email);
        String url= "https://validect-email-verification-v1.p.rapidapi.com/v1/verify?email="+userE+"%40"+domainE;
        StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    status =jsonObject.getString("status");
                    // Toast.makeText(Register.this, "api called", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  headers = new HashMap<String, String>();
                headers.put("X-RapidAPI-Key", "913e4c036amshda7a70cc4ce01f4p153409jsn4e199e82472d");
                headers.put("X-RapidAPI-Host", "validect-email-verification-v1.p.rapidapi.com");
                return headers;
            }
        };
        requestQueueE.add(request);
    }
    public  void  addToDatabase(String name, String email, String dob) {
        user = new User(name,email,dob);
        mDatabase.child("users").child(name).setValue(user);
    }
}