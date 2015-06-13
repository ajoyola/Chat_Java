/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author aoyola
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import static java.lang.System.in;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.JOptionPane.*;

/**
 * 
 * @author Administrador
 */
public class UserWindow extends JFrame implements ActionListener {
     String mensajeCliente;
     JTextArea panel_Central;
     JTextField txtMensage;
     JButton butSend;
     JLabel lblNomUser;
     JList lstActivos;
     User cliente;	
     JMenuBar barraMenu;
     Vector<String> nomUsers;
     Private ventPrivada;
     
     /** Creates a new instance of User */
     public UserWindow() throws IOException {
             super("Chat Redes_2014_2T");
             txtMensage = new JTextField(30);
             txtMensage.setBackground(Color.LIGHT_GRAY);
             butSend = new JButton("Send");
             lblNomUser = new JLabel("Usuario <<  >>");
             lblNomUser.setHorizontalAlignment(JLabel.CENTER);
             panel_Central = new JTextArea();             
             panel_Central.setColumns(25);
             txtMensage.addActionListener(this);
             butSend.addActionListener(this);
             lstActivos=new JList();  
             lstActivos.addMouseListener(new MouseAdapter() {
             
             public void mouseClicked(MouseEvent evt) {
            JList list = (JList)evt.getSource();
            int pos=list.getSelectedIndex();
            if (evt.getClickCount() == 2) {
                ventPrivada.setAmigo(nomUsers.get(pos));           
                ventPrivada.setVisible(true);
            } 
                }
            });
             lstActivos.setBackground(Color.LIGHT_GRAY);
             
             barraMenu=new JMenuBar();
             panel_Central.setBackground(Color.PINK);
             panel_Central.setForeground(Color.BLACK);
             panel_Central.setEditable(false);            
             panel_Central.setBorder(javax.swing.BorderFactory.createMatteBorder(5,5,5,5, Color.BLACK));
            
            

             // comenzamos a distrubir los elemenots en la ventana
             JPanel panel_Sur = new JPanel();
             panel_Sur.setLayout(new BorderLayout());
                panel_Sur.add(new JLabel("  Conversación:"),BorderLayout.NORTH);
                panel_Sur.add(txtMensage, BorderLayout.CENTER);
                panel_Sur.add(butSend, BorderLayout.EAST);
                panel_Sur.setBorder(javax.swing.BorderFactory.createMatteBorder(3,3,3,3, Color.WHITE));
                
             JPanel panel_Derecha = new JPanel();
             panel_Derecha.setLayout(new BorderLayout());
                panel_Derecha.add(lblNomUser, BorderLayout.NORTH);
                panel_Derecha.add(new JScrollPane(panel_Central), BorderLayout.CENTER);
                panel_Derecha.add(panel_Sur,BorderLayout.SOUTH);
                
             JPanel panel_Izquierda=new JPanel();
             panel_Izquierda.setLayout(new BorderLayout());
               panel_Izquierda.add(new JScrollPane(this.lstActivos),BorderLayout.CENTER);
               panel_Izquierda.add(new JLabel("Double Click - Private"),BorderLayout.NORTH);
               panel_Izquierda.setBorder(javax.swing.BorderFactory.createMatteBorder(5,5,5,5, Color.BLACK));
               
             JSplitPane sldCentral=new JSplitPane();  
             sldCentral.setDividerLocation(300);
             sldCentral.setDividerSize(7);
             sldCentral.setOneTouchExpandable(true);
             sldCentral.setLeftComponent(panel_Izquierda);
             sldCentral.setRightComponent(panel_Derecha);
             
             
             setLayout(new BorderLayout());
             add(sldCentral, BorderLayout.CENTER);   
             add(barraMenu,BorderLayout.NORTH);
             
             txtMensage.requestFocus();
             
             cliente=new User(this);
             cliente.conectar();     
             nomUsers=new Vector();
             setlstActivos(cliente.pedirUsuarios())  ; 
             ventPrivada=new Private(cliente);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
             
             setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				
             setVisible(true);
     }
     
     public void setNombreUser(String user)
     {
        lblNomUser.setText("Usuario " + user);
     }
     public void mostrarMsg(String msg) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException
     {
         
          
        this.panel_Central.append(msg+"\n");
     }
     public void setlstActivos(Vector datos)
     {
        nomUsers=datos;
        LlenarLista(this.lstActivos,nomUsers);
     }
     public void addUser(String user)
     {
        nomUsers.add(user);
        LlenarLista(this.lstActivos,nomUsers);
     }
     public void removeUser(String user)
     {        
        nomUsers.remove(user);
        LlenarLista(this.lstActivos,nomUsers);
     }
    private void LlenarLista(JList list,final Vector datos)
    {
        list.setModel(new AbstractListModel() {            
            @Override
            public int getSize() { return datos.size(); }
            @Override
            public Object getElementAt(int i) { return datos.get(i); }
        });
    }
    @Override
     public void actionPerformed(ActionEvent evt) {
      
        if(evt.getSource()==this.butSend || evt.getSource()==this.txtMensage)
        {
           String mensaje = txtMensage.getText();        
            try {
                cliente.consola_Msg(mensaje);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(UserWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPaddingException ex) {
                Logger.getLogger(UserWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex) {
                Logger.getLogger(UserWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalBlockSizeException ex) {
                Logger.getLogger(UserWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadPaddingException ex) {
                Logger.getLogger(UserWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
           txtMensage.setText("");
        }
      
     }
    
     public void PrivateMsg(String amigo,String msg) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, Exception
     {
         
        
        ventPrivada.setAmigo(amigo);  
        ventPrivada.mostrarMsg(msg);        
        ventPrivada.setVisible(true);
     }

   
}

