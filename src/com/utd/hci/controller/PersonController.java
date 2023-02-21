package com.utd.hci.controller;

import com.utd.hci.MainApp;
import com.utd.hci.model.Person;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class PersonController {

    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> nameColumn;
    @FXML
    private TableColumn<Person, String> phoneNumberColumn;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField middleInitialField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField addressLine1Field;
    @FXML
    private TextField addressLine2Field;
    @FXML
    private TextField stateField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField zipCodeField;
    @FXML
    private ComboBox<String> genderField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private ComboBox<String> proofOfPurchaseField;
    @FXML
    private DatePicker dateReceivedField;
    @FXML
    private Label message;

    private int backspaceCounter;
    private boolean isFormFillingStarted;
    private LocalDateTime formFillingStartedAt;

    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor, called before the initialize() method.
     */
    public PersonController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getFirstName().concat(" ").concat(cellData.getValue().getLastName()));
        phoneNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getPhoneNumber());

        // Listen for selection changes and show the person details when changed.
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));

        Platform.runLater(() -> firstNameField.requestFocus());
        addInputValidation();
        setDateDefaultValue();
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        personTable.setItems(mainApp.getPersonData());
    }
    
    /**
     * Fills all text fields to show details about the person.
     * @param person the person
     */
    private void showPersonDetails(Person person) {
        if (person != null) {
            // Fill the labels with info from the person object.
            firstNameField.setText(person.getFirstName().getValue());
            middleInitialField.setText(person.getMiddleInitial());
            lastNameField.setText(person.getLastName());
            zipCodeField.setText(String.valueOf(person.getZipcode()));
            cityField.setText(person.getCity());
            addressLine1Field.setText(person.getLine1());
            addressLine2Field.setText(person.getLine2());
            stateField.setText(person.getState());
            phoneNumberField.setText(person.getPhoneNumber().getValue());
            emailField.setText(person.getEmail());
            dateReceivedField.setValue(person.getDateReceived());

            if(!person.getGender().equals("null"))
                genderField.getSelectionModel().select(person.getGender());
            if(person.isProofOfPurchaseAttached()) {
                proofOfPurchaseField.getSelectionModel().select("Yes");
            } else {
                proofOfPurchaseField.getSelectionModel().select("No");
            }
            message.setText("");
        }
    }
    
    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeletePerson() throws IOException {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {

            String lineToRemove = personTable.getSelectionModel().getSelectedItem().toString();

            //Remove the data from person table and clear up the person overview screen
            personTable.getItems().remove(selectedIndex);
            handleNewPerson();

            //Remove record from the file
            removeDataFromFile(lineToRemove);

        } else {
            // Nothing selected.
            message.setText("Please select a person from table to delete.");
            message.setTextFill(Paint.valueOf("#f71505"));
        }
    }

    private void removeDataFromFile(String lineToRemove) throws IOException {
        File inputFile = new File("CS6326Asg2.txt");
        File tempFile = new File("myTempFile.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            if(trimmedLine.startsWith(lineToRemove)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close();
        reader.close();
        tempFile.renameTo(inputFile);
    }

    /**
     * Called when the user clicks the clear button.
     */
    @FXML
    private void handleNewPerson() {

        mainApp.showPersonOverview(null);
    }

    /**
     * Called when the user clicks the save button. Saves the
     * details for the new person in a file.
     */
    @FXML
    private void savePerson() throws IOException {
        if(isFormDataValidToSave()){
            Person tempPerson = createPerson();
            String key = tempPerson.getFirstName()+"-"+tempPerson.getLastName()+"-"+tempPerson.getPhoneNumber();
            if(mainApp.setOfRecords.contains(key)){
                message.setText("Duplicate record: \nPlease change first name, last name or phone number");
                message.setTextFill(Paint.valueOf("#f71505"));
                return;
            }
            if(personTable.getSelectionModel().getSelectedItem() != null){
                handleDeletePerson();
            }

            FileWriter myWriter = new FileWriter("CS6326Asg2.txt", true);
            myWriter.write(tempPerson.toString());
            myWriter.append('\n');
            myWriter.close();
            handleNewPerson();
            mainApp.setOfRecords.add(key);
            mainApp.getPersonData().add(tempPerson);

        }
    }

    //Start of helper functions to validate input form data

    /**
     * Called when the user clicks the save button. It maps the form
     * data into person object to persist.
     */
    private Person createPerson(){
        Person person = new Person();
        person.setFirstName(new SimpleStringProperty(firstNameField.getText().trim()));
        person.setMiddleInitial(middleInitialField.getText().trim());
        person.setLastName((lastNameField.getText().trim()));
        person.setDateReceived(dateReceivedField.getValue());
        person.setCity(cityField.getText());
        person.setLine1(addressLine1Field.getText());
        person.setLine2(addressLine2Field.getText());
        person.setState(stateField.getText());
        person.setZipcode(zipCodeField.getText());
        person.setPhoneNumber(new SimpleStringProperty(phoneNumberField.getText()));
        person.setEmail(emailField.getText());
        person.setGender(genderField.getValue());

        if(proofOfPurchaseField.getValue().equals("Yes"))
            person.setProofOfPurchaseAttached(Boolean.TRUE);
        else
            person.setProofOfPurchaseAttached(Boolean.FALSE);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String key = person.getFirstName()+"-"+person.getLastName()+"-"+person.getPhoneNumber();
        if(!mainApp.setOfRecords.contains(key)){
            person.setFormSavedAt(LocalDateTime.now().format(formatter));
            person.setBackspaceCounter(backspaceCounter);
            person.setFormStartedAt(formFillingStartedAt.format(formatter));
        }
        return person;
    }

    /**
     * Adding validation to textfield to check the empty length of the input
     */
    private void addEmptyValueCheck(TextField tf){
        if(tf.getText().length()==0){
            tf.setStyle("-fx-control-inner-background: #"+ Paint.valueOf("#f7c6c9").toString().substring(2));
        } else {
            tf.setStyle(null);
        }
    }

    /**
     * Adding Listener to textfield to check the max length of the input and trimming the input accordingly
     */
    private void addOptionalTextLimiterListener(TextField tf, int maxLength){
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if(!isFormFillingStarted){
                isFormFillingStarted = true;
                formFillingStartedAt = LocalDateTime.now();
            }
            if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            } else {
                tf.setStyle(null);
            }
        });
    }

    private void emailValidation(){
        if(!Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$")
                .matcher(emailField.getText())
                .matches()){
            emailField.setStyle("-fx-control-inner-background: #"+ Paint.valueOf("#f7c6c9").toString().substring(2));
        } else {
            emailField.setStyle(null);
        }
    }

    private void usPhoneNumberValidation(){
        if(!Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4}$")
                .matcher(phoneNumberField.getText())
                .matches()){
            phoneNumberField.setStyle("-fx-control-inner-background: #"+ Paint.valueOf("#f7c6c9").toString().substring(2));
        } else {
            phoneNumberField.setStyle(null);
        }
    }

    private void zipCodeValidation(){
        if(!Pattern.compile("^[0-9]{5}-[0-9]{4}$")
                .matcher(zipCodeField.getText())
                .matches()){
            zipCodeField.setStyle("-fx-control-inner-background: #"+ Paint.valueOf("#f7c6c9").toString().substring(2));
        } else {
            zipCodeField.setStyle(null);
        }
    }

    private void textFieldAlphanumericValidation(TextField tf){
        if(!Pattern.compile("^[A-Za-z]*$")
                .matcher(tf.getText())
                .matches()){
            tf.setStyle("-fx-control-inner-background: #"+ Paint.valueOf("#f7c6c9").toString().substring(2));
        }
    }

    private void backspaceKeyEventListener(TextField tf){
        tf.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE) {
                backspaceCounter++;
            }
        });
    }

    /**
     * Attaches listener to the textfields
     */
    private void addInputValidation(){
        addOptionalTextLimiterListener(firstNameField, 25);
        addOptionalTextLimiterListener(middleInitialField, 1);
        addOptionalTextLimiterListener(lastNameField, 25);
        addOptionalTextLimiterListener(addressLine1Field, 35);
        addOptionalTextLimiterListener(addressLine2Field, 35);
        addOptionalTextLimiterListener(cityField, 25);
        addOptionalTextLimiterListener(stateField, 2);
        addOptionalTextLimiterListener(zipCodeField, 10);
        addOptionalTextLimiterListener(phoneNumberField, 12);
        addOptionalTextLimiterListener(emailField, 60);
        proofOfPurchaseField.setOnAction( e-> proofOfPurchaseField.setStyle(null));

        backspaceKeyEventListener(firstNameField);
        backspaceKeyEventListener(middleInitialField);
        backspaceKeyEventListener(lastNameField);
        backspaceKeyEventListener(addressLine1Field);
        backspaceKeyEventListener(addressLine2Field);
        backspaceKeyEventListener(cityField);
        backspaceKeyEventListener(stateField);
        backspaceKeyEventListener(zipCodeField);
        backspaceKeyEventListener(phoneNumberField);
        backspaceKeyEventListener(emailField);

    }

    private void setDateDefaultValue() {
        dateReceivedField.setValue(LocalDate.now());
        dateReceivedField.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0 );
            }
        });
    }

    /**
     * Called just before the save button gets hit to validate final input
     */
    private boolean isFormDataValidToSave(){

        addEmptyValueCheck(firstNameField);
        addEmptyValueCheck(lastNameField);
        addEmptyValueCheck(addressLine1Field);
        addEmptyValueCheck(cityField);
        addEmptyValueCheck(stateField);
        textFieldAlphanumericValidation(firstNameField);
        textFieldAlphanumericValidation(middleInitialField);
        textFieldAlphanumericValidation(lastNameField);
        textFieldAlphanumericValidation(cityField);
        textFieldAlphanumericValidation(stateField);
        emailValidation();
        usPhoneNumberValidation();
        zipCodeValidation();

        if(firstNameField.getStyle().length() != 0 || lastNameField.getStyle().length()!=0 || addressLine1Field.getStyle().length() != 0
                || cityField.getStyle().length() != 0 || stateField.getStyle().length() != 0 || zipCodeField.getStyle().length() != 0
                || phoneNumberField.getStyle().length() != 0 || emailField.getStyle().length() != 0)
            return false;

        if(proofOfPurchaseField.getValue()==null){
            proofOfPurchaseField.setStyle("-fx-outer-border: #"+ Paint.valueOf("#f71505").toString().substring(2));
            return false;
        }


        return true;
    }

}