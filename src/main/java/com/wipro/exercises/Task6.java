/*
In this challenge, you are required to calculate and print the sum of the elements in an array, keeping in mind that some of those integers may be quite large.

        Function Description

        Complete the aVeryBigSum function in the editor below. It must return the sum of all array elements.

        aVeryBigSum has the following parameter(s):

        int ar[n]: an array of integers .
        Return

        long: the sum of all array elements
        Input Format

        The first line of the input consists of an integer .
        The next line contains  space-separated integers contained in the array.

        Output Format

        Return the integer sum of the elements in the array.

        Constraints


        Sample Input

        5
        1000000001 1000000002 1000000003 1000000004 1000000005
        Output

        5000000015
        Note:

        The range of the 32-bit integer is .
        When we add several integer values, the resulting sum might exceed the above range. You might need to use long int C/C++/Java to store such sums.
*/


package com.wipro.exercises;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import static java.util.stream.Collectors.toList;

import static java.util.stream.Collectors.joining;

public class Task6 {

    public static class Result {

        /*
         * Complete the 'aVeryBigSum' function below.
         *
         * The function is expected to return a LONG_INTEGER.
         * The function accepts LONG_INTEGER_ARRAY ar as parameter.
         */

        public static long aVeryBigSum(List<Long> ar) {
            // Write your code here
            return ar.stream()
                    .filter(x -> x >= 0 && x <= Math.pow(10, 10))
                    .mapToLong(Long::longValue)
                    .sum();
        }
    }

    @FunctionalInterface
    interface InputValidator {
        boolean isValid(int value);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("output.txt"));

        InputValidator validator = value -> value >= 1 && value <= 10;
        int arCount = Integer.parseInt(bufferedReader.readLine().trim());
/*
        if (arCount < 1 || arCount > 10) {
            throw new IllegalArgumentException("The number must be between 1 and 10.");
        }
*/

        if (!validator.isValid(arCount)) {
            throw new IllegalArgumentException("The number must be between 1 and 10.");
        }

        List<Long> ar = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Long::parseLong)
                .collect(toList());

        long result = Result.aVeryBigSum(ar);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
