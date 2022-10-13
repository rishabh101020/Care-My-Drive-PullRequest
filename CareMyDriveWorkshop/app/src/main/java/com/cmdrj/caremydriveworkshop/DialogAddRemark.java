package com.cmdrj.caremydriveworkshop;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;

public class DialogAddRemark extends AppCompatDialogFragment {

//    TextInputEditText Dlg_add_remark_TIET;
//    Button Dlg_add_remark_Reject;
//    Button Dlg_add_remark_Accept;
//
//
//    public interface onInputListenerAR{
//        void sendInputAR(String s, int position);
//    }
//
//    private onInputListenerAR listener;
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//
//        try {
//            listener = (onInputListenerAR) getTargetFragment();
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() + " must implement onInputListenerAR interface");
//        }
//    }
//
//    //    /**
////     * dialogInterface - instance of AddRemarkDialogInterface which will handle
////     * callback events
////     */
////    public static DialogAddRemark getInstance(AddRemarkDialogInterface dialogInterface) {
////        DialogAddRemark dialogAddRemark = new DialogAddRemark();
////
////        // set fragment arguments
////        Bundle args = new Bundle();
////        args.putSerializable("dialogInterface", dialogInterface);
////        dialogAddRemark.setArguments(args);
////
////        return dialogAddRemark;
////    }
//
////    @Override
////    public void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View pushDialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_remark, null);
//
////        // get reference to AddRemarkDialogInterface instance from arguments
////        listener = (AddRemarkDialogInterface) getArguments().getSerializable("dialogInterface");
//
////        pushDialogView.
//        Dlg_add_remark_TIET = (TextInputEditText) pushDialogView.findViewById(R.id.Dlg_add_remark_TIET);
//        Dlg_add_remark_Reject = (Button) pushDialogView.findViewById(R.id.Dlg_add_remark_Reject);
//        Dlg_add_remark_Accept = (Button) pushDialogView.findViewById(R.id.Dlg_add_remark_Accept);
//
//        Dlg_add_remark_Accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getDialog().dismiss();
//            }
//        });
//
//        Dlg_add_remark_Accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String s = Dlg_add_remark_TIET.getText().toString();
//                if (s.trim().isEmpty())
//                    Toast.makeText(getContext(), "Please give some reasoning", Toast.LENGTH_SHORT).show();
////
//                else {
//                            listener.sendInputAR(s, position);
//                }
//
////                // send click event
////                listener.onClickEvent();
//            }
//        });
//
//        return pushDialogView;
//    }


    private TextInputEditText Dlg_add_remark_TIET;
    private onInputListenerAR listener;

    public interface onInputListenerAR {
        void sendInputAR(String s, RecVewBookings booking);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (onInputListenerAR) context;
        } catch (ClassCastException e) {

            try {
                listener = (onInputListenerAR) getTargetFragment();
            } catch (ClassCastException e1) {
                throw new ClassCastException(context.toString() + " must implement onInputListenerAR interface");
            }
        }


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Bundle bundleObj = getArguments();

        // TODO : if position received is 2 and if a new booking is requested during the time of writing of comments then rejection will be of 2nd only or it will be of 1st ( as now initialy booking at 1st position is at 2nd )
        RecVewBookings itemBooking = bundleObj.getParcelable("itemBooking");

        // TODO : over

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_remark, null);

        builder.setView(view).setTitle("Give Reason")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String s = Dlg_add_remark_TIET.getText().toString();
                        if (s.trim().isEmpty())
                            Toast.makeText(getContext(), "Please give some reasoning", Toast.LENGTH_SHORT).show();
//
                        else {
                            listener.sendInputAR(s, itemBooking);
                        }
                    }
                });

        Dlg_add_remark_TIET = view.findViewById(R.id.Dlg_add_remark_TIET);

        return builder.create();

    }
}
