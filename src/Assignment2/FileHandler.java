package Assignment2;

import netscape.javascript.JSObject;

import java.io.*;
import java.util.*;

interface FileHandlerIF
{
    boolean openFile(String filename); // opens file, or creates if cannot open
    String readLine(); // returns lines from files
    boolean writeLine(String line, boolean eow) throws IOException; // writes line into file, eow indicates close file
}

abstract class FileHandler implements FileHandlerIF
{
    private Scanner fileReader;
    private PrintStream contentWriter; // outputs string to file
    List<String> fileLines = new ArrayList<>(); // variable used to store lines
    private String filename;
    private File file;

    public FileHandler(String filename)
    {
        if (openFile(filename)) // opens file, if file exists returns true
        {
            getContents();  // exists, so get contents
        }
    }

    private void getContents()
    {
        fileReader = new Scanner(filename);
        while (fileReader.hasNextLine())
        {
            String line = readLine();
            fileLines.add(line);
        }
        fileReader.close();
    }

    @Override
    public String readLine() {
        return fileReader.nextLine();
    }

    @Override
    public boolean openFile(String fname)
    {
        this.filename = fname;
        file = new File(filename);
        return file.exists();
    }

    @Override
    public boolean writeLine(String line, boolean eow) throws IOException{
        contentWriter = new PrintStream(file);
        contentWriter.println(line);
        if (eow)
        {
            contentWriter.close();
        }
        return false;
    }
}

class ScoreHandler extends FileHandler
{
    private TreeSet<PlayerScore> scores;

    ScoreHandler(String filename)
    {
        super(filename);
        scores = new TreeSet();
        // populates scores with new PlayerScore entities for every line in file
        if (fileLines.size() > 0)
        {
            for(int i = 0; i < fileLines.size(); i++)
            {
                String entry = fileLines.get(i);
                scores.add(new PlayerScore(entry));
            }
        }
    }

    public void writeScores() throws IOException
    {
        for (Iterator<PlayerScore> it = scores.iterator(); it.hasNext(); )
        {
            PlayerScore next = it.next();
            if (next != scores.last())
            {
                writeLine(next.getName() + " " + next.getScore() + " " + next.getTime(), false);
            }
        }
        writeLine(scores.last().getName() + " " + scores.last().getScore() + " " + scores.last().getTime(), true);
    }
}