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
import java.net.*;
import java.lang.*;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class User_hilo extends Thread{
   DataInputStream entrada;
   UserWindow vcli;
   public User_hilo (DataInputStream entrada, UserWindow vcli) throws IOException
   {
      this.entrada=entrada;
      this.vcli=vcli;
   }
   public void run()
   {
      String menser="",amigo="";
      int opcion=0;
      while(true)
      {         
         try{
            opcion=entrada.readInt();
            switch(opcion)
            {
               case 1://mensage general/ publico
                  menser=entrada.readUTF();
                // La siguiente parte de cifrar con AES fue realizada
                  //con ayuda de varias fuentes y ejercicos de internet
                String key1 = "1234567812345678"; 
                byte[] key2 = key1.getBytes();
                SecretKeySpec secret = new SecretKeySpec(key2, "AES");
                Cipher cipher = Cipher.getInstance("AES"); 
                cipher.init(Cipher.ENCRYPT_MODE, secret);
                byte[] encrypted = cipher.doFinal(menser.getBytes("UTF-8"));                   
                System.out.println("En el Servidor:"+ new String(encrypted));
                vcli.mostrarMsg(menser);            
                break;
               case 2://añadiendo clientes
                  menser=entrada.readUTF();
                  vcli.addUser(menser);                  
                  break;
               case 3://mensage privado
                  amigo=entrada.readUTF();
                  menser=entrada.readUTF();
                  
                  String key3 = "1234567812345678"; 
                  byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                    IvParameterSpec ivspec = new IvParameterSpec(iv);
                    byte[] key4 = key3.getBytes();
                    SecretKeySpec secret1 = new SecretKeySpec(key4, "AES");
                    Cipher cipher1 = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
                    cipher1.init(Cipher.ENCRYPT_MODE, secret1, ivspec);
                    byte[] encryptedpri = cipher1.doFinal(menser.getBytes("UTF-8"));
                    
                  System.out.println("En el Servidor:"+ new String(encryptedpri));
                  String clearText = new String(encryptedpri, "UTF-8");
                  //vcli.PrivateMsg(amigo,clearText);
                  vcli.PrivateMsg(amigo,menser);
                  break;
            }
         }
         catch (IOException e){
            System.out.println("Error en la comunicación "+"Información para el usuario");
            break;
         } catch (NoSuchAlgorithmException ex) {
              Logger.getLogger(User_hilo.class.getName()).log(Level.SEVERE, null, ex);
          } catch (NoSuchPaddingException ex) {
              Logger.getLogger(User_hilo.class.getName()).log(Level.SEVERE, null, ex);
          } catch (InvalidKeyException ex) {
              Logger.getLogger(User_hilo.class.getName()).log(Level.SEVERE, null, ex);
          } catch (IllegalBlockSizeException ex) {
              Logger.getLogger(User_hilo.class.getName()).log(Level.SEVERE, null, ex);
          } catch (BadPaddingException ex) {
              Logger.getLogger(User_hilo.class.getName()).log(Level.SEVERE, null, ex);
          } catch (InvalidAlgorithmParameterException ex) {
              Logger.getLogger(User_hilo.class.getName()).log(Level.SEVERE, null, ex);
          } catch (Exception ex) {
              Logger.getLogger(User_hilo.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
      System.out.println("se desconecto el servidor");
   }

   
}