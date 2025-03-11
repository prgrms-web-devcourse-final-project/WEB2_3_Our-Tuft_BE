package com.example.web2_3_ourtuft_be.item.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.web2_3_ourtuft_be.common.PageResponse;
import com.example.web2_3_ourtuft_be.discount.entity.Discount;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.item.dto.ItemRequest;
import com.example.web2_3_ourtuft_be.item.dto.ItemResponse;
import com.example.web2_3_ourtuft_be.item.entity.Item;
import com.example.web2_3_ourtuft_be.item.entity.enums.Category;
import com.example.web2_3_ourtuft_be.item.repository.ItemRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock private ItemRepository itemRepository;

    @InjectMocks private ItemService itemService;

    private Item item1;
    private Item item2;

    @BeforeEach
    public void setUp() {
        item1 =
                Item.builder()
                        .id(1L)
                        .name("item1")
                        .category("EYE")
                        .imageUrl("eye.jpg")
                        .nickColor(null)
                        .originalPrice(100)
                        .stock(10)
                        .build();

        item2 =
                Item.builder()
                        .id(2L)
                        .name("item2")
                        .category("NICKNAME")
                        .imageUrl(null)
                        .nickColor("red")
                        .originalPrice(100)
                        .stock(10)
                        .build();
    }

    @Test
    @DisplayName("모든 아이템 조회 시 전체 목록 반환")
    public void getItemsSuccess() {
        // given
        PageRequest pageable = PageRequest.of(0, 6);
        Slice<Item> items = new SliceImpl<>(List.of(item1, item2), pageable, false);

        when(itemRepository.findFilteredItems(null, null, "default", pageable)).thenReturn(items);

        // when
        PageResponse<ItemResponse> response = itemService.getItems(null, null, pageable);

        // then
        assertNotNull(response);
        assertEquals(2, response.getContent().size());
        assertEquals("item1", response.getContent().get(0).getName());
        assertEquals("item2", response.getContent().get(1).getName());
    }

    @Test
    @DisplayName("카테고리별 아이템 조회 시 해당하는 아이템 반환")
    public void getItemsByCategorySuccess() {
        // given
        PageRequest pageable = PageRequest.of(0, 6);
        Slice<Item> items = new PageImpl<>(List.of(item1), pageable, 1);

        when(itemRepository.findFilteredItems("EYE", null, "default", pageable)).thenReturn(items);

        // when
        PageResponse<ItemResponse> response = itemService.getItems("EYE", null, pageable);

        // then
        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("item1", response.getContent().get(0).getName());
    }

    @Test
    @DisplayName("아이템 등록 성공")
    public void registerItemSuccess() {
        // given
        ItemRequest itemRequest = new ItemRequest("item1", Category.EYE, "eye.jpg", null, 100, 10);
        when(itemRepository.save(any(Item.class))).thenReturn(item1);

        // when
        ItemResponse response = itemService.registerItem(itemRequest);

        // then
        assertNotNull(response);
        assertEquals("item1", response.getName());

        verify(itemRepository).save(any(Item.class));
    }

    @Test
    @DisplayName("아이템 등록 실패 - imageUrl 존재X")
    void registerItemFailMissingImage() {
        // given
        ItemRequest invalidRequest = new ItemRequest("item1", Category.EYE, null, null, 1000, 10);

        // when & then
        assertThatThrownBy(() -> itemService.registerItem(invalidRequest))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining(InvalidRequestMessages.INVALID_IMAGE_VALUE.getMessage());
    }

    @Test
    @DisplayName("아이템 등록 실패 - nickColor 존재X")
    void registerItemFailMissingColor() {
        // given
        ItemRequest invalidRequest =
                new ItemRequest("item1", Category.NICKNAME, null, null, 1000, 10);

        // when & then
        assertThatThrownBy(() -> itemService.registerItem(invalidRequest))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining(InvalidRequestMessages.INVALID_COLOR_VALUE.getMessage());
    }

    @Test
    @DisplayName("아이템 수정 성공")
    public void updateItemSuccess() {
        // given
        ItemRequest itemRequest =
                new ItemRequest("new item1", Category.EYE, "eye.jpg", null, 100, 10);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));

        // when
        ItemResponse response = itemService.updateItem(1L, itemRequest);

        // then
        assertNotNull(response);
        assertEquals("new item1", response.getName());
    }

    @Test
    @DisplayName("아이템 수정 실패 - 아이템 존재X")
    void updateItemFailNotFound() {
        // given
        ItemRequest itemRequest =
                new ItemRequest("new item1", Category.EYE, "eye.jpg", null, 100, 10);
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> itemService.updateItem(1L, itemRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(NotFoundMessages.ITEM.getMessage());
    }

    @Test
    @DisplayName("아이템 삭제 성공")
    public void deleteItemSuccess() {
        // given
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));

        // when
        itemService.deleteItem(1L);

        // then
        verify(itemRepository).delete(any(Item.class));
    }

    @Test
    @DisplayName("아이템 삭제 실패 - 아이템 존재X")
    public void deleteItemFailNotFound() {
        // given
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> itemService.deleteItem(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(NotFoundMessages.ITEM.getMessage());
    }

    @Test
    @DisplayName("찜한 아이템 목록 조회")
    void testGetItemInfoByWishlist() {
        // given
        List<Long> itemIds = List.of(1L, 2L);
        PageRequest pageable = PageRequest.of(0, 6);

        Slice<Item> items = new SliceImpl<>(List.of(item1, item2), pageable, false);

        when(itemRepository.findAllByIdIn(itemIds, pageable)).thenReturn(items);

        // when
        PageResponse<ItemResponse> response = itemService.getItemInfoByWishlist(itemIds, pageable);

        // then
        assertNotNull(response);
        assertFalse(response.isHasNext());
        assertTrue(response.isFirst());
        assertTrue(response.isLast());
        assertFalse(response.isEmpty());
        assertEquals(2, response.getNumberOfElements());
        assertEquals("item1", response.getContent().get(0).getName());
        assertEquals("item2", response.getContent().get(1).getName());
    }

    @Test
    @DisplayName("itemIds가 비어있다면 찜한 빈 목록 조회")
    void getItemInfoByWishlist_ShouldReturnEmptyPage_WhenItemIdsAreEmpty() {
        // given
        List<Long> emptyItemIds = Collections.emptyList();
        PageRequest pageable = PageRequest.of(0, 6);

        // when
        PageResponse<ItemResponse> response =
                itemService.getItemInfoByWishlist(emptyItemIds, pageable);

        // then
        assertNotNull(response);
        assertTrue(response.getContent().isEmpty());
        assertFalse(response.isHasNext());
        assertTrue(response.isFirst());
        assertTrue(response.isLast());
        assertTrue(response.isEmpty());
        assertEquals(0, response.getNumberOfElements());
    }

    @Test
    @DisplayName("주어진 아이템들의 할인 ID를 업데이트한다")
    void testSetDiscountId() {
        // given
        List<Long> itemIds = List.of(1L, 2L);

        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);

        Discount discount =
                Discount.builder()
                        .id(1L)
                        .type("PERCENTAGE")
                        .value(20)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(1))
                        .build();

        when(itemRepository.findAllById(itemIds)).thenReturn(List.of(item1, item2));

        // when
        itemService.setDiscountId(itemIds, discount);

        // then
        verify(item1).updateDiscountId(discount.getId());
        verify(item2).updateDiscountId(discount.getId());
    }
}
