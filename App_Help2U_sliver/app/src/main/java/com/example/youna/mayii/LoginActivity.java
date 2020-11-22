package com.example.youna.mayii;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    private Button button5;
    private EditText p_phoneNumber;
    private EditText codeNumber;
    private int dataCnt;
    private DatabaseReference testFirebase;
    private int i;
    public List emailList;
    public List nickList;
    public List codeList;
    public List heartData;
    private String[] tempData;
    private String[] splitData;
    private double average;
    private  int sum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailList=new ArrayList();
        nickList=new ArrayList();
        codeList=new ArrayList();
        heartData=new ArrayList();
        tempData=new String[500];
        splitData=new String[500];
        average=0;
        p_phoneNumber = (EditText)findViewById(R.id.p_phoneNumber);
        codeNumber = (EditText)findViewById(R.id.editText);
        i=0;
        sum=0;
        testFirebase = FirebaseDatabase.getInstance().getReference();
        dataCnt=0;
        copyFirebase();
        button5 = (Button)findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCode(codeNumber.getText().toString());

            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
        alert_ex.setMessage("헬프투유를 종료하시겠습니까?");

        alert_ex.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alert_ex.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alert = alert_ex.create();
        alert.show();
    }
    public void copyFirebase()
    {
        emailList.clear();
        codeList.clear();
        sum=0;
        testFirebase.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("@@@@@@@@", "!!!");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.child("이메일").getValue()==null)
                        break;
                    emailList.add(snapshot.child("이메일").getValue().toString());
                    if(snapshot.child("닉네임").getValue()==null)
                        break;
                    nickList.add(snapshot.child("닉네임").getValue().toString());
                    if(snapshot.child("회원 코드").getValue()==null)
                        break;
                    codeList.add(snapshot.child("회원 코드").getValue().toString());

                    Iterator j=snapshot.child("심장박동 데이터").getChildren().iterator();
                    while(j.hasNext())
                    {
                        heartData.add(j.next().toString());
                        tempData= heartData.get(i).toString().split(",");
                        splitData=tempData[1].split("=");
                        Log.d("eeeeff",splitData[1]);
                        String temp=splitData[1];
                        String[] ex=temp.split("\r");
                        String[] test3=ex[0].split(" ");
                        int numb=Integer.parseInt(test3[1]);
                       // sum=sum+numb;
                        Log.d("eeee!!",Integer.toString(sum));
                        Log.d("eee",heartData.get(i).toString());

                        i++;
                    }
                 //   average=sum/(i-1);
                    average=100;
                    Log.d("eee@@",Double.toString(average));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void checkCode(final String abc){
         i=0;

         Log.d("ddddd",codeList.get(i).toString());
         while(codeList.size()!=i)
         {
             if(codeList.get(i).toString().compareTo(abc)==0)
             {

                 String tempAverage=Double.toString(average);
                 Log.d("eee$$",tempAverage);
                 Intent intent = new Intent(LoginActivity.this, SilverActivity.class);
                 intent.putExtra("name", p_phoneNumber.getText().toString()).putExtra("dataCode",codeList.get(i).toString()).putExtra("평균",tempAverage);
                 startActivity(intent);
                 dataCnt=0;
                 break;
             }
             dataCnt++;
             i++;
         }
        if(dataCnt!=0)
            Toast.makeText(getApplicationContext(), "존재하지 않는 회원코드입니다.", Toast.LENGTH_SHORT).show();

    }

/*
    public void checkCode(final String abc){
        testFirebase.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Log.d("[Test]", "ValueEventListener : " + snapshot.getValue());

                    if(snapshot.child("이메일").getValue()==null) {
                        dataCnt=1;
                        return;
                    }
                    String test2 =snapshot.child("회원 코드").getValue().toString();

                    if(test2.equals(abc)==true) {
                        Intent intent = new Intent(LoginActivity.this, SilverActivity.class);
                        intent.putExtra("name", p_phoneNumber.getText().toString()).putExtra("dataCode",test2);
                        startActivity(intent);
                        dataCnt=0;
                        break;
                    }
                    dataCnt++;
                }
                if(dataCnt!=0)
                    Toast.makeText(getApplicationContext(), "존재하지 않는 회원코드입니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
*/
}
