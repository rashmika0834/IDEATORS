import java.util.Scanner;

public class app {

  public static void main(String ar[]) {
    int choice1;
    String url = "jdbc:mysql://localhost:3366/gpa_info";
    String table = "gpas";
    String username = "admin";
    String password = "1234";

    Scanner sc = new Scanner(System.in);
    System.out.println("---GPA Calculator---");
    System.out.println("Enter your name: ");
    String nameDb = sc.nextLine();
    while (nameDb.trim().length() <= 0) {
      System.out.println("Enter a valid name: ");
      nameDb = sc.nextLine();
    }
    System.out.println("Course duration in years: ");
    String durYrs = sc.nextLine();
    while (
      durYrs.trim().length() <= 0 ||
      Integer.parseInt(durYrs) <= 0 ||
      Integer.parseInt(durYrs) > 4
    ) {
      System.out.println("Enter a valid duration: ");
      durYrs = sc.nextLine();
    }

    sqlHandle sqlDb = new sqlHandle(url, table, username, password);
    System.out.println("\n\nCongratulations! Registration was completed.\n");
    System.out.println("Hello " + nameDb + ", Welcome back!\n");

    do{
      System.out.println("------Menu------");
      System.out.println("1. View results");
      System.out.println("2. Add results");
      System.out.println("3. Export data");
      System.out.println("4. Clear data");
      System.out.println("5. Exit");

      System.out.println("Enter your choice : ");
      choice1 = sc.nextInt();

      if(choice1<1 || choice1>5){
        System.out.println("Wrong choice. Please enter your choice again");
      }
    }while (choice1<1 || choice1>5);

    {
      if (choice1 == 1) {
        viewRes(sqlDb);
      } else if (choice1 == 2) {
        dbUpdate(sqlDb);
      } else if (choice1 == 4) {
        dbDelete(sqlDb);
      } else if (choice1 == 3) {
        exportDb(sqlDb);
      }
    }
    sc.close();
  }

  static void dbDelete(sqlHandle sqq) {
    sqq.deleteTable();
  }

  static void dbUpdate(sqlHandle sqq) {
    Scanner scan = new Scanner(System.in);
    String another = "y";

    System.out.println("-----Add Results------");

    System.out.println("Year(1,2,3,4) : ");
    int year = scan.nextInt();
    scan.nextLine();

    while (!another.equals("n")) {
      System.out.println("Course code : ");
      String subject = scan.nextLine();
      System.out.println("Grade : ");
      String grade = scan.nextLine();
      System.out.println("Credits : ");
      int credit = scan.nextInt();
      sqq.addRow(subject, credit, grade, year);
      System.out.println("Confirm (y/n) : ");
      String conf = scan.nextLine();

      System.out.println("\n\nRecord was added.\n");

      if (conf == "y") {
        sqq.addRow(subject, credit, grade, year);
      }
      System.out.println("Do you want to add another record ? (y/n)");
      another = scan.nextLine();
    }
    scan.close();
  }

  static void viewRes(sqlHandle sqq) {
    System.out.println("---View Results---");
    System.out.println("Current GPA: ");
    System.out.println("No. of subjects: ");
    System.out.println("Total credits: ");
    System.out.println("\nTo view records: ");
    System.out.println("1. Year 1");
    System.out.println("2. Year 2");
    System.out.println("3. Year 3");
    System.out.println("4. Year 4");
    System.out.println("0. Back");
    System.out.println("\nEnter your choice: ");
    Scanner scan = new Scanner(System.in);

    int inpt = scan.nextInt();
    if (inpt >= 1 && inpt <= 4) {
      sqq.viewRowYr(inpt);
    }
    if(inpt == 0){

    }
    System.out.println("\n\nd to delete records");
    System.out.println("Press 0 to go back");
    System.out.println("Enter input: ");
    String inpt2 = scan.nextLine();
    if (inpt2.equals("d")) {
      System.out.println("Enter row id to delete: ");
      int del_id = scan.nextInt();

      System.out.println("Confim? (y)");
      String del_conf = scan.nextLine();
      if (del_conf.equals("y")) {
        int del_out = sqq.deleteRow(del_id);
        if (del_out < 1) {
          System.out.println("Row was not deleted");
        } else {
          System.out.println("Row was deleted");
        }
      }
    }
  }

  static void exportDb(sqlHandle sqq) {
    sqq.exportDb();
    System.out.println("\nExported successfully.");
  }
}
