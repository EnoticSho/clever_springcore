package clevertec.service.impl;

import clevertec.dto.InfoProductDto;
import clevertec.service.PdfService;
import clevertec.service.ProductService;
import clevertec.utils.pdfserializer.PdfSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.UUID;

/**
 * Класс {@code PdfServiceImpl} предоставляет реализацию сервиса для работы с PDF.
 * Этот сервис используется для преобразования информации о продукте в формат PDF.
 *
 * <p>Данный класс использует {@link PdfSerializer} для сериализации объектов в PDF
 * и {@link ProductService} для получения данных о продукте.
 */
@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private final PdfSerializer pdfSerializer;
    private final ProductService productService;

    /**
     * Преобразует данные продукта, идентифицируемого по UUID, в PDF-документ.
     *
     * @param uuid Уникальный идентификатор продукта, информацию о котором необходимо сериализовать в PDF.
     * @return Путь к созданному PDF-документу.
     * @throws IllegalArgumentException если продукт с указанным UUID не найден.
     */
    @Override
    public Path productToPdf(UUID uuid) {
        InfoProductDto infoProductDto = productService.get(uuid);
        return pdfSerializer.serializeObjectToPdf(infoProductDto);
    }
}
