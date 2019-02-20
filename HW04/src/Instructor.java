import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;

class Instructor
{
    private int Id;
    private String Name;
    private ArrayList<Section> Sections;

    private Instructor(int id, String name)
    {
        this.Id = id;
        this.Name = name;
    }

    String getName()
    {
        return this.Name;
    }

    String getFormattedId()
    {
        // pad the ID with leading zeros
        return String.format("%05d", this.Id);
    }

    static ArrayList<Instructor> findByName(Connection conn, String search)
    {
        ArrayList<Instructor> instructors = new ArrayList<>();

        try
        {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, name FROM instructor WHERE INSTR(name, ?) <> 0");
            stmt.setString(1, search);
            ResultSet res = stmt.executeQuery();

            while (res.next())
            {
                int id = res.getInt("ID");
                String name = res.getString("NAME");

                Instructor instructor = new Instructor(id, name);
                instructors.add(instructor);
            }

            stmt.close();
        }
        catch (Exception e)
        {
            System.out.println("An error has occurred. Please try again.");
        }

        return instructors;
    }

    static Instructor getById(Connection conn, int instructorId)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, name FROM instructor WHERE id = ?");
            stmt.setInt(1, instructorId);
            ResultSet res = stmt.executeQuery();

            if (res.next())
            {
                String name = res.getString("NAME");

                Instructor instructor = new Instructor(instructorId, name);

                if(!instructor.loadSections(conn))
                {
                    System.out.println("An error occurred while trying to load the instructor's sections.");
                }

                stmt.close();
                return instructor;
            }
        }
        catch (Exception e)
        {
            System.out.println("An error occurred in retrieving the instructor by their ID");
        }

        return null;
    }

    void printTeachingRecord()
    {
        System.out.printf("Teaching record for instructor %s %s\n\n", this.getFormattedId(), this.getName());

        if (this.Sections.isEmpty())
        {
            System.out.println("This instructor teaches no classes.");
            return;
        }

        String rowFormat = "%-20s %-8s %-50s %-8s %-6s %-4s %-10s\n";

        System.out.printf(rowFormat, "Department", "CNO", "Title", "Sec", "Sem", "Year", "Enrollment");
        for (Section s : this.Sections)
        {
            System.out.printf(rowFormat, s.getDepartment(), s.getCourseId(), s.getTitle(), s.getSectionId(), s.getSemester(), s.getYear(), s.getEnrollment());
        }
    }

    private boolean loadSections(Connection conn)
    {
        this.Sections = new ArrayList<>();

        String sql =
            "SELECT c.dept_name DEPT_NAME, c.course_id COURSE_ID, c.title TITLE, t.sec_id SEC_ID, t.semester SEMESTER, t.year YEAR, COALESCE(e.enrollment, 0) ENROLLMENT\n" +
            "    FROM teaches t\n" +
            "    INNER JOIN course c ON t.course_id = c.course_id\n" +
            "    LEFT JOIN (\n" +
            "        SELECT t.sec_id SEC_ID, t.course_id COURSE_ID, COUNT(*) ENROLLMENT\n" +
            "            FROM takes t\n" +
            "            GROUP BY t.course_id, t.sec_id\n" +
            "    ) e ON e.course_id = t.course_id AND e.sec_id = t.sec_id\n" +
            "    WHERE t.ID = ?\n" +
            "    ORDER BY c.dept_name, c.course_id, year, semester";

        try
        {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, this.Id);
            ResultSet res = stmt.executeQuery();

            while(res.next())
            {
                String deptName = res.getString("DEPT_NAME");
                int courseId = res.getInt("COURSE_ID");
                String title = res.getString("TITLE");
                int sectionId = res.getInt("SEC_ID");
                String semester = res.getString("SEMESTER");
                int year = res.getInt("YEAR");
                int enrollment = res.getInt("ENROLLMENT");

                this.Sections.add(new Section(deptName, courseId, title, sectionId, semester, year, enrollment));
            }

            stmt.close();
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }
}