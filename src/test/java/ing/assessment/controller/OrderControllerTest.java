package ing.assessment.controller;

import ing.assessment.db.order.Order;
import ing.assessment.db.order.OrderProduct;
import ing.assessment.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    private final OrderService orderService = mock(OrderService.class);
    private final OrderController orderController = new OrderController(orderService);

    @Test
    void shouldPlaceOrderSuccessfully() {
        Order order = new Order();
        order.setOrderProducts(List.of(new OrderProduct(1, 1)));

        Order savedOrder = new Order();
        savedOrder.setOrderCost(200.0);
        savedOrder.setDeliveryCost(0);
        savedOrder.setDeliveryTime(2);

        when(orderService.placeOrder(order)).thenReturn(savedOrder);

        ResponseEntity<?> response = orderController.placeOrder(order);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Order);
        Order returnedOrder = (Order) response.getBody();

        assertAll(
                () -> assertEquals(200.0, returnedOrder.getOrderCost()),
                () -> assertEquals(0, returnedOrder.getDeliveryCost()),
                () -> assertEquals(2, returnedOrder.getDeliveryTime())
        );
    }

    @Test
    void shouldReturnBadRequestIfInvalid() {
        Order invalidOrder = new Order();
        invalidOrder.setOrderProducts(List.of(new OrderProduct(99, 10)));

        when(orderService.placeOrder(invalidOrder)).thenThrow(new IllegalArgumentException("Invalid product"));

        ResponseEntity<?> response = orderController.placeOrder(invalidOrder);

        assertAll(
                () -> assertEquals(400, response.getStatusCodeValue()),
                () -> assertEquals("Invalid product", response.getBody())
        );
    }
}