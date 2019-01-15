package com.full.wasah.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.full.wasah.R;
import com.full.wasah.Util.Turno;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TurnoAdapter extends RecyclerView.Adapter<TurnoAdapter.MyViewHolder> {

    Context context;
    ArrayList<Turno> turnos;
    private int lastSelectedPosition = -1;
    public OnItemCheckListener mCallback;
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal = Calendar.getInstance();
    Calendar time = Calendar.getInstance();

    public TurnoAdapter(Context context, ArrayList<Turno> turnos) {
        this.context = context;
        this.turnos = turnos;
    }

    public TurnoAdapter(Context context, ArrayList<Turno> turnos, OnItemCheckListener mCallback) {
        this.context = context;
        this.turnos = turnos;
        this.mCallback = mCallback;
    }

    public interface OnItemCheckListener {
        void onItemCheck(String item);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_turno, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.radioButton.setChecked(lastSelectedPosition == i);

        Date date = null;
        try {
            date = timeFormat.parse(turnos.get(i).getHora());
            time.setTime(dateFormat.parse(turnos.get(i).getFecha()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);


        if(Calendar.getInstance().get(Calendar.DAY_OF_YEAR)==(time.get(Calendar.DAY_OF_YEAR))){
        if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)>= cal.get(Calendar.HOUR_OF_DAY)){
            myViewHolder.radioButton.setEnabled(false);
        }else if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)== cal.get(Calendar.HOUR_OF_DAY) && Calendar.getInstance().get(Calendar.MINUTE)> cal.get(Calendar.MINUTE)){}else{
            if(turnos.get(i).getEstado().equals("reservado")){
                myViewHolder.radioButton.setChecked(true);
                myViewHolder.radioButton.setEnabled(false);
            }else{
                myViewHolder.radioButton.setEnabled(true);
            }
        }
        }else{
            if(turnos.get(i).getEstado().equals("reservado")){
                myViewHolder.radioButton.setChecked(true);
                myViewHolder.radioButton.setEnabled(false);
            }else{
                myViewHolder.radioButton.setEnabled(true);
            }
        }
        myViewHolder.radioButton.setText(turnos.get(i).getHora());
    }

    @Override
    public int getItemCount() {
        return turnos.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        RadioButton radioButton;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        radioButton = itemView.findViewById(R.id.radioButton);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            lastSelectedPosition = getAdapterPosition();
            mCallback.onItemCheck(turnos.get(lastSelectedPosition).getHora());
            notifyDataSetChanged();

            }
        });
    }
}

}
