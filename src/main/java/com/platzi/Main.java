package com.platzi;

import javax.swing.*;

import com.platzi.service.GatoService;

public class Main {
    public static void main(String[] args) {

        int opcionMenu = -1;
        String[] botones = {"1. Ver gatos", "2. Ver favoritos", "3. Salir"};

        do{
            String opcion = (String) JOptionPane.showInputDialog(
                    null,
                    "Gatitos Java",
                    "Menu principal",
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    botones,
                    botones[0]
            );

            //Validamos la opción selecionada por el usuario
            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])) {
                    opcionMenu = i;
                }

            }

            switch (opcionMenu) {
                case 0:
                    GatoService.verGatos();
                    break;

                case 1:
                    GatoService.verFavoritos();
                    break;

                default:
                    break;
            }
        } while(opcionMenu != 2);
    }

}