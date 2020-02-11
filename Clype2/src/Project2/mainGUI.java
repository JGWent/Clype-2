/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project2;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import main.*;
import javafx.application.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Desktop;
import javafx.stage.WindowEvent;
/**
 *
 * @author manth, John Graham
 * 
 */
public class mainGUI extends Application{
    private final Button sendBtn;
    private final Button sendFileBtn;
    private final Button sendPDFBtn;
    private final TextField textField1;
    private final TextArea textArea1;
    private final TextFlow textFlow1;
    private final Label label1;
    private final Label label2;
    private final ScrollPane scrollPane1;
    private ClypeClient client;
    private List<String> list;
    private boolean listEmpty;
    private String args[];
    private Thread clientThread;
    private String messages;
    private BufferedImage image;
    private TextField filePathText;
    private TextField PDFPathText;
    private Object media;
    
    public mainGUI(){
        sendBtn = new Button();
        sendBtn.getStyleClass().add("buttontheme");
        sendFileBtn = new Button();
        sendFileBtn.getStyleClass().add("buttontheme");
        sendPDFBtn = new Button();
        sendPDFBtn.getStyleClass().add("buttontheme");
        textField1 = new TextField();
        textArea1 = new TextArea();
        textFlow1 = new TextFlow();
        textFlow1.getStyleClass().add("text-flow");
        scrollPane1 = new ScrollPane();
        filePathText = new TextField();
        PDFPathText = new TextField();
        label1 = new Label();
        label1.getStyleClass().add("labeltheme");
        label2 = new Label();
        label2.getStyleClass().add("labeltheme");
        listEmpty = false;
        messages = "";
        image = null;
        try{
            Parameters params = getParameters();
            list = params.getRaw();
            args = new String[list.size()];
            for(int i=0; i<list.size(); i++){
                args[i] = list.get(i);
            }
            if(args.length == 1){
                if(args[0].contains("@")){
                    String arrayInput[] = args[0].split("@");
                    for(String arrayInput1 : arrayInput) {
                        System.out.println(arrayInput1);
                    }
                    if(args[0].contains(":")){
                        String arrayInput2[] = arrayInput[1].split(":");
                        client = new ClypeClient(arrayInput[0], arrayInput2[0], Integer.parseInt(arrayInput2[1]), this);
                        clientThread = new Thread(client);
                    }
                    else{
                        client = new ClypeClient(arrayInput[0], arrayInput[1], this);
                        clientThread = new Thread(client);
                    }
                }
                else{
                    client = new ClypeClient(args[0], this);
                    clientThread = new Thread(client);
                }
            }
            else{
                client = new ClypeClient(this);
                System.out.println(client.toString());
                clientThread = new Thread(client);
            }
        }catch(NullPointerException E){
            System.err.println("No command line arguments given");
            listEmpty = true;
            client = new ClypeClient(this);
            clientThread = new Thread(client);
        }
    }
 
    @Override
    public void start(Stage primaryStage){
        try{
            clientThread.start();
            VBox root = new VBox();
            primaryStage.setTitle("Clype Messanger");
            root.getStyleClass().add("greentheme");
            Scene scene = new Scene(root, 400, 400);
            scene.getStylesheets().add( getClass().getResource("application.css").toExternalForm() );
            HBox chatWindow = new HBox();
            HBox messageBar = new HBox();
            VBox fileBar = new VBox();
            sendBtn.setText("Send Message");//also send normal text files.
            sendPDFBtn.setText("Load and Send PDF");
            sendFileBtn.setText("Load and Send Image");
            SendBtnHandler sendHandler = new SendBtnHandler();
            sendFileBtn.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    String filePath = filePathText.getText();
                    try{
                        image = ImageIO.read(new File(filePath));
                        ImageIcon image5 = new ImageIcon(image);
                        client.readClientPhoto(image5);
                    }catch(IOException e){
                        System.err.println("File Not Found");
                        
                    }
                }
            });
            sendPDFBtn.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    String filePath = PDFPathText.getText();
                    try{
                        File file = new File(filePath);
                        client.readClientPDF(file);
                    }catch(Exception e){
                        System.err.println("File Not Found");
                        
                    }
                }
            });
            sendBtn.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    String text = textField1.getText();
                    textField1.clear();
                    String total = client.getUserName() + ": " + text;
                    client.readClientData(total);
                }
            });
            textField1.setOnKeyPressed(new EventHandler<KeyEvent>(){//must use textField user is using as an event for keyboard
                @Override
                public void handle(KeyEvent ke){
                    if(ke.getCode().equals(KeyCode.ENTER) || ke.getCharacter().getBytes()[0] == '\n' || ke.getCharacter().getBytes()[0] == '\r'){
                        String text = textField1.getText();
                        textField1.clear();
                        String total = client.getUserName() + ": " + text;
                        client.readClientData(total);
                    }
                    else{
                        //do nothing
                    }
                }
            });
            
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    String total = client.getUserName() + ": " + "DONE";
                    client.readClientData(total);
                    Platform.exit();
                    System.exit(0);
                }
            });
            
            textArea1.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                    String[] arr = newValue.split("\n");          
                    final Text textForFlow;     
                    String s = arr[arr.length - 1];
                    if(textFlow1.getChildren().isEmpty()){
                        textForFlow = new Text(s);
                    } else {
                    	textForFlow = new Text("\n" + s);
                    }
                    if(s.contains(":)")) {
            		textForFlow.setText(textForFlow.getText().replace(":)"," "));
            		Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            ImageView imageView = null;
                            try {
                                imageView = new ImageView( new Image(new FileInputStream("src/FriendSmiley.png")));
                            } catch (FileNotFoundException e) {
                                System.err.println(e.getMessage());
                            }
                            textFlow1.getChildren().addAll(textForFlow, imageView );								
                        }
                        });   
                    } else {
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                textFlow1.getChildren().add(textForFlow);
                            }
                        });                        
                    }
                    scrollPane1.setVvalue(scrollPane1.getVmax());
                }
            });
            textArea1.editableProperty().set(false);
            
            label1.setText(" Enter File path:");
            label2.setText(" Enter PDF File path:");
            

            scrollPane1.setMinSize(400, 200);
            scrollPane1.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            
            scrollPane1.setContent(textFlow1);
            
            chatWindow.getChildren().add(scrollPane1);
            messageBar.getChildren().add(textField1);
            messageBar.getChildren().add(sendBtn);
            
            fileBar.getChildren().add(sendFileBtn);
            fileBar.getChildren().add(label1);
            fileBar.getChildren().add(filePathText);
            fileBar.getChildren().add(sendPDFBtn);
            fileBar.getChildren().add(label2);
            fileBar.getChildren().add(PDFPathText);
            
            root.getChildren().add(chatWindow);
            root.getChildren().add(messageBar);
            root.getChildren().add(fileBar);
            
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(Exception E){
            System.err.println(E.getMessage());
        }
    }
    
    
    public void setTextfield2(String text){
        textArea1.setText(text);
    }
    
    public void setMessages(String text){
        messages += text + "\r\n";
        textArea1.setText(messages);
    }
    
    public void setPDF(byte[] byteArray){
        writePDF(byteArray);
        showPDF();
    }
    
    public void showPDF(){
        try {
            File pdfFile = new File("output111.pdf");
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("Awt Desktop is not supported!");
                }
            } else {
                System.out.println("File does not exist!");
            }
            System.out.println("Done");
	} catch (IOException ex) {
            System.err.println(ex.getMessage());
	}
    }
    
    public void writePDF(byte[] byteArray){
        try{
            OutputStream PDFout = new FileOutputStream("output111.pdf");//weird filename ensures user does not have identical file
            PDFout.write(byteArray);
            PDFout.close();
        }catch(FileNotFoundException e){
            System.err.println(e.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     *
     * @param image1
     */
    public void setImage(javax.swing.ImageIcon image1){
       JFrame imageFrame = new JFrame();
       int xlim = image1.getIconWidth();
       int ylim = image1.getIconHeight();
       imageFrame.setSize(xlim, ylim);
       JLabel imageLabel = new JLabel();
       imageLabel.setIcon(image1);
       imageFrame.add(imageLabel);
       imageFrame.setVisible(true);
    }
    
    @Override
    public void stop(){
        Platform.exit();
        System.exit(0);
    }
    
    public static void main(String[] args){
        launch(args);
    }
}