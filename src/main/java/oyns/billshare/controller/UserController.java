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

    @PatchMapping("/party/{id}")
    public UserDto saveUser(@RequestBody UserDto userDto, @PathVariable(name = "id") String partyId) {
        log.info("Save user={}, partyId={}", userDto, partyId);
        return userService.saveUser(userDto, partyId);
    }
}
