package com.myapp.riddle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.riddle.config.Common;
import com.myapp.riddle.config.Constants;
import com.myapp.riddle.database.AddRiddles;
import com.myapp.riddle.database.Database;
import com.myapp.riddle.database.Firebase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton girl, joker, dog, frog, boy, elf;
    EditText name;
    static Button next;
    int picId;
    AddRiddles addRiddles;
    Database db;
    Firebase firebase;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private final Activity CURRENT_ACTIVITY=MainActivity.this;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Common common=new Common();
        db=common.getDatabaseObject(getApplicationContext());
        firebase=common.getFirebaseObject();

        db.insertData();

        addRiddles=new AddRiddles();
        name=findViewById(R.id.username);
        name.clearFocus();
        next=findViewById(R.id.save);
        name.setText(db.getFromDb(Constants.NAME));

        girl = findViewById(R.id.girl);
        joker = findViewById(R.id.joker);
        dog = findViewById(R.id.dog);
        frog = findViewById(R.id.frog);
        boy = findViewById(R.id.boy);
        elf = findViewById(R.id.elf);

        picId = girl.getId();

        dog.setOnClickListener(this);
        frog.setOnClickListener(this);
        boy.setOnClickListener(this);
        elf.setOnClickListener(this);
        girl.setOnClickListener(this);
        joker.setOnClickListener(this);


        if(!db.checkForNewUser())
        {
            selectImage();
            Intent i=new Intent(getApplicationContext(), Homepage.class);
            i.putExtra(Constants.ID, girl.getId());
            startActivity(i);
        }
        else{
            picId = girl.getId();
            girl.setAlpha(.5f);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.checkForNewUser()){
                    addRiddles.readRiddles(MainActivity.this);
                    db.updateUserInfo(Constants.START,1);
                }

                validation();
                next.setEnabled(false);
                name.clearFocus();
            }
        });
    }


    @Override
    public void onClick(View v) {

        ImageButton[] imageButtons=new ImageButton[]{dog, frog, boy, elf, girl, joker};
        for(ImageButton ib:imageButtons)
        {
            if(ib.getId()==v.getId()) {
                ib.setAlpha(.5f);
                picId =ib.getId();
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
                        firebase.createNewUser(name.getText().toString(), picId);
                    else {
                        firebase.updateName(presentName, name.getText().toString());
                        firebase.updateImageId(name.getText().toString(), picId);
                        firebase.updateScore(Constants.SCORE,Integer.parseInt(db.getFromDb(Constants.SCORE)));
                    }

                    db.updateUsername(Constants.NAME, name.getText().toString());
                    db.updateUserInfo(Constants.PIC, picId);
                    Intent i=new Intent(getApplicationContext(), Homepage.class);
                    i.putExtra(Constants.ID, picId);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void selectImage(){
        int id=Integer.parseInt(db.getFromDb(Constants.PIC));
        ImageView imageView=(ImageView)findViewById(id);
        imageView.setAlpha(.5f);
    }
}
