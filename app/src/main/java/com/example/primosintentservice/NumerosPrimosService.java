package com.example.primosintentservice;
import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

public class NumerosPrimosService extends IntentService {

    public static final String DAME_PRIMOS = "com.carlesramos.dameprimos";

    public NumerosPrimosService() {
        super("ServiciosPrimo");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        ArrayList<Integer> numerosPrimos = new ArrayList<>();
        int posiblesPrimos = 2;
        int cantidadPrimos = 0;
        int contador = 0;

        while (true) {
            if (posiblesPrimos > 2) {
                posiblesPrimos += 2;
            }

            for (int i = 1; i <= posiblesPrimos; i++) {

                if (posiblesPrimos % i == 0) {
                    contador++;
                }
            }

            if (contador == 2) {
                cantidadPrimos++;
                Intent bcIntent = new Intent();
                numerosPrimos.add(posiblesPrimos);
                bcIntent.setAction(DAME_PRIMOS);
                bcIntent.putExtra("cant",cantidadPrimos);
                bcIntent.putExtra("primos",numerosPrimos);
                LocalBroadcastManager.getInstance(this).sendBroadcast(bcIntent);

            }

            if (posiblesPrimos == 2){
                posiblesPrimos++;
            }

            contador = 0;
        }
    }
}