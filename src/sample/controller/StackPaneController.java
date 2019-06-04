package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Rule;


import java.util.Random;

public class StackPaneController {

    @FXML
    ScrollPane containerScrollPane;

    @FXML
    private TextField iterationTextField;

    @FXML
    private Canvas canvas1;

    @FXML
    private TextField sizeField;

    @FXML
    private ChoiceBox<Integer> boxRules;

    private Rule rules;
    //private PixelWriter writer;
    private GraphicsContext drawer;


    public StackPaneController(){
        System.out.println("jestem w controllerze stackaPane");
    }

    @FXML
    void initialize(){
        // Dopiero tutaj jest widoczny zainicjalizowany obiekt Button
        System.out.println("Inicjalizuje dane w StackPaneController");
        boxRules.getItems().add(rules.RULE_30);
        boxRules.getItems().add(rules.RULE_60);
        boxRules.getItems().add(rules.RULE_90);
        boxRules.getItems().add(rules.RULE_150);
        boxRules.getItems().add(rules.RULE_250);
        boxRules.setValue(rules.RULE_30);

        drawer = canvas1.getGraphicsContext2D();
    }

    @FXML
    public void draw(ActionEvent event){

        drawer.clearRect(0,0,canvas1.getWidth(),canvas1.getHeight());

        int size =Integer.parseInt(sizeField.getCharacters().toString());
        if(size<4 || size>50){
            size = 10;
            sizeField.setText("10");
        }

        int amountOfIteration =Integer.parseInt(iterationTextField.getCharacters().toString());

        int width = (int) (canvas1.getWidth()/size +1);
        int height = (int) (canvas1.getWidth()/size+1);

        boolean[] startPoints = setInitialPoint(width,height,size,amountOfIteration);
        boolean [] tmpPoints = new boolean[size];

        String binaryRepr = Integer.toBinaryString(boxRules.getValue());
        boolean []binaryTab;
        binaryTab = Rule.createValueRep(binaryRepr);

        int index=7;
        int x;
        for(int i=1;i<amountOfIteration;i++){
            for(int j=0;j<size;j++){

                x=j;
                for(int k=-1;k<2;k++){
                    x= j+k;
                    //warunki brzegowe
                    if(x<0){
                        x=size-1;
                    }
                    if(x>size-1){
                        x=0;
                    }

                    if(startPoints[x]){
                        if(k==-1){
                            index-=4;
                        }else if(k==0){
                            index-=2;
                        }else{
                            index-=1;
                        }
                    }
                }

                if(binaryTab[index]){
                    drawer.fillRoundRect(j*width,i*height,width,height,0,0);
                    tmpPoints[j]=true;
                }else{
                    tmpPoints[j]=false;
                }
                index=7;
            }
            for(int j=0;j<size;j++){
                startPoints[j]=tmpPoints[j];
            }
        }



   }

   public boolean[] setInitialPoint(int width,int height,int size,int amountOfIteration){

        Random random = new Random();
        boolean []startPoints = new boolean[size];
        int index;
        canvas1.setHeight(height*amountOfIteration);


        for(int i=0;i<size;i++){
            index = random.nextInt(size);
            startPoints[index] = true;
        }

        for(int i=0;i<startPoints.length;i++){
            startPoints[i]=false;
        }
        startPoints[size/2]=true;


        for(int i=0;i<amountOfIteration;i++){
            for(int j=0;j<size;j++){
                if(startPoints[j] && i==0){
                    drawer.fillRoundRect(j*width,0,width,height,0,0);
                }
                drawer.strokeRect(j*width,i*height,width,height);
            }
        }




        return startPoints;
    }




}


