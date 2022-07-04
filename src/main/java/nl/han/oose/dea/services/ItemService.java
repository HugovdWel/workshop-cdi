package nl.han.oose.dea.services;

import nl.han.oose.dea.services.dto.ItemDTO;

import java.util.List;

public interface ItemService {
    List<ItemDTO> getAll();

    void addItem(ItemDTO itemDTO);

    ItemDTO getItem(int id);

    void deleteItem(int id);
}
