package com.cmdrj.caremydrive;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdapterRecVewPastBookings  extends RecyclerView.Adapter<AdapterRecVewPastBookings.ViewHolder> {

    private ArrayList<RecVewBookings> newAppli;
    public AdapterRecVewPastBookings(ArrayList<RecVewBookings> newAppli) {
        this.newAppli = newAppli;
    }

    @NonNull
    @Override
    public AdapterRecVewPastBookings.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_vew_bookings, parent, false);
        return new AdapterRecVewPastBookings.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecVewPastBookings.ViewHolder holder, int position) {

        RecVewBookings appointment = newAppli.get(position);

        holder.setData(appointment);
    }

    @Override
    public int getItemCount() {
        return newAppli.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView RV_bookings_CarName, RV_bookings_ServiceName, RV_bookings_ServiceDate, RV_bookings_Price, RV_bookings_Rating, RV_bookings_Status;
        private Button RV_bookings_VewDet, RV_bookings_Contact;
        private RelativeLayout RV_bookings_RV2;
        RecVewBookings bookings;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            RV_bookings_CarName = itemView.findViewById(R.id.RV_bookings_CarName);
            RV_bookings_ServiceName = itemView.findViewById(R.id.RV_bookings_ServiceName);
            RV_bookings_ServiceDate = itemView.findViewById(R.id.RV_bookings_ServiceDate);
            RV_bookings_Status = itemView.findViewById(R.id.RV_bookings_Status);
            RV_bookings_VewDet = itemView.findViewById(R.id.RV_bookings_VewDet);
            RV_bookings_Contact = itemView.findViewById(R.id.RV_bookings_Contact);
            RV_bookings_RV2 = itemView.findViewById(R.id.RV_bookings_RV2);
            RV_bookings_Rating = itemView.findViewById(R.id.RV_bookings_Rating);
            RV_bookings_Price = itemView.findViewById(R.id.RV_bookings_Price);



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

                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Choose Contact Option")
                            .setMessage("Choose an application to contact Admin")
                            .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", v.getContext().getResources().getString(R.string.admin_Email), null));
//                    if (intent.resolveActivity(getPackageManager()) != null) {
                                    v.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
//                    }

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

            bookings.getServiceRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    RV_bookings_ServiceName.setText(documentSnapshot.getString("name"));

                }
            });

//            RV_bookings_WorkshopName.setText(newWorkshop.get(Integer.parseInt(bWorkshopId.substring(4, bWorkshopId.length()))).getName());

            String pattern = "dd-MM-yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            RV_bookings_ServiceDate.setText(df.format(bookings.getServiceDate().toDate()));

            if(bookings.getPrice() == -1)
            {
                RV_bookings_Price.setText("Variable");
            }
            else
            {
                RV_bookings_Price.setText(Double.toString(bookings.getPrice()));
            }

            if(bookings.getRating() == -1)
            {
                RV_bookings_VewDet.setVisibility(View.INVISIBLE);
                RV_bookings_Rating.setVisibility(View.INVISIBLE);
            }

            else if(bookings.getRating() != -1)
            {
                RV_bookings_VewDet.setVisibility(View.INVISIBLE);
                RV_bookings_Rating.setVisibility(View.VISIBLE);
                RV_bookings_Rating.setText(Integer.toString(bookings.getRating()));

            }


            RV_bookings_Status.setVisibility(View.GONE);

            if (RV_bookings_RV2.getVisibility() == View.GONE)
                RV_bookings_RV2.setVisibility(View.VISIBLE);

            if (RV_bookings_Price.getVisibility() == View.GONE)
                RV_bookings_Price.setVisibility(View.VISIBLE);
        }
    }
}
