package project.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Data
@Table(name = "books")
@SQLDelete(sql = "UPDATE books SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private BigDecimal price;

    private String description;

    private String coverImage;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Getter
    public enum SpecificationKey {
        AUTHOR("author"),
        TITLE_PART("title"),
        ISBN("isbn"),
        DESCRIPTION_PART("description");

        private final String value;

        SpecificationKey(String value) {
            this.value = value;
        }
    }
}
