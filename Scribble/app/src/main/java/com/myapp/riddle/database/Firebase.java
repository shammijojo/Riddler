package com.myapp.riddle.database;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

import static android.content.ContentValues.TAG;

public class Firebase {

    FirebaseDatabase database;
    DatabaseReference myRef;

    public Firebase(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    public void createNewUser(String name, int imageid){
        String key=myRef.push().getKey();
        myRef.child(key).child("name").setValue(name);
        myRef.child(key).child("score").setValue("0");
        myRef.child(key).child("image").setValue(imageid);
        Log.i(TAG,"FIREBASE:New User Registered:"+name);
    }


    public void updateScore(final String name, final int score){

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    String username=data.child("name").getValue().toString();
                    if(username.equals(name)) {
                        data.child("score").getRef().setValue(score);
                        Log.i(TAG,"FIREBASE:score updatd for "+name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void updateName(final String name, final String newname){

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    String username=data.child("name").getValue().toString();
                    if(username.equals(name)) {
                        data.child("name").getRef().setValue(newname);
                        Log.i(TAG, "FIREBASE:Name updatd to " + newname + " from " + name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateImageId(final String name, final int imageid){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    String username=data.child("name").getValue().toString();
                    if(username.equals(name)) {
                        data.child("image").getRef().setValue(imageid);
                        Log.i(TAG,"FIREBASE:dp updatd for "+name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

