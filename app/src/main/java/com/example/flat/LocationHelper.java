package com.example.flat;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationHelper {

    // Verifica se a permissão de localização foi concedida
    public static boolean checkLocationPermission(Activity activity, int requestCode) {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Se a permissão não foi concedida, solicita permissão
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
            return false;
        }
        return true; // Se a permissão foi concedida, retorna true
    }
}
