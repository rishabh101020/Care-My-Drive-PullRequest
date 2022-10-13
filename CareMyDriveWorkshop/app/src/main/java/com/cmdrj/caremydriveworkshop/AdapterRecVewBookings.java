package com.cmdrj.caremydriveworkshop;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class AdapterRecVewBookings extends RecyclerView.Adapter<AdapterRecVewBookings.ViewHolder> {

//    private Context context;


    private ArrayList<RecVewBookings> newAppli;

    //    DialogAddRemark.AddRemarkDialogInterface addRemarkDialogInterface;
    Fragment fragmentInitiated;
    FragmentManager fragmentManager;


    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;


    public AdapterRecVewBookings(ArrayList<RecVewBookings> newAppli, Fragment fragmentInitiated, FragmentManager fragmentManager) {
        this.newAppli = newAppli;
        this.fragmentInitiated = fragmentInitiated;
        this.fragmentManager = fragmentManager;


        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
    }

    public AdapterRecVewBookings(ArrayList<RecVewBookings> newAppli) {
        this.newAppli = newAppli;

        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

    }


    @NonNull
    @Override
    public AdapterRecVewBookings.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_vew_bookings, parent, false);
//        context = parent.getContext();
        return new AdapterRecVewBookings.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecVewBookings.ViewHolder holder, int position) {

        RecVewBookings jobCard = newAppli.get(position);

        holder.setData(jobCard);
    }

    @Override
    public int getItemCount() {
        return newAppli.size();
    }

//    @Override
//    public void sendInputAR(String s, int position) {
//
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView RV_bookings_CarModel, RV_bookings_CarNo, RV_bookings_ServiceDate, RV_bookings_Rating, RV_bookings_Price;
        private Button RV_bookings_Accept, RV_bookings_Reject;
        private ImageView RV_bookings_IV, RV_bookings_Reschedule;
        private RelativeLayout RV_bookings_RL3;

        RecVewBookings booking;
        DocumentReference dReference;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            RV_bookings_CarModel = itemView.findViewById(R.id.RV_bookings_CarModel);
            RV_bookings_CarNo = itemView.findViewById(R.id.RV_bookings_CarNo);
            RV_bookings_ServiceDate = itemView.findViewById(R.id.RV_bookings_ServiceDate);
            RV_bookings_Rating = itemView.findViewById(R.id.RV_bookings_Rating);
            RV_bookings_Price = itemView.findViewById(R.id.RV_bookings_Price);

            RV_bookings_RL3 = itemView.findViewById(R.id.RV_bookings_RL3);

            RV_bookings_Accept = itemView.findViewById(R.id.RV_bookings_Accept);
            RV_bookings_Reject = itemView.findViewById(R.id.RV_bookings_Reject);
            RV_bookings_Reschedule = itemView.findViewById(R.id.RV_bookings_Reschedule);

            RV_bookings_IV = itemView.findViewById(R.id.RV_bookings_IV);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), DetailsBooking.class);
                    intent.putExtra("itemBooking", booking);
//                    intent.putExtra("parentActivity",parentActivity);
//                    intent.putExtra("itemService",newService);
                    v.getContext().startActivity(intent);
                }
            });

            RV_bookings_Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (booking.getStatus() == 0) {

                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("status", 1);

                        Map<String, Object> map2 = new HashMap<>();
                        map2.put("id", booking.getID());
                        map2.put("ref", dReference);

                        dReference.update(map1)               ;
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {

                                        booking.getClientRef().collection("requestedBookings").document(booking.getID()).delete();
                                        booking.getClientRef().collection("acceptedBookings").document(booking.getID()).set(map2);

                                        booking.getWorkShopRef().collection("requestedBookings").document(booking.getID()).delete();
                                        booking.getWorkShopRef().collection("acceptedBookings").document(booking.getID()).set(map2);

                                        booking.getServiceRef().collection("requestedBookings").document(booking.getID()).delete();
                                        booking.getServiceRef().collection("acceptedBookings").document(booking.getID()).set(map2);

                                        booking.getParentServiceRef().collection("requestedBookings").document(booking.getID()).delete();
                                        booking.getParentServiceRef().collection("acceptedBookings").document(booking.getID()).set(map2);

                                        booking.getVehicleRef().collection("requestedBookings").document(booking.getID()).delete();
                                        booking.getVehicleRef().collection("acceptedBookings").document(booking.getID()).set(map2);

//                                    }
//                                });

                    } else if (booking.getStatus() == 1) {





                        booking.getClientRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                RecVewClient itemClient = documentSnapshot.toObject(RecVewClient.class);

                                new AlertDialog.Builder(v.getContext())
                                        .setTitle("Choose Contact Option")
                                        .setMessage("Choose an application to contact Client")
                                        .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", itemClient.getEmail(), null));
//                                if (intent.resolveActivity(getPackageManager()) != null) {
                                                v.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
//                                }

                                            }
                                        })
                                        .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", itemClient.getMbNo(), null));
