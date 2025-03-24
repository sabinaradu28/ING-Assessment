package ing.assessment.service;

import ing.assessment.db.order.Order;

public interface OrderService {
    Order placeOrder(Order order);
}