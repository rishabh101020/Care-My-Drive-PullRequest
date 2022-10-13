package com.cmdrj.caremydriveadmin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class DialogShowDetails extends AppCompatDialogFragment {

    private TextInputEditText Dlg_show_details_TIET;

    private FirebaseFirestore fStore;








//    private onInputListenerSD listener;
//
//    public interface onInputListenerSD {
//        void sendInputSD(String s);
//    }
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//
//
//        try{
//            listener = (onInputListenerSD) getTargetFragment();
//        } catch (ClassCastException e1){
//            throw new ClassCastException(context.toString() + " must implement onInputListenerAR interface");
//        }
//
//
//    }












    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Bundle bundleObj = getArguments();
        final RecVewService[] service = {bundleObj.getParcelable("itemService")};

        fStore = FirebaseFirestore.getInstance();




        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_show_details, null);

        // TODO : edit when firebase

        builder.setView(view).setTitle(service[0].getName())
        //TODO : over
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(getContext(), EditIndividualService.class);
                        intent.putExtra("itemService", service[0]);
                        startActivity(intent);







//                        String s = Dlg_show_details_TIET.getText().toString();
//                        if (s.trim().isEmpty())
//                            Toast.makeText(getContext(), "Please give some reasoning", Toast.LENGTH_SHORT).show();
//                        else {
//                            listener.sendInputSD(s);
//                        }










                    }
                });

        Dlg_show_details_TIET = view.findViewById(R.id.Dlg_show_details_TIET);


        DocumentReference dReference = fStore.collection("Services").document(service[0].getID());
        dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Snackbar.make(Dlg_show_details_TIET, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {
                    service[0] = value.toObject(RecVewService.class);

                    Dlg_show_details_TIET.setText(service[0].getDetails());

                }
                else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }

            }
        });



        return builder.create();

    }
}
