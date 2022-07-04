package nl.han.oose.dea.resources;

import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.services.ItemService;
import nl.han.oose.dea.services.dto.ItemDTO;
import nl.han.oose.dea.services.exceptions.IdAlreadyInUseException;
import nl.han.oose.dea.services.exceptions.ItemNotAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ItemResourceTest {

    private static final String TEXT_ITEMS = "bread, butter";
    private static final int ITEM_ID = 1;
    private static final int HTTP_OK = 200;
    private static final int HTTP_CREATED = 201;

    private ItemResource sut;
    private ItemService itemService;

    @BeforeEach
    void setup() {
        this.sut = new ItemResource();

        this.itemService = mock(ItemService.class);

        this.sut.setItemService(itemService);
    }

    @Test
    void getTextItemsReturnsTextItems() {
        // Arrange

        // Act
        var textItems = sut.getTextItems();

        // Assert
        assertEquals(TEXT_ITEMS, textItems);
    }

    @Test
    void getJsonReturnsObjectFromServiceAsEntity() {
        // Arrange
        var itemsToReturn = new ArrayList<ItemDTO>();
        when(itemService.getAll()).thenReturn(itemsToReturn);

        // Act
        var response = sut.getJsonItems();

        // Assert
        assertEquals(HTTP_OK, response.getStatus());
        assertEquals(itemsToReturn, response.getEntity());
    }

    @Test
    void getJsonItemsCallsGetAll() {
        // Arrange

        // Act
        sut.getJsonItems();

        // Assert
        verify(itemService).getAll();
    }

    @Test
    void addItemsCallsaAddItemOnService() {
        // Arrange
        var item = new ItemDTO(37, "Chocolate spread", new String[]{"Breakfast, Lunch"}, "Not to much");

        // Act
        sut.addItem(item);

        // Assert
        verify(itemService).addItem(item);
    }

    @Test
    void addItemsReturnsHttp201() {
        // Arrange
        var item = new ItemDTO(37, "Chocolate spread", new String[]{"Breakfast, Lunch"}, "Not to much");

        // Act
        var response = sut.addItem(item);

        // Assert
        assertEquals(HTTP_CREATED, response.getStatus());
    }

    @Test
    void addItemsLetsIdAlreadyInUseExceptionPass() {
        // Arrange
        var item = new ItemDTO(37, "Chocolate spread", new String[]{"Breakfast, Lunch"}, "Not to much");
        doThrow(IdAlreadyInUseException.class).when(itemService).addItem(item);

        // Act & Assert
        assertThrows(IdAlreadyInUseException.class, () -> sut.addItem(item));
    }

    @Test
    void getItemCallsGetItemOnService() {
        // Arrange
        var item = new ItemDTO(ITEM_ID, "Chocolate spread", new String[]{"Breakfast, Lunch"}, "Not to much");

        // Act
        sut.getItem(ITEM_ID);

        // Assert
        verify(itemService).getItem(ITEM_ID);
    }

    @Test
    void getItemReturnsObjectFromServiceAsEntity() {
        // Arrange
        var item = new ItemDTO(ITEM_ID, "Chocolate spread", new String[]{"Breakfast, Lunch"}, "Not to much");
        when(itemService.getItem(ITEM_ID)).thenReturn(item);

        // Act
        Response response = sut.getItem(ITEM_ID);

        // Assert
        assertEquals(item, response.getEntity());
    }

    @Test
    void getItemReturnsHttp200() {
        // Arrange
        var item = new ItemDTO(ITEM_ID, "Chocolate spread", new String[]{"Breakfast, Lunch"}, "Not to much");
        when(itemService.getItem(ITEM_ID)).thenReturn(item);

        // Act
        Response response = sut.getItem(ITEM_ID);

        // Assert
        assertEquals(HTTP_OK, response.getStatus());
    }

    @Test
    void getItemLetsItemNotAvailableExceptionPass() {
        // Arrange
        doThrow(ItemNotAvailableException.class).when(itemService).getItem(ITEM_ID);

        // Act & Assert
        assertThrows(ItemNotAvailableException.class, () -> sut.getItem(ITEM_ID));
    }

    @Test
    void deleteItemCallsDeleteItemOnService() {
        // Arrange

        // Act
        sut.deleteItem(ITEM_ID);

        // Assert
        verify(itemService).deleteItem(ITEM_ID);
    }

    @Test
    void deleteItemsReturnsHttp200() {
        // Arrange

        // Act
        var response = sut.deleteItem(ITEM_ID);

        // Assert
        assertEquals(HTTP_OK, response.getStatus());
    }

    @Test
    void deleteItemLetsItemNotAvailableExceptionPass() {
        // Arrange
        doThrow(ItemNotAvailableException.class).when(itemService).deleteItem(ITEM_ID);

        // Act & Assert
        assertThrows(ItemNotAvailableException.class, () -> sut.deleteItem(ITEM_ID));
    }
}
