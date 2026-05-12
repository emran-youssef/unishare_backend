package com.unishare.unishare.dtos.chat;

import com.unishare.unishare.dtos.user.PublicUserDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDto {

    private Long id;
    private String content;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private PublicUserDto sender;
    private PublicUserDto receiver;
    private Long listingId;

}
