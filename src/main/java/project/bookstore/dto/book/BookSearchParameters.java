package project.bookstore.dto.book;

public record BookSearchParameters(String titlePart,
                                   String author,
                                   String isbn,
                                   String descriptionPart) {
}
