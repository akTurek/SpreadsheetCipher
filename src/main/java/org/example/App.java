package org.example;

import javax.crypto.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class App
{
    public static void main( String[] args ) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        CipherMySheet scipher = new CipherMySheet();
        scipher.enkriptexl("C:\\Users\\lukat\\Documents\\Testexl.xlsx","geslo123geslo123");
        scipher.deEnkriptexl("C:\\Users\\lukat\\Documents\\Testexl.xlsx","geslo123geslo123");

    }
}
