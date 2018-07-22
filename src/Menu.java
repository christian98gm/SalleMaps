import java.util.Scanner;

public class Menu {

    private String option;

    public Menu() {
        option = "";
    }

    public void showMainMenu() {

        //Show options
        System.out.println();
        System.out.println("1. Import map");
        System.out.println("2. Search city");
        System.out.println("3. Calculate route");
        System.out.println("4. Shut down");
        System.out.println();

        //Get option
        Scanner sc = new Scanner(System.in).useDelimiter(System.lineSeparator());
        System.out.print("Option: ");
        option = sc.nextLine();

    }

    public void askForFile() {
        //Get file name
        System.out.println();
        Scanner sc = new Scanner(System.in).useDelimiter(System.lineSeparator());
        System.out.print("File: ");
        option = sc.nextLine();
    }

    public boolean isOptionEmpty() {
        return option.isEmpty() || option.startsWith(System.lineSeparator());
    }

    public boolean isOptionInt() {

        //Check if it's empty
        if(isOptionEmpty()) {
            return false;
        }

        //Check if it's an integer
        try {
            Integer.parseInt(option);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }

    }

    public int getOptionInt() {
        if(!isOptionEmpty() && isOptionInt()) {
            return Integer.parseInt(option);
        }
        return 0;
    }

    public String getOptionString() {
        if(!isOptionEmpty()) {
            return option;
        }
        return null;
    }

}