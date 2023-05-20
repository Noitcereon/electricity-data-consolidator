package me.noitcereon.exceptions;

public class NotImplementedException extends RuntimeException{
    public NotImplementedException(){
        super("This method is not implemented yet!");
    }
    public NotImplementedException(String message){
        super(message);
    }
}
