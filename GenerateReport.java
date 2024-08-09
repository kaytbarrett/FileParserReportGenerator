/**
 * Katherine (Katie) Barrett
 * EN.605.201.81.SU24
 * Assignment #10
 * 
 * References used for this assignment: 
 * Module 10 Office Hours Presentation (mentions BufferedReader, which I looked into and used instead of scanner)
 * Stack Overflow taught me about using FileWriter inside of PrintWriter, as well as String.format()
 * Stack Overflow for numberFormat to add commas to the report numbers
 */

import java.io.*;
import java.text.NumberFormat;
import java.util.Locale;

public class GenerateReport {

    public static void main(String[] args) {

         // Check if the correct number of command-line arguments are provided
         if (args.length != 2) {
            // Print an error message if the number of arguments is incorrect
            System.err.println("Please include 2 args: <input-file> <number-of-records>");
            // Exit the method to avoid further execution with invalid arguments
            return;
        }

        // Assign command-line arguments to variables
        String intermediateFile = args[0];
        int numberOfRecords = Integer.parseInt(args[1]); // This should be 13486

        // Display the file path
        System.out.println("File: " + intermediateFile);

        // Generate the report from the intermediate file created in ParseFile
        generateReport(intermediateFile, numberOfRecords);
    }

    private static void generateReport(String intermediateFile, int numberOfRecords) {
        try {
            // Initialize reader 
            BufferedReader reader = new BufferedReader(new FileReader(intermediateFile));

            // Define FIPS codes and initialize accumulators
            String[] fipsCodes = {
                "01", "02", "04", "05", "06", "08", "09", "10", "11", "12", "13", "15", "16", 
                "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", 
                "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", 
                "44", "45", "46", "47", "48", "49", "50", "51", "53", "54", "55", "56"
            };
            int[] totalPopulation = new int[fipsCodes.length];
            int[] relevantChildren = new int[fipsCodes.length];
            int[] childrenInPoverty = new int[fipsCodes.length];

            // Initialize counters
            for (int i = 0; i < fipsCodes.length; i++) {
                totalPopulation[i] = 0;
                relevantChildren[i] = 0;
                childrenInPoverty[i] = 0;
            }
            // Intialize string variable used for lines in the file
            String line;

            // Initialize record count
            int recordCount = 0;

             // Process each line of the intermediate file
             while ((line = reader.readLine()) != null && recordCount < numberOfRecords) {

                // Split the intermediate file into columns; looked up specific Java syntax for this
                String[] columns = line.split("\\s+");

                if (columns.length >= 4) {
                    // FipsCode is a string to caption 01, 02, etc easier
                    String fipsCode = columns[0];
                    int population = Integer.parseInt(columns[1]);
                    int relevantChildrenCount = Integer.parseInt(columns[2]);
                    int childrenInPovertyCount = Integer.parseInt(columns[3]);

                    // Find index for the FIPS code
                    int index = findFipsIndex(fipsCodes, fipsCode);
                    if (index != -1) {
                        // Accumulate totals by FIPS code
                        totalPopulation[index] += population;
                        relevantChildren[index] += relevantChildrenCount;
                        childrenInPoverty[index] += childrenInPovertyCount;
                    }
                }
                // Increment record count
                recordCount++;
            }

            // Print table header with appropriate formatting
            String header = String.format("%-6s %-17s %-17s %-20s %-21s", "State", "Total Population", "Child Population", "Children In Poverty", "Child Poverty %");
            String separator = String.format("%-6s %-17s %-17s %-20s %-21s", "-----", "----------------", "----------------", "-------------------", "---------------");
            System.out.println("\n" + header + "\n" + separator);

            // Create a NumberFormat instance for formatting numbers with commas
            NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
            
            // Print out report totals
            for (int i = 0; i < fipsCodes.length; i++) {
                // Calculate percentage of children in poverty relative to child population
                double percentageInPoverty = ((double) childrenInPoverty[i] * 100) / relevantChildren[i];

                // Print out values in correct format
                System.out.printf("%-6s %-17s %-17s %-20s %.2f%%%n", 
                                  fipsCodes[i], 
                                  numberFormat.format(totalPopulation[i]), 
                                  numberFormat.format(relevantChildren[i]), 
                                  numberFormat.format(childrenInPoverty[i]),  
                                  percentageInPoverty);
            }

            // Close file reader
            reader.close();
        
        } catch (FileNotFoundException e) {
            // Handle the case where the specified file could not be found
            System.err.println("File not found: " + e);
        } catch (IOException e) {
            // Handle other I/O related exceptions that may occur during file operations
            System.err.println("IOException found: " + e);
        }
    }

    // Method to find Fips index to add to the correct total
    private static int findFipsIndex(String[] fipsCodes, String fipsCode) {
        for (int i = 0; i < fipsCodes.length; i++) {
            if (fipsCodes[i].equals(fipsCode)) {
                return i;
            }
        }
        return -1; // FIPS code not found
    }
}
