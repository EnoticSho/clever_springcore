package clevertec.servletlistener;

import clevertec.config.AppConfig;
import clevertec.filter.EncodingFilter;
import clevertec.filter.ErrorHandlingFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Слушатель контекста сервлета, который инициализирует контекст Spring и регистрирует фильтры при старте приложения.
 *
 * <p>Этот слушатель отвечает за создание и конфигурацию {@link ApplicationContext} с использованием класса {@link AppConfig}.
 * Помимо этого, он регистрирует фильтры {@link EncodingFilter} и {@link ErrorHandlingFilter} в контексте сервлета.
 */
@WebListener
public class SpringContextListener implements ServletContextListener {

    /**
     * Вызывается контейнером сервлетов при инициализации контекста приложения.
     *
     * <p>В этом методе создается контекст Spring с использованием {@link AnnotationConfigApplicationContext} и
     * регистрируются фильтры {@link EncodingFilter} и {@link ErrorHandlingFilter}.
     * Контекст Spring сохраняется в атрибутах {@link ServletContext} для дальнейшего использования в приложении.
     *
     * @param sce событие инициализации контекста сервлета, предоставляющее доступ к {@link ServletContext}.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        FilterRegistration.Dynamic encodingFilter = ctx.addFilter("EncodingFilter", new EncodingFilter());
        encodingFilter.addMappingForUrlPatterns(null, false, "/*");

        FilterRegistration.Dynamic errorHandlingFilter = ctx.addFilter("ErrorHandlingFilter", new ErrorHandlingFilter());
        errorHandlingFilter.addMappingForUrlPatterns(null, false, "/*");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        sce.getServletContext().setAttribute("springContext", context);
    }

    /**
     * Вызывается контейнером сервлетов при уничтожении контекста приложения.
     *
     * <p>Этот метод отвечает за закрытие и освобождение ресурсов, занимаемых контекстом Spring.
     *
     * @param sce событие уничтожения контекста сервлета, предоставляющее доступ к {@link ServletContext}.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ApplicationContext context = (ApplicationContext) sce.getServletContext().getAttribute("springContext");
        ((AnnotationConfigApplicationContext) context).close();
    }
}
