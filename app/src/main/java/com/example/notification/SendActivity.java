package com.example.notification;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity {

    private TextView textView;
    private String mUserid;
    private String user_name;
    private EditText mMessage;
    private Button mSendBtn;
    private ProgressDialog progressDialog;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        textView = findViewById(R.id.sendTextView);
        mUserid  = getIntent().getStringExtra("key");
        user_name = getIntent().getStringExtra("name");
        mMessage = findViewById(R.id.sendUser);
        mSendBtn = findViewById(R.id.button2);
        firestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        textView.setText(user_name);

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = mMessage.getText().toString();
                if(!TextUtils.isEmpty(msg)){
                    progressDialog.setMessage("Sending...");
                    progressDialog.show();
                    Map<String,Object> map = new HashMap<>();
                    map.put("message",msg);
                    map.put("from", FirebaseAuth.getInstance().getUid());

                    firestore.collection("Users")
                            .document(mUserid)
                            .collection("Notification")
                            .add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            /*RANDOM DOC IS REQUIRED OTHERWISE IF WE STORE NOTIFICATION AS CURRENT USER DOC
                            THEN IT WILL BE OVERWRITTEN WHEN USER WILL SEND MORE THAN ONE NOTIFICATION
                            */
                            if(task.isSuccessful()){
                                mMessage.setText(null);
                                progressDialog.dismiss();
                                print("Sent");
                            }else {
                                progressDialog.dismiss();
                                print(task.getException().getMessage());
                            }
                        }
                    });


                }else print("Enter some message");
            }
        });
    }

    private void print(String message) {
        Toast.makeText(SendActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}
