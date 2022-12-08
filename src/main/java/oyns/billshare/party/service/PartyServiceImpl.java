package oyns.billshare.party.service;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import oyns.billshare.item.model.Item;
import oyns.billshare.party.dto.PartyCreationDto;
import oyns.billshare.party.model.Party;
import oyns.billshare.party.repository.PartyRepository;
import oyns.billshare.user.dto.UserDto;
import oyns.billshare.user.model.User;
import oyns.billshare.user.repository.UserRepository;

import java.util.Set;
import java.util.UUID;

import static oyns.billshare.party.mapper.PartyMapper.toPartyCreationDto;
import static oyns.billshare.party.mapper.PartyMapper.toPartyFromCreationDto;
import static oyns.billshare.user.mapper.UserMapper.toUser;
import static oyns.billshare.user.mapper.UserMapper.toUserDto;

@Service
public class PartyServiceImpl implements PartyService {
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;

    public PartyServiceImpl(PartyRepository partyRepository, UserRepository userRepository) {
        this.partyRepository = partyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PartyCreationDto saveParty(UserDto userDto) {
        User user = userRepository.save(toUser(userDto));
        userDto = toUserDto(user);
        PartyCreationDto partyDto = PartyCreationDto.builder()
                .name(userDto.getUserName())
                .owner(User.builder()
                        .id(userDto.getId())
                        .name(userDto.getUserName())
                        .build())
                .build();
        return toPartyCreationDto(partyRepository
                .save(toPartyFromCreationDto(partyDto)), userDto);
    }

    @Override
    public void saveNewUserToParty(UserDto userDto, String partyId) {
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new ValidationException("Нет пати с таким id."));
        User user = userRepository.save(toUser(userDto));
        Set<User> users = party.getUsers();
        users.add(user);
        party.setUsers(users);
        partyRepository.save(party);
    }

    @Override
    public PartyCreationDto getPartyById(String partyId) {
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new ValidationException("Нет пати с таким id"));
        return toPartyCreationDto(party, toUserDto(userRepository.findById(party.getInitiator())
                .orElseThrow(() -> new ValidationException("Нет инициатора с таким id"))));
    }

    @Override
    public void deleteUserFromParty(String userId, String partyId) {
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new ValidationException("Нет пати с таким id"));
        Set<User> users = party.getUsers();
        users.removeIf(user -> user.getId().equals(UUID.fromString(userId)));
        party.setUsers(users);
        partyRepository.save(party);
    }

    @Override
    public void deleteItemFromParty(String itemId, String partyId) {
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new ValidationException("Нет пати с таким id"));
        Set<Item> items = party.getItems();
        items.removeIf(item -> item.getId().equals(UUID.fromString(itemId)));
        party.setItems(items);
        partyRepository.save(party);
    }
}
