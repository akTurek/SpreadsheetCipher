package org.example;

import org.apache.poi.ss.usermodel.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CipherMySheet {

    public CipherMySheet() {

    }

    public static void enkriptexl(String path, String myKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {

        FileInputStream inputStream = new FileInputStream(new File(path));
        Workbook workbook = WorkbookFactory.create(inputStream);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                 for (Row row : sheet) {
                    for (Cell cell : row) {
                        cell.setCellValue(aByteValueOfCell(myKey,cell));
                    }
                System.out.println();
            }
        }
            FileOutputStream outputStream = new FileOutputStream(new File(path));
            workbook.write(outputStream);
            workbook.close();

    }

    public static void deEnkriptexl(String path, String myKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {

        String decodedCell;
        FileInputStream inputStream = new FileInputStream(new File(path));
        Workbook workbook = WorkbookFactory.create(inputStream);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        decodedCell = aCellValueOfByte(myKey, cell);
                        insertRightType(decodedCell, cell);
                    }
                    System.out.println();
                }
            }
            FileOutputStream outputStream = new FileOutputStream(new File(path));
            workbook.write(outputStream);
            workbook.close();
    }

    private static String aByteValueOfCell(String myKey, Cell cell) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {

        byte[] encryptedData = null;
        byte[] key = myKey.getBytes(); // Dolžina ključa mora biti 16 bajtov
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        switch (cell.getCellType()) {
            case STRING:
                encryptedData = cipher.doFinal(cell.getStringCellValue().getBytes(StandardCharsets.UTF_8));
                break;
            case NUMERIC:
                encryptedData = cipher.doFinal(Double.toString(cell.getNumericCellValue()).getBytes(StandardCharsets.UTF_8));
                break;
            case BOOLEAN:
                encryptedData = cipher.doFinal(Boolean.toString(cell.getBooleanCellValue()).getBytes(StandardCharsets.UTF_8));
                break;
            case FORMULA:
                encryptedData = cipher.doFinal((cell.getCellFormula()).getBytes(StandardCharsets.UTF_8));
                break;
            default:
                encryptedData = cipher.doFinal(cell.getStringCellValue().getBytes(StandardCharsets.UTF_8));
        };

        String encriptedData = Base64.getEncoder().encodeToString(encryptedData);
        return encriptedData;
    }

    private static String aCellValueOfByte(String myKey, Cell cell) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] key = myKey.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);


        byte[] decodedBytes = Base64.getDecoder().decode(cell.getStringCellValue());
        String decriptedData = new String(cipher.doFinal(decodedBytes),"UTF-8");
        return decriptedData;
    }

    private static void insertRightType(String decodedCell, Cell cell){
        if (isNumeric(decodedCell)){
            cell.setCellValue(Double.parseDouble(decodedCell));
        } else if (isBoolean(decodedCell)) {
            cell.setCellValue(Boolean.parseBoolean(decodedCell));
        } else if (isFormula(decodedCell)) {
            cell.setCellFormula(decodedCell);
        } else {
            cell.setCellValue(decodedCell);
        }
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isBoolean(String str) {
        return str.equals("True") || str.equals("False");
    }

    public static boolean isFormula(String str) {
        return str.startsWith("=");
    }

    public static boolean isString(String str) {
        return str.matches("[a-zA-Z]+");
    }


}
