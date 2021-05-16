package classes;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

public class Person {
    private String name;
    private String surname;
    private String email;
    private String phone;
    private Date birthDate;
    private String password;
    private int photoID;

    public Person(String name, String email, String password, int photoID) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.photoID = photoID;
    }

    public Person(String name, String surname, String email, String phone, Date birthDate, String password, int photoID) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.password = password;
        this.photoID = photoID;
    }

    public Person(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhotoID() {
        return photoID;
    }

    public void setPhotoID(int photoID) {
        this.photoID = photoID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /* public static List<Person> getPersonList() {
        List<Person> personList = new ArrayList<>();
        Person p1 = new Person("p1","person1@yildiz.edu.tr","person1", R.drawable.fish);
        Person p2 = new Person("p2","person2@yildiz.edu.tr","person2", R.drawable.organic);
        Person p3 = new Person("p3","person3@yildiz.edu.tr","person3", R.drawable.egg);

        personList.add(p1);
        personList.add(p2);
        personList.add(p3);

        return personList;
    } */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(email, person.email) &&
                Objects.equals(password, person.password);
    }
}
