package com.example.web2_3_ourtuft_be.room.repository;

import com.example.web2_3_ourtuft_be.room.entity.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByRoomNameContaining(String roomName);
}
