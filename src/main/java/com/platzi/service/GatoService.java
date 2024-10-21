package com.platzi.service;

import okhttp3.*;
import com.google.gson.Gson;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.platzi.model.Gato;
import com.platzi.model.GatoFav;


public class GatoService {

    public static String urlBase = "https://api.thecatapi.com/v1/";
    public static String endpointBusqueda = urlBase + "images/search";
    public static String endpointFavoritos = urlBase + "favourites";

    public static String gatoAleatorioMenu = "Opciones:\n"
            + "1. Ver otra imagen\n"
            + "2. Favorito\n"
            + "3. Volver\n";

    public static String gatoFavoritoAleatorioMenu = "Opciones:\n"
            + "1. Ver otra imagen\n"
            + "2. Eliminar favorito\n"
            + "3. Volver\n";

    public static void verGatos() {
        //Creamos el cliente http
        OkHttpClient client = new OkHttpClient();

        //Creamos la request
        Request request = new Request.Builder()
                .url(endpointBusqueda)
                .get()
                .build();

        //Definimos el string de la response
        String jsonData = "";

        //Obtenemos la response de nuestra request
        try (Response response = client.newCall(request).execute()){
            //Consumimos la response
            jsonData = response.body() != null ? response.body().string() : null;
            System.out.println(jsonData);

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        //Eliminamos las llaves de la jsonData al inicio y al final
        jsonData = jsonData != null ? jsonData.substring(1) : null;
        jsonData = jsonData != null ? jsonData.substring(0, jsonData.length() - 1) : null;

        //Creamos el Gson y deserializamos el gato obtenido
        Gson gson = new Gson();
        Gato gato = gson.fromJson(jsonData, Gato.class);

        //Definimos la imagen del gato
        Image image = null;

        try {
            //Obtenemos la url de la imagen en formato URL
            URL url = new URL(gato.getUrl());
            //Creamos la imagen usando la url
            image = ImageIO.read(url);
            //Convertimos la imagen a Ícono para mostrarlo en el JOptionPane
            ImageIcon fotoIconoGato = new ImageIcon(image);

            //Verificamos el tamaño de la imagen y redimensionamos en caso de ser necesario
            if (fotoIconoGato.getIconWidth() > 500 || fotoIconoGato.getIconWidth() > 500) {
                //Redimensiona la foto y crea un nuevo icono
                System.out.println(fotoIconoGato.getIconWidth());
                System.out.println(fotoIconoGato.getIconHeight());
                Image fondo = fotoIconoGato.getImage();
                Image modificada = fondo.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
                fotoIconoGato = new ImageIcon(modificada);
                System.out.println(fotoIconoGato.getIconWidth());
                System.out.println(fotoIconoGato.getIconHeight());

            }

            //Creamos el JOptionPane
            String[] botones = {"Ver otra imagen", "Favorito", "Volver"};
            String opcionMenu = (String) JOptionPane.showInputDialog(
                    null,
                    gatoAleatorioMenu,
                    gato.getId(),
                    JOptionPane.INFORMATION_MESSAGE,
                    fotoIconoGato,
                    botones,
                    botones[0]);

            int seleccion = -1;

            for (int i = 0; i < botones.length; i++) {
                if (opcionMenu.equals(botones[i])) seleccion = i;
            }

            switch (seleccion) {
                case 0:
                    verGatos();
                    break;

                case 1:
                    favoritoGato(gato);
                    break;

                default:
                    break;

            }

        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    public static void favoritoGato(Gato gato) {
        try {
            //Creamos un cliente
            OkHttpClient cliente = new OkHttpClient();

            //Creamos un mediaType de tipo Json
            MediaType mediaType = MediaType.parse("application/json");

            //Creamos un requestBody con el mediaType creado anteriormente
            RequestBody requestBody = RequestBody.create("{\n\t\"image_id\":\"" + gato.getId() + "\"\n}", mediaType);

            //Creamos la request con la requestBody creada previamente y pasamos los parámetros necesarios
            Request request = new Request.Builder()
                    .url(endpointFavoritos)
                    .post(requestBody)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", Gato.getApiKey())
                    .build();

            //Creamos un call con la request y lo ejecutamos, obteniendo el response
            Response response = cliente.newCall(request).execute();

        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    public static void verFavoritos() {

        try {
            //Creamos un cliente http
            OkHttpClient cliente = new OkHttpClient();

            //Creamos la petición
            Request request = new Request.Builder()
                    .url(endpointFavoritos)
                    .get()
                    .addHeader("x-api-key", Gato.getApiKey())
                    .build();

            //Creamos un call desde el cliente a partir de la request, lo ejecutamos y obtenemos la respuesta del servidor
            Response response = cliente.newCall(request).execute();

            //Creamos el String para crear el Gson
            String jsonData = response.body().string();

            //Creamos el Gson
            Gson gson = new Gson();

            GatoFav[] gatosArray = gson.fromJson(jsonData, GatoFav[].class);

            if (gatosArray.length > 0) {
                final int min = 1;
                final int max = gatosArray.length;

                int aleatorio = (int) (Math.random() * ((max - min) + 1)) + min; //Formula general para un num aleatorio entre un rango max y min
                int indice = aleatorio - 1;

                GatoFav gatoFav = gatosArray[indice];

                //Definimos la imagen del gato
                Image image = null;

                try {
                    //Obtenemos la url de la imagen en formato URL
                    URL url = new URL(gatoFav.getImage().getUrl());
                    //Creamos la imagen usando la url
                    image = ImageIO.read(url);
                    //Convertimos la imagen a Ícono para mostrarlo en el JOptionPane
                    ImageIcon fotoIconoGato = new ImageIcon(image);

                    //Verificamos el tamaño de la imagen y redimensionamos en caso de ser necesario
                    if (fotoIconoGato.getIconWidth() > 500 || fotoIconoGato.getIconHeight() > 500) {
                        //Redimensiona la foto y crea un nuevo icono
                        System.out.println(fotoIconoGato.getIconWidth());
                        Image fondo = fotoIconoGato.getImage();
                        Image modificada = fondo.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
                        fotoIconoGato = new ImageIcon(modificada);
                        System.out.println(fotoIconoGato.getIconWidth());

                    }

                    //Creamos el JOptionPane
                    String[] botones = {"Ver otra imagen", "Eliminar favorito", "Volver"};
                    String opcionMenu = (String) JOptionPane.showInputDialog(
                            null,
                            gatoFavoritoAleatorioMenu,
                            gatoFav.getImage().getId(),
                            JOptionPane.INFORMATION_MESSAGE,
                            fotoIconoGato,
                            botones,
                            botones[0]);

                    int seleccion = -1;

                    for (int i = 0; i < botones.length; i++) {
                        if (opcionMenu.equals(botones[i])) seleccion = i;
                    }

                    switch (seleccion) {
                        case 0:
                            verFavoritos();
                            break;

                        case 1:
                            borrarFavorito(gatoFav);
                            break;

                        default:
                            break;

                    }
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }


        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void borrarFavorito(GatoFav gatoFav) {
        try {
            //Creamos un cliente Http
            OkHttpClient cliente = new OkHttpClient();

            //Creamos la petición usando el cuerpo de la petición
            Request request = new Request.Builder()
                    .url(endpointFavoritos + "/" + gatoFav.getId())
                    .delete()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", Gato.getApiKey())
                    .build();

            //Creamos la call a partir de la request desde el cliente y la ejecutamos (Ejecutamos la petición y obtenemos la respueste)
            Response response = cliente.newCall(request).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
