package oyns.billshare.item.service;

import oyns.billshare.item.dto.ItemDto;

public interface ItemService {
    ItemDto saveItem(ItemDto itemDto, String partyId, String userId);
}
