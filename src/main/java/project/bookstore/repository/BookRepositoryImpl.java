package project.bookstore.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import project.bookstore.exception.DataProcessingException;
import project.bookstore.model.Book;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save book to DB: "
            + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return book;
    }

    @Override
    public Optional<Book> findBookById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> byIdQuery = session.createQuery("FROM Book b "
                    + "WHERE b.id = :id", Book.class);
            byIdQuery.setParameter("id", id);
            return Optional.ofNullable(byIdQuery.getSingleResultOrNull());
        } catch (Exception e) {
            throw new DataProcessingException("Can't find Book by id: " + id, e);
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> getAllBooks = session.createQuery("FROM Book", Book.class);
            return getAllBooks.list();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get list of all books from DB", e);
        }
    }
}
