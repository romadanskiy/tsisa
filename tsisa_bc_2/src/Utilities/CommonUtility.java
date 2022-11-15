package Utilities;

public class CommonUtility {
    public static byte[] ConcatArrays(byte[] a, byte[] b) {
        if (a == null)
            return b;

        if (b == null)
            return a;

        var lengthA = a.length;
        var lengthB = b.length;
        var c = new byte[lengthA + lengthB];
        System.arraycopy(a, 0, c, 0, lengthA);
        System.arraycopy(b, 0, c, lengthA, lengthB);

        return c;
    }
}
