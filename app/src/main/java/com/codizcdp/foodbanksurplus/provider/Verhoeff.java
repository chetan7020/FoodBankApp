package com.codizcdp.foodbanksurplus.provider;

public final class Verhoeff {


    private static int[][] d = new int[][]
            {
                    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 2, 3, 4, 0, 6, 7, 8, 9, 5},
                    {2, 3, 4, 0, 1, 7, 8, 9, 5, 6},
                    {3, 4, 0, 1, 2, 8, 9, 5, 6, 7},
                    {4, 0, 1, 2, 3, 9, 5, 6, 7, 8},
                    {5, 9, 8, 7, 6, 0, 4, 3, 2, 1},
                    {6, 5, 9, 8, 7, 1, 0, 4, 3, 2},
                    {7, 6, 5, 9, 8, 2, 1, 0, 4, 3},
                    {8, 7, 6, 5, 9, 3, 2, 1, 0, 4},
                    {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
            };

    /**
     * The permutation table.
     */
    private static int[][] p = new int[][]
            {
                    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 5, 7, 6, 2, 8, 3, 0, 9, 4},
                    {5, 8, 0, 3, 7, 9, 6, 1, 4, 2},
                    {8, 9, 1, 6, 0, 4, 3, 5, 2, 7},
                    {9, 4, 5, 3, 1, 2, 6, 8, 7, 0},
                    {4, 2, 8, 6, 5, 7, 3, 9, 0, 1},
                    {2, 7, 9, 3, 8, 0, 6, 4, 1, 5},
                    {7, 0, 4, 6, 9, 1, 3, 2, 5, 8}
            };

    /**
     * The inverse table.
     */
    private static int[] inv = {0, 4, 3, 2, 1, 5, 6, 7, 8, 9};


    /**
     * Generates the Verhoeff digit for the provided numeric string.
     *
     * @return The generated Verhoeff digit for the provided numeric string.
     */
    public static String GenerateVerhoeffDigit(String num) {

        int c = 0;
        int[] myArray = StringToReversedIntArray(num);

        for (int i = 0; i < myArray.length; i++) {
            c = d[c][p[((i + 1) % 8)][myArray[i]]];
        }

        return Integer.toString(inv[c]);
    }

    /**
     * Validates that an entered number is Verhoeff compliant.
     * NB: Make sure the check digit is the last one.
     *
     * @param num The numeric string data for Verhoeff compliance check.
     * @return TRUE if the provided number is Verhoeff compliant.
     */
    public static boolean ValidateVerhoeff(String num) {

        int c = 0;
        int[] myArray = StringToReversedIntArray(num);

        for (int i = 0; i < myArray.length; i++) {
            c = d[c][p[(i % 8)][myArray[i]]];
        }

        return (c == 0);
    }

    /**
     * Converts a string to a reversed integer array.
     *
     * @param num The numeric string data converted to reversed int array.
     * @return Integer array containing the digits in the numeric string
     * provided in reverse.
     */
    private static int[] StringToReversedIntArray(String num) {

        int[] myArray = new int[num.length()];

        for (int i = 0; i < num.length(); i++) {
            myArray[i] = Integer.parseInt(num.substring(i, i + 1));
        }

        myArray = Reverse(myArray);

        return myArray;

    }

    /**
     * Reverses an int array.
     *
     * @param myArray The input array which needs to be reversed
     * @return The array provided in reverse order.
     */
    private static int[] Reverse(int[] myArray) {

        int[] reversed = new int[myArray.length];

        for (int i = 0; i < myArray.length; i++) {
            reversed[i] = myArray[myArray.length - (i + 1)];
        }

        return reversed;
    }
}
