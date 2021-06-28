package com.example.test_ex.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @NonNull
    private String first_name;

    @Column(name = "last_name")
    @NonNull
    private String last_name;

    @Column(name = "age")
    @NonNull
    private Integer age;

    @Column(name = "last_online")
    @NonNull
    private LocalDateTime last_online;

    @Column(name = "online")
    @NonNull
    private Boolean online;

    @Column(name = "email")
    @NonNull
    private String email;

    @Column(name = "phone")
    @NonNull
    private String phone;

    @Setter
    private static int awayOffset;

    public JsonElement getJsonRepresentation() {
        JsonObject inst = new JsonObject();
        inst.addProperty("id", id);
        inst.addProperty("first_name", first_name);
        inst.addProperty("last_name", last_name);
        inst.addProperty("age", age);

        if (!online) {
            inst.addProperty("online", 0);
        } else {
            if (LocalDateTime.now().isAfter(last_online.plusSeconds(awayOffset))) {
                inst.addProperty("online", 2);
            } else {
                inst.addProperty("online", 1);
            }
        }

        inst.addProperty("email", email);
        inst.addProperty("phone", phone);

        return inst;
    }

}
