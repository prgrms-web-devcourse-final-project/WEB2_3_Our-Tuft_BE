package com.mockApi.api.controller;

import com.mockApi.api.dto.ItemResponseDto;
import com.mockApi.api.dto.ShopResponseDto;
import com.mockApi.api.global.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "🧨상점", description = "상점 관련 API")
public class ShopController {

  @Operation(summary = "상점의 아이템 조회", description = "상점의 아이템을 조회합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @GetMapping("/shop")
  public ResponseEntity<GlobalResponse<ShopResponseDto>> getShopInfo() {

    String username = "tester1";
    int points = 1500;

    List<ItemResponseDto> items =
        List.of(
            new ItemResponseDto(1, "갈색 머리카락", 500, "hair", "기본 갈색 머리카락", 100, false),
            new ItemResponseDto(2, "졸린 눈", 630, "eye", "졸려 보이는 눈", 50, false),
            new ItemResponseDto(3, "녹색 피부", 130, "skin", "바이러스 먹은 피부", 1500, false));

    ShopResponseDto response = new ShopResponseDto(username, points, items);

    return ResponseEntity.ok().body(GlobalResponse.success(response));
  }

  @Operation(summary = "상점의 아이템 조회", description = "상점의 아이템을 조회합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @GetMapping("/shop/{itemId}")
  public ResponseEntity<ItemResponseDto> getShopInfo(@PathVariable int itemId) {

    ItemResponseDto item =
        new ItemResponseDto(itemId, "갈색 머리카락", 500, "hair", "기본 갈색 머리카락", 100, false);

    return ResponseEntity.ok(item);
  }

  @Operation(summary = "찜 목록 조회", description = "찜 목록을 조회합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @GetMapping("/shop/liked")
  public ResponseEntity<GlobalResponse<List<ItemResponseDto>>> getLikedItems() {
    List<ItemResponseDto> likedItems =
        List.of(
            new ItemResponseDto(1, "갈색 머리카락", 500, "hair", "기본 갈색 머리카락", 100, true),
            new ItemResponseDto(3, "녹색 피부", 130, "skin", "바이러스 먹은 피부", 1500, true));

    return ResponseEntity.ok().body(GlobalResponse.success(likedItems));
  }

  @Operation(summary = "아이템 이름 검색", description = "아이템 이름으로 검색합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @GetMapping("/shop/search")
  public ResponseEntity<GlobalResponse<?>> searchItem(
      @RequestParam(name = "itemName") String itemName) {

    List<ItemResponseDto> allItems =
        List.of(
            new ItemResponseDto(1, "갈색 머리카락", 500, "hair", "기본 갈색 머리카락", 100, false),
            new ItemResponseDto(2, "졸린 눈", 630, "eye", "졸려 보이는 눈", 50, false),
            new ItemResponseDto(3, "녹색 피부", 130, "skin", "바이러스 먹은 피부", 1500, false));

    List<ItemResponseDto> filteredItems = new ArrayList<>();
    for (ItemResponseDto item : allItems) {
      if (item.getItemName().contains(itemName)) {
        filteredItems.add(item);
      }
    }

    if (filteredItems.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(GlobalResponse.failed(HttpStatus.NOT_FOUND, "검색 결과가 없습니다."));
    }

    return ResponseEntity.ok().body(GlobalResponse.success(filteredItems));
  }

  @Operation(summary = "아이템 구매", description = "아이템을 구매합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @GetMapping("/shop/purchase/{itemId}")
  public ResponseEntity<GlobalResponse<?>> purchaseItem(@PathVariable int itemId) {

    int userPoints = 30000;

    ItemResponseDto item =
        new ItemResponseDto(itemId, "갈색 머리카락", 500, "hair", "기본 갈색 머리카락", 100, false);

    if (userPoints < item.getItemPrice()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(GlobalResponse.failed(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."));
    }

    return ResponseEntity.ok().body(GlobalResponse.success(item));
  }
}
