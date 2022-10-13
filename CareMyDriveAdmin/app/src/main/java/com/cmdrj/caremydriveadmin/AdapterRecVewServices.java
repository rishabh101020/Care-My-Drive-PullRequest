package com.cmdrj.caremydriveadmin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterRecVewServices extends RecyclerView.Adapter<AdapterRecVewServices.ViewHolder> {

    private ArrayList<RecVewService> newServices;
    RecVewBookings itemBooking = null;
    FirebaseFirestore fStore;
    private Activity activity;
    FirebaseStorage fStorage;




    Drawable cardBack_green;
    Drawable cardBack_white;

    public AdapterRecVewServices(ArrayList<RecVewService> newServices, Drawable cardBack_green, Drawable cardBack_white) {
        this.newServices = newServices;
        this.cardBack_green = cardBack_green;
        this.cardBack_white = cardBack_white;
        fStorage = FirebaseStorage.getInstance();

    }

    public AdapterRecVewServices(Activity activity, ArrayList<RecVewService> newServices, RecVewBookings itemBooking) {

        this.activity = activity;
        this.newServices = newServices;
        this.itemBooking = itemBooking;
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
    }


    @NonNull
    @Override
    public com.cmdrj.caremydriveadmin.AdapterRecVewServices.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_vew_services, parent, false);
        return new com.cmdrj.caremydriveadmin.AdapterRecVewServices.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.cmdrj.caremydriveadmin.AdapterRecVewServices.ViewHolder holder, int position) {

        RecVewService job = newServices.get(position);

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


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(itemBooking == null)
                    {
                        Intent intent = new Intent(v.getContext(), DetailsWorkshopService.class);
                        intent.putExtra("itemService", service);
                        intent.putExtra("parentActivity",0);
//                    intent.putExtra("itemService",newService);
                        v.getContext().startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(v.getContext(), DetailsWorkshopService.class);
                        intent.putExtra("itemService", service);
                        intent.putExtra("parentActivity",1);
                        intent.putExtra("itemBooking",itemBooking);
                        v.getContext().startActivity(intent);
                    }
                }
            });

            RV_services_Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(itemBooking == null)
                    {
                        Intent intent = new Intent(v.getContext(), ListBookings.class);
                        intent.putExtra("initBookings", service.getWorkShopRef().collection("Services").document(service.getID()).collection("requestedBookings").getPath());
                        intent.putExtra("liveBookings", service.getWorkShopRef().collection("Services").document(service.getID()).collection("acceptedBookings").getPath());
                        intent.putExtra("compBookings", service.getWorkShopRef().collection("Services").document(service.getID()).collection("completedBookings").getPath());
                        v.getContext().startActivity(intent);
                    }
                    else
                    {
                        RV_services_Accept.setEnabled(false);
                        service.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                RecVewWorkshop workshop = documentSnapshot.toObject(RecVewWorkshop.class);

                                Map<String, Object> map = new HashMap<>();
                                map.put("workShopRef",service.getWorkShopRef());
                                map.put("serviceRef",fStore.collection("Workshops").document(workshop.getID()).collection("Services").document(service.getID()));
                                map.put("status", -3);
                                map.put("details",service.getDetails());
                                map.put("price",service.getPrice());

                                if(service.getPickUp() == 0)
                                {
                                    map.put("pickUp", 0);
                                    map.put("pickUpAddress", null);
                                    map.put("pickUpCity", null);
                                    map.put("pickUpCountry", null);
                                    map.put("pickUpGeoPoint", null);
                                    map.put("pickUpPincode", null);
                                    map.put("pickUpState", null);
                                }
                                else
                                {

                                    if(itemBooking.getPickUp() == 0)
                                    {
                                        map.put("pickUp", 0);
                                        map.put("pickUpAddress", null);
                                        map.put("pickUpCity", null);
                                        map.put("pickUpCountry", null);
                                        map.put("pickUpGeoPoint", null);
                                        map.put("pickUpPincode", null);
                                        map.put("pickUpState", null);
                                    }

                                }

                                fStore.collection("Bookings").document(itemBooking.getID()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        activity.finish();
                                    }
                                });
                            }
                        });




                    }


                }
            });


        }

        public void setData(RecVewService job) {

            service = job;

            RV_services_IV.setImageResource(R.drawable.ic_cars_repair_70_green);

            if(itemBooking == null)
            {
                if(service.getVisibility() && !service.getDisabled())
                    RV_services_CV1.setBackground(cardBack_green);
                else
                    RV_services_CV1.setBackground(cardBack_white);
            }

            if(itemBooking == null)
            {
                RV_services_Accept.setText("Bookings");


                service.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        RecVewWorkshop workshop = documentSnapshot.toObject(RecVewWorkshop.class);

                        if(service.getPic() != null)
                        {
                            StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("Services").child(service.getID()).child("pic1.jpeg");

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
                });


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


            }
            else
            {
                RV_services_Accept.setText("Book");


                service.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        RecVewWorkshop workshop = documentSnapshot.toObject(RecVewWorkshop.class);

                        if(service.getPic() != null)
                        {
                            StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("Services").child(service.getID()).child("pic1.jpeg");

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
                        else if(workshop.getPic() != null)
                        {
                            StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("pic1.jpeg");

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

                        RV_services_Category.setText(workshop.getCity() + ", " + workshop.getState());

                        RV_services_EstTime.setText(workshop.getMbNo());
                    }
                });
            }

            RV_services_ServiceName.setText(service.getName());

            if (service.getPrice() == -1)
                RV_services_Reject.setText("Variable");
            else
                RV_services_Reject.setText(Double.toString(service.getPrice()));

            // TODO : complete while doing Rating

//            if(service.getRating() != -1)
//                RV_services_Rating.setText(Integer.toString(service.getRating()));
//            else
                RV_services_Rating.setVisibility(View.GONE);

            // TODO : over

        }
    }
}
