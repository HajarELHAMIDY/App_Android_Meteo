package com.example.meteo;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView dateText,tempText,tempMinText,tempMaxText,cityText,humidityText,pressureText;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dateText = findViewById(R.id.date);
        tempMaxText = findViewById(R.id.tempMaxV);
        tempMinText = findViewById(R.id.tempMinV);
        tempText = findViewById(R.id.temp);
        cityText = findViewById(R.id.ville);
        humidityText = findViewById(R.id.humiditeV);
        pressureText = findViewById(R.id.pressionV);
        imageView = findViewById(R.id.imageView2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem =menu.findItem(R.id.search);

        android.widget.SearchView  searchView =
                (android.widget.SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                 RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url="http://api.openweathermap.org/data/2.5/weather?q="+query.trim()+
                        "&appid=1527fd01a838d323b987ab3a5c5eff77";


                 StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            Date date = new Date(jsonObject.getLong("dt")*1000);


                            String  simpleDateFormat =new SimpleDateFormat("dd/MM/yyyy hh:mm a",
                                    Locale.ENGLISH).format(date);

                            JSONObject main = jsonObject.getJSONObject("main");

                            int temp = (int)(main.getDouble("temp")-273.15);
                            int tempMin = (int)(main.getDouble("temp_min")-273.15);
                            int tempMax = (int)(main.getDouble("temp_max")-273.15);
                            int humidity = (int)main.getDouble("humidity");
                            int pressure = (int)main.getDouble("pressure");
                            JSONArray weather = jsonObject.getJSONArray("weather");
                            String meteo = weather.getJSONObject(0).getString("main");
                            setImage(meteo);
                            tempText.setText(String.valueOf(temp)+"°C");
                            tempMaxText.setText(String.valueOf(tempMax)+"°C");
                            tempMinText.setText(String.valueOf(tempMin)+"°C");
                            humidityText.setText(String.valueOf(humidity)+"%");
                            pressureText.setText(String.valueOf(pressure)+" hPa");
                            dateText.setText(simpleDateFormat);
                            cityText.setText(query);
                }catch (Exception e){
                            Log.d("pays","erreur ....");

                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Log.i("MyLog","---Connection problem----------");
                            }
                        });
                queue.add(stringRequest);
                 return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return true;
            }
        });
        return true;
    }
    public void setImage(String meteo){
        if(meteo.equals("Clouds")){
            imageView.setImageResource(R.drawable.clouds);
        }
        if(meteo.equals("Clear")){
            imageView.setImageResource(R.drawable.clear);
        }
        if(meteo.equals("Rain")){
            imageView.setImageResource(R.drawable.rain);
        }
        if(meteo.equals("Thunderstorm")){
            imageView.setImageResource(R.drawable.th);
        }
    }


}
