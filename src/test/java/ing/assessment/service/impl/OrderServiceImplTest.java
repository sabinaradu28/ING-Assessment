package ing.assessment.service.impl;

import ing.assessment.db.order.Order;
import ing.assessment.db.order.OrderProduct;
import ing.assessment.db.product.Product;
import ing.assessment.db.product.ProductCK;
import ing.assessment.db.repository.OrderRepository;
import ing.assessment.db.repository.ProductRepository;
import ing.assessment.model.Location;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    public OrderServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void placeOrder_successfulOrder() {
        var orderProduct = new OrderProduct(1, 2);
        var order = new Order();
        order.setOrderProducts(singletonList(orderProduct));

        var productCK = new ProductCK(1, Location.COLOGNE);
        var product = new Product(productCK, "Shirt", 100.0, 10);

        when(productRepository.findByProductCk_Id(1)).thenReturn(singletonList(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order savedOrder = orderService.placeOrder(order);

        assertAll(
                () -> assertEquals(200.0, savedOrder.getOrderCost(), 0.001),
                () -> assertEquals(30, savedOrder.getDeliveryCost()),
                () -> assertEquals(2, savedOrder.getDeliveryTime())
        );
    }

    @Test
    void placeOrder_productNotFound_throwsException() {
        var order = new Order();
        order.setOrderProducts(singletonList(new OrderProduct(999, 1)));

        when(productRepository.findByProductCk_Id(999)).thenReturn(List.of());

        var exception = assertThrows(IllegalArgumentException.class, () -> orderService.placeOrder(order));
        assertTrue(exception.getMessage().contains("Product with ID 999 not found"));
    }

    @Test
    void placeOrder_insufficientStock_throwsException() {
        var order = new Order();
        order.setOrderProducts(singletonList(new OrderProduct(1, 100)));

        var product = new Product(new ProductCK(1, Location.MUNICH), "Shoes", 400.0, 10);

        when(productRepository.findByProductCk_Id(1)).thenReturn(singletonList(product));

        var exception = assertThrows(IllegalArgumentException.class, () -> orderService.placeOrder(order));
        assertTrue(exception.getMessage().contains("Not enough stock"));
    }
}