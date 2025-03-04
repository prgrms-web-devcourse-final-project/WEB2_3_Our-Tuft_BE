package com.example.web2_3_ourtuft_be.user.entity;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeType;
import jakarta.persistence.Embeddable;

@Embeddable
public class Point {
    private final int point;

    public Point() {
        this.point = 0;
    }

    public Point(int point) {
        checkMinus(point);
        this.point = point;
    }

    private void checkMinus(int value) {
        if (value < 0) {
            throw new InvalidRequestException(InvalidRequestMessages.INSUFFICIENT_POINTS);
        }
    }

    public Point updatePoint(int value, PointChangeType type) {

        if (type == PointChangeType.INCREASE) {
            return new Point(point + value);
        }
        return new Point(point - value);
    }

    public int getValue() {
        return point;
    }
}
