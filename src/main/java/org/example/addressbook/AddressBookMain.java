package org.example.addressbook;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class AddressBookMain {
    static Map<String, AddressBook> addressBooks = new HashMap<>();
    static Gson gson = new Gson();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("Menu:");
            System.out.println("1. Create a new Address Book");
            System.out.println("2. Add a contact to an Address Book");
            System.out.println("3. Display contact in an Address Book");
            System.out.println("4. Update contact in an Address Book");
            System.out.println("5. Delete contact in an Address Book");
            System.out.println("6. Exit");
            System.out.println("7. Search Persons by City or State");
            System.out.println("8. View Persons by City or State");
            System.out.println("9. Get count of contacts by City or State");
            System.out.println("10. Sort the entries in the address book");
            System.out.println("Enter your Choice: ");

            int option = sc.nextInt();
            sc.nextLine();

            switch(option) {
                case 1:
                    createAddressBook(sc);
                    break;
                case 2:
                    addContact(sc);
                    break;
                case 3:
                    displayAddressBook(sc);
                    break;
                case 4:
                    updateContact(sc);
                    break;
                case 5:
                    deleteContact(sc);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                case 7:
                    searchPerson(sc);
                    break;
                case 8:
                    viewPerson(sc);
                    break;
                case 9:
                    countContacts(sc);
                    break;
                case 10:
                    sortEntries(sc);
                    break;
                default:
                    System.out.println("Invalid option. Please choose a valid option");
            }
        }

    }

    private static void createAddressBook(Scanner sc) {
        System.out.println("Enter a unique name for the Address Book:");
        String bookName = sc.nextLine();
        AddressBook newAddressBook = new AddressBook();
        addressBooks.put(bookName, newAddressBook);
        createCSVAddressBook(bookName);
        System.out.println("Address Book '" + bookName + "' Created successfully!");
    }

    private static void createCSVAddressBook(String bookName) {
        String filepath = "C:\\Users\\Arjun Reddy\\Desktop\\" + bookName + ".csv";
        try(CSVWriter csvWriter = new CSVWriter(new FileWriter(filepath))) {
            String[] header = {"First Name", "Last Name", "Address", "City", "State", "Zip Code", "Phone Number", "Email"};
            csvWriter.writeNext(header);
            System.out.println("CSV file created Successfully");

        } catch (IOException e) {
            System.out.println("Some Error");
            e.printStackTrace();
        }
    }

    private static void addContact(Scanner sc) {
        System.out.println("Enter the name of the Address Book: ");
        String selectedBook = sc.nextLine();
        AddressBook selectedAddressBook = addressBooks.get(selectedBook);
        Contact newContact = readContacts(sc, selectedAddressBook);
        selectedAddressBook.addContacts(newContact);
        String filepath = "C:\\Users\\Arjun Reddy\\Desktop\\" + selectedBook + ".csv";
        try(CSVWriter csvWriter = new CSVWriter(new FileWriter(filepath, true))) {
            String[] contact = {
                    newContact.getFirstName(), newContact.getLastName()
                    , newContact.getAddress(), newContact.getCity(), newContact.getState()
                    , newContact.getZipCode(), newContact.getPhoneNumber(), newContact.getEmail()
            };
            csvWriter.writeNext(contact);
            System.out.println("Contact added Successfully");
        } catch (IOException e) {
            System.out.println("Some Error");
            e.printStackTrace();
        }
    }

    private static void displayAddressBook(Scanner sc) {
        System.out.println("Enter the address book name to display contacts");
        String displayBook = sc.nextLine();
        AddressBook displayAddressBook = addressBooks.get(displayBook);
        if(displayAddressBook!=null) {
            displayAddressBook.displayContacts();
            displayAddressBook.displayContactsJson();
        }
        else {
            System.out.println("Address Book not found");
        }
        String filepath = "C:\\Users\\Arjun Reddy\\Desktop\\" + displayBook + ".csv";
        try(CSVReader csvReader = new CSVReader(new FileReader(filepath))) {
            String[] header = csvReader.readNext();
            String[] rowData;
            while((rowData = csvReader.readNext()) != null) {
                String firstName = rowData[0];
                String lastName = rowData[1];
                String address = rowData[2];
                String city = rowData[3];
                String state = rowData[4];
                String zip = rowData[5];
                String phoneNumber = rowData[6];
                String email = rowData[7];
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Address: " + address);
                System.out.println("City: " + city);
                System.out.println("State: " + state);
                System.out.println("Zip Code: " + zip);
                System.out.println("PhoneNumber: " + phoneNumber);
                System.out.println("Email: " + email);
            }
            System.out.println("CSV fetching details successfully");
        } catch (Exception ex) {
            System.out.println("Some error");
            ex.printStackTrace();
        }
    }

    private static void updateContact(Scanner sc) {
        System.out.println("Enter the address book name to Update Contact");
        String updateBook = sc.nextLine();
        AddressBook updateAddressBook = addressBooks.get(updateBook);
        Contact updateContact = readContacts(sc, updateAddressBook);
        updateAddressBook.updateContacts(updateContact);
    }

    private static void deleteContact(Scanner sc) {
        System.out.println("Enter the address book name to delete Contact");
        String deleteBook = sc.nextLine();
        AddressBook deleteAddressBook = addressBooks.get(deleteBook);
        deleteAddressBook.deleteContact(sc);
    }

    private static void searchPerson(Scanner sc) {
        System.out.println("Enter the City or State to search");
        String location = sc.nextLine();
        System.out.println("Search Results: ");
        addressBooks.values().stream()
                .flatMap(addressBook -> addressBook.searchContactsInCityorState(location).stream())
                .forEach(contact -> System.out.println(contact.getFirstName()+ " " +contact.getLastName()));

    }

    private static void viewPerson(Scanner sc) {
        System.out.println("View Person by (1) City or (2) State:");
        int option = sc.nextInt();
        switch (option) {
            case 1:
                viewPersonByCity(sc);
                break;
            case 2:
                viewPersonByState(sc);
                break;
            default:
                System.out.println("Invalid Option.");
        }
    }

    private static void countContacts(Scanner sc) {
        System.out.println("Get count of contact by (1) City or (2) State: ");
        int option = sc.nextInt();
        switch (option) {
            case 1:
                countByCity(sc);
                break;
            case 2:
                countByState(sc);
                break;
            default:
                System.out.println("Invalid Option.");
        }
    }

    private static Contact readContacts(Scanner sc, AddressBook selectedAddressBook) {
        if(selectedAddressBook == null) {
            System.out.println("Address Book not found");
            return null;
        }
        System.out.println("Enter details for a new contact:");

        System.out.println("Enter first name:");
        String firstName = sc.nextLine();
        System.out.println("Enter last name:");
        String lastName = sc.nextLine();
        System.out.println("Enter address:");
        String address = sc.nextLine();
        System.out.println("Enter city:");
        String city = sc.nextLine();
        System.out.println("Enter state:");
        String state = sc.nextLine();
        System.out.println("Enter zip code:");
        String zipCode = sc.nextLine();
        System.out.println("Enter phone number:");
        String phoneNumber = sc.nextLine();
        System.out.println("Enter email:");
        String email = sc.nextLine();

        return new Contact(firstName, lastName, address, city, state, zipCode, phoneNumber, email);
    }

    private static void viewPersonByCity(Scanner sc) {
        Map<String, List<Contact>> cityPersonMap = new HashMap<>();
        System.out.println("Enter the City name to view persons");
        String city = sc.nextLine();
        addressBooks.values().forEach(addressBook ->
                addressBook.getContactsInCity(city).forEach(contact ->
                        cityPersonMap.computeIfAbsent(city, k -> new ArrayList<>()).add(contact)));
        if(cityPersonMap.containsKey(city)) {
            System.out.println("Persons in " + city + ":");
            cityPersonMap.get(city).forEach(person ->
                    System.out.println(person.getFirstName()+ " " + person.getLastName()));
        } else {
            System.out.println("No person found in " + city);
        }
    }

    private static void viewPersonByState(Scanner sc) {
        Map<String, List<Contact>> statePersonMap = new HashMap<>();
        System.out.println("Enter the State to view persons");
        String state = sc.nextLine();
        addressBooks.values().forEach(addressBook ->
                addressBook.getContactsInState(state).forEach(contact ->
                        statePersonMap.computeIfAbsent(state, k -> new ArrayList<>()).add(contact)));
        if(statePersonMap.containsKey(state)) {
            System.out.println("Person in " + state + ":");
            statePersonMap.get(state).forEach(person ->
                    System.out.println(person.getFirstName()+ " " +person.getLastName()));
        } else {
            System.out.println("No person found in " + state);
        }
    }

    private static void countByCity(Scanner sc) {
        System.out.println("Enter the city name to search");
        String city = sc.nextLine();
        long count = addressBooks.values().stream()
                .flatMap(addrressBook -> addrressBook.getContactsInCity(city).stream()).count();
        System.out.println("Number of contacts in " + city + ": " + count);
    }

    private static void countByState(Scanner sc) {
        System.out.println("Enter the state name to search");
        String state = sc.nextLine();
        long count = addressBooks.values().stream()
                .flatMap(addrressBook -> addrressBook.getContactsInState(state).stream()).count();
        System.out.println("Number of contacts in " + state + ": " + count);
    }

    private static void sortEntries(Scanner sc) {
        System.out.println("Enter the address book name to display contacts");
        String displayBook = sc.nextLine();
        AddressBook displayAddressBook = addressBooks.get(displayBook);
        if(displayAddressBook==null) {
            System.out.println("Address Book not found");
        } else {
            System.out.println("Enter the Field to sort:");
            System.out.println("1. Display contacts sorted by First Name");
            System.out.println("2. Display contacts sorted by City");
            System.out.println("3. Display contacts sorted by State");
            System.out.println("4. Display contacts sorted by Zip");
            int option = sc.nextInt();
            switch (option) {
                case 1:
                    displaySortedByFirstName(displayAddressBook);
                    break;
                case 2:
                    displaySortedByCity(displayAddressBook);
                    break;
                case 3:
                    displaySortedByState(displayAddressBook);
                    break;
                case 4:
                    displaySortedByZip(displayAddressBook);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void displaySortedByFirstName(AddressBook displayAddressBook) {
        List<Contact> sortedContacts = displayAddressBook.getContactsSortedByFirstName();
        sortedContacts.forEach(System.out::println);
        displayContactsJson(sortedContacts);
    }

    private static void displaySortedByCity(AddressBook displayAddressBook) {
        List<Contact> sortedContacts = displayAddressBook.getContactsSortedByCity();
        sortedContacts.forEach(System.out::println);
        displayContactsJson(sortedContacts);
    }

    private static void displaySortedByState(AddressBook displayAddressBook) {
        List<Contact> sortedContacts = displayAddressBook.getContactsSortedByState();
        sortedContacts.forEach(System.out::println);
        displayContactsJson(sortedContacts);
    }

    private static void displaySortedByZip(AddressBook displayAddressBook) {
        List<Contact> sortedContacts = displayAddressBook.getContactsSortedByZip();
        sortedContacts.forEach(System.out::println);
        displayContactsJson(sortedContacts);
    }

    private static void displayContactsJson(List<Contact> sortedContacts) {
        System.out.println("JSON Data: ");
        for(Contact contact: sortedContacts) {
             String jsonContact = gson.toJson(contact);
             System.out.println(jsonContact);
         }
    }
}
