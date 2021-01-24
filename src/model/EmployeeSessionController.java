package model;

//singleton design pattern
public final class EmployeeSessionController {

    private static int employeeId;
    private static EmployeeSessionController instance;
    private static String employeeFullName;
    private static String employeeUsername;
    private static int employeeHolidays;


    public static int getEmployeeId() {
        return employeeId;
    }

    public static String getEmployeeFullName() {
        return employeeFullName;
    }

    public static String getEmployeeUsername() {
        return employeeUsername;
    }

    public static int getEmployeeHolidays() {
        return employeeHolidays;
    }

    public EmployeeSessionController(int employeeId, String employeeFullName, String employeeUsername, int employeeHolidays) {
        EmployeeSessionController.employeeId = employeeId;
        EmployeeSessionController.employeeFullName = employeeFullName;
        EmployeeSessionController.employeeUsername= employeeUsername;
        EmployeeSessionController.employeeHolidays = employeeHolidays;
    }

    public static EmployeeSessionController getInstance(int employeeId, String employeeFullName, String employeeUsername, int employeeHolidays) {
        if (instance == null) {
            instance = new EmployeeSessionController(employeeId, employeeFullName, employeeUsername, employeeHolidays);
        }
        return instance;
    }

    public static void cleanEmployeeSession() {
        employeeId = 0;
        employeeFullName = null;
        employeeUsername= null;
        employeeHolidays = 0;
    }

    public static int decreaseEmployeeHolidaysSession(int nrDays) {
        employeeHolidays = employeeHolidays - nrDays;
        return employeeHolidays;
    }

}
