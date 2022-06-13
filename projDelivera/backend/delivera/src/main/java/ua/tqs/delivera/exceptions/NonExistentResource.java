package ua.tqs.delivera.exceptions;

public class NonExistentResource extends Exception {

    public NonExistentResource(String message){
        super(message);
    }
}