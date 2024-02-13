package clevertec.proxy;

import clevertec.cache.Cache;
import clevertec.data.ProductTestData;
import clevertec.entity.Product;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CachingAspectTest {

    @Mock
    private Cache<UUID, Product> cache;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private CachingAspect cachingAspect;

    @Test
    void testCacheProduct() throws Throwable {
        // Given
        Product product = ProductTestData
                .builder()
                .build()
                .buildProduct();
        UUID id = product.getId();

        when(cache.get(id)).thenReturn(Optional.empty());
        when(joinPoint.proceed()).thenReturn(Optional.of(product));

        // When
        Object result = cachingAspect.cacheProduct(joinPoint, id);

        // Then
        verify(cache)
                .put(id, product);
        assert result.equals(Optional.of(product));

        // Given
        when(cache.get(id)).thenReturn(Optional.of(product));

        // When
        result = cachingAspect.cacheProduct(joinPoint, id);

        // Then
        verify(joinPoint, times(1)).proceed();
        assert result.equals(Optional.of(product));
    }

    @Test
    void testCacheSaveProduct() {
        // Given
        Product product = ProductTestData
                .builder()
                .build()
                .buildProduct();
        UUID id = product.getId();

        // When
        cachingAspect.cacheSaveProduct(product);

        // Then
        verify(cache).put(id, product);
    }

    @Test
    void testCacheUpdateProduct() {
        // Given
        Product product = ProductTestData
                .builder()
                .build()
                .buildProduct();
        UUID id = product.getId();

        // When
        cachingAspect.cacheUpdateProduct(product);

        // Then
        verify(cache).put(id, product);
    }

    @Test
    void testCacheDeleteProduct() {
        // Given
        UUID id = UUID.fromString("3ecb77f7-0114-47a7-ada7-3ec685d202a7");

        // When
        cachingAspect.cacheDeleteProduct(id);

        // Then
        verify(cache).delete(id);
    }
}