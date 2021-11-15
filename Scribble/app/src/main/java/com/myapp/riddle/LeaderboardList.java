package com.myapp.riddle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapp.riddle.Model.leaderboard_user;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LeaderboardList extends ArrayAdapter<leaderboard_user> {

    Context context;
    LayoutInflater inflater;
    ImageView image;

    public LeaderboardList(@NonNull Context context, List<leaderboard_user> list) {
        super(context, R.layout.leaderboard_score_layout,list);
       inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

    View view=inflater.inflate(R.layout.leaderboard_score_layout,null,true);
    TextView name=(TextView)view.findViewById(R.id.lbname);
    TextView score=(TextView)view.findViewById(R.id.score);
    image=(ImageView)view.findViewById(R.id.dp);

    leaderboard_user leaderboard_user=getItem(position);
    name.setText(leaderboard_user.getUsername());
    score.setText(leaderboard_user.getScore());
    setImage(leaderboard_user.getImageId());

    return view;
    }


    private void setImage(int id)
    {
        switch(id){
            case R.id.male:
                image.setImageResource(R.drawable.dp1);
                break;
            case R.id.female:
                image.setImageResource(R.drawable.dp6);
                break;
            case R.id.id1:
                image.setImageResource(R.drawable.dp2);
                break;
            case R.id.id2:
                image.setImageResource(R.drawable.dp3);
                break;
            case R.id.id3:
                image.setImageResource(R.drawable.dp4);
                break;
            case R.id.id4:
                image.setImageResource(R.drawable.dp5);
                break;
        }
    }


}
