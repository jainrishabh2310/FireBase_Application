package com.example.firebase_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
EditText name,roll,course,duration;
Button btn,browse,google_btn;
ImageView imageView;
Uri uri;
Bitmap bitmap;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=findViewById(R.id.name);
        roll=findViewById(R.id.roll);
        duration=findViewById(R.id.duration);
        course=findViewById(R.id.course);
        google_btn=findViewById(R.id.google_btn);

        btn=findViewById(R.id.update);
        imageView=findViewById(R.id.imageview);
        browse=findViewById(R.id.browse);
        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Sign_in_Google.class));
            }
        });










        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(MainActivity.this)
                        .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent,1);





                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
permissionToken.continuePermissionRequest();
                            }
                        }).check();



            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
                StorageReference reference1=firebaseStorage.getReference("image1"+new Random().nextInt(50));
                reference1.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        FirebaseDatabase db=FirebaseDatabase.getInstance();
                                        DatabaseReference upload=db.getReference("student");

                                        String Roll=roll.getText().toString().trim();
                                        String Name=name.getText().toString().trim();
                                        String Course=course.getText().toString().trim();
                                        String Duration=duration.getText().toString().trim();
                                        dataholder dh= new dataholder(Course,Duration,Name,uri.toString());


                                        upload.child(Roll).setValue(dh);
                                        roll.setText("");
                                        name.setText("");

                                        course.setText("");
                                        duration.setText("");
                                        Toast.makeText(getApplicationContext(),"Inserted",Toast.LENGTH_LONG).show();




                                    }
                                });

                                Toast.makeText(getApplicationContext(),"File uploaded",Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                            }
                        });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK){
            uri=data.getData();
            System.out.println(uri);
            try {
                InputStream inputStream=getContentResolver().openInputStream(uri);
                bitmap= BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}