package oyns.billshare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import oyns.billshare.user.dto.NewUserDto;
import oyns.billshare.user.dto.UserDto;
import oyns.billshare.webconfig.WebSocketConfig;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final WebSocketConfig userService;

    @PostMapping("/user")
    public UserDto addUserToParty(@RequestBody @Valid NewUserDto newUserDto) throws IOException {
        return userService.saveUser(newUserDto);
    }
}
