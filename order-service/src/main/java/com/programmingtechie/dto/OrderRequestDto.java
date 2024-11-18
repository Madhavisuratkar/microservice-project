package com.programmingtechie.dto;
import java.util.List;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private List<OrderLineItemsDto> orderLineItemsListDto; // Ensure this is OrderLineItemsDto
}
