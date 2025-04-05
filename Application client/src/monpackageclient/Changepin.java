package monpackageclient;

import java.io.*;
import javax.swing.*;
import java.awt.Font;
import java.awt.event.*;
import com.sun.javacard.apduio.Apdu;
import com.sun.javacard.apduio.CadTransportException;

public class Changepin extends JFrame {
    private JPanel jPanel1;
    private JPasswordField jOldPinField;
    private JPasswordField jNewPinField;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JButton jButton1;
    private JButton jRetourButton;

    public static final byte INS_VERIFY_PIN = 0x00;
    public static final byte INS_CHANGE_PIN = 0x01;

    private Maclasse client;
    private Apdu apdu;

    public Changepin(Maclasse client, Apdu apdu) {
        this.client = client;
        this.apdu = apdu;

        // Initialisation des composants de l'interface graphique
        jPanel1 = new JPanel();
        jOldPinField = new JPasswordField();
        jNewPinField = new JPasswordField();
        jLabel1 = new JLabel("Ancien PIN :");
        jLabel2 = new JLabel("Nouveau PIN :");
        jButton1 = new JButton("Changer PIN");
        jRetourButton = new JButton("Retour");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 204, 255));

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));
        jLabel1.setFont(new Font("Yu Gothic", Font.BOLD, 20));
        jLabel2.setFont(new Font("Yu Gothic", Font.BOLD, 20));

        jButton1.setBackground(new java.awt.Color(153, 255, 153));
        jButton1.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jRetourButton.setBackground(new java.awt.Color(255, 153, 153)); 
        jRetourButton.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
        jRetourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                jRetourButtonActionPerformed(evt);
            }
        });

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(50, 50, 50)
                    .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(18, 18, 18)
                            .addComponent(jOldPinField, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(18, 18, 18)
                            .addComponent(jNewPinField, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(100, 100, 100)
                            .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(100, 100, 100)
                            .addComponent(jRetourButton, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jOldPinField, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                    .addGap(20, 20, 20)
                    .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jNewPinField, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                    .addGap(30, 30, 30)
                    .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                    .addGap(20, 20, 20)
                    .addComponent(jRetourButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)  
                    .addContainerGap(30, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1);
        pack();
        setSize(400, 350);  
        setLocationRelativeTo(null);

        // Si le fichier PIN n'existe pas, demander à l'utilisateur de définir un PIN initial
        if (!Pinstorage.pinFileExists()) {
            messageBox("Aucun PIN enregistré. Veuillez en définir un nouveau.", JOptionPane.INFORMATION_MESSAGE, "Nouveau PIN");
        }
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        String oldPin = String.valueOf(jOldPinField.getPassword());
        String newPin = String.valueOf(jNewPinField.getPassword());

        // Vérification de validité du format du PIN
        if (!isValidPin(oldPin) || !isValidPin(newPin)) {
            messageBox("Le format du PIN est invalide. Utilisez 4 chiffres.", JOptionPane.WARNING_MESSAGE, "Erreur");
            return;
        }

        // Vérification de l'ancien PIN stocké
        String storedPin = Pinstorage.getStoredPin();
        if (storedPin == null || !storedPin.equals(oldPin)) {
            messageBox("Ancien PIN incorrect !", JOptionPane.WARNING_MESSAGE, "Erreur");
            return;
        }

        try {
            byte[] oldPinBytes = convertPinToBytes(oldPin);
            byte[] newPinBytes = convertPinToBytes(newPin);

            // Envoyer le nouveau PIN
            this.apdu = client.Msg(INS_CHANGE_PIN, (byte) 0x08, concatArrays(oldPinBytes, newPinBytes), (byte) 0x7F);
            if (apdu.getStatus() == 0x9000) {
                // Sauvegarder le nouveau PIN dans le fichier
                Pinstorage.savePin(newPin);
                messageBox("PIN changé avec succès !", JOptionPane.INFORMATION_MESSAGE, "Succès");
            } else {
                messageBox("Erreur lors du changement de PIN.", JOptionPane.ERROR_MESSAGE, "Erreur");
            }

        } catch (Exception e) {
            messageBox("Une erreur s'est produite : " + e.getMessage(), JOptionPane.ERROR_MESSAGE, "Erreur");
        }

        jOldPinField.setText("");
        jNewPinField.setText("");
    }

    private void jRetourButtonActionPerformed(ActionEvent evt) {
        this.dispose();  // Fermer la fenêtre actuelle
        new MainPanel(client, apdu).setVisible(true); 
    }

    private boolean isValidPin(String pin) {
        return pin.length() == 4 && pin.matches("\\d+");
    }
    

    private byte[] convertPinToBytes(String pin) {
        int byte1 = (Integer.parseInt(pin) / 1000) % 10;
        int byte2 = (Integer.parseInt(pin) / 100) % 10;
        int byte3 = (Integer.parseInt(pin) / 10) % 10;
        int byte4 = Integer.parseInt(pin) % 10;
        return new byte[]{(byte) byte1, (byte) byte2, (byte) byte3, (byte) byte4};
    }

    private byte[] concatArrays(byte[] arr1, byte[] arr2) {
        byte[] result = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, result, 0, arr1.length);
        System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
        return result;
    }

    private void messageBox(String msg, int type, String title) {
        JOptionPane.showMessageDialog(this, msg, title, type);
    }
}