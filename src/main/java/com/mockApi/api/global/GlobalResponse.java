package com.mockApi.api.global;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mockApi.api.dto.ResponseMessageDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class GlobalResponse<T> {

  private boolean success;
  private String code;
  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T data;

  public static <T> GlobalResponse<T> success(T data) {
    return new GlobalResponse<>(true, "200", "요청에 성공하였습니다.", data);
  }

  public static <T> GlobalResponse<T> created(T data) {
    return new GlobalResponse<>(true, "201", "리소스가 성공적으로 생성되었습니다.", data);
  }

  // 이거 사용 안합니다. Mock 용 임시
  public static <T> GlobalResponse<T> failed(
      HttpStatus status, ResponseMessageDto responseMessage) {
    return new GlobalResponse<>(
        false, String.valueOf(status.value()), responseMessage.getMessage(), null);
  }

  // 이거 사용 안합니다. Mock 용 임시
  public static <T> GlobalResponse<T> failed(HttpStatus status, String message) {
    return new GlobalResponse<>(false, String.valueOf(status.value()), message, null);
  }
}
