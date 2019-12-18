package com.example.primosintentservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvList;
    private Button btConsultar;
    private TextView tvCantidad;
    private ArrayList<Integer> numerosPrimos;
    private NumerosPrimosService myService;
    private int cantidadPrimos;
    private Button btGuarda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvList = findViewById(R.id.rvList);
        btConsultar = findViewById(R.id.btInicia);
        tvCantidad = findViewById(R.id.tvCantidad);
        myService = null;
        btGuarda = findViewById(R.id.btGuarda);
        numerosPrimos=new ArrayList<>();
        Intent intent = new Intent(this, NumerosPrimosService.class);
        startService(intent);
        IntentFilter filter = new IntentFilter();
        filter.addAction(NumerosPrimosService.DAME_PRIMOS);
        PrimosReciver primosReciver = new PrimosReciver();
        LocalBroadcastManager.getInstance(this).registerReceiver(primosReciver, filter);

        btConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCantidad.setText(String.valueOf(cantidadPrimos));
                rvList.setAdapter(new AdapterPrimos(numerosPrimos, MainActivity.this));
                rvList.setHasFixedSize(true);
                rvList.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));

            }
        });

        btGuarda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File directorio = new File(getExternalFilesDir(null), "NumerosPrimos");

                if (!directorio.exists()){
                    directorio.mkdir();
                }

                File file = new File(directorio, "listaPrimos.txt");

                try {

                    FileOutputStream outputStream = new FileOutputStream(file);
                    OutputStreamWriter writer = new OutputStreamWriter(outputStream);

                    for (int i=0; i<numerosPrimos.size(); i++){
                        writer.write((numerosPrimos.get(i).toString() + "\n"));
                    }

                    writer.close();
                    outputStream.close();
                    Toast.makeText(MainActivity.this, "Data has been saved!!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "Error saving data...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class PrimosReciver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            cantidadPrimos = intent.getIntExtra("cant",0);
            numerosPrimos = intent.getIntegerArrayListExtra("primos");
        }
    }

}