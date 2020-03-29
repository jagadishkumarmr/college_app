package com.example.whereismycab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity
{
    EditText e1_name,e2_USN,e4_email,e5_password,e6_phone;

    FirebaseAuth auth;
    ProgressDialog dialog;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Spinner DeptSpinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(SignUpActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Department));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DeptSpinner.setAdapter(myAdapter);

        e1_name = findViewById(R.id.editText1);
        e2_USN = findViewById(R.id.editText2);
        e4_email = findViewById(R.id.editText4);
        e5_password = findViewById(R.id.editText5);
        e6_phone = findViewById(R.id.editText6);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
    }


    public void signUpUser(View v)
    {
        dialog.setMessage("Registering, Please wait.. ");
        dialog.show();

        String name = e1_name.getText().toString();
        String USN = e2_USN.getText().toString();
        String email = e4_email.getText().toString();
        String password = e5_password.getText().toString();
        String phone= e6_phone.getText().toString();

        if(name.equals("") || USN.equals("") || email.equals("") || password.equals("") || phone.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Fields cannot be blank",Toast.LENGTH_SHORT).show();
        }
        else
        {
            auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {

                            if(task.isSuccessful())
                            {
                                dialog.hide();
                                Toast.makeText(getApplicationContext(),"User registered successfully",
                                        Toast.LENGTH_SHORT).show();


                                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                                Users user_object= new Users(e1_name.getText().toString(),e2_USN.getText().toString(),
                                        e4_email.getText().toString(),
                                        e5_password.getText().toString(),e6_phone.getText().toString());
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                databaseReference.child(firebaseUser.getUid()).setValue(user_object)
                                        .addOnCompleteListener(new OnCompleteListener<Void>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(getApplicationContext(),
                                                    "User data saved",Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(SignUpActivity.this,MainPageDrawerActivity.class);
                                                    startActivity(i);

                                                }
                                                else
                                                {
                                                    Toast.makeText(getApplicationContext(),
                                                    "User data could not saved",Toast.LENGTH_SHORT).show();

                                                }

                                            }
                                        });


                            }
                            else
                            {
                                dialog.hide();
                                Toast.makeText(getApplicationContext(),"User could not be registered",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }

    }

}
