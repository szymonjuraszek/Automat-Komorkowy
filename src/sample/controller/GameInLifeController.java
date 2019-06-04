package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import sample.BadSizeException;
import sample.Colors;
import sample.SquareShape;
import sample.boundary.AbsorbingCondition;
import sample.boundary.BoundaryCondition;
import sample.boundary.PeriodicalCondition;
import sample.neighborhood.*;
import sample.structure.Cell;

import java.util.*;

public class GameInLifeController {

    private static int X;
    private static int Y;

    @FXML
    Canvas canvas;

    @FXML
    TextField sizeFieldX;
    @FXML
    TextField sizeFieldY;

    @FXML
    Button startButton;
    @FXML
    Button stopButton;

    @FXML
    RadioButton slowRadio;
    @FXML
    RadioButton fastRadio;
    @FXML
    RadioButton veryFastRadio;

    @FXML
    TextField rowText;
    @FXML
    TextField columnText;
    @FXML
    TextField radiusText;
    @FXML
    TextField pointsText;

    private GraphicsContext drawer;
    private Cell startPoints[][];
    private Cell tmpStartPoints[][];
    volatile static boolean ifStart = false;
    private static Neighborhood neighborhood = new Moore();
    private static BoundaryCondition condition = new PeriodicalCondition(45,80);
    private byte howManyDifferentColours=1;

    private void initCells(){
        startPoints= new Cell[X][Y];
        tmpStartPoints= new Cell[X][Y];
        for(int i=0;i<X;i++){
            for(int j=0;j<Y;j++){
                startPoints[i][j] = new Cell(i,j);
                tmpStartPoints[i][j]=new Cell(i,j);
            }
        }
    }







    @FXML
    void initialize(){

       System.out.println("Inicjuje dane dla game in life");
       sizeFieldX.setText("45");
       sizeFieldY.setText("80");
       slowRadio.setSelected(true);
       drawer = canvas.getGraphicsContext2D();

       X =Integer.parseInt(sizeFieldX.getCharacters().toString());
       Y =Integer.parseInt(sizeFieldY.getCharacters().toString());
        initCells();


       canvas.setWidth(Y* SquareShape.WIDTH);
       canvas.setHeight(X*SquareShape.HEIGHT);
//       int width = (int) (canvas.getWidth()/size);
//       int height = (int) (canvas.getHeight()/size);

       for(int i=0;i<X;i++){
           for(int j=0;j<Y;j++){
               drawer.strokeRect(j*SquareShape.WIDTH,i*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT);
           }
       }

        canvas.setOnMouseClicked(event -> {

            double x = event.getX(), y = event.getY();
//            System.out.println(" " + x + " " + y);
            for(int i=0;i<X;i++){
                if(event.getY()>i*SquareShape.WIDTH && event.getY()<((i+1)*SquareShape.WIDTH)){
                    for(int j=0;j<Y;j++){
                        if(event.getX()>j*SquareShape.HEIGHT && event.getX()<((j+1)*SquareShape.HEIGHT)) {

                            if(startPoints[i][j].getColorNumber() != 0){
//                                System.out.println(i);
//                                System.out.println(j);
                                drawer.setFill(Color.WHITE);
                                drawer.fillRoundRect(j*SquareShape.WIDTH,i*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT,0,0);
                                startPoints[i][j].setColorNumber(0);
                            }
                            else{
                                drawer.setFill(Colors.getColor(howManyDifferentColours));
                                startPoints[i][j].setColorNumber(howManyDifferentColours);
                                if(howManyDifferentColours<3600){
                                    howManyDifferentColours++;
                                }else{
                                    howManyDifferentColours=0;
                                }

                                drawer.fillRoundRect(j*SquareShape.WIDTH,i*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT,0,0);

                            }

                            break;
                        }

                    }
                }

            }
            drawer.setFill(Color.BLACK);
        });


       slowRadio.setOnMouseClicked(event -> {
           fastRadio.setSelected(false);
           veryFastRadio.setSelected(false);
       });

       fastRadio.setOnMouseClicked(event -> {
           slowRadio.setSelected(false);
           veryFastRadio.setSelected(false);
       });

       veryFastRadio.setOnMouseClicked(event -> {
           slowRadio.setSelected(false);
           fastRadio.setSelected(false);
       });

    }

    @FXML
    public void stop(){
        ifStart=false;
    }

