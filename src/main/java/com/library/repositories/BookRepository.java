package com.library.repositories;

import com.library.entities.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findAll();

    List<Book> findBooksByTitleContaining(String title);

    List<Book> findBookByAuthor(String author);

    Optional<Book> findBookByIsbn(String isbn);


}
