package torres.wguappointmentapp.Model;

public class Contacts {

    // Fields taken from the ERD database provided for table

    private int contactId;
    private String contactName;
    private String email;


    // Constructor for Contacts
    public Contacts(int contactId, String contactName) { //String email) {
        this.contactId = contactId;
        this.contactName = contactName;
       // this.email = email;
    }

    // Getter and Setter methods

    // Contact ID Getter and Setter
    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {

        this.contactId = contactId;
    }


    // Contact name Getter and Setter
    public String getContactName() {

        return contactName;
    }

    public void setContactName(String contactName) {

        this.contactName = contactName;
    }


    //Email Getter and Setter
    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }
    @Override
    public String toString(){
        return contactName;
    }


}
