package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner languageSpinner;
    EditText userId, passId;
    TextView createAccnt;
    Button loginBtn;
    ProgressDialog pd;
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(android.R.color.black);
        setContentView(R.layout.activity_start);

        languageSpinner = findViewById(R.id.languageSpinner);
        userId = findViewById(R.id.userId);
        passId = findViewById(R.id.passId);
        loginBtn = findViewById(R.id.loginBtn);
        createAccnt = findViewById(R.id.createAccnt);

        auth = FirebaseAuth.getInstance();


        loginBtn.setOnClickListener(this);
        createAccnt.setOnClickListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languageList, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);



    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBtn:
                login();
                break;
            case R.id.createAccnt:
                startActivity(new Intent(getApplicationContext(), CreateAccount.class));
                finish();
                break;

        }
    }

    private void login(){
        String email = userId.getText().toString();
        String pass = passId.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            Toast.makeText(StartActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
        }else{
            pd = new ProgressDialog(StartActivity.this);
            pd.setMessage("Please wait...");
            pd.show();
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(StartActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                pd.dismiss();
                                startActivity(new Intent(StartActivity.this, HomeActivity.class));
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                pd.dismiss();
                            }
                        });
                    }else{
                        pd.dismiss();
                        Toast.makeText(StartActivity.this, "Error! Go Home", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}