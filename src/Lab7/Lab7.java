package Lab7;
import java.io.*;
import java.sql.*;

public class Lab7 {
     public static void main(String[] args)
    {
        Connection connection = null;

        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/orderdb", "root", "ma216000");
            // past experience has shown that using the one-argument version of getConnection doesn't work for some students
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("driver not found");
            System.exit(1);
        }
        catch (SQLException e)
        { System.out.println("failed to connect to database");
            System.exit(1);
        }

        Statement statement = null;

        try{
            statement = connection.createStatement();
        }
        catch (SQLException e)
        {
            System.out.println("failed to access database");
            System.exit(1);
        }

        queryDatabase(statement);

        try
        {
            statement.close();
            connection.close();
            System.out.println("Connection closed");
        }
        catch (SQLException e)
        {
            System.out.println("problems closing connection");
            System.exit(1);
        }
    }

    static void queryDatabase(Statement statement)
    {
//        String query = "INSERT INTO orders VALUES(8, 3, 'desktop pc', 375.5)";
        String query;
        try
        {
            BufferedReader b = new BufferedReader(new FileReader("E:\\M drive\\Second Year\\CE203 - Application Programming\\lab7\\orders_2.txt"));
            for (query = b.readLine(); query != null; query=b.readLine())
            {
                String[] queryArray = query.split("\\s+");
                query = String.format("INSERT INTO orders VALUES (%s, %s, '%s', %s)", queryArray[0], queryArray[1],
                        queryArray[2], queryArray[3]);
                statement.executeUpdate(query);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File could not be found");
        }
        catch (IOException e)
        {
            System.out.println("Issue with buffered reader");
        }
        catch (SQLException e)
        {
            System.out.println("Unable to insert values" + "\n" + e.getMessage());
        }
        query = "SELECT * FROM orders";
        int total, count;
        total = count = 0;
        try
        {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next())
            {
                count += 1;
                total += rs.getInt(4);
            }
            System.out.println("Total: " + total + ", Count: " + count + ", Mean: " + (double) total/count);
        }
        catch (SQLException e)
        {
            System.out.println("issue with query " + query);
        }
    }
}

