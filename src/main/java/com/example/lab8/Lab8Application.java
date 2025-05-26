package com.example.lab8;

import com.example.lab8.enums.GuestStatus;
import com.example.lab8.enums.Status;
import com.example.lab8.model.*;
import com.example.lab8.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Lab8Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab8Application.class, args);
    }

    @Bean
    CommandLineRunner runner(
            ContactRepository contactRepo,
            AssociationRepository associationRepo,
            ConferenceRepository conferenceRepo,
            ExhibitionRepository exhibitionRepo

    ) {
        return args -> {

            Contact contact = new Contact();
            contact.setCompany("Acme PR");
            contact.setTitle("Director");
            Name name = new Name();
            name.setSalutation("Mr.");
            name.setFirstName("Juan");
            name.setMiddleName("Carlos");
            name.setLastName("García");
            contact.setName(name);
            contactRepo.save(contact);


            List<Division> divisions = new ArrayList<>();
            for (int i = 1; i <= 7; i++) {
                Member pres = new Member();
                pres.setName("Presidente " + i);
                pres.setStatus(Status.ACTIVE);
                pres.setRenewalDate(LocalDate.now().plusYears(1));

                Member member = new Member();
                member.setName("Miembro " + i);
                member.setStatus(Status.ACTIVE);
                member.setRenewalDate(LocalDate.now().plusYears(1));

                Division division = new Division();
                division.setName("División " + i);
                division.setDistrict("Distrito " + (i % 3));
                division.setPresident(pres);
                division.setMembers(List.of(member));
                divisions.add(division);
            }
            Association association = new Association();
            association.setName("Asociación de Enfermería de España");
            association.setDivisions(divisions);
            associationRepo.save(association);


            Guest guest = new Guest();
            guest.setName("Invitado 1");
            guest.setStatus(GuestStatus.ATTENDING);

            Speaker speaker = new Speaker();
            speaker.setName("Ponente A");
            speaker.setPresentationDuration(45);

            Conference conf = new Conference();
            conf.setTitle("Conferencia Salud");
            conf.setDate(LocalDate.now());
            conf.setDuration(2);
            conf.setLocation("Madrid");
            conf.setGuests(List.of(guest));
            conf.setSpeakers(List.of(speaker));
            conferenceRepo.save(conf);

            Exhibition exhibition = new Exhibition();
            exhibition.setTitle("Exposición Equipos Médicos");
            exhibition.setDate(LocalDate.now());
            exhibition.setDuration(1);
            exhibition.setLocation("Barcelona");
            exhibition.setGuests(List.of(guest));

        };
    }

}



