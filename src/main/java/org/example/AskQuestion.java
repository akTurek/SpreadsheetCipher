package org.example;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class AskQuestion {
    private boolean over = false;


    public void askQuestion() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeyException {
        Scanner scanner = new Scanner(System.in);

        while (!over) {
            switch (askQ(scanner)) {
                case 'E':
                    enkript(scanner);
                    break;
                case 'D':
                    dekript(scanner);
                    break;
                case 'Q':
                    over = true;
                    break;
                default:
                    System.out.println("napacen ukaz");
                    break;
            }
        }
        scanner.close();

    }



    private void enkript(Scanner scanner) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeyException {
        String path = getPath(scanner);
        String key = getKey(scanner);
        CipherMySheet.enkriptexl(path, key);
        System.out.println("Koncana kriptacija");
    }

    private void dekript(Scanner scanner) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeyException {
        String path = getPath(scanner);
        String key = getKey(scanner);
        CipherMySheet.deEnkriptexl(path, key);
        System.out.println("Koncana dekriptacija");
    }

     private char askQ(Scanner scanner){
        System.out.println("***************************************************************************************************************");
        System.out.println("Kaj bi rad naredil (E)nkripr (D)ekript (Q)uit");
        char response = Character.toUpperCase(scanner.nextLine().charAt(0));
        return response;
    }

    private String pathToJavaPath(String path){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == '\\') {
                sb.append("\\\\");
            } else {
                sb.append(c);
            }
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }

    private boolean rightPath (String path){
        File file = new File(path);
        return file.exists();
    }

    private String getPath(Scanner scanner){
        System.out.println("Vnesite pot do .xlsx datoteke");
        String path = pathToJavaPath(scanner.nextLine());
        while (!path.endsWith(".xlsx") || !rightPath(path)){
            System.out.println("Ponovne vnesite pot do .xlsx datoteke");
            path = pathToJavaPath(scanner.nextLine());
        }
        return path;
    }

    private String getKey(Scanner scanner){
        System.out.println("Vnesite kljuc za kriptacijo dolzine 16");
        String key = pathToJavaPath(scanner.nextLine());
        while (key.length() != 16){
            System.out.println("Ponovne vnesite kljuc za kriptacijo dolzine 16");
            key = pathToJavaPath(scanner.nextLine());
        }
        return key;
    }
}
