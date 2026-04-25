package com.unishare.unishare.mappers;

import com.unishare.unishare.dtos.payment.PaymentDto;
import com.unishare.unishare.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    // booking.id lives one level deep — MapStruct needs the explicit path
    @Mapping(source = "booking.id", target = "bookingId")
    //@Mapping(source = "paymentMethod", target = "paymentMethod")
    PaymentDto toPaymentDto(Payment payment);
}
