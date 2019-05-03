import java.sql.Connection;
import java.util.ArrayList;

public class SupplierRelationsInterface
{
	public static void Run(Connection conn)
	{

		while (true)
		{
			Console.Clear();

			Console.WriteLine();
			Console.WriteLine("Welcome, supplier relations manager!", "green");
			Console.WriteLine();

			Console.WriteLine("* * * * * * * * * * * * * * * * * * * * *");
			Console.WriteLine("*   SUPPLIER RELATION MANAGEMENT MENU   *");
			Console.WriteLine("* * * * * * * * * * * * * * * * * * * * *");

			Console.WriteLine();

			Console.WriteLine("Please select an action from the list below:");
			Console.WriteLine("\t1) Find and Manage Suppliers");
			Console.WriteLine("\t2) Create a New Supplier");
			Console.WriteLine();
			int action = Console.GetInt("Please enter a number between 1 and 2, or enter 0 to exit: ", "blue", 0, 2);

			switch (action)
			{
				case 0:
					return;
				case 1:
					SupplierSearch(conn);
					break;
				case 2:
					CreateSupplier(conn);
					break;
				default:
					Console.WriteLine("An unexpected error occurred. Returning to supplier relation manager's menu.", "red");
					break;
			}

			Console.Wait();
		}
	}

	private static void SupplierSearch(Connection conn)
	{
		Console.WriteLine("Welcome to the supplier search!");

		// loop until there are successful search results
		while (true)
		{
			String search = Console.GetString("Please enter a supplier name for the search: ", "blue");

			// search for suppliers and loop if no results found
			ArrayList<Supplier> suppliers = Supplier.FindByName(conn, search);
			if (suppliers.isEmpty())
			{
				Console.WriteLine("No results found. Please try again.", "yellow");
				continue;
			}

			// print out the instructors and break out of the loop
			Console.WriteLine("Here is a list of all matching suppliers:");
			for (Supplier supplier : suppliers)
			{
				Console.WriteLine("\t" + supplier.toString(true));
			}
			break;
		}

		Console.WriteLine("Enter the ID for the supplier you seek.", "blue");
		int id = Console.GetInt("Please enter an integer between 0 and 9999: ", "blue", 0, 9999);

		// attempt to retrieve the supplier with the given ID
		Supplier supplier = Supplier.GetById(conn, id);
		if (supplier == null)
		{
			Console.WriteLine("There are no suppliers found with that ID.", "yellow");
			return;
		}

		while(true)
		{
			Console.WriteLine("Please select an action from the list below:");
			Console.WriteLine("\t1) View Supplier's Address");
			Console.WriteLine("\t2) List Supplier's Phone Numbers");
			Console.WriteLine("\t3) Update Supplier Info");
			Console.WriteLine("\t4) Add New Phone Number for Supplier");

			int action = Console.GetInt("Please enter a number between 1 and 2, or enter 0 to exit: ", "blue", 0, 3);

			switch (action)
			{
				case 0:
					return;
				case 1:
					Console.WriteLine(supplier.GetFormattedAddress(), "green");
					break;
				case 2:
					ListSupplierPhoneNumbers(supplier);
					break;
				case 3:
					UpdateSupplier(conn, supplier);
					break;
				case 4:
					AddPhoneNumber(conn, supplier);
					break;
				default:
					Console.WriteLine("An unexpected error occurred. Returning to supplier relation manager's menu.", "red");
					return;
			}
		}
	}

	private static void ListSupplierPhoneNumbers(Supplier supplier)
	{
		if (supplier.GetPhoneNumbers().size() == 0)
		{
			Console.WriteLine("This supplier has no phone numbers in the database!", "yellow");
		}

		for (String phoneNumber : supplier.GetPhoneNumbers())
		{
			Console.WriteLine(phoneNumber, "green");
		}
	}

	private static void AddPhoneNumber(Connection conn, Supplier supplier)
	{
		String phoneNumber = Console.GetString("Please enter another phone number for this supplier: ", "blue");
		supplier.AddPhoneNumber(phoneNumber);

		if(supplier.Save(conn))
		{
			Console.WriteLine("Phone number '" + phoneNumber + "' added successfully!", "green");
		}
		else
		{
			supplier.Refresh(conn);
		}
	}

	private static void UpdateSupplier(Connection conn, Supplier supplier)
	{
		Console.WriteLine("You are now updating Supplier " + supplier.toString());

		String supplierName = Console.GetString("Please enter the new supplier name: ", "blue", 50);
		String streetName = Console.GetString("Please enter the new supplier's street name: ", "blue", 80);
		int streetNumber = Console.GetInt("Please enter the new supplier's street number: ", "blue", 1, 99999999);
		String city = Console.GetString("Please enter the new supplier's city: ", "blue", 50);
		String state = Console.GetString("Please enter the new supplier's state: ", "blue", 20);
		int zipCode = Console.GetInt("Please enter the new supplier's zip code: ", "blue", 0, 99999);

		supplier.SetName(supplierName);
		supplier.SetAddress(streetName, streetNumber, city, state, zipCode);

		boolean bSaved = supplier.Save(conn);
		if (bSaved)
		{
			Console.WriteLine("Product was successfully updated! Status is Supplier " + supplier.toString(), "green");
		}
	}

	private static void CreateSupplier(Connection conn)
	{
		Console.WriteLine("You are now adding a new supplier.");

		String supplierName = Console.GetString("Please enter the supplier name: ", "blue", 50);
		String streetName = Console.GetString("Please enter the supplier's street name: ", "blue", 80);
		int streetNumber = Console.GetInt("Please enter the supplier's street number: ", "blue", 1, 99999999);
		String city = Console.GetString("Please enter the supplier's city: ", "blue", 50);
		String state = Console.GetString("Please enter the supplier's state: ", "blue", 20);
		int zipCode = Console.GetInt("Please enter the supplier's zip code: ", "blue", 0, 99999);
		String phoneNumber = Console.GetString("Please enter a phone number for the supplier: ", "blue", 20);

		Supplier supplier = new Supplier(0, supplierName, streetName, streetNumber, city, state, zipCode);

		boolean bCreated = supplier.Create(conn);
		if (bCreated)
		{
			supplier.AddPhoneNumber(phoneNumber);
			boolean bPhoneAdded = supplier.Save(conn);
			if (bPhoneAdded)
			{
				Console.WriteLine("Product was successfully created! Status is Supplier " + supplier.toString(), "green");
			}
		}
	}
}
