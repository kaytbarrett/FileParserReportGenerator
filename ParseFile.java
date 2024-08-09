/**
 * Katherine (Katie) Barrett
 * EN.605.201.81.SU24
 * Assignment #10
 * 
 * References used for this assignment: 
 * Module 10 Office Hours Presentation (mentions BufferedReader, which I looked into and used instead of scanner)
 * Stack Overflow taught me about using FileWriter inside of PrintWriter, as well as String.format()
 */

import java.io.*;

public class ParseFile {
    public static void main(String[] args) {

        // Check if the correct number of command-line arguments are provided
        if (args.length != 3) {
            // Print an error message if the number of arguments is incorrect
            System.err.println("Please include 3 args: <input-file> <output-file> <number-of-records>");
            // Exit the method to avoid further execution with invalid arguments
            return;
        }

        // Assign command-line arguments to variables
        String inputFilePath = args[0];
        String intermediateFile = args[1];
        int numberOfRecords = Integer.parseInt(args[2]); // This should be 13486

        // Call the method with its three arguments 
        parseFile(inputFilePath, intermediateFile, numberOfRecords);
    }

    private static void parseFile(String inputFilePath, String intermediateFile, int numberOfRecords)
    {
        try (
            FileReader fileReader = new FileReader(inputFilePath); // Input file reader
            BufferedReader bufferedReader = new BufferedReader(fileReader); // BufferedReader to read lines (I decided to use this over the scanner, it was mentioned in Module 10 slides)
            PrintWriter writer = new PrintWriter(new FileWriter(intermediateFile)) // Output file writer
        ) {
            // Initialize record count
            int recordCount = 0;

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Extract columns and trim spaces
                String column1 = line.substring(0, 2).trim(); // State
                String column2 = line.substring(83, 90).trim(); // Population
                String column3 = line.substring(92, 99).trim(); // Child Population
                String column4 = line.substring(101, 108).trim(); // Child Poverty

                // Write to output file using formatted string
                writer.println(String.format("%-6s %-10s %-10s %-12s", column1, column2, column3, column4));

                // Increment recordCount
                recordCount++;
            }

            // Validate record count
            if (recordCount != numberOfRecords) {
                System.err.println("Warning: Number of records read (" + recordCount + ") does not match the expected number (" + numberOfRecords + ").");
            }
        } catch (FileNotFoundException e) {
            // Handle the case where the specified file could not be found
            System.err.println("File not found: " + e);
        } catch (IOException e) {
            // Handle other I/O related exceptions that may occur during file operations
            System.err.println("IOException found: " + e);
        }
    }
}