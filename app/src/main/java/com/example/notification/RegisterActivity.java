package com.example.notification;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 123;
    private CircleImageView imageView;
    private EditText newEmail;
    private EditText newPassword;
    private Button create;
    private Uri imageUri = null;
    private StorageReference storageReference;
    private FirebaseAuth regAuth;
    private FirebaseFirestore firestore;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imageView = findViewById(R.id.profile_image);
        newEmail = findViewById(R.id.newemail);
        newPassword = findViewById(R.id.newpassword);
        create = findViewById(R.id.create);
        storageReference = FirebaseStorage.getInstance().getReference("images");
        regAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);

            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri != null){
                    final String newemail = newEmail.getText().toString();
                    final String pass = newPassword.getText().toString();
                    if(newemail.isEmpty() || pass.isEmpty())
                        print("Empty username or password");
                    else{
                         progressDialog.setMessage("Please Wait");
                         progressDialog.show();
                          regAuth.createUserWithEmailAndPassword(newemail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                              @Override
                              public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        StorageReference path = storageReference.child(imageUri.getLastPathSegment());
                                        path.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                  if(task.isSuccessful()){
                                                      String downloadUri = task.getResult().getDownloadUrl().toString();
                                                      store(newemail,downloadUri);
                                                  }else {
                                                      progressDialog.dismiss();
                                                      print(task.getException().getMessage());
                                                  }
                                            }
                                        });
                                    }else{
                                        print(task.getException().getMessage());
                                        progressDialog.dismiss();
                                    }
                              }
                          });
                    }

                }else print("Upload an Image");
            }
        });
    }

    private void store(String newemail,String downloadUri) {

        Map<String,Object> map = new HashMap<>();
        map.put("name",newemail);
        map.put("image",downloadUri);

        firestore.collection("Users").document(regAuth.getCurrentUser().getUid()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    finish();
                }else{
                    progressDialog.dismiss();
                    print(task.getException().getMessage());
                }
            }
        });

    }

    private void print(String message) {
        Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}
