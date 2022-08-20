import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

/* 

"jdbc:mysql://localhost:3366/new_schema",
          "admin",
          "1234"

*/

class sqlHandle {

  //this.con = new Connection();
  String url;
  String table;
  String username;
  String password;

  sqlHandle(String url, String table, String username, String password) {
    this.url = url;
    this.table = table;
    this.username = username;
    this.password = password;
    //

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
        this.url,
        this.username,
        this.password
      );

      DatabaseMetaData md = con.getMetaData();
      ResultSet rs = md.getTables(null, null, "%", null);
      boolean db_available = false;
      while (rs.next()) {
        if (rs.getString(3).equals("table"));
        db_available = true;
        break;
      }
      if (!db_available) {
        Statement stmt = con.createStatement();
        String query =
          "create table " +
          this.table +
          " ( subject varchar(45), credit int, grade varchar(45), year int, id int )";
        stmt.executeUpdate(query);
      }
      con.close();
    } catch (Exception e) {
      System.out.println(e);
    }
    //
  }

  void addRow(String subject, int credit, String grade, int year) {
    int id = 1;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
        this.url,
        this.username,
        this.password
      );

      Statement stmt0 = con.createStatement();
      ResultSet rs = stmt0.executeQuery("select * from " + this.table + ";");

      if (rs.next()) {
        while (rs.next()) {
          id = rs.getInt("id") + 1;
        }
      }

      Statement stmt1 = con.createStatement();
      stmt1.executeUpdate(
        "INSERT INTO " +
        this.table +
        " (subject, credit, grade, year, id) VALUES ( '" +
        subject +
        "'," +
        credit +
        ", '" +
        grade +
        "'," +
        year +
        "," +
        id +
        ")"
      );
      con.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  void viewRowYr(int year) {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
        this.url,
        this.username,
        this.password
      );
      Statement stmt1 = con.createStatement();
      ResultSet rs = stmt1.executeQuery(
        "select * from " + this.table + " where year = " + year + ";"
      );
      int i0 = 0;
      int creditToll = 0;
      double yrGPA = 0;
      System.out.println("---Year " + year + "---");
      if (rs.next()) {
        while (rs.next()) {
          System.out.println(
            ++i0 + ".Course code - " + rs.getString("subject")
          );
          System.out.println("  Grade - " + rs.getString("grade"));
          yrGPA += getGPA(rs.getInt("credit"), rs.getString("grade"));
          System.out.println("  Credits - " + rs.getInt("credit"));
          creditToll += rs.getInt("credit");
          System.out.println("\n");
        }
        System.out.println("No. Of subjects: " + i0);
        System.out.println("Total credits: " + creditToll);
        System.out.println("GPA for year " + year + ": " + yrGPA / creditToll);
      } else {
        System.out.println("---No records ---");
      }

      con.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  double[] fullSummery() {
    double subs = 0;
    double creditToll = 0;
    double fullGPA = 0;
    double summery[] = { subs, creditToll, fullGPA };
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
        this.url,
        this.username,
        this.password
      );
      Statement stmt1 = con.createStatement();
      ResultSet rs = stmt1.executeQuery("select * from " + this.table + ";");

      if (rs.next()) {
        while (rs.next()) {
          subs++;
          creditToll += rs.getInt("credit");
          fullGPA += this.getGPA(rs.getInt("credit"), rs.getString("grade"));

          summery[0] = subs;
          //{ subs, creditToll, fullGPA/creditToll };
          summery[1] = creditToll;
          summery[2] = fullGPA / creditToll;
          //return summery;
        }
      }

      con.close();
    } catch (Exception e) {
      System.out.println(e);
    }
    return summery;
  }

  double getGPA(int credit, String grade) {
    double gpv = 0;
    if (grade.equals("A+")) {
      gpv = 4.0;
    }
    if (grade.equals("A")) {
      gpv = 4.0;
    }
    if (grade.equals("A-")) {
      gpv = 3.7;
    }
    if (grade.equals("B+")) {
      gpv = 3.3;
    }
    if (grade.equals("B")) {
      gpv = 3.0;
    }
    if (grade.equals("B-")) {
      gpv = 2.7;
    }
    if (grade.equals("C+")) {
      gpv = 2.3;
    }
    if (grade.equals("C")) {
      gpv = 2.0;
    }
    if (grade.equals("C-")) {
      gpv = 1.7;
    }
    if (grade.equals("D+")) {
      gpv = 1.3;
    }
    if (grade.equals("D")) {
      gpv = 1.0;
    }

    double tt = gpv * (double) credit;
    return tt;
  }

  int deleteRow(int id) {
    int k = 0;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
        this.url,
        this.username,
        this.password
      );
      Statement stmt1 = con.createStatement();
      int rs = stmt1.executeUpdate(
        "delete from " + this.table + " where id = " + id + ";"
      );

      con.close();
      if (rs >= 1) {
        k = 1;
      }
    } catch (Exception e) {
      System.out.println(e);
      k = -1;
    }
    return k;
  }

  void deleteTable() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
        this.url,
        this.username,
        this.password
      );
      Statement stmt1 = con.createStatement();
      int rs = stmt1.executeUpdate("delete from " + this.table + ";");

      con.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  void exportDb() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
        this.url,
        this.username,
        this.password
      );
      Statement stmt1 = con.createStatement();
      ResultSet rs = stmt1.executeQuery("select * from " + this.table + ";");

      if (rs.next()) {
        int i0 = 0;
        while (rs.next()) {
          writeToFile(
            ++i0 + "." +
            "  " +
            rs.getString("subject") +
            "  " +
            rs.getString("grade") +
            "  " +
            rs.getInt("credit") +
            "  " +
            rs.getInt("year")
          );
        }
      }

      con.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  void writeToFile(String fileData) {
    try {
      FileWriter dbExport = new FileWriter("export.txt", true);
      dbExport.write("\n" + fileData);
      dbExport.close();
     // System.out.println("Exported Successfully.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}