    @FXML
    public void start(){
        if(!ifStart) {

            sizeFieldX.setEditable(false);
            sizeFieldY.setEditable(false);
            if(neighborhood instanceof RadiusNeighborhood){
                ((RadiusNeighborhood) neighborhood).setRadious(Double.parseDouble(radiusText.getCharacters().toString()));
            }

            ifStart = true;
            new Thread(() -> {

                while (ifStart) {
                    for (int i = 0; i < X; i++) {
                        for (int j = 0; j < Y; j++) {
                            // na przyszlosc mozna tu zrobi poprawke zeby calej tablic nie przesylac
                            neighborhood.check(condition,startPoints,i,j,tmpStartPoints);
                        }
                    }


//                     program dziala za szybko i nie dziala wiec spowolnienie
                    try {
                        if(slowRadio.isSelected()){
                            Thread.sleep(1000);
                        }else if(fastRadio.isSelected()){
                            Thread.sleep(250);
                        }else{
                            Thread.sleep(150);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // przenosimy obliczone punkty do oryginalnej tablicy
                    for (int i = 0; i < X; i++) {
                        for (int j = 0; j < Y; j++) {

                            startPoints[i][j].setColorNumber(tmpStartPoints[i][j].getColorNumber());
//                            System.out.print(startPoints[i][j].getColorNumber()+ " ");
                            tmpStartPoints[i][j].setColorNumber(0);
                        }
//                        System.out.println();
                    }
                    preparationBeforeDrawing();
                    drawOnCanvasRectangles(startPoints);

                }

                sizeFieldX.setEditable(true);
                sizeFieldY.setEditable(true);
            }).start();

        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Program is working(pick stop if yo want to stop)");
            alert.showAndWait();
        }
        System.out.println("Skonczylem prace");

    }











/*******************************           Ustawianie sasiedztwa i waunkow brzegowych                 *******************************/
    @FXML
    void setPeriodicalBoundary(){
        condition = new PeriodicalCondition(X,Y);
    }

    @FXML
    void setAbsorbingBoundary(){
        condition = new AbsorbingCondition(X,Y);
    }

    @FXML
    void setVonNewman(){
        neighborhood = new VonNewman();
    }

    @FXML
    void setMoore(){
        neighborhood = new Moore();
    }

    @FXML
    void setHexagonalLeft(){
        neighborhood = new HexagonalLeft();
    }

    @FXML
    void setHexagonalRight(){
        neighborhood = new HexagonalRight();
    }

    @FXML
    void setHexagonalRandom(){
        neighborhood = new HexagonalRandom();
    }

    @FXML
    void setPentagonalRandom(){
        neighborhood = new PentagonalRandom();
    }

    @FXML
    void setRadius(){
        neighborhood = new RadiusNeighborhood();
    }

    /**********************************************           Sposoby ustawiania ziaren                   **************************************************/
    @FXML
    public void setRandomShape(){

        if(!ifStart){
            Random random = new Random();
            preparationBeforeDrawing();
            clear();
            int indexX;
            int indexY;

            for(int i=0;i<X;i++){
                for(int j=0;j<Y;j++){
                    indexX = random.nextInt(X);
                    indexY = random.nextInt(Y);
                    startPoints[indexX][indexY].setColorNumber(1);
                }
            }

            drawOnCanvasRectangles(startPoints);
        }else{
            System.out.println("Pracuje watek nie mozna");
        }

    }

    @FXML
    public void setGliderShape(){

        if(!ifStart){
            preparationBeforeDrawing();
            clear();

            startPoints[5][6].setColorNumber(1);
            startPoints[5][5].setColorNumber(1);
            startPoints[6][4].setColorNumber(1);
            startPoints[6][5].setColorNumber(1);
            startPoints[7][6].setColorNumber(1);

            drawOnCanvasRectangles(startPoints);
        }else{
            System.out.println("Pracuje watek nie mozna");
        }
    }

    @FXML
    public void setStateShape(){

        if(!ifStart){
            preparationBeforeDrawing();
            clear();

            startPoints[5][5].setColorNumber(1);
            startPoints[5][6].setColorNumber(1);
            startPoints[6][4].setColorNumber(1);
            startPoints[6][7].setColorNumber(1);
            startPoints[7][5].setColorNumber(1);
            startPoints[7][6].setColorNumber(1);

            drawOnCanvasRectangles(startPoints);
        }else{
            System.out.println("Pracuje watek nie mozna");
        }

    }

    @FXML
    public void setOscellatorShape(){

        if(!ifStart){
            preparationBeforeDrawing();
            clear();

            startPoints[5][5].setColorNumber(1);
            startPoints[6][5].setColorNumber(1);
            startPoints[7][5].setColorNumber(1);

            drawOnCanvasRectangles(startPoints);
        }else{
            System.out.println("Pracuje watek nie mozna");
        }

    }



    @FXML
    void setSeedWithRadius(){
        if(!ifStart) {
            long startTime = System.currentTimeMillis();
            //System.out.println(startTime);
            preparationBeforeDrawing();
            clear();
            Random r = new Random();
            int x;
            int y;
            byte counter = 0;

            int radius = Integer.parseInt(radiusText.getCharacters().toString());
            int numberOfSeed = Integer.parseInt(pointsText.getCharacters().toString());

            int howManySquare = radius / SquareShape.WIDTH;

            List<Double> listX = new LinkedList<>();
            List<Double> listY = new LinkedList<>();

            do {
                //System.out.println(System.currentTimeMillis());
                if(System.currentTimeMillis()-startTime >10000){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cant find all points, found: " + listX.size());
                    alert.showAndWait();
                    break;

                }
                x = r.nextInt(X);
                y = r.nextInt(Y);


                boolean flag = true;
                if (startPoints[x][y].getColorNumber() == 0) {
                    for (int i = 0; i < counter; i++) {
                        double lengthBetweenPoints = Math.sqrt(Math.pow(x * SquareShape.HEIGHT + SquareShape.HEIGHT / 2 - listX.get(i), 2) + Math.pow(y * SquareShape.WIDTH + SquareShape.WIDTH / 2 - listY.get(i), 2));
                        //System.out.println(lengthBetweenPoints);
//                        System.out.println(x);
//                        System.out.println(y);
                        if (lengthBetweenPoints <= radius) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        counter++;
//                System.out.println(counter);
                        startPoints[x][y].setColorNumber(counter);
                        double positionX = x * SquareShape.HEIGHT + SquareShape.HEIGHT / 2;
                        double positionY = y * SquareShape.WIDTH + SquareShape.WIDTH / 2;
                        listX.add(positionX);
                        listY.add(positionY);
                    }

                }

            } while (counter < numberOfSeed);

            drawOnCanvasRectangles(startPoints);
        }
    }


    @FXML
    void setSeedHomogeus(){
        if(!ifStart) {
            preparationBeforeDrawing();
            clear();

            int row = Integer.parseInt(rowText.getCharacters().toString());
            int column = Integer.parseInt(columnText.getCharacters().toString());

            int positionX = Y / column;
            int positionY = X / row;
            int counter = 1;

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    startPoints[i * positionY][j * positionX].setColorNumber(counter);
                    counter++;
                }

            }

            drawOnCanvasRectangles(startPoints);
        }

    }

