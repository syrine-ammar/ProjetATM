package monpackageclient;

import java.io.*;
import javax.swing.*;

public class Pinstorage {

    private static final String PIN_FILE = "pin.txt";

    // Sauvegarder le PIN dans le fichier
    public static void savePin(String newPin) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PIN_FILE))) {
            writer.write(newPin);
        } catch (IOException e) {
            System.out.println("Erreur lors de l'enregistrement du PIN : " + e.getMessage());
        }
    }

    // Charger le PIN depuis le fichier
    public static String getStoredPin() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PIN_FILE))) {
            String pin = reader.readLine();
            return (pin != null && pin.matches("\\d{4}")) ? pin : null;
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du PIN : " + e.getMessage());
            return null;
        }
    }

    // Vérifier si le fichier existe déjà
    public static boolean pinFileExists() {
        File file = new File(PIN_FILE);
        return file.exists();
    }
}
