package sample;

public class BadSizeException extends Exception {

    public void messageError(String message){
        System.err.println(message);
    }
}
