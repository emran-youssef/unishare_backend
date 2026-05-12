package com.unishare.unishare.controllers;

import com.unishare.unishare.dtos.chat.ChatMessageDto;
import com.unishare.unishare.dtos.chat.SendMessageRequest;
import com.unishare.unishare.dtos.listing.ListingDto;
import com.unishare.unishare.services.ChatService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    // Send a message to a user about a listing
    @PostMapping("/{listingId}/{receiverId}")
    public ResponseEntity<ChatMessageDto> sendMessage(
            Authentication authentication,
            @PathVariable Long listingId,
            @PathVariable Long receiverId,
            @Valid @RequestBody SendMessageRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(chatService.sendMessage(
                        authentication.getName(),
                        receiverId,
                        listingId,
                        request));
    }

    // Get full conversation between caller and another user on a listing
    @GetMapping("/{listingId}/{userId}")
    public ResponseEntity<List<ChatMessageDto>> getConversation(
            Authentication authentication,
            @PathVariable Long listingId,
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                chatService.getConversation(
                        authentication.getName(),
                        listingId,
                        userId));
    }

    // List all conversations (by listing) for the authenticated user
    @GetMapping("/conversations")
    public ResponseEntity<List<ListingDto>> getConversations(
            Authentication authentication) {

        return ResponseEntity.ok(
                chatService.getConversations(authentication.getName()));
    }


}
