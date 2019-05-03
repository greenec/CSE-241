import java.sql.Connection;
import java.util.ArrayList;

public class ProductManagerInterface
{
	public static void Run(Connection conn)
	{
		while (true)
		{
			Console.Clear();

			Console.WriteLine();
			Console.WriteLine("Welcome, product manager!", "green");
			Console.WriteLine();

			Console.WriteLine("* * * * * * * * * * * * * * * *");
			Console.WriteLine("*   PRODUCT MANAGEMENT MENU   *");
			Console.WriteLine("* * * * * * * * * * * * * * * *");

			Console.WriteLine();

			Console.WriteLine("Please select an action from the list below:");
			Console.WriteLine("\t1) Manage Products");
			Console.WriteLine("\t2) Create a New Product");
			Console.WriteLine();
			int action = Console.GetInt("Please enter a number between 1 and 2, or enter 0 to exit: ", "blue", 0, 2);

			switch (action)
			{
				case 0:
					return;
				case 1:
					ProductSearch(conn);
					break;
				case 2:
					CreateProduct(conn);
					break;
				default:
					Console.WriteLine("An unexpected error occurred. Returning to manager's menu.", "red");
					break;
			}

			Console.Wait();
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
				Console.WriteLine("\t" + product.toString(true));
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
					Console.WriteLine(product.toString(false, true), "green");
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
		Console.WriteLine("You are now updating Product " + product.toString(false, true));

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
				product.Name = Console.GetString("Please enter a new product name: ", "blue", 50);

				Console.WriteLine("Product has changes to be saved. Status is " + product.toString(false, true), "yellow");
			}
			else if (action == 2)
			{
				Console.WriteLine("Note that the price will be stored with 4 decimal places, but only 2 will be shown.", "yellow");
				product.Price = Console.GetDouble("Please enter a new Regork sale price: ", "blue", 0.0001, 999999.9999);

				Console.WriteLine("Product has changes to be saved. Status is " + product.toString(false, true), "yellow");
			}
			else if(action == 3)
			{
				boolean bSaved = product.Save(conn);
				if (bSaved)
				{
					Console.WriteLine("Product has been saved successfully!", "green");
					Console.WriteLine("New status is " + product.toString(false, true), "green");
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

	private static void CreateProduct(Connection conn)
	{
		Product product = new Product(0);

		Console.WriteLine("You are now creating a new product.");

		String productName = Console.GetString("Please enter the product name: ", "blue", 50);
		double productPrice = Console.GetDouble("Please enter the Regork retail price: ", "blue", 0, 999999.9999);

		product.Name = productName;
		product.Price = productPrice;

		boolean bCreated = product.Create(conn);
		if (bCreated)
		{
			Console.WriteLine("Product was successfully created! Status is Product " + product.toString(false, true), "green");
		}
	}
}
