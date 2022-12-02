package oyns.billshare.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import oyns.billshare.user.dto.UserDto;
import oyns.billshare.user.service.UserServiceImpl;

@RestController
@Slf4j
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @MessageMapping(value = "")
    @SendTo(value = "")
    public UserDto saveUser(@RequestBody UserDto userDto) {
        log.info("Save user={}", userDto);
        return userService.saveUser(userDto);
    }
}
