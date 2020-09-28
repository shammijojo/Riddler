package com.myapp.riddle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myapp.riddle.database.Database;
import com.myapp.riddle.database.Firebase;

import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class riddle extends AppCompatActivity {

    pl.droidsonroids.gif.GifImageView gifImageView;
    ImageView correctimageview,pass;
    ImageButton next;
    View hintsview,passview;
    TextView que,score1,score2,score3,qno;
    Database db;
    String quetn,ans;
    int level,points=3;
    static int queno=0;
    int anslength;
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riddle);

        hintsview=findViewById(R.id.hintsview);
        passview=findViewById(R.id.passview);
        score1=findViewById(R.id.scorebox1);
        score2=findViewById(R.id.scorebox2);
        score3=findViewById(R.id.scorebox3);
        qno=findViewById(R.id.number);

        firebase=new Firebase();

        correctimageview=findViewById(R.id.next);
        correctimageview.setEnabled(false);
        correctimageview.setAlpha(.5f);
        que=findViewById(R.id.que);
        db=new Database(this);

        final int level=Integer.parseInt(db.getfromdb("level"));
        setque(level);

        String cscore=db.getfromdb("score");
        if(Integer.parseInt(cscore)<10)
        {
            score1.setText("0");
            score2.setText("0");
            score3.setText(cscore);
        }
        else if(Integer.parseInt(cscore)<100)
        {
            score1.setText("0");
            score2.setText(cscore.substring(0,1));
            score3.setText(cscore.substring(1,2));
        }
        else
        {
            score1.setText(cscore.substring(0,1));
            score2.setText(cscore.substring(1,2));
            score3.setText(cscore.substring(2,3));
        }






        correctimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentscore=db.getfromdb("score");
                points=Integer.parseInt(currentscore)+points;
                db.updateuserinfo("level",level+1);
                db.updateuserinfo("score",points);
                firebase.updatescore(db.getfromdb("name"),points);
                finish();
                Intent i=new Intent(getApplicationContext(),riddle.class);
                startActivity(i);
            }
        });



        hintsview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gethints();
                points=points-1;
                hintsview.setEnabled(false);
                hintsview.setAlpha(.5f);

            }
        });

        passview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                points=0;
                showanswer();
                passview.setAlpha(.5f);
                passview.setEnabled(false);
                hintsview.setEnabled(false);
                hintsview.setAlpha(.5f);
            }
        });

        addtextbox();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        super.onCreateOptionsMenu(m);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_layout,m);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println(item.getTitle());
        if(item.getTitle().toString().equals("Leaderboard")){
            Intent i=new Intent(getApplicationContext(),Leaderboard.class);
            startActivity(i);
        }
        else if(item.getTitle().toString().equals("Exit")){
            moveTaskToBack(true);
            System.exit(0);
        }

        return true;
    }


    public void gethints()
    {
        cleartextbox();
        EditText et;
        Set<Integer> set=new HashSet<>();
        int n=anslength/2;
        while(set.size()<n)
        {
            set.add(new Random().nextInt(anslength));
        }

        cleartextbox();
        for(int i:set)
        {
            et=(EditText)findViewById(1+i);
            et.setText(ans.substring(i,i+1));
            et.invalidate();
            et.setFocusable(false);
            et.setEnabled(false);
        }
    }


    public void nextfocus(int id)
    {
        EditText et;
        for(int i=0;i<anslength;i++)
        {
            et=(EditText)findViewById(i+1);
            if(et.isEnabled() && id!=i+1)
            {
                et.requestFocus();
                et.invalidate();
                return;
            }

        }
    }

    public void showanswer()
    {
        for(int i=0;i<anslength;i++)
        {
            EditText et=(EditText)findViewById(1+i);
            et.setText(ans.substring(i,i+1));
            et.setFocusable(false);
            et.invalidate();
        }

    }



    public void setque(int id)
    {
        Cursor cursor=db.getdata(id);
        que.setText(cursor.getString(1));
        qno.setText("#"+cursor.getString(0));
        ans=cursor.getString(2);
        anslength=cursor.getString(2).length();
    }

    public void cleartextbox()
    {
        for(int i=0;i<anslength;i++)
        {
            EditText et=findViewById(1+i);
            et.getText().clear();
            et.setEnabled(true);
            et.setBackgroundResource(R.drawable.rect);
            et.invalidate();
        }
    }

    @SuppressLint("ResourceType")
    public void addtextbox()
    {
        final String str=ans.toLowerCase();
        final RelativeLayout ll=findViewById(R.id.rl);
        int t=20;
        for(int i=0;i<anslength;i++) {
            final EditText et = new EditText(this);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            p.leftMargin=t;
            p.topMargin=50;
            et.setLayoutParams(p);
            et.setSingleLine(true);
            et.setCursorVisible(false);
            et.setId(1+i);
            et.setMaxLines(1);
            et.setWidth(100);
            et.setHeight(100);
            et.setPadding(40,0,0,0);
            et.setBackgroundResource(R.drawable.rect);
            if(i==0){
                et.requestFocus();
                et.setAlpha(.5f);}
            //et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

            et.setFilters(new InputFilter[]{
                    new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                            if(source.toString().matches("[a-zA-Z]+")){
                                source=source.subSequence(source.length()-1,source.length());
                                return source.toString().toLowerCase();
                            }
                            return "";
                        }
                    },new InputFilter.LengthFilter(1)

            });
            ll.addView(et, p);

            t=t+120;
            //et.setPadding(0,0,0,20);



            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus)
                        et.getText().clear();
                }
            });


            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus==true)
                        et.setAlpha(.5f);
                    else
                        et.setAlpha(1f);
                }
            });

            et.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    et.setSelection(et.getText().length());
                }
            });


            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String ans="";
                    int id=et.getId();
                   String c=s.toString();

                    for(int i=0;i<anslength;i++)
                    {
                        EditText text=findViewById(1+i);
                        ans=ans.concat(text.getText().toString());
                    }
                    System.out.println(ans+" "+str);
                    EditText next=findViewById(id+1);


                    if(s.length()>0)
                    if(s.charAt(0)==str.charAt(id-1))
                    {
                        et.setBackgroundResource(R.drawable.correct);
                        nextfocus(et.getId());
                        et.setEnabled(false);
                    }

                    if(ans.equals(str))
                    {
                        System.out.println(ans+" "+str);
                        correctimageview.setEnabled(true);
                        correctimageview.setAlpha(1f);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }
}
