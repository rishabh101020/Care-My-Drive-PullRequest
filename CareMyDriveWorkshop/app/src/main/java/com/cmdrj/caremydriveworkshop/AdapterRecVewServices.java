package com.cmdrj.caremydriveworkshop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterRecVewServices extends RecyclerView.Adapter<AdapterRecVewServices.ViewHolder> {

    private ArrayList<RecVewService> newServices;
    public boolean fabSelected;
    Drawable cardBack_green;
    Drawable cardBack_white;

    private FirebaseStorage fStorage;
    private String workshopID;


    public AdapterRecVewServices(ArrayList<RecVewService> newServices, boolean fabSelected, Drawable cardBack_green, Drawable cardBack_white, String workshopID) {
        this.newServices = newServices;
        this.fabSelected = fabSelected;
        this.cardBack_green = cardBack_green;
        this.cardBack_white = cardBack_white;

        this.workshopID = workshopID;

        fStorage = FirebaseStorage.getInstance();

    }


    @NonNull
    @Override
    public AdapterRecVewServices.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_vew_services, parent, false);
        return new AdapterRecVewServices.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecVewServices.ViewHolder holder, int position) {

        RecVewService job = newServices.get(position);

//        String bCarId = newCars.get(position).getCarID();
//        String bWorkshopId = newCars.get(position).getWorkShopID();
//        String bServiceId = newCars.get(position).getServiceID();

        holder.setData(job);
    }

    @Override
    public int getItemCount() {
        return newServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView RV_services_ServiceName, RV_services_Category, RV_services_EstTime, RV_services_Reject, RV_services_Rating;
        private Button RV_services_Accept;
        private ImageView RV_services_IV;
        private CardView RV_services_CV1;
        private RelativeLayout RV_services_RL3, RV_services_RL4, RV_services_RL5;
        RecVewService service;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            RV_services_ServiceName = itemView.findViewById(R.id.RV_services_ServiceName);
            RV_services_Category = itemView.findViewById(R.id.RV_services_Category);
            RV_services_EstTime = itemView.findViewById(R.id.RV_services_EstTime);
            RV_services_Reject = itemView.findViewById(R.id.RV_services_Reject);
            RV_services_Rating = itemView.findViewById(R.id.RV_services_Rating);

            RV_services_Accept = itemView.findViewById(R.id.RV_services_Accept);

            RV_services_IV = itemView.findViewById(R.id.RV_services_IV);

            RV_services_CV1 = itemView.findViewById(R.id.RV_services_CV1);

            RV_services_RL3 = itemView.findViewById(R.id.RV_services_RL3);
            RV_services_RL4 = itemView.findViewById(R.id.RV_services_RL4);
            RV_services_RL5 = itemView.findViewById(R.id.RV_services_RL5);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!fabSelected) {
                        Intent intent = new Intent(v.getContext(), DetailsService.class);
                        intent.putExtra("itemService", service);
//                    intent.putExtra("parentActivity",parentActivity);
//                    intent.putExtra("itemService",newService);
                        v.getContext().startActivity(intent);
                    } else {

                        if(getAdapterPosition() != -1)
                        {
                            if (newServices.get(getAdapterPosition()).getID().equals("new")) {

                                // TODO : when firebase startActivityForResult wala + add to     2nd last     as last will be "new"

                                Intent intent = new Intent(v.getContext(), EditDetService.class);
                                intent.putExtra("itemService",service);
                                v.getContext().startActivity(intent);

                                //TODO : over
                            }
//                            else {
//
//                                if(newServices.get(getAdapterPosition()).getDisabled())
//                                {
//                                    Snackbar.make(v, "Disabled By Admin.", Snackbar.LENGTH_SHORT).show();
//                                }
//                                else
//                                {
//                                    newServices.get(getAdapterPosition()).setVisibility(!newServices.get(getAdapterPosition()).getVisibility());
//                                }
//                                AdapterRecVewServices.this.notifyDataSetChanged();
//                            }
                        }
//                        else
//                        {
//
//                        }

                    }
                }
            });

            RV_services_Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!fabSelected) {
                        // TODO : start ActivityForResult() wala finish

                        Intent intent = new Intent(v.getContext(), EditDetService.class);
                        intent.putExtra("itemService", service);
                        v.getContext().startActivity(intent);

                        //Todo : over
                    } else {

                        if(getAdapterPosition() != -1)
                        {
                            if (newServices.get(getAdapterPosition()).getID().equals("new")) {

                                // TODO : when firebase startActivityForResult wala + add to     2nd last     as last will be "new"

                                Intent intent = new Intent(v.getContext(), EditDetService.class);
                                intent.putExtra("itemService",service);
                                v.getContext().startActivity(intent);

                                //TODO : over
                            } else {

                                if(! (newServices.get(getAdapterPosition()).getDisabled()))
                                {
                                    newServices.get(getAdapterPosition()).setVisibility(!newServices.get(getAdapterPosition()).getVisibility());
                                }
                                AdapterRecVewServices.this.notifyDataSetChanged();
                            }
                        }
//                        else
//                        {
//
//                        }

                    }
                }
            });

        }

        public void setData(RecVewService job) {

            service = job;

            if (fabSelected)
            {
                if (newServices.get(getAdapterPosition()).getID().equals("new")) {

                    RV_services_RL4.setVisibility(View.VISIBLE);
                    RV_services_RL5.setVisibility(View.GONE);

                    RV_services_CV1.setBackground(cardBack_green);

                }
                else
                {

                    RV_services_RL4.setVisibility(View.GONE);
                    RV_services_RL5.setVisibility(View.VISIBLE);

                    if(newServices.get(getAdapterPosition()).getDisabled())
                    {
                        RV_services_CV1.setBackground(cardBack_white);
                        RV_services_Accept.setEnabled(false);
                        RV_services_Accept.setText("Disabled");
                    }
                    else
                    {
                        if(newServices.get(getAdapterPosition()).getVisibility())
                        {
                            RV_services_CV1.setBackground(itemView.getResources().getDrawable(R.drawable.dashboard_card_vew_background));
                            RV_services_Accept.setEnabled(true);
                            RV_services_Accept.setText("Hide");
                        }
                        else
                        {
                            RV_services_CV1.setBackground(cardBack_white);
                            RV_services_Accept.setEnabled(true);
                            RV_services_Accept.setText("Show");
                        }
                    }

                    RV_services_ServiceName.setText(service.getName());

                    String categ = "";
//            Collections.sort(service.getCategory());
                    for (int i : service.getCategory()) {
                        if (i == 2)
                            categ = categ + ", Bikes";

                        else if (i == 4)
                            categ = categ + ", Cars";

                        else if (i == 6)
                            categ = categ + ", Heavy";

                        else if (i == 3)
                            categ = categ + ", Others";
                    }

                    categ = categ.substring(2);
                    RV_services_Category.setText(categ);

                    RV_services_EstTime.setText(service.getEstTime());

                    if (service.getPrice() == -1)
                        RV_services_Reject.setText("Variable");
                    else
                        RV_services_Reject.setText(Double.toString(service.getPrice()));

                    // TODO : complete while doing Rating

//            if(service.getRating() != -1)
//            {
//                RV_services_Rating.setVisibility(View.VISIBLE);
//                RV_services_Rating.setText(Integer.toString(service.getRating()));
//            }
//            else
//            {
                RV_services_Rating.setVisibility(View.GONE);
//            }

                    // TODO : over

                    RV_services_IV.setImageResource(R.drawable.ic_cars_repair_48_green);

                    if(service.getPic() != null)
                    {
                        StorageReference sReference = fStorage.getReference().child("Workshops").child(workshopID).child("Services").child(service.getID()).child("pic1.jpeg");

                        sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {

                                sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                        RV_services_IV.setImageBitmap(bitmap);

                                    }
                                });
                            }
                        });
                    }
                }
            }

            else
            {
                RV_services_RL4.setVisibility(View.GONE);
                RV_services_RL5.setVisibility(View.VISIBLE);

                RV_services_CV1.setBackground(cardBack_green);
                RV_services_Accept.setEnabled(true);
                RV_services_Accept.setText("Edit");

                RV_services_ServiceName.setText(service.getName());

                String categ = "";
//            Collections.sort(service.getCategory());
                for (int i : service.getCategory()) {
                    if (i == 2)
                        categ = categ + ", Bikes";

                    else if (i == 4)
                        categ = categ + ", Cars";

                    else if (i == 6)
                        categ = categ + ", Heavy";

                    else if (i == 3)
                        categ = categ + ", Others";
                }

                categ = categ.substring(2);
                RV_services_Category.setText(categ);

                RV_services_EstTime.setText(service.getEstTime());

                if (service.getPrice() == -1)
                    RV_services_Reject.setText("Variable");
                else
                    RV_services_Reject.setText(Double.toString(service.getPrice()));

                // TODO : complete while doing Rating

//            if(service.getRating() != -1)
//            {
//                RV_services_Rating.setVisibility(View.VISIBLE);
//                RV_services_Rating.setText(Integer.toString(service.getRating()));
//            }
//            else
//            {
                RV_services_Rating.setVisibility(View.GONE);
//            }

                // TODO : over

                RV_services_IV.setImageResource(R.drawable.ic_cars_repair_48_green);

                if(service.getPic() != null)
                {

                    StorageReference sReference = fStorage.getReference().child("Workshops").child(workshopID).child("Services").child(service.getID()).child("pic1.jpeg");

                    sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                        @Override
                        public void onSuccess(StorageMetadata storageMetadata) {

                            sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                    RV_services_IV.setImageBitmap(bitmap);

                                }
                            });
                        }
                    });
                }

            }

        }
    }
}
