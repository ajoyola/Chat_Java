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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
/**
 *
 * @author Administrador
 */
public class Private extends JFrame implements ActionListener
{
   JTextArea panMostrar;
   JTextField txtMensage;
   JButton butEnviar;
   User cliente;
   String user_solicitante;
   
   public Private(User cliente)
   {
      super("Private Conversation");
      this.cliente=cliente;
      txtMensage = new JTextField(30);
      butEnviar = new JButton("Enviar");
      panMostrar = new JTextArea(); 
      panMostrar.setEditable(false);
      txtMensage.requestFocus();
      txtMensage.addActionListener(this);
      butEnviar.addActionListener(this);
      
      JPanel panAbajo = new JPanel();
             panAbajo.setLayout(new BorderLayout());
             panAbajo.add(new JLabel("  Ingrese mensage a enviar:"),
                                BorderLayout.NORTH);
             panAbajo.add(txtMensage, BorderLayout.CENTER);
             panAbajo.add(butEnviar, BorderLayout.EAST);
      
      setLayout(new BorderLayout());
      add(new JScrollPane(panMostrar),BorderLayout.CENTER);
      add(panAbajo,BorderLayout.SOUTH);
       
      user_solicitante="";
      
      this.addWindowListener(new WindowListener()
      {         
         public void windowClosing(WindowEvent e) {
            cerrarVentana();
         }
         public void windowClosed(WindowEvent e) {}         
         public void windowOpened(WindowEvent e) {}
         public void windowIconified(WindowEvent e) {}
         public void windowDeiconified(WindowEvent e) {}
         public void windowActivated(WindowEvent e) {}
         public void windowDeactivated(WindowEvent e) {}
        
      });
      
      setSize(300,300);
      setLocation(570,90);      			      
   }
   
   
   
   
   
   public void setAmigo(String ami)
   {      
      this.user_solicitante=ami;
      this.setTitle(ami);      
   }
    private void cerrarVentana() 
    {       
      this.setVisible(false);      
    }
    
     public byte[] cifra(String sinCifrar) throws Exception {
	final byte[] bytes = sinCifrar.getBytes("UTF-8");
	final Cipher aes = obtieneCipher(true);
	final byte[] cifrado = aes.doFinal(bytes);
	return cifrado;
}
    private Cipher obtieneCipher(boolean paraCifrar) throws Exception {
	final String frase = "FraseLargaConDiferentesLetrasNumerosYCaracteresEspeciales_áÁéÉíÍóÓúÚüÜñÑ1234567890!#%$&()=%_NO_USAR_ESTA_FRASE!_";
	final MessageDigest digest = MessageDigest.getInstance("SHA");
	digest.update(frase.getBytes("UTF-8"));
	final SecretKeySpec key = new SecretKeySpec(digest.digest(), 0, 16, "AES");
 
	final Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
	if (paraCifrar) {
		aes.init(Cipher.ENCRYPT_MODE, key);
	} else {
		aes.init(Cipher.DECRYPT_MODE, key);
	}
 
	return aes;
}
    
    public String descifra(byte[] cifrado) throws Exception {
	final Cipher aes = obtieneCipher(false);
	final byte[] bytes = aes.doFinal(cifrado);
	final String sinCifrar = new String(bytes, "UTF-8");
	return sinCifrar;
}
    public void mostrarMsg(String msg) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, Exception 
     {
         
       
         this.panMostrar.append(msg + "\n");
        
                   
     }
    
   @Override
   public void actionPerformed(ActionEvent e) 
   {
      String mensaje = txtMensage.getText();              
      
       try {
           mostrarMsg(cliente.getnickName()+">"+mensaje);
       } catch (UnsupportedEncodingException ex) {
           Logger.getLogger(Private.class.getName()).log(Level.SEVERE, null, ex);
       } catch (NoSuchPaddingException ex) {
           Logger.getLogger(Private.class.getName()).log(Level.SEVERE, null, ex);
       } catch (Exception ex) {
           Logger.getLogger(Private.class.getName()).log(Level.SEVERE, null, ex);
       }
     
      
       
       try {
           cliente.consola_Msg(user_solicitante,mensaje);
       } catch (NoSuchAlgorithmException ex) {
           Logger.getLogger(Private.class.getName()).log(Level.SEVERE, null, ex);
       } catch (NoSuchPaddingException ex) {
           Logger.getLogger(Private.class.getName()).log(Level.SEVERE, null, ex);
       } catch (InvalidKeyException ex) {
           Logger.getLogger(Private.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IllegalBlockSizeException ex) {
           Logger.getLogger(Private.class.getName()).log(Level.SEVERE, null, ex);
       } catch (BadPaddingException ex) {
           Logger.getLogger(Private.class.getName()).log(Level.SEVERE, null, ex);
       }
      txtMensage.setText("");
   }
}
