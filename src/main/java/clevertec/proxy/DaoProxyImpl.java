package clevertec.proxy;

import clevertec.cache.Cache;
import clevertec.dao.ProductDao;
import clevertec.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Прокси-класс для доступа к данным продуктов, инкапсулирующий логику кэширования.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DaoProxyImpl {

    private final ProductDao productDao;
    private final Cache<UUID, Product> cache;

    /**
     * Получает продукт по его идентификатору. Проверяет наличие продукта в кэше.
     * Если продукт не найден в кэше, метод загружает его из DAO, помещает в кэш и возвращает.
     * Если продукт находится в кэше, возвращает его непосредственно из кэша.
     *
     * @param id Идентификатор продукта
     * @return Optional<Product>, содержащий продукт, если он найден, иначе пустой Optional
     */
    public Optional<Product> getProductById(UUID id) {
        Optional<Product> product = cache.get(id);
        if (product.isEmpty()) {
            product = productDao.findById(id);
            product.ifPresent(p -> cache.put(id, p));
        }
        return product;
    }

    /**
     * Получает список всех продуктов с учетом пагинации.
     *
     * @param pageSize Размер страницы (количество продуктов на странице).
     * @param pageNumber Номер страницы (начиная с 1).
     * @return Список продуктов на указанной странице.
     */
    public List<Product> getAllProducts(int pageSize, int pageNumber) {
        return productDao.findAll(pageSize, pageNumber);
    }

    /**
     * Сохраняет продукт, используя DAO, и добавляет его в кэш.
     *
     * @param product Продукт для сохранения
     * @return Сохраненный продукт
     */
    public Product save(Product product) {
        Product save = productDao.save(product);
        cache.put(product.getId(), save);
        return save;
    }

    /**
     * Обновляет продукт с помощью DAO и обновляет его в кэше.
     *
     * @param product Продукт для обновления
     * @return Обновленный продукт
     */
    public Product update(Product product) {
        Product update = productDao.update(product);
        cache.put(product.getId(), update);
        return update;
    }

    /**
     * Удаляет продукт по его идентификатору с помощью DAO и удаляет его из кэша.
     *
     * @param id Идентификатор продукта для удаления
     */
    public void deleteProductById(UUID id) {
        productDao.delete(id);
        cache.delete(id);
    }
}
