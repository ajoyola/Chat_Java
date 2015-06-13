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
import java.io.*;
import java.net.*;
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
import javax.swing.JOptionPane;

/**
 *
 * @author Administrador
 */
public class User
{
   
   UserWindow ventana_user;
   DataInputStream in_communication = null;
   DataOutputStream out = null;
   DataInputStream in_msj = null;
   Socket socket_comunic = null;
   Socket socket_msg = null;
   String nickName;
   
   
   public User(UserWindow vent) throws IOException
   {      
      this.ventana_user=vent;
   }
   
   public void conectar() throws IOException 
   {
      try {
         socket_comunic = new Socket("127.0.0.1", 8081);
         socket_msg = new Socket("127.0.0.1", 8082);
         in_communication = new DataInputStream(socket_comunic.getInputStream());
         out = new DataOutputStream(socket_comunic.getOutputStream());
         in_msj = new DataInputStream(socket_msg.getInputStream());
         nickName = JOptionPane.showInputDialog("Ingrese su Nickname :");
         ventana_user.setNombreUser(nickName);         
         out.writeUTF(nickName);
      } catch (IOException e) {
         System.out.println("\tProblemas con el Servidor");
      }
      new User_hilo(in_msj, ventana_user).start();
   }
   public String getnickName()
   {
      return nickName;
   }
   public Vector<String> pedirUsuarios()
   {
      Vector<String> users = new Vector();
      try {         
         out.writeInt(2);
         int numUsers=in_communication.readInt();
         for(int i=0;i<numUsers;i++)
            users.add(in_communication.readUTF());
      } catch (IOException ex) {
         Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
      }
      return users;
   }
   public void consola_Msg(String msg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
   {
      try {             
          
          
          String key3 = "1234567812345678"; 
                    byte[] key4 = key3.getBytes();
                    SecretKeySpec secret1 = new SecretKeySpec(key4, "AES");
                    Cipher cipher1 = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
                    cipher1.init(Cipher.ENCRYPT_MODE, secret1);
                    byte[] encryptedpri = cipher1.doFinal(msg.getBytes("UTF-8"));
         System.out.println("el mensaje enviado desde el cliente es :"
             + new String (encryptedpri));
         out.writeInt(1);
        out.writeUTF(msg);
         //out.writeUTF(new String(encryptedpri));
      } catch (IOException e) {
         System.out.println(e);
      }
   }
  
   public void consola_Msg(String user_solicitante,String mens) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
   {
      try {        
          
              String key3 = "1234567812345678"; 
                    byte[] key4 = key3.getBytes();
                    SecretKeySpec secret1 = new SecretKeySpec(key4, "AES");
                    Cipher cipher1 = Cipher.getInstance("AES"); 
                    cipher1.init(Cipher.ENCRYPT_MODE, secret1);
                    byte[] encryptedpri = cipher1.doFinal(mens.getBytes("UTF-8"));
         System.out.println("el mensaje enviado desde el cliente es :"
             + new String(encryptedpri));
         out.writeInt(3);//opcion de mensage a user_solicitante
         out.writeUTF(user_solicitante);
         //out.writeUTF(new String(encryptedpri));
         out.writeUTF(mens);
      } catch (IOException e) {
         System.out.println("error...." + e);
      }
   }
   
  
}

