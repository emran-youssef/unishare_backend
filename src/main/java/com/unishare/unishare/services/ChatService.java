package com.unishare.unishare.services;

import com.unishare.unishare.dtos.chat.ChatMessageDto;
import com.unishare.unishare.dtos.chat.SendMessageRequest;
import com.unishare.unishare.dtos.listing.ListingDto;
import com.unishare.unishare.entities.ChatMessage;
import com.unishare.unishare.exceptions.Listing.ListingNotFoundException;
import com.unishare.unishare.exceptions.User.UserNotFoundException;
import com.unishare.unishare.mappers.ChatMapper;
import com.unishare.unishare.mappers.ListingMapper;
import com.unishare.unishare.repositories.ChatMessageRepository;
import com.unishare.unishare.repositories.ListingRepository;
import com.unishare.unishare.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;
    private final ChatMapper chatMapper;
    private final ListingMapper listingMapper;

    @Transactional
    public ChatMessageDto sendMessage(
            String senderEmail,
            Long receiverId,
            Long listingId,
            SendMessageRequest request)
    {
        // load sender by email from JWT
        var sender = userRepository.findByUniversityEmail(senderEmail)
                .orElseThrow(() -> new UserNotFoundException("Sender user not found: " + senderEmail));

        // load receiver by ID from path variable
        var receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException("Receiver user not found: " + receiverId));

        // load listing for context
        var listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException(
                        "Listing not found: " + listingId));

        var message = ChatMessage.builder()
                .sender(sender)
                .reciever(receiver)
                .content(request.getContent())
                .listing(listing)
                .isRead(false)
                .build();

        return chatMapper.toDto(chatMessageRepository.save(message));
    }



    @Transactional
    //get a specific conversation between two users on a listing
    public List<ChatMessageDto> getConversation(
            String callerEmail,
            Long listingId,
            Long otherUserId) {

        var caller = userRepository.findByUniversityEmail(callerEmail)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found: " + callerEmail));

        return chatMessageRepository
                .findConversation(listingId, caller.getId(), otherUserId)
                .stream()
                .map(chatMapper::toDto)
                .toList();
    }

    @Transactional
    //get All conversations for a user
    public List<ListingDto> getConversations(String callerEmail) {

        var caller = userRepository.findByUniversityEmail(callerEmail)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found: " + callerEmail));

        // get all listing IDs where this user has messages
        return chatMessageRepository
                .findDistinctListingIdForUser(caller.getId())
                .stream()
                .map(listingId -> listingRepository.findById(listingId)
                        .orElseThrow(() -> new ListingNotFoundException(
                                "Listing not found: " + listingId)))
                .map(listingMapper::toDto)
                .toList();
    }


}
