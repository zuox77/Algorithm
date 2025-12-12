package Interview.Airwallex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Set;

/*
CSV Query Tool
Summary
We would like you to develop a program which queries CSV (comma separated values) files. The program will take in a query and output the results of the query. For example:
> FROM countries.csv

country,latitude,longitude,country_name
AD,42.546245,1.601554,Andorra
AE,23.424076,53.847818,United Arab Emirates
AF,33.93911,67.709953,Afghanistan
AG,17.060816,-61.796428,Antigua and Barbuda
AI,18.220554,-63.068615,Anguilla
AL,41.153332,20.168331,Albania
AM,40.069099,45.038189,Armenia
AN,12.226079,-69.060087,Netherlands Antilles
AO,-11.202692,17.873887,Angola
AQ,-75.250973,-0.071389,Antarctica
AR,-38.416097,-63.616672,Argentina
...
In total, there are six commands to implement. Please review the specification below implement each command one-by-one. There are several assumptions you may assume about the data.
The data is well formatted, meaning you can call split(,) on a single row without issue (e.g. no value has a comma inside it).
All the data will fit in memory, meaning you do not need advanced file-based implementations.
The columns in each CSV are unique and not duplicated.
The commands except FROM can be combined together in any order
Do not use libraries such as pandas which make the problem trivial. We want to understand how you think and code your service.
You can assume there’s a function to read in a file. We are not interested in knowing you can read in a file.
FROM
All commands start with FROM. Without additional commands, this single command reads and displays the csv file in the terminal
> FROM countries.csv

country,latitude,longitude,country_name
AD,42.546245,1.601554,Andorra
AE,23.424076,53.847818,United Arab Emirates
AF,33.93911,67.709953,Afghanistan
AG,17.060816,-61.796428,Antigua and Barbuda
AI,18.220554,-63.068615,Anguilla
...
SELECT
Select the columns to be displayed. Ideally, the results are row stable if no additional commands are specified. The column names will be a string command deliminated string without spaces.
> FROM countries.csv SELECT country,contry_name,latitude

country,country_name,latitude
AD,Andorra,42.546245
AE,United Arab Emirates,23.424076
AF,Afghanistan,33.93911
AG,Antigua and Barbuda,17.060816
AI,Anguilla,18.220554
JOIN
Join two csv files on a single column. The command format is JOIN [filename] [column name]. The join column will always be a string value (e.g. non-numeric).
> FROM countries.csv JOIN languages.csv country_name SELECT country,contry_name,latitude
...
TAKE
Take the top N values from the CSV
FROM countries.csv TAKE 2

country,latitude,longitude,country_name
AD,42.546245,1.601554,Andorra
AE,23.424076,53.847818,United Arab Emirates

> FROM countries.csv SELECT country,country_name,latitude TAKE 2

country,country_name,latitude
AD,Andorra,42.546245
AE,United Arab Emirates,23.424076
COUNT
Count the number of distinct values in a column. The resulting output should be the column name and count in descending order. The count will always be over string columns.
>FROM languages.csv COUNT lang_name

lang_name, count
Spanish,19
Arabic,16
English,8
Vietnamese,3
SORT
Sort the rows by a numeric column in ascending order. We guarantee that the command should only handle numeric columns when sorting.
> FROM languages.csv SORT country_code

country_name,country_code_name,country_code,lang_name,lang_code
Canada,ca,1,Mohawk,moh
United States,us,1,Arabic,ar
United States,us,1,English,en
United States,us,1,Italian,it
United States,us,1,Mandarin,mn
United States,us,1,Vietnamese,ve
Kazakhstan,kz,7,Kazakh,kk
Russia,ru,7,Yakut,sah
Egypt,eg,20,Arabic,ar
South Africa,za,27,Setswana,tn
Greece,gr,30,Greek,el
Netherlands,nl,31,Frisian,fy
Belgium,be,32,French,fr
France,fr,33,Occitan,oc
 */

