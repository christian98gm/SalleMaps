import Model.City;
import model.DestinationCity;
import model.Route;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
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

    public void askForCity() {
        //Get city name
        System.out.println();
        Scanner sc = new Scanner(System.in).useDelimiter(System.lineSeparator());
        System.out.print("City: ");
        option = sc.nextLine();
    }

    public void showCityInformation(City city, List<DestinationCity> destinations) {

        System.out.println();
        System.out.println(String.format("%s (%s)", city.getName(), city.getCountry()));
        System.out.println(String.format(Locale.US, "Geolocation: (%.2f, %.2f)", city.getLatitude(),
                city.getLongitude()));

        for(int i = 0; i < destinations.size(); i++) {
            DestinationCity destination = destinations.get(i);
            City destinationCity = destination.getCity();
            System.out.println();
            System.out.println(String.format("Destination %d", (i + 1)));
            System.out.println(String.format("%s (%s)", destinationCity.getName(), destinationCity.getCountry()));
            System.out.println(String.format(Locale.US, "Geolocation: (%.2f, %.2f)", destinationCity.getLatitude(),
                    destinationCity.getLongitude()));
            System.out.println(String.format("Travel duration: %s", timeFromSeconds(destination.getDuration())));
            System.out.println(String.format(Locale.US, "Travel distance: %.3f km",
                    (double) destination.getDistance() / 1000));
        }

    }

    private String timeFromSeconds(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime time = LocalTime.of(hours, minutes, seconds);
        return time.format(formatter);
    }

    public void askForOrigin() {
        //Get city name
        System.out.println();
        Scanner sc = new Scanner(System.in).useDelimiter(System.lineSeparator());
        System.out.print("From: ");
        option = sc.nextLine();
    }

    public void askForDestiny() {
        //Get city name
        System.out.println();
        Scanner sc = new Scanner(System.in).useDelimiter(System.lineSeparator());
        System.out.print("To: ");
        option = sc.nextLine();
    }

    public void showRouteMenu() {

        //Show options
        System.out.println();
        System.out.println("1. Shortest route");
        System.out.println("2. Fastest route");

        //Get option
        Scanner sc = new Scanner(System.in).useDelimiter(System.lineSeparator());
        System.out.print("Option: ");
        option = sc.nextLine();

    }

    public void showRoute(City from, City to, Route route) {

        System.out.println();
        System.out.println(String.format("Route from %s to %s", from.getName(), to.getName()));
        System.out.println(String.format(Locale.US, "Travel distance: %.3f km",
                    (double) route.getDistance() / 1000));
        System.out.println(String.format("Travel duration: %s", timeFromSeconds(route.getDuration())));

        //Show cities
        List<City> cities = route.getCities();
        for(int i = 0; i < cities.size(); i++) {
            System.out.print(cities.get(i).getName());
            if(i < cities.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();

    }

    public void showOptimizationMenu() {

        //Show options
        System.out.println();
        System.out.println("1. No optimization");
        System.out.println("2. Tree optimization");
        System.out.println("3. Hash table optimization");

        //Get option
        Scanner sc = new Scanner(System.in).useDelimiter(System.lineSeparator());
        System.out.print("Option: ");
        option = sc.nextLine();

    }

    public void notifyEmptyOption() {
        System.out.println();
        System.out.println("No option was introduced!");
    }

    public void notifyNoIntOption() {
        System.out.println();
        System.out.println("Introduced option is not a number!");
    }

    public void notifyExit() {
        System.out.println();
        System.out.println("Thanks for using our services!");
    }

    public void notifyInvalidIntRange() {
        System.out.println();
        System.out.println("Selected option out of range!");
    }

    public void notifyUnavailableOption() {
        System.out.println();
        System.out.println("Unavailable option!");
    }

    public void notifyCityNotFound() {
        System.out.println();
        System.out.println("Requested city was not found!");
    }

    public void notifyConnectionsError() {
        System.out.println();
        System.out.println("Error! Could not load some connections!");
    }

    public void notifyCityAdded() {
        System.out.println();
        System.out.println("NEW CITY ADDED");
    }

    public void notifyUnreachableCity(City from, City to) {
        System.out.println();
        System.out.println(String.format("Can't reach %s from %s", to.getName(), from.getName()));
    }

    public void notifyOperationTime(long nanos) {
        System.out.println();
        System.out.println(String.format("This operation last %.3f ms", (double) nanos / 1000000));
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