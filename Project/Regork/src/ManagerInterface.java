import java.sql.Connection;
import java.util.ArrayList;

public class ManagerInterface
{
	public static void Run(Connection conn)
	{
		Console.WriteLine("Welcome, manager!", "green");

		while (true)
		{
			Console.WriteLine("* * * * * * * * * * *");
			Console.WriteLine("*  MANAGER'S  MENU  *");
			Console.WriteLine("* * * * * * * * * * *");

			Console.WriteLine("Please select an action from the list below:");
			Console.WriteLine("\t1) Manage Products");

			int action = Console.GetInt("Please select an action, or enter 0 to exit: ", "blue", 0, 1);

			switch (action)
			{
				case 0:
					return;
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
		Console.WriteLine("Welcome to the product search!");

		// loop until there are successful product search results
		while (true)
		{
			// prompt the user for a product name to find
			String search = Console.GetString("Please enter a product name for the search: ", "blue");

			// search for instructors and loop if no results found
			ArrayList<Product> products = Product.FindByName(conn, search);
			if (products.isEmpty())
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
		int id = Console.GetInt("Please enter an integer between 0 and 9999: ", "blue", 0, 9999);

		// attempt to retrieve the product with the given ID
		Product product = Product.GetById(conn, id);
		if (product == null)
		{
			Console.WriteLine("There are no products found with that ID.", "yellow");
			return;
		}

		while(true)
		{
			Console.WriteLine("Please select an action from the list below:");
			Console.WriteLine("\t1) View Product Status");
			Console.WriteLine("\t2) Update Product");

			int action = Console.GetInt("Please enter a number between 1 and 2, or enter 0 to exit: ", "blue", 0, 2);

			switch (action)
			{
				case 0:
					return;
				case 1:
					Console.WriteLine(product.toString(), "green");
					break;
				case 2:
					UpdateProduct(conn, product);
					break;
				default:
					Console.WriteLine("An unexpected error occurred. Returning to manager's menu.", "red");
					return;
			}
		}
	}

	private static void UpdateProduct(Connection conn, Product product)
	{
		Console.WriteLine("You are now updating " + product.toString());

		while (true)
		{
			Console.WriteLine("Please select an action from the list below:");
			Console.WriteLine("\t1) Update Product Name");
			Console.WriteLine("\t2) Update Regork Sale Price");
			Console.WriteLine("\t3) Save product and return to product management");

			int action = Console.GetInt("Please enter a number between 1 and 3, or enter 0 to exit without saving: ", "blue", 0, 3);

			if (action == 0)
			{
				product.Refresh(conn);
				Console.WriteLine("Product changes have been reverted.", "yellow");
				return;
			}
			else if (action == 1)
			{
				String newName = Console.GetString("Please enter a new product name: ", "blue");
				product.SetName(newName);

				Console.WriteLine("Product has changes to be saved. Status is " + product.toString(), "yellow");
			}
			else if (action == 2)
			{
				Console.WriteLine("Note that the price will be stored with 4 decimal places, but only 2 will be shown.", "yellow");
				double newPrice = Console.GetDouble("Please enter a new Regork sale price: ", "blue", 0.0001, 999999.9999);
				product.SetPrice(newPrice);

				Console.WriteLine("Product has changes to be saved. Status is " + product.toString(), "yellow");
			}
			else if(action == 3)
			{
				boolean bSaved = product.Save(conn);
				if (bSaved)
				{
					Console.WriteLine("Product has been saved successfully!", "green");
					Console.WriteLine("New status is " + product.toString(), "green");
				}
				return;
			}
			else
			{
				Console.WriteLine("An unexpected error occurred. Returning to manager's menu.", "red");
				return;
			}
		}
	}
}
