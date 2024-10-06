package com.example.flat;

import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;

public class GNSSActivity extends AppCompatActivity {

    private LocationManager locationManager; // Gerente de localização
    private static final int REQUEST_LOCATION = 1; // Código para a requisição de permissão

    // Componentes UI para exibir as informações
    private TextView positionTextView;
    private TextView satelliteTextView;
    private TextView signalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnss);

        // Inicializando componentes de interface
        positionTextView = findViewById(R.id.textview_position);
        satelliteTextView = findViewById(R.id.textview_satellites);
        signalTextView = findViewById(R.id.textview_signal);

        // Inicializando LocationManager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Verifica se as permissões de localização estão concedidas
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicita permissão se ainda não foi concedida
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            // Configura o LocationProvider se as permissões já foram concedidas
            setupLocationProvider();
        }
    }

    // Configura o LocationProvider e solicita atualizações de localização
    private void setupLocationProvider() {
        // Obtém o provider do GPS
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Solicita atualizações de localização usando o GPS_PROVIDER
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.1f, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        // Atualiza a posição do usuário quando a localização mudar
                        updatePosition(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        // Tratamento adicional se necessário
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        // Quando o provider é ativado
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        // Quando o provider é desativado
                    }
                });

                // Registra o callback para atualizações dos satélites GNSS
                locationManager.registerGnssStatusCallback(new GnssStatus.Callback() {
                    @Override
                    public void onSatelliteStatusChanged(GnssStatus status) {
                        // Atualiza as informações dos satélites
                        updateSatelliteInfo(status);
                    }
                });
            }
        } else {
            // Caso o provider não esteja disponível, exiba uma mensagem ou trate a situação
            positionTextView.setText("GPS Provider não disponível.");
        }
    }

    // Atualiza e exibe a posição do usuário na interface
    private void updatePosition(Location location) {
        if (location != null) {
            String position = "Latitude: " + location.getLatitude() + "\n" +
                    "Longitude: " + location.getLongitude() + "\n" +
                    "Altitude: " + location.getAltitude();
            positionTextView.setText(position); // Exibe a posição no TextView
        } else {
            positionTextView.setText("Localização não disponível.");
        }
    }

    // Atualiza e exibe as informações dos satélites GNSS
    private void updateSatelliteInfo(GnssStatus status) {
        StringBuilder satelliteInfo = new StringBuilder();
        StringBuilder signalInfo = new StringBuilder();

        int satelliteCount = status.getSatelliteCount(); // Obtém o número de satélites
        satelliteInfo.append("Número de Satélites: ").append(satelliteCount).append("\n");

        // Itera sobre todos os satélites visíveis
        for (int i = 0; i < satelliteCount; i++) {
            satelliteInfo.append("SVID: ").append(status.getSvid(i)).append("\n");
            satelliteInfo.append("Azimute: ").append(status.getAzimuthDegrees(i)).append("\n");
            satelliteInfo.append("Elevação: ").append(status.getElevationDegrees(i)).append("\n");

            // Coleta informações de sinal
            int snr = (int) status.getCn0DbHz(i); // Força do sinal
            signalInfo.append("SNR: ").append(snr).append(" dB\n");
        }

        satelliteTextView.setText(satelliteInfo.toString()); // Atualiza o TextView dos satélites
        signalTextView.setText(signalInfo.toString()); // Atualiza o TextView do sinal
    }

    // Trata a resposta da solicitação de permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, configura o provedor de localização
                setupLocationProvider();
            } else {
                // Permissão negada, informe o usuário
                positionTextView.setText("Permissão de localização negada.");
            }
        }
    }
}
