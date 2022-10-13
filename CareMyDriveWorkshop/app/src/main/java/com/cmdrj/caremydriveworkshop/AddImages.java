package com.cmdrj.caremydriveworkshop;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddImages extends AppCompatActivity {

    int GALLERY_CAMERA_PERMISSION_REQUEST_CODE = 10023;
    int GALLERY_PERMISSION_REQUEST_CODE = 10024;
    int CAMERA_PERMISSION_REQUEST_CODE = 10025;
    int READ_GALLERY_REQUEST_CODE = 10026;
    int CAPTURE_CAMERA_REQUEST_CODE = 10027;

    CardView addImages_CV1, addImages_CV2, addImages_CV3, addImages_CV4;
    ImageView addImages_IV1, addImages_IV2, addImages_IV3, addImages_IV4;
    ProgressBar addImages_PB1, addImages_PB2, addImages_PB3, addImages_PB4;
    ImageButton addImages_IB1, addImages_IB2, addImages_IB3, addImages_IB4;

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;

    RecVewWorkshop workshop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarAddImages);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

        }
        else if(ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            askStoragePermission();
        }
        else if(ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {
            askStoragePermission();
        }
        else {
            askStoragePermission();
        }





        addImages_CV1 = findViewById(R.id.addImages_CV1);
        addImages_CV2 = findViewById(R.id.addImages_CV2);
        addImages_CV3 = findViewById(R.id.addImages_CV3);
        addImages_CV4 = findViewById(R.id.addImages_CV4);

        addImages_IV1 = findViewById(R.id.addImages_IV1);
        addImages_IV2 = findViewById(R.id.addImages_IV2);
        addImages_IV3 = findViewById(R.id.addImages_IV3);
        addImages_IV4 = findViewById(R.id.addImages_IV4);

        addImages_PB1 = findViewById(R.id.addImages_PB1);
        addImages_PB2 = findViewById(R.id.addImages_PB2);
        addImages_PB3 = findViewById(R.id.addImages_PB3);
        addImages_PB4 = findViewById(R.id.addImages_PB4);

        addImages_IB1 = findViewById(R.id.addImages_IB1);
        addImages_IB2 = findViewById(R.id.addImages_IB2);
        addImages_IB3 = findViewById(R.id.addImages_IB3);
        addImages_IB4 = findViewById(R.id.addImages_IB4);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        workshop = intent.getExtras().getParcelable("itemWorkshop");

        DocumentReference dReference = fStore.collection("Workshops").document(fUser.getUid());

        dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {

                    Snackbar.make(addImages_IV1, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    workshop = value.toObject(RecVewWorkshop.class);

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });



        if( workshop.getImg1() != null)
        {

            addImages_CV1.setVisibility(View.VISIBLE);
            addImages_PB1.setVisibility(View.VISIBLE);
            addImages_IB1.setVisibility(View.GONE);

            StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("img1.jpeg");

            sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                @Override
                public void onSuccess(StorageMetadata storageMetadata) {

                    sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {

                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                            addImages_IV1.setImageBitmap(bitmap);
                            addImages_PB1.setVisibility(View.GONE);
                            addImages_IB1.setImageResource(R.drawable.ic_edit_24);
                            addImages_IB1.setVisibility(View.VISIBLE);

//                                        addImages_IB1.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//
//                                                if( workshop.getImg1() != null)
//                                                {
//
//                                                }
//
//                                            }
//                                        });

                            addImages_IV1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(AddImages.this, ViewImage.class);
                                    intent.putExtra("path", fStorage.getReference().child("Workshops").child(workshop.getID()).child("img1.jpeg").getPath());
                                    startActivity(intent);

                                }
                            });














                            if( workshop.getImg2() != null)
                            {

                                addImages_CV2.setVisibility(View.VISIBLE);
                                addImages_PB2.setVisibility(View.VISIBLE);
                                addImages_IB2.setVisibility(View.GONE);

                                StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("img2.jpeg");

                                sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                    @Override
                                    public void onSuccess(StorageMetadata storageMetadata) {

                                        sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                            @Override
                                            public void onSuccess(byte[] bytes) {

                                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                                addImages_IV2.setImageBitmap(bitmap);
                                                addImages_PB2.setVisibility(View.GONE);
                                                addImages_IB2.setImageResource(R.drawable.ic_edit_24);
                                                addImages_IB2.setVisibility(View.VISIBLE);

//                                                            addImages_IB2.setOnClickListener(new View.OnClickListener() {
//                                                                @Override
//                                                                public void onClick(View v) {
//
//                                                                    if( workshop.getImg2() != null)
//                                                                    {
//
//                                                                    }
//
//                                                                }
//                                                            });

                                                addImages_IV2.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        Intent intent = new Intent(AddImages.this, ViewImage.class);
                                                        intent.putExtra("path", fStorage.getReference().child("Workshops").child(workshop.getID()).child("img2.jpeg").getPath());
                                                        startActivity(intent);

                                                    }
                                                });









                                                if( workshop.getImg3() != null)
                                                {

                                                    addImages_CV3.setVisibility(View.VISIBLE);
                                                    addImages_PB3.setVisibility(View.VISIBLE);
                                                    addImages_IB3.setVisibility(View.GONE);

                                                    StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("img3.jpeg");

                                                    sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                                        @Override
                                                        public void onSuccess(StorageMetadata storageMetadata) {

                                                            sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                                @Override
                                                                public void onSuccess(byte[] bytes) {

                                                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                                                    addImages_IV3.setImageBitmap(bitmap);
                                                                    addImages_PB3.setVisibility(View.GONE);
                                                                    addImages_IB3.setImageResource(R.drawable.ic_edit_24);
                                                                    addImages_IB3.setVisibility(View.VISIBLE);

//                                                                                addImages_IB3.setOnClickListener(new View.OnClickListener() {
//                                                                                    @Override
//                                                                                    public void onClick(View v) {
//
//                                                                                        if( workshop.getImg3() != null)
//                                                                                        {
//
//                                                                                        }
//
//                                                                                    }
//                                                                                });

                                                                    addImages_IV3.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {

                                                                            Intent intent = new Intent(AddImages.this, ViewImage.class);
                                                                            intent.putExtra("path", fStorage.getReference().child("Workshops").child(workshop.getID()).child("img3.jpeg").getPath());
                                                                            startActivity(intent);

                                                                        }
                                                                    });









                                                                    if( workshop.getImg4() != null)
                                                                    {

                                                                        addImages_CV4.setVisibility(View.VISIBLE);
                                                                        addImages_PB4.setVisibility(View.VISIBLE);
                                                                        addImages_IB4.setVisibility(View.GONE);

                                                                        StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("img4.jpeg");

                                                                        sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                                                            @Override
                                                                            public void onSuccess(StorageMetadata storageMetadata) {

                                                                                sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                                                    @Override
                                                                                    public void onSuccess(byte[] bytes) {

                                                                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                                                                        addImages_IV4.setImageBitmap(bitmap);
                                                                                        addImages_PB4.setVisibility(View.GONE);
                                                                                        addImages_IB4.setImageResource(R.drawable.ic_edit_24);
                                                                                        addImages_IB4.setVisibility(View.VISIBLE);

//                                                                                                    addImages_IB4.setOnClickListener(new View.OnClickListener() {
//                                                                                                        @Override
//                                                                                                        public void onClick(View v) {
//
//                                                                                                            if( workshop.getImg4() != null)
//                                                                                                            {
//
//                                                                                                            }
//
//                                                                                                        }
//                                                                                                    });

                                                                                        addImages_IV4.setOnClickListener(new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {

                                                                                                Intent intent = new Intent(AddImages.this, ViewImage.class);
                                                                                                intent.putExtra("path", fStorage.getReference().child("Workshops").child(workshop.getID()).child("img4.jpeg").getPath());
                                                                                                startActivity(intent);

                                                                                            }
                                                                                        });



                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }
                                                                    else
                                                                    {
                                                                        addImages_CV4.setVisibility(View.VISIBLE);
                                                                        addImages_PB4.setVisibility(View.GONE);
                                                                        addImages_IB4.setImageResource(R.drawable.ic_add_24);
                                                                        addImages_IB4.setVisibility(View.VISIBLE);


//                                                                                    addImages_IB4.setOnClickListener(new View.OnClickListener() {
//                                                                                        @Override
//                                                                                        public void onClick(View v) {
//
//                                                                                            if( workshop.getImg4() == null)
//                                                                                            {
//
//                                                                                            }
//
//                                                                                        }
//                                                                                    });

                                                                        addImages_IV4.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {

                                                                            }
                                                                        });
                                                                    }










                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                                else
                                                {
                                                    addImages_CV3.setVisibility(View.VISIBLE);
                                                    addImages_PB3.setVisibility(View.GONE);
                                                    addImages_IB3.setImageResource(R.drawable.ic_add_24);
                                                    addImages_IB3.setVisibility(View.VISIBLE);


//                                                                addImages_IB3.setOnClickListener(new View.OnClickListener() {
//                                                                    @Override
//                                                                    public void onClick(View v) {
//
//                                                                        if( workshop.getImg3() == null)
//                                                                        {
//
//                                                                        }
//
//                                                                    }
//                                                                });

                                                    addImages_IV3.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                        }
                                                    });
                                                }










                                            }
                                        });
                                    }
                                });
                            }
                            else
                            {
                                addImages_CV2.setVisibility(View.VISIBLE);
                                addImages_PB2.setVisibility(View.GONE);
                                addImages_IB2.setImageResource(R.drawable.ic_add_24);
                                addImages_IB2.setVisibility(View.VISIBLE);


//                                            addImages_IB2.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//
//                                                    if( workshop.getImg2() == null)
//                                                    {
//
//                                                    }
//
//                                                }
//                                            });

                                addImages_IV2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                            }
















                        }
                    });
                }
            });
        }
        else
        {
            addImages_CV1.setVisibility(View.VISIBLE);
            addImages_PB1.setVisibility(View.GONE);
            addImages_IB1.setImageResource(R.drawable.ic_add_24);
            addImages_IB1.setVisibility(View.VISIBLE);


//                        addImages_IB1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                if( workshop.getImg1() == null)
//                                {
//
//                                }
//
//                            }
//                        });

            addImages_IV1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        addImages_IB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openUploadChoiceDialogBox(100);

                }
                else if(ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
//                    askStoragePermission();
                    openGallery(100);
                }
                else if(ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                {
//                    askStoragePermission();
                    openCamera(100);
                }
                else {

                    Snackbar.make(addImages_IV1, "Both Gallery and Camera Permissions Denied.\nAtleast one required permission to move forward." , Snackbar.LENGTH_SHORT).show();
//                    askStoragePermission();
                }

            }
        });

        addImages_IB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openUploadChoiceDialogBox(200);

                }
                else if(ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
//                    askStoragePermission();
                    openGallery(200);
                }
                else if(ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                {
//                    askStoragePermission();
                    openCamera(200);
                }
                else {

                    Snackbar.make(addImages_IV1, "Both Gallery and Camera Permissions Denied.\nAtleast one required permission to move forward." , Snackbar.LENGTH_SHORT).show();
//                    askStoragePermission();
                }

            }
        });

        addImages_IB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openUploadChoiceDialogBox(300);

                }
                else if(ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
//                    askStoragePermission();
                    openGallery(300);
                }
                else if(ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                {
//                    askStoragePermission();
                    openCamera(300);
                }
                else {

                    Snackbar.make(addImages_IV1, "Both Gallery and Camera Permissions Denied.\nAtleast one required permission to move forward." , Snackbar.LENGTH_SHORT).show();
//                    askStoragePermission();
                }

            }
        });

        addImages_IB4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openUploadChoiceDialogBox(400);

                }
                else if(ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
//                    askStoragePermission();
                    openGallery(400);
                }
                else if(ContextCompat.checkSelfPermission(AddImages.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                {
//                    askStoragePermission();
                    openCamera(400);
                }
                else {

                    Snackbar.make(addImages_IV1, "Both Gallery and Camera Permissions Denied.\nAtleast one required permission to move forward." , Snackbar.LENGTH_SHORT).show();
//                    askStoragePermission();
                }

            }
        });







    }

    private void openGallery(int code) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

