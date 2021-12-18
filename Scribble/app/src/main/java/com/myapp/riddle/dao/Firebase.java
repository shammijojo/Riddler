package com.myapp.riddle.dao;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.riddle.config.Constants;

import androidx.annotation.NonNull;
import static android.content.ContentValues.TAG;

/**
 * Read/Write operations from/to Firebase
 */
public class Firebase {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public Firebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void createNewUser(String name, int imageid){
        String key= databaseReference.push().getKey();
        databaseReference.child(key).child(Constants.NAME).setValue(name);
        databaseReference.child(key).child(Constants.SCORE).setValue("0");
        databaseReference.child(key).child(Constants.IMAGE).setValue(imageid);
       // Log.i(TAG,"FIREBASE:New User Registered:"+name);
    }


    public void updateScore(final String name, final int score){

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    String username=data.child(Constants.NAME).getValue().toString();
                    if(username.equals(name)) {
                        data.child(Constants.SCORE).getRef().setValue(score);
                       // Log.i(TAG,"FIREBASE:Score updated for "+name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void updateName(final String name, final String newName){

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    String username=data.child(Constants.NAME).getValue().toString();
                    if(username.equals(name)) {
                        data.child(Constants.NAME).getRef().setValue(newName);
                       // Log.i(TAG, "FIREBASE:Name updated to " + newName + " from " + name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateImageId(final String name, final int imageId){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    String username=data.child(Constants.NAME).getValue().toString();
                    if(username.equals(name)) {
                        data.child(Constants.IMAGE).getRef().setValue(imageId);
                       // Log.i(TAG,"FIREBASE:Profile pic updated for "+name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

