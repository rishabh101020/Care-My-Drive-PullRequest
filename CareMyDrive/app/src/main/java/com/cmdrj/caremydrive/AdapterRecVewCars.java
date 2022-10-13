package com.cmdrj.caremydrive;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterRecVewCars extends RecyclerView.Adapter<AdapterRecVewCars.ViewHolder> {

    private ArrayList<RecVewCar> newCars;
    private int parentActivity;
    //    private RecVewWorkshop newWorkshop;
    private RecVewService newService;
    private Activity activity;


    public boolean fabSelected;
    Drawable cardBack_green;
    Drawable cardBack_white;

    private FirebaseStorage fStorage;

//    private FirebaseAuth fAuth;
//    private FirebaseUser fUser;
//    private FirebaseFirestore fStore;


    public AdapterRecVewCars(Activity activity, ArrayList<RecVewCar> newCars, int parentActivity, RecVewService newService, boolean fabSelected, Drawable cardBack_green, Drawable cardBack_white) {
        this.activity = activity;
        this.newCars = newCars;
        this.parentActivity = parentActivity;
//        this.newWorkshop = newWorkshop;
        this.newService = newService;


        this.fabSelected = fabSelected;
        this.cardBack_green = cardBack_green;
        this.cardBack_white = cardBack_white;

        fStorage = FirebaseStorage.getInstance();

//        fAuth = FirebaseAuth.getInstance();
//        fUser = fAuth.getCurrentUser();
//        fStore = FirebaseFirestore.getInstance();


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

//        String bCarId = newCars.get(position).getCarID();
//        String bWorkshopId = newCars.get(position).getWorkShopID();
//        String bServiceId = newCars.get(position).getServiceID();

        holder.setData(vehicle);
    }

    @Override
    public int getItemCount() {
        return newCars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView RV_cars_CarName, RV_cars_CarNo, RV_cars_Model;
        private Button RV_cars_Service;
        private ImageView RV_cars_IV;
        private CardView RV_cars_CV1;
        private RelativeLayout RV_cars_RL5, RV_cars_RL4;

        RecVewCar cars;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            RV_cars_CarName = itemView.findViewById(R.id.RV_cars_CarName);
            RV_cars_CarNo = itemView.findViewById(R.id.RV_cars_CarNo);
            RV_cars_Model = itemView.findViewById(R.id.RV_cars_Model);
//            RV_cars_VewDet = itemView.findViewById(R.id.RV_cars_VewDet);
            RV_cars_Service = itemView.findViewById(R.id.RV_cars_Service);
            RV_cars_IV = itemView.findViewById(R.id.RV_cars_IV);

            RV_cars_CV1 = itemView.findViewById(R.id.RV_cars_CV1);

            RV_cars_RL5 = itemView.findViewById(R.id.RV_cars_RL5);
            RV_cars_RL4 = itemView.findViewById(R.id.RV_cars_RL4);

            if (parentActivity == 1) {
//                RV_cars_VewDet.setVisibility(View.INVISIBLE);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), DetailsCars.class);
                        intent.putExtra("item", cars);
                        intent.putExtra("parentActivity", parentActivity);
                        intent.putExtra("itemService", newService);
                        v.getContext().startActivity(intent);
                    }
                });


                RV_cars_Service.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        Intent intent = new Intent(v.getContext(),ConfirmBooking.class);
//                        intent.putExtra("itemWorkshop",newWorkshop);
//                        intent.putExtra("itemService",newService);
//                        intent.putExtra("itemCar",cars);
//                        v.getContext().startActivity(intent);

                        Intent intent = new Intent(v.getContext(), ChooseWorkshop.class);
//                        intent.putExtra("itemWorkshop",newWorkshop);
                        intent.putExtra("itemService", newService);
                        intent.putExtra("itemCar", cars);
                        v.getContext().startActivity(intent);

                        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });
            } else if (parentActivity == 0) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!fabSelected) {
                            Intent intent = new Intent(v.getContext(), DetailsCars.class);
                            intent.putExtra("item", cars);
                            intent.putExtra("parentActivity", parentActivity);
                            intent.putExtra("itemService", newService);
                            v.getContext().startActivity(intent);
                        } else {
                            if (getAdapterPosition() != -1) {

                                if (newCars.get(getAdapterPosition()).getID().equals("new")) {

                                    // TODO : when firebase startActivityForResult wala + add to     2nd last     as last will be "new"

                                    Intent intent = new Intent(v.getContext(), EditDetCar.class);
                                    intent.putExtra("itemCar",cars);
                                    v.getContext().startActivity(intent);

                                    //TODO : over
                                }
// //                               else {

//                                Map<String, Object> map = new HashMap<>();
//                                map.put("visibility", !newCars.get(getAdapterPosition()).getVisibility());
//
//                                fStore.collection("Vehicles").document(newCars.get(getAdapterPosition()).getID()).update(map)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//
//                                                fStore.collection("Clients").document(fUser.getUid()).collection("Vehicles").document(newCars.get(getAdapterPosition()).getID()).update(map);
//                                            }
//                                        });




// //                                   newCars.get(getAdapterPosition()).setVisibility(!newCars.get(getAdapterPosition()).getVisibility());
// //                                   AdapterRecVewCars.this.notifyDataSetChanged();
// //                               }
                            }
//                            else
//                            {
//                                for(RecVewCar z: newCars)
//                                {
//                                    Log.d("TAG","whoytrfwre + error" + z.getID());
//
//                                }
//
//                                Log.d("TAG","whoytrfwre + error ----------------------------------" + newCars.get(getAdapterPosition()));
//
//                            }
                        }
                    }
                });

