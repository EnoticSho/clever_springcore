package clevertec.proxy;

import clevertec.cache.Cache;
import clevertec.entity.Product;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class CachingAspect {

    private final Cache<UUID, Product> cache;

    @Around("execution(* clevertec.dao.ProductDao.findById(..)) && args(id)")
    public Object cacheProduct(ProceedingJoinPoint joinPoint, UUID id) throws Throwable {
        Optional<Product> cachedProduct = cache.get(id);
        if (cachedProduct.isPresent()) {
            return cachedProduct;
        } else {
            Object result = joinPoint.proceed();
            if (result instanceof Optional<?> && ((Optional<?>) result).isPresent()) {
                cache.put(id, (Product) ((Optional<?>) result).get());
            }
            return result;
        }
    }

    @AfterReturning(value = "execution(* clevertec.dao.ProductDao.save(..))", returning = "product")
    public void cacheSaveProduct(Product product) {
        cache.put(product.getId(), product);
    }

    @AfterReturning("execution(* clevertec.dao.ProductDao.update(..)) && args(product)")
    public void cacheUpdateProduct(Product product) {
        cache.put(product.getId(), product);
    }

    @After("execution(* clevertec.dao.ProductDao.delete(..)) && args(id)")
    public void cacheDeleteProduct(UUID id) {
        cache.delete(id);
    }
}
