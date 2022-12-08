package oyns.billshare.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import oyns.billshare.user.dto.UserDto;
import oyns.billshare.user.service.UserServiceImpl;

@RestController
@Slf4j
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    @CrossOrigin(origins = "*")
    public UserDto saveUser(@RequestBody UserDto userDto) {
        log.info("Save user={}, partyId={}", userDto, userDto.getPartyId());
        return userService.saveUser(userDto);
    }
}
