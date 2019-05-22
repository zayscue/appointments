package edu.wgu.c195.appointments.ui.customers;

import edu.wgu.c195.appointments.application.CustomersViewModel;
import edu.wgu.c195.appointments.domain.entities.Address;
import edu.wgu.c195.appointments.domain.entities.City;
import edu.wgu.c195.appointments.domain.entities.Country;
import edu.wgu.c195.appointments.domain.entities.Customer;
import edu.wgu.c195.appointments.persistence.repositories.AddressRepository;
import edu.wgu.c195.appointments.persistence.repositories.CityRepository;
import edu.wgu.c195.appointments.persistence.repositories.CountryRepository;
import edu.wgu.c195.appointments.persistence.repositories.CustomerRepository;
import edu.wgu.c195.appointments.ui.AppointmentsUI;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CustomersController implements Initializable {

    private final CustomerRepository customers;
    private final AddressRepository addresses;
    private final CityRepository cities;
    private final CountryRepository countries;
    private final ObservableList<Customer> customersObservableList;
    private final City OtherCity;
    private final ObservableList<City> citiesObservableList;
    private final Country OtherCountry;
    private final ObservableList<Country> countriesObservableList;


    @FXML
    private TextField customerAddressStreetAddress1TextField;
    @FXML
    private TextField customerAddressStreetAddress2TextField;
    @FXML
    private ComboBox<City> customerAddressCityComboBox;
    @FXML
    private TextField customerAddressOtherCityTextField;
    @FXML
    private ComboBox<Country> customerAddressCountryComboBox;
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
    private Button backBtn;
    @FXML
    private TableView<Customer> customersTable;
    private ResourceBundle resources;
    private CustomersViewModel viewModel;

    public CustomersController() {
        this.customers = new CustomerRepository();
        this.addresses = new AddressRepository();
        this.cities = new CityRepository();
        this.countries = new CountryRepository();
        this.viewModel = new CustomersViewModel();

        this.OtherCity = new City(-1, "Other", -1);
        this.citiesObservableList = this.cities.getAll()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        this.citiesObservableList.add(this.OtherCity);

        this.OtherCountry = new Country(-1, "Other");
        this.countriesObservableList = this.countries.getAll()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        this.countriesObservableList.add(this.OtherCountry);

        this.customersObservableList = this.customers.getAll()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set Local Resources
        this.resources = resources;

        // Initialize Back Button Text
        this.backBtn.setText("<- Back");

        // Set Bidirectional Bindings
        Bindings.bindBidirectional(this.customerNameTextField.textProperty(), this.viewModel.customerNameProperty());
        Bindings.bindBidirectional(this.customerAddressPhoneNumber.textProperty(), this.viewModel.phoneProperty());
        Bindings.bindBidirectional(this.customerAddressStreetAddress1TextField.textProperty(), this.viewModel.streetAddressProperty());
        Bindings.bindBidirectional(this.customerAddressStreetAddress2TextField.textProperty(), this.viewModel.streetAddress2Property());
        Bindings.bindBidirectional(this.customerAddressCityComboBox.valueProperty(), this.viewModel.cityProperty());
        Bindings.bindBidirectional(this.customerAddressOtherCityTextField.textProperty(), this.viewModel.otherCityProperty());
        Bindings.bindBidirectional(this.customerAddressCountryComboBox.valueProperty(), this.viewModel.countryProperty());
        Bindings.bindBidirectional(this.customerAddressOtherCountryTextField.textProperty(), this.viewModel.otherCountryProperty());
        Bindings.bindBidirectional(this.customerAddressPostalCode.textProperty(), this.viewModel.postalCodeProperty());
        Bindings.bindBidirectional(this.customerActiveCheckBox.selectedProperty(), this.viewModel.activeProperty());

        // Set CustomerTable Items
        TableColumn idColumn = new TableColumn("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerId"));
        TableColumn customerNameColumn = new TableColumn("Customer Name");
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        TableColumn activeColumn = new TableColumn("Active");
        activeColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("active"));
        this.customersTable.getColumns().addAll(idColumn, customerNameColumn, activeColumn);
        this.customersTable.getSelectionModel().selectedItemProperty().addListener(this::customersTableSelectionChanged);
        this.customersTable.setItems(this.customersObservableList);

        // Initialize City drop down
        this.customerAddressCityComboBox.setItems(this.citiesObservableList);

        // Initialize country drop down
        this.customerAddressCountryComboBox.setItems(this.countriesObservableList);
    }

    @FXML
    private void onChangeCityComboBox(ActionEvent event) {
        City selectedCity = this.customerAddressCityComboBox.getValue();
        if (selectedCity != null && selectedCity.equals(this.OtherCity)) {
            this.customerAddressOtherCityTextField.setDisable(false);
            this.customerAddressOtherCityTextField.setVisible(true);
        } else {
            this.customerAddressOtherCityTextField.setText("");
            this.customerAddressOtherCityTextField.setVisible(false);
            this.customerAddressOtherCityTextField.setDisable(true);
        }
    }

    @FXML
    private void onChangeCountryComboBox(ActionEvent event) {
        Country selectedCountry = this.customerAddressCountryComboBox.getValue();
        if (selectedCountry == null) {
            this.customerAddressCityComboBox.setDisable(true);
        } else if (selectedCountry.equals(this.OtherCountry)) {
            this.customerAddressOtherCountryTextField.setDisable(false);
            this.customerAddressOtherCountryTextField.setVisible(true);
            this.customerAddressCityComboBox.setItems(this.citiesObservableList
                    .filtered(city -> city.equals(this.OtherCity)));
            this.customerAddressCityComboBox.getSelectionModel().select(this.OtherCity);
            this.customerAddressCityComboBox.setDisable(true);
            this.customerAddressOtherCityTextField.setDisable(false);
        } else {
            this.customerAddressOtherCountryTextField.setText("");
            this.customerAddressOtherCountryTextField.setVisible(false);
            this.customerAddressOtherCountryTextField.setDisable(true);
            if(this.viewModel.getCity() == null || selectedCountry.getCountryId() != this.viewModel.getCity().getCountryId()) {
                this.customerAddressCityComboBox.setItems(this.citiesObservableList
                        .filtered(city -> city.getCountryId() == selectedCountry.getCountryId()
                                || city.equals(this.OtherCity)));
                this.viewModel.setCity(null);
                this.customerAddressCityComboBox.getSelectionModel().clearSelection();
            }
            this.customerAddressCityComboBox.setDisable(false);
        }
    }

    @FXML
    private void onBackBtnClick(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) this.backBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../calendar/MainView.fxml"), this.resources);
        primaryStage.setScene(new Scene(root,primaryStage.getWidth(), primaryStage.getHeight()));
    }

    @FXML
    private void onCreateBtnClick(ActionEvent event) {
        this.viewModel.reset();
    }

    @FXML
    private void onDeleteBtnClick(ActionEvent event) throws SQLException {
        Address address = this.viewModel.getAddress();
        if (address != null && address.getAddressId() > 0) {
            this.addresses.delete(address.getAddressId());
            this.addresses.save();
        }

        Customer customer = this.viewModel.getCustomer();
        if (customer != null && customer.getCustomerId() > 0) {
            this.customers.delete(customer.getCustomerId());
            this.customers.save();
            this.customersTable.getItems().remove(customer);
        }
    }

    @FXML
    private void saveCustomerBtnClicked(ActionEvent actionEvent) throws SQLException {
        Date currentDate = java.sql.Date.valueOf(LocalDate.now());
        Timestamp currentTimestamp = java.sql.Timestamp.valueOf(LocalDateTime.now());
        String currentUser = AppointmentsUI.CurrentUser.getUserName();

        Country country = this.viewModel.getCountry();
        if (country != null && country.getCountryId() <= 0) {
            country.setLastUpdate(currentTimestamp);
            country.setLastUpdateBy(currentUser);
            country.setCreateDate(currentDate);
            country.setCreatedBy(currentUser);
            this.countries.add(country);
            this.countries.save();
            this.countriesObservableList.add(country);
        }

        City city = this.viewModel.getCity();
        if (city != null && city.getCityId() <= 0) {
            city.setLastUpdate(currentTimestamp);
            city.setLastUpdateBy(currentUser);
            city.setCountryId(country.getCountryId());
            city.setCreateDate(currentDate);
            city.setCreatedBy(currentUser);
            this.cities.add(city);
            this.cities.save();
            this.citiesObservableList.add(city);
            this.customerAddressCountryComboBox.getSelectionModel().select(country);
            this.customerAddressCityComboBox.getSelectionModel().select(city);
        }

        Address address = this.viewModel.getAddress();
        if (address != null) {
            address.setLastUpdate(currentTimestamp);
            address.setLastUpdateBy(currentUser);
            address.setCityId(city.getCityId());
            if (address.getAddressId() > 0) {
                this.addresses.update(address);
                this.addresses.save();
            } else {
                address.setCreateDate(currentDate);
                address.setCreatedBy(currentUser);
                this.addresses.add(address);
                this.addresses.save();
            }
        }

        Customer customer = this.viewModel.getCustomer();
        if (customer != null) {
            customer.setLastUpdate(currentTimestamp);
            customer.setLastUpdateBy(currentUser);
            if (customer.getCustomerId() > 0) {
                this.customers.update(customer);
                this.customers.save();
            } else {
                customer.setCreateDate(currentDate);
                customer.setCreatedBy(currentUser);
                customer.setAddressId(address.getAddressId());
                this.customers.add(customer);
                this.customers.save();
                this.customersObservableList.add(customer);
            }
            this.customersTable.refresh();
        }
    }

    private void customersTableSelectionChanged(ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue)  {
        try {
            if (newValue != null) {
                this.viewModel.setCustomer(newValue);
                if (newValue.getAddressId() > 0) {
                    Address address = this.addresses.get(newValue.getAddressId());
                    if (address != null) {
                        this.viewModel.setAddress(address);
                        if (address.getCityId() > 0) {
                            City city = this.cities.get(address.getCityId());
                            if (city != null) {
                                this.viewModel.setCity(city);
                                if (city.getCountryId() > 0) {
                                    Country country = this.countries.get(city.getCountryId());
                                    if (country != null) {
                                        this.viewModel.setCountry(country);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (SQLException except) { }
    }
}
