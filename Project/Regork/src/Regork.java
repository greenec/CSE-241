import java.sql.Connection;
import java.sql.DriverManager;

import java.util.ArrayList;
import java.util.Scanner;

public class Regork
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        // check for the Oracle DB driver
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Oracle driver not found, exiting with status code 1.");
            System.exit(1);
        }

        // establish the database connection by prompting the user for credentials
        Connection conn;
        while(true)
        {
            System.out.print("enter Oracle user id: ");
            String userId = sc.nextLine();

            System.out.print("enter Oracle password for " + userId + ": ");
            String password = sc.nextLine();

            try
            {
                conn = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", userId, password);
                break;
            }
            catch (Exception e)
            {
                System.out.println("An error occurred while connecting to the database.");
                System.out.println("The username or password may be incorrect, or the database cannot be reached. Please try again.");
            }
        }

        // loop until there are successful product search results
        while(true)
        {
            // prompt the user for a product name to find
            String search;
            while(true)
            {
                System.out.print("Please enter a product name for the search: ");
                search = sc.nextLine();

                if(search.length() > 0)
                {
                    break;
                }

                System.out.println("Your search cannot be empty.");
            }

            // search for instructors and loop if no results found
            ArrayList<Product> products = Product.FindByName(conn, search);
            if(products.isEmpty())
            {
                System.out.println("No results found. Please try again.");
                continue;
            }

            // print out the instructors and break out of the loop
            System.out.println("Here is a list of all matching IDs");
            for (Product product : products)
            {
                System.out.println("    #" + product.GetProductId() + " " + product.GetName());
            }
            break;
        }

        // loop until there is a valid instructor ID entered
        System.out.println("Enter the ID for the product you seek.");

        int id;
        while(true)
        {
            System.out.print("Please enter an integer between 0 and 9999: ");
            String sId = sc.nextLine();

            // make sure that the ID is a valid integer
            try
            {
                id = Integer.parseInt(sId);
            }
            catch (NumberFormatException e)
            {
                continue;
            }

            // make sure that the ID is in range
            if(id >= 0 && id <= 9999)
            {
                break;
            }
        }

        // attempt to retrieve the product with the given ID
        Product product = Product.GetById(conn, id);
        if(product == null)
        {
            System.out.println("There were no products found with that ID.");
        }
        else
        {
            System.out.println();
            System.out.println("Product #" + product.GetProductId() + ": " + product.GetName() + " ($" + product.GetPrice() + ")");
        }

        // close the connection before exiting
        try
        {
            conn.close();
        }
        catch (Exception e)
        {
            System.out.println("There was an error in trying to close the database connection.");
        }
    }
}
