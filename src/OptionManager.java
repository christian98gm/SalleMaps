import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class OptionManager {

    //Raw path
    private final static String RAW_PATH = "raw/";

    //Possible options
    private final static int IMPORT = 1;
    private final static int SEARCH = 2;
    private final static int ROUTE = 3;
    private final static int EXIT = 4;

    //Managers
    private MapGraph mapGraph;

    public OptionManager() {}

    public boolean optionChosen(int option) {
        switch(option) {
            case IMPORT:
                importMap();
                return false;
            case SEARCH:
                return false;
            case ROUTE:
                return false;
            case EXIT:
                System.out.println();
                System.out.println("Thanks for using our services!");
                return true;
            default:
                System.out.println();
                System.out.println("Selected option out of range [1, 4]!");
                return false;
        }
    }

    private void importMap() {

        Menu menu = new Menu();
        boolean importOk;

        //Ask for file until ok
        do {

            importOk = false;
            menu.askForFile();
            if(!menu.isOptionEmpty()) {

                try {

                    //Read file as text
                    BufferedReader br = new BufferedReader(new FileReader(RAW_PATH + menu.getOptionString()));
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();

                    while(line != null) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                        line = br.readLine();
                    }

                    //Read file content
                    JsonManager jsonManager = new JsonManager(sb.toString());
                    List<City> cities = jsonManager.getCities();
                    List<Connection> connections = jsonManager.getConnections();

                    //Create graph
                    mapGraph = new MapGraph(cities, connections);

                    //All ok
                    importOk = true;
                    System.out.println();
                    System.out.println("Map loaded!");

                } catch(IOException e1) {
                    System.out.println();
                    System.out.println("File not found!");
                } catch(JSONException e2) {
                    System.out.println();
                    System.out.println(e2.getMessage());
                }

            }

        } while(!importOk);

    }

}