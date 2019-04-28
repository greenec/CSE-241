import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class ManagerInterface
{
	private static Scanner sc = new Scanner(System.in);

	public static void Run(Connection conn)
	{
		Console.WriteLine("Welcome, manager!", "green");

		while(true)
		{
			Console.WriteLine("* * * * * * * * * * *");
			Console.WriteLine("*  MANAGER'S  MENU  *");
			Console.WriteLine("* * * * * * * * * * *");

			Console.WriteLine("Please select an action from the list below:");
			Console.WriteLine("\t1) Search Products");

			int action = Console.GetInt("Please enter a number between 1 and 2, or enter 0 to exit: ", "blue", 0, 2);

			if (action == 0) {
				return;
			}

			switch (action) {
				case 1:
					ProductSearch(conn);
					break;
				default:
					Console.WriteLine("An unexpected error occurred. Returning to manager's menu.", "red");
					break;
			}
		}
	}

	private static void ProductSearch(Connection conn)
	{
		Console.WriteLine("Welcome to the product serach!");

		// loop until there are successful product search results
		while(true)
		{
			// prompt the user for a product name to find
			String search;
			while(true)
			{
				Console.Write("Please enter a product name for the search: ", "blue");
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

		Console.WriteLine("Enter the ID for the product you seek.", "blue");

		// loop until there is a valid instructor ID entered
		int id = Console.GetInt("Please enter an integer between 0 and 9999: ", "blue", 0, 9999);

		// attempt to retrieve the product with the given ID
		Product product = Product.GetById(conn, id);
		if(product == null)
		{
			Console.WriteLine("There are no products found with that ID.", "yellow");
		}
		else
		{
			Console.WriteLine("Product #" + product.GetProductId() + ": " + product.GetName() + " ($" + product.GetFormattedPrice() + ")");
		}
	}
}
