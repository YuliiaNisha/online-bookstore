package project.bookstore.repository.book;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.bookstore.dto.BookSearchParameters;
import project.bookstore.model.Book;
import project.bookstore.repository.SpecificationBuilder;
import project.bookstore.repository.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book, BookSearchParameters> {
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        if (searchParameters == null) {
            throw new IllegalArgumentException("Search Parameters cannot be null");
        }
        Specification<Book> specification = Specification.where(null);
        specification = getSpecificationForParameter(searchParameters.titlePart(),
                Book.SpecificationKey.TITLE_PART.name(),
                        specification);
        specification = getSpecificationForParameter(searchParameters.author(),
                Book.SpecificationKey.AUTHOR.name(),
                        specification);
        specification = getSpecificationForParameter(searchParameters.isbn(),
                Book.SpecificationKey.ISBN.name(),
                        specification);
        specification = getSpecificationForParameter(searchParameters.descriptionPart(),
                Book.SpecificationKey.DESCRIPTION_PART.name(),
                        specification);
        return specification;
    }

    private Specification<Book> getSpecificationForParameter(
            String searchParameter,
            String key,
            Specification<Book> specification) {
        if (searchParameter != null && !searchParameter.isEmpty()) {
            return specification.and(specificationProviderManager.getSpecificationProvider(key)
                                                        .getSpecification(searchParameter));
        }
        return specification;
    }
}
