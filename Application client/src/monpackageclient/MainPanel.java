package monpackageclient;


import javax.swing.*;


import com.sun.javacard.apduio.Apdu;
import com.sun.javacard.jpcsclite.APDU;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JFrame implements ActionListener{
	private JButton jButton1, jButton2, jButton3, jButton4, jButton5, jButton6, jButton7;  
    private JLabel jLabel1,jLabel2,jLabel3, jLabel10;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JPanel jPanel1;
    public static final byte CLA_MONAPPLET = (byte) 0xB0;
	public static final byte INS_TEST_CODE_PIN = 0x00;
	public static final byte INS_INTERROGER_COMPTE = 0x01;
	public static final byte INS_INCREMENTER_COMPTE = 0x02;
	public static final byte INS_DECREMENTER_COMPTE = 0x03;
	private Maclasse client;
	private Apdu apdu;
	    
	public MainPanel(Maclasse client, Apdu apdu) {
        this.client = client;
        this.apdu = apdu;
       
        jPanel1 = new JPanel();
        jPanel1.setBackground(new java.awt.Color(153, 204, 255));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 204, 255));
      
     jLabel6 = new javax.swing.JLabel();
     jLabel1 = new javax.swing.JLabel();
     jButton2 = new javax.swing.JButton();
     jButton4 = new javax.swing.JButton();
     jLabel2 = new javax.swing.JLabel();
     jLabel8 = new javax.swing.JLabel();
     jLabel9 = new javax.swing.JLabel();
     jLabel10 = new javax.swing.JLabel();
     jButton7 = new javax.swing.JButton();

     jLabel6.setBackground(new java.awt.Color(255, 255, 255));
     jLabel6.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
     jLabel6.setText("View amount");

     setBackground(new java.awt.Color(153, 204, 255));

     jLabel1.setFont(new java.awt.Font("Yu Gothic", 1, 34)); // NOI18N
     jLabel1.setForeground(new java.awt.Color(255, 255, 255));
     jLabel1.setText("Select a transaction");

     jButton2.setBackground(new java.awt.Color(0, 255, 255));
     jButton2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
     jButton2.setText("B");
     jButton2.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
             jButton2ActionPerformed(evt);
         }
     });

     jButton4.setBackground(new java.awt.Color(0, 255, 255));
     jButton4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
     jButton4.setText("A");
     jButton4.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
             jButton4ActionPerformed(evt);
         }
     });

     jLabel2.setIcon(new javax.swing.ImageIcon("C:\\Users\\PC\\Desktop\\interface2.png")); // NOI18N

     jLabel8.setBackground(new java.awt.Color(255, 255, 255));
     jLabel8.setFont(new java.awt.Font("Segoe UI Black", 0, 24)); // NOI18N
     jLabel8.setText("Change Pin");

     jLabel9.setBackground(new java.awt.Color(255, 255, 255));
     jLabel9.setFont(new java.awt.Font("Segoe UI Black", 0, 24)); // NOI18N
     jLabel9.setText("Make a Transaction");

     jLabel10.setFont(new java.awt.Font("Yu Gothic", 1, 36)); // NOI18N
     jLabel10.setText("Welcome !");

     jButton7.setBackground(new java.awt.Color(255, 153, 153));
     jButton7.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
     jButton7.setText("Logout");
     jButton7.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
             jButton7ActionPerformed(evt);
         }
     });

     GroupLayout layout = new GroupLayout(jPanel1);
     jPanel1.setLayout(layout);
     layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
             .addGap(17, 17, 17)
             .addComponent(jButton7)
             .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
             .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
             .addGap(260, 260, 260))
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
             .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
             .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
             .addGap(162, 162, 162))
         .addGroup(layout.createSequentialGroup()
             .addGap(82, 82, 82)
             .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE)
             .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
         .addGroup(layout.createSequentialGroup()
             .addGap(92, 92, 92)
             .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
             .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
             .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
             .addGap(83, 83, 83))
         .addGroup(layout.createSequentialGroup()
             .addGap(141, 141, 141)
             .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
             .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
             .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
             .addGap(180, 180, 180))
     );
     layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
             .addGap(23, 23, 23)
             .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                 .addComponent(jLabel10)
                 .addComponent(jButton7))
             .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
             .addComponent(jLabel1)
             .addGap(65, 65, 65)
             .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                 .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                 .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
             .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
             .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                 .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                 .addComponent(jLabel9))
             .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
             .addComponent(jLabel2)
             .addContainerGap())
     );
         
         getContentPane().add(jPanel1);
         pack();
         setSize(800, 600); // Set the JFrame size to 800x600
         setLocationRelativeTo(null);
         }


 



    private void jButton2ActionPerformed(ActionEvent evt) {
    	Transaction direction = new Transaction(client, apdu);
        direction.setVisible(true);
        
    
    }

 

    private void jButton4ActionPerformed(ActionEvent evt) {
    	  Changepin changePinFrame = new Changepin(client, apdu);
          changePinFrame.setVisible(true);
          this.dispose();
    }


    private void jButton7ActionPerformed(ActionEvent evt) {
        Login loginFrame = new Login(client, apdu);
        this.setVisible(false);
        loginFrame.setVisible(true);
    }



	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
