package com.vrajdesai78.truthify;

import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLRemoteModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    FirebaseAutoMLRemoteModel remoteModel; // For loading the model remotely
    FirebaseVisionImageLabeler labeler; //For running the image labeler
    FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder optionsBuilder; // Which option is use to run the labeler local or remotely
    ProgressDialog progressDialog; //Show the progress dialog while model is downloading...
    FirebaseModelDownloadConditions conditions; //Conditions to download the model
    FirebaseVisionImage image; // preparing the input image
    TextView labeltxt; // Displaying the label for the input image
    Button adidas, apple, armani, nike, puma, reebok; // To select the image from device
    ImageView img; //To display the selected image
    private FirebaseAutoMLLocalModel localModel;
    private Button cnl;
    AlertDialog alert1;
    Dialog dialog;
    String clicked;
    FloatingActionButton camera, logout, fab;
    private boolean isopen;
    private Animation mfabopen, mfabclose, fabclock, fabanticlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        adidas = findViewById(R.id.adidas);
        apple = findViewById(R.id.apple);
        armani = findViewById(R.id.armani);
        nike = findViewById(R.id.nike);
        puma = findViewById(R.id.puma);
        reebok = findViewById(R.id.reebok);
        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        camera = findViewById(R.id.camera);
        logout = findViewById(R.id.logout);
        fab = findViewById(R.id.fab);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, RegisterActivity.class));
                finish();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(HomeActivity.this);
            }
        });

        mfabopen = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.fab_open);
        mfabclose = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.fab_close);
        fabclock = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.fab_rotate_clock);
        fabanticlock = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.fab_rotate_anticlock);

        isopen = false;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isopen)
                {
                    camera.setVisibility(View.INVISIBLE);
                    logout.setVisibility(View.INVISIBLE);
                    fab.startAnimation(fabanticlock);
                    camera.startAnimation(mfabclose);
                    logout.startAnimation(mfabclose);
                    camera.setClickable(false);
                    logout.setClickable(false);
                    isopen = false;
                }
                else
                {
                    camera.setVisibility(View.VISIBLE);
                    logout.setVisibility(View.VISIBLE);
                    fab.startAnimation(fabclock);
                    camera.startAnimation(mfabopen);
                    logout.startAnimation(mfabopen);
                    camera.setClickable(true);
                    logout.setClickable(true);
                    isopen = true;
                }
            }
        });

        dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.image_dialog);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        img = dialog.findViewById(R.id.img);
        labeltxt = dialog.findViewById(R.id.label);
        cnl = dialog.findViewById(R.id.cancel);
        cnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        adidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked="adidas";
                CropImage.activity().start(HomeActivity.this);
            }
        });

        apple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked="apple";
                CropImage.activity().start(HomeActivity.this);
            }
        });

        armani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked="armani";
                CropImage.activity().start(HomeActivity.this);
            }
        });

        nike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked="nike";
                CropImage.activity().start(HomeActivity.this);
            }
        });

        puma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked="puma";
                CropImage.activity().start(HomeActivity.this);
            }
        });

        reebok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked="reebok";
                CropImage.activity().start(HomeActivity.this);
            }
        });

    }

    private void setLabelerFromLocalModel(Uri uri) {
        localModel = new FirebaseAutoMLLocalModel.Builder()
                .setAssetFilePath("model/manifest.json")
                .build();
        try {
            FirebaseVisionOnDeviceAutoMLImageLabelerOptions options =
                    new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(localModel)
                            .setConfidenceThreshold(0.0f)
                            .build();
            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);
            image = FirebaseVisionImage.fromFilePath(HomeActivity.this, uri);
            processImageLabeler(labeler, image);
        } catch (FirebaseMLException | IOException e) { }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if (result != null) {
                    Uri uri = result.getUri(); //path of image in phone
                    img.setImageURI(uri); //set image in imageview
//                    textView.setText(""); //so that previous text don't get append with new one
                    setLabelerFromLocalModel(uri);
                } else
                    progressDialog.cancel();
            } else
                progressDialog.cancel();
        }
    }

    private void processImageLabeler(FirebaseVisionImageLabeler labeler, FirebaseVisionImage image) {
        labeler.processImage(image).addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onComplete(@NonNull Task<List<FirebaseVisionImageLabel>> task) {
                progressDialog.cancel();
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeActivity.this);
//                builder1.setCancelable(true);
//
//                builder1.setNegativeButton(
//                        "Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });

//                alert1 = builder1.create();
                if(!TextUtils.isEmpty(clicked)) {
                    for (FirebaseVisionImageLabel label : task.getResult()) {
                        String eachlabel = label.getText().toUpperCase();
                        float confidence = label.getConfidence();
                        if ((confidence * 100) >= 90 && TextUtils.equals(clicked.toUpperCase(), eachlabel)) {
                            labeltxt.setText(eachlabel);
                            //textView.setText(eachlabel + " - "+ (confidence*100));
                            break;
                        } else {
                            labeltxt.setText("Logo not found");
                        }
                    }
                    clicked=null;
                }
                else {
                    for (FirebaseVisionImageLabel label : task.getResult()) {
                        String eachlabel = label.getText().toUpperCase();
                        float confidence = label.getConfidence();
                        if ((confidence * 100) >= 90) {
                            labeltxt.setText(eachlabel);
                            //textView.setText(eachlabel + " - "+ (confidence*100));
                            break;
                        } else {
                            labeltxt.setText("Logo not found");
                        }
                    }
                }
                dialog.show();
//                builder1.setCancelable(true);
//
//                builder1.setNegativeButton(
//                        "Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//                alert1 = builder1.create();
//                alert1.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("OnFail", "" + e);
                Toast.makeText(HomeActivity.this, "Something went wrong! " + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
