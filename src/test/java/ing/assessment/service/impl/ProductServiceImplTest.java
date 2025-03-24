package ing.assessment.service.impl;

import ing.assessment.db.product.Product;
import ing.assessment.db.product.ProductCK;
import ing.assessment.db.repository.ProductRepository;
import ing.assessment.model.Location;
import ing.assessment.service.ProductService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final ProductService productService = new ProductServiceImpl(productRepository);

    @Test
    void shouldReturnAllProducts() {
        var product = new Product(new ProductCK(1, Location.MUNICH), "Shoes", 400.0, 50);
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> result = productService.getAllProducts();

        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Shoes", result.get(0).getName()),
                () -> assertEquals(Location.MUNICH, result.get(0).getProductCk().getLocation()),
                () -> assertEquals(400.0, result.get(0).getPrice(), 0.001),
                () -> assertEquals(50, result.get(0).getQuantity())
        );
    }

    @Test
    void shouldReturnProductsById() {
        var product = new Product(new ProductCK(1, Location.COLOGNE), "Shoes", 400.0, 50);
        when(productRepository.findByProductCk_Id(1)).thenReturn(List.of(product));

        List<Product> result = productService.getProductsById(1);

        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Shoes", result.get(0).getName()),
                () -> assertEquals(Location.COLOGNE, result.get(0).getProductCk().getLocation()),
                () -> assertEquals(400.0, result.get(0).getPrice(), 0.001),
                () -> assertEquals(50, result.get(0).getQuantity())
        );
    }
}
