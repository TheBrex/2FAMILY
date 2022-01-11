package com.example.a2family.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.a2family.Fragment.DeleteEventFragment;
import com.example.a2family.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends BaseActivity {

    private List<EventDay> events = new ArrayList<EventDay>();
    private ArrayList<String> eventDescription = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private FloatingActionButton editEvent;
    private CalendarView calendarView;
    private ListView listView;
    private TextView dateSelectedLabel;
    public static Calendar c;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //richiamo i fragment per i submenu
        bottMenu();
        bottoMenu();

        this.editEvent = (FloatingActionButton) findViewById(R.id.eventbutton);
        this.calendarView = (CalendarView) findViewById(R.id.calendarView);
        this.listView = (ListView) findViewById(R.id.event_list);
        this.dateSelectedLabel=(TextView) findViewById(R.id.date_selected_label);



        //inizializzo l'array di stringhe dove gli elementi hanno il layot definito in list_item.xml
        initializeCalendar(getFamilyIdFromFile());
        this.adapter=new ArrayAdapter<>(this, R.layout.list_event, R.id.description_event ,this.eventDescription);

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                c = eventDay.getCalendar();
                dateSelectedLabel.setText("Data : " + c.get(Calendar.DAY_OF_MONTH) + "-"+ (c.get(Calendar.MONTH)+1) +"-"+c.get(Calendar.YEAR));
                dateSelectedLabel.setVisibility(View.VISIBLE);

                eventDescription.clear();
                adapter.notifyDataSetChanged();
                //Caricare nella listView gli eventi relativi a quel giorno

                String eventKey = String.valueOf(eventDay.getCalendar().getTimeInMillis());
                firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("events").child(eventKey).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        //aggiorno la listview
                        String eventHour = String.valueOf(snapshot.child("hour").getValue(Integer.class));
                        String eventMinute = String.valueOf(snapshot.child("minute").getValue(Integer.class));
                        String eventDesc = snapshot.child("eventDescription").getValue(String.class);

                        if(eventHour.length()<2) eventHour="0"+eventHour;
                        if(eventMinute.length()<2) eventMinute="0"+eventMinute;
                        eventDescription.add(eventHour+":"+eventMinute + " --- "+ eventDesc);
                        adapter.notifyDataSetChanged();

                        //reinizializzo graficamente il calendario
                        initializeCalendar(getFamilyIdFromFile());

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        String eventHour = String.valueOf(snapshot.child("hour").getValue(Integer.class));
                        String eventMinute = String.valueOf(snapshot.child("minute").getValue(Integer.class));
                        String eventDesc = snapshot.child("eventDescription").getValue(String.class);
                        if(eventHour.length()<2) eventHour="0"+eventHour;
                        if(eventMinute.length()<2) eventMinute="0"+eventMinute;
                        eventDescription.remove(eventHour+":"+eventMinute + " --- "+ eventDesc);
                        adapter.notifyDataSetChanged();
                        initializeCalendar(getFamilyIdFromFile());

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                listView.setAdapter(adapter);
            }
        });

        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EditCalendar = new Intent(CalendarActivity.this, AddCalendarActivity.class);
                startActivity(EditCalendar, ActivityOptions.makeSceneTransitionAnimation(CalendarActivity.this).toBundle());
                finish();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DeleteEventFragment deleteEventFragment = new DeleteEventFragment();

                String item=(String) parent.getItemAtPosition(position);
                //TODO: tagliare la stringa in modo da recuperare orario e descrizione
                String hour= getHourFromItem(item);
                String minute= getMinuteFromItem(item);
                String description= getDescriptionFromItem(item);

                //devo passare al bottom sheet fragment l'adapter, la posizione dell'elemento cliccato, e l'array di event description
                Bundle bundle = new Bundle();
                bundle.putString("item_toDelete", description );
                bundle.putInt("position", position);
                bundle.putString("familyId", getFamilyIdFromFile());
                bundle.putString("minute", minute);
                bundle.putString("hour", hour);
                deleteEventFragment.setArguments(bundle);
                deleteEventFragment.show(getSupportFragmentManager(), "TAG");

            }
        });



    }

    private String getDescriptionFromItem(String item) {

        int index=item.lastIndexOf("-");
        String subString = item.substring(index+2);
        //System.out.println(subString);
        return subString;
    }

    private String getMinuteFromItem(String item) {
        int index=item.indexOf(":");
        int index1=item.indexOf(" ");
        String subString=item.substring(index+1,index1);
        //System.out.println(subString);
        return subString;
    }

    private String getHourFromItem(String item) {
        int index=item.indexOf(":");
        String subString = item.substring(0, index);
        //System.out.println(subString);
        return subString;
    }

    private void initializeCalendar(String familyIdFromFile) {
        //metodo che prende il calendario della famiglia e lo inizializza graficamente

        firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //pulisco la lista degli eventi
                events.clear();
                calendarView.setEvents(events);
                //inizializzo graficamente in base alle informazioni memorizzate nel database
                for (DataSnapshot day: snapshot.getChildren()
                     ) {
                    for (DataSnapshot event : day.getChildren()
                         ) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(event.child("milliseconds").getValue(long.class));
                        events.add(new EventDay(calendar, R.drawable.dot));
                    }
                }
                calendarView.setEvents(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}