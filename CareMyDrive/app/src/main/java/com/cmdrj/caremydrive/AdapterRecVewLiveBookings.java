package com.cmdrj.caremydrive;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class AdapterRecVewLiveBookings extends RecyclerView.Adapter<AdapterRecVewLiveBookings.ViewHolder> {

    private ArrayList<RecVewBookings> newAppli;
    FirebaseFirestore fStore;
    Activity activity;

    public AdapterRecVewLiveBookings(ArrayList<RecVewBookings> newAppli) {
        this.newAppli = newAppli;
        fStore = FirebaseFirestore.getInstance();
    }

    public AdapterRecVewLiveBookings(ArrayList<RecVewBookings> newAppli, Activity activity) {
        this.newAppli = newAppli;
        fStore = FirebaseFirestore.getInstance();
        this.activity = activity;

    }

    @NonNull
    @Override
    public AdapterRecVewLiveBookings.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_vew_bookings, parent, false);
        return new AdapterRecVewLiveBookings.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecVewLiveBookings.ViewHolder holder, int position) {

        RecVewBookings appointment = newAppli.get(position);

        holder.setData(appointment);
    }

    @Override
    public int getItemCount() {
        return newAppli.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView RV_bookings_CarName, RV_bookings_ServiceName, RV_bookings_ServiceDate, RV_bookings_Status;
        private Button RV_bookings_VewDet, RV_bookings_Contact;
        RecVewBookings bookings;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            RV_bookings_CarName = itemView.findViewById(R.id.RV_bookings_CarName);
            RV_bookings_ServiceName = itemView.findViewById(R.id.RV_bookings_ServiceName);
            RV_bookings_ServiceDate = itemView.findViewById(R.id.RV_bookings_ServiceDate);
            RV_bookings_Status = itemView.findViewById(R.id.RV_bookings_Status);
            RV_bookings_VewDet = itemView.findViewById(R.id.RV_bookings_VewDet);
            RV_bookings_Contact = itemView.findViewById(R.id.RV_bookings_Contact);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), DetailsBooking.class);
                    intent.putExtra("itemBooking", bookings);
                    v.getContext().startActivity(intent);
                }
            });

            RV_bookings_Contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(bookings.getStatus() == -3)
                    {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", bookings.getID());
                        map.put("ref", fStore.collection("Bookings").document(bookings.getID()));

                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("status", 0);

                        bookings.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                RecVewWorkshop itemWorkshop = documentSnapshot.toObject(RecVewWorkshop.class);

                                fStore.collection("Workshops").document(itemWorkshop.getID()).collection("requestedBookings").document(bookings.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                        bookings.getServiceRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                RecVewService itemService = documentSnapshot.toObject(RecVewService.class);

                                                fStore.collection("Workshops").document(itemWorkshop.getID()).collection("Services").document(itemService.getID()).collection("requestedBookings").document(bookings.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        fStore.collection("Bookings").document(bookings.getID()).update(map1);
                                                    }
                                                });
                                            }
                                        });


                                    }
                                });
                            }
                        });
                    }
                    else if(bookings.getStatus() == -2)
                    {
                        Map<String, Object> map = new HashMap<>();
                        map.put("status", 0);

                        fStore.collection("Bookings").document(bookings.getID()).update(map);
                    }
                    else if(bookings.getStatus() == -1)
                    {

                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Choose Contact Option")
                                .setMessage("Choose an application to contact Admin")
                                .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", v.getContext().getResources().getString(R.string.admin_Email), null));
//                        if (intent.resolveActivity(getPackageManager()) != null) {
                                        v.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
//                        }

                                    }
                                })
                                .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", v.getContext().getResources().getString(R.string.admin_PhoneNo), null));
//                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                            v.getContext().startActivity(phoneIntent);
//                                        }

                                    }
                                })
                                .create().show();
                    }
                    else
                    {
                        bookings.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                RecVewWorkshop itemWorkshop = documentSnapshot.toObject(RecVewWorkshop.class);

                                new AlertDialog.Builder(v.getContext())
                                        .setTitle("Choose Contact Option")
                                        .setMessage("Choose an application to contact Workshop")
                                        .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", itemWorkshop.getEmail(), null));
//                                if (intent.resolveActivity(getPackageManager()) != null) {
                                                v.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
//                                }

                                            }
                                        })
                                        .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", itemWorkshop.getMbNo(), null));
