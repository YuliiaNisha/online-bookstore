package project.bookstore.dto;

public record BookSearchParameters(String titlePart,
                                   String author,
                                   String isbn,
                                   String descriptionPart) {
}
