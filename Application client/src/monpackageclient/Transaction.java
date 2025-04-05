package monpackageclient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.sun.javacard.apduio.Apdu;

public class Transaction extends JFrame implements ActionListener {
    JLabel titre, lSolde, jLabel2;
    JButton b10, b20, b50, b100, b150, b200, returnButton, showHistoryButton;
    JRadioButton rCredit, rDebit;
    ButtonGroup grp;
    JPanel pT, pSolde, pChoix, pBtn, pBottom;

    public static final byte CLA_MONAPPLET = (byte) 0xB0;
    public static final byte INS_INTERROGER_COMPTE = 0x01;
    public static final byte INS_INCREMENTER_COMPTE = 0x02;
    public static final byte INS_DECREMENTER_COMPTE = 0x03;
 // Declare the history array and index variable
    private String[] historiqueActions = new String[10];  // Adjust the size as needed
    private int indexHistorique = 0;
    private Maclasse client;
    private Apdu apdu;

    public Transaction(Maclasse client, Apdu apdu) {
        this.client = client;
        this.apdu = apdu;

        // Set layout and background color
        this.setLayout(new GridLayout(7, 1));  // Increased rows to accommodate new button
        this.getContentPane().setBackground(new java.awt.Color(153, 204, 255));

        // Panels
        pT = new JPanel();
        pSolde = new JPanel();
        pChoix = new JPanel();
        pBtn = new JPanel();
        pBottom = new JPanel(); // New panel for bottom buttons and icon

        // Title
        titre = new JLabel("Make a transaction :");
        titre.setFont(new Font("Serif", Font.BOLD, 40));
        pT.setBackground(new java.awt.Color(153, 204, 255));
        pSolde.setBackground(new java.awt.Color(153, 204, 255));
        pChoix.setBackground(new java.awt.Color(153, 204, 255));
        pBtn.setBackground(new java.awt.Color(153, 204, 255));
        pBottom.setBackground(new java.awt.Color(153, 204, 255));

        // Fetch initial balance
        long solde = 0;
        try {
            apdu = client.Msg(INS_INTERROGER_COMPTE, (byte) 0x00, null, (byte) 0x7f);
            if (apdu.getStatus() == 0x9000) {
                int nombre_outputs = apdu.dataOut[1];
                for (int i = 0; i < nombre_outputs; i++) {
                    solde += apdu.dataOut[i + 2];
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        lSolde = new JLabel("Votre solde est : " + solde + " DT");
        lSolde.setFont(new Font("Serif", Font.CENTER_BASELINE, 20));

        // Buttons
        b10 = createButton("10 DT");
        b20 = createButton("20 DT");
        b50 = createButton("50 DT");
        b100 = createButton("100 DT");
        b150 = createButton("150 DT");
        b200 = createButton("200 DT");

        // Radio Buttons
        grp = new ButtonGroup();
        rCredit = createRadioButton("Deposit (+)");
        rDebit = createRadioButton("Withdraw (-)");

        grp.add(rCredit);
        grp.add(rDebit);

        // Bottom buttons (Return and Show History)
        returnButton = createButton("Return");
        returnButton.setBackground(new java.awt.Color(255, 153, 153)); // Red color for return button
        showHistoryButton = createButton("Show History");
        showHistoryButton.setBackground(Color.LIGHT_GRAY); // Light green for show history button
        showHistoryButton.setBackground(new Color(144, 238, 144)); // More specific light green

        // Reduce the height of the buttons
        returnButton.setPreferredSize(new Dimension(150, 20));
        showHistoryButton.setPreferredSize(new Dimension(150, 20));

        // Add components to panels
        pT.add(titre);
        pSolde.add(lSolde);

        pChoix.setLayout(new GridLayout(3, 2, 10, 10));
        pChoix.add(b10);
        pChoix.add(b20);
        pChoix.add(b50);
        pChoix.add(b100);
        pChoix.add(b150);
        pChoix.add(b200);

        pBtn.add(rCredit);
        pBtn.add(rDebit);

        // Icon Label
        jLabel2 = new JLabel();
        jLabel2.setIcon(new javax.swing.ImageIcon("C:\\Users\\PC\\Desktop\\interface2.png"));

        // Bottom panel for buttons and icon
        pBottom.setLayout(new FlowLayout(FlowLayout.CENTER));
        pBottom.add(returnButton);
        pBottom.add(showHistoryButton);
        pBottom.add(jLabel2);
        showHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // When the "Show History" button is clicked, call afficherHistorique()
                if (e.getSource() == showHistoryButton) {
                    afficherHistorique();
                }
            }
        
        });

        // Add panels to frame
        this.add(pT);
        this.add(pSolde);
        this.add(pChoix);
        this.add(pBtn);
        this.add(pBottom);  // Added new bottom panel

        // Frame properties
        this.setVisible(true);
        pack();
        setSize(800, 600); // Set the JFrame size to 800x600
        setLocationRelativeTo(null);
    }

