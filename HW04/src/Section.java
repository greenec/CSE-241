class Section
{
    private String Department;
    private int CourseId;
    private String Title;
    private int SectionId;
    private String Semester;
    private int Year;
    private int Enrollment;

    Section(String Department, int CourseId, String Title, int SectionId, String Semester, int Year, int Enrollment)
    {
        this.Department = Department;
        this.CourseId = CourseId;
        this.Title = Title;
        this.SectionId = SectionId;
        this.Semester = Semester;
        this.Year = Year;
        this.Enrollment = Enrollment;
    }

    String getDepartment()
    {
        return this.Department;
    }

    int getCourseId()
    {
        return CourseId;
    }

    String getTitle()
    {
        return Title;
    }

    int getSectionId()
    {
        return SectionId;
    }

    String getSemester()
    {
        return Semester;
    }

    int getYear()
    {
        return Year;
    }

    int getEnrollment()
    {
        return Enrollment;
    }
}
