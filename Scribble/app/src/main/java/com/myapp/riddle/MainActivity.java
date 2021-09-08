package com.myapp.riddle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.myapp.riddle.database.AddRiddles;
import com.myapp.riddle.database.Database;
import com.myapp.riddle.database.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton male,female,id1,id2,id3,id4;
    EditText name;
    static Button next;
    String gender;
    int picid;
    Database db;
    AddRiddles addRiddles;
    SQLiteDatabase sqLiteDatabase;
    Firebase firebase;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = new Intent();

        db=new Database(this);
        db.insertData();

        addRiddles=new AddRiddles();
        final Context context=getApplicationContext();

        firebase=new Firebase();

        name=findViewById(R.id.username);
        name.clearFocus();
        next=findViewById(R.id.save);
        name.setText(db.getFromDb("name"));

        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        id1 = findViewById(R.id.id1);
        id2 = findViewById(R.id.id2);
        id3 = findViewById(R.id.id3);
        id4 = findViewById(R.id.id4);

        picid=male.getId();

        id1.setOnClickListener(this);
        id2.setOnClickListener(this);
        id3.setOnClickListener(this);
        id4.setOnClickListener(this);
        male.setOnClickListener(this);
        female.setOnClickListener(this);


        if(!db.checkForNewUser())
        {
            selectImage();
            Intent i=new Intent(getApplicationContext(), Homepage.class);
            i.putExtra("id",male.getId());
            startActivity(i);
        }
        else{
            picid=male.getId();
            male.setAlpha(.5f);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.checkForNewUser()){
                    addRiddles.readriddles(context);
                    db.updateUserInfo("start",1);
                }

                validation();
                next.setEnabled(false);
                name.clearFocus();
            }
        });
    }


    @Override
    public void onClick(View v) {

        ImageButton[] imageButtons=new ImageButton[]{id1,id2,id3,id4,male,female};
        for(ImageButton ib:imageButtons)
        {
            if(ib.getId()==v.getId()) {
                ib.setAlpha(.5f);
                picid=ib.getId();
            }
            else
                ib.setAlpha(1f);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("Are you sure to exit?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public void validation(){


        if(TextUtils.isEmpty(name.getText().toString())){
            name.setError("Enter username");
            return;
        }

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean found=false;
               // Boolean newuser=true;
                String presentName=db.getFromDb("name");
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    String uname=data.child("name").getValue().toString();
                    if(uname.equals(name.getText().toString()) && !uname.equals(presentName)){
                        found=true;
                        break;
                    }
                }

                if(found==true){
                    Toast.makeText(getApplicationContext(),"Username not available.Try a different name",Toast.LENGTH_SHORT).show();
                    next.setEnabled(true);
                    return;
                }
                else{

                    if(presentName.length()==0)
                        firebase.createNewUser(name.getText().toString(),picid);
                    else {
                        firebase.updateName(presentName, name.getText().toString());
                        firebase.updateImageId(name.getText().toString(), picid);
                        firebase.updateScore("score",Integer.parseInt(db.getFromDb("score")));
                    }

                    db.updateUsername("name", name.getText().toString());
                    db.updateUserInfo("pic", picid);
                    Intent i=new Intent(getApplicationContext(), Homepage.class);
                    i.putExtra("id",picid);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void selectImage(){
        int id=Integer.parseInt(db.getFromDb("pic"));
        ImageView imageView=(ImageView)findViewById(id);
        imageView.setAlpha(.5f);
    }
}
