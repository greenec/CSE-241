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
            Console.WriteLine("Oracle driver not found, exiting with status code 1.", "red");
            System.exit(1);
        }

        // establish the database connection by prompting the user for credentials
        Connection conn;
        while(true)
        {
            Console.Write("enter Oracle user id: ", "cyan");
            String userId = sc.nextLine();

            Console.Write("enter Oracle password for " + userId + ": ", "cyan");
            String password = sc.nextLine();

            try
            {
                conn = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", userId, password);
                break;
            }
            catch (Exception e)
            {
                Console.WriteLine("An error occurred while connecting to the database.", "red");
                Console.WriteLine("The username or password may be incorrect, or the database cannot be reached. Please try again.", "red");
            }
        }

        // loop until there are successful product search results
        while(true)
        {
            // prompt the user for a product name to find
            String search;
            while(true)
            {
                Console.Write("Please enter a product name for the search: ", "cyan");
                search = sc.nextLine();

                if(search.length() > 0)
                {
                    break;
                }

                Console.WriteLine("Your search cannot be empty.", "yellow");
            }

            // search for instructors and loop if no results found
            ArrayList<Product> products = Product.FindByName(conn, search);
            if(products.isEmpty())
            {
                Console.WriteLine("No results found. Please try again.", "yellow");
                continue;
            }

            // print out the instructors and break out of the loop
            Console.WriteLine("Here is a list of all matching products:");
            for (Product product : products)
            {
                Console.WriteLine("    #" + String.format("%-4s", product.GetProductId()) + " " + product.GetName());
            }
            break;
        }

        Console.WriteLine("Enter the ID for the product you seek.", "cyan");

        // loop until there is a valid instructor ID entered
        int id = Console.GetInt("Please enter an integer between 0 and 9999: ", "cyan", 0, 9999);

        // attempt to retrieve the product with the given ID
        Product product = Product.GetById(conn, id);
        if(product == null)
        {
            Console.WriteLine("There are no products found with that ID.", "yellow");
        }
        else
        {
            Console.WriteLine();
            Console.WriteLine("Product #" + product.GetProductId() + ": " + product.GetName() + " ($" + product.GetFormattedPrice() + ")");
        }

        // close the connection before exiting
        try
        {
            conn.close();
        }
        catch (Exception e)
        {
            Console.WriteLine("There was an error in trying to close the database connection.", "red");
        }
    }
}
