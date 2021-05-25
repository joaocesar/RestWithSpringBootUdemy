package br.dev.jcp.study.restwithspringbootudemy.services;

import br.dev.jcp.study.restwithspringbootudemy.converter.DozerConverter;
import br.dev.jcp.study.restwithspringbootudemy.data.model.Book;
import br.dev.jcp.study.restwithspringbootudemy.data.vo.v1.BookVO;
import br.dev.jcp.study.restwithspringbootudemy.exception.ResourceNotFoundException;
import br.dev.jcp.study.restwithspringbootudemy.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookServices {

    private final BookRepository repository;

    public BookServices(BookRepository repository) {
        this.repository = repository;
    }

    public BookVO create(BookVO book) {
        var entity = DozerConverter.parseObject(book, Book.class);
        return DozerConverter.parseObject(repository.save(entity), BookVO.class);
    }

    public Page<BookVO> findAll(Pageable pageable) {
        var page = repository.findAll(pageable);
        return page.map(this::convertToBookVO);
    }

    private BookVO convertToBookVO(Book entity) {
        return DozerConverter.parseObject(entity, BookVO.class);
    }

    public BookVO findById(Long id) {

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        return DozerConverter.parseObject(entity, BookVO.class);
    }

    public BookVO update(BookVO book) {
        var entity = repository.findById(book.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        return DozerConverter.parseObject(repository.save(entity), BookVO.class);
    }

    public void delete(Long id) {
        Book entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        repository.delete(entity);
    }

}
