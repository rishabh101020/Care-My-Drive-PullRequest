package com.cmdrj.caremydriveworkshop;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class DialogUploadImage extends AppCompatDialogFragment {


    int GALLERY_CAMERA_PERMISSION_REQUEST_CODE = 10018;
    int GALLERY_PERMISSION_REQUEST_CODE = 10019;
    int CAMERA_PERMISSION_REQUEST_CODE = 10020;
    int READ_GALLERY_REQUEST_CODE = 100021;
    int CAPTURE_CAMERA_REQUEST_CODE = 10022;

    ByteArrayOutputStream baos = null;
    boolean isValid = false;
    int profitablePrice, profit;
//    Date nextServiceDate = null;


    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;


    private ImageView Dlg_upload_image_IV;
    private ProgressBar Dlg_upload_image_PB;
    TextInputLayout Dlg_upload_image_commissionablePrice_TIL, Dlg_upload_image_commission_TIL, Dlg_upload_image_nextKM_TIL;//, Dlg_upload_image_nextDate_TIL;
    TextInputEditText Dlg_upload_image_commissionablePrice_TIET, Dlg_upload_image_commission_TIET, Dlg_upload_image_nextKM_TIET;
//    MaterialAutoCompleteTextView Dlg_upload_image_nextDate_MACT;

    private onInputListenerUI listener;

    public interface onInputListenerUI {
        void sendInputUI(boolean valid, RecVewBookings booking);
        void sendInputUI(boolean valid, RecVewBookings booking, double commissionablePrice, double commission, double nextKM);//, Date nextDate);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (onInputListenerUI) context;
        } catch (ClassCastException e) {

            try {
                listener = (onInputListenerUI) getTargetFragment();
            } catch (ClassCastException e1) {
                throw new ClassCastException(context.toString() + " must implement onInputListenerUI interface");
            }
        }


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Bundle bundleObj = getArguments();

        // TODO : if position received is 2 and if a new booking is requested during the time of writing of comments then rejection will be of 2nd only or it will be of 1st ( as now initialy booking at 1st position is at 2nd )
        RecVewBookings itemBooking = bundleObj.getParcelable("itemBooking");
        int parentActivity = bundleObj.getInt("parentActivity");

        // TODO : over

        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_upload_image, null);

        if (parentActivity == 0) {
            // jobCard

            builder.setView(view).setTitle("Upload Job Card")
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (isValid && baos != null) {
                                Dlg_upload_image_PB.setVisibility(View.VISIBLE);
                                StorageReference sReference = fStorage.getReference().child("Bookings").child(itemBooking.getID()).child("jobCard.jpeg");

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
                                                        map.put("jobCard", uri.toString());

                                                        fStore.collection("Bookings").document(itemBooking.getID()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                Dlg_upload_image_PB.setVisibility(View.GONE);
                                                                listener.sendInputUI(true, itemBooking);

                                                            }
                                                        });

                                                    }
                                                });
                                            }
                                        }


                                    }
                                });


                            } else {
                                listener.sendInputUI(false, itemBooking);
                            }
                        }
                    });


        } else if (parentActivity == 1) {
            // invoice

            builder.setView(view).setTitle("Upload Invoice")
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (isValid && baos != null && Dlg_upload_image_commissionablePrice_TIET.getText().toString().trim() != null && !Dlg_upload_image_commissionablePrice_TIET.getText().toString().trim().equals("") && Dlg_upload_image_commission_TIET.getText().toString().trim() != null && !Dlg_upload_image_commission_TIET.getText().toString().trim().equals("")       && Dlg_upload_image_nextKM_TIET.getText().toString().trim() != null && !Dlg_upload_image_nextKM_TIET.getText().toString().trim().equals("")){// && Dlg_upload_image_nextDate_MACT.getText().toString().trim() != null && !Dlg_upload_image_nextDate_MACT.getText().toString().trim().equals("")) {
                                Dlg_upload_image_PB.setVisibility(View.VISIBLE);
                                StorageReference sReference = fStorage.getReference().child("Bookings").child(itemBooking.getID()).child("invoice.jpeg");

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
                                                        map.put("invoice", uri.toString());
                                                        map.put("commissionablePrice", Double.valueOf( Dlg_upload_image_commissionablePrice_TIET.getText().toString() ) );
                                                        map.put("commission", Double.valueOf( Dlg_upload_image_commission_TIET.getText().toString() ) );
                                                        map.put("nextScheduledKM", Double.valueOf( Dlg_upload_image_nextKM_TIET.getText().toString() ) );
//                                                        map.put("nextScheduledDate", new Timestamp(nextServiceDate) );


                                                        fStore.collection("Bookings").document(itemBooking.getID()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                Dlg_upload_image_PB.setVisibility(View.GONE);
                                                                listener.sendInputUI(true, itemBooking, Double.valueOf( Dlg_upload_image_commissionablePrice_TIET.getText().toString() ), Double.valueOf( Dlg_upload_image_commission_TIET.getText().toString() ), Double.valueOf( Dlg_upload_image_nextKM_TIET.getText().toString() ));//, nextServiceDate );

                                                            }
                                                        });

                                                    }
                                                });
                                            }
                                        }


                                    }
                                });


                            } else {
                                listener.sendInputUI(false, itemBooking,-1, -1, -1);//, nextServiceDate);
                            }
                        }
                    });
        }


        Dlg_upload_image_IV = view.findViewById(R.id.Dlg_upload_image_IV);
        Dlg_upload_image_PB = view.findViewById(R.id.Dlg_upload_image_PB);
        Dlg_upload_image_commissionablePrice_TIL = view.findViewById(R.id.Dlg_upload_image_commissionablePrice_TIL);
        Dlg_upload_image_commissionablePrice_TIET = view.findViewById(R.id.Dlg_upload_image_commissionablePrice_TIET);
        Dlg_upload_image_commission_TIL = view.findViewById(R.id.Dlg_upload_image_commission_TIL);
        Dlg_upload_image_commission_TIET = view.findViewById(R.id.Dlg_upload_image_commission_TIET);
