package edu.wgu.c195.appointments.ui.customers;

import edu.wgu.c195.appointments.domain.entities.Address;
import edu.wgu.c195.appointments.domain.entities.City;
import edu.wgu.c195.appointments.domain.entities.Country;
import edu.wgu.c195.appointments.domain.entities.Customer;
import javafx.beans.property.*;

public class CustomersViewModel {

    private Customer customer;
    private Address address;
    private City city;
    private Country country;

    private StringProperty customerName = new SimpleStringProperty();
    private StringProperty streetAddress = new SimpleStringProperty();
    private StringProperty streetAddress2 = new SimpleStringProperty();
    private StringProperty phone = new SimpleStringProperty();
    private StringProperty postalCode = new SimpleStringProperty();
    private ObjectProperty<City> citySimpleObjectProperty = new SimpleObjectProperty<>();
    private StringProperty otherCity = new SimpleStringProperty();
    private ObjectProperty<Country> countrySimpleObjectProperty = new SimpleObjectProperty<>();
    private StringProperty otherCountry = new SimpleStringProperty();
    private BooleanProperty active = new SimpleBooleanProperty();

    public CustomersViewModel() {
        this.customer = new Customer();
        this.address = new Address();
        this.city = new City();
        this.country = new Country();
        this.initializeProperties();
    }

    private void initializeProperties() {
        this.customerName.addListener((observable, oldValue, newValue) -> {
            this.customer.setCustomerName(newValue);
        });
        this.streetAddress.addListener((observable, oldValue, newValue) -> {
            this.address.setAddress(newValue);
        });
        this.streetAddress2.addListener((observable, oldValue, newValue) -> {
            this.address.setAddress2(newValue);
        });
        this.phone.addListener((observable, oldValue, newValue) -> {
            this.address.setPhone(newValue);
        });
        this.postalCode.addListener((observable, oldValue, newValue) -> {
            this.address.setPostalCode(newValue);
        });
        this.citySimpleObjectProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                this.address.setCityId(0);
            }
            this.city = newValue;
        });
        this.otherCity.addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.equals("")) {
                City newCity = new City();
                newCity.setCity(newValue);
                if(this.country != null) {
                    newCity.setCountryId(this.country.getCountryId());
                }
                this.address.setCityId(newCity.getCityId());
                this.city = newCity;
            }
        });
        this.countrySimpleObjectProperty.addListener((observable, oldValue, newValue) -> {
            this.country = newValue;
        });
        this.otherCountry.addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.equals("")) {
                Country newCountry = new Country();
                newCountry.setCountry(newValue);
                this.city.setCountryId(newCountry.getCountryId());
                this.country = newCountry;
            }
        });
        this.active.addListener((observable, oldValue, newValue) -> {
            this.customer.setActive(newValue);
        });
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.customerName.set(customer.getCustomerName());
        this.active.set(customer.isActive());
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
        this.customer.setAddressId(address.getAddressId());
        this.phone.set(address.getPhone());
        this.streetAddress.set(address.getAddress());
        this.streetAddress2.set(address.getAddress2());
        this.postalCode.set(address.getPostalCode());
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
        if (city == null) {
            this.address.setCityId(0);
        } else {
            this.address.setCityId(city.getCityId());
        }
        this.citySimpleObjectProperty.set(city);
    }

    public String getOtherCity() {
        return this.otherCity.get();
    }

    public void setOtherCity(String otherCity) {
        this.otherCity.set(otherCity);
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
        this.countrySimpleObjectProperty.set(country);
    }

    public String getOtherCountry() {
        return this.otherCountry.get();
    }

    public void setOtherCountry(String otherCountry) {
        this.otherCountry.set(otherCountry);
    }

    public StringProperty customerNameProperty() {
        return this.customerName;
    }

    public StringProperty streetAddressProperty() {
        return this.streetAddress;
    }

    public StringProperty streetAddress2Property() {
        return this.streetAddress2;
    }

    public StringProperty phoneProperty() {
        return this.phone;
    }

    public StringProperty postalCodeProperty() {
        return this.postalCode;
    }

    public ObjectProperty<City> cityProperty() {
        return this.citySimpleObjectProperty;
    }

    public StringProperty otherCityProperty() {
        return this.otherCity;
    }

    public ObjectProperty<Country> countryProperty() {
        return this.countrySimpleObjectProperty;
    }

    public StringProperty otherCountryProperty() {
        return this.otherCountry;
    }

    public BooleanProperty activeProperty() {
        return this.active;
    }

    public void reset() {
        this.customer = new Customer();
        this.address = new Address();
        this.city = new City();
        this.country = new Country();
        this.customerName.set(null);
        this.streetAddress.set(null);
        this.streetAddress2.set(null);
        this.phone.set(null);
        this.postalCode.set(null);
        this.citySimpleObjectProperty.set(null);
        this.otherCity.set(null);
        this.countrySimpleObjectProperty.set(null);
        this.otherCountry.set(null);
        this.active.set(false);
    }
}
