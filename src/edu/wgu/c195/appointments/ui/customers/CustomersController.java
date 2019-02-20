package edu.wgu.c195.appointments.ui.customers;

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

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CustomersController implements Initializable {

    public TextField customerAddressStreetAddress1TextField;
    public TextField customerAddressStreetAddress2TextField;
    public ComboBox customerAddressCityComboBox;
    public Label customerAddressOtherCityLabel;
    public TextField customerAddressOtherCityTextField;
    public Label customerAddressCountryLabel;
    public ComboBox customerAddressCountryComboBox;
    public Label customerAddressOtherCountryLabel;
    public TextField customerAddressOtherCountryTextField;
    public TextField customerAddressPhoneNumber;
    public TextField customerAddressPostalCode;
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


    public CustomersController() {
        this.customers = new CustomerRepository();
        this.addresses = new AddressRepository();
        this.cities = new CityRepository();
        this.countries = new CountryRepository();

        data = this.customers.getAll()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.customersTable.setItems(data);
    }

    public void saveCustomerBtnClicked(ActionEvent actionEvent) throws SQLException {
        long timeMillis = System.currentTimeMillis();
        Customer customer = new Customer();
        customer.setAddressId(1);
        customer.setCustomerName(this.customerNameTextField.getText());
        customer.setActive(this.customerActiveCheckBox.isSelected());
        customer.setCreatedBy(AppointmentsUI.CurrentUser.getUserName());
        customer.setCreateDate(new java.sql.Date(timeMillis));
        customer.setLastUpdateBy(AppointmentsUI.CurrentUser.getUserName());
        customer.setLastUpdate(new Timestamp(timeMillis));
        this.customers.add(customer);
        this.customers.save();
    }
}
