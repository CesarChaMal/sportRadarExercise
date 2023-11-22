/*
Given an array of integers, find the sum of its elements.

        For example, if the array , , so return .

        Function Description

        Complete the simpleArraySum function in the editor below. It must return the sum of the array elements as an integer.

        simpleArraySum has the following parameter(s):

        ar: an array of integers
        Input Format

        The first line contains an integer, , denoting the size of the array.
        The second line contains  space-separated integers representing the array's elements.

        Constraints


        Output Format

        Print the sum of the array's elements as a single integer.

        Sample Input

        6
        1 2 3 4 10 11
        Sample Output

        31
*/

package com.wipro.exercises;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Task5 {

    public static class Result {

        /*
         * Complete the 'simpleArraySum' function below.
         *
         * The function is expected to return an INTEGER.
         * The function accepts INTEGER_ARRAY ar as parameter.
         */

        public static int simpleArraySum(List<Integer> ar) {
            // Write your code here
            return ar.stream().filter(x -> x > 0 && x <= 1000).mapToInt(Integer::intValue).sum();
//        return ar.stream().filter(x -> x > 0 && x <= 1000).mapToInt(Integer::intValue).reduce(0, Integer::sum);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("output.txt"));

        int arCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> ar = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Integer::parseInt)
                .collect(toList());

        int result = Result.simpleArraySum(ar);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
