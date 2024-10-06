package com.example.flat;

import android.app.AlertDialog;
import android.content.Context;
import android.opengl.GLES20;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.Renderer;

import org.rajawali3d.util.RajLog;

import javax.microedition.khronos.opengles.GL10;

public class GloboRenderer extends Renderer {

    private Sphere earth; // Globo 3D
    private Object3D[] satellites; // Satélites representados como pontos vermelhos
    private Context context; // Contexto para exibir as informações

    public GloboRenderer(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void initScene() {

        // Criar luz para iluminar a cena
        PointLight light = new PointLight();
        light.setPosition(0, 0, 4);
        light.setPower(2);
        getCurrentScene().addLight(light);

        // Criar o globo terrestre e aplicar a textura
        Material earthMaterial = new Material();
        try {
            earthMaterial.addTexture(new Texture("earthMap", R.drawable.earth_map)); // Imagem da textura do globo
        } catch (Exception e) {
            RajLog.e("Erro ao adicionar a textura: " + e.getMessage());
        }
        earthMaterial.setColorInfluence(0);

        earth = new Sphere(1, 64, 64);
        earth.setMaterial(earthMaterial);
        getCurrentScene().addChild(earth);

        // Criar satélites
        satellites = new Object3D[10]; // Exemplo com 10 satélites
        Material satelliteMaterial = new Material();
        satelliteMaterial.setColor(0xFF0000); // Cor vermelha para os satélites

        for (int i = 0; i < satellites.length; i++) {
            satellites[i] = new Sphere(0.05f, 12, 12); // Pequena esfera representando um satélite
            satellites[i].setMaterial(satelliteMaterial);
            getCurrentScene().addChild(satellites[i]);
        }
    }

    // Atualiza a posição dos satélites conforme os dados GNSS
    public void updateSatellitePosition(int index, double latitude, double longitude, double distance) {
        if (index >= 0 && index < satellites.length) {
            // Converter latitude e longitude para coordenadas cartesianas
            Vector3 position = new Vector3();
            position.setAll(Math.toRadians(longitude), Math.toRadians(latitude), distance);
            satellites[index].setPosition(position);
        }
    }

    // Método para exibir informações do satélite clicado
    @Override
    public void onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Implementação de qualquer lógica de clique se necessário
            // Se você tiver um método para detectar cliques, chame-o aqui.
            // picker.getObjectAt(event.getX(), event.getY());
        }
        // Chama o método da superclasse sem retornar nada
        // Isso está correto
    }

    // Exibe as informações do satélite em um diálogo
    private void showSatelliteInfo() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Informações do Satélite");
        dialog.setMessage("Latitude: -\nLongitude: -\nVelocidade: - m/s\nRumo: - graus");
        dialog.setPositiveButton("OK", null);
        dialog.show();
    }

    // Rotação automática do globo
    @Override
    public void onRenderFrame(GL10 gl) {
        super.onRenderFrame(gl);
        earth.rotate(Vector3.Axis.Y, 0.1); // Rotação suave do globo
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
        // Implementação vazia (se não precisar de nada aqui)
    }
}
