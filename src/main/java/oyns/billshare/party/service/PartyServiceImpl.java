package oyns.billshare.party.service;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import oyns.billshare.item.dto.ItemDto;
import oyns.billshare.party.dto.PartyCreationDto;
import oyns.billshare.party.model.Party;
import oyns.billshare.party.repository.PartyRepository;
import oyns.billshare.user.dto.UserDto;
import oyns.billshare.user.model.User;
import oyns.billshare.user.repository.UserRepository;

import java.util.UUID;

import static oyns.billshare.party.mapper.PartyMapper.*;
import static oyns.billshare.user.mapper.UserMapper.*;

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
                .owner(PartyCreationDto.User.builder()
                        .id(userDto.getId())
                        .userName(userDto.getUserName())
                        .build())
                .build();
        ItemDto itemDto = ItemDto.builder()
                .name(userDto.getUserName())
                .build();
        return toPartyCreationDto(partyRepository
                .save(toPartyFromCreationDto(partyDto)), userDto, itemDto);
    }

    @Override
    public PartyCreationDto getPartyById(String partyId) {
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new ValidationException("Нет пати с таким id"));
        return toPartyCreationDto(party, new UserDto(), new ItemDto());
    }
}
