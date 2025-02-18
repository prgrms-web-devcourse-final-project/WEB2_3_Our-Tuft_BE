package com.example.web2_3_ourtuft_be.global.response;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.GlobalException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

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

  public static <T> GlobalResponse<T> fail(GlobalException e) {
    return new GlobalResponse<>(false, String.valueOf(e.getStatus().value()), e.getMessage(), null);
  }
}
