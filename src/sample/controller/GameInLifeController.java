package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import sample.BadSizeException;
import sample.Colors;
import sample.ConstantValue;
import sample.SquareShape;
import sample.boundary.AbsorbingCondition;
import sample.boundary.BoundaryCondition;
import sample.boundary.PeriodicalCondition;
import sample.neighborhood.*;
import sample.structure.Cell;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GameInLifeController {

    private static int X;
    private static int Y;
    public static final double  J=1.0;

    @FXML
    Canvas canvas;

    @FXML
    TextField sizeFieldX;
    @FXML
    TextField sizeFieldY;
    @FXML
    TextField constantText;
    @FXML
    TextField iterationText;

    @FXML
    Button startButton;
    @FXML
    Button stopButton;
    @FXML
    Button monteCarloButton;

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

       System.out.println("Inicjalizuje dane dla programu");
       sizeFieldX.setText("45");
       sizeFieldY.setText("80");
       constantText.setText("0.6");
       slowRadio.setSelected(true);
       drawer = canvas.getGraphicsContext2D();

       X =Integer.parseInt(sizeFieldX.getCharacters().toString());
       Y =Integer.parseInt(sizeFieldY.getCharacters().toString());
       initCells();

       canvas.setWidth(Y* SquareShape.WIDTH);
       canvas.setHeight(X*SquareShape.HEIGHT);

       for(int i=0;i<X;i++){
           for(int j=0;j<Y;j++){
               drawer.strokeRect(j*SquareShape.WIDTH,i*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT);
           }
       }

        canvas.setOnMouseClicked(event -> {
            for(int i=0;i<X;i++){
                if(event.getY()>i*SquareShape.WIDTH && event.getY()<((i+1)*SquareShape.WIDTH)){
                    for(int j=0;j<Y;j++){
                        if(event.getX()>j*SquareShape.HEIGHT && event.getX()<((j+1)*SquareShape.HEIGHT)) {
                            if(startPoints[i][j].getColorNumber() != 0){
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
           slowRadio.setSelected(true);
           fastRadio.setSelected(false);
           veryFastRadio.setSelected(false);
       });

       fastRadio.setOnMouseClicked(event -> {
           slowRadio.setSelected(false);
           fastRadio.setSelected(true);
           veryFastRadio.setSelected(false);
       });

       veryFastRadio.setOnMouseClicked(event -> {
           slowRadio.setSelected(false);
           fastRadio.setSelected(false);
           veryFastRadio.setSelected(true);
       });

    }

    @FXML
    public void stop(){
        ifStart=false;
        startButton.setDisable(false);
        monteCarloButton.setDisable(false);
    }

    @FXML
    public void start(){
        if(!ifStart) {

            monteCarloButton.setDisable(true);
            startButton.setDisable(true);
            sizeFieldX.setEditable(false);
            sizeFieldY.setEditable(false);
            if(neighborhood instanceof RadiusNeighborhood){
                if(Double.parseDouble(radiusText.getCharacters().toString())>40){
                    radiusText.setText("20");
                }
                ((RadiusNeighborhood) neighborhood).setRadious(Double.parseDouble(radiusText.getCharacters().toString()));
            }

            ifStart = true;
            new Thread(() -> {

                while (ifStart) {
                    for (int i = 0; i < X; i++) {
                        for (int j = 0; j < Y; j++) {
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
                    drawOnCanvasRectangles(startPoints,true);

                }

                sizeFieldX.setEditable(true);
                sizeFieldY.setEditable(true);
                System.err.println("Skonczylem rozrost ziarna metoda CA");
            }).start();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Program is working(pick stop if yo want to stop)");
            alert.showAndWait();
        }
    }

    @FXML
    void showCA(){
        drawOnCanvasRectangles(startPoints,true);
    }












/*******************************           Ustawianie sasiedztwa i warunkow brzegowych                 *******************************/
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

            drawOnCanvasRectangles(startPoints,true);
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

            drawOnCanvasRectangles(startPoints,true);
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

            drawOnCanvasRectangles(startPoints,true);
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

            drawOnCanvasRectangles(startPoints,true);
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

            drawOnCanvasRectangles(startPoints,true);
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

            drawOnCanvasRectangles(startPoints,true);
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

            drawOnCanvasRectangles(startPoints,true);
        }
    }

    /*****************************************************************             Monte Carlo             **************************************************************************/

    @FXML
    void countMonteCarlo(){
        if(!ifStart) {

            startButton.setDisable(true);
            monteCarloButton.setDisable(true);

            sizeFieldX.setEditable(false);
            sizeFieldY.setEditable(false);

            ifStart = true;
            new Thread(() -> {

                Random random = new Random();
                int x;
                int y;
                int iterations = Integer.parseInt(iterationText.getCharacters().toString());

                for (int g = 0; g < iterations; g++) {
                    for (x = 0; x < X; x++) {
                        for (y = 0; y < Y; y++) {
                            if(ifStart){

                                int energy= (int) (neighborhood.check(condition,startPoints,x,y,tmpStartPoints)*J);
                                Cell cell[][] = new Cell[1][1];
                                cell[0][0]=new Cell();
                                int newValue=neighborhood.giveRandomIdNeighborhood(startPoints,condition,x,y);
                                cell[0][0].setColorNumber(newValue);
                                int oldValue = startPoints[x][y].getColorNumber();
                                startPoints[x][y].setColorNumber(cell[0][0].getColorNumber());
                                //tmpStartPoints[x][y].setColorNumber(cell[0][0].getColorNumber());
                                int tmpEnergy = (int) (neighborhood.check(condition,startPoints,x,y,tmpStartPoints)*J);

                                int differenceEnergy = tmpEnergy-energy;

                                if(differenceEnergy>0){
                                    double likelihood=Math.exp((-1*differenceEnergy)/Double.parseDouble(constantText.getCharacters().toString()));
//                                    System.out.println("prawdopodobienstwo:  " + likelihood);
                                    if(random.nextDouble()>likelihood){
                                        startPoints[x][y].setColorNumber(oldValue);
                                        startPoints[x][y].setEnergy(energy);
                                        tmpStartPoints[x][y].setEnergy(energy);
                                    }else {
                                        drawer.setFill(Colors.getColor(newValue-1));
                                        startPoints[x][y].setEnergy(tmpEnergy);
                                        tmpStartPoints[x][y].setEnergy(energy);
                                        drawer.fillRoundRect(y*SquareShape.WIDTH,x*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT,0,0);
                                    }
                                }else{
                                    startPoints[x][y].setColorNumber(oldValue);
                                    startPoints[x][y].setEnergy(energy);
                                    tmpStartPoints[x][y].setEnergy(energy);
                                }
                            }

                        }
                    }
                }


////                    for(int i=0;i<iterations;i++){
////                        if(ifStart){
////                            x = random.nextInt(X);
////                            y = random.nextInt(Y);
////                            int energy= (int) (neighborhood.check(condition,startPoints,x,y,tmpStartPoints)*J);
////                            Cell cell[][] = new Cell[1][1];
////                            cell[0][0]=new Cell();
////                            int newValue=neighborhood.giveRandomIdNeighborhood(startPoints,condition,x,y);
////                            cell[0][0].setColorNumber(newValue);
////                            int oldValue = startPoints[x][y].getColorNumber();
////                            startPoints[x][y].setColorNumber(cell[0][0].getColorNumber());
////                            int tmpEnergy = (int) (neighborhood.check(condition,startPoints,x,y,tmpStartPoints)*J);
////
////                            int differenceEnergy = tmpEnergy-energy;
////
////                            if(differenceEnergy>0){
////                                double likelihood=Math.exp(-differenceEnergy/Double.parseDouble(constantText.getCharacters().toString()));
////                                if(random.nextDouble()<likelihood){
////                                    startPoints[x][y].setColorNumber(oldValue);
////                                }else {
////                                    drawer.setFill(Colors.getColor(newValue-1));
////                                    drawer.fillRoundRect(y*SquareShape.WIDTH,x*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT,0,0);
////                                }
////                            }else{
////                                startPoints[x][y].setColorNumber(oldValue);
////                            }
////                        }else{
////                            break;
////                        }
//                }

                sizeFieldX.setEditable(true);
                sizeFieldY.setEditable(true);
                stop();
            }).start();

        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Program is working(pick stop if yo want to stop)");
            alert.showAndWait();
        }
        System.out.println("Skonczylem prace");
    }

    @FXML
    void showEnergy(){
        canvas.setWidth(Y* SquareShape.WIDTH);
        canvas.setHeight(X*SquareShape.HEIGHT);

        drawer.setFill(Color.WHITE);
        for (int i = 0; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                drawer.fillRoundRect(j*SquareShape.WIDTH,i*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT,0,0);
                drawer.strokeRect(j*SquareShape.WIDTH,i*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT);
            }

        }

        for(int i=0;i<X;i++){
            ww:
            for(int j=0;j<Y;j++){

                for(int w=1;w< Colors.getNumberOfColors();w++){
                    if(tmpStartPoints[i][j].getEnergy()==w){
                        drawer.setFill(Colors.getColor(w-1));
                        drawer.fillRoundRect(j*SquareShape.WIDTH,i*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT,0,0);
                        continue ww;
                    }
                }
                drawer.strokeRect(j*SquareShape.WIDTH,i*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT);
            }
        }
    }

    /*************************************************           Rekrystalizacja                 *******************************************************************************************************/

    @FXML
    public void recrystallize() throws IOException {

        int iterations = Integer.parseInt(iterationText.getCharacters().toString());
        Random random = new Random();

        Cell.CRITICAL_DISSLOCATION= (ConstantValue.A/ ConstantValue.B + (1 - ConstantValue.A/ ConstantValue.B)*Math.exp(-(ConstantValue.B*0.065)))/(X*Y);
        System.out.println(Cell.CRITICAL_DISSLOCATION);

        //1.1710667062009494E9

        for(int i=0;i<iterations;i++){
            double roFirst = ConstantValue.A/ ConstantValue.B + (1 - ConstantValue.A/ ConstantValue.B)*Math.exp(-(ConstantValue.B*ConstantValue.TIME*i));
            double roSecond = ConstantValue.A/ ConstantValue.B + (1 - ConstantValue.A/ ConstantValue.B)*Math.exp(-(ConstantValue.B*((i+1)*0.001)));
            double roDelta = roSecond-roFirst;
            double overageDislocation = roDelta/(X*Y);
            double percent = random.nextDouble();
            double pack = overageDislocation*percent;

//            System.out.println("overageDislocation " + overageDislocation);
//            System.out.println("percent:" + percent);
            //System.out.println("Paczka po 1 % " + pack);
            for(int x=0;x<X;x++){
                for(int y=0;y<Y;y++){

                    startPoints[x][y].addDislocationDensity(pack);


                    //System.out.print(startPoints[x][y].getDislocationDensity()+ "   ");
                }
                //System.out.println();
            }

            int howManyPacks = random.nextInt(100);
            pack = (overageDislocation*(1 - percent))/howManyPacks;
            //System.out.println("Paczka po 2 % " + pack);
            //System.out.println("howManyPacks "+ howManyPacks);
            double likelihood = 0.8;
            int z;

            wew1:
            for(z=0;z<howManyPacks;z++){

                boolean flag=true;
                double randomPoint = random.nextDouble();

                // na granicy ziarna
                if(randomPoint<=likelihood){

                    do {
                        int indexX = random.nextInt(X);
                        int indexY = random.nextInt(Y);
                        // Moore

                        for (int w = -1; w < 2; w++)
                            for (int g = -1; g < 2; g++) {
                                if (((startPoints[condition.funY(indexX + w)][condition.funX(indexY + g)].getColorNumber() != startPoints[indexX][indexY].getColorNumber()))) {

                                    startPoints[indexX][indexY].addDislocationDensity(pack);


                                    if(startPoints[indexX][indexY].getDislocationDensity()>Cell.CRITICAL_DISSLOCATION){
                                        startPoints[indexX][indexY].setColorNumber(2000);
                                        startPoints[indexX][indexY].setIfGlacial(true);
                                        startPoints[indexX][indexY].setDislocationDensity(0);
                                    }
                                    continue wew1;
                                }
                            }
                    }while(flag!=false);

                    //wewnatrz ziarna
                }else{

                    wew2:
                    do {
                        int indexX = random.nextInt(X);
                        int indexY = random.nextInt(Y);

                        // Moore

                        for (int w = -1; w < 2; w++) {
                            for (int g = -1; g < 2; g++) {
                                if (((startPoints[condition.funY(indexX + w)][condition.funX(indexY + g)].getColorNumber() != startPoints[indexX][indexY].getColorNumber()))) {
                                    continue wew2;
                                }
                            }
                        }


                        startPoints[indexX][indexY].addDislocationDensity(pack);


                        if(startPoints[indexX][indexY].getDislocationDensity()>Cell.CRITICAL_DISSLOCATION){
                            startPoints[indexX][indexY].setColorNumber(2000);
                            startPoints[indexX][indexY].setIfGlacial(true);
                            startPoints[indexX][indexY].setDislocationDensity(0);
                        }
                        continue wew1;

                    }while(flag!=false);

                }
            }


            // regula przejscia
            double tmpDislocationDensity = 0;

            for(int d=0;d<X;d++){
                for(int e=0;e<Y;e++){
                    boolean ifGlacial=false;
                    for (int w = -1; w < 2; w++) {
                        for (int g = -1; g < 2; g++) {
                            if(w==0 && g==0){

                            }else{
                                tmpDislocationDensity+=startPoints[condition.funY(d + w)][condition.funX(e + g)].getDislocationDensity();
                                if(startPoints[condition.funY(d + w)][condition.funX(e + g)].isIfGlacial()==true){

                                    ifGlacial=true;
                                }

//                                if((startPoints[condition.funY(d + w)][condition.funX(e + g)].isIfGlacial()==true) && (startPoints[d][e].getDislocationDensity()>startPoints[condition.funY(d + w)][condition.funX(e + g)].getDislocationDensity())){
//                                }else{
//                                    ifGlacial=false;
//                                }
                            }


                        }
                    }
//                    if(ifGlacial && (!startPoints[d][e].isIfGlacial())){
//                        startPoints[d][e].setIfGlacial(true);
//                        startPoints[d][e].setColorNumber(2000);
//                        startPoints[d][e].setDislocationDensity(0);
//                    }
                    if(ifGlacial && (startPoints[d][e].getDislocationDensity()>tmpDislocationDensity)){
                        startPoints[d][e].setIfGlacial(true);
                        startPoints[d][e].setColorNumber(2000);
                        startPoints[d][e].setDislocationDensity(0);
                    }
                }
            }


            drawer.setFill(Colors.getColor(2000));
            for (int d = 0; d < X; d++) {
                for (int e = 0; e < Y; e++) {
                    System.out.print(startPoints[d][e].getDislocationDensity() + "   ");
                    if(startPoints[d][e].isIfGlacial()){
                        drawer.fillRoundRect(e*SquareShape.WIDTH,d*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT,0,0);
                        drawer.strokeRect(e*SquareShape.WIDTH,d*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT);
                    }
                }
                System.out.println();
            }
            saveToFile(i);
        }
    }


    @FXML
    public void showRecrystallization(){
        canvas.setWidth(Y* SquareShape.WIDTH);
        canvas.setHeight(X*SquareShape.HEIGHT);

        for(int i=0;i<X;i++){
            for(int j=0;j<Y;j++){
//                if(startPoints[i][j].isIfGlacial()){
//                    drawer.setFill(Colors.getColor(2000));
//                }else{
//                    drawer.setFill(Color.WHITE);
//                }
                if(startPoints[i][j].getDislocationDensity()<1.10986464696034E9){
                    drawer.setFill(Color.rgb(20,20,20));
                }else if(startPoints[i][j].getDislocationDensity()<1.13986464696034E9){
                    drawer.setFill(Color.BLACK);
                }else if(startPoints[i][j].getDislocationDensity()<1.151986464696034E9){
                    drawer.setFill(Color.BLUE);
                }else if(startPoints[i][j].getDislocationDensity()<1.172986464696034E9){
                    drawer.setFill(Color.RED);
                }else if(startPoints[i][j].getDislocationDensity()<1.190986464696034E9){
                    drawer.setFill(Color.rgb(60,60,155));
                }else if(startPoints[i][j].getDislocationDensity()<2.10986464696034E9){
                    drawer.setFill(Color.WHITE);
                }else if(startPoints[i][j].getDislocationDensity()<2.586464696034E9) {
                    drawer.setFill(Color.YELLOW);
                }else if(startPoints[i][j].getDislocationDensity()<2.986464696034E9) {
                    drawer.setFill(Color.BLACK);
                }
                drawer.fillRoundRect(j*SquareShape.WIDTH,i*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT,0,0);
                drawer.strokeRect(j*SquareShape.WIDTH,i*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT);
            }
        }



    }

    public void saveToFile(int timeIndex) throws IOException {
        String filePath = "date.txt";
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(filePath, true);
            double number = 1;
            for(int i=0;i<X;i++){
                for(int j=0;j<Y;j++){
                    number = number+startPoints[i][j].getDislocationDensity();
                }
            }

            fileWriter.write(timeIndex*0.001 + "                        " + number);
            fileWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                fileWriter.close();
            }
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

    private void drawOnCanvasRectangles(Cell startPoints[][],boolean ifDrawNet){

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
                if(ifDrawNet){
                    drawer.strokeRect(j*SquareShape.WIDTH,i*SquareShape.HEIGHT,SquareShape.WIDTH,SquareShape.HEIGHT);
                }

            }
        }
    }

    private void clear(){
        for(int i=0;i<X;i++){
            for(int j=0;j<Y;j++){
                startPoints[i][j].setColorNumber(0);
                tmpStartPoints[i][j].setColorNumber(0);
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