//        Dlg_upload_image_nextDate_TIL = view.findViewById(R.id.Dlg_upload_image_nextDate_TIL);
//        Dlg_upload_image_nextDate_MACT = view.findViewById(R.id.Dlg_upload_image_nextDate_MACT);
        Dlg_upload_image_nextKM_TIL = view.findViewById(R.id.Dlg_upload_image_nextKM_TIL);
        Dlg_upload_image_nextKM_TIET = view.findViewById(R.id.Dlg_upload_image_nextKM_TIET);


        if(parentActivity == 1)
        {
            Dlg_upload_image_commissionablePrice_TIL.setVisibility(View.VISIBLE);
            Dlg_upload_image_commission_TIL.setVisibility(View.VISIBLE);
//            Dlg_upload_image_nextDate_TIL.setVisibility(View.VISIBLE);
            Dlg_upload_image_nextKM_TIL.setVisibility(View.VISIBLE);

            if(itemBooking.getPrice() != -1)
            {
                Dlg_upload_image_commissionablePrice_TIET.setText(Double.toString(itemBooking.getPrice()));
                Dlg_upload_image_commission_TIET.setText(Double.toString(itemBooking.getPrice() * .05));

//                Calendar cal = Calendar.getInstance();
//
////                Dlg_upload_image_nextDate_MACT.setText(cal.get(Calendar.DAY_OF_MONTH) +" /"+ (cal.get(Calendar.MONTH) +1) +" /"+ cal.get(Calendar.YEAR));
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
////                            month =  month +1;
////                            startDate = dayOfMonth+" /"+month+" /"+year;
////                            yy[0] = year;
////                            mm[0] = month-1;
////                            dd[0] = dayOfMonth;
////                            ConfirmBooking_Date_MACT.setText(startDate);
//
//                        Dlg_upload_image_nextDate_MACT.setText(dayOfMonth +" /"+ (month +1) +" /"+ year);
//
//                        Calendar cal = GregorianCalendar.getInstance();
//                        cal.set(year, month, dayOfMonth);
//                        nextServiceDate = cal.getTime();
//
////                        Map<String, Object> map = new HashMap<>();
////                        map.put("serviceDate", new Timestamp(serviceDate));
////                        map.put("status", -2);
////
////
////                        fStore.collection("Bookings").document(booking.getID()).update(map);
//
//                    }
//                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
//                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                datePickerDialog.setCancelable(false);
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                datePickerDialog.show();

            }
        }

        Dlg_upload_image_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openUploadChoiceDialogBox();

                }
                else if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    askStoragePermission();
