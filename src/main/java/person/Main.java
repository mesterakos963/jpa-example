package person;

import com.github.javafaker.Faker;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZoneId;
import java.util.List;
import lombok.*;

public class Main {

    private static EntityManagerFactory databaseFactory;
    private static EntityManager database;
    private static Faker faker;

    public static Address makeAddress()
    {
        Address address = Address.builder()
                .country(faker.address().country())
                .state(faker.address().state())
                .city(faker.address().city())
                .streetAddress(faker.address().streetAddress())
                .zip(faker.address().zipCode())
                .build();
        return address;
    }

    public static Person randomPerson()
    {
        Person person = Person.builder()
                .name(faker.name().fullName())
                .dob(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .gender(faker.options().option(Person.Gender.class))
                .address(makeAddress())
                .email(faker.internet().emailAddress())
                .profession(faker.company().profession())
                .build();
        return person;
    }

    public static void main(String[] args) {
        int NumberOfPersons=1000;
        databaseFactory = Persistence.createEntityManagerFactory("jpa-example");
        database = databaseFactory.createEntityManager();
        faker = new Faker();
        try {
            database.getTransaction().begin();
            for (int i = 0; i < NumberOfPersons; i ++) {
                database.persist(randomPerson());
            }
            database.getTransaction().commit();
        } finally {
            database.close();
            databaseFactory.close();
        }
    }


}