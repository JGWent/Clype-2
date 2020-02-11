/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import data.*;
import java.io.*;
import java.net.*;

/**
 *
 * @author manth
 */
public class ServerSideClientIO implements Runnable{
    private boolean closeConnection;
    private ClypeData dataToReceiveFromClient;
    private ClypeData dataToSendToClient;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;
    private final ClypeServer server;
    private final Socket clientSocket;
    private String userName;
    private boolean listUsers;
    
    /**
     *
     * @param server
     * @param clientSocket
     */
    public ServerSideClientIO(ClypeServer server, Socket clientSocket){
        this.server = server;
        this.clientSocket = clientSocket;
        this.closeConnection = false;
        this.dataToReceiveFromClient = null;
        this.dataToSendToClient = null;
        this.inFromClient = null;
        this.outToClient = null;
        this.listUsers = false;
    }
    
    @Override
    public void run(){
        try{
            this.outToClient = new ObjectOutputStream(this.clientSocket.getOutputStream());
            this.inFromClient = new ObjectInputStream(this.clientSocket.getInputStream());
            this.receiveUserName();
            while(!this.closeConnection){
                receiveData();
                if(!this.listUsers){
                    this.server.broadcast(this.dataToReceiveFromClient);
                }
            }
            this.clientSocket.close();
            this.inFromClient.close();
            this.outToClient.close();
        }catch(IOException ioe){
            System.err.println("IOException caught2");
        }
    }
    
    /**
     *
     */
    public void receiveData(){
        try{
            this.dataToReceiveFromClient = (ClypeData) this.inFromClient.readObject();
            if(this.dataToReceiveFromClient == null){
                this.closeConnection = true;
                this.listUsers = true;
                String message = this.userName + " has left the chat";
                this.dataToSendToClient = new MessageClypeData(this.userName, message, 3);
                this.server.broadcast(this.dataToSendToClient);
                this.server.remove(this);
            }
            else if(this.dataToReceiveFromClient.getData().equals("LISTUSERS")){
                this.server.listUsers(this);
                this.listUsers = true;
            }
            else{
                this.dataToSendToClient = this.dataToReceiveFromClient;
                this.listUsers = false;
            }
        }catch(IOException E){
            System.err.println(E.getMessage());
        }catch (ClassNotFoundException E) {
            System.err.println("ClassNotFoundException caught");
        }
    }
    
    /**
     *
     */
    public void sendData(){
        try{
            this.outToClient.writeObject(this.dataToSendToClient);
        }catch(IOException E){
            System.err.println("IOException caught4");
        }
    }
    
    /**
     *
     */
    public void receiveUserName(){
        try{
            this.setUserName(this.inFromClient.readLine());
        }catch(IOException E){
            System.err.println("IOException caught");
        }
    }
    
    /**
     *
     * @return
     */
    public String getUserName(){
        return this.userName;
    }
    
    public void setUserName(String username){
        this.userName = username;
    }
    
    /**
     *
     * @param dataToSendToClient
     */
    public void setDataToSendToClient(ClypeData dataToSendToClient){
        this.dataToSendToClient = dataToSendToClient;
    }
}
