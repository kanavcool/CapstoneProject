package contactlist;

public class ContactPayloads {

    public static String getNewUserPayload(String email) {
        return "{ \"firstName\":\"Kanav\", \"lastName\":\"Vij\", \"email\":\"" + email + "\", \"password\":\"myPassword\" }";
    }

    public static String getUpdateUserPayload() {
        return "{ \"firstName\":\"Updated\", \"lastName\":\"Username\", \"email\":\"updated_user@gmail.com\", \"password\":\"myNewPassword\" }";
    }

    public static String getLoginPayload(String testEmail, String testpassword) {
        return "{ \"email\":\""+testEmail+"\", \"password\":\""+testpassword+"\" }";
    }

    public static String getAddContactPayload() {
        return "{ \"firstName\":\"John\", \"lastName\":\"Doe\", \"birthdate\":\"1970-01-01\", \"email\":\"jdoe@fake.com\", \"phone\":\"8005555555\", \"street1\":\"1 Main St.\", \"street2\":\"Apartment A\", \"city\":\"Anytown\", \"stateProvince\":\"KS\", \"postalCode\":\"12345\", \"country\":\"USA\" }";
    }

    public static String getUpdateFullContactPayload() {
        return "{ \"firstName\":\"Amy\", \"lastName\":\"Miller\", \"birthdate\":\"1992-02-02\", \"email\":\"test@fake.com\", \"phone\":\"8005554242\", \"street1\":\"13 School St.\", \"street2\":\"Apt. 5\", \"city\":\"Washington\", \"stateProvince\":\"QC\", \"postalCode\":\"A1A1A1\", \"country\":\"Canada\" }";
    }

    public static String getUpdatePartialContactPayload() {
        return "{ \"firstName\":\"Snow\" }";
    }
}
