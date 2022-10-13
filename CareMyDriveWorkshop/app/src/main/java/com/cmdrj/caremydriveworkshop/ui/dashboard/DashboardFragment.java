package com.cmdrj.caremydriveworkshop.ui.dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmdrj.caremydriveworkshop.AddImages;
import com.cmdrj.caremydriveworkshop.CompServices;
import com.cmdrj.caremydriveworkshop.InitServices;
import com.cmdrj.caremydriveworkshop.LiveServices;
import com.cmdrj.caremydriveworkshop.R;
import com.cmdrj.caremydriveworkshop.RecVewBookings;
import com.cmdrj.caremydriveworkshop.RecVewService;
import com.cmdrj.caremydriveworkshop.RecVewWorkshop;
import com.cmdrj.caremydriveworkshop.ServicesProvided;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.List;

public class DashboardFragment extends Fragment {

    LinearLayout dash_initSer, dash_liveSer, dash_compSer, dash_serProv, dash_workshops_Rating_LL;
    TextView dash_WorkshopName, dash_workshops_Rating_Val, dash_initSer_TV2, dash_liveSer_TV2, dash_compSer_TV2, dash_serProv_TV2;
    ImageView dash_IV;
    FloatingActionButton dash_fab;

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;


    RecVewWorkshop workshop;

    View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();


        dash_initSer = root.findViewById(R.id.dash_initSer);
        dash_liveSer = root.findViewById(R.id.dash_liveSer);
        dash_compSer = root.findViewById(R.id.dash_compSer);
        dash_serProv = root.findViewById(R.id.dash_serProv);
        dash_workshops_Rating_LL = root.findViewById(R.id.dash_workshops_Rating_LL);

        dash_WorkshopName = root.findViewById(R.id.dash_WorkshopName);
        dash_workshops_Rating_Val = root.findViewById(R.id.dash_workshops_Rating_Val);
        dash_initSer_TV2 = root.findViewById(R.id.dash_initSer_TV2);
        dash_liveSer_TV2 = root.findViewById(R.id.dash_liveSer_TV2);
        dash_compSer_TV2 = root.findViewById(R.id.dash_compSer_TV2);
        dash_serProv_TV2 = root.findViewById(R.id.dash_serProv_TV2);

        dash_IV = root.findViewById(R.id.dash_IV);
        dash_fab = root.findViewById(R.id.dash_fab);


        DocumentReference dReference = fStore.collection("Workshops").document(fUser.getUid());

        dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(root, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {


                    workshop = value.toObject(RecVewWorkshop.class);

                    dash_IV.setImageResource(R.drawable.ic_workshop_100_green);

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

                                        dash_IV.setImageBitmap(bitmap);


                                    }
                                });
                            }
                        });

                    }




                    dash_WorkshopName.setText(workshop.getName());

                    if(workshop.getRating() != -1)
                    {
                        dash_workshops_Rating_LL.setVisibility(View.VISIBLE);
                        dash_workshops_Rating_Val.setText(Integer.toString(workshop.getRating()));
                    }
                    else
                        dash_workshops_Rating_LL.setVisibility(View.INVISIBLE);


                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        fStore.collection("Workshops").document(fUser.getUid()).collection("Services").whereEqualTo("visibility",true).whereEqualTo("disabled",false).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(root, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    List<RecVewService> snapshotList = value.toObjects(RecVewService.class);
                    dash_serProv_TV2.setText(Integer.toString(snapshotList.size()));

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        fStore.collection("Workshops").document(fUser.getUid()).collection("requestedBookings").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(root, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
                    dash_initSer_TV2.setText(Integer.toString(snapshotList.size()));


                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }

            }
        });


        fStore.collection("Workshops").document(fUser.getUid()).collection("acceptedBookings").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(root, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
                    dash_liveSer_TV2.setText(Integer.toString(snapshotList.size()));

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }



            }
        });

        fStore.collection("Workshops").document(fUser.getUid()).collection("completedBookings").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(root, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
                    dash_compSer_TV2.setText(Integer.toString(snapshotList.size()));

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        dash_initSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), InitServices.class);
                startActivity(intent);

            }
        });

        dash_liveSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), LiveServices.class);
                startActivity(intent);
            }
        });

        dash_compSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), CompServices.class);
                startActivity(intent);
            }
        });

        dash_serProv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ServicesProvided.class);
                startActivity(intent);

            }
        });

        dash_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fStore.collection("Workshops").document(fUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        RecVewWorkshop itemWorkshop = documentSnapshot.toObject(RecVewWorkshop.class);

                        Intent intent = new Intent(getContext(), AddImages.class);
                        intent.putExtra("itemWorkshop", itemWorkshop);
                        startActivity(intent);

                    }
                });
            }
        });

        return root;
    }
}