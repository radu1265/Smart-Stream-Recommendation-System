package proiect;
import java.io.*;
import java.util.*;

public class ProiectPOO {

    public static void main(String[] args) {
        if(args == null) {
            System.out.println("Nothing to read here");
        }
        else {
            SaveCommand command = new SaveCommand();
            command.build(args);
        }
    }
}
