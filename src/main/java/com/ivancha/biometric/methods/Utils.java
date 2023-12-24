package com.ivancha.biometric.methods;

import com.ivancha.biometric.methods.dto.StandardDto;

import java.util.*;

public class Utils {

    public static Map<Integer, StandardDto> getMinMaxElemByElem(List<List<Integer>> lists)
    {
        if (lists.size() == 0)
            return new HashMap<>();

        int listSize = lists.get(0).size();

        for (List<Integer> list : lists)
            if (list.size() != listSize)
                throw new RuntimeException("Размеры списков должны совпадать");

        Map<Integer, StandardDto> minMaxMap = new HashMap<>();
        for (int elemNum = 0; elemNum < listSize; elemNum++)
        {
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;

            for (List<Integer> list : lists) {

                int listElemVal = list.get(elemNum);
                min = Math.min(listElemVal, min);
                max = Math.max(listElemVal, max);
            }

            minMaxMap.put(elemNum, new StandardDto(min, max));
        }

        return minMaxMap;
    }


    public static double mathExpectedVal(List<Integer> values)
    {
        return values.stream()
                .mapToInt(Integer::intValue)
                .sum() * 1.0 / values.size();
    }


    public static double dispersion(List<Integer> values)
    {
        double expectedVal = mathExpectedVal(values);

        return values.stream()
                .mapToDouble(elem -> expectedVal - elem)
                .map(difference -> difference * difference)
                .sum() / values.size();
    }


    public static List<Integer> calcHammingVector(Map<Integer, StandardDto> minMaxMap, List<Integer> values)
    {
        List<Integer> hammingVector = new ArrayList<>();
        for (int i = 0; i < values.size(); i++)
        {
            StandardDto standard = minMaxMap.get(i);
            hammingVector.add(
                    values.get(i) > standard.min() && values.get(i) < standard.max()
                            ? 0
                            : 1);
        }
        return hammingVector;
    }

    // ну тут эта перегрузка, конечно...
    public static List<Integer> calcHammingVector(Map<Integer, StandardDto> minMaxMap, Map<Integer, Integer> values)
    {
        List<Integer> hammingVector = new ArrayList<>();
        for (int i = 0; i < values.size(); i++)
        {
            StandardDto standard = minMaxMap.get(i);
            hammingVector.add(
                    values.get(i) > standard.min() && values.get(i) < standard.max()
                            ? 1
                            : 0);
        }
        return hammingVector;
    }


    public static int calcHammingDistance(List<Integer> hammingVector)
    {
        return (int) hammingVector.stream()
                .filter(val -> val == 1)
                .count();
    }


    public static double calcThresholdValueOfHamming(double StudentsCoef)
    {
        return 4; // ?
    }
}
