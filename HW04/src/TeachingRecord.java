import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
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

        try
        {
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", userId, password);

            System.out.print("Input name search substring: ");
            String search = sc.nextLine();

            PreparedStatement stmt = conn.prepareStatement("SELECT id, name FROM instructor WHERE name LIKE ?");
            stmt.setString(1, "%" + search + "%");
            ResultSet res = stmt.executeQuery();

            while(res.next())
            {
                String id = res.getString("ID");
                String name = res.getString("NAME");

                System.out.println(id + " " + name);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
