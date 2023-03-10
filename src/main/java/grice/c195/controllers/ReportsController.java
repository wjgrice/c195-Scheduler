package grice.c195.controllers;

import grice.c195.DAO.AppointmentsDAO;
import grice.c195.DAO.ContactDAO;
import grice.c195.helper.ScreenChange;
import grice.c195.helper.TableUpdater;
import grice.c195.model.Appointment;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 * Controller for the Reports screen.
 */
public class ReportsController implements Initializable {
    @FXML private TableView<Appointment> filterTable;
    @FXML private TableColumn<Appointment, Integer> reportsAppointmentsIdColumn;
    @FXML private TableColumn<Appointment, String> reportAppointmentsTypeColumn;
    @FXML private TableColumn<Appointment, String> reportAppointmentsTitleColumn;
    @FXML private TableColumn<Appointment, String> reportAppointmentsDescriptionColumn;
    @FXML private TableColumn<Appointment, String> reportAppointmentsContactColumn;
    @FXML private TableColumn<Appointment, String> reportAppointmentsLocationColumn;
    @FXML private TableColumn<Appointment, String> reportAppointmentsStartColumn;
    @FXML private TableColumn<Appointment, String> reportAppointmentsEndColumn;
    @FXML private TableColumn<Appointment, Integer> reportAppointmentsCustIDColumn;
    @FXML private TableColumn<Appointment, Integer> reportAppointmentsUserColumn;
    @FXML private TableView<Appointment> scheduleTable;
    @FXML private TableColumn<Appointment, Integer> reportsScheduleIdColumn;
    @FXML private TableColumn<Appointment, String> reportsScheduleTypeColumn;
    @FXML private TableColumn<Appointment, String> reportsScheduleTitleColumn;
    @FXML private TableColumn<Appointment, String> reportsScheduleDescriptionColumn;
    @FXML private TableColumn<Appointment, String> reportsScheduleContactColumn;
    @FXML private TableColumn<Appointment, String> reportsScheduleLocationColumn;
    @FXML private TableColumn<Appointment, String> reportsScheduleStartColumn;
    @FXML private TableColumn<Appointment, String> reportsScheduleEndColumn;
    @FXML private TableColumn<Appointment, Integer> reportsScheduleCustIDColumn;
    @FXML private TableColumn<Appointment, Integer> reportsScheduleUserColumn;
    @FXML private TableView<Appointment> billingTable;
    @FXML private TableColumn<Appointment, Integer> reportsBillingIdColumn;
    @FXML private TableColumn<Appointment, String> reportsBillingTypeColumn;
    @FXML private TableColumn<Appointment, String> reportsBillingTitleColumn;
    @FXML private TableColumn<Appointment, String> reportsBillingDescriptionColumn;
    @FXML private TableColumn<Appointment, String> reportsBillingContactColumn;
    @FXML private TableColumn<Appointment, String> reportsBillingLocationColumn;
    @FXML private TableColumn<Appointment, String> reportsBillingStartColumn;
    @FXML private TableColumn<Appointment, String> reportsBillingEndColumn;
    @FXML private TableColumn<Appointment, Integer> reportsBillingCustIDColumn;
    @FXML private TableColumn<Appointment, Integer> reportsBillingUserColumn;
    @FXML private ComboBox<String> appTypeFilterCombo;
    @FXML private ComboBox<String> appMonthFilterCombo;
    @FXML private ComboBox<String> contactFilterCombo;
    @FXML private ComboBox<String> billingFilterCombo;
    @FXML private Label reportScheduleCountLabel;
    @FXML private Label reportsBillingHoursLabel;
    @FXML private Label reportsAppointmentsCountLabel;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Combobox of all months in year
        appMonthFilterCombo.getItems().addAll( "All", "January", "February", "March", "April", "May", "June", "July",
                                                   "August", "September", "October", "November", "December");
        // Set Default value to "All" for months
        appMonthFilterCombo.setValue("All");
        // Combobox of all appointment types
        appTypeFilterCombo.getItems().addAll(AppointmentsDAO.getAllAppointmentTypes());
        // Prepend "All" to the list of appointment types
        appTypeFilterCombo.getItems().add(0, "All");
        // Set Default value to "All" for appointment types
        appTypeFilterCombo.setValue("All");
        // Combobox of all contacts
        contactFilterCombo.getItems().addAll(ContactDAO.getAllContactNames());
        // Prepend "All" to the list of contacts
        contactFilterCombo.getItems().add(0, "All");
        // Set Default value to "All" for contacts
        contactFilterCombo.setValue("All");
        // Combobox of all billing types
        billingFilterCombo.getItems().addAll("All", "Week", "Month", "Quarter", "Year");
        // Set Default value to "All" for billing types
        billingFilterCombo.setValue("All");

