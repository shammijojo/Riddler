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
            case R.id.girl:
                avatarImage.setImageResource(R.drawable.girl);
                break;
            case R.id.joker:
                avatarImage.setImageResource(R.drawable.joker);
                break;
            case R.id.dog:
                avatarImage.setImageResource(R.drawable.dog);
                break;
            case R.id.frog:
                avatarImage.setImageResource(R.drawable.frog);
                break;
            case R.id.boy:
                avatarImage.setImageResource(R.drawable.boy);
                break;
            case R.id.elf:
                avatarImage.setImageResource(R.drawable.elf);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Homepage.this);
        alertDialogBuilder.setMessage(Constants.CONFIRM_EXIT);
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton(
                Constants.YES,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        moveTaskToBack(true);
                        System.exit(0);
                    }
                });

        alertDialogBuilder.setNegativeButton(
                Constants.NO,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