public class ImplementSQL {
    public static class FileData {
        // File name
        String tableName;
        // Row
        int row;
        // Column
        int col;
        // Map of index -> values
        Map<Integer, String> indexMap;
        // Map of values -> index
        Map<String, Integer> valueMap;
        /*
        [1, 2, 3],
        [4, 5, 6],
        [7, 8, 9],
        [10, 11, 12]
        row = 4
        col = 3
        数字8的坐标为：[2, 1]
        数字8是第几个数：7（从0开始数）
        2 * col + 1 = 2 * 4 + 1 = 7
        7 / col = 7 / 3 = 2
        7 % col = 7 % 3 = 1
         */
        public int[] getPosition(int k) {
            return new int[]{k / col, k % col};
        }

        public int getIndex(int x, int y) {
            return x * col + y;
        }
        
        public FileData(String tableName, int row, int col) {
            this.tableName = tableName;
            this.row = row;
            this.col = col;
            indexMap = new HashMap<>();
            valueMap = new HashMap<>();
        }
    }

    public static class QueryTool {

        private final Map<String, FileData> fileMap;

        public QueryTool(String fileName, String tableName, int row, int col) throws FileNotFoundException {
            fileMap = new HashMap<>();
            FileData fd = new FileData(tableName, row, col);
            fileMap.put(tableName, fd);

            int x = 0;
            String line;
            try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                while ((line = reader.readLine()) != null) {
                    int y = 0;
                    for (String word: line.split(",")) {
                        fd.indexMap.put(fd.getIndex(x, y), word.trim());
                        fd.valueMap.put(word.trim(), fd.getIndex(x, y));
                        y++;
                    }
                    x++;
                }
            } catch (Exception e) {
                System.out.println("Error reading file");
                System.exit(1);
            }
        }

        private void print(FileData fd, int rowEnd, int[] rows, int[] cols) {
            String[] rowValues = new String[cols.length];
            if (rowEnd == -1 && rows == null) {
                throw new NoSuchElementException();
            } else if (rows == null) {
                for (int i = 0; i <= rowEnd; i++) {
                    for (int j: cols) {
                        rowValues[j] = fd.indexMap.get(fd.getIndex(i, j));
                    }
                    System.out.println(String.join(",", rowValues));
                }
            } else {
                for (int i: rows) {
                    for (int j: cols) {
                        rowValues[j] = fd.indexMap.get(fd.getIndex(i, j));
                    }
                    System.out.println(String.join(",", rowValues));
                }
            }
        }

        public void from(String tableName) {
            take(tableName, -1);
        }

        public void take(String tableName, int k) {
            // get fileData
            FileData fd = fileMap.get(tableName);
            // Reset k
            if (k == -1 || k >= fd.row) {
                k = fd.row - 1;
            }
            // Print
            int[] cols = new int[fd.col];
            for (int i = 0; i < fd.col; i++) {
                cols[i] = i;
            }
            print(fd, k, null, cols);
        }

        public void select(String tableName, String[] colNames) {
            // Get fileData
            FileData fd = fileMap.get(tableName);
            // Get col ids
            int[] cols = new int[colNames.length];
            for (int i = 0; i < colNames.length; i++) {
                int index = fd.valueMap.get(colNames[i]);
                cols[i] = fd.getPosition(index)[1];
            }
            // print
            print(fd, fd.row - 1, null, cols);
        }

        public void count(String tableName, String colName) {
            // Get fileData
            FileData fd = fileMap.get(tableName);
            // Get column name index
            int colIndex = fd.getPosition(fd.valueMap.get(colName))[1];
            // Map to save word occurrence
            Map<String, Integer> freq = new HashMap<>();
            // Iterate all values on that column to get occurrence
            int row = 1;
            while (row < fd.row) {
                int index = fd.getIndex(row++, colIndex);
                String word = fd.indexMap.get(index);
                freq.merge(word, 1, Integer::sum);
            }
            // Put occurrence to a heap
            PriorityQueue<Map.Entry<String, Integer>> heap =
                    new PriorityQueue<>(
                            freq.size(),
                            (a, b) -> b.getValue() - a.getValue()
                    );
            // Push all values to heap
            for (Map.Entry<String, Integer> entry: freq.entrySet()) {
                heap.offer(entry);
            }
            // Print column name first
            System.out.println(colName + "," + "count");
            // Poll values from heap and print
            while (!heap.isEmpty()) {
                Map.Entry<String, Integer> entry = heap.poll();
                System.out.println(entry.getKey() + "," + entry.getValue());
            }
        }

        public void sort(String tableName, String colName) {
            // Get fileData
            FileData fd = fileMap.get(tableName);
            // Get col values
            int colIndex = fd.getPosition(fd.valueMap.get(colName))[1];
            // Create a heap to save by row
            PriorityQueue<Map.Entry<Integer, String[]>> heap = new PriorityQueue<>(fd.row, new Comparator<Map.Entry<Integer, String[]>>() {
                @Override
                public int compare(Map.Entry<Integer, String[]> o1, Map.Entry<Integer, String[]> o2) {
                    int o1Value = Integer.parseInt(o1.getValue()[colIndex]);
                    int o2Value = Integer.parseInt(o2.getValue()[colIndex]);
                    if (o1Value != o2Value) {
                        return o1Value - o2Value;
                    }
                    // Compare rest column values
                    for (int col = 0; col < fd.col; col++) {
                        if (col == colIndex) continue;
                        String o1Word = o1.getValue()[col];
                        String o2Word = o2.getValue()[col];
                        if (o1Word.equals(o2Word)) continue;
                        return o1Word.compareTo(o2Word);
                    }
                    // If these 2 records are totally identical, return the one which exists earlier
                    return o1.getKey() - o2.getKey();
                }
            });
            // Save row values to a map
            Map<Integer, String[]> rowMap = new HashMap<>();
            for (int i = 0; i < fd.row; i++) {
                String[] rowValues = new String[fd.col];
                for (int j = 0; j < fd.col; j++) {
                    rowValues[j] = fd.indexMap.get(fd.getIndex(i, j));
                }
                rowMap.put(i, rowValues);
            }
            for (Map.Entry<Integer, String[]> entry: rowMap.entrySet()) {
                // Exclude column names
                if (entry.getKey() == 0) continue;
                heap.offer(entry);
            }
            // Print
            while (!heap.isEmpty()) {
                Map.Entry<Integer, String[]> entry = heap.poll();
                System.out.println(String.join(",", entry.getValue()));
            }
        }

        private List<String> getRowValuesWithColNames(FileData fd, String[] colNames, String curWord, int row) {
            int n = colNames.length;
            List<String> rowValues = new ArrayList<>();
            for (String colName: colNames) {
                if (!fd.valueMap.containsKey(colName)) continue;
                // Get column name index
                int colNameIndex = fd.getPosition(fd.valueMap.get(colName))[1];
                // Save row values
                rowValues.add(fd.indexMap.get(fd.getIndex(row, colNameIndex)));
            }
            return rowValues;
        }

        public void join(String tableName1, String tableName2, String colName, String[] colNames) {
            // Get fileData
            FileData fd1 = fileMap.get(tableName1);
            FileData fd2 = fileMap.get(tableName2);
            // Get colIndex
            int colIndex1 = fd1.getPosition(fd1.valueMap.get(colName))[1];
            int colIndex2 = fd2.getPosition(fd2.valueMap.get(colName))[1];

            // For first fileData, put all values in that column to a Map
            Map<String, Integer> colMap = new HashMap<>();
            for (int i = 1; i < fd1.row; i++) {
                colMap.put(fd1.indexMap.get(fd1.getIndex(i, colIndex1)), i);
            }

            // Iterate another fileData to get identical values
            for (int i = 1; i < fd2.row; i++) {
                // Current word from fd2
                String curWord = fd2.indexMap.get(fd2.getIndex(i, colIndex2));
                // Bingo! Found the identical word, let's join
                if (colMap.containsKey(curWord)) {
                    // Get all words from 1st fd in that row under colNames
                    int row = colMap.get(curWord);
                    List<String> rowValues1 = getRowValuesWithColNames(fd1, colNames, curWord, row);
                    // Get all words from 2nd fd in that row under colNames
                    List<String> rowValues2 = getRowValuesWithColNames(fd2, colNames, curWord, i);
                    // Print
                    System.out.println(String.join(",", rowValues1) + "," + String.join(",", rowValues2));
                }
            }
        }
    }
}