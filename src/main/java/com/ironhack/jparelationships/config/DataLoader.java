package com.ironhack.jparelationships.config;

import com.ironhack.jparelationships.contact.Contact;
import com.ironhack.jparelationships.contact.ContactRepository;
import com.ironhack.jparelationships.contact.Name;
import com.ironhack.jparelationships.event.Conference;
import com.ironhack.jparelationships.event.ConferenceRepository;
import com.ironhack.jparelationships.event.Event;
import com.ironhack.jparelationships.event.EventRepository;
import com.ironhack.jparelationships.event.Exhibition;
import com.ironhack.jparelationships.event.ExhibitionRepository;
import com.ironhack.jparelationships.event.Guest;
import com.ironhack.jparelationships.event.GuestRepository;
import com.ironhack.jparelationships.event.GuestStatus;
import com.ironhack.jparelationships.event.Speaker;
import com.ironhack.jparelationships.event.SpeakerRepository;
import com.ironhack.jparelationships.nurse.Association;
import com.ironhack.jparelationships.nurse.AssociationRepository;
import com.ironhack.jparelationships.nurse.Division;
import com.ironhack.jparelationships.nurse.DivisionRepository;
import com.ironhack.jparelationships.nurse.Member;
import com.ironhack.jparelationships.nurse.MemberRepository;
import com.ironhack.jparelationships.nurse.MemberStatus;
import com.ironhack.jparelationships.task.BillableTask;
import com.ironhack.jparelationships.task.InternalTask;
import com.ironhack.jparelationships.task.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Seeds sample data on startup, then reads it back in a second transaction
 * (a fresh persistence context) so the printed report reflects what was
 * actually stored, not objects still held in memory.
 */
@Configuration
public class DataLoader {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    @Bean
    CommandLineRunner seedAndReport(TransactionTemplate tx,
                                    ContactRepository contacts,
                                    AssociationRepository associations,
                                    DivisionRepository divisions,
                                    MemberRepository members,
                                    SpeakerRepository speakers,
                                    ConferenceRepository conferences,
                                    ExhibitionRepository exhibitions,
                                    EventRepository events,
                                    GuestRepository guests,
                                    TaskRepository tasks) {
        return args -> {
            tx.executeWithoutResult(status -> {
                seedContacts(contacts);
                seedNurseAssociation(associations);
                seedEvents(speakers, conferences, exhibitions);
                seedTasks(tasks);
            });
            tx.executeWithoutResult(status ->
                    report(contacts, associations, divisions, members, speakers, events, guests, tasks));
        };
    }

    // ---------------------------------------------------------------- seeding

    private void seedContacts(ContactRepository contacts) {
        contacts.saveAll(List.of(
                new Contact(new Name("Ms.", "Laura", "Isabel", "Martinez"), "Communications Director", "Iberia Media Group"),
                new Contact(new Name("Mr.", "James", null, "Whitaker"), "Press Officer", "Northbridge PR"),
                new Contact(new Name("Dr.", "Sofia", "Elena", "Rossi"), "Head of Public Affairs", "Mediterranean Health Institute")
        ));
    }

    private void seedNurseAssociation(AssociationRepository associations) {
        Association association = new Association("Nurse Association of Spain");

        association.addDivision(division("Madrid Central", "Madrid",
                member("Carmen Lopez", MemberStatus.ACTIVE, "2026-03-15"),
                member("Javier Ruiz", MemberStatus.ACTIVE, "2026-07-01"),
                member("Lucia Gomez", MemberStatus.LAPSED, "2025-01-10")));
        association.addDivision(division("Madrid North", "Madrid",
                member("Pablo Navarro", MemberStatus.ACTIVE, "2026-05-20"),
                member("Elena Torres", MemberStatus.LAPSED, "2024-11-30")));
        association.addDivision(division("Barcelona", "Catalonia",
                member("Marta Puig", MemberStatus.ACTIVE, "2026-09-12"),
                member("Jordi Serra", MemberStatus.ACTIVE, "2026-02-28")));
        association.addDivision(division("Girona", "Catalonia",
                member("Nuria Vidal", MemberStatus.ACTIVE, "2026-06-05")));
        association.addDivision(division("Valencia", "Valencian Community",
                member("Andres Moreno", MemberStatus.LAPSED, "2025-04-18"),
                member("Rosa Ferrer", MemberStatus.ACTIVE, "2026-08-22")));
        association.addDivision(division("Seville", "Andalusia",
                member("Manuel Ortega", MemberStatus.ACTIVE, "2026-10-03"),
                member("Ines Delgado", MemberStatus.ACTIVE, "2026-01-14")));
        association.addDivision(division("Bilbao", "Basque Country",
                member("Iker Etxeberria", MemberStatus.ACTIVE, "2026-12-01"),
                member("Ane Zubiri", MemberStatus.LAPSED, "2025-03-09")));

        // Cascades to the 7 divisions and every member.
        associations.save(association);
    }

    /** The first member listed becomes the division's president. */
    private static Division division(String name, String district, Member president, Member... others) {
        Division division = new Division(name, district);
        division.setPresident(president);
        for (Member member : others) {
            division.addMember(member);
        }
        return division;
    }

    private static Member member(String name, MemberStatus status, String renewalDate) {
        return new Member(name, status, LocalDate.parse(renewalDate));
    }

