package sample;

import javafx.scene.paint.Color;

import java.util.Random;

public class Colors {

    private static final int numbersOfColors=3600;
    private static Color colors[]= new Color[numbersOfColors];
    private static final Random r= new Random();

    public static Color getColor(int i){
        return colors[i];
    }
    public static int getNumberOfColors(){
        return colors.length;
    }

    static{
        //Generuje kolory
        //System.out.println("jestem tutaj");
        int counter=0;

        zew:
        do{
            double red = r.nextInt(255);
            double green = r.nextInt(255);
            double blue = r.nextInt(255);
            for(int j=0;j<colors.length;j++) {
                if(colors[j]!=null) {
                    if (colors[j].getBlue() == blue && colors[j].getGreen() == green && colors[j].getRed() == red) {
                        continue zew;
                    }
                }else{
                    colors[counter]=Color.rgb((int)red,(int)green,(int)blue);
                }
            }
            counter++;
        }while(counter<3600);

    }

}