    @FXML
    void setSeedRandom(){
        if(!ifStart) {
            preparationBeforeDrawing();
            clear();
            long startTime = System.currentTimeMillis();

            Random r = new Random();
            int x;
            int y;
            int counter = 0;

            int numberOfSeed;

            numberOfSeed =checkNumberOfPoints();


            do {
                x = r.nextInt(X);
                y = r.nextInt(Y);
//            System.out.println(x);
//            System.out.println(y);
                if (startPoints[x][y].getColorNumber() == 0) {
                    counter++;
//                System.out.println(counter);
                    startPoints[x][y].setColorNumber(counter);
                }
                //System.out.println(counter);
            } while (counter < numberOfSeed);

            drawOnCanvasRectangles(startPoints);
        }
    }




    /****************************************************          Metody prywatne            **************************************************************/

    private void setSize(){
        try{
            X = Integer.parseInt(sizeFieldX.getCharacters().toString());
            Y = Integer.parseInt(sizeFieldY.getCharacters().toString());
            checkSize();
        }catch(NumberFormatException e){
            //e.printStackTrace();
            System.err.println("Nie podano liczby! BLAD!!");
            sizeFieldX.setText("45");
            sizeFieldY.setText("80");
            X=45;
            Y=80;
        }catch(BadSizeException e){
            e.messageError("Bad size canvas only (X: 10 to 45 and Y: 10 to 80");
            sizeFieldX.setText("45");
            sizeFieldY.setText("80");
            X=45;
            Y=80;
        }
        finally {
            condition.setSizeX(X);
            condition.setSizeY(Y);
        }
    }

    private void preparationBeforeDrawing(){
        drawer.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        if(!ifStart) {
            setSize();
        }
        if(X!=startPoints.length || Y!=startPoints[0].length){
            System.out.println("jestem przy init");
            initCells();
        }
    }

    private void drawOnCanvasRectangles(Cell startPoints[][]){

        //System.out.println("Jestem w rysowaniu");
        //System.out.println(Thread.currentThread());
        canvas.setWidth(Y* SquareShape.WIDTH);
        canvas.setHeight(X*SquareShape.HEIGHT);

        for(int i=0;i<X;i++){
            ww:
            for(int j=0;j<Y;j++){

                for(int w=1;w< Colors.getNumberOfColors();w++){
                    if(startPoints[i][j].getColorNumber()==w){
                        drawer.setFill(Colors.getColor(w-1));
                        drawer.fillRoundRect(j*SquareShape.WIDTH,i*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT,0,0);
                        continue ww;
                    }
                }
                drawer.strokeRect(j*SquareShape.WIDTH,i*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT);
            }
        }
    }

    private void clear(){
        for(int i=0;i<X;i++){
            for(int j=0;j<Y;j++){
                startPoints[i][j].setColorNumber(0);
            }
        }
    }

    private void checkSize() throws BadSizeException{
        if(X>45 || X<10 || Y>80 || Y<10){
            throw new BadSizeException();
        }
    }

    private int checkNumberOfPoints(){

        int numberOfSeed;
        try{
            numberOfSeed = Integer.parseInt(pointsText.getCharacters().toString());
        }catch(NumberFormatException e){
            System.err.println("Nie podano liczby! BLAD!!");
            pointsText.setText("10");
            numberOfSeed=10;
        }

        if(X*Y < numberOfSeed || numberOfSeed<1){
            pointsText.setText("10");
            numberOfSeed=10;
        }

        return numberOfSeed;
    }



}
