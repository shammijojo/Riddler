package com.myapp.riddle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.myapp.riddle.config.Common;
import com.myapp.riddle.database.Database;
import com.myapp.riddle.database.Firebase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Leaderboard extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    List<leaderboard_user> leaderboardUsers;
    ListView listView;
    Database db;
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        leaderboardUsers =new ArrayList<>();
        db=new Database(this);
        firebase=new Firebase();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                leaderboardUsers.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    leaderboard_user leaderboard_user=new leaderboard_user();
                    leaderboard_user.setUsername(data.child("name").getValue().toString());
                    leaderboard_user.setScore(data.child("score").getValue().toString());
                    leaderboard_user.setImageId(Integer.parseInt(data.child("image").getValue().toString()));
                    leaderboardUsers.add(leaderboard_user);
                }


                Comparator<leaderboard_user> compareByScore=new Comparator<leaderboard_user>() {
                    @Override
                    public int compare(leaderboard_user u1, leaderboard_user u2) {
                        Integer s1=Integer.parseInt(u1.getScore());
                        Integer s2=Integer.parseInt(u2.getScore());

                        return s2.compareTo(s1);
                    }
                };

                Collections.sort(leaderboardUsers,compareByScore);

                LeaderboardList adapter=new LeaderboardList(Leaderboard.this, leaderboardUsers);
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
        return new Common().createAlertDialog(Leaderboard.this,item,db);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
