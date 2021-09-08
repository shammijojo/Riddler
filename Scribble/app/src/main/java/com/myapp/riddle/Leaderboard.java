package com.myapp.riddle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import com.myapp.riddle.Model.leaderboard_user;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Leaderboard extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;

    List<leaderboard_user> list;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        list=new ArrayList<>();


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
    public void onBackPressed() {
        finish();
    }
}
