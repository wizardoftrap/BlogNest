package com.shivprakash.objekto;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    EditText emailL,passwordL;
    String emlL,passL;
    Button login,toRegister,reset;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailL= findViewById(R.id.loginUsername);
        passwordL=findViewById(R.id.loginPassword);
        login = findViewById(R.id.login);
        reset=findViewById(R.id.resetPassword);
        toRegister = findViewById(R.id.register);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailL.setText("");
                passwordL.setText("");
                Intent intent = new Intent(Login.this,Register.class);
                // Toast.makeText(Login.this, "Logged in", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emlL=emailL.getText().toString();
                passL=passwordL.getText().toString();
                emailL.setText("");
                passwordL.setText("");
                if(emlL.equals("")&&passL.equals("")){
                    Toast.makeText(Login.this, "Enter your email and password to login", Toast.LENGTH_SHORT).show();
                }
                else if(emlL.equals("")){
                    Toast.makeText(Login.this, "Enter your email to log in", Toast.LENGTH_SHORT).show();
                }
                else if(passL.equals("")){
                    Toast.makeText(Login.this, "Enter your password to login", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.signInWithEmailAndPassword(emlL, passL)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        //  Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        //  updateUI(user);
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        Toast.makeText(Login.this, "Logged In", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        // Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(Login.this, cutter(task.getException().toString()), Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }
                                }
                            });
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emlL=emailL.getText().toString();
                emailL.setText("");
                passwordL.setText("");
                if(emlL.equals("")){
                    Toast.makeText(Login.this, "Enter your email to reset password", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.sendPasswordResetEmail(emlL).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                // if isSuccessful then done message will be shown
                                // and you can change the password
                                Toast.makeText(Login.this,"Reset password link sent to your email",Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(Login.this,cutter(task.getException().toString()),Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(Login.this,"Error:Failed",Toast.LENGTH_LONG).show();
                        }
                    });
                }
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
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(Login.this,MainActivity.class);
            startActivity(intent);
        }
    }
}