package org.example.addressbook;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AddressBook {
    private List<Contact> contactList;

    public AddressBook() {
        this.contactList = new ArrayList<>();
    }

    public void addContacts(Contact contact) {
        contactList.add(contact);
    }

    public void displayContacts() {
        for(Contact contact: contactList) {
            System.out.println(contact.getFirstName() + " " + contact.getLastName());
            System.out.println(contact.getAddress() + " " + contact.getCity());
            System.out.println(contact.getState() + " " + contact.getZipCode());
            System.out.println(contact.getPhoneNumber() + " " + contact.getEmail());
        }
    }

    public void updateContacts(Contact updateContact) {
        String firstName = updateContact.getFirstName();
        for(Contact contact: contactList) {
            int index = contactList.indexOf(contact);
            if(firstName.equals(contact.getFirstName())) {
                contactList.set(index, updateContact);
                break;
            }
        }
    }

    public void deleteContact(Scanner sc) {
        System.out.println("Enter First name to delete");
        String firstName = sc.nextLine();
        for(Contact contact: contactList) {
            if(contact.getFirstName().equals(firstName)) {
                contactList.remove(contact);
                return;
            }
        }
        System.out.println("No Contact found for given name.");
    }

    public List<Contact> searchContactsInCityorState(String location) {
        return contactList.stream()
                .filter(contact -> contact.getCity().equalsIgnoreCase(location) || contact.getState().equalsIgnoreCase(location))
                .collect(Collectors.toList());
    }

    public List<Contact> getContactsInCity(String city) {
        return contactList.stream()
                .filter(contact -> contact.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

    public List<Contact> getContactsInState(String state) {
        return contactList.stream()
                .filter(contact -> contact.getState().equalsIgnoreCase(state))
                .collect(Collectors.toList());
    }

    public List<Contact> getContactsSortedByFirstName() {
        return contactList.stream().sorted(Comparator.comparing(Contact::getFirstName)).collect(Collectors.toList());
    }

    public List<Contact> getContactsSortedByCity() {
        return contactList.stream().sorted(Comparator.comparing(Contact::getCity)).collect(Collectors.toList());
    }

    public List<Contact> getContactsSortedByState() {
        return contactList.stream().sorted(Comparator.comparing(Contact::getState)).collect(Collectors.toList());
    }

    public List<Contact> getContactsSortedByZip() {
        return contactList.stream().sorted(Comparator.comparing(Contact::getZipCode)).collect(Collectors.toList());
    }

    public void displayContactsJson() {
        System.out.println("JSON Data: ");
        Gson gson = new Gson();
        for(Contact contact: contactList) {
            String jsonContact = gson.toJson(contact);
            System.out.println(jsonContact);
        }

    }
}
