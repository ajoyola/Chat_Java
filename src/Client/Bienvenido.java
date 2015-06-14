/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author aoyola
 */
public class Bienvenido extends JFrame implements ActionListener {
     JButton iniciar;
     
     
    public void mostrar () {
    iniciar = new JButton("Ingresar");
    
    iniciar.addActionListener(this);
    JLabel lab = new JLabel();
        URL is = getClass().getResource("elchat.jpg");
        ImageIcon imgThisImg = new ImageIcon(is);
        lab.setIcon(imgThisImg);
        
        JPanel panel_Izquierda=new JPanel();
        JPanel panel_but=new JPanel();
        panel_Izquierda.setLayout(new BorderLayout());
        panel_but.setLayout(new BorderLayout());
        panel_but.add(iniciar,BorderLayout.CENTER);
         
        
        panel_Izquierda.add( new JLabel( "BIENVENDIDOS AL CHAT") ,BorderLayout.NORTH);
        panel_Izquierda.add(lab,BorderLayout.CENTER);
        panel_Izquierda.add(panel_but,BorderLayout.EAST);
        
       
        this.add(panel_Izquierda);
        this.setVisible(true);
        this.setSize(330, 330);
        
    }
    
   
    @Override
    public void actionPerformed(ActionEvent evt)  {
         if(evt.getSource()==this.iniciar )
        {
             try {
                 iniciarChat();
             } catch (IOException ex) {
                 Logger.getLogger(Bienvenido.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
    }
    
    public void iniciarChat () throws IOException{
        UserWindow p = new UserWindow();
    }
     public static void main(String args[]) throws IOException {
            //Cliente.IP_SERVER = JOptionPane.showInputDialog("Introducir AJOS :","localhost");
            Bienvenido b= new Bienvenido();
            b.mostrar();
            
     }

      
}

