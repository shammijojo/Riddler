package com.myapp.riddle;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.myapp.riddle.config.Common;
import com.myapp.riddle.config.Constants;
import com.myapp.riddle.database.Database;

public class Homepage extends AppCompatActivity {

    ImageView avatarImage;
    Button startGameButton, changeAvatarButton;
    Database db;

    private final Activity CURRENT_ACTIVITY=Homepage.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        avatarImage =(ImageView)findViewById(R.id.imageView2);
        startGameButton =(Button)findViewById(R.id.button);
        changeAvatarButton =(Button)findViewById(R.id.changebutton);

        db=new Database(this);
        String picId=db.getFromDb(Constants.PIC);
        setImage(Integer.parseInt(picId));

        if(Integer.parseInt(db.getFromDb(Constants.LEVEL))==1)
            startGameButton.setText(Constants.START_THE_GAME);
        else{
            startGameButton.setText(Constants.CONTINUE_THE_GAME);
            startGameButton.setTextSize(15);}


        changeAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.next.setEnabled(true);
                finish();
            }
        });

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new Common().validateCompletion(CURRENT_ACTIVITY))
                    return;
                Intent i=new Intent(getApplicationContext(), Constants.RIDDLE_CLASS);
                startActivity(i);
            }
        });
    }

    private void setImage(int id)
    {
        switch(id){
            case R.id.male:
                avatarImage.setImageResource(R.drawable.dp1);
                break;
            case R.id.female:
                avatarImage.setImageResource(R.drawable.dp6);
                break;
            case R.id.id1:
                avatarImage.setImageResource(R.drawable.dp2);
                break;
            case R.id.id2:
                avatarImage.setImageResource(R.drawable.dp3);
                break;
            case R.id.id3:
                avatarImage.setImageResource(R.drawable.dp4);
                break;
            case R.id.id4:
                avatarImage.setImageResource(R.drawable.dp5);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Homepage.this);
        alertDialogBuilder.setMessage("Are you sure to exit?");
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        moveTaskToBack(true);
                        System.exit(0);
                    }
                });

        alertDialogBuilder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
