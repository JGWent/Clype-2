/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;
import java.util.*;
import java.io.Serializable;
import javax.swing.ImageIcon;
/**
 *
 * @author manth
 */
public abstract class ClypeData<T> implements Serializable{
    private String userName;
    private int type;//0 lists all connected users, 1 logout, 2 send file, 3 send message
    private Date date;

    /**
     * Default constructor
     */
    public ClypeData(){
        this("Anon", 0);//sets type to zeron
    }

    /**
     * Constructor for when only type is provided
     * @param itype
     */
    public ClypeData(int itype){
        this("Anon", itype);
    }

    /**
     * Constructor for when type and username are provided
     * @param uname
     * @param itype
     */
    public ClypeData(String uname, int itype){
        this.userName = uname;
        this.type = itype;
        date = new Date();
        if(this.type==2){
        }
        if(this.type==0 || this.type==1 || this.type==3){
        }
    }

    /**
     * Encrypts data with the Vignere cipher by using a key.
     * 
     * Encrypts only upper and lower case letters. Does not encrypt numbers and special characters.
     * 
     * @param inputStringToEncrypt
     * @param key
     * @return
     */
    protected String encrypt(String inputStringToEncrypt, String key ){
        String output = "";
        String upkey = key.toUpperCase();
        //little a is 97 big A is 65
        for(int i=0; i<inputStringToEncrypt.length(); i++){
            int rotateKey = (int)(upkey.charAt(i%4)) - 64;
            if(inputStringToEncrypt.charAt(i)>64 && inputStringToEncrypt.charAt(i)<91){//if capital letter
                int rotateCharCap = (((int)inputStringToEncrypt.charAt(i)-65+rotateKey)%26)+65;
                output += (char)rotateCharCap;
            }
            else if(inputStringToEncrypt.charAt(i)>96 && inputStringToEncrypt.charAt(i)<123){
                int rotateCharCap = (((int)inputStringToEncrypt.charAt(i)-97+rotateKey)%26)+97;
                output += (char)rotateCharCap;
            }
            else{
                output += inputStringToEncrypt.charAt(i);
            }
        }
        return output;
    }

    /**
     * Decrypts data encrypted with the Vignere cipher by using a key.
     * 
     * Decrypts only upper and lower case letters. Does not decrypt numbers and special characters. 
     * 
     * @param inputStringToDecrypt
     * @param key
     * @return
     */
    protected String decrypt(String inputStringToDecrypt, String key){
        String output = "";
                String upkey = key.toUpperCase();
        for(int i=0; i<inputStringToDecrypt.length(); i++){
            int rotateKey = (int)(upkey.charAt(i%4)) - 64;
            if(inputStringToDecrypt.charAt(i)>64 && inputStringToDecrypt.charAt(i)<91){//if capital letter
                int rotateCharCap = ((int)inputStringToDecrypt.charAt(i)-65-rotateKey);
                if(rotateCharCap < 0){
                    output += (char)((26+rotateCharCap)+65);
                }
                else{
                    output += (char)(rotateCharCap+65);
                }
            }
            else if(inputStringToDecrypt.charAt(i)>96 && inputStringToDecrypt.charAt(i)<123){
                int rotateCharCap = ((int)inputStringToDecrypt.charAt(i)-97-rotateKey);
                if(rotateCharCap < 0){
                    output += (char)((26+rotateCharCap)+97);
                }
                else{
                    output += (char)(rotateCharCap+97);
                }
            }
            else{
                output += inputStringToDecrypt.charAt(i);
            }
        }
        return output;
    }

    /**
     * returns the type identifier
     * @return
     */
    public int getType(){
        return this.type;
    }

    /**
     * returns the date
     * @return
     */
    public Date getDate(){
        return this.date;
    }
    
    /**
     * returns the username
     * @return
     */
    public String getUserName(){
        return this.userName;
    }

    /**
     * returns a string for a given ClypeData instance.
     * @param <T>
     * @return
     */
    public abstract T getData();

    /**
     * returns a string for a given ClypeData instance given a key.
     * @param <T>
     * @param key
     * @return
     */
    public abstract T getData(String key);
}