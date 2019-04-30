import java.util.Date;

public class Shipment
{
	private int ShipmentId;
	private Date ShipmentDate;
	private double UnitPrice;
	private int Quantity;

	public Shipment(int shipmentId, Date shipmentDate, double unitPrice, int quantity)
	{
		this.ShipmentId = shipmentId;
		this.ShipmentDate = shipmentDate;
		this.UnitPrice = unitPrice;
		this.Quantity = quantity;
	}

	public int GetShipmentId()
	{
		return this.ShipmentId;
	}

	public Date GetShipmentDate()
	{
		return this.ShipmentDate;
	}

	public String toString()
	{
		return toString(false);
	}

	public String toString(boolean formatId)
	{
		String out = "#";

		if (formatId)
		{
			out += String.format("%-4s", this.GetShipmentId());
		}
		else
		{
			out += this.GetShipmentId();
		}

		out += " " + this.GetShipmentDate();

		return out;
	}
}