//                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                                v.getContext().startActivity(phoneIntent);
//                                        }

                                            }
                                        })
                                        .create().show();

                            }
                        });







//                        Bundle bundle = new Bundle();
//                        bundle.putInt("parentActivity",0);
//                        bundle.putParcelable("itemBooking", booking);
//
//                        DialogUploadImage dialogUploadImage = new DialogUploadImage();
//                        dialogUploadImage.setTargetFragment(fragmentInitiated, 2);
//                        dialogUploadImage.setArguments(bundle);
//                        dialogUploadImage.show(fragmentManager, "Upload job Card");
//
//
//
//
//
//
//
//
////                        Map<String, Object> map1 = new HashMap<>();
////                        map1.put("status", 2);
////
////                        dReference.update(map1)
////                                .addOnSuccessListener(new OnSuccessListener<Void>() {
////                                    @Override
////                                    public void onSuccess(Void aVoid) {
//
////                                        booking.getClientRef().collection("acceptedBookings").document(booking.getID()).update(map1);
////
////                                        booking.getWorkShopRef().collection("acceptedBookings").document(booking.getID()).update(map1);
////
////                                        booking.getServiceRef().collection("acceptedBookings").document(booking.getID()).update(map1);
////
////                                        booking.getParentServiceRef().collection("acceptedBookings").document(booking.getID()).update(map1);
////
////                                        booking.getVehicleRef().collection("acceptedBookings").document(booking.getID()).update(map1);
//
////                                    }
////                                });

                    } else if (booking.getStatus() == 2) {




                        booking.getClientRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                RecVewClient itemClient = documentSnapshot.toObject(RecVewClient.class);

                                new AlertDialog.Builder(v.getContext())
                                        .setTitle("Choose Contact Option")
                                        .setMessage("Choose an application to contact Client")
                                        .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", itemClient.getEmail(), null));
//                                if (intent.resolveActivity(getPackageManager()) != null) {
                                                v.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
//                                }

                                            }
                                        })
                                        .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", itemClient.getMbNo(), null));
//                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                                v.getContext().startActivity(phoneIntent);
//                                        }

                                            }
                                        })
                                        .create().show();

                            }
                        });







//                        Bundle bundle = new Bundle();
//                        bundle.putInt("parentActivity",1);
//                        bundle.putParcelable("itemBooking", booking);
//
//                        DialogUploadImage dialogUploadImage = new DialogUploadImage();
//                        dialogUploadImage.setTargetFragment(fragmentInitiated, 4);
//                        dialogUploadImage.setArguments(bundle);
//                        dialogUploadImage.show(fragmentManager, "Upload Invoice");
//
//
//
//
////                        Map<String, Object> map1 = new HashMap<>();
////                        map1.put("status", 3);
////
////                        Map<String, Object> map2 = new HashMap<>();
////                        map2.put("id", booking.getID());
////                        map2.put("ref", dReference);
////
////                        dReference.update(map1)
////                                .addOnSuccessListener(new OnSuccessListener<Void>() {
////                                    @Override
////                                    public void onSuccess(Void aVoid) {
////
////                                        booking.getClientRef().collection("acceptedBookings").document(booking.getID()).delete();
////                                        booking.getClientRef().collection("completedBookings").document(booking.getID()).set(map2);
////
////                                        booking.getWorkShopRef().collection("acceptedBookings").document(booking.getID()).delete();
////                                        booking.getWorkShopRef().collection("completedBookings").document(booking.getID()).set(map2);
////
////                                        booking.getServiceRef().collection("acceptedBookings").document(booking.getID()).delete();
////                                        booking.getServiceRef().collection("completedBookings").document(booking.getID()).set(map2);
////
////                                        booking.getParentServiceRef().collection("acceptedBookings").document(booking.getID()).delete();
////                                        booking.getParentServiceRef().collection("completedBookings").document(booking.getID()).set(map2);
////
////                                        booking.getVehicleRef().collection("acceptedBookings").document(booking.getID()).delete();
////                                        booking.getVehicleRef().collection("completedBookings").document(booking.getID()).set(map2);
////
////                                    }
////                                });

                    } else if (booking.getStatus() == 3) {
                        // TODO : start ActivityForResult() wala finish


                        Intent intent = new Intent(v.getContext(), ViewImage.class);
                        intent.putExtra("path", fStorage.getReference().child("Bookings").child(booking.getID()).child("invoice.jpeg").getPath());
                        v.getContext().startActivity(intent);

//                    Intent intent = new Intent(v.getContext(), EditDetService.class);
//                    intent.putExtra("itemService", service);
//                    v.getContext().startActivity(intent);

                        //Todo : over

                    }
                    else if (booking.getStatus() == -2)
                    {



                        booking.getClientRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                RecVewClient itemClient = documentSnapshot.toObject(RecVewClient.class);

                                new AlertDialog.Builder(v.getContext())
                                        .setTitle("Choose Contact Option")
                                        .setMessage("Choose an application to contact Client")
                                        .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", itemClient.getEmail(), null));
//                                if (intent.resolveActivity(getPackageManager()) != null) {
                                                v.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
//                                }

                                            }
                                        })
                                        .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", itemClient.getMbNo(), null));
//                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                                v.getContext().startActivity(phoneIntent);
//                                        }

                                            }
                                        })
                                        .create().show();

                            }
                        });







//                        Intent intent = new Intent(v.getContext(), DetailsBooking.class);
//                        intent.putExtra("itemBooking", booking);
////                    intent.putExtra("parentActivity",parentActivity);
////                    intent.putExtra("itemService",newService);
//                        v.getContext().startActivity(intent);
                    }
                }
            });

            RV_bookings_Reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    DialogAddRemark dialog = new DialogAddRemark();             /*DialogAddRemark.getInstance(addRemarkDialogInterface);*/
//                    dialogAddRemark.setTargetFragment(fragmentInitiated,1);
//                    dialog.show(((FragmentActivity)context).getSupportFragmentManager(), "Give Reason");

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("itemBooking", booking);

                    DialogAddRemark dialogAddRemark = new DialogAddRemark();
                    dialogAddRemark.setTargetFragment(fragmentInitiated, 1);
                    dialogAddRemark.setArguments(bundle);
                    dialogAddRemark.show(fragmentManager, "Give Reason");
                }
            });

            RV_bookings_Reschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    long timestamp = booking.getServiceDate().toDate().getTime();
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
                            map.put("status", -2);


                            fStore.collection("Bookings").document(booking.getID()).update(map);

                        }
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.setCancelable(false);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();

                }
            });


        }

        public void setData(RecVewBookings jobCard) {

            booking = jobCard;

            dReference = fStore.collection("Bookings").document(booking.getID());

            booking.getVehicleRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    RV_bookings_CarModel.setText(documentSnapshot.getString("model") + ", " + documentSnapshot.getString("manufacturer"));
                    RV_bookings_CarNo.setText(documentSnapshot.getString("no"));
                }
            });


            String pattern = "dd-MM-yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            RV_bookings_ServiceDate.setText(df.format(booking.getServiceDate().toDate()));

//            for (RecVewService i : newWorkshop.get(Integer.parseInt(bWorkshopId.substring(4, bWorkshopId.length()))).getServicesProvided()) {
//                if (i.getID().equals(bServiceId)) {
//                    RV_bookings_ServiceDate.setText(i.getName());
//
//                    break;
//                }
//            }

            if(booking.getPrice() != -1)
                RV_bookings_Price.setText(Double.toString(booking.getPrice()));
            else
                RV_bookings_Price.setText("Variable");

            if (booking.getStatus() == 0) {
                RV_bookings_Reject.setVisibility(View.VISIBLE);
                RV_bookings_Reschedule.setVisibility(View.VISIBLE);
                RV_bookings_Price.setVisibility(View.GONE);
                RV_bookings_Accept.setText("Accept");
            } else if(booking.getStatus() == -2){
                RV_bookings_Reject.setVisibility(View.GONE);
                RV_bookings_Reschedule.setVisibility(View.GONE);
                RV_bookings_Price.setVisibility(View.INVISIBLE);
                RV_bookings_Accept.setText("Contact");

            } else if (booking.getStatus() == 1) {

                RV_bookings_Reject.setVisibility(View.GONE);
                RV_bookings_Reschedule.setVisibility(View.GONE);
                RV_bookings_Price.setVisibility(View.VISIBLE);
                RV_bookings_Accept.setText("Contact");
            } else if (booking.getStatus() == 2) {

                RV_bookings_Reject.setVisibility(View.GONE);
                RV_bookings_Reschedule.setVisibility(View.GONE);
                RV_bookings_Price.setVisibility(View.VISIBLE);
                RV_bookings_Accept.setText("Contact");
            } else if (booking.getStatus() == 3) {
                RV_bookings_Reject.setVisibility(View.GONE);
                RV_bookings_Reschedule.setVisibility(View.GONE);
                RV_bookings_Price.setVisibility(View.VISIBLE);
                RV_bookings_Accept.setText("Invoice");
            }

            if (booking.getStatus() == 3 && booking.getRating() != -1) {
                RV_bookings_RL3.setVisibility(View.VISIBLE);
                RV_bookings_Rating.setVisibility(View.VISIBLE);
                RV_bookings_Rating.setText(Integer.toString(booking.getRating()));
            } else {
                RV_bookings_RL3.setVisibility(View.GONE);
                RV_bookings_Rating.setVisibility(View.GONE);

            }

//            RV_bookings_IV.setImageResource(R.drawable.ic_cars_repair_48_green);

        }
    }
}
