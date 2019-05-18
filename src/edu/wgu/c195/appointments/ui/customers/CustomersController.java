package edu.wgu.c195.appointments.ui.customers;

import edu.wgu.c195.appointments.domain.entities.City;
import edu.wgu.c195.appointments.domain.entities.Country;
import edu.wgu.c195.appointments.domain.entities.Customer;
import edu.wgu.c195.appointments.persistence.repositories.AddressRepository;
import edu.wgu.c195.appointments.persistence.repositories.CityRepository;
import edu.wgu.c195.appointments.persistence.repositories.CountryRepository;
import edu.wgu.c195.appointments.persistence.repositories.CustomerRepository;
import edu.wgu.c195.appointments.ui.AppointmentsUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CustomersController implements Initializable {

    @FXML
    private TextField customerAddressStreetAddress1TextField;
    @FXML
    private TextField customerAddressStreetAddress2TextField;
    @FXML
    private ComboBox customerAddressCityComboBox;
    @FXML
    private TextField customerAddressOtherCityTextField;
    @FXML
    private ComboBox customerAddressCountryComboBox;
    @FXML
    private TextField customerAddressOtherCountryTextField;
    @FXML
    private TextField customerAddressPhoneNumber;
    @FXML
    private TextField customerAddressPostalCode;
    @FXML
    private TextField customerNameTextField;
    @FXML
    private CheckBox customerActiveCheckBox;
    @FXML
    private Button saveCustomerBtn;
    @FXML
    private TableView customersTable;

    private ResourceBundle resources;

    private final CustomerRepository customers;
    private final AddressRepository addresses;
    private final CityRepository cities;
    private final CountryRepository countries;

    private final ObservableList<Customer> data;

    private final City OtherCity;
    private final ObservableList<City> citiesObservableList;

    private final Country OtherCountry;
    private final ObservableList<Country> countriesObservableList;


    public CustomersController() {
        this.customers = new CustomerRepository();
        this.addresses = new AddressRepository();
        this.cities = new CityRepository();
        this.countries = new CountryRepository();

        this.OtherCity = new City(-1, "Other", -1);
        this.citiesObservableList = this.cities.getAll()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        this.citiesObservableList.add(this.OtherCity);

        this.OtherCountry = new Country(-1, "Other");
        this.countriesObservableList = this.countries.getAll()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        this.countriesObservableList.add(this.OtherCountry);

        data = this.customers.getAll()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set Local Resources
        this.resources = resources;

        // Set CustomerTable Items
        this.customersTable.setItems(data);

        // Initialize City drop down
        this.customerAddressCityComboBox.setItems(this.citiesObservableList);

        // Initialize country drop down
        this.customerAddressCountryComboBox.setItems(this.countriesObservableList);
    }

    public void onChangeCityComboBox(ActionEvent event) {
        City currentSelectedCity = (City) this.customerAddressCityComboBox.getValue();
        if (currentSelectedCity.equals(this.OtherCity)) {
            this.customerAddressOtherCityTextField.setVisible(true);
        } else {
            this.customerAddressOtherCityTextField.setVisible(false);
        }
    }

    public void onChangeCountryComboBox(ActionEvent event) {
        Country currentSelectedCountry = (Country) this.customerAddressCountryComboBox.getValue();
        if (currentSelectedCountry.equals(this.OtherCountry)) {
            this.customerAddressOtherCountryTextField.setVisible(true);
        } else {
            this.customerAddressOtherCountryTextField.setVisible(false);
        }
    }

    public void saveCustomerBtnClicked(ActionEvent actionEvent) throws SQLException {
        System.out.println(this.customerNameTextField.getText());
        System.out.println(this.customerAddressStreetAddress1TextField.getText());
        System.out.println(this.customerAddressStreetAddress2TextField.getText());
        System.out.println(this.customerAddressCityComboBox.getValue());
        System.out.println(this.customerAddressPostalCode.getText());
        System.out.println(this.customerAddressPhoneNumber.getText());
        System.out.println(this.customerActiveCheckBox.isSelected());
//        long timeMillis = System.currentTimeMillis();
//        Customer customer = new Customer();
//        customer.setAddressId(1);
//        customer.setCustomerName(this.customerNameTextField.getText());
//        customer.setActive(this.customerActiveCheckBox.isSelected());
//        customer.setCreatedBy(AppointmentsUI.CurrentUser.getUserName());
//        customer.setCreateDate(new java.sql.Date(timeMillis));
//        customer.setLastUpdateBy(AppointmentsUI.CurrentUser.getUserName());
//        customer.setLastUpdate(new Timestamp(timeMillis));
//        this.customers.add(customer);
//        this.customers.save();
    }
}
