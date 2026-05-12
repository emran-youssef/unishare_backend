package com.unishare.unishare.mappers;

import com.unishare.unishare.dtos.chat.ChatMessageDto;
import com.unishare.unishare.entities.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses ={UserMapper.class})
public interface ChatMapper {

    @Mapping(source = "reciever", target = "receiver")
    @Mapping(source = "listing.id", target = "listingId")
    ChatMessageDto toDto(ChatMessage chatMessage);

}