//                RV_cars_VewDet.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(v.getContext(), ServicesCars.class);
//                        intent.putExtra("item", cars);
//                        v.getContext().startActivity(intent);
//                    }
//                });

                RV_cars_Service.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!fabSelected) {

                            Intent intent = new Intent(v.getContext(), EditDetCar.class);
                            intent.putExtra("itemCar",  cars);
                            v.getContext().startActivity(intent);

                        } else {

                            if (getAdapterPosition() != -1) {

                                if (newCars.get(getAdapterPosition()).getID().equals("new")) {

                                    // TODO : when firebase startActivityForResult wala + add to     2nd last     as last will be "new"

                                    Intent intent = new Intent(v.getContext(), EditDetCar.class);
                                    intent.putExtra("itemCar", cars);
                                    v.getContext().startActivity(intent);

                                    //TODO : over
                                } else {

//                                Map<String, Object> map = new HashMap<>();
//                                map.put("visibility", !newCars.get(getAdapterPosition()).getVisibility());

//                                fStore.collection("Vehicles").document(newCars.get(getAdapterPosition()).getID()).update(map)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//
//                                                fStore.collection("Clients").document(fUser.getUid()).collection("Vehicles").document(newCars.get(getAdapterPosition()).getID()).update(map);
//                                            }
//                                        });

                                    newCars.get(getAdapterPosition()).setVisibility(!newCars.get(getAdapterPosition()).getVisibility());
                                    AdapterRecVewCars.this.notifyDataSetChanged();
                                }
                            }
//                            else
//                            {
//                                for(RecVewCar z: newCars)
//                                {
//                                    Log.d("TAG","whoytrfwre + error" + z.getID());
//
//                                }
//
//                                Log.d("TAG","whoytrfwre + error ----------------------------------" + newCars.get(getAdapterPosition()));
//
//                            }
                        }
                    }
                });
            }

        }

        public void setData(RecVewCar vehicles) {

            cars = vehicles;

            if (fabSelected) {
                if (newCars.get(getAdapterPosition()).getID().equals("new")) {

                    RV_cars_RL4.setVisibility(View.VISIBLE);
                    RV_cars_RL5.setVisibility(View.GONE);

                    RV_cars_CV1.setBackground(cardBack_green);

                }
                else
                {
                    RV_cars_RL4.setVisibility(View.GONE);
                    RV_cars_RL5.setVisibility(View.VISIBLE);
//                    RV_cars_VewDet.setVisibility(View.INVISIBLE);

                    if (newCars.get(getAdapterPosition()).getVisibility()) {
                        RV_cars_CV1.setBackground(itemView.getResources().getDrawable(R.drawable.dashboard_card_vew_background));
                        RV_cars_Service.setText("Delete");

                    } else {
                        RV_cars_CV1.setBackground(cardBack_white);
                        RV_cars_Service.setText("Restore");

                    }

                    RV_cars_CarName.setText(cars.getName());
                    RV_cars_CarNo.setText(cars.getNo());
                    RV_cars_Model.setText(cars.getModel() + ", " + cars.getManufacturer());

//            Log.d("TAG", "Hre 1"+ cars.getWheelType());


                    if (cars.getWheelType() == 2)
                        RV_cars_IV.setImageResource(R.drawable.ic_bike_70_green);
                    else if (cars.getWheelType() == 4)
                        RV_cars_IV.setImageResource(R.drawable.ic_cars_70_green);
                    else if (cars.getWheelType() == 6)
                        RV_cars_IV.setImageResource(R.drawable.ic_heavy_70_green);
                    else if (cars.getWheelType() == 3)
                        RV_cars_IV.setImageResource(R.drawable.ic_others_70_green);

                    if(cars.getPic() != null)
                    {
                        StorageReference sReference = fStorage.getReference().child("Vehicles").child(cars.getID()).child("pic1.jpeg");

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
            else
            {
                RV_cars_RL4.setVisibility(View.GONE);
                RV_cars_RL5.setVisibility(View.VISIBLE);

                if (parentActivity == 1)
                {
                    RV_cars_Service.setText("Service");
//                    RV_cars_VewDet.setVisibility(View.VISIBLE);
                }
                else
                {
                    RV_cars_Service.setText("Edit");
                }
                RV_cars_CV1.setBackground(cardBack_green);

                RV_cars_CarName.setText(cars.getName());
                RV_cars_CarNo.setText(cars.getNo());
                RV_cars_Model.setText(cars.getModel() + ", " + cars.getManufacturer());

//            Log.d("TAG", "Hre 1"+ cars.getWheelType());

                if (cars.getWheelType() == 2)
                    RV_cars_IV.setImageResource(R.drawable.ic_bike_70_green);
                else if (cars.getWheelType() == 4)
                    RV_cars_IV.setImageResource(R.drawable.ic_cars_70_green);
                else if (cars.getWheelType() == 6)
                    RV_cars_IV.setImageResource(R.drawable.ic_heavy_70_green);
                else if (cars.getWheelType() == 3)
                    RV_cars_IV.setImageResource(R.drawable.ic_others_70_green);

                if(cars.getPic() != null)
                {
                    StorageReference sReference = fStorage.getReference().child("Vehicles").child(cars.getID()).child("pic1.jpeg");

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
}