    private void seedEvents(SpeakerRepository speakers,
                            ConferenceRepository conferences,
                            ExhibitionRepository exhibitions) {
        Speaker ada = new Speaker("Ada Fernandez", 45);
        Speaker ben = new Speaker("Ben Carter", 30);
        Speaker chloe = new Speaker("Chloe Dubois", 60);
        Speaker diego = new Speaker("Diego Alvarez", 20);
        speakers.saveAll(List.of(ada, ben, chloe, diego));

        Conference javaConf = new Conference("JavaConf Madrid", LocalDate.of(2026, 3, 10), 2, "IFEMA Madrid");
        javaConf.addSpeaker(ada);
        javaConf.addSpeaker(ben);
        javaConf.addGuest(new Guest("Marcos Rivera", GuestStatus.ATTENDING));
        javaConf.addGuest(new Guest("Paula Sanz", GuestStatus.NO_RESPONSE));

        Conference cloudSummit = new Conference("Cloud Summit", LocalDate.of(2026, 5, 22), 1, "Fira Barcelona");
        cloudSummit.addSpeaker(ada); // same speaker at a second conference
        cloudSummit.addSpeaker(chloe);
        cloudSummit.addGuest(new Guest("Tomas Gil", GuestStatus.ATTENDING));
        cloudSummit.addGuest(new Guest("Irene Castro", GuestStatus.NOT_ATTENDING));

        Conference healthTech = new Conference("HealthTech Forum", LocalDate.of(2026, 9, 15), 3, "Palacio de Congresos Valencia");
        healthTech.addSpeaker(chloe);
        healthTech.addSpeaker(diego);
        healthTech.addGuest(new Guest("Raul Pena", GuestStatus.ATTENDING));

        conferences.saveAll(List.of(javaConf, cloudSummit, healthTech));

        Exhibition modernArt = new Exhibition("Modern Art Showcase", LocalDate.of(2026, 4, 1), 30, "Museo Reina Sofia");
        modernArt.addGuest(new Guest("Clara Molina", GuestStatus.ATTENDING));
        modernArt.addGuest(new Guest("Hugo Ramos", GuestStatus.NO_RESPONSE));

        Exhibition photo = new Exhibition("World Press Photo", LocalDate.of(2026, 6, 12), 21, "CCCB Barcelona");
        photo.addGuest(new Guest("Alba Prieto", GuestStatus.NOT_ATTENDING));

        Exhibition robotics = new Exhibition("Robotics Expo", LocalDate.of(2026, 11, 5), 4, "BEC Bilbao");
        robotics.addGuest(new Guest("Sergio Leon", GuestStatus.ATTENDING));
        robotics.addGuest(new Guest("Eva Marin", GuestStatus.NO_RESPONSE));

        exhibitions.saveAll(List.of(modernArt, photo, robotics));
    }

    private void seedTasks(TaskRepository tasks) {
        tasks.saveAll(List.of(
                new BillableTask("Client website redesign", LocalDate.of(2026, 2, 28), false, new BigDecimal("65.00")),
                new BillableTask("Database migration for Acme", LocalDate.of(2026, 1, 15), true, new BigDecimal("80.50")),
                new BillableTask("Quarterly security audit", LocalDate.of(2026, 4, 30), false, new BigDecimal("95.00")),
                new InternalTask("Update onboarding docs", LocalDate.of(2026, 1, 31), true),
                new InternalTask("Team retrospective", LocalDate.of(2026, 2, 6), false),
                new InternalTask("Rotate CI credentials", LocalDate.of(2026, 3, 1), false)
        ));
    }

    // ---------------------------------------------------------------- report

    private void report(ContactRepository contacts,
                        AssociationRepository associations,
                        DivisionRepository divisions,
                        MemberRepository members,
                        SpeakerRepository speakers,
                        EventRepository events,
                        GuestRepository guests,
                        TaskRepository tasks) {
        log.info("===== 1. PR contacts ({}) =====", contacts.count());
        contacts.findAll().forEach(c -> log.info("  {}", c));

        log.info("===== 2. Nurse association =====");
        for (Association association : associations.findAll()) {
            log.info("{} with {} divisions", association, association.getDivisions().size());
            for (Division division : association.getDivisions()) {
                log.info("  {} | president: {} | members: {}", division,
                        division.getPresident().getName(),
                        division.getMembers().stream().map(Member::getName).toList());
            }
        }
        log.info("  Total members: {}, ACTIVE: {}, LAPSED: {}", members.count(),
                members.findByStatus(MemberStatus.ACTIVE).size(),
                members.findByStatus(MemberStatus.LAPSED).size());
        log.info("  findByDistrict(\"Madrid\")    -> {}", names(divisions.findByDistrict("Madrid")));
        log.info("  findByDistrict(\"Catalonia\") -> {}", names(divisions.findByDistrict("Catalonia")));

        log.info("===== 3. Events ({}), speakers ({}), guests ({}) =====",
                events.count(), speakers.count(), guests.count());
        for (Event event : events.findAll()) {
            log.info("  {}", event);
            if (event instanceof Conference conference) {
                log.info("      speakers: {}", conference.getSpeakers().stream()
                        .map(s -> s.getName() + " (" + s.getPresentationDuration() + " min)").toList());
            }
            log.info("      guests:   {}", event.getGuests().stream()
                    .map(g -> g.getName() + " " + g.getStatus()).toList());
        }
        for (GuestStatus status : GuestStatus.values()) {
            log.info("  findByStatus({}) -> {}", status,
                    guests.findByStatus(status).stream().map(Guest::getName).toList());
        }

        log.info("===== Optional: tasks ({}) =====", tasks.count());
        tasks.findAll().forEach(t -> log.info("  {}", t));
    }

    private static List<String> names(List<Division> divisions) {
        return divisions.stream().map(Division::getName).toList();
    }
}
