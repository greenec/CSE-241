import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class TeachingRecord
{
    public static void main(String[] args)
    {
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Oracle driver not found, exiting with status code 1.");
            System.exit(1);
        }

        Scanner sc = new Scanner(System.in);

        System.out.print("enter Oracle user id: ");
        String userId = sc.nextLine();

        System.out.print("enter Oracle password for " + userId + ": ");
        String password = sc.nextLine();

        try ( Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", userId, password); )
        {
            System.out.print("Input name search substring: ");
            String search = sc.nextLine();

            // TODO: check for empty strings and sanitize

            ArrayList<Instructor> instructors = searchInstructors(conn, search);
            for (Instructor instructor : instructors)
            {
                System.out.println(instructor.getId() + " " + instructor.getName());
            }
        }
        catch (Exception e)
        {
            System.out.println("An error occurred while connecting to the database.");
            System.out.println("The username or password may be incorrect, or the database cannot be reached.");
            System.exit(1);

            // TODO: prompt for credentials again?
        }
    }

    private static ArrayList<Instructor> searchInstructors(Connection conn, String search)
    {
        ArrayList<Instructor> instructors = new ArrayList<>();

        try
        {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, name FROM instructor WHERE name LIKE ?");
            stmt.setString(1, "%" + search + "%");
            ResultSet res = stmt.executeQuery();

            while (res.next())
            {
                String id = res.getString("ID");
                String name = res.getString("NAME");

                Instructor instructor = new Instructor(id, name);
                instructors.add(instructor);
            }

            stmt.close();
        }
        catch (Exception e)
        {
            // TODO: handle the exception
        }

        return instructors;
    }

    private static Instructor getInstructorById(Connection conn, String id)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, name FROM instructor WHERE id = ?");
            stmt.setString(1, id);
            ResultSet res = stmt.executeQuery();

            if (res.next())
            {
                // String id = res.getString("ID");
                String name = res.getString("NAME");

                Instructor instructor = new Instructor(id, name);

                stmt.close();
                return instructor;
            }
        }
        catch (Exception e)
        {
            // TODO: handle the exception
        }

        // handle this
        return null;
    }
}
