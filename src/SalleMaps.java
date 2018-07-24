public class SalleMaps {

    public static void main(String[] args) {

        Menu menu = new Menu();
        OptionManager optionManager = new OptionManager();
        boolean exit = false;

        do {

            menu.showMainMenu();

            if(menu.isOptionEmpty()) {
                menu.notifyEmptyOption();
            }

            if(!menu.isOptionInt()) {
                menu.notifyNoIntOption();
            } else {
                exit = optionManager.optionChosen(menu.getOptionInt());
            }

        } while(!exit);

    }

}