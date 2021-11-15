package com.myapp.riddle;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.riddle.Model.leaderboardUser;
import com.myapp.riddle.common.Common;
import com.myapp.riddle.config.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Leaderboard extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    List<leaderboardUser> leaderboardUsers;
    ListView listView;

    private final Activity CURRENT_ACTIVITY=Leaderboard.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        leaderboardUsers =new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                leaderboardUsers.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    leaderboardUser leaderboardUser =new leaderboardUser();
                    leaderboardUser.setUsername(data.child(Constants.NAME).getValue().toString());
                    leaderboardUser.setScore(data.child(Constants.SCORE).getValue().toString());
                    leaderboardUser.setImageId(Integer.parseInt(data.child(Constants.IMAGE).getValue().toString()));
                    leaderboardUsers.add(leaderboardUser);
                }

                Comparator<leaderboardUser> compareByScore=new Comparator<leaderboardUser>() {
                    @Override
                    public int compare(leaderboardUser u1, leaderboardUser u2) {
                        Integer s1=Integer.parseInt(u1.getScore());
                        Integer s2=Integer.parseInt(u2.getScore());
                        return s2.compareTo(s1);
                    }
                };

                Collections.sort(leaderboardUsers,compareByScore);

                LeaderboardList adapter=new LeaderboardList(Leaderboard.this, leaderboardUsers);
                listView=findViewById(R.id.leaderboardList);
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
        Common common=new Common(Leaderboard.this);
        return common.selectMenuItemOption(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
