package recipes.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import recipes.Models.User;
import recipes.Services.UserService;
import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping("/api/register")
    public void register(@Valid @RequestBody User user) {
        if (userService.findAll().stream()
                .anyMatch(e -> e.getEmail().equals(user.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.save(user);
    }
/*
    ** commented-out methods were for testing purposes with Postman,
    ** since I could not figure out how to send a User object **
    @PostMapping("/api/register")
    public void register(@RequestParam String email, @RequestParam String password) {
        if (userService.findAll().stream()
                .anyMatch(e -> e.getEmail().equals(email))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        userService.save(user);
    }

 */



}