package es.gigashop.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtil {
    
    /**
     * Encripta una cadena de texto utilizando el algoritmo MD5.
     *
     * @param input La cadena de texto a encriptar.
     * @return La cadena encriptada en formato hexadecimal.
     */
    public static String encrypt(String input) {
        try {
            // Crear una instancia de MessageDigest con el algoritmo MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Calcular el hash en bytes
            byte[] hashBytes = md.digest(input.getBytes());

            // Convertir los bytes del hash a formato hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0'); // Asegura dos dígitos para cada byte
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Manejar excepciones en caso de que el algoritmo no esté disponible
            throw new RuntimeException("Error al encriptar con MD5", e);
        }
    }
    
}
