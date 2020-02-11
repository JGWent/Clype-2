/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;
import javax.swing.ImageIcon;

/**
 *
 * @author manth
 */
public class PictureClypeData extends ClypeData{
    ImageIcon image1;
    
    public PictureClypeData(String userName, ImageIcon image, int type) {
            super(userName,type);
            this.image1 = image;
    }
    public PictureClypeData() {
            super(3);
            this.image1 = null;
    }
    
    public ImageIcon getImage(){
        return this.image1;
    }

    /**
     * getData()
     * Returns message
     * @return 
    */
    @Override
    public ImageIcon getData() {
        try{
            return this.image1;
        }catch(Exception e){
            System.err.println("hello");
            return this.image1;
        }
    }

    @Override
    public ImageIcon getData(String key) {
        return this.image1;
    }
}