    // Helper method to create buttons with consistent design
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setPreferredSize(new Dimension(200, 50));
        button.setBackground(new java.awt.Color(0, 255, 255));
        button.setFont(new Font("Serif", Font.BOLD, 16));
        return button;
    }

    // Helper method to create radio buttons with consistent design
    private JRadioButton createRadioButton(String text) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.addActionListener(this);
        radioButton.setPreferredSize(new Dimension(150, 25));
        return radioButton;
    }

    private void debiter(int montant) {
        // Vérifier si le montant est inférieur ou égal au solde
        if (montant > balance) {
            messageBox("Erreur : Le montant à débiter est supérieur à votre solde.", JOptionPane.WARNING_MESSAGE, "Warning");
            return; // Sortir de la méthode sans débiter
        }

        int montantInt = montant;
        int number_bytes = 1;
        int montantSave = montantInt;

        // Calculer le nombre d'octets nécessaires
        while (montantSave > 127) {
            number_bytes++;
            montantSave -= 127;
        }

        byte[] montantTab = new byte[number_bytes];
        int counter = 0;
        montantSave = montantInt;

        // Remplir le tableau d'octets
        while (montantSave > 127) {
            montantTab[counter] = (byte) 127;
            montantSave -= 127;
            counter++;
        }
        montantTab[counter] = (byte) montantSave;

        try {
            apdu = client.Msg(INS_DECREMENTER_COMPTE, (byte) number_bytes, montantTab, (byte) 0x7f);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        if (apdu.getStatus() == 0x6A85) {
            System.out.println("Erreur : status word différent de 0x9000");
            messageBox("Impossible d'effectuer cette opération !", JOptionPane.WARNING_MESSAGE, "Warning");
        } else {
            System.out.println("OK");
            messageBox("Un montant de " + montant + " DT a été retiré avec succès de votre compte !", JOptionPane.INFORMATION_MESSAGE, "Success");
            ajouterHistorique("Retrait de " + montant + " DT");

            // Mise à jour du solde local
            balance -= montant;  // Soustraire le montant du solde

            System.out.println("Débité de : " + montant + " DT. Nouveau solde : " + balance);
            lSolde.setText("Votre solde est : " + balance + " DT");  // Mise à jour du label
        }
    }




    private void crediter(long montant) {
        int montantInt = (int) montant;
        int number_bytes = 1;
        int montantSave = montantInt;

        // Calculer le nombre d'octets nécessaires
        while (montantSave > 127) {
            number_bytes++;
            montantSave -= 127;
        }

        byte[] montantTab = new byte[number_bytes];
        int counter = 0;
        montantSave = montantInt;

        // Remplir le tableau d'octets
        while (montantSave > 127) {
            montantTab[counter] = (byte) 127;
            montantSave -= 127;
            counter++;
        }
        montantTab[counter] = (byte) montantSave;

        try {
            apdu = client.Msg(INS_INCREMENTER_COMPTE, (byte) number_bytes, montantTab, (byte) 0x7f);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        if (apdu.getStatus() == 0x6A85) {
            System.out.println("Erreur : status word différent de 0x9000");
            messageBox("Impossible d'effectuer cette opération !", JOptionPane.WARNING_MESSAGE, "Warning");
        } else {
            System.out.println("OK");
            messageBox("Un montant de " + montant + " DT a été déposé avec succès sur votre compte !", JOptionPane.INFORMATION_MESSAGE, "Success");
            ajouterHistorique("Dépot de " + montant + " DT");

            // Mise à jour du solde local
            balance += montant;  // Ajouter le montant au solde

            System.out.println("Crédité de : " + montant + " DT. Nouveau solde : " + balance);
            lSolde.setText("Votre solde est : " + balance + " DT");  // Mise à jour du label
        }
    }



    public void actionPerformed(ActionEvent e ){
    	if (e.getSource() == b10) handleTransaction(10);
    	if (e.getSource() == b20) handleTransaction(20);
    	if (e.getSource() == b50) handleTransaction(50);
    	if (e.getSource() == b100) handleTransaction(100);
    	if (e.getSource() == b150) handleTransaction(150);
    	if (e.getSource() == b200) handleTransaction(200);

    	// Handle new buttons
        if (e.getSource() == returnButton) {
            // Add action for return button
            System.out.println("Return pressed");
            // For example, go back to previous screen or close the window
            dispose();
        } else if (e.getSource() == showHistoryButton) {
            // Add action for show history button
            System.out.println("Show History pressed");
            // You can implement functionality to show history here
        }
    }

    private void handleTransaction(int amount) {
        if (rCredit.isSelected()) {
            crediter(amount);
        } else if (rDebit.isSelected()) {
            debiter(amount);
        }
    }
    private long balance = 0;
    private void afficherSolde() {
        try {
            apdu = client.Msg(INS_INTERROGER_COMPTE, (byte) 0x00, null, (byte) 0x7f);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        if (apdu.getStatus() != 0x9000) {
            System.out.println("Erreur : status word différent de 0x9000");
        } else {
            int nombre_outputs = apdu.dataOut[1];
            long newBalance = 0;
            for (int i = 0; i < nombre_outputs; i++) {
                newBalance += apdu.dataOut[i + 2];
            }

            // Si le solde a changé, on met à jour la variable balance et le label
            if (newBalance != balance) {
                balance = newBalance;
                System.out.println("Valeur du compteur : " + balance);
                lSolde.setText("Votre solde est : " + balance + " DT");
            }
        }
    }

    // Add the action to the history
    private void ajouterHistorique(String action) {
        if (indexHistorique < historiqueActions.length) {
            historiqueActions[indexHistorique] = action;
            indexHistorique++;
        } else {
            // If the history is full, shift items to make space
            System.arraycopy(historiqueActions, 1, historiqueActions, 0, historiqueActions.length - 1);
            historiqueActions[historiqueActions.length - 1] = action;
        }
    }

    // Display the history of actions
    private void afficherHistorique() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indexHistorique; i++) {
            sb.append(historiqueActions[i]).append("\n");
        }
        messageBox(sb.toString(), JOptionPane.INFORMATION_MESSAGE, "Historique des actions");
    }

    // This function displays the message box
    private void messageBox(String msg, int type, String title) {
        JOptionPane.showMessageDialog(null, msg, title, type);
    }

}