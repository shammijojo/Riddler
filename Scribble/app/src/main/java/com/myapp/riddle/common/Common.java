package com.myapp.riddle.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;

import com.myapp.riddle.Homepage;
import com.myapp.riddle.Leaderboard;
import com.myapp.riddle.config.Constants;
import com.myapp.riddle.dao.Database;
import com.myapp.riddle.dao.Firebase;

public class Common {


    private static Firebase firebase;
    private static Database database;

    private Activity activity;
    public Common(Activity activity) {
        this.activity = activity;
    }

    public Firebase getFirebaseObject(){
        if(firebase==null) {
            firebase=new Firebase();
        }
        return firebase;
    }

    public Database getDatabaseObject(Context context){
        if(database==null){
            database=new Database(context);
        }
        return database;
    }

    /**
     * Checks if the user has attempted all questions
     * @return true if all questions attempted
     */
    public boolean validateCompletion(){
        if(Integer.parseInt(new Database(activity).getDataFromUser(Constants.LEVEL))>Constants.TOTAL_QUE){
            activity.finish();
            Intent i = new Intent(activity, Leaderboard.class);
            activity.startActivity(i);
            return true;
        }
        return false;
    }

    /**
     * Manages side-menu options
     * @param item menu-item
     * @return
     */
    public boolean selectMenuItemOption(MenuItem item){
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
            alertDialogBuilder.setMessage(Constants.CONFIRM_RESTART);
            alertDialogBuilder.setCancelable(true);

            alertDialogBuilder.setPositiveButton(
                    Constants.YES,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            database.updateUserInfo(Constants.LEVEL,1);
                            database.updateUserInfo(Constants.SCORE,0);
                            firebase.updateScore(database.getDataFromUser(Constants.NAME),0);
                            Intent i=new Intent(activity,Homepage.class);
                            activity.startActivity(i);
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

        return true;
    }




}
