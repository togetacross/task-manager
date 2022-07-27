/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mycompany.taskmanager.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 *
 * @author Gerdan Tibor
 */
public class PrintUtil {

    private final static int TABLE_COL_MARGIN = 2;
    private final static int TABLE_COL_MIN_WIDTH = 10;

    public static void printTable(List<String[]> tableRows, String[] header) {
        if (!tableRows.isEmpty()) {
            Map<Integer, Integer> columnsMaxLengthMap = getTableColumnsMaxLength(tableRows);
            System.out.println();
            printTableRow(columnsMaxLengthMap, header);
            tableRows.forEach(arr -> {
                printTableRow(columnsMaxLengthMap, arr);
            });
        } else {
            System.out.println("There are no result!");
        }
    }

    private static Map<Integer, Integer> getTableColumnsMaxLength(List<String[]> arrList) {
        Map<Integer, Integer> columnsMaxLengthMap = new HashMap<>();
        arrList.stream().forEach(arr -> {
            IntStream.range(0, arr.length)
                    .forEach(i -> {
                        if (columnsMaxLengthMap.get(i) == null) {
                            columnsMaxLengthMap.put(i, TABLE_COL_MIN_WIDTH);
                        }
                        if (columnsMaxLengthMap.get(i) < arr[i].length()) {
                            columnsMaxLengthMap.put(i, arr[i].length());
                        }
                    });
        });
        return columnsMaxLengthMap;
    }

    private static void printTableRow(Map<Integer, Integer> columnsMaxLengthMap, String[] row) {
        IntStream.range(0, row.length)
                .forEach(i -> {
                    int totalWidth = columnsMaxLengthMap.get(i) + TABLE_COL_MARGIN;
                    String textFormat = "%-" + totalWidth + "s";
                    System.out.printf(textFormat, row[i]);
                });
        System.out.println();
    }
}
