package com.example.a2family.Activities;


import android.app.ActivityOptions;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.applandeo.materialcalendarview.CalendarView;
import com.example.a2family.Classes.Event;
import com.example.a2family.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;

public class AddCalendarActivity extends BaseActivity {

    private CalendarView datePicker;
    private Button addEventButton;
    private EditText eventDescription;
    private ImageView deleteEvent;
    private EditText timePicker;
    private int Hour;
    private int Minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calendar);

        this.datePicker = (com.applandeo.materialcalendarview.CalendarView) findViewById(R.id.datePicker);
        this.addEventButton = (Button) findViewById(R.id.add_event);
        this.eventDescription = (EditText) findViewById(R.id.event_Description);
        this.timePicker = (EditText) findViewById(R.id.timePicker);
        //this.deleteEvent = (ImageView) findViewById(R.id.deleteEvent);

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { addNewEvent(); }
        });


        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                Hour = c.get(Calendar.HOUR_OF_DAY);
                Minute = c.get(Calendar.MINUTE);
                //nel momento in cui viene cliccata la textbox si apre il timepicker
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddCalendarActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Hour=hourOfDay;
                        Minute=minute;
                        //una volta scelta l'ora chiama Update text che formatta la stringa visualizzata nella textbox
                        UpdateText(hourOfDay, minute, timePicker);
                    }
                }, Hour, Minute, true);
                timePickerDialog.show();
            }
        });

    }

    private void UpdateText(int hourOfDay, int minute, EditText timePicker) {
        if(hourOfDay<10 && minute<10) {
            timePicker.setText("0" + Hour + ":0" + minute);

        }
        if(hourOfDay<10 && minute>10){
            timePicker.setText("0"+Hour+":"+minute);

        }
        if(hourOfDay>=10 && minute<10){
            timePicker.setText(Hour+":0"+minute);

        }
        if(hourOfDay>=10 && minute>=10) {
            timePicker.setText(Hour+":"+minute);

        }
    }

    private void addNewEvent() {
        //preleva la descrizione dell'evento
        String eventDescription = AddCalendarActivity.this.eventDescription.getText().toString();
        //salva in una variabile Calendar la data selezionata in cui aggiungere l'evento
        Calendar selectedDate= datePicker.getFirstSelectedDate();
        //crea un nuovo evento con i millisecondi corrispondenti alla data selezionata e la descrizione
        Event newEvent = new Event(selectedDate.getTimeInMillis(), eventDescription, Hour, Minute);
        int error_description = checkField(this.eventDescription.getText().toString(), this.eventDescription);
        int error_time = checkField(this.timePicker.getText().toString(), this.eventDescription);
        if(error_description==0 || error_time==0){
            return;
        }
        //pusha l'evento del database e ritorna alla pagina di visualizzazone del calendario
        String eventKey=String.valueOf(selectedDate.getTimeInMillis());
        firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("events").child(eventKey).push().setValue(newEvent).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddCalendarActivity.this, "Evento aggiunto con Successo !", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AddCalendarActivity.this, CalendarActivity.class), ActivityOptions.makeSceneTransitionAnimation(AddCalendarActivity.this).toBundle());
                    finish();
                }
            }
        });
    }
}