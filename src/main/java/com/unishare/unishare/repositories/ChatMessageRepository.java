package com.unishare.unishare.repositories;

import com.unishare.unishare.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // fetch all messages between two users in the context of a specific listing, ordered oldest first
    @Query("""
        SELECT m FROM ChatMessage m
        WHERE m.listing.id = :listingId
          AND ((m.sender.id = :userA AND m.reciever.id = :userB)
           OR  (m.sender.id = :userB AND m.reciever.id = :userA))
        ORDER BY m.createdAt ASC
    """)
    List<ChatMessage> findConversation(
            @Param("listingId") Long listingId,
            @Param("userA") Long userA,
            @Param("userB") Long userB
    );

    @Query(
            """
                    SELECT DISTINCT m.listing.id FROM ChatMessage m 
                    WHERE m.sender.id = :userId OR m.reciever.id = :userId
                    """
           )
    List<Long> findDistinctListingIdForUser(@Param("userId") Long userId);

}
