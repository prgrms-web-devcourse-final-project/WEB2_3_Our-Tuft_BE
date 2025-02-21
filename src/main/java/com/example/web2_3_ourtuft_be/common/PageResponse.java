package com.example.web2_3_ourtuft_be.common;

import java.util.List;
import lombok.Getter;

@Getter
public class PageResponse<T> {

    private final List<T> content;
    private final boolean hasNext;
    private final boolean isFirst;
    private final boolean isLast;
    private final boolean isEmpty;
    private final int numberOfElements;

    public PageResponse(
            List<T> content,
            boolean hasNext,
            boolean isFirst,
            boolean isLast,
            boolean isEmpty,
            int numberOfElements) {
        this.content = content;
        this.hasNext = hasNext;
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.isEmpty = isEmpty;
        this.numberOfElements = numberOfElements;
    }
}
