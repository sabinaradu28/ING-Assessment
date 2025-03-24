# Order Management

1. [x] Created REST endpoint to place orders:

   - Implemented OrderController with POST /orders
   - Returns HTTP responses (200 OK, 400 Bad Request)


2. [x] Validated product existence:

   Throws IllegalArgumentException if product ID doesn't exist


3. [x] Validated stock availability:

   Throws IllegalArgumentException if quantity exceeds stock


4. [x] Updated product stock:

   Decrements product stock and saves to DB


5. [x] Computed total cost:

   Sums up (price * quantity) for each product


6. [x] Applied delivery cost rules:

    - Free delivery over 500 RON
    - Over 1000 RON: free delivery + 10% discount


7. [x] Computed delivery time:

    - 2 days base
    - Add 2 days for each additional product location


8. [x] Set final order details:

   Sets timestamp, cost, delivery cost, and time


9. [x] Saved order:

   Saves the Order object in the DB via OrderRepository


10. [x] Used Java 17+ features:

    - `var op : order.getOrderProducts()`
    - `"Product with ID %d not found".formatted(id)`
    - `Date.from(Instant.now())`