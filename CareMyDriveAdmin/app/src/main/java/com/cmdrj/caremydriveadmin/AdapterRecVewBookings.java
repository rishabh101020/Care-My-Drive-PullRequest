package com.cmdrj.caremydriveadmin;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdapterRecVewBookings extends RecyclerView.Adapter<AdapterRecVewBookings.ViewHolder> {

    private ArrayList<RecVewBookings> newAppli;
    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;

    Activity activity;

//    public AdapterRecVewBookings(ArrayList<RecVewBookings> newAppli) {
//        this.newAppli = newAppli;
//        fStore = FirebaseFirestore.getInstance();
//        fStorage = FirebaseStorage.getInstance();
//
//    }

    public AdapterRecVewBookings(ArrayList<RecVewBookings> newAppli, Activity activity) {

        this.activity = activity;
        this.newAppli = newAppli;
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

    }

    @NonNull
    @Override
    public AdapterRecVewBookings.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_vew_bookings, parent, false);
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


    public class ViewHolder extends RecyclerView.ViewHolder {


        private CardView RV_bookings_CV1;
        private TextView RV_bookings_CarModel, RV_bookings_ServiceName, RV_bookings_ServiceDate, RV_bookings_Rating, RV_bookings_Reject, RV_bookings_RejectionReason;
        private Button RV_bookings_Accept;
        private ImageView RV_bookings_IV;
        private RelativeLayout RV_bookings_RL3;

        RecVewBookings booking;
        RecVewCar car;
        RecVewService parentService;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            RV_bookings_CV1 = itemView.findViewById(R.id.RV_bookings_CV1);
            RV_bookings_CarModel = itemView.findViewById(R.id.RV_bookings_CarModel);
            RV_bookings_ServiceName = itemView.findViewById(R.id.RV_bookings_ServiceName);
            RV_bookings_ServiceDate = itemView.findViewById(R.id.RV_bookings_ServiceDate);
            RV_bookings_Rating = itemView.findViewById(R.id.RV_bookings_Rating);
            RV_bookings_Reject = itemView.findViewById(R.id.RV_bookings_Reject);
            RV_bookings_RejectionReason = itemView.findViewById(R.id.RV_bookings_RejectionReason);

            RV_bookings_RL3 = itemView.findViewById(R.id.RV_bookings_RL3);

            RV_bookings_Accept = itemView.findViewById(R.id.RV_bookings_Accept);

            RV_bookings_IV = itemView.findViewById(R.id.RV_bookings_IV);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), DetailsBooking.class);
                    intent.putExtra("itemBooking", booking);
