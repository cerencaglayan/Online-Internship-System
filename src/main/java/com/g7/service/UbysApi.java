package com.g7.service;

import com.g7.model.IztechUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UbysApi {

    // Fake DB
// Fake DB
    // IztechUser -> Password
    private static final Map<IztechUser, String> validIztechUsers = Map.of(
            new IztechUser("Hakan", "Çoban", 4, 3.0, "hakancoban@std.iyte.edu.tr", "IZTECH_STUDENT", "+901234567890", "123456789","123456789"), "Hakan.coban123!",
            new IztechUser("Hakan", "Taşar", 4, 3.0, "hakantasar@std.iyte.edu.tr", "IZTECH_STUDENT", "+901234567890", "123456789","123456789"),"Hakan.tasar123!",
            new IztechUser("Fahrettin", "Çetin", 4, 3.0, "fahrettincetin@std.iyte.edu.tr", "IZTECH_STUDENT", "+901234567890", "123456789","123456789"),"Fahrettin.cetin123!",
            new IztechUser("Elif", "Özyürek", 4, 3.0, "elifozyurek@std.iyte.edu.tr", "IZTECH_STUDENT", "+901234567890", "123456789","123456789"),"Elif.ozyurek123!",
            new IztechUser("Ceren", "Çağlayan", 4, 3.0, "cerencaglayan@std.iyte.edu.tr", "IZTECH_STUDENT", "+901234567890", "123456789","123456789"),"Ceren.caglayan123!",
            new IztechUser("Cihat", "Gelir", 4, 3.0, "cihatgelir@std.iyte.edu.tr", "IZTECH_STUDENT", "+901234567890", "123456789","123456789"),"Cihat.gelir123!",
            new IztechUser("Emre", "Çapar", 4, 3.0, "emrecapar@std.iyte.edu.tr", "IZTECH_STUDENT", "+901234567890", "123456789","123456789"),"Emre.capar123!",


            new IztechUser("Hakan", "Çoban", null, null, "Coban.hakan@outlook.com", "INTERNSHIP_COORDINATOR", "+901234567890", null,"123456789"),"Coban.hakan123!",
            new IztechUser("Fahrettin", "Çetin", null, null, "cetin.fahrettin@outlook.com", "INTERNSHIP_COORDINATOR", "+901234567890", null,"123456789"),"Cein.fahrettin123!",
            new IztechUser("Hakan", "Taşar", null, null, "hakantasariztech@gmail.com", "INTERNSHIP_COORDINATOR", "+901234567890", null,"123456789"),"Hakan.tasar123!"
    );



    public String[] getStudents() {
        List<String> students = new ArrayList<>();
        for (Map.Entry<IztechUser, String> entry : validIztechUsers.entrySet()) {
            if (entry.getKey().getRole().equals("IZTECH_STUDENT")) {
                students.add(entry.getKey().getEmail());
            }
        }
        return students.toArray(new String[0]);
    }

    public String[] getInternshipCoordinators() {
        List<String> internshipCoordinators = new ArrayList<>();
        for (Map.Entry<IztechUser, String> entry : validIztechUsers.entrySet()) {
            if (entry.getKey().getRole().equals("INTERNSHIP_COORDINATOR")) {
                internshipCoordinators.add(entry.getKey().getEmail());
            }
        }
        return internshipCoordinators.toArray(new String[0]);
    }

    public IztechUser getStudent(String username) {
        if (username == null ) {
            return null;
        }
        for (Map.Entry<IztechUser, String> entry : validIztechUsers.entrySet()) {
            if (entry.getKey().getEmail().equals(username)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public IztechUser signIn(String email, String password) {
        for (Map.Entry<IztechUser, String> entry : validIztechUsers.entrySet()) {
            if (entry.getKey().getEmail().equals(email) && entry.getValue().equals(password)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
