package oyns.billshare.user.mapper;

import org.springframework.stereotype.Component;
import oyns.billshare.user.dto.NewUserDto;
import oyns.billshare.user.dto.UserDto;
import oyns.billshare.user.model.User;

@Component
public class UserMapper {
    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User toUserFromNew(NewUserDto newUserDto) {
        return User.builder()
                .id(newUserDto.getId())
                .name(newUserDto.getUserName())
                .build();
    }
}
