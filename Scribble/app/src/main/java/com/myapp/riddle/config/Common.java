package com.myapp.riddle.config;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;

import com.myapp.riddle.Homepage;
import com.myapp.riddle.Leaderboard;
import com.myapp.riddle.database.Database;
import com.myapp.riddle.database.Firebase;

public class Common {



    public boolean validateCompletion(Activity activity){
        if(Integer.parseInt(new Database(activity).getFromDb("level"))>Constants.TOTAL_QUE){
            activity.finish();
            Intent i = new Intent(activity, Leaderboard.class);
            activity.startActivity(i);
            return true;
        }
        return false;
    }

    public boolean createAlertDialog(final Activity activity, MenuItem item, final Database db){
        final Firebase firebase=new Firebase();
        if(item.getTitle().toString().equals(Constants.LEADERBOARD)){
            Intent i=new Intent(activity,Leaderboard.class);
            if(!activity.getClass().equals(Leaderboard.class))
            activity.startActivity(i);
        }
        else if(item.getTitle().toString().equals(Constants.EXIT)){
            activity.moveTaskToBack(true);
            System.exit(0);
        }
        else if(item.getTitle().toString().equals(Constants.RESTART_GAME)){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setMessage("Are you sure to restart?");
            alertDialogBuilder.setCancelable(true);

            alertDialogBuilder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            db.updateUserInfo(Constants.LEVEL,1);
                            db.updateUserInfo(Constants.SCORE,0);
                            firebase.updateScore(db.getFromDb("name"),0);
                            Intent i=new Intent(activity,Homepage.class);
                            activity.startActivity(i);
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

        return true;

    }


}
