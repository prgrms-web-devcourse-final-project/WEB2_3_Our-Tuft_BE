package com.example.web2_3_ourtuft_be.room.repository;

import com.example.web2_3_ourtuft_be.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findRoomNameContaining(String roomName);
}
