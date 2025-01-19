package project.bookstore.repository.book.spec;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.bookstore.model.Book;
import project.bookstore.repository.SpecificationProvider;

@Component
public class TitlePartSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return Book.SpecificationKey.TITLE_PART.getValue();
    }

    @Override
    public Specification<Book> getSpecification(String param) {
        return new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get(Book.SpecificationKey.TITLE_PART.getValue())),
                                "%" + param.toLowerCase() + "%");
            }
        };
    }
}
