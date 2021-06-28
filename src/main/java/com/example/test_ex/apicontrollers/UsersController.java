package com.example.test_ex.apicontrollers;

import com.example.test_ex.user.User;
import com.example.test_ex.user.UserRepository;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api")
public class UsersController {

    public final UserRepository userRepository;

    @Value("${users.away_offset}")
    public int awayOffset;

    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${users.away_offset}")
    public void injectAwayOffset(int awayOffset) {
        User.setAwayOffset(awayOffset);
    }

    /**
     * Adds new user.
     * @param first_name
     * @param last_name
     * @param age
     * @return Id of new user
     */
    @GetMapping("/users.addNew")
    public ResponseEntity<String> addNew(@RequestParam String  first_name,
                                         @RequestParam String  last_name,
                                         @RequestParam Integer age,
                                         @RequestParam String email,
                                         @RequestParam String phone) {
        User user = User.builder()
                .first_name(first_name)
                .last_name(last_name)
                .age(age)
                .last_online(LocalDateTime.now())
                .online(false)
                .email(email)
                .phone(phone)
                .build();
        user = userRepository.save(user);

        log.info("Saved new user.");
        log.info(user.toString());

        JsonObject resultObject = new JsonObject();
        JsonObject returnId = new JsonObject();
        returnId.addProperty("id", user.getId());
        resultObject.add("response", returnId);

        return new ResponseEntity<>(resultObject.toString(), HttpStatus.OK);
    }

    /**
     * Sends info about user.
     * @param id User id, which details is required
     * @throws 300 - if id is undefined
     * @return Json information about user
     */
    @GetMapping("/users.get")
    public ResponseEntity<String> get(@RequestParam Long id) {
        var user = userRepository.findById(id);

        JsonObject resultObject = new JsonObject();
        if (user.isPresent()) {
            resultObject.add("response", user.get().getJsonRepresentation());

            return new ResponseEntity<>(resultObject.toString(), HttpStatus.OK);
        }

        resultObject.addProperty("error", 300);
        resultObject.addProperty("message", "There is no user with that id");
        return new ResponseEntity<>(resultObject.toString(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Changes status for user
     * @param id User, that status should be changed
     * @param status 0, 1, 2 - offline, online, away
     * @throws 300 - if id or status is undefined
     * @return User id, previous and new status
     */
    @GetMapping("/users.changeStatus")
    public ResponseEntity<String> changeStatus(@RequestParam Long id, @RequestParam Integer status) {
        var user = userRepository.findById(id);
        JsonObject resultObject = new JsonObject();

        if (user.isEmpty()) {
            resultObject.addProperty("error", 300);
            resultObject.addProperty("message", "There is no user with that id");
            return new ResponseEntity<>(resultObject.toString(), HttpStatus.BAD_REQUEST);
        }

        if (status < 0 || status > 2) {
            resultObject.addProperty("error", 300);
            resultObject.addProperty("message", "Incorrect status");
            return new ResponseEntity<>(resultObject.toString(), HttpStatus.BAD_REQUEST);
        }

        JsonObject response = new JsonObject();

        response.addProperty("id", user.get().getId());
        boolean previous = user.get().getOnline();
        if (previous) {
            if (LocalDateTime.now().isAfter(user.get().getLast_online().plusSeconds(awayOffset))) {
                response.addProperty("previous", "2");
            } else {
                response.addProperty("previous", 1);
            }
        } else {
            response.addProperty("previous", 0);
        }

        if (status == 0) {
            user.get().setOnline(false);
        }
        if (status == 1) {
            user.get().setOnline(true);
            user.get().setLast_online(LocalDateTime.now());
        }
        if (status == 2) {
            user.get().setOnline(true);
            user.get().setLast_online(LocalDateTime.now().plusSeconds(awayOffset));
        }

        userRepository.save(user.get());
        response.addProperty("now", status);
        resultObject.add("response", response);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }
}
