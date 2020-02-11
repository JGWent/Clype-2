/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author manth
 */
public class PDFClypeData extends ClypeData{
    byte[] bytesArray;
    
    public PDFClypeData(String userName, File file, int type) {
            super(userName,type);
            bytesArray = null;
            convertToArray(file);
    }
    public PDFClypeData() {
            super(3);
            bytesArray = null;
    }

    @Override
    public byte[] getData() {
        return this.bytesArray;
    }

    @Override
    public byte[] getData(String key) {
         return this.bytesArray;
    }
    
    private void convertToArray(File file){
        bytesArray = new byte[(int) file.length()];
        try{
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
}
