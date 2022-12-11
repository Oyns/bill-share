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

    @Override
    public ItemDto saveItem(ItemDto itemDto, String partyId, String userId) {
        log.info("Save item {}, partyId {}, userId {}", itemDto, partyId, userId);
        itemDto.setIsEqually(true);
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new EntityNotFoundException("Пати с таким id не существует."));
        Set<Item> items = party.getItems();
        Item item = itemRepository.save(toItem(itemDto));
        items.add(item);
        party.setItems(items);
        partyRepository.save(party);
        return toItemDto(item);
    }
}
