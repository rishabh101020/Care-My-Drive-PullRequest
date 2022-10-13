package com.cmdrj.caremydriveadmin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterGridVewIndividualServices extends BaseAdapter {

    Context context;
    ArrayList<RecVewService> services = new ArrayList<>();;
    boolean fabSelected;
    Drawable cardBack;

    LayoutInflater inflater;

    private FirebaseStorage fStorage;


    public AdapterGridVewIndividualServices(Context context, ArrayList<RecVewService> services, boolean fabSelected, Drawable cardBack) {
        this.context = context;
        this.services = services;
        this.fabSelected = fabSelected;
        this.cardBack = cardBack;

        fStorage = FirebaseStorage.getInstance();

    }

    @Override
    public int getCount() {
        return services.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(inflater == null)
        {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.grid_vew_individual_services,null);

        }

        ImageView GV_ind_services_IV = convertView.findViewById(R.id.GV_ind_services_IV);
        TextView GV_ind_services_TV = convertView.findViewById(R.id.GV_ind_services_TV);
        CardView GV_ind_services_CV = convertView.findViewById(R.id.GV_ind_services_CV);

        if(fabSelected)
        {
            if(services.get(position).getID().equals("new"))
            {
                GV_ind_services_CV.setBackground(cardBack);
            }
            else
            {
                if(services.get(position).getVisibility())
                {
                    GV_ind_services_CV.setBackground(convertView.getResources().getDrawable(R.drawable.dashboard_card_vew_background));
                }
                else
                {
                    GV_ind_services_CV.setBackground(null);
                }
            }
        }
        else
        {
            GV_ind_services_CV.setBackground(cardBack);
        }

        GV_ind_services_TV.setSelected(true);

        if(services.get(position).getID().equals("new"))
        {
            GV_ind_services_IV.setImageResource(R.drawable.ic_add_48_green);
        }
        else if(services.get(position).getPic() == null)
        {
            GV_ind_services_IV.setImageResource(R.drawable.ic_cars_repair_48_green);
        }
        else
        {

            StorageReference sReference = fStorage.getReference().child("Services").child(services.get(position).getID()).child("pic1.jpeg");

            sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                @Override
                public void onSuccess(StorageMetadata storageMetadata) {

                    sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {

                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                            GV_ind_services_IV.setImageBitmap(bitmap);
//                            editIndSer_PB.setVisibility(View.GONE);

                        }
                    });
                }
            });

        }
        GV_ind_services_TV.setText(services.get(position).getName());

        return convertView;
    }
}
