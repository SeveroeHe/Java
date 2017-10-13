import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;
/**
 * @Author: Qiyang He (qiyangh AT andrew DOT cmu DOT edu).
 * This csv reader is mainly for self-use.
 * Simple realization of reading csv format file into regular output(String arrays) from line to line.
 *
 */
public class Csvreader extends BufferedReader {
    /**
     * Initializes the class.
     * @param in the reader specify the csv file input
     */
    public Csvreader(Reader in) {
        super(in);
    }
    /**
     * @return processed string array with clean format (in origin seq).
     * @throws IOException throws IOException
     */
    public String[] readCSVLine() throws IOException{
        //read one line from a file as a string, probably call this method within a loop
        String line = super.readLine();
        if (line == null) {
            return null;
        }
        // Count comma to set breakpoint 
        int countcomma = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ',') {
                countcomma += 1;
            }
        }
        // Allocate an array of the necessary size to return the strings
        String[] clean = new String[countcomma + 1];
        // Set begin and end index to scratch each string into single array element
        int startid = 0;

        for (int i = 0; i < countcomma; i++) {
            // set endid to the position of the (next) comma. 
            // during the loop it will be positioned onto diff comma
            int endid = line.indexOf(',', startid);
            // remove the quotes aroung the string
            if (line.charAt(startid) == '"' && line.charAt(endid - 1) == '"') {
                clean[i] = line.substring(startid + 1, endid - 1);
            } else {
                //if the string does not surrounded by quotes, simply scratch it
                clean[i] = line.substring(startid, endid);
            }
            //set new startid
            startid = endid + 1;
        }

        // handle the last string(which positioned after the last comma).
        if (line.charAt(startid) == '"' && line.charAt(line.length() - 1) == '"') {
            clean[countcomma] = line.substring(startid + 1, line.length() - 1);
        } else {
            clean[countcomma] = line.substring(startid, line.length());
        }
        return clean;
    }
}
