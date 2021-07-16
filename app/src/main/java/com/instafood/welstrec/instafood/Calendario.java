package com.instafood.welstrec.instafood;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Calendario extends AppCompatActivity {

    MaterialCalendarView calendarView;
    TextView myDate;
    Button botonObtenerFechas;
    Button botonEliminarFechas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        calendarView=(MaterialCalendarView)findViewById(R.id.calendarView);

        botonObtenerFechas=(Button)findViewById(R.id.botonObtenerFechas);
        botonEliminarFechas= (Button)findViewById(R.id.botonBorrarFechas);

        myDate= (TextView) findViewById(R.id.myDate);
        cargarFechas();
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(2010, 1, 1))
                .setMaximumDate(CalendarDay.from(2020, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        calendarView.setTopbarVisible(true);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                myDate.setText(""+date.getDay()+"/"+date.getMonth()+"/"+date.getYear());
            }
        });
        botonEliminarFechas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.clearSelection();
                SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("words", null);
                editor.commit();
            }
        });
        botonObtenerFechas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List fechas= calendarView.getSelectedDates();

                String fe="";

                ArrayList<String> fechasString= new ArrayList<String>();
                for (int i=0; i< fechas.size();i++)
                {
                    CalendarDay p= (CalendarDay) fechas.get(i);
                    fechasString.add(p.getDay()+"-"+p.getMonth()+"-"+p.getYear());
                }
                StringBuilder stringBuilder= new StringBuilder();
                for(String s:fechasString)
                {
                    stringBuilder.append(s);
                    stringBuilder.append(",");
                }
                //--------------------------------------------------
                SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("words", stringBuilder.toString());
                editor.commit();
                Toast.makeText(getApplicationContext(),"Se ha guardado sus fechas",Toast.LENGTH_LONG).show();
                Intent i= new Intent(Calendario.this, Main2Activity.class);
                startActivity(i);
            }
        });


    }

    private void cargarFechas() {

        SharedPreferences settings= getSharedPreferences("PREFS", MODE_PRIVATE);
        String wordsString= settings.getString("words", "");
        String[] itemsWords= wordsString.split(",");
        if(wordsString!=null && !wordsString.isEmpty())
        {
            ArrayList<String> items= new ArrayList<>();
            for(int i=0; i<itemsWords.length;i++)
            {
                String []fechax= itemsWords[i].split("-");
                Date dia= new Date();
                dia.setDate(Integer.parseInt(fechax[0]));
                dia.setMonth(Integer.parseInt(fechax[1]));
                dia.setYear(Integer.parseInt(fechax[2]));
                Calendar p= Calendar.getInstance() ;
                p.set(Integer.parseInt(fechax[2]), Integer.parseInt(fechax[1]), Integer.parseInt(fechax[0]));
                CalendarDay diax= CalendarDay.from(p);
                CalendarDay diaCal= new CalendarDay(Integer.parseInt(fechax[2]), Integer.parseInt(fechax[1]), Integer.parseInt(fechax[0]));
                items.add(itemsWords[i]);
                calendarView.setDateSelected(diaCal,true);
            }
        }

    }

}