//                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                                v.getContext().startActivity(phoneIntent);
//                                        }

                                            }
                                        })
                                        .create().show();

                            }
                        });
                    }
                }
            });

            RV_bookings_VewDet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bookings.getStatus() == -3)
                    {
                        bookings.getParentServiceRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                RecVewService itemParentService = documentSnapshot.toObject(RecVewService.class);

                                fStore.collection("Bookings").document(bookings.getID()).collection("rejectedBy").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                                        ArrayList<String> rejectedWorkshops = new ArrayList<>();
                                        ArrayList<String> rejectedServices = new ArrayList<>();

                                        for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots )
                                        {
                                            rejectedWorkshops.add(queryDocumentSnapshot.getString("workShopId"));
                                            rejectedServices.add(queryDocumentSnapshot.getString("serviceId"));
                                        }



                                        Intent intent = new Intent(v.getContext(), ChooseRemainingWorkshop.class);
                                        intent.putExtra("itemService",itemParentService);
                                        intent.putExtra("itemBooking",bookings);
                                        intent.putStringArrayListExtra("rejectedWorkshops",rejectedWorkshops);
                                        intent.putStringArrayListExtra("rejectedServices",rejectedServices);
                                        v.getContext().startActivity(intent);
                                        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    }
                                });


                            }
                        });
                    }
                    else if(bookings.getStatus() == -2)
                    {
                        long timestamp = bookings.getServiceDate().toDate().getTime();
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(timestamp);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                v.getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

//                            month =  month +1;
//                            startDate = dayOfMonth+" /"+month+" /"+year;
//                            yy[0] = year;
//                            mm[0] = month-1;
//                            dd[0] = dayOfMonth;
//                            ConfirmBooking_Date_MACT.setText(startDate);

                                Date serviceDate = null;
                                Calendar cal = GregorianCalendar.getInstance();
                                cal.set(year, month, dayOfMonth);
                                serviceDate = cal.getTime();

                                Map<String, Object> map = new HashMap<>();
                                map.put("serviceDate", new Timestamp(serviceDate));
                                map.put("status", 0);


                                fStore.collection("Bookings").document(bookings.getID()).update(map);

                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        datePickerDialog.setCancelable(false);
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                    }
                }
            });
        }

        public void setData(RecVewBookings appointment) {

            bookings = appointment;

            bookings.getVehicleRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    RV_bookings_CarName.setText(documentSnapshot.getString("name"));

                }
            });

            if(bookings.getStatus() == -1)
            {
                bookings.getParentServiceRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        RV_bookings_ServiceName.setText(documentSnapshot.getString("name"));
                        RV_bookings_ServiceName.setTypeface(null, Typeface.NORMAL);

                    }
                });
            }
            else if(bookings.getStatus() == -3)
            {
                bookings.getServiceRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        RV_bookings_ServiceName.setText(documentSnapshot.getString("name"));
                        RV_bookings_ServiceName.setTypeface(null, Typeface.BOLD);
                    }
                });
            }
            else
            {
                bookings.getServiceRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        RV_bookings_ServiceName.setText(documentSnapshot.getString("name"));
                        RV_bookings_ServiceName.setTypeface(null, Typeface.NORMAL);

                    }
                });
            }

            if(bookings.getStatus() == 0)
            {
                RV_bookings_Status.setVisibility(View.GONE);
                RV_bookings_VewDet.setVisibility(View.INVISIBLE);

                RV_bookings_Contact.setText("Contact");

            }
            else if(bookings.getStatus() == -1)
            {
                RV_bookings_Status.setVisibility(View.GONE);
                RV_bookings_VewDet.setVisibility(View.INVISIBLE);

                RV_bookings_Contact.setText("Contact");

            }
            else if(bookings.getStatus() == -3)
            {
                RV_bookings_Status.setVisibility(View.GONE);
                RV_bookings_VewDet.setVisibility(View.VISIBLE);

                RV_bookings_Contact.setText("Accept");
                RV_bookings_VewDet.setText("Change");

            }
            else if(bookings.getStatus() == -2)
            {
                RV_bookings_Status.setVisibility(View.GONE);
                RV_bookings_VewDet.setVisibility(View.VISIBLE);

                RV_bookings_Contact.setText("Accept");
                RV_bookings_VewDet.setText("Change");
            }
            else if(bookings.getStatus() == 1)
            {
                RV_bookings_Status.setVisibility(View.INVISIBLE);
                RV_bookings_VewDet.setVisibility(View.GONE);

                RV_bookings_Contact.setText("Contact");

            }
            else if(bookings.getStatus() == 2)
            {
                RV_bookings_Status.setVisibility(View.VISIBLE);
                RV_bookings_VewDet.setVisibility(View.GONE);

                RV_bookings_Contact.setText("Contact");
                RV_bookings_Status.setText("Started");

            }

            String pattern = "dd-MM-yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            RV_bookings_ServiceDate.setText(df.format(bookings.getServiceDate().toDate()));

            if(bookings.getStatus() == -2)
            {
                RV_bookings_ServiceDate.setTypeface(null, Typeface.BOLD);
            }
            else
            {
                RV_bookings_ServiceDate.setTypeface(null, Typeface.NORMAL);
            }

        }
    }
}