//                    openGallery();
                }
                else if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                {
                    askStoragePermission();
//                    openCamera();
                }
                else {
                    askStoragePermission();
                }

            }
        });

        return builder.create();

    }


    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, READ_GALLERY_REQUEST_CODE);
    }

    private void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, CAPTURE_CAMERA_REQUEST_CODE);
    }

    private void openUploadChoiceDialogBox() {

        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Choose Image Upload Option")
                .setMessage("Choose an application to Upload Image")
                .setCancelable(false)
                .setPositiveButton("CAMERA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        openCamera();

                    }
                })
                .setNegativeButton("GALLERY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        openGallery();
                    }
                })
                .create().show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {

                    InputStream is = null;
                    try {
                        is = getActivity().getContentResolver().openInputStream(data.getData());       // TODO: check this line    getActivity()        // TODO : over


                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        isValid = false;
//                        editDetSer_Accept.setEnabled(false);

                        baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);


                        Dlg_upload_image_PB.setVisibility(View.VISIBLE);
                        Dlg_upload_image_IV.setImageBitmap(bitmap);
                        Dlg_upload_image_PB.setVisibility(View.GONE);
//                        editDetSer_Accept.setEnabled(true);
                        isValid = true;


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
//                else{
//                    Snackbar.make(editDetSer_Name_TIET, "Select an Image", Snackbar.LENGTH_SHORT).show();
////                    Toast.makeText(EditIndividualService.this,"Select a File",Toast.LENGTH_SHORT).show();
//                }
            }
        }

        if (requestCode == CAPTURE_CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                    isValid = false;
//                    editDetSer_Accept.setEnabled(false);

                    baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                    Dlg_upload_image_PB.setVisibility(View.VISIBLE);
                    Dlg_upload_image_IV.setImageBitmap(bitmap);
                    Dlg_upload_image_PB.setVisibility(View.GONE);
//                    editDetSer_Accept.setEnabled(true);
                    isValid = true;
                }

//                else{
//
//                    Snackbar.make(editDetSer_Name_TIET, "Capture an Image", Snackbar.LENGTH_SHORT).show();
////                    Toast.makeText(EditIndividualService.this,"Select a File",Toast.LENGTH_SHORT).show();
//                }
            }
        }
    }


    private void askStoragePermission() {

        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) && shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {

            new androidx.appcompat.app.AlertDialog.Builder(getContext())
                    .setTitle("Storage and Camera Permissions Needed")
                    .setMessage("Storage permission is needed to read images from gallery.\nCamera permission is needed to access camera application.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, GALLERY_CAMERA_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            new androidx.appcompat.app.AlertDialog.Builder(getContext())
                    .setTitle("Storage Permission Needed")
                    .setMessage("Storage permission is needed to read images from gallery.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            openCamera();
                        }
                    })
                    .create().show();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
        {
            new androidx.appcompat.app.AlertDialog.Builder(getContext())
                    .setTitle("Camera Permission Needed")
                    .setMessage("Camera permission is needed to access camera application.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            openGallery();
                        }
                    })
                    .create().show();
        } else {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, GALLERY_CAMERA_PERMISSION_REQUEST_CODE);

            } else if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
            } else if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
//            else {
//                askStoragePermission();
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == GALLERY_CAMERA_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
//                Snackbar.make(Dlg_upload_image_IV, "Gallery and Camera Permissions Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                openUploadChoiceDialogBox();
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
//                Snackbar.make(editDetSer_Name_TIET, "Gallery Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    openGallery();
                }
                else if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                {
                    openCamera();
                }

            } else if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                Snackbar.make(editDetSer_Name_TIET, "Camera Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    openGallery();
                }
                else if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                {
                    openCamera();
                }
            } else {
//                Snackbar.make(editDetSer_Name_TIET, "Both Gallery and Camera Permissions Denied.\nAtleast one required permission to move forward." , Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Snackbar.make(editDetSer_Name_TIET, "Gallery Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                openUploadChoiceDialogBox();
            } else {
//                Snackbar.make(editDetSer_Name_TIET, "Gallery Permission Denied" , Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                openCamera();
            }
        } else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Snackbar.make(editDetSer_Name_TIET, "Camera Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                openUploadChoiceDialogBox();
            } else {
//                Snackbar.make(editDetSer_Name_TIET, "Camera Permission Denied" , Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                openGallery();
            }
        }
    }
}