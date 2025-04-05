package monpackageclient;

import javax.swing.*;

import java.awt.Font;
import java.awt.event.*;
import java.io.IOException;
import com.sun.javacard.apduio.Apdu;
import com.sun.javacard.apduio.CadTransportException;

public class Login extends JFrame {

    private JPanel jPanel1;
    private JPasswordField jPasswordField3;
    private JLabel jLabel1;
    private JButton jButton1;
    private JLabel jLabel2;
    private JLabel jLabel4;

    public static final byte CLA_MONAPPLET = (byte) 0xB0;
    public static final byte INS_TEST_CODE_PIN = 0x00;

    private Maclasse client;
    private Apdu apdu;

    public static void main(String[] args) throws IOException, CadTransportException {
        Maclasse client = new Maclasse();
        client.Connect();
        client.Select();
        Login l = new Login(client, null);
        l.setVisible(true);
    }

    public Login(Maclasse client, Apdu apdu) {
        this.client = client;
        this.apdu = apdu;

        jPanel1 = new JPanel();
        jPasswordField3 = new JPasswordField();
        jLabel1 = new JLabel();
        jButton1 = new JButton();
        jLabel2 = new JLabel();
        jLabel4 = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 204, 255));

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));

        jLabel1.setFont(new java.awt.Font("Yu Gothic", Font.BOLD, 30));
        jLabel1.setText("Enter PIN:");

        jButton1.setBackground(new java.awt.Color(153, 255, 153));
        jButton1.setFont(new java.awt.Font("Segoe UI Black", Font.PLAIN, 18));
        jButton1.setText("Login");
        jButton1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

        jLabel2.setIcon(new ImageIcon("C:\\Users\\PC\\Desktop\\interface2.png"));
        jLabel4.setFont(new java.awt.Font("Sylfaen", Font.BOLD, 36));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("YOUR ATM MACHINE:");

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addGap(49, 49, 49)
                    .addComponent(jLabel1)
                    .addGap(18, 18, 18)
                    .addComponent(jPasswordField3, GroupLayout.PREFERRED_SIZE, 196, GroupLayout.PREFERRED_SIZE)
                    .addGap(189, 189, 189))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(56, 56, 56)
                    .addComponent(jLabel2)
                    .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(90, 90, 90)
                    .addComponent(jLabel4)
                    .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(200, 200, 200)
                    .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(55, 55, 55)
                    .addComponent(jLabel4)
                    .addGap(11, 11, 11)
                    .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jPasswordField3, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addGap(18, 18, 18)
                    .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
                    .addGap(27, 27, 27)
                    .addComponent(jLabel2)
                    .addContainerGap(15, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setSize(800, 600); // Set the JFrame size to 800x600
        setLocationRelativeTo(null); // Center the JFrame on the screen
    }

 
    private void jButton1ActionPerformed(ActionEvent evt) {
        String pass = String.valueOf(jPasswordField3.getPassword());

        // Vérification du format du PIN
        if (pass.length() != 4 || !pass.matches("\\d+")) {
            messageBox("Invalid PIN format! Please enter a 4-digit PIN.", JOptionPane.WARNING_MESSAGE, "Error");
            return;
        }

        try {
            // Vérifier si le fichier PIN existe
            if (!Pinstorage.pinFileExists()) {
                // Initialiser le fichier avec un PIN par défaut
                String defaultPin = "2003";
                Pinstorage.savePin(defaultPin);
            }

            // Lecture du PIN actuel depuis le fichier
            String currentPin = Pinstorage.getStoredPin();

            if (currentPin == null || !currentPin.equals(pass)) {
                messageBox("Invalid PIN!", JOptionPane.WARNING_MESSAGE, "Warning");
                return;
            }

            messageBox("Login successful!", JOptionPane.INFORMATION_MESSAGE, "Success");

            MainPanel panelframe = new MainPanel(client, apdu);
            panelframe.setVisible(true);
            this.dispose();
        } catch (Exception e) {
            messageBox("An error occurred: " + e.getMessage(), JOptionPane.ERROR_MESSAGE, "Error");
        }

        jPasswordField3.setText("");
    }




    private void messageBox(String msg, int type, String title) {
        JOptionPane optionPane = new JOptionPane(msg, type);
        JDialog dialog = optionPane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
}