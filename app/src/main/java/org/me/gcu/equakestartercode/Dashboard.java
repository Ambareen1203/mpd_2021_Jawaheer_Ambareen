package org.me.gcu.equakestartercode;

//Student name: Ambareen Shabnam Jawaheer
//Student ID: S1903330


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class Dashboard extends AppCompatActivity {
    ArrayList<EarthQuake> earthQuakeList = new ArrayList();
    int width;
    DisplayMetrics displayMetrics;
    final SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes
        savedInstanceState.putString("firstDate",((EditText) findViewById(R.id.firstDate)).getText().toString());
        savedInstanceState.putString("secondDate",((EditText) findViewById(R.id.secondDate)).getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        displayMetrics = Dashboard.this.getResources().getDisplayMetrics();
        width = (int) (displayMetrics.widthPixels*0.9);
        Bundle bundleObject = getIntent().getExtras();
        this.earthQuakeList = (ArrayList<EarthQuake>) bundleObject.getSerializable("earthquakeList");

        final Calendar myCalendar = Calendar.getInstance();

        final String[] assign = {""};

        final EditText firstDate = (EditText) findViewById(R.id.firstDate);
        final EditText secondDate = (EditText) findViewById(R.id.secondDate);

        Calendar beforeDate = Calendar.getInstance();
        beforeDate.add(Calendar.DAY_OF_MONTH,-100);
        if (savedInstanceState != null){
            firstDate.setText(savedInstanceState.getString("firstDate"));
            secondDate.setText(savedInstanceState.getString("secondDate"));

        }
        else {
            firstDate.setText(format1.format(beforeDate.getTime()));
            secondDate.setText(format1.format(Calendar.getInstance().getTime()));
        }


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                if (assign[0].equals("first")){
                    firstDate.setText(sdf.format(myCalendar.getTime()));
                }
                else {
                    secondDate.setText(sdf.format(myCalendar.getTime()));
                }
                ((LinearLayout)findViewById(R.id.linearlayout)).removeAllViews();
                cardSetup();
            }

        };

        firstDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assign[0] ="first";
                new DatePickerDialog(Dashboard.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        secondDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assign[0] ="second";
                new DatePickerDialog(Dashboard.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        cardSetup();


    }


    private void cardSetup() {
        final EditText firstDate = (EditText) findViewById(R.id.firstDate);
        final EditText secondDate = (EditText) findViewById(R.id.secondDate);

        final LinearLayout l_layout = findViewById(R.id.linearlayout);
        l_layout.setPadding(0,20,0,0);

        l_layout.setGravity(Gravity.CENTER);
        // create a new textview
        l_layout.addView(createTextTitle("From date range",18));

        EarthQuake North=null; EarthQuake South=null; EarthQuake East=null; EarthQuake West = null;
        EarthQuake magnitude=null; EarthQuake depth=null;

        double glasgow_latitude = 4.2518;
        double glasgow_longitude = 55.8642;
        //4.2518 55.8642
        for (int i =0;i<earthQuakeList.size();i++){
            if (filter(earthQuakeList.get(i),firstDate,secondDate)){
                if ((magnitude == null) || (earthQuakeList.get(i).getMagnitude() > magnitude.getMagnitude())){
                    magnitude = earthQuakeList.get(i);
                }
                if ((depth == null) || (earthQuakeList.get(i).getDepth() > depth.getDepth())){
                    depth = earthQuakeList.get(i);
                }
                double earth_lat = earthQuakeList.get(i).getLatitude();
                double earth_long = earthQuakeList.get(i).getLongitude();
                if (earth_lat>glasgow_latitude){
                    if ((North == null) || (earth_lat < North.getLatitude())){
                        North = earthQuakeList.get(i);
                    }
                }
                else {
                    if ((South == null) || (earth_lat > South.getLatitude())){
                        South = earthQuakeList.get(i);
                    }
                }
                if (earth_long>glasgow_longitude){
                    if ((East == null) || (earth_long < East.getLongitude())){
                        East = earthQuakeList.get(i);
                    }
                }
                else {
                    if ((West == null) || (earth_long > West.getLatitude())){
                        West = earthQuakeList.get(i);
                    }
                }
            }
        }

        l_layout.addView(createQuakeCard(North,"North","Direction"));
        l_layout.addView(createQuakeCard(South,"South","Direction"));
        l_layout.addView(createQuakeCard(East,"East","Direction"));
        l_layout.addView(createQuakeCard(North,"West","Direction"));
        Space space = new Space(Dashboard.this);
        space.setLayoutParams(
                new LinearLayout.LayoutParams(
                        0,40
                )
        );
        l_layout.addView(space);
        l_layout.addView(createTextTitle("Largest Magnitude Earthquake",17));
        l_layout.addView(createQuakeCard(magnitude,"North","Magnitude"));
        l_layout.addView(createTextTitle("Deepest Earthquake",17));
        l_layout.addView(createQuakeCard(depth,"North","Depth"));
        space = new Space(Dashboard.this);
        space.setLayoutParams(
                new LinearLayout.LayoutParams(
                        0,80
                )
        );
        l_layout.addView(space);

        l_layout.addView(createTextTitle("All Earthquakes",18));

        space = new Space(Dashboard.this);
        space.setLayoutParams(
                new LinearLayout.LayoutParams(
                        0,80
                )
        );
        l_layout.addView(space);

        for (int i = 0; i < earthQuakeList.size(); i++) {
            final EarthQuake currentEarthquake = earthQuakeList.get(i);
            if (filter(currentEarthquake,firstDate,secondDate)) {
                space = new Space(Dashboard.this);
                space.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,40));
                CardView card = new CardView(Dashboard.this);
                LinearLayout inner_card = new LinearLayout(Dashboard.this);
                inner_card.setOrientation(LinearLayout.HORIZONTAL);
                inner_card.setBackgroundColor(Color.BLACK);
                inner_card.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                width,
                                200
                        )
                );
                inner_card.setWeightSum(1.0f);
                inner_card.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent ide = new Intent(Dashboard.this, Details.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("earthquake",currentEarthquake);
                        ide.putExtras(bundle);
                        startActivity(ide);
                    }
                });


                card.addView(inner_card);
                card.setLayoutParams(
                        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,200)
                );
                card.setRadius(15);
                card.setPadding(25, 25, 25, 25);
                card.setMaxCardElevation(6);

//
//            inner_card.addView(mapview);

                LinearLayout left_inner_card = new LinearLayout(Dashboard.this);
                left_inner_card.setOrientation(LinearLayout.VERTICAL);
                left_inner_card.setBackgroundColor(Color.WHITE);
                left_inner_card.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                0.5f
                        )
                );
                left_inner_card.setGravity(Gravity.CENTER);
                left_inner_card.setPadding(40,20,40,20);


                final TextView locationTextView = new TextView(Dashboard.this);

                locationTextView.setText((earthQuakeList.get(i)).getLocation());
                locationTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,75));
                locationTextView.setTextColor(Color.BLACK);
                locationTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                locationTextView.setTypeface(null, Typeface.BOLD);
                locationTextView.setGravity(Gravity.LEFT);
                left_inner_card.addView(locationTextView);

                final TextView dateTextView = new TextView(Dashboard.this);

                dateTextView.setText(format1.format((earthQuakeList.get(i)).getStartDate().getTime()));
                dateTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,60));
                dateTextView.setTextColor(Color.GRAY);
                dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                dateTextView.setGravity(Gravity.LEFT);
                left_inner_card.addView(dateTextView);


                inner_card.addView(left_inner_card);


                LinearLayout right_inner_card = new LinearLayout(Dashboard.this);
                right_inner_card.setOrientation(LinearLayout.VERTICAL);
                right_inner_card.setBackgroundColor(Color.WHITE);
                right_inner_card.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                0.5f
                        )
                );
                right_inner_card.setGravity(Gravity.CENTER);

                final TextView magnitudeTitle = new TextView(Dashboard.this);

                magnitudeTitle.setText("Magnitude");
                magnitudeTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,60));
                magnitudeTitle.setTextColor(Color.BLACK);
                magnitudeTitle.setTypeface(null, Typeface.BOLD);
                magnitudeTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                magnitudeTitle.setGravity(Gravity.CENTER);
                magnitudeTitle.setPadding(20,0,20,0);

                right_inner_card.addView(magnitudeTitle);


                final TextView magnitudeTextView = new TextView(Dashboard.this);

                int conditionColor = 0;



                magnitudeTextView.setText(Double.toString((earthQuakeList.get(i)).getMagnitude()));
                magnitudeTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,80));
                if ((earthQuakeList.get(i).getMagnitude()) < 1.0)
                {

                    conditionColor = Color.parseColor("#66ff66");
                }
                else if ((earthQuakeList.get(i).getMagnitude()) < 2.0 )
                {

                    conditionColor = Color.parseColor("#F1C40F");
                }
                else if ((earthQuakeList.get(i).getMagnitude()) > 2.0 ) {

                    conditionColor = Color.parseColor("#E74C3C");
                }
                magnitudeTextView.setTextColor(conditionColor);
                magnitudeTextView.setTypeface(null, Typeface.BOLD);
                magnitudeTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                magnitudeTextView.setGravity(Gravity.CENTER);
                magnitudeTextView.setPadding(20,0,20,0);

                right_inner_card.addView(magnitudeTextView);

                inner_card.addView(right_inner_card);
                l_layout.addView(card);
                l_layout.addView(space);
            }
        }
        space = new Space(Dashboard.this);
        space.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,80));
        l_layout.addView(space);
    }
    private boolean filter(EarthQuake currentEarthquake, EditText firstDateView, EditText secondDateView) {
        String[] temp = firstDateView.getText().toString().split("-");
        Calendar firstDate = Calendar.getInstance();
        firstDate.set(Integer.parseInt(temp[2]),Integer.parseInt(temp[1]),Integer.parseInt(temp[0]));


        temp = secondDateView.getText().toString().split("-");
        Calendar secondDate = Calendar.getInstance();
        secondDate.set(Integer.parseInt(temp[2]),Integer.parseInt(temp[1]),Integer.parseInt(temp[0]));

        if ((currentEarthquake.getStartDate().after(firstDate)) && currentEarthquake.getStartDate().before(secondDate)){
            return true;
        }
        return false;
    }

    private TextView createTextTitle(String title, int size){
        TextView temp = new TextView(Dashboard.this);
        temp.setPadding(40,40,40,0);
        temp.setText(title);
        temp.setTextSize(size);
        temp.setTextColor(Color.BLACK);
        temp.setTypeface(null, Typeface.BOLD);
        return temp;
    }

    private View createQuakeCard(final EarthQuake earthQuake, String directionText, String mode) {
        final LinearLayout l_layout = findViewById(R.id.linearlayout);
        Space space = new Space(Dashboard.this);
        space.setLayoutParams(
                new LinearLayout.LayoutParams(
                        0,40
                )
        );
        l_layout.addView(space);
        if (earthQuake == null){
            TextView temp = new TextView(Dashboard.this);
            temp.setGravity(Gravity.CENTER);
            temp.setPadding(80,0,80,0);
            temp.setText("No EarthQuake within the last 100 Days is "+directionText+" of Glasgow City");
            temp.setTextSize(18);
            return temp;
        }
        else {
            CardView card = new CardView(Dashboard.this);
            card.setPadding(0,40,0,40);
            card.setRadius(8.0f);
            card.setElevation(8.0f);
            card.setLayoutParams(
                    new FrameLayout.LayoutParams(
                            width, 200
                    )
            );
            card.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent ide = new Intent(Dashboard.this, Details.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("earthquake",earthQuake);
                    ide.putExtras(bundle);
                    startActivity(ide);
                }
            });
            LinearLayout inner_card = new LinearLayout(Dashboard.this);
            inner_card.setOrientation(LinearLayout.HORIZONTAL);
            inner_card.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    )
            );
            inner_card.setWeightSum(1.0f);
            card.addView(inner_card);

            if (mode.equals("Direction")){
                TextView direction = new TextView(Dashboard.this);
                direction.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                0.4f
                        )
                );
                direction.setBackgroundColor(Color.BLACK);
                direction.setTextColor(Color.WHITE);
                direction.setText(directionText.substring(0,1));
                direction.setTextSize(21);
                direction.setGravity(Gravity.CENTER);

                inner_card.addView(direction);
            }

            TextView location = new TextView(Dashboard.this);
            location.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0.3f
                    )
            );
            location.setBackgroundColor(Color.WHITE);
            location.setTextColor(Color.BLACK);
            location.setGravity(Gravity.CENTER);
            location.setText(earthQuake.getLocation());
            location.setTextSize(18);
            location.setTypeface(null,Typeface.BOLD);

            inner_card.addView(location);

            LinearLayout right_inner_card = new LinearLayout(Dashboard.this);
            right_inner_card.setOrientation(LinearLayout.VERTICAL);
            right_inner_card.setBackgroundColor(Color.WHITE);
            right_inner_card.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0.35f
                    )
            );
            right_inner_card.setGravity(Gravity.CENTER);

            final TextView magnitudeTitle = new TextView(Dashboard.this);

            magnitudeTitle.setText("Magnitude");
            magnitudeTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,60));
            magnitudeTitle.setTextColor(Color.BLACK);
            magnitudeTitle.setTypeface(null, Typeface.BOLD);
            magnitudeTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            magnitudeTitle.setGravity(Gravity.CENTER);
            magnitudeTitle.setPadding(20,0,20,0);

            right_inner_card.addView(magnitudeTitle);


            final TextView magnitudeTextView = new TextView(Dashboard.this);



            magnitudeTextView.setText(Double.toString(earthQuake.getMagnitude()));
            magnitudeTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,80));


            magnitudeTextView.setTypeface(null, Typeface.BOLD);
            magnitudeTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            magnitudeTextView.setGravity(Gravity.CENTER);
            magnitudeTextView.setPadding(20,0,20,0);

            right_inner_card.addView(magnitudeTextView);

            inner_card.addView(right_inner_card);

            if (!mode.equals("Direction")){
                right_inner_card = new LinearLayout(Dashboard.this);
                right_inner_card.setOrientation(LinearLayout.VERTICAL);
                right_inner_card.setBackgroundColor(Color.WHITE);
                right_inner_card.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                0.35f
                        )
                );
                right_inner_card.setGravity(Gravity.CENTER);

                final TextView depthTitle = new TextView(Dashboard.this);

                depthTitle.setText("Depth");
                depthTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,60));
                depthTitle.setTextColor(Color.BLACK);
                depthTitle.setTypeface(null, Typeface.BOLD);
                depthTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                depthTitle.setGravity(Gravity.CENTER);
                depthTitle.setPadding(20,0,20,0);

                right_inner_card.addView(depthTitle);


                final TextView depthTextView = new TextView(Dashboard.this);

                depthTextView.setText(Double.toString(earthQuake.getDepth()));
                depthTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,80));
                depthTextView.setTextColor(Color.BLACK);
                depthTextView.setTypeface(null, Typeface.BOLD);
                depthTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                depthTextView.setGravity(Gravity.CENTER);
                depthTextView.setPadding(20,0,20,0);

                right_inner_card.addView(depthTextView);

                inner_card.addView(right_inner_card);
            }

            return card;
        }
    }

    public ArrayList<EarthQuake> getEarthQuakeList(){
        return this.earthQuakeList;
    }

}
