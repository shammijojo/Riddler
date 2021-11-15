package com.myapp.riddle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myapp.riddle.Model.leaderboardUser;

import java.util.List;

public class LeaderboardList extends ArrayAdapter<leaderboardUser> {

    LayoutInflater inflater;
    ImageView image;

    public LeaderboardList(@NonNull Context context, List<leaderboardUser> list) {
        super(context, R.layout.leaderboard_score_layout,list);
       inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

    View view=inflater.inflate(R.layout.leaderboard_score_layout,null,true);
    TextView name=(TextView)view.findViewById(R.id.userName);
    TextView score=(TextView)view.findViewById(R.id.score);
    image=(ImageView)view.findViewById(R.id.profilePic);

    leaderboardUser leaderboardUser =getItem(position);
    name.setText(leaderboardUser.getUsername());
    score.setText(leaderboardUser.getScore());
    setImage(leaderboardUser.getImageId());

    return view;
    }


    /**
     * Sets the profile picture in ImageView
     * @param id Image-id of user read from Firebase
     */
    private void setImage(int id)
    {
        switch(id){
            case R.id.girl:
                image.setImageResource(R.drawable.girl);
                break;
            case R.id.joker:
                image.setImageResource(R.drawable.joker);
                break;
            case R.id.dog:
                image.setImageResource(R.drawable.dog);
                break;
            case R.id.frog:
                image.setImageResource(R.drawable.frog);
                break;
            case R.id.boy:
                image.setImageResource(R.drawable.boy);
                break;
            case R.id.elf:
                image.setImageResource(R.drawable.elf);
                break;
        }
    }


}