//                    intent.putExtra("parentActivity",parentActivity);
//                    intent.putExtra("itemWorkshop",newWorkshop);
                    v.getContext().startActivity(intent);
                }
            });

            RV_bookings_Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(booking.getStatus() == -1)
                    {

                        booking.getParentServiceRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                RecVewService itemParentService = documentSnapshot.toObject(RecVewService.class);

                                fStore.collection("Bookings").document(booking.getID()).collection("rejectedBy").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                                        ArrayList<String> rejectedWorkshops = new ArrayList<>();
                                        ArrayList<String> rejectedServices = new ArrayList<>();

                                        for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots )
                                        {
                                            rejectedWorkshops.add(queryDocumentSnapshot.getString("workShopId"));
                                            rejectedServices.add(queryDocumentSnapshot.getString("serviceId"));
                                        }


                                        Intent intent = new Intent(v.getContext(), ChooseWorkshop.class);
                                        intent.putExtra("itemService",itemParentService);
                                        intent.putExtra("itemBooking",booking);
                                        intent.putStringArrayListExtra("rejectedWorkshops",rejectedWorkshops);
                                        intent.putStringArrayListExtra("rejectedServices",rejectedServices);
                                        v.getContext().startActivity(intent);
                                        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                                    }
                                });
                            }
                        });

                    }
                    else if( booking.getStatus() == 3)
                    {

                        Intent intent = new Intent(v.getContext(), ViewImage.class);
                        intent.putExtra("path", fStorage.getReference().child("Bookings").child(booking.getID()).child("invoice.jpeg").getPath());
                        v.getContext().startActivity(intent);


                        // TODO : start ActivityForResult() wala finish

//                    Intent intent = new Intent(v.getContext(), EditDetService.class);
//                    intent.putExtra("itemService", service);
//                    v.getContext().startActivity(intent);

                        //Todo : over
                    }
                    else
                    {
                        booking.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

//            RV_bookings_Reject.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });


        }

        public void setData(RecVewBookings jobCard) {

            booking = jobCard;

            booking.getVehicleRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    car = documentSnapshot.toObject(RecVewCar.class);
                    RV_bookings_CarModel.setText(car.getModel() + ", " + car.getManufacturer());
                }
            });

            if(booking.getStatus() == -1)
            {
                booking.getParentServiceRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        parentService = documentSnapshot.toObject(RecVewService.class);
                        RV_bookings_ServiceName.setText(parentService.getName());


                        fStore.collection("Bookings").document(booking.getID()).collection("rejectedBy").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                                ArrayList<String> rejectedWorkshops = new ArrayList<>();
                                ArrayList<String> rejectedServices = new ArrayList<>();

                                for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots )
                                {
                                    rejectedWorkshops.add(queryDocumentSnapshot.getString("workShopId"));
                                    rejectedServices.add(queryDocumentSnapshot.getString("serviceId"));
                                }

                                RV_bookings_Accept.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if(booking.getStatus() == -1)
                                        {
                                            Intent intent = new Intent(v.getContext(), ChooseWorkshop.class);
                                            intent.putExtra("itemService",parentService);
                                            intent.putExtra("itemBooking",booking);
                                            intent.putStringArrayListExtra("rejectedWorkshops",rejectedWorkshops);
                                            intent.putStringArrayListExtra("rejectedServices",rejectedServices);
                                            v.getContext().startActivity(intent);
                                            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                        }
                                        else if( booking.getStatus() == 3)
                                        {
                                            // TODO : start ActivityForResult() wala finish

//                    Intent intent = new Intent(v.getContext(), EditDetService.class);
//                    intent.putExtra("itemService", service);
//                    v.getContext().startActivity(intent);

                                            //Todo : over
                                        }
                                        else
                                        {
                                            booking.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                            }
                        });



                    }
                });

                booking.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String rejectingWorkshop = documentSnapshot.getString("id");

                        fStore.collection("Bookings").document(booking.getID()).collection("rejectedBy").document(rejectingWorkshop).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                RV_bookings_RejectionReason.setText("Reason: " + documentSnapshot.getString("reason"));

                                RV_bookings_RejectionReason.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                });

                RV_bookings_Reject.setVisibility(View.INVISIBLE);
                RV_bookings_Accept.setText("Assign");

            }
            else
            {
                RV_bookings_RejectionReason.setVisibility(View.GONE);
                RV_bookings_Accept.setText("Contact");

                booking.getServiceRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        RV_bookings_ServiceName.setText(documentSnapshot.getString("name"));

                    }
                });

                if(booking.getPrice() != -1)
                    RV_bookings_Reject.setText(Double.toString(booking.getPrice()));
                else
                    RV_bookings_Reject.setText("Variable");
            }


            String pattern = "dd-MM-yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            RV_bookings_ServiceDate.setText(df.format(booking.getServiceDate().toDate()));





            if (booking.getStatus() == 0) {

                RV_bookings_CarModel.setTypeface(null, Typeface.NORMAL);
                RV_bookings_ServiceName.setTypeface(null, Typeface.NORMAL);
                RV_bookings_ServiceDate.setTypeface(null, Typeface.NORMAL);
                RV_bookings_Reject.setTypeface(null, Typeface.NORMAL);


                RV_bookings_Accept.setText("Contact");
            } else if (booking.getStatus() == 1) {

//                RV_bookings_CV1.setCardBackgroundColor(itemView.getResources().getColor(R.color.green_100));
                RV_bookings_CarModel.setTypeface(null, Typeface.NORMAL);
                RV_bookings_ServiceName.setTypeface(null, Typeface.NORMAL);
                RV_bookings_ServiceDate.setTypeface(null, Typeface.NORMAL);
                RV_bookings_Reject.setTypeface(null, Typeface.NORMAL);


                RV_bookings_Accept.setText("Contact");
            } else if (booking.getStatus() == 2) {

//                RV_bookings_CV1.setBackground(itemView.getResources().getDrawable(R.drawable.dashboard_card_vew_background));
                RV_bookings_CarModel.setTypeface(null, Typeface.BOLD);
                RV_bookings_ServiceName.setTypeface(null, Typeface.BOLD);
                RV_bookings_ServiceDate.setTypeface(null, Typeface.BOLD);
                RV_bookings_Reject.setTypeface(null, Typeface.BOLD);


                RV_bookings_Accept.setText("Contact");
            } else if (booking.getStatus() == 3) {

                RV_bookings_CarModel.setTypeface(null, Typeface.NORMAL);
                RV_bookings_ServiceName.setTypeface(null, Typeface.NORMAL);
                RV_bookings_ServiceDate.setTypeface(null, Typeface.NORMAL);
                RV_bookings_Reject.setTypeface(null, Typeface.NORMAL);


                RV_bookings_Accept.setText("Invoice");
            }
            else if (booking.getStatus() == -2) {

//                RV_bookings_CV1.setBackground(itemView.getResources().getDrawable(R.drawable.dashboard_card_vew_background));
                RV_bookings_CarModel.setTypeface(null, Typeface.NORMAL);
                RV_bookings_ServiceName.setTypeface(null, Typeface.NORMAL);
                RV_bookings_ServiceDate.setTypeface(null, Typeface.BOLD);
                RV_bookings_Reject.setTypeface(null, Typeface.NORMAL);


                RV_bookings_Accept.setText("Contact");
            }
            else if (booking.getStatus() == -3) {

//                RV_bookings_CV1.setBackground(itemView.getResources().getDrawable(R.drawable.dashboard_card_vew_background));
                RV_bookings_CarModel.setTypeface(null, Typeface.NORMAL);
                RV_bookings_ServiceName.setTypeface(null, Typeface.BOLD);
                RV_bookings_ServiceDate.setTypeface(null, Typeface.NORMAL);
                RV_bookings_Reject.setTypeface(null, Typeface.NORMAL);


                RV_bookings_Accept.setText("Contact");
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