        ObservableList<Appointment> appointments = AppointmentsDAO.getAppointments();
        // Update the appointments table with all appointments
        TableUpdater.appsTableUpdate(filterTable, reportsAppointmentsIdColumn, reportAppointmentsUserColumn,
                reportAppointmentsCustIDColumn, reportAppointmentsTypeColumn, reportAppointmentsTitleColumn,
                reportAppointmentsDescriptionColumn, reportAppointmentsLocationColumn, reportAppointmentsContactColumn,
                reportAppointmentsStartColumn, reportAppointmentsEndColumn, appointments);
        // Update the schedule table with all appointments
        TableUpdater.appsTableUpdate(scheduleTable, reportsScheduleIdColumn, reportsScheduleUserColumn,
                reportsScheduleCustIDColumn, reportsScheduleTypeColumn, reportsScheduleTitleColumn,
                reportsScheduleDescriptionColumn, reportsScheduleLocationColumn, reportsScheduleContactColumn,
                reportsScheduleStartColumn, reportsScheduleEndColumn, appointments);
        // Update the billing table with all appointments
        TableUpdater.appsTableUpdate(billingTable, reportsBillingIdColumn, reportsBillingUserColumn,
                reportsBillingCustIDColumn, reportsBillingTypeColumn, reportsBillingTitleColumn,
                reportsBillingDescriptionColumn, reportsBillingLocationColumn, reportsBillingContactColumn,
                reportsBillingStartColumn, reportsBillingEndColumn, appointments);

        //Display count of appointments in appointments table
        reportScheduleCountLabel.setText("Appointment Count: " + scheduleTable.getItems().size());
        //Display count of appointments in schedule table
        reportScheduleCountLabel.setText("Appointment Count: " + scheduleTable.getItems().size());
        //Display count of appointments.  If no appointments then display 0
        appFilter();
        //Display count of minutes of appointments in billing table
        billingFilter();


    }
    /**
     * Filters the appointments table by the selected month then selected type.  If any has been selected on
     * either comboBox then no filter is applied.
     */
    public void appFilter() {
        String selectedMonth = appMonthFilterCombo.getSelectionModel().getSelectedItem();
        String selectedType = appTypeFilterCombo.getSelectionModel().getSelectedItem();

        //Create cell factory for filterTable using returned list of appointments
        if (selectedMonth.equals("All") && selectedType.equals("All")) {
            filterTable.setItems(AppointmentsDAO.getAppointments());
        } else if (!selectedMonth.equals("All") && selectedType.equals("All")) {
            filterTable.setItems(AppointmentsDAO.getAppointmentsByMonth(selectedMonth));
        } else if (selectedMonth.equals("All")) {
            filterTable.setItems(AppointmentsDAO.getAppointmentsByType(selectedType));
        } else {
            filterTable.setItems(AppointmentsDAO.getAppointmentsByMonthAndType(selectedMonth, selectedType));
        }
        //Count of appointments in filter table
        reportsAppointmentsCountLabel.setText("Appointment Count: " + filterTable.getItems().size());
    }

    /**
     * Filters the schedule table by the selected contact.  If "All" is selected then no filter is applied.
     */
    public void contactFilter() {
        String selectedContact = contactFilterCombo.getSelectionModel().getSelectedItem();
        if (selectedContact.equals("All")) {
            scheduleTable.setItems(AppointmentsDAO.getAppointments());
        } else {
            scheduleTable.setItems(AppointmentsDAO.getAppointmentsByContact(selectedContact) );
        }
        //Count of appointments in schedule table
        reportScheduleCountLabel.setText("Appointment Count: " + scheduleTable.getItems().size());
    }

    /**
     * Filters the billing table by the selected billing time period.  If "All" is selected then no filter is applied.
     */
    public void billingFilter() {
        ObservableList<Appointment> appointments = AppointmentsDAO.getAppointments();
        String selectedFilter = billingFilterCombo.getSelectionModel().getSelectedItem();
        LocalDate now = LocalDate.now();
        Predicate<Appointment> filterPredicate = null;
        switch (selectedFilter) {
            case "All" -> filterPredicate = appointment -> true;
            case "Week" -> {
                // Filter List to current week
                LocalDate startOfWeek = now.with(DayOfWeek.SUNDAY);
                LocalDate endOfWeek = now.with(DayOfWeek.SATURDAY);
                filterPredicate = appointment -> {
                    LocalDateTime startTime = appointment.getStart();
                    LocalDate startDate = startTime.toLocalDate();
                    return startDate.isEqual(startOfWeek) || (startDate.isAfter(startOfWeek) && startDate.isBefore(endOfWeek));
                };
            }
            case "Month" -> {
                // Filter List to current month
                LocalDate startOfMonth = now.withDayOfMonth(1);
                LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());
                filterPredicate = appointment -> {
                    LocalDateTime startTime = appointment.getStart();
                    LocalDate startDate = startTime.toLocalDate();
                    return startDate.isEqual(startOfMonth) || (startDate.isAfter(startOfMonth) && startDate.isBefore(endOfMonth));
                };
            }
            case "Quarter" -> {
                // Filter List to current quarter
                Month currentMonth = now.getMonth();
                int currentQuarter = (currentMonth.getValue() / 3) + 1;
                LocalDate quarterStart = LocalDate.of(now.getYear(), ((currentQuarter - 1) * 3) + 1, 1);
                LocalDate quarterEnd = quarterStart.plusMonths(3).minusDays(1);
                filterPredicate = appointment -> {
                    LocalDateTime startTime = appointment.getStart();
                    LocalDate startDate = startTime.toLocalDate();
                    return startDate.isEqual(quarterStart) || (startDate.isAfter(quarterStart) && startDate.isBefore(quarterEnd));
                };
            }
            case "Year" -> {
                // Filter List to current year
                LocalDate startOfYear = LocalDate.of(now.getYear(), Month.JANUARY, 1);
                LocalDate endOfYear = LocalDate.of(now.getYear(), Month.DECEMBER, 31);
                filterPredicate = appointment -> {
                    LocalDateTime startTime = appointment.getStart();
                    LocalDate startDate = startTime.toLocalDate();
                    return startDate.isEqual(startOfYear) || (startDate.isAfter(startOfYear) && startDate.isBefore(endOfYear));
                };
            }
        }
        billingTable.setItems(appointments.filtered(filterPredicate));
        //Count of appointments in billing table
        reportsBillingHoursLabel.setText("Appointment Count: " + billingTable.getItems().size());
        //Calculate the total hours of appointments in billing table
        reportsBillingHoursLabel.setText("Total Minutes: " + AppointmentsDAO.getTotalMinutes(billingTable.getItems()));
    }
    /**
     * Returns to the main menu.
     * @param actionEvent The event that triggered the method.
     * @throws IOException If the fxml file cannot be found.
     */
    public void mainMenuClick(ActionEvent actionEvent) throws IOException {
        ScreenChange.newView("/grice/c195/customerScreen.fxml", actionEvent);
    }

}
