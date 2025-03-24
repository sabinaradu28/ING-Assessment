package ing.assessment.service.impl;

import ing.assessment.db.order.Order;
import ing.assessment.db.repository.OrderRepository;
import ing.assessment.db.repository.ProductRepository;
import ing.assessment.model.Location;
import ing.assessment.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Order placeOrder(Order order) {
        double totalCost = 0;
        var locations = new HashSet<Location>();

        for (var op : order.getOrderProducts()) {
            var products = productRepository.findByProductCk_Id(op.getProductId());
            if (products.isEmpty()) throw new IllegalArgumentException("Product with ID %d not found".formatted(op.getProductId()));

            var product = products.get(0);
            if (product.getQuantity() < op.getQuantity())
                throw new IllegalArgumentException("Not enough stock for product ID %d".formatted(op.getProductId()));

            product.setQuantity(product.getQuantity() - op.getQuantity());
            productRepository.save(product);

            totalCost += product.getPrice() * op.getQuantity();
            locations.add(product.getProductCk().getLocation());
        }

        int deliveryCost = totalCost > 500 ? 0 : 30;
        if (totalCost > 1000) {
            totalCost *= 0.9;
        }

        int deliveryTime = 2 + Math.max(0, locations.size() - 1) * 2;

        order.setTimestamp(Date.from(Instant.now()));
        order.setOrderCost(totalCost);
        order.setDeliveryCost(deliveryCost);
        order.setDeliveryTime(deliveryTime);

        return orderRepository.save(order);
    }
}