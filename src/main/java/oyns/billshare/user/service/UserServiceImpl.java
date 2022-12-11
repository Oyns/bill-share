package oyns.billshare.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import oyns.billshare.exception.EntityNotFoundException;
import oyns.billshare.party.model.Party;
import oyns.billshare.party.repository.PartyRepository;
import oyns.billshare.user.dto.UserDto;
import oyns.billshare.user.model.User;
import oyns.billshare.user.repository.UserRepository;

import java.util.Set;

import static oyns.billshare.user.mapper.UserMapper.toUser;
import static oyns.billshare.user.mapper.UserMapper.toUserDto;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PartyRepository partyRepository;

    @Override
    public UserDto saveUser(UserDto userDto) {
        Party party = partyRepository.findById(userDto.getPartyId())
                .orElseThrow(() -> new EntityNotFoundException("Нет пати с таким id"));
        Set<User> users = party.getUsers();
        User user = userRepository.save(toUser(userDto));
        users.add(user);
        party.setUsers(users);
        partyRepository.save(party);
        return toUserDto(user);
    }
}
