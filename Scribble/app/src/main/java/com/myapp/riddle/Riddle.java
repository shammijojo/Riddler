package com.myapp.riddle;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.myapp.riddle.config.Common;
import com.myapp.riddle.config.Constants;
import com.myapp.riddle.database.Database;
import com.myapp.riddle.database.Firebase;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Riddle extends AppCompatActivity {

    ImageView next;
    View hintsView, passView;
    TextView que, scoreBox1, scoreBox2, scoreBox3,qno;
    String ans;
    int points=3;
    int ansLength;
    Database db;
    Firebase firebase;

    private final Activity CURRENT_ACTIVITY=Riddle.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riddle);

        hintsView = findViewById(R.id.hintsview);
        passView = findViewById(R.id.passview);
        scoreBox1 = findViewById(R.id.scoreBox1);
        scoreBox2 = findViewById(R.id.scoreBox2);
        scoreBox3 = findViewById(R.id.scoreBox3);
        qno = findViewById(R.id.queNumber);

        Common common=new Common();
        db=common.getDatabaseObject(getApplicationContext());
        firebase=common.getFirebaseObject();

        next =findViewById(R.id.next);
        next.setEnabled(false);
        next.setAlpha(.5f);
        que=findViewById(R.id.que);
        db=new Database(this);

        final int level=Integer.parseInt(db.getFromDb(Constants.LEVEL));
        setQue(level);

        String currentScore=db.getFromDb(Constants.SCORE);
        if(Integer.parseInt(currentScore)<10)
        {
            scoreBox1.setText("0");
            scoreBox2.setText("0");
            scoreBox3.setText(currentScore);
        }
        else if(Integer.parseInt(currentScore)<100)
        {
            scoreBox1.setText("0");
            scoreBox2.setText(currentScore.substring(0,1));
            scoreBox3.setText(currentScore.substring(1,2));
        }
        else
        {
            scoreBox1.setText(currentScore.substring(0,1));
            scoreBox2.setText(currentScore.substring(1,2));
            scoreBox3.setText(currentScore.substring(2,3));
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                String currentScore=db.getFromDb(Constants.SCORE);
                points=Integer.parseInt(currentScore)+points;
                db.updateUserInfo(Constants.LEVEL,level+1);
                db.updateUserInfo(Constants.SCORE,points);
                firebase.updateScore(db.getFromDb(Constants.NAME),points);
                finish();
                if(new Common().validateCompletion(Riddle.this))
                    return;
                else {
                    i = new Intent(getApplicationContext(), Riddle.class);
                    startActivity(i);
                }
            }
        });



        hintsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHints();
                points=points-1;
                hintsView.setEnabled(false);
                hintsView.setAlpha(.5f);

            }
        });

        passView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                points=0;
                showAnswer();
                passView.setAlpha(.5f);
                passView.setEnabled(false);
                hintsView.setEnabled(false);
                hintsView.setAlpha(.5f);
            }
        });

        addTextBox();

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
        return new Common().createAlertDialog(Riddle.this,item,db);
    }


    public void getHints() {
        clearTextBox();
        EditText et;
        Set<Integer> set=new HashSet<>();
        int n= ansLength /2;
        while(set.size()<n) {
            set.add(new Random().nextInt(ansLength));
        }

        clearTextBox();
        for(int i:set) {
            et=(EditText)findViewById(1+i);
            et.setText(ans.substring(i,i+1));
            et.invalidate();
            et.setFocusable(false);
            et.setEnabled(false);
        }
    }


    public void nextFocus(int id) {
        EditText editText;
        for(int i = 0; i< ansLength; i++)
        {
            editText=(EditText)findViewById(i+1);
            if(editText.isEnabled() && id!=i+1)
            {
                editText.requestFocus();
                editText.invalidate();
                return;
            }

        }
    }

    public void showAnswer() {
        for(int i = 0; i< ansLength; i++) {
            EditText et=(EditText)findViewById(1+i);
            et.setText(ans.substring(i,i+1));
            et.setFocusable(false);
            et.invalidate();
        }
    }



    public void setQue(int id) {
        Cursor cursor=db.getData(id);
        que.setText(cursor.getString(1));
        qno.setText("#"+cursor.getString(0));
        ans=cursor.getString(2);
        ansLength =cursor.getString(2).length();
    }

    public void clearTextBox()
    {
        for(int i = 0; i< ansLength; i++) {
            EditText et=findViewById(1+i);
            et.getText().clear();
            et.setEnabled(true);
            et.setBackgroundResource(R.drawable.rect);
            et.invalidate();
        }
    }

    @SuppressLint("ResourceType")
    public void addTextBox()
    {
        final String str=ans.toLowerCase();
        final RelativeLayout ll=findViewById(R.id.rl);
        int t=20;
        for(int i = 0; i< ansLength; i++) {
            final EditText editText = new EditText(this);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            p.leftMargin=t;
            p.topMargin=50;
            editText.setLayoutParams(p);
            editText.setSingleLine(true);
            editText.setCursorVisible(false);
            editText.setId(1+i);
            editText.setMaxLines(1);
            editText.setWidth(100);
            editText.setHeight(100);
            editText.setPadding(40,0,0,0);
            editText.setBackgroundResource(R.drawable.rect);
            if(i==0){
                editText.requestFocus();
                editText.setAlpha(.5f);
            }
            //et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

            editText.setFilters(new InputFilter[]{
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
            ll.addView(editText, p);

            t=t+120;
            //et.setPadding(0,0,0,20);

            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus)
                        editText.getText().clear();
                }
            });


            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus==true)
                        editText.setAlpha(.5f);
                    else
                        editText.setAlpha(1f);
                }
            });

            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setSelection(editText.getText().length());
                }
            });


            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String ans="";
                    int id=editText.getId();

                    for(int i = 0; i< ansLength; i++) {
                        EditText text=findViewById(1+i);
                        ans=ans.concat(text.getText().toString());
                    }


                    if(s.length()>0)
                    if(s.charAt(0)==str.charAt(id-1)) {
                        editText.setBackgroundResource(R.drawable.correct);
                        nextFocus(editText.getId());
                        editText.setEnabled(false);
                    }

                    if(ans.equals(str)) {
                        Riddle.this.next.setEnabled(true);
                        Riddle.this.next.setAlpha(1f);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }
}