//        String[] mimeTypes = {"image/jpeg", "image/png"};
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult(intent, READ_GALLERY_REQUEST_CODE + code);
    }

    private void openCamera(int code) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);     -> for gallery

//        String[] mimeTypes = {"image/jpeg", "image/png"};
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult(intent, CAPTURE_CAMERA_REQUEST_CODE + code);
    }

    private void openUploadChoiceDialogBox(int code) {

        new AlertDialog.Builder(this)
                .setTitle("Choose Image Upload Option")
                .setMessage("Choose an application to Upload Image")
                .setCancelable(false)
                .setPositiveButton("CAMERA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        openCamera(code);

                    }
                })
                .setNegativeButton("GALLERY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        openGallery(code);
                    }
                })
                .create().show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_GALLERY_REQUEST_CODE + 100)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if (data != null) {

                    InputStream is = null;
                    try {
                        is = getContentResolver().openInputStream(data.getData());


                        Bitmap bitmap = BitmapFactory.decodeStream(is);

//                        editDetSer_Accept.setEnabled(false);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);


                        addImages_PB1.setVisibility(View.VISIBLE);
                        addImages_IV1.setImageBitmap(bitmap);
                        addImages_IB1.setVisibility(View.GONE);

                        StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("img1.jpeg");

                        sReference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                if (taskSnapshot.getMetadata() != null) {
                                    if (taskSnapshot.getMetadata().getReference() != null) {

                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                Map<String, Object> map = new HashMap<>();
                                                map.put("img1", uri.toString());

                                                fStore.collection("Workshops").document(workshop.getID()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        addImages_PB1.setVisibility(View.GONE);
//                                                      editDetSer_Accept.setEnabled(true);

                                                        addImages_IB1.setImageResource(R.drawable.ic_edit_24);
                                                        addImages_IB1.setVisibility(View.VISIBLE);

                                                        addImages_IV1.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                Intent intent = new Intent(AddImages.this, ViewImage.class);
                                                                intent.putExtra("path", fStorage.getReference().child("Workshops").child(workshop.getID()).child("img1.jpeg").getPath());
                                                                startActivity(intent);

                                                            }
                                                        });

                                                        if(addImages_CV2.getVisibility() != View.VISIBLE)
                                                        {
                                                            addImages_CV2.setVisibility(View.VISIBLE);
                                                            addImages_PB2.setVisibility(View.GONE);
                                                            addImages_IB2.setImageResource(R.drawable.ic_add_24);
                                                            addImages_IB2.setVisibility(View.VISIBLE);

                                                        }

                                                    }
                                                });

                                            }
                                        });
                                    }
                                }


                            }
                        });

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    Snackbar.make(addImages_IV1, "Select an Image", Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(EditIndividualService.this,"Select a File",Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == CAPTURE_CAMERA_REQUEST_CODE + 100)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if (data != null) {

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");

//                    editDetSer_Accept.setEnabled(false);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

                    addImages_PB1.setVisibility(View.VISIBLE);
                    addImages_IV1.setImageBitmap(bitmap);
                    addImages_IB1.setVisibility(View.GONE);

                    StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("img1.jpeg");

                    sReference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {

                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            Map<String, Object> map = new HashMap<>();
                                            map.put("img1", uri.toString());

                                            fStore.collection("Workshops").document(workshop.getID()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    addImages_PB1.setVisibility(View.GONE);
//                                                      editDetSer_Accept.setEnabled(true);

                                                    addImages_IB1.setImageResource(R.drawable.ic_edit_24);
                                                    addImages_IB1.setVisibility(View.VISIBLE);

                                                    addImages_IV1.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            Intent intent = new Intent(AddImages.this, ViewImage.class);
                                                            intent.putExtra("path", fStorage.getReference().child("Workshops").child(workshop.getID()).child("img1.jpeg").getPath());
                                                            startActivity(intent);

                                                        }
                                                    });

                                                    if(addImages_CV2.getVisibility() != View.VISIBLE)
                                                    {
                                                        addImages_CV2.setVisibility(View.VISIBLE);
                                                        addImages_PB2.setVisibility(View.GONE);
                                                        addImages_IB2.setImageResource(R.drawable.ic_add_24);
                                                        addImages_IB2.setVisibility(View.VISIBLE);
                                                    }


                                                }
                                            });

                                        }
                                    });
                                }
                            }


                        }
                    });
                }

                else{

                    Snackbar.make(addImages_IV1, "Capture an Image", Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(EditIndividualService.this,"Select a File",Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == READ_GALLERY_REQUEST_CODE + 200)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if (data != null) {

                    InputStream is = null;
                    try {
                        is = getContentResolver().openInputStream(data.getData());


                        Bitmap bitmap = BitmapFactory.decodeStream(is);

//                        editDetSer_Accept.setEnabled(false);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);


                        addImages_PB2.setVisibility(View.VISIBLE);
                        addImages_IV2.setImageBitmap(bitmap);
                        addImages_IB2.setVisibility(View.GONE);

                        StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("img2.jpeg");

                        sReference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                if (taskSnapshot.getMetadata() != null) {
                                    if (taskSnapshot.getMetadata().getReference() != null) {

                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                Map<String, Object> map = new HashMap<>();
                                                map.put("img2", uri.toString());

                                                fStore.collection("Workshops").document(workshop.getID()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        addImages_PB2.setVisibility(View.GONE);
//                                                      editDetSer_Accept.setEnabled(true);

                                                        addImages_IB2.setImageResource(R.drawable.ic_edit_24);
                                                        addImages_IB2.setVisibility(View.VISIBLE);

                                                        addImages_IV2.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                Intent intent = new Intent(AddImages.this, ViewImage.class);
                                                                intent.putExtra("path", fStorage.getReference().child("Workshops").child(workshop.getID()).child("img2.jpeg").getPath());
                                                                startActivity(intent);

                                                            }
                                                        });

                                                        if(addImages_CV3.getVisibility() != View.VISIBLE)
                                                        {
                                                            addImages_CV3.setVisibility(View.VISIBLE);
                                                            addImages_PB3.setVisibility(View.GONE);
                                                            addImages_IB3.setImageResource(R.drawable.ic_add_24);
                                                            addImages_IB3.setVisibility(View.VISIBLE);
                                                        }

                                                    }
                                                });

                                            }
                                        });
                                    }
                                }


                            }
                        });

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    Snackbar.make(addImages_IV1, "Select an Image", Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(EditIndividualService.this,"Select a File",Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == CAPTURE_CAMERA_REQUEST_CODE + 200)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if (data != null) {

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");

//                    editDetSer_Accept.setEnabled(false);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

                    addImages_PB2.setVisibility(View.VISIBLE);
                    addImages_IV2.setImageBitmap(bitmap);
                    addImages_IB2.setVisibility(View.GONE);

                    StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("img2.jpeg");

                    sReference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {

                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            Map<String, Object> map = new HashMap<>();
                                            map.put("img2", uri.toString());

                                            fStore.collection("Workshops").document(workshop.getID()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    addImages_PB2.setVisibility(View.GONE);
//                                                      editDetSer_Accept.setEnabled(true);

                                                    addImages_IB2.setImageResource(R.drawable.ic_edit_24);
                                                    addImages_IB2.setVisibility(View.VISIBLE);

                                                    addImages_IV2.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            Intent intent = new Intent(AddImages.this, ViewImage.class);
                                                            intent.putExtra("path", fStorage.getReference().child("Workshops").child(workshop.getID()).child("img2.jpeg").getPath());
                                                            startActivity(intent);

                                                        }
                                                    });

                                                    if(addImages_CV3.getVisibility() != View.VISIBLE)
                                                    {
                                                        addImages_CV3.setVisibility(View.VISIBLE);
                                                        addImages_PB3.setVisibility(View.GONE);
                                                        addImages_IB3.setImageResource(R.drawable.ic_add_24);
                                                        addImages_IB3.setVisibility(View.VISIBLE);
                                                    }


                                                }
                                            });

                                        }
                                    });
                                }
                            }


                        }
                    });

                }

                else{

                    Snackbar.make(addImages_IV1, "Capture an Image", Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(EditIndividualService.this,"Select a File",Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == READ_GALLERY_REQUEST_CODE + 300)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if (data != null) {

                    InputStream is = null;
                    try {
                        is = getContentResolver().openInputStream(data.getData());


                        Bitmap bitmap = BitmapFactory.decodeStream(is);

//                        editDetSer_Accept.setEnabled(false);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);


                        addImages_PB3.setVisibility(View.VISIBLE);
                        addImages_IV3.setImageBitmap(bitmap);
                        addImages_IB3.setVisibility(View.GONE);

                        StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("img3.jpeg");

                        sReference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                if (taskSnapshot.getMetadata() != null) {
                                    if (taskSnapshot.getMetadata().getReference() != null) {

                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                Map<String, Object> map = new HashMap<>();
                                                map.put("img3", uri.toString());

                                                fStore.collection("Workshops").document(workshop.getID()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        addImages_PB3.setVisibility(View.GONE);
//                                                      editDetSer_Accept.setEnabled(true);

                                                        addImages_IB3.setImageResource(R.drawable.ic_edit_24);
                                                        addImages_IB3.setVisibility(View.VISIBLE);

                                                        addImages_IV3.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                Intent intent = new Intent(AddImages.this, ViewImage.class);
                                                                intent.putExtra("path", fStorage.getReference().child("Workshops").child(workshop.getID()).child("img3.jpeg").getPath());
                                                                startActivity(intent);

                                                            }
                                                        });

                                                        if(addImages_CV4.getVisibility() != View.VISIBLE)
                                                        {
                                                            addImages_CV4.setVisibility(View.VISIBLE);
                                                            addImages_PB4.setVisibility(View.GONE);
                                                            addImages_IB4.setImageResource(R.drawable.ic_add_24);
                                                            addImages_IB4.setVisibility(View.VISIBLE);
                                                        }

                                                    }
                                                });

                                            }
                                        });
                                    }
                                }


                            }
                        });

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    Snackbar.make(addImages_IV1, "Select an Image", Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(EditIndividualService.this,"Select a File",Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == CAPTURE_CAMERA_REQUEST_CODE + 300)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if (data != null) {

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");

//                    editDetSer_Accept.setEnabled(false);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

                    addImages_PB3.setVisibility(View.VISIBLE);
                    addImages_IV3.setImageBitmap(bitmap);
                    addImages_IB3.setVisibility(View.GONE);

                    StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("img3.jpeg");

                    sReference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {

                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            Map<String, Object> map = new HashMap<>();
                                            map.put("img3", uri.toString());

                                            fStore.collection("Workshops").document(workshop.getID()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    addImages_PB3.setVisibility(View.GONE);
//                                                      editDetSer_Accept.setEnabled(true);

                                                    addImages_IB3.setImageResource(R.drawable.ic_edit_24);
                                                    addImages_IB3.setVisibility(View.VISIBLE);

                                                    addImages_IV3.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            Intent intent = new Intent(AddImages.this, ViewImage.class);
                                                            intent.putExtra("path", fStorage.getReference().child("Workshops").child(workshop.getID()).child("img3.jpeg").getPath());
                                                            startActivity(intent);

                                                        }
                                                    });

                                                    if(addImages_CV4.getVisibility() != View.VISIBLE)
                                                    {
                                                        addImages_CV4.setVisibility(View.VISIBLE);
                                                        addImages_PB4.setVisibility(View.GONE);
                                                        addImages_IB4.setImageResource(R.drawable.ic_add_24);
                                                        addImages_IB4.setVisibility(View.VISIBLE);
                                                    }


                                                }
                                            });

                                        }
                                    });
                                }
                            }


                        }
                    });

                }

                else{

                    Snackbar.make(addImages_IV1, "Capture an Image", Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(EditIndividualService.this,"Select a File",Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == READ_GALLERY_REQUEST_CODE + 400)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if (data != null) {

                    InputStream is = null;
                    try {
                        is = getContentResolver().openInputStream(data.getData());


                        Bitmap bitmap = BitmapFactory.decodeStream(is);

//                        editDetSer_Accept.setEnabled(false);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);


                        addImages_PB4.setVisibility(View.VISIBLE);
                        addImages_IV4.setImageBitmap(bitmap);
                        addImages_IB4.setVisibility(View.GONE);

                        StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("img4.jpeg");

                        sReference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                if (taskSnapshot.getMetadata() != null) {
                                    if (taskSnapshot.getMetadata().getReference() != null) {

                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                Map<String, Object> map = new HashMap<>();
                                                map.put("img4", uri.toString());

                                                fStore.collection("Workshops").document(workshop.getID()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {


                                                        addImages_PB4.setVisibility(View.GONE);
//                                                      editDetSer_Accept.setEnabled(true);
                                                        addImages_IB4.setImageResource(R.drawable.ic_edit_24);
                                                        addImages_IB4.setVisibility(View.VISIBLE);

                                                        addImages_IV4.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                Intent intent = new Intent(AddImages.this, ViewImage.class);
                                                                intent.putExtra("path", fStorage.getReference().child("Workshops").child(workshop.getID()).child("img4.jpeg").getPath());
                                                                startActivity(intent);

                                                            }
                                                        });

                                                    }
                                                });

                                            }
                                        });
                                    }
                                }


                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    Snackbar.make(addImages_IV1, "Select an Image", Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(EditIndividualService.this,"Select a File",Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == CAPTURE_CAMERA_REQUEST_CODE + 400)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if (data != null) {

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");

//                    editDetSer_Accept.setEnabled(false);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

                    addImages_PB4.setVisibility(View.VISIBLE);
                    addImages_IV4.setImageBitmap(bitmap);
                    addImages_IB4.setVisibility(View.GONE);

                    StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("img4.jpeg");

                    sReference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {

                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            Map<String, Object> map = new HashMap<>();
                                            map.put("img4", uri.toString());

                                            fStore.collection("Workshops").document(workshop.getID()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {


                                                    addImages_PB4.setVisibility(View.GONE);
//                                                      editDetSer_Accept.setEnabled(true);

                                                    addImages_IB4.setImageResource(R.drawable.ic_edit_24);
                                                    addImages_IB4.setVisibility(View.VISIBLE);

                                                    addImages_IV4.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            Intent intent = new Intent(AddImages.this, ViewImage.class);
                                                            intent.putExtra("path", fStorage.getReference().child("Workshops").child(workshop.getID()).child("img4.jpeg").getPath());
                                                            startActivity(intent);

                                                        }
                                                    });
                                                }
                                            });

                                        }
                                    });
                                }
                            }


                        }
                    });

                }

                else{

                    Snackbar.make(addImages_IV1, "Capture an Image", Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(EditIndividualService.this,"Select a File",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    private void askStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            new AlertDialog.Builder(this)
                    .setTitle("Storage and Camera Permissions Needed")
                    .setMessage("Storage permission is needed to read images from gallery.\nCamera permission is needed to access camera application.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddImages.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, GALLERY_CAMERA_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
        else if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Storage Permission Needed")
                    .setMessage("Storage permission is needed to read images from gallery.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddImages.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
//                            openCamera();
                        }
                    })
                    .create().show();
        }
        else if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Camera Permission Needed")
                    .setMessage("Camera permission is needed to access camera application.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddImages.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
//                            openGallery();
                        }
                    })
                    .create().show();
        }
        else {

            if (ActivityCompat.checkSelfPermission(AddImages.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddImages.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(AddImages.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, GALLERY_CAMERA_PERMISSION_REQUEST_CODE);

            }
            else if(ActivityCompat.checkSelfPermission(AddImages.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(AddImages.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
            }
            else if(ActivityCompat.checkSelfPermission(AddImages.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(AddImages.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
//            else {
//                askStoragePermission();
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == GALLERY_CAMERA_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(addImages_IV1, "Gallery and Camera Permissions Granted" , Snackbar.LENGTH_SHORT).show();
//                openUploadChoiceDialogBox();
            }
            else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(addImages_IV1, "Gallery Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                openGallery();
            }
            else if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(addImages_IV1, "Camera Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                openCamera();
            }
            else
            {
                Snackbar.make(addImages_IV1, "Both Gallery and Camera Permissions Denied.\nAtleast one required permission to move forward." , Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        else if (requestCode == GALLERY_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(addImages_IV1, "Gallery Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
//                openUploadChoiceDialogBox();
            }
            else
            {
                Snackbar.make(addImages_IV1, "Gallery Permission Denied" , Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
//                openCamera();
            }
        }

        else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(addImages_IV1, "Camera Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
//                openUploadChoiceDialogBox();
            }
            else
            {
                Snackbar.make(addImages_IV1, "Camera Permission Denied" , Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
//                openGallery();
            }
        }
    }
}