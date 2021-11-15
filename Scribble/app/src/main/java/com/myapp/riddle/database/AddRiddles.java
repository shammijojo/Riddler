package com.myapp.riddle.database;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static android.content.ContentValues.TAG;

import com.myapp.riddle.config.Constants;

public class AddRiddles extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void readRiddles(Context context)
    {
        Database db=new Database(context);
        int i=1;
     try{
         String que="",ans="";
         InputStream is = context.getAssets().open(Constants.FILENAME);
         BufferedReader reader = new BufferedReader(new InputStreamReader(is));

         if (is!=null) {
             while ((que = reader.readLine()) != null) {
                 ans=reader.readLine();
                 db.insertRows(i,que,ans);
                 i++;
             }
         }
    }
        catch(Exception e) {
            Log.i(TAG,"Error while inserting data into riddles");
        }
    }

}
