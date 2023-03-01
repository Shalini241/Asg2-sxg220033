package com.utd.hci;

import com.utd.hci.model.Person;
import com.utd.hci.controller.PersonController;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * The set of person records to check for duplicates.
     */
    public HashSet<String> setOfRecords;

    /**
     * The data as an observable list of Persons.
     */
    private final ObservableList<Person> personData = FXCollections.observableArrayList();

    /**
     * Constructor; It initializes the service and loads existing
     * data from file to memory.
     */
    public MainApp() {

        setOfRecords = new HashSet<>();
        try {
            File file = new File("CS6326Asg2.txt");
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){
                String objectString = scan.nextLine();
                Person person = mapStringToPerson(objectString);
                String key = person.getFirstName().toString().toLowerCase()+"-"+person.getLastName().toLowerCase()+"-"+person.getPhoneNumber();
                setOfRecords.add(key);
                personData.add(person);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File with name CS6326Asg2.txt does not exist");
            e.printStackTrace();
        }

    }


    /**
     * Returns the data as an observable list of Persons. 
     * @return
     */
    public ObservableList<Person> getPersonData() {
        return personData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Rebate Form");

        initRootLayout();

        showPersonOverview(null);
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPersonOverview(Person person) {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
            AnchorPane personOverview = loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            PersonController controller = loader.getController();
            controller.setMainApp(this);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Person mapStringToPerson(String objectString) {
        int index = 0;
        while(objectString.charAt(index++)!='{');
        objectString = objectString.substring(index, objectString.length()-1);
        HashMap<String, String> fieldMap = new HashMap<>();
        String [] fields = objectString.split("\t");
        for(String field : fields){
            String[] data = field.split("=");
            fieldMap.put(data[0].trim(), data[1].replace('\'',' ').trim());
        }
        Person person = new Person();
        person.setFirstName(new SimpleStringProperty(fieldMap.get("firstName")));
        person.setMiddleInitial(fieldMap.get("middleInitial"));
        person.setLastName(fieldMap.get("lastName"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateTime = LocalDate.parse(fieldMap.get("dateReceived"), formatter);
        person.setDateReceived(dateTime);

        person.setCity(fieldMap.get("city"));
        person.setLine1(fieldMap.get("line1"));
        person.setLine2(fieldMap.get("line2"));
        person.setState(fieldMap.get("state"));
        person.setZipcode(fieldMap.get("zipcode"));

        person.setPhoneNumber(new SimpleStringProperty(fieldMap.get("phoneNumber")));
        person.setEmail(fieldMap.get("email"));
        person.setGender(fieldMap.get("gender"));
        person.setFormStartedAt(fieldMap.get("formStartedAt"));
        person.setFormSavedAt(fieldMap.get("formSavedAt"));
        person.setBackspaceCounter(Integer.parseInt(fieldMap.get("backspaceCounter")));

        if(fieldMap.get("proofOfPurchaseAttached").equals("true"))
            person.setProofOfPurchaseAttached(Boolean.TRUE);
        else
            person.setProofOfPurchaseAttached(Boolean.FALSE);
        return person;
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}