package com.cmdrj.caremydriveadmin;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterRecVewCars extends RecyclerView.Adapter<AdapterRecVewCars.ViewHolder> {

    private ArrayList<RecVewCar> newCars;

    Drawable cardBack_green;
    Drawable cardBack_white;

    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;

    public AdapterRecVewCars(ArrayList<RecVewCar> newCars, Drawable cardBack_green, Drawable cardBack_white) {
        this.newCars = newCars;
        this.cardBack_green = cardBack_green;
        this.cardBack_white = cardBack_white;

        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

    }


    @NonNull
    @Override
    public AdapterRecVewCars.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_vew_cars, parent, false);
        return new AdapterRecVewCars.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecVewCars.ViewHolder holder, int position) {

        RecVewCar vehicle = newCars.get(position);

        holder.setData(vehicle);
    }

    @Override
    public int getItemCount() {
        return newCars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView RV_cars_Model, RV_cars_Manufacturer, RV_cars_CarNo;
        private Button RV_cars_Accept;
        private ImageView RV_cars_IV;
        private CardView RV_cars_CV1;
        RecVewCar car;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            RV_cars_Model = itemView.findViewById(R.id.RV_cars_Model);
            RV_cars_Manufacturer = itemView.findViewById(R.id.RV_cars_Manufacturer);
            RV_cars_CarNo = itemView.findViewById(R.id.RV_cars_CarNo);

            RV_cars_Accept = itemView.findViewById(R.id.RV_cars_Accept);

            RV_cars_IV = itemView.findViewById(R.id.RV_cars_IV);

            RV_cars_CV1 = itemView.findViewById(R.id.RV_cars_CV1);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), DetailsCar.class);
                    intent.putExtra("item", car);
//                    intent.putExtra("parentActivity",parentActivity);
//                    intent.putExtra("itemService",newService);
                    v.getContext().startActivity(intent);
                }
            });


            RV_cars_Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), ListBookings.class);
                    intent.putExtra("initBookings", fStore.collection("Vehicles").document(car.getID()).collection("requestedBookings").getPath());
                    intent.putExtra("liveBookings", fStore.collection("Vehicles").document(car.getID()).collection("acceptedBookings").getPath());
                    intent.putExtra("compBookings", fStore.collection("Vehicles").document(car.getID()).collection("completedBookings").getPath());
                    v.getContext().startActivity(intent);

                }
            });

        }

        public void setData(RecVewCar vehicles) {

            car = vehicles;

            if(car.getVisibility())
                RV_cars_CV1.setBackground(cardBack_green);
            else
                RV_cars_CV1.setBackground(cardBack_white);


            RV_cars_Model.setText(car.getModel());
            RV_cars_Manufacturer.setText(car.getManufacturer());
            RV_cars_CarNo.setText(car.getNo());


            if (car.getWheelType() == 2)
                RV_cars_IV.setImageResource(R.drawable.ic_bike_70_green);
            else if (car.getWheelType() == 4)
                RV_cars_IV.setImageResource(R.drawable.ic_cars_70_green);
            else if (car.getWheelType() == 6)
                RV_cars_IV.setImageResource(R.drawable.ic_heavy_70_green);
            else if (car.getWheelType() == 3)
                RV_cars_IV.setImageResource(R.drawable.ic_others_70_green);

            if(car.getPic() != null)
            {
                StorageReference sReference = fStorage.getReference().child("Vehicles").child(car.getID()).child("pic1.jpeg");

                sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {

                        sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {

                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                RV_cars_IV.setImageBitmap(bitmap);

                            }
                        });
                    }
                });
            }


        }
    }
}
