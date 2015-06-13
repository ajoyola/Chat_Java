/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author aoyola
 */


import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
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
import javax.swing.*;

/**
 *
 * @author Administrador
 * 
 * Esta clase simulará las diferentes sesiones iniciadas en el chat, 
 * a la vez contendrá la lista de usuarios online los cuales se manejarán con hilos.
 * 
 */
public class Server_hilo extends Thread
{
    
     Socket socket_Server=null;
     Socket socket_Clients=null;
     DataInputStream in=null;
     DataOutputStream out_server=null;
     DataOutputStream out_clients=null;
     public static Vector<Server_hilo> Clientes_Online=new Vector();	
     String User;
     Chat chat;
     
     public Server_hilo(Socket ss,Socket sc,Chat serv)
     {
        socket_Server=ss;
        socket_Clients=sc;
        this.chat=serv;
        User="";
        Clientes_Online.add(this);        
        serv.send_Message(" " + this + " Nuevo user ha iniciado sesión");			
     }

    public String getUser() {
        return User;
    }

    public void setUser(String User) {
        this.User = User;
    }
     
    public void run()
     {
    	chat.send_Message("Estado: ONLINE");
    	
    	try // a continuación el desarrollo del chat--- flujo de mensajes entre los diversos clientes
    	{
          in=new DataInputStream(socket_Server.getInputStream());
          out_server=new DataOutputStream(socket_Server.getOutputStream()); //uso personal del servidor_hilo
          out_clients=new DataOutputStream(socket_Clients.getOutputStream()); //notificará a los demás usuarios online
          this.setUser(in.readUTF());
          enviaUserActivos();
    	}
    	catch (IOException e) { 
            e.printStackTrace();   
        }
    	
        int opcion=0,numUsers=0;
        String user_solicitante="",msg="";
                
    	while(true)
    	{
          try
          {
             opcion=in.readInt();
             switch(opcion)
             {
                case 1://envio de mensage a todos
                   msg=in.readUTF();
                   //encriptando el mensaje
                   
                   String key1 = "1234567812345678"; 
                    byte[] key2 = key1.getBytes();
                    SecretKeySpec secret = new SecretKeySpec(key2, "AES");
                    Cipher cipher = Cipher.getInstance("AES"); 
                    cipher.init(Cipher.ENCRYPT_MODE, secret);
                    byte[] encrypted = cipher.doFinal(msg.getBytes("UTF-8"));
                   chat.send_Message("Mensaje: "+encrypted);
                   Server_hilo.this.sendMsg(msg);
                   break;
                case 2://envio de lista de activos
                   numUsers=Clientes_Online.size();
                   out_server.writeInt(numUsers);
                   for(int i=0;i<numUsers;i++)
                      out_server.writeUTF(Clientes_Online.get(i).User);
                   break;
               case 3: // envio mensaje encriptado a uno solo
                   user_solicitante=in.readUTF();//captura nombre de user_solicitante
                   msg=in.readUTF();//mensage enviado
                 /*String key3 = "1234567812345678"; 
                    byte[] key4 = key3.getBytes();
                    SecretKeySpec secret1 = new SecretKeySpec(key4, "AES");
                    Cipher cipher1 = Cipher.getInstance("AES"); 
                    cipher1.init(Cipher.ENCRYPT_MODE, secret1);
                    byte[] encrypted1 = cipher1.doFinal(msg.getBytes("UTF-8"));
                   //sendMsg(user_solicitante,new String (encrypted1));*/
                   
                    sendMsg(user_solicitante,msg);
                    
                   break;
             }
          }
          catch (IOException e) 
          {
              System.out.println("Fin de la conexión.... Hasta pronto");
              break;
          }     catch (NoSuchAlgorithmException ex) {     
                    Logger.getLogger(Server_hilo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchPaddingException ex) {
                    Logger.getLogger(Server_hilo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidKeyException ex) {
                    Logger.getLogger(Server_hilo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalBlockSizeException ex) {
                    Logger.getLogger(Server_hilo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BadPaddingException ex) {
                    Logger.getLogger(Server_hilo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(Server_hilo.class.getName()).log(Level.SEVERE, null, ex);
                }     
    	}
        
    	chat.send_Message("Se removio un usuario");
    	Clientes_Online.removeElement(this);
    	try
    	{
          chat.send_Message("Usuario Offline");
          socket_Server.close();
    	}	
        catch(Exception et)
        {chat.send_Message("Problemas al cerrar el socket");}   
     }
     
     
     public void enviaUserActivos()
     {
        Server_hilo user=null;
        for(int i=0;i<Clientes_Online.size();i++)
        {           
           try
            {
              user=Clientes_Online.get(i);
              if(user==this)
                  continue;
              user.out_clients.writeInt(2); 
              user.out_clients.writeUTF(this.getUser());	
            }catch (IOException e) {e.printStackTrace();}
        }
     }

     
     public void sendMsg(String msg) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, IOException
     {
        Server_hilo user=null;
        for(int i=0;i<Clientes_Online.size();i++) // CON ESTO SE EVIA EL MSG A TODOS LOS HILOS (ONLINE USERS)
        {
            try
            {
           
              user=Clientes_Online.get(i);
              user.out_clients.writeInt(1);//opcion de mensage 
            user.out_clients.writeUTF(""+this.getUser()+" dice : "+ msg);              
            }catch (IOException e) {e.printStackTrace();}
        }
     }
     
   private void sendMsg(String user_solicitante, String mencli) 
   {
      Server_hilo user=null;
  
        for(int i=0;i<Clientes_Online.size();i++)
        {           
           try
            {
                  user=Clientes_Online.get(i);
              
              if(user.User.equals(user_solicitante))
              {
                 user.out_clients.writeInt(3);//opcion de mensage user_solicitante   
                 user.out_clients.writeUTF(this.getUser());

                 user.out_clients.writeUTF(user_solicitante+ ": >>>"+ mencli);
              }
            }catch (IOException e) {e.printStackTrace();}
        }
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
}
