package oyns.billshare.user.service;

import oyns.billshare.user.dto.NewUserDto;
import oyns.billshare.user.dto.UserDto;

public interface UserService {
    UserDto saveUser(NewUserDto newUserDto);
}
