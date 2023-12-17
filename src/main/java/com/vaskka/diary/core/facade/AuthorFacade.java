package com.vaskka.diary.core.facade;

import com.vaskka.diary.core.model.bizobject.Author;

import java.util.List;

public interface AuthorFacade {

    List<Author> findAll();

    Author findById(String authorId);

    void addAuthor(Author author);

    List<String> getAllAuthorType();

    List<Author> getAuthorByType(String authorType);
}
