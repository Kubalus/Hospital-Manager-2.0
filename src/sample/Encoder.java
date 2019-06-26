package sample;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Objects;


public class Encoder{

    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";


    public static final int HASH_BYTES = 24;
    public static final int PBKDF2_ITERATIONS = 1000;

    public static final int ITERATION_INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int PBKDF2_INDEX = 2;

    // ENCODING


    public static String encrypt(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return createHash(password.toCharArray(), salt.toCharArray());
    }

    public static String createHash(char[] password, char[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        byte[] byteSalt = toBytes(salt);
        byte[] hash = pbkdf2(password, byteSalt, PBKDF2_ITERATIONS, HASH_BYTES);

        return PBKDF2_ITERATIONS + ":" + toHex(byteSalt) + ":" +  toHex(hash);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    private static String toHex(byte[] array)
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

    private static byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(charBuffer.array(), '\u0000');
        Arrays.fill(byteBuffer.array(), (byte) 0);
        return bytes;
    }


    // DECODING


    public static boolean validatePassword(String password, String goodHash)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
            if (Objects.equals(password, goodHash))
                return true;
            else
                return false;
//        return validatePassword(password.toCharArray(), goodHash);
    }

//    public static boolean validatePassword(char[] password, String goodHash)
//            throws NoSuchAlgorithmException, InvalidKeySpecException
//    {
//
//        String[] params = goodHash.split(":");
//        int iterations = Integer.parseInt(params[ITERATION_INDEX]);
//        byte[] salt = fromHex(params[SALT_INDEX]);
//        byte[] hash = fromHex(params[PBKDF2_INDEX]);
//
//
//        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
//
//        return slowEquals(hash, testHash);
//    }
//
//
//    private static boolean slowEquals(byte[] a, byte[] b)
//    {
//        int diff = a.length ^ b.length;
//        for(int i = 0; i < a.length && i < b.length; i++)
//            diff |= a[i] ^ b[i];
//        return diff == 0;
//    }
//
//    private static byte[] fromHex(String hex)
//    {
//        byte[] binary = new byte[hex.length() / 2];
//        for(int i = 0; i < binary.length; i++)
//        {
//            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
//        }
//        return binary;
//    }

}
