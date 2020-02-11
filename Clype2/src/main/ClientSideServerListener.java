    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import Project2.*;
import data.*;
import javafx.scene.control.*;
import javax.swing.ImageIcon;

/**
 * Listener for the Client threads 
 * @author John Graham
 */
public class ClientSideServerListener implements Runnable{
    private final ClypeClient client;
    private mainGUI maingui;
    
    public ClientSideServerListener(ClypeClient client, mainGUI maingui){
        this.client = client;
        this.maingui = maingui;
    }

    @Override
    public void run() {
        Object obj = new Object();
        synchronized( obj ) {
            client.sendUserName();
            while (!this.client.getClosedConnection()) {
                this.client.receiveData();
                ClypeData data1 = this.client.getDataToReceiveFromServer();
                if(data1.getData() instanceof ImageIcon){
                    this.maingui.setImage((ImageIcon)data1.getData());
                }
                else if(data1.getData() instanceof String){
                    this.maingui.setMessages(data1.getData().toString());
                    //this.maingui.setEmojiAndText(data1.getData().toString());
                }
                else if(data1.getData() instanceof byte[]){
                    this.maingui.setPDF((byte[])data1.getData());
                }
            }
            this.client.receiveData();
   	}
    }
}