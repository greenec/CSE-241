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
        return this.Id;
    }
}