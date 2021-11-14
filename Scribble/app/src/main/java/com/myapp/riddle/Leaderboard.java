package com.myapp.riddle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import com.myapp.riddle.Model.leaderboard_user;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.riddle.database.Database;
import com.myapp.riddle.database.Firebase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Leaderboard extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;

    List<leaderboard_user> list;
    ListView listView;
    Database db;
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        list=new ArrayList<>();
        db=new Database(this);
        firebase=new Firebase();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    leaderboard_user leaderboard_user=new leaderboard_user();
                    leaderboard_user.setUsername(data.child("name").getValue().toString());
                    leaderboard_user.setScore(data.child("score").getValue().toString());
                    leaderboard_user.setImageid(Integer.parseInt(data.child("image").getValue().toString()));
                    list.add(leaderboard_user);
                }


                Comparator<leaderboard_user> comparebyscore=new Comparator<leaderboard_user>() {
                    @Override
                    public int compare(leaderboard_user u1, leaderboard_user u2) {
                        Integer s1=Integer.parseInt(u1.getScore());
                        Integer s2=Integer.parseInt(u2.getScore());

                        return s2.compareTo(s1);
                    }
                };

                Collections.sort(list,comparebyscore);

                LeaderboardList adapter=new LeaderboardList(Leaderboard.this,list);
                listView=findViewById(R.id.list);
                listView.setAdapter(adapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        super.onCreateOptionsMenu(m);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_layout,m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println(item.getTitle());
        if(item.getTitle().toString().equals("Leaderboard")){
            Intent i=new Intent(getApplicationContext(),Leaderboard.class);
            startActivity(i);
        }
        else if(item.getTitle().toString().equals("Exit")){
            moveTaskToBack(true);
            System.exit(0);
        }
        else if(item.getTitle().toString().equals("Restart Game")){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Leaderboard.this);
            builder1.setMessage("Are you sure to restart?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            db.updateUserInfo("level",1);
                            db.updateUserInfo("score",0);
                            firebase.updateScore(db.getFromDb("name"),0);
                            Intent i=new Intent(getApplicationContext(),Homepage.class);
                            startActivity(i);
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

        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
