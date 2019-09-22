package controller;

import repository.ConnectionSaver;
import service.CommonService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Dispatcher extends Thread {
    private CommonService commonService;
    private List<String> allowedFormats = new ArrayList();

    {
        allowedFormats.add("create [0-9]+ loads in location .+");
        allowedFormats.add("get information about locations [[a-zA-Z0-9]+ ]+[a-zA-Z0-9]+");
        allowedFormats.add("load data to xml file with name .+");
    }

    public Dispatcher() {
        this.commonService = new CommonService();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Please print command in format 'create N loads in location [LOCATION_NAME]' or " +
                    "'get information about locations [LOCATION_NAMES]' or " +
                    "'load data to xml file with name [NAME_OF_FILE]' or exit");
            String inputString = scanner.nextLine();
            int commandNumber = -1;
            for (int i = 0; i < allowedFormats.size(); i++) {
                if (inputString.matches(allowedFormats.get(i))) {
                     commandNumber = i;
                     break;
                }
            }
            if (inputString.equals("exit")) {
                ConnectionSaver.closeConnection();
                System.exit(0);
            }
            if (commandNumber == -1 && !inputString.equals("exit")) {
                System.out.println("Wrong format");
            } else {
                List<String> strings = Arrays.asList(inputString.split(" "));
                if (commandNumber == 0) {
                    commonService.createLoads(Integer.valueOf(strings.get(1)), strings.get(5));
                } else if (commandNumber == 1) {
                    commonService.getInfoAboutLocations(strings.subList(4, strings.size()));
                } else {
                    commonService.createXmlFile(strings.get(7));
                }
            }
        }
    }
}
