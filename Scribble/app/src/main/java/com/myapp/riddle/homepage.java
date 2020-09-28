package com.myapp.riddle;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.myapp.riddle.database.Database;

public class homepage extends AppCompatActivity {

    ImageView imageview;
    Button b,changeb;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        imageview=(ImageView)findViewById(R.id.imageView2);
        b=(Button)findViewById(R.id.button);
        changeb=(Button)findViewById(R.id.changebutton);

        db=new Database(this);
        String id=db.getfromdb("pic");
        setImage(Integer.parseInt(id));

        if(Integer.parseInt(db.getfromdb("level"))==1)
            b.setText("START THE GAME");
        else{
            b.setText("CONTINUE THE GAME");
            b.setTextSize(15);}



        changeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.next.setEnabled(true);
                finish();
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),riddle.class);
                startActivity(i);
            }
        });
    }

    private void setImage(int id)
    {
        switch(id){
            case R.id.male:
                imageview.setImageResource(R.drawable.dp1);
                break;
            case R.id.female:
                imageview.setImageResource(R.drawable.dp6);
                break;
            case R.id.id1:
                imageview.setImageResource(R.drawable.dp2);
                break;
            case R.id.id2:
                imageview.setImageResource(R.drawable.dp3);
                break;
            case R.id.id3:
                imageview.setImageResource(R.drawable.dp4);
                break;
            case R.id.id4:
                imageview.setImageResource(R.drawable.dp5);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(homepage.this);
        builder1.setMessage("Are you sure to exit?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        moveTaskToBack(true);
                        System.exit(0);
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
}
