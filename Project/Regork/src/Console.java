import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Console
{
	private enum Background { Light, Dark };

	private static BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
	private static Background BackgroundColor = Background.Dark;

	public static void Initialize()
	{
		WriteLine("Please describe your terminal so that colored text can be displayed clearly.");
		while(true)
		{
			Write("Is your terminal background 'light' or 'dark'? ");
			String background = Console.ReadLine().toLowerCase();

			if (background.equals("light"))
			{
				BackgroundColor = Background.Light;
				WriteLine("Thanks! Output will be optimized for terminals with a 'light' background.", "green");
				return;
			}

			if (background.equals("dark"))
			{
				BackgroundColor = Background.Dark;
				WriteLine("Thanks! Output will be optimized for terminals with a 'dark' background.", "green");
				return;
			}
		}
	}

	public static String ReadLine()
	{
		String input;
		while (true)
		{
			try
			{
				if ((input = systemIn.readLine()) != null)
				{
					return input.trim();
				}

				Console.WriteLine();
				return "";
			}
			catch (Exception e)
			{
				Console.WriteLine("An error occurred while reading your input. Please try again.", "red");
			}
		}
	}

	public static Date GetDate(String prompt, String color)
	{
		while (true)
		{
			String sDate = GetString(prompt, color);
			try
			{
				java.util.Date utilDate = new SimpleDateFormat("MM-dd-yyyy").parse(sDate);
				Date date = new Date(utilDate.getTime());

				Date epoch = new Date(0);
				if (date.before(epoch))
				{
					Console.WriteLine("Please enter a date after January 1st, 1970.", "yellow");
					continue;
				}

				Date today = java.sql.Date.valueOf(LocalDate.now());
				if (date.after(today))
				{
					Console.WriteLine("Please enter a date that isn't in the future.", "yellow");
					continue;
				}

				return date;
			}
			catch (ParseException e)
			{
				Console.WriteLine("Please enter a valid date.", "yellow");
			}
		}
	}


	public static String GetString(String prompt, String color)
	{
		while (true)
		{
			Console.Write(prompt, color);
			String input = Console.ReadLine();

			if (input.length() != 0)
			{
				return input;
			}

			Console.WriteLine("Your input should not be empty.", "yellow");
		}
	}

	public static String GetString(String prompt, String color, int maxLength)
	{
		String input = "";
		while (true)
		{
			input = GetString(prompt, color);

			if (input.length() <= maxLength)
			{
				break;
			}

			WriteLine("Input should not be more than " + maxLength + " characters.", "yellow");
		}
		return input;
	}

	public static int GetInt(String prompt, String color, int min, int max)
	{
		int out;

		while(true)
		{
			Console.Write(prompt, color);
			String sInput = Console.ReadLine();

			// make sure that the input is a valid integer
			try
			{
				out = Integer.parseInt(sInput);
			}
			catch (NumberFormatException e)
			{
				Console.WriteLine("Please enter a valid integer.", "yellow");
				continue;
			}

			// make sure that the input is in range
			if(out >= min && out <= max)
			{
				return out;
			}

			Console.WriteLine("The number you entered is out of range.", "yellow");
		}
	}

	public static double GetDouble(String prompt, String color, double min, double max)
	{
		double out;

		while(true)
		{
			Console.Write(prompt, color);
			String sInput = Console.ReadLine();

			// make sure that the input is a valid double
			try
			{
				out = Double.parseDouble(sInput);
			}
			catch (NumberFormatException e)
			{
				Console.WriteLine("Please enter a valid decimal.", "yellow");
				continue;
			}

			// make sure that the double is in range
			if(out >= min && out <= max)
			{
				return out;
			}

			Console.WriteLine("The number you entered is out of range.");
		}
	}

	public static void Clear()
	{
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static void Wait()
	{
		Write("Please press enter to continue...");
		ReadLine();
	}

	public static void WriteLine()
	{
		Write("\n", "");
	}

	public static void WriteLine(String message)
	{
		Write(message + "\n", "");
	}

	public static void WriteLine(String message, String color)
	{
		Write(message + "\n", color);
	}

	public static void Write(String message)
	{
		Write(message, "");
	}


	public static void Write(String message, String color)
	{
		// clear formatting after each message
		message += "\033[0m";

		if (BackgroundColor == Background.Dark)
		{
			switch (color) {
				case "red":
					System.out.print("\033[1;31m" + message);
					break;
				case "green":
					System.out.print("\033[1;32m" + message);
					break;
				case "yellow":
					System.out.print("\033[1;33m" + message);
					break;
				case "blue":
					System.out.print("\033[1;36m" + message);
					break;
				default:
					System.out.print("\033[0m" + message);
			}
		}
		else if (BackgroundColor == Background.Light)
		{
			switch (color) {
				case "red":
					System.out.print("\033[0;31m" + message);
					break;
				case "green":
					System.out.print("\033[0;32m" + message);
					break;
				case "yellow":
					System.out.print("\033[0;33m" + message);
					break;
				case "blue":
					System.out.print("\033[0;34m" + message);
					break;
				default:
					System.out.print("\033[0m" + message);
			}
		}
		else
		{
			// invalid background color, print without formatting
			System.out.print("\033[0m" + message);
		}
	}
}
