package es.udc.ws.runfic.ui;

import es.udc.ws.runfic.service.ClientRunFicService;
import es.udc.ws.runfic.service.ClientRunFicServiceFactory;
import es.udc.ws.runfic.service.dto.ClientInscriptionDto;
import es.udc.ws.runfic.service.dto.ClientRaceDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RunFicServiceClient {
    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientRunFicService clientRunFicService =
                ClientRunFicServiceFactory.getService();
        if("-a".equalsIgnoreCase(args[0])) {
            validateArgs(args, 6, new int[] {4, 5});

            // [addRace] RaceServiceClient -a <city> <description> <startDateTime> <price> <maxParticipants>

            try {
                Long raceID = clientRunFicService.addRace(
                        new ClientRaceDto(null, args[1], args[2], LocalDateTime.parse(args[3]),
                        Float.parseFloat(args[4]), 0, Integer.parseInt(args[5])));

                System.out.println("Race " + raceID + " created successfully");

            } catch (NumberFormatException ex) {
                printErrorMsgAndExit("Invalid input (not a number): " + ex.getLocalizedMessage());
            } catch (InputValidationException ex) {
                printErrorMsgAndExit("Invalid arguments: " + ex.getLocalizedMessage());
            } catch (Exception ex) {
                printErrorMsgAndExit(ex.getLocalizedMessage());
            }

        } else if("-f".equalsIgnoreCase(args[0])) {
        validateArgs(args, 2, new int[] {1});

        // [findRace] RaceServiceClient -f <raceID>

            try {
                ClientRaceDto race = clientRunFicService.findRace(Long.parseLong(args[1]));

                System.out.println("Id: " + race.getRaceID() +
                    ", City: " + race.getCity() +
                    ", Description: " + race.getDescription() +
                    ", StartDateTime: " + race.getStartDateTime() +
                    ", Price: " + race.getPrice() +
                    ", Participants: " + race.getParticipants() +
                    ", MaxParticipants: " + race.getMaxParticipants());

            } catch (NumberFormatException ex) {
                printErrorMsgAndExit("Invalid input (not a number): " + ex.getLocalizedMessage());
            } catch (InstanceNotFoundException ex) {
                printErrorMsgAndExit("Instance not found: " + ex.getLocalizedMessage());
            } catch (Exception ex) {
                printErrorMsgAndExit(ex.getLocalizedMessage());
            }
        } else if("-d".equalsIgnoreCase(args[0])) {
            validateArgs(args, 3, new int[] {});

            // [findByDate] RaceServiceClient -d <date> <city>
            try {
                List<ClientRaceDto> races = clientRunFicService.findByDate(LocalDate.parse(args[1]), args[2]);
                System.out.println("Found " + races.size() +
                        " race(s) on date '" + args[1] + "'");
                for (int i = 0; i < races.size(); i++) {
                    ClientRaceDto raceDto = races.get(i);
                    printRace(raceDto);
                }
            } catch (InputValidationException ex){
                printErrorMsgAndExit("Invalid arguments: " + ex.getLocalizedMessage());
            } catch (Exception ex) {
                printErrorMsgAndExit(ex.getLocalizedMessage());
            }

        } else if("-i".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[] {1});

            // [inscribe] RaceServiceClient -i <raceID> <email> <creditCardNumber>

            ClientInscriptionDto inscription;
            try {
                inscription = clientRunFicService.inscribe(Long.parseLong(args[1]),
                        args[2], args[3]);

                System.out.println("Inscribed successfully in race " + args[1] +
                        " with inscription ID " +
                        inscription.getInscriptionID());

            } catch (NumberFormatException ex) {
                printErrorMsgAndExit("Invalid input (not a number): " + ex.getLocalizedMessage());
            } catch (InstanceNotFoundException ex) {
                printErrorMsgAndExit("Instance not found: " + ex.getLocalizedMessage());
            } catch (InputValidationException ex) {
                printErrorMsgAndExit("Invalid arguments: " + ex.getLocalizedMessage());
            } catch (Exception ex) {
                printErrorMsgAndExit(ex.getLocalizedMessage());
            }

        } else if("-u".equalsIgnoreCase(args[0])){
            validateArgs(args, 2, new int[] {});

            // [findAllFromUser] RaceServiceClient -u <email>

            List<ClientInscriptionDto> inscriptions = new ArrayList<>();
            try{
                inscriptions = clientRunFicService.findAllFromUser(args[1]);

                System.out.println("Found " + inscriptions.size() + " inscriptions from user " + args[1]);
                for (ClientInscriptionDto ins : inscriptions){
                    printInscription(ins);
                    System.out.println();
                }

            } catch (InputValidationException ex) {
                printErrorMsgAndExit("Invalid arguments: " + ex.getLocalizedMessage());
            } catch (Exception ex){
                printErrorMsgAndExit(ex.getLocalizedMessage());
            }

        } else if("-g".equalsIgnoreCase(args[0])){
            validateArgs(args, 3, new int[] {1});

            // [getRunnerNumber] RaceServiceClient -g <inscriptionID> <creditCardNumber>

            int runnerNumber;

            try{
                runnerNumber = clientRunFicService.getRunnerNumber(Long.parseLong(args[1]), args[2]);

                System.out.println("Dorsal número " + runnerNumber + " entregado con éxito");
                
            } catch (InputValidationException ex) {
                printErrorMsgAndExit("Invalid arguments: " + ex.getLocalizedMessage());
            } catch (InstanceNotFoundException ex) {
                printErrorMsgAndExit("Instance not found: " + ex.getLocalizedMessage());
            } catch (Exception ex){
                printErrorMsgAndExit(ex.getLocalizedMessage());
            }

        } else{
            printUsageAndExit();
        }
    }

    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for (int position : numericArguments) {
            try {
                Double.parseDouble(args[position]);
            } catch (NumberFormatException n) {
                printErrorMsgAndExit("Invalid input (not a number): " + n.getLocalizedMessage());
            }
        }
    }

    public static void printRace(ClientRaceDto raceDto){
        System.out.println("Id: " + raceDto.getRaceID() +
                "\nCity: " + raceDto.getCity() +
                "\nDescription: " + raceDto.getDescription() +
                "\nStartDateTime: " + raceDto.getStartDateTime() +
                "\nPrice: " + raceDto.getPrice() +
                "\nParticipants: " + raceDto.getParticipants() +
                "\nMaxParticipants: " + raceDto.getMaxParticipants());
    }

    public static void printInscription(ClientInscriptionDto inscriptionDto){
        System.out.println("Id: " + inscriptionDto.getInscriptionID() +
                "\nUser: " + inscriptionDto.getUser() +
                "\nCredit Card Number: " + inscriptionDto.getCreditCardNumber() +
                "\nRace ID: " + inscriptionDto.getRaceID() +
                "\nRunner Number: " + inscriptionDto.getRunnerNumber() +
                "\nIs number Taken?: " + inscriptionDto.isNumberTaken());
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printErrorMsgAndExit(String err){
        System.err.println("Error: " + err);
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [addRace]          RaceServiceClient -a <city> <description> <startDateTime> <price> <maxParticipants>\n" +
                "    [findRace]         RaceServiceClient -f <raceID>\n" +
                "    [findByDate]       RaceServiceClient -d <date> <city>\n" +
                "    [inscribe]         RaceServiceClient -i <raceID> <email> <creditCardNumber>\n" +
                "    [findAllFromUser]  RaceServiceClient -u <email>\n" +
                "    [getRunnerNumber]  RaceServiceClient -g <inscriptionID> <creditCardNumber>\n");
    }
}
