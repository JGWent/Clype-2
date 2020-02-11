/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import data.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 *
 * @author manth
 */
public class ClypeServer {
    private int port;
    private boolean closeConnection;
    private ArrayList<ServerSideClientIO> serverSideClientIOList;

    /**
     * Default constructor
     */
    public ClypeServer(){
        this(7000);
    }

    /**
     * Constructor when port is given
     * @param iport
     */
    public ClypeServer(int iport) throws IllegalArgumentException{
        if(iport < 1024){
            throw new IllegalArgumentException(String.format("port number must be greater than 1023"));
        }
        this.port = 7000;
        this.closeConnection = false;
        this.serverSideClientIOList = new ArrayList<ServerSideClientIO>();
    }
    public void start(){
        try{
            ServerSocket socket;
            Socket clientSocket;
            socket = new ServerSocket(this.port);
            while(!this.closeConnection){
                clientSocket = socket.accept();
                ServerSideClientIO client = new ServerSideClientIO(this, clientSocket);
                this.serverSideClientIOList.add(client);
                Thread t = new Thread(client);
                t.start();
            }
            socket.close();
        }catch(IOException ioe){
            System.err.println("IOException Caught");
        }
    }
    
    /**
     *
     * @param serverSideClientToRemove
     */
    public synchronized void remove(ServerSideClientIO serverSideClientToRemove){
        this.serverSideClientIOList.remove(serverSideClientToRemove);
    }
    
    /**
     *
     * @param dataToBroadcastToClients
     */
    public synchronized void broadcast(ClypeData dataToBroadcastToClients){
        for(ServerSideClientIO client : serverSideClientIOList){
            client.setDataToSendToClient(dataToBroadcastToClients);
            client.sendData();
        }
    }
    
    /**
     *
     * @param serverClientToGetUsers
     */
    public void listUsers(ServerSideClientIO serverClientToGetUsers){
        for(ServerSideClientIO client : serverSideClientIOList){
            ClypeData userNameObject = new MessageClypeData(client.getUserName(), client.getUserName(), 3);
            serverClientToGetUsers.setDataToSendToClient(userNameObject);
            serverClientToGetUsers.sendData();
        }
    }
    
    /**
     * returns the port number
     * @return
     */
    public int getPort(){
        return this.port;
    }
    
    /**
     * returns a unique hashcode for every different ClypeServer object.
     * @return
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClypeServer other = (ClypeServer) obj;
        if (this.port != other.port) {
            return false;
        }
        if (this.closeConnection != other.closeConnection) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + this.port;
        hash = 73 * hash + (this.closeConnection ? 1 : 0);
        hash = 73 * hash + (this.serverSideClientIOList != null ? this.serverSideClientIOList.hashCode() : 0);
        return hash;
    }
    
    /**
     * Checks equality of two ClypeServer objects.
     * @return
     */
    
    /**
     * returns a String representation of the ClypeServer object
     * @return
     */
    @Override
    public String toString(){
        String output;
        output = "Port: " + this.port + "\n" + "Closed Connection: " + this.closeConnection + "\n";
        return output;
    }
    
    /**
     *
     * @param args
     */
    public static void main(String args[]){
        ClypeServer Server;
        if(args.length == 1){
            Server = new ClypeServer(Integer.parseInt(args[0]));
            Server.start();
        }
        else if(args.length == 0){
            Server = new ClypeServer();
            Server.start();
        }
    }
}
