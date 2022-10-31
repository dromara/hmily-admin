package org.dromara.hmily.admin.helper;

/**
 * ByteAndHexadecimalHelper.
 *
 * @author zhangwanjie3
 */
public class ByteAndHexadecimalHelper {
    
    /**
     * Converts a byte[] array to hexadecimal characters,A byte generates two characters of length equal to 1:2.
     * @param bytes  input byte[]
     * @return Hex
     */
    public static String byte2Hex(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        /**
         * Traversing the byte[] array, converting each byte number to hexadecimal characters,
         * and stitching them together into a string.
         * */
        for (int i = 0; i < bytes.length; i++) {
            /***
             *Bytes [I] & 0xff when each byte is converted to hexadecimal characters, if the high digit is 0, the output is removed,
             *  so +0x100(plus 1 in the higher bit) is truncated to the next two characters.
             */
            builder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return builder.toString();
    }
    
    /**
     *  Converts hexadecimal characters to a byte[] array,As opposed to the function byte2Hex.
     * @param string Hexadecimal string.
     * @return byte[]
     */
    public static byte[] hex2Byte(final String string) {
        if (string == null || string.length() < 1) {
            return null;
        }
        /**
         * Because a byte generates two characters of length equal to 1:2.
         * the length of the byte[] array is half the length of the string.
         * */
        byte[] bytes = new byte[string.length() / 2];
        for (int i = 0; i < string.length() / 2; i++) {
            // Intercepts the first of two characters and converts it to an int value
            int high = Integer.parseInt(string.substring(i * 2, i * 2 + 1), 16);
            // Intercepts the last of two characters and converts it to an int value
            int low = Integer.parseInt(string.substring(i * 2 + 1, i * 2 + 2), 16);
            /**
             * The value of int *16+ the value of int of low character is strong converted to byte value.
             * For example, DD, high 13*16+ low 13=221(strong converted to byte binary 11011101, corresponding to decimal -35).
             * */
            bytes[i] = (byte) (high * 16 + low);
        }
        return bytes;
    }
}
