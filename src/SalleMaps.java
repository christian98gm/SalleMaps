public class SalleMaps {

    public static void main(String[] args) {

        Menu menu = new Menu();
        OptionManager optionManager = new OptionManager();
        boolean exit = false;

        do {

            menu.showMainMenu();

            if(menu.isOptionEmpty()) {
                System.out.println();
                System.out.println("No option was introduced!");
            }

            if(!menu.isOptionInt()) {
                System.out.println();
                System.out.println("Introduced option is not a number!");
            } else {
                exit = optionManager.optionChosen(menu.getOptionInt());
            }

        } while(!exit);

    }

}