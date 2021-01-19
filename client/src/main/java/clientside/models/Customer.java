package clientside.models;

public class Customer extends User {

    public Customer() {}

    public Customer(String firstName, String lastName, String cprNumber, String userId, boolean admin) {
        super(firstName, lastName, cprNumber, userId, admin);
    }


}
