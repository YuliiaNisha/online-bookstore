package project.bookstore.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.bookstore.exception.SpecificationProviderNotFoundException;
import project.bookstore.model.Book;
import project.bookstore.repository.SpecificationProvider;
import project.bookstore.repository.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(
                        () -> new SpecificationProviderNotFoundException(
                                "Can't find SpecificationProvider for key: "
                                        + key));
    }
}
