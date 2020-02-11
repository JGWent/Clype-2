/**
*
* The Class that will take care of file transfers for the Clype Application
* Has 2 Constructors
* Has 2 private properties
* Has 0 default vars
* Has 8 methods
* @author John Graham
*
*
*/
package data;
import java.io.*;

/**
 *
 * @author manth
 */
public class FileClypeData extends ClypeData{
    private String fileName;
    private String fileContents;
    /**
     * Default constructor
     */
    public FileClypeData(){
        super();
    }

    /**
     * Constructor when given username, filename, and type.
     * @param uname
     * @param filename
     * @param itype
     */
    public FileClypeData(String uname, String filename, int itype){
        super(uname, itype);
        this.fileName = filename;
        this.fileContents = null;
    }

    /**
     * Changes filename to the given filename.
     * @param filename
     */
    public void setFileName(String filename){
        this.fileName = filename;
    }

    /**
     * returns filename
     * @return
     */
    public String getFileName(){
        return this.fileName;
    }

    /**
     * reads the contents of the file and stores the contents in the fileContents variable.
     * @throws IOException
     */
    public void readFileContents()throws IOException{
        FileReader reader = new FileReader(this.fileName);
        boolean doneReadingFile = false;
        while(!doneReadingFile){
            char character = (char)reader.read();
            if(((int)character) == 65535){
                doneReadingFile = true;
            }
            else{
                fileContents += character;
            }
        }
        reader.close();
    }

    /**
     * reads the contents of the file and stores encrypted contents in the fileContents variable.
     * @param key
     * @throws IOException
     */
    public void readFileContents(String key) throws IOException{
        FileReader reader = new FileReader(this.fileName);
        boolean doneReadingFile = false;
        while(!doneReadingFile){
            char character = (char)reader.read();
            if(((int)character) == 65535){
                doneReadingFile = true;
            }
            else{
                this.fileContents += character;
            }
        }
        reader.close();
        this.fileContents = this.encrypt(this.fileContents, key);
        System.out.println(this.fileContents);
    }

    /**
     * Writes fileContents to the file
     * @throws IOException
     */
    public void writeFileContents()throws IOException{
        FileWriter writer = new FileWriter(this.fileName);
        for(int i=0; i<this.fileContents.length(); i++){
            writer.write(this.fileContents.charAt(i));
        }
        writer.close();
    }

    /**
     * writes decrypted fileContents to the file
     * @param key
     * @throws IOException
     */
    public void writeFileContents(String key)throws IOException{
        FileWriter writer = new FileWriter(this.fileName);
        this.fileContents = this.decrypt(this.fileContents, key);
        for(int i=0; i<this.fileContents.length(); i++){
            writer.write(this.fileContents.charAt(i));
        }
        writer.close();
    }
    /**
     * returns the fileContents
     * @return
     */
    @Override
    public String getData(){
        return this.fileContents;
    }
    /**
     * returns the decrypted fileContents
     * @return
     */
    @Override
    public String getData(String key){
        return this.decrypt(this.fileContents, key);
    }
    /**
     * returns a unique value for every different FileClypeData object.
     * @return
     */
    @Override
    public int hashCode(){
        int result = 17;
        try{
            result = 37*result + this.fileName.hashCode();
            result = 37*result + this.fileContents.hashCode();
        }catch(Exception e){
            return result;
        }
        return result;
    }
    /**
     * check equality of two objects of type FileClypeData.
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
        final FileClypeData other = (FileClypeData) obj;
        if ((this.fileContents == null) ? (other.fileContents != null) : !this.fileContents.equals(other.fileContents)) {
            return false;
        }
        return true;
    }
    /**
     * returns a String representation of the class object.
     * @return
     */
    @Override
    public String toString(){
        String output;
        output = "Filename: " + this.fileName + "\n" + "File Contents:" + this.fileContents + "\n";
        return output;
    }
}
