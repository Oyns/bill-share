package oyns.billshare.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import oyns.billshare.item.dto.ItemDto;
import oyns.billshare.item.service.ItemServiceImpl;

@RestController
@AllArgsConstructor
@Slf4j
public class ItemController {
    private final ItemServiceImpl itemService;

    @PostMapping("/party/{partyId}/users/{userId}")
    public ItemDto saveItem(@RequestBody ItemDto itemDto,
                            @PathVariable("partyId") String partyId,
                            @PathVariable("userId") String userId) {
        log.info("Save item={}, partyId={}, userId={}", itemDto, partyId, userId);
        return itemService.saveItem(itemDto, partyId, userId);
    }
}
