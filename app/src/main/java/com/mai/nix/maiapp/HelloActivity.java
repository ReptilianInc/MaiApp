package com.mai.nix.maiapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class HelloActivity extends AppCompatActivity {
    private Button mButton;
    private CheckBox mCheckBox;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        mButton = (Button)findViewById(R.id.lets_go_button);
        mCheckBox =(CheckBox)findViewById(R.id.checkbox_student);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCheckBox.isChecked()){
                    mIntent = ChooseGroupActivity.newIntent(HelloActivity.this, true);
                }else{
                    mIntent = new Intent(HelloActivity.this, MainActivity.class);
                }
                startActivity(mIntent);
            }
        });
    }
}
