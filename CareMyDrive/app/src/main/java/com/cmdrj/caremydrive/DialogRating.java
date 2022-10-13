package com.cmdrj.caremydrive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;

public class DialogRating extends AppCompatDialogFragment {

    private NumberPicker Dlg_rating_NP;
    private onInputListenerR listener;
    int newRating = -1;

    public interface onInputListenerR {
        void sendInputR(int rating, RecVewBookings booking);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (onInputListenerR) context;
        } catch (ClassCastException e) {

            try {
                listener = (onInputListenerR) getTargetFragment();
            } catch (ClassCastException e1) {
                throw new ClassCastException(context.toString() + " must implement onInputListenerR interface");
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
        View view = inflater.inflate(R.layout.dialog_rating, null);

        builder.setView(view).setTitle("Rate: ")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        String s = Dlg_add_remark_TIET.getText().toString();
                        if (newRating == -1)
                            Toast.makeText(getContext(), "Please give some rating", Toast.LENGTH_SHORT).show();
//
                        else {
                            listener.sendInputR(newRating, itemBooking);
                        }
                    }
                });

        Dlg_rating_NP = view.findViewById(R.id.Dlg_rating_NP);

        Dlg_rating_NP.setMinValue(1);
        Dlg_rating_NP.setMaxValue(5);

        newRating = Dlg_rating_NP.getValue();

        Dlg_rating_NP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                newRating = newVal;
            }
        });
        return builder.create();

    }
}
