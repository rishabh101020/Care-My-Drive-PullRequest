package com.cmdrj.caremydrive;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class AdapterRecVewWorkshops extends RecyclerView.Adapter<AdapterRecVewWorkshops.ViewHolder> {
    private ArrayList<RecVewService> newServices;
    private Activity activity;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseStorage fStorage = FirebaseStorage.getInstance();

    private RecVewCar itemCar = null;
    private RecVewBookings itemBooking = null;

    public AdapterRecVewWorkshops(Activity activity, ArrayList<RecVewService> newServices, RecVewCar itemCar) {

        this.activity = activity;
        this.newServices = newServices;
        this.itemCar = itemCar;
    }

    public AdapterRecVewWorkshops(Activity activity, ArrayList<RecVewService> newServices, RecVewBookings itemBooking) {

        this.activity = activity;
        this.newServices = newServices;
        this.itemBooking = itemBooking;
    }


    @NonNull
    @Override
    public AdapterRecVewWorkshops.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_vew_services, parent, false);
        return new AdapterRecVewWorkshops.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecVewWorkshops.ViewHolder holder, int position) {

        RecVewService center = newServices.get(position);

        holder.setData(center);
    }

    @Override
    public int getItemCount() {
        return newServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView RV_Services_ServiceName, RV_Services_Address, RV_Services_WorkshopNo, RV_Services_Rating, RV_Services_VewDet;
        private Button RV_Services_Service;
        private ImageView RV_Services_IV;
        RecVewService service;
        RecVewWorkshop workshop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            RV_Services_ServiceName = itemView.findViewById(R.id.RV_Services_ServiceName);
            RV_Services_Address = itemView.findViewById(R.id.RV_Services_Address);
            RV_Services_WorkshopNo = itemView.findViewById(R.id.RV_Services_WorkshopNo);
            RV_Services_Rating = itemView.findViewById(R.id.RV_Services_Rating);
            RV_Services_VewDet = itemView.findViewById(R.id.RV_Services_VewDet);
            RV_Services_Service = itemView.findViewById(R.id.RV_Services_Service);
            RV_Services_IV = itemView.findViewById(R.id.RV_Services_IV);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(itemBooking == null)
                    {
                        Intent intent = new Intent(v.getContext(), DetailsService.class);
//                        intent.putExtra("itemWorkshop", workshop);
                        intent.putExtra("parentActivity",0);
                        intent.putExtra("itemService",  service);
                        intent.putExtra("itemCar",  itemCar);
                        v.getContext().startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(v.getContext(), DetailsService.class);
                        intent.putExtra("itemService", service);
                        intent.putExtra("parentActivity",1);
                        intent.putExtra("itemBooking",itemBooking);
                        v.getContext().startActivity(intent);
                    }
                }
            });

//            if(itemCar == null)
//            {
//                RV_workshops_Service.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(v.getContext(), ChooseCar.class);
//                        intent.putExtra("itemWorkshop", workshop);
//                        intent.putExtra("itemService", itemService);
//                        intent.putExtra("parentActivity",  1);
//                        v.getContext().startActivity(intent);
//
//
//                        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                    }
//                });
//            }
//            else
//            {
            RV_Services_Service.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(itemBooking == null)
                        {

                            Intent intent = new Intent(v.getContext(), ConfirmBooking.class);
                            intent.putExtra("itemWorkshop", workshop);
                            intent.putExtra("itemService",  service);
                            intent.putExtra("itemCar",  itemCar);
                            v.getContext().startActivity(intent);

                            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                        else
                        {
                            RV_Services_Service.setEnabled(false);
                            service.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    RecVewWorkshop workshop = documentSnapshot.toObject(RecVewWorkshop.class);


                                    Map<String, Object> map1 = new HashMap<>();
                                    map1.put("id", itemBooking.getID());
                                    map1.put("ref", fStore.collection("Bookings").document(itemBooking.getID()));


                                    Map<String, Object> map = new HashMap<>();
                                    map.put("workShopRef",service.getWorkShopRef());
                                    map.put("serviceRef",fStore.collection("Workshops").document(workshop.getID()).collection("Services").document(service.getID()));
                                    map.put("status", 0);
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


                                    fStore.collection("Workshops").document(workshop.getID()).collection("requestedBookings").document(itemBooking.getID()).set(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            fStore.collection("Workshops").document(workshop.getID()).collection("Services").document(service.getID()).collection("requestedBookings").document(itemBooking.getID()).set(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    fStore.collection("Bookings").document(itemBooking.getID()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            activity.finish();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
//            }

        }

        public void setData(RecVewService center) {

            service = center;

            RV_Services_ServiceName.setText(service.getName());
            RV_Services_IV.setImageResource(R.drawable.ic_cars_repair_70_green);


            service.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    workshop = documentSnapshot.toObject(RecVewWorkshop.class);



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

                                        RV_Services_IV.setImageBitmap(bitmap);

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

                                        RV_Services_IV.setImageBitmap(bitmap);

                                    }
                                });
                            }
                        });
                    }





//                    RV_Services_Address.setText(documentSnapshot.getString("city") + ", " + documentSnapshot.getString("state"));
//                    RV_Services_WorkshopNo.setText(documentSnapshot.getString("mbNo"));


                    RV_Services_Address.setText(documentSnapshot.getString("name"));
                    RV_Services_WorkshopNo.setText(documentSnapshot.getString("city") + ", " + documentSnapshot.getString("state"));

                    if(workshop.getRating() != -1)
                    {
                        RV_Services_Rating.setVisibility(View.VISIBLE);
                        RV_Services_Rating.setText(Integer.toString(workshop.getRating()));
                    }
                    else
                        RV_Services_Rating.setVisibility(View.GONE);
                }
            });



            if(service.getPrice() != -1)
                RV_Services_VewDet.setText(Double.toString(service.getPrice()));
            else
                RV_Services_VewDet.setText("Variable");

        }
    }
}
