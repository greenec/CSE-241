public class Instructor
{
    private String Id;
    private String Name;

    public Instructor(String id, String name)
    {
        this.Id = id;
        this.Name = name;
    }

    public String getName()
    {
        return this.Name;
    }

    public String getId()
    {
        // pad the ID with leading zeros
        return String.format("%05d", Integer.parseInt(this.Id));
    }
}