package com.cmdrj.caremydriveadmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterRecVewUsers extends RecyclerView.Adapter<AdapterRecVewUsers.ViewHolder> {

    private ArrayList<RecVewClient> newClients = null;
    private FirebaseFirestore fStore;
    FirebaseStorage fStorage;


    public AdapterRecVewUsers(ArrayList<RecVewClient> newClients, int useLessButNecessary) {
        this.newClients = newClients;
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
    }

    private ArrayList<RecVewWorkshop> newWorkshops = null;

    public AdapterRecVewUsers(ArrayList<RecVewWorkshop> newWorkshops) {
        this.newWorkshops = newWorkshops;
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public AdapterRecVewUsers.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_vew_users, parent, false);
        return new AdapterRecVewUsers.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecVewUsers.ViewHolder holder, int position) {

        if (newWorkshops == null) {
            RecVewClient person = newClients.get(position);
            holder.setData(person);
        } else if (newClients == null) {
            RecVewWorkshop office = newWorkshops.get(position);
            holder.setData(office);
        }

    }

    @Override
    public int getItemCount() {
        if (newWorkshops == null) {
            return newClients.size();
        } else if (newClients == null) {
            return newWorkshops.size();
        }
        return -1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView RV_users_UserName, RV_users_UserNo, RV_users_UserAdd, RV_users_Rating;
        private Button RV_users_Reject, RV_users_Accept;
        private ImageView RV_users_IV;
        private RelativeLayout RV_users_RL3;

        RecVewClient client = null;
        RecVewWorkshop workshop = null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            RV_users_UserName = itemView.findViewById(R.id.RV_users_UserName);
            RV_users_UserNo = itemView.findViewById(R.id.RV_users_UserNo);
            RV_users_UserAdd = itemView.findViewById(R.id.RV_users_UserAdd);


            RV_users_Rating = itemView.findViewById(R.id.RV_users_Rating);


            RV_users_RL3 = itemView.findViewById(R.id.RV_users_RL3);

            RV_users_Reject = itemView.findViewById(R.id.RV_users_Reject);
            RV_users_Accept = itemView.findViewById(R.id.RV_users_Accept);

            RV_users_IV = itemView.findViewById(R.id.RV_users_IV);


            if (newClients == null) {

                RV_users_IV.setImageResource(R.drawable.ic_workshop_70_green);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), DetailsWorkshop.class);
                        intent.putExtra("itemWorkshop", workshop);
////                    intent.putExtra("parentActivity",parentActivity);
////                    intent.putExtra("itemService",newService);
                        v.getContext().startActivity(intent);
                    }
                });

                RV_users_Accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), ListBookings.class);
                        intent.putExtra("initBookings", fStore.collection("Workshops").document(workshop.getID()).collection("requestedBookings").getPath());
                        intent.putExtra("liveBookings", fStore.collection("Workshops").document(workshop.getID()).collection("acceptedBookings").getPath());
                        intent.putExtra("compBookings", fStore.collection("Workshops").document(workshop.getID()).collection("completedBookings").getPath());
                        v.getContext().startActivity(intent);
                    }
                });

                RV_users_Reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Choose Contact Option")
                                .setMessage("Choose an application to contact Workshop")
                                .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", workshop.getEmail(), null));
//                                if (intent.resolveActivity(getPackageManager()) != null) {
                                        v.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
//                                }

                                    }
                                })
                                .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", workshop.getMbNo(), null));
//                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                        v.getContext().startActivity(phoneIntent);
//                                        }

                                    }
                                })
                                .create().show();

                    }
                });
            }
            else if (newWorkshops == null)
            {
                RV_users_IV.setImageResource(R.drawable.ic_person_70_green);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), DetailsClient.class);
                        intent.putExtra("itemClient", client);
////                    intent.putExtra("parentActivity",parentActivity);
////                    intent.putExtra("itemService",newService);
                        v.getContext().startActivity(intent);
                    }
                });

                RV_users_Accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        Intent intent = new Intent(v.getContext(), ListBookings.class);

                        intent.putExtra("initBookings", fStore.collection("Clients").document(client.getID()).collection("requestedBookings").getPath());
                        intent.putExtra("liveBookings", fStore.collection("Clients").document(client.getID()).collection("acceptedBookings").getPath());
                        intent.putExtra("compBookings", fStore.collection("Clients").document(client.getID()).collection("completedBookings").getPath());

//                intent.putExtra("initBookingsDocument", false);
//                intent.putExtra("liveBookingsDocument", false);
//                intent.putExtra("compBookingDocuments", false);


                        v.getContext().startActivity(intent);

                    }
                });

                RV_users_Reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Choose Contact Option")
                                .setMessage("Choose an application to contact Client")
                                .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", client.getEmail(), null));
//                                if (intent.resolveActivity(getPackageManager()) != null) {
                                        v.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
//                                }

                                    }
                                })
                                .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", client.getMbNo(), null));
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

        public void setData(RecVewClient person) {

            client = person;

            RV_users_UserName.setText(client.getName());
            RV_users_UserNo.setText(client.getMbNo());
            RV_users_UserAdd.setText(client.getCity() + ", " + client.getState());

            RV_users_Rating.setVisibility(View.GONE);
            RV_users_RL3.setVisibility(View.GONE);


            if(client.getPic() != null)
            {
                StorageReference sReference = fStorage.getReference().child("Clients").child(client.getID()).child("pic1.jpeg");

                sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {

                        sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {

                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                RV_users_IV.setImageBitmap(bitmap);

                            }
                        });
                    }
                });
            }

//            if (newClients == null) {
//
////                RV_users_IV.setImageResource(R.drawable.ic_workshop_48_green);
//            }
//            else if (newWorkshops == null) {
//
////                RV_users_IV.setImageResource(R.drawable.ic_client_48);
//            }
        }

        public void setData(RecVewWorkshop office) {

            workshop = office;

            RV_users_UserName.setText(workshop.getName());
            RV_users_UserNo.setText(workshop.getMbNo());
            RV_users_UserAdd.setText(workshop.getCity() + ", " + workshop.getState());



            if(workshop.getPic() != null)
            {
                StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("pic1.jpeg");

                sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {

                        sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {

                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                RV_users_IV.setImageBitmap(bitmap);

                            }
                        });
                    }
                });
            }



            if(workshop.getRating() != -1)
            {
                RV_users_Rating.setVisibility(View.VISIBLE);
                RV_users_RL3.setVisibility(View.VISIBLE);
                RV_users_Rating.setText(Integer.toString(workshop.getRating()));
            }
            else
            {
                RV_users_Rating.setVisibility(View.GONE);
                RV_users_RL3.setVisibility(View.GONE);
            }
        }
    }

}
