import java.util.Date;

public class Batch
{
	private int BatchId;
	public Product Product;
	public Date ProcessingDate;

	public Batch(int batchId, Product product, Date processingDate)
	{
		this.BatchId = batchId;
		this.Product = product;
		this.ProcessingDate = processingDate;
	}

	public int GetBatchId()
	{
		return this.BatchId;
	}

	public String toString()
	{
		return "#" + this.BatchId + " (" + this.ProcessingDate + ")";
	}
}
