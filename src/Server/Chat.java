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


import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

class Chat extends JFrame
{
   JTextArea zona_Texto;
  
   public Chat()
   {
      super("SERVER STATUS: ONLINE");
      zona_Texto=new JTextArea();      
      this.setContentPane(new JScrollPane(zona_Texto));
      setSize(350,350);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setVisible(true);      
      
   }
   public void send_Message(String msg)
   {
      zona_Texto.append(msg+"\n");
   }
   public void initServer()
   {
       // se crearán 2 sockets: 1 para establecer la comuniacion y otr para enviar los mensajes
      ServerSocket serv=null;
      ServerSocket serv2=null;
      
      try{
         serv=new ServerSocket(8081); //establezco los puertos
         serv2=new ServerSocket(8082);
         send_Message("*****  Bienvenidos al chat Redes2014_2T *****");
         while(true)
         {
            Socket sock=null,sock2=null;
            try {
               
               send_Message("\n\n***** Materia: Redes de Computadores Par. 1 *****");
               send_Message("\n>>> Objetivo: Elaborar chat con Encriptación AES");
               send_Message("\n   Elaborado por: ");
               send_Message("\n>>> Ajoyola");
               sock=serv.accept();
               sock2=serv2.accept();
            } catch (IOException e)
            {
               send_Message("La conexión ha fallado. Lamentamos lo sucedido: " + serv + ", " + e.getMessage());
               continue;
            }
            Server_hilo sesion=new Server_hilo(sock,sock2,this);            
	    sesion.start();
         }
      
      }catch(IOException e){
         send_Message("Inconvenientes con la conexión :"+e);
      }
   }
   
   public static void main(String abc[]) throws IOException
   {                
     Chat chat= new Chat();
     chat.initServer();
   }
}



