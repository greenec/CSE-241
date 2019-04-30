import java.sql.Connection;
import java.sql.DriverManager;

public class Regork
{
	public static void main(String[] args)
	{
		// prompt the user to get information about their terminal
		Console.Initialize();

		// check for the Oracle DB driver
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e)
		{
			Console.WriteLine("Oracle driver not found, exiting with status code 1.", "red");
			System.exit(1);
		}

		// establish the database connection by prompting the user for credentials
		Connection conn;
		while (true)
		{
			Console.Write("enter Oracle user id: ", "blue");
			String userId = Console.ReadLine();

			Console.Write("enter Oracle password for " + userId + ": ", "blue");
			String password = Console.ReadLine();

			try
			{
				conn = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", userId, password);
				conn.setAutoCommit(false);
				break;
			} catch (Exception e)
			{
				Console.WriteLine("An error occurred while connecting to the database.", "red");
				Console.WriteLine("The username or password may be incorrect, or the database cannot be reached. Please try again.", "red");
			}
		}

		Console.WriteLine("Welcome to Regork!", "green");

		while (true)
		{
			Console.WriteLine("* * * * * * * * *");
			Console.WriteLine("*   MAIN MENU   *");
			Console.WriteLine("* * * * * * * * *");

			Console.WriteLine("Please enter the number next to your role from the list below: ");
			Console.WriteLine("\t1) Regork Product Manager");
			Console.WriteLine("\t2) Supplier Relations Manager");
			Console.WriteLine("\t3) Regork Customer");

			int role = Console.GetInt("Please enter a number between 1 and 3, or press 0 to exit: ", "blue", 0, 3);

			if (role == 0)
			{
				break;
			}

			switch (role)
			{
				case 1:
					ProductManagerInterface.Run(conn);
					break;
				case 2:
					SupplierRelationsInterface.Run(conn);
					break;
				case 3:
					CustomerInterface.Run(conn);
					break;
				default:
					Console.WriteLine("An unexpected error occurred. Returning to main menu.", "red");
					break;
			}
		}

		// close the connection before exiting
		try
		{
			conn.close();
			Console.WriteLine("Thanks for using Regork!", "green");
		} catch (Exception e)
		{
			Console.WriteLine("There was an error in trying to close the database connection.", "red");
		}
	}
}
