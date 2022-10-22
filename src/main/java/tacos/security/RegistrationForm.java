package tacos.security;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.User;

@Data
public class RegistrationForm {
    private String username, password, fullname ,street,city,state,zip,phone;

    public User toUser(PasswordEncoder encoder) {
        var user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setFullname(fullname);
        user.setState(state);
        user.setStreet(street);
        user.setZip(zip);
        user.setPhoneNumber(phone);
        return user;
    }
}
