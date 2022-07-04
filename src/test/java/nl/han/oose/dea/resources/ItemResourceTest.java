package nl.han.oose.dea.resources;

import nl.han.oose.dea.services.dto.ItemDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ItemResourceTest {

    private static final String TEXT_ITEMS = "bread, butter";
    private static final int ITEM_ID = 1;
    private static final int HTTP_OK = 200;
    private static final int HTTP_CREATED = 201;

    private ItemResource sut;

    @BeforeEach
    void setup() {
        this.sut = new ItemResource();
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
    void getJsonItemsreturnJsonItems() {
        // Arrange

        // Act
        var response = sut.getJsonItems();

        // Assert
        Assertions.assertEquals(HTTP_OK, response.getStatus());
        Assertions.assertTrue(response.getEntity() instanceof List);
    }

    @Test
    void addItemsAddsItem() {
        // Arrange
        var item = new ItemDTO(37, "Chocolate spread", new String[]{"Breakfast, Lunch"}, "Not to much");

        // Act
        var response = sut.addItem(item);

        // Assert
        Assertions.assertEquals(HTTP_CREATED, response.getStatus());
    }

    @Test
    void getItemGetsCorrectItem() {
        // Arrange

        // Act
        var response = sut.getItem(ITEM_ID);

        // Assert
        Assertions.assertEquals(HTTP_OK, response.getStatus());
        Assertions.assertTrue(response.getEntity() instanceof ItemDTO);

        if (response.getEntity() instanceof ItemDTO) {
            var item = (ItemDTO) response.getEntity();
            assertEquals(ITEM_ID, item.getId());
        } else {
            assertFalse(false);
        }
    }

    @Test
    void deleteItemDeletesItem() {
        // Arrange

        // Act
        var response = sut.deleteItem(ITEM_ID);

        // Assert
        Assertions.assertEquals(HTTP_OK, response.getStatus());
    }
}
