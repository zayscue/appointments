package edu.wgu.c195.appointments.ui;

import edu.wgu.c195.appointments.application.UserManager;
import edu.wgu.c195.appointments.domain.entities.IncrementType;
import edu.wgu.c195.appointments.domain.entities.User;
import edu.wgu.c195.appointments.persistence.ConnectionFactory;
import edu.wgu.c195.appointments.persistence.repositories.IRepository;
import edu.wgu.c195.appointments.persistence.repositories.IncrementTypeRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Locale deLocale = new Locale("de");
        ResourceBundle labels = ResourceBundle.getBundle("LabelsBundle", deLocale);
        String s2 = labels.getString("s2");
        System.out.println(s2);
        Connection connection = ConnectionFactory.getConnection();
        IncrementTypeRepository repository = new IncrementTypeRepository();
        UserManager userManager = new UserManager();
        try {
            User user = userManager.findByUserName("zayscue");
            User sameUser = userManager.findById(user.getUserId());
            List<IncrementType> incrementTypes = repository.getAll()
                    .filter(x -> x.getIncrementTypeId() > 1)
                    .collect(Collectors.toList());
            IncrementType incrementType = new IncrementType();
            IncrementType incrementType2 = new IncrementType();
            incrementType.setIncrementTypeDescription("Test Type 1");
            incrementType2.setIncrementTypeDescription("Test Type 2");
            repository.add(incrementType);
            repository.add(incrementType2);
            repository.save();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
