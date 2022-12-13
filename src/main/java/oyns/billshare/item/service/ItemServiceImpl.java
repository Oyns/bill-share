package oyns.billshare.item.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oyns.billshare.exception.EntityNotFoundException;
import oyns.billshare.item.dto.ItemDto;
import oyns.billshare.item.model.Item;
import oyns.billshare.item.repository.ItemRepository;
import oyns.billshare.party.model.Party;
import oyns.billshare.party.repository.PartyRepository;
import oyns.billshare.user.model.User;
import oyns.billshare.user.repository.UserRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static oyns.billshare.item.mapper.ItemMapper.toItem;
import static oyns.billshare.item.mapper.ItemMapper.toItemDto;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto saveItem(ItemDto itemDto, String partyId, String userId) {
        log.info("Save item {}, partyId {}, userId {}", itemDto, partyId, userId);
        itemDto.setEqually(true);
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("Нет пользователя с таким id."));
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new EntityNotFoundException("Пати с таким id не существует."));
        itemDto.setUsers(Set.of(ItemDto.User.builder()
                .id(user.getId())
                .name(user.getName())
                .build()));
        Item item = itemRepository.save(toItem(itemDto));
        Set<Item> items = party.getItems();
        items.add(item);
        party.setItems(items);
        partyRepository.save(party);
        return toItemDto(item);
    }
}
