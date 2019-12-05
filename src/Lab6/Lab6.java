package Lab6;

import javax.naming.InvalidNameException;
import java.util.*;
import java.io.*;

public class Lab6
{
    static HashMap<String,Integer> marks = new HashMap<String,Integer>();

    public static void main(String[] args)
    {
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        boolean more = true;
        while (more)
        { System.out.print("Name: ");
            String name = null;
            try
            { name = buf.readLine();
            }
            catch (Exception e)
            {
            }
            System.out.print("Mark: ");
            int mark = 0;
            try
            { mark = Integer.parseInt(buf.readLine().trim());
              if (mark < 0)
              {
                  throw new InvalidNumberException("Integer " + mark + " is an invalid negative entry");
              }
            }
            catch (InvalidNumberException e)
            {
                System.out.println(e.getMessage() + ", using default value 0.");
                mark = 0;
            }
            catch (Exception e)
            { System.out.println("invalid input - using 0");
            mark = 0;
            }
            marks.put(name, mark);
            System.out.print("More? ");
            try
            { if (buf.readLine().charAt(0)!='y')
                more = false;
            }
            catch (Exception e)
            {
            }
        }
        System.out.println(marks);
        printFormatted(marks);
        System.out.println(calcAverage(marks));
        System.out.println(getHighest(marks));
    }

    private static void printFormatted(Map<String, Integer> input)
    {
        for (Iterator<Map.Entry<String, Integer>> it = input.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<String, Integer> next = it.next();
            System.out.println(next.getKey() + ": " + next.getValue());
        }
    }

    private static double calcAverage(Map<String, Integer> input)
    {
        double count = 0;
        double sum = 0;
        for (Iterator<Map.Entry<String, Integer>> it = input.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<String, Integer> next = it.next();
            sum += next.getValue();
            count++;
        }
        return sum/count;
    }

    private static Map.Entry<String, Integer> getHighest(Map<String, Integer> input)
    {
        int highest = Integer.MIN_VALUE;
        Map.Entry<String, Integer> highestEnt = new AbstractMap.SimpleEntry<>("", 0);
        for (Iterator<Map.Entry<String, Integer>> it = input.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<String, Integer> next = it.next();
            if (next.getValue() > highest)
            {
                highest = next.getValue();
                highestEnt = next;
            }
        }
        return highestEnt;
    }
}

class InvalidNumberException extends Exception
{
    public InvalidNumberException(String errorMessage)
    {
        super(errorMessage);
    }
}


