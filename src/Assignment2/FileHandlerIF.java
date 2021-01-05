package Assignment2;

import java.io.*;
import java.util.*;

/*
 * FileHandler is an interface pertaining methods considered standard for any file reader. This was implemented
 * with a view of abstraction, in which it could be re-used in further programs
 */
interface FileHandlerIF
{
    boolean openFile(String filename); // opens file, or creates if cannot open
    String readLine(); // returns lines from files
    boolean writeLine(String line, boolean eow); // writes line into file, eow indicates close file
}

/*
 * FileHandler is an abstract class which defines the methods set about in the FileHandler interface. This has been created
 * as abstract given getContents may differ from usage to usage, but generally the methods set about here will be
 * interchangeable
 */
abstract class FileHandler implements FileHandlerIF
{
    List<String> fileLines = new ArrayList<>(); // variable used to store lines
    Scanner fileReader; // used to read lines
    private PrintStream contentWriter; // outputs string to file
    File file; // reference to the file opened

    /*
     * default constructor, creates a new file or gets the contents of the opened file, therefor IOException will never
     * occur and can be ignored
     */
    FileHandler(String filename)
    {
        if (openFile(filename)) // opens file, if file exists returns true
        {
            getContents();  // exists, so get contents
        }
    }

    // abstract method to handle file contents
    abstract void getContents();

    // overrides I/F, simple reads a line. Usage would be combined with getContents implementation
    @Override
    public String readLine() {
        return fileReader.nextLine();
    }

    // opens a file, returns true if file exists so that getContents can handle contents
    @Override
    public boolean openFile(String fname)
    {
        file = new File(fname);
        return file.exists();
    }

    // overrides writeLine
    @Override
    public boolean writeLine(String line, boolean eow)
    {
        try {
            contentWriter = new PrintStream(file);
        }
        catch (IOException e){} // does nothing as file is either opened or created in openFile method
        contentWriter.println(line);
        if (eow)
        {
            contentWriter.close();
        }
        return false;
    }
}

/*
 * ScoreHandler is a more specialized version of File handler for this specific case of reading scores in the format
 * that I have set about, i.e forst word of a line is name, second is an integer of time in milliseconds, and third is
 * an integer of score. It's purpose is to both read this format and store it in an array of PlayerScore objects, ignoring
 * invalid lines of input.
 */
class ScoreHandler extends FileHandler
{
    private TreeSet<PlayerScore> scores; // sorted set of PlayerScore objects

    /*
     * default constructor, calls super to open file and instantiate fileLines through implementation fo getContents
     * if file exists populating fileLines
     */
    ScoreHandler(String filename)
    {
        super(filename);
        scores = new TreeSet();
        // populates scores with new PlayerScore entities for every line in file stored in fileLines
        if (fileLines.size() > 0)
        {
            for(int i = 0; i < fileLines.size(); i++)
            {
                String entry = fileLines.get(i);
                // only allows lines which can be split into 3 sections and with 2nd & 3rd word as integer format
                String[] entity= entry.split(" ");
                if (entity.length == 3)
                {
                    try
                    {
                        Integer.parseInt(entity[1]);
                        Integer.parseInt(entity[2]);
                        scores.add(new PlayerScore(entity));
                    }
                    catch (NumberFormatException ignore){} // skips to next line if invalid
                }
            }
        }
    }

    /*
     * Given this instance can use lines directly, getContents populates the fileLines from readLine
     */
    void getContents()
    {
        try {
            fileReader = new Scanner(file);
        }catch(FileNotFoundException ignored){} // does nothing as file is created by this class if not exists
        while (fileReader.hasNextLine())
        {
            String line = readLine();
            if (line != null)
            {
                fileLines.add(line);
            }
        }
        fileReader.close();
    }

    /*
     * writeScore's creates a string of every line required to write, and calls abstract classes writeLine to write to file
     */
    void writeScores()
    {
        String out = "";
        for (Iterator<PlayerScore> it = scores.iterator(); it.hasNext();)
        {
            PlayerScore next = it.next();
            out += next.getName() + " " + next.getScore() + " " + next.getTime() + (next != scores.last()? "\n" : "");
        }
        writeLine(out, true);
    }

    /*
     * addScore adds a new score object for writing via writeScores
     */
    void addScore(PlayerScore score)
    {
        scores.add(score);
    }

    // basic get method
    TreeSet<PlayerScore> getScores() {
        return scores;
    }
}