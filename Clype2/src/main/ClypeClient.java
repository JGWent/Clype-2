
package main;

import data.*;
import Project2.*;
import javax.swing.ImageIcon;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.*;

/**
*
* The Class that will be the client for the Clype Application
* @author John Graham
*
*
*/
public class ClypeClient implements Runnable{
    private String userName;
    private String hostName;
    private final int port;
    private boolean closeConnection;
    private ClypeData dataToSendToServer;
    private ClypeData dataToReceiveFromServer;
    private Scanner inFromStd;
    private ObjectInputStream inFromServer;
    private ObjectOutputStream outToServer;
    private static String key = "armalite";
    private volatile boolean sendDataBool;
    private boolean getDataBool;
    private mainGUI maingui;
    public ClypeClient(mainGUI maingui){
        this("Anon", "localhost", 7000, maingui);
    }
    /**
     *Constructor for when only the username is provided
     * @param uname
     * @param maingui
     */
    public ClypeClient(String uname, mainGUI maingui){
        this(uname, "localhost", 7000, maingui);
    }
    /**
     *Constructor for when the username and hostname is provided
     * @param uname
     * @param hostname
     * @param maingui
     */
    public ClypeClient(String uname, String hostname, mainGUI maingui){
        this(uname, hostname, 7000, maingui);
    }
    /**
     * Constructor for when the username, hostname, and port number are provided.
     * @param uname
     * @param hostname
     * @param iport
     * @param maingui
     */
    public ClypeClient(String uname, String hostname, int iport, mainGUI maingui) throws IllegalArgumentException{
        if(uname== null){
            throw new IllegalArgumentException(String.format("userName may not be null"));
        }
        if(hostname == null){
            throw new IllegalArgumentException(String.format("hostName may not be null"));
        }
        if(iport < 1024){
            throw new IllegalArgumentException(String.format("port number must be greater than 1023"));
        }
        this.userName = uname;
        this.hostName = hostname;
        this.port = iport;
        this.maingui = maingui;
        this.dataToReceiveFromServer = null;
        this.dataToSendToServer = null;
        this.closeConnection = false;
        this.outToServer = null;
        this.inFromServer = null;
        this.getDataBool = false;
        this.sendDataBool = false;
    }
    /**
     * reads data from client and prints the data
     */
    public void start(){
        try{
            Socket socket;
            socket = new Socket(this.hostName, this.port);
            outToServer = new ObjectOutputStream(socket.getOutputStream());
            inFromServer = new ObjectInputStream(socket.getInputStream());
            ClientSideServerListener listener1 = new ClientSideServerListener(this, this.maingui);
            Thread t = new Thread(listener1);
            t.start();
            while(!this.closeConnection){
                if(sendDataBool){
                    this.sendData();
                    sendDataBool = false;
                }
            }
            this.sendData();
            t.wait();
            socket.close();
            outToServer.close();
            inFromServer.close();
        }catch(IOException e){
            System.err.println("IOExpection Caught1");
        } catch (InterruptedException ex) {
            Logger.getLogger(ClypeClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Takes input from a user for different tasks.
     * 
     * Will list users, close the connection, or create and instance of FileClypeData to store data from a file specified by the user.
     * @param a
     */
    public void readClientData(String a){
//        System.out.println("Enter your input");
//        inFromStd = new Scanner(System.in);
//        String inFromStdS = inFromStd.nextLine();
        int spaceCount = 0;
        boolean sendfile = false;
        for (char c : a.toCharArray()){
            if (c == ' ') {
                spaceCount++;
            }
        }
        if(spaceCount == 1){
            sendfile = true;
        }
        String[] messages = a.split(": ", 2);
        String[] inArray = messages[1].split(" ");
        if(messages[1].equals("DONE")){
            this.dataToSendToServer = null;
            this.closeConnection = true;
            try {
                this.maingui.stop();
            } catch (Exception ex) {
                Logger.getLogger(ClypeClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(inArray[0].equals("SENDFILE") && sendfile){
            FileClypeData fileclype1 = new FileClypeData(this.userName, inArray[1], 2);
            try{
                fileclype1.readFileContents();
                this.dataToSendToServer = fileclype1;
            }catch(FileNotFoundException e){
                this.dataToSendToServer = null;
                System.err.println("The file was not found");
            }catch(IOException ioe){
                this.dataToSendToServer = null;
                System.err.println("There was an error opening or closing the file");
            }
        }
        else if(messages[1].equals("LISTUSERS")){
            this.dataToSendToServer = new MessageClypeData(this.userName, "LISTUSERS", 3);
        }
        else{
            this.dataToSendToServer = new MessageClypeData(this.userName, a, 3);
        }
        this.sendDataBool = true;
    }
    
    public void readClientPhoto(ImageIcon image){
        this.dataToSendToServer = new PictureClypeData(this.userName, image, 3);
        this.sendDataBool = true;
    }
    public void readClientPDF(File file){
        this.dataToSendToServer = new PDFClypeData(this.userName, file, 3);
        this.sendDataBool = true;
    }
    
    /**
     *
     */
    public void sendData(){
        try{
            outToServer.writeObject((ClypeData)dataToSendToServer);
        }catch(IOException E){
            System.err.println("IOException caught4");
        }
    }
    /**
     *
     */
    public void receiveData(){
        try{
            dataToReceiveFromServer = (ClypeData) inFromServer.readObject();
            getDataBool = true;
        }catch(IOException E){
            System.err.println("IOException caught");
        }catch (ClassNotFoundException E) {
            System.err.println("ClassNotFoundException caught");
        }
    }
    
    public void sendUserName(){
        try{
            outToServer.writeBytes(this.getUserName()+ "\n");
            outToServer.flush();
        }catch(IOException E){
            System.err.println("IOException caught1");
        }
    }
    /**
     *Prints the data from dataToReceiveFromServer object
     */
    public void printData(){
        try{
            System.out.println(dataToReceiveFromServer.getData());
        }catch(NullPointerException npe){
            System.err.println("A client has left the chat");
        }
    }
    public ClypeData getDataToReceiveFromServer(){
        return dataToReceiveFromServer;
    }
    
    public void setDataBool(boolean bool){
        this.getDataBool = bool;
    }
    public boolean getDataBool(){
        return this.getDataBool;
    }
    /**
     * 
     * @return
     */
    public String getUserName(){
        return this.userName;
    }
    /**
     *
     * @return
     */
    public boolean getClosedConnection(){
        return this.closeConnection;
    }
    /**
     *
     * @return
     */
    public String getHostName(){
        return this.hostName;
    }
    /**
     *
     * @return
     */
    public int getPort(){
        return this.port;
    }
    /**
     * returns a unique hashcode identifier for every different object
     * @return
     */
    @Override
    public int hashCode(){
        int result = 17;
        result = 37*result + this.userName.hashCode();
        result = 37*result + this.hostName.hashCode();
        return result;
    }
    /**
     * checks whether two objects of ClypeClient class are equal
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        else if (obj == null) {
            return false;
        }
        else if (getClass() != obj.getClass()) {
            return false;
        }
        final ClypeClient other = (ClypeClient) obj;
        if ((this.userName == null) ? (other.userName != null) : !this.userName.equals(other.userName)) {
            return false;
        }
        else if ((this.hostName == null) ? (other.hostName != null) : !this.hostName.equals(other.hostName)) {
            return false;
        }
        return true;
    }
    /**
     * returns a String that represents the data in the class object
     * 
     * returns userName, hostName, port, and the value of closeConnection.
     * 
     * @return
     */
    @Override
    public String toString(){
        String output;
        output = "Username: " + this.userName + "\n" + "Port: " + this.port + "\n" + "Hostname: " + this.hostName + "\n" + "Closed Connection: " + this.closeConnection + "\n";
        return output;
    }
    
    @Override
    public void run() {
        this.start(); //To change body of generated methods, choose Tools | Templates.
    }
}