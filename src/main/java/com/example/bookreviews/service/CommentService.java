package com.example.bookreviews.service;

import com.example.bookreviews.cache.BookCacheManager;
import com.example.bookreviews.dto.CommentDto;
import com.example.bookreviews.dto.CommentRequest;
import com.example.bookreviews.exception.ResourceNotFoundException;
import com.example.bookreviews.mapper.CommentMapper;
import com.example.bookreviews.model.Book;
import com.example.bookreviews.model.Comment;
import com.example.bookreviews.model.User;
import com.example.bookreviews.repository.BookRepository;
import com.example.bookreviews.repository.CommentRepository;
import com.example.bookreviews.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private static final Logger LOG = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final BookCacheManager bookCacheManager;

    public CommentService(
            final CommentRepository commentRepository,
            final BookRepository bookRepository,
            final UserRepository userRepository,
            final CommentMapper commentMapper,
            final BookCacheManager bookCacheManager) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
        this.bookCacheManager = bookCacheManager;
    }

    public List<CommentDto> getAllComments() {
        return commentRepository.findAll().stream()
                .map(commentMapper::toDto).toList();
    }

    public CommentDto getCommentById(final Long id) {
        Comment comment = commentRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Комментарий не найден с id: " + id));
        return commentMapper.toDto(comment);
    }

    @Transactional
    public CommentDto createComment(final CommentRequest request) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена с id: " + request.bookId()));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с id: " + request.userId()));

        Comment comment = new Comment(request.text(), request.rating(), book, user);
        Comment saved = commentRepository.save(comment);
        bookCacheManager.invalidate();
        return commentMapper.toDto(saved);
    }

    @Transactional
    public List<CommentDto> createBulkComments(final List<CommentRequest> requests) {
        LOG.info("Bulk-создание {} комментариев С транзакцией (Идеально решен N+1)", requests.size());

        // 1. Вытаскиваем все ID
        List<Long> bookIds = requests.stream().map(CommentRequest::bookId).distinct().toList();
        List<Long> userIds = requests.stream().map(CommentRequest::userId).distinct().toList();

        // 2. Делаем ровно ДВА запроса в БД (Вместо 2*N)
        Map<Long, Book> booksMap = bookRepository.findAllById(bookIds).stream()
                .collect(Collectors.toMap(Book::getId, b -> b));
        Map<Long, User> usersMap = userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        // 3. Формируем сущности в памяти
        List<Comment> commentsToSave = requests.stream().map(req -> {
            Book book = Optional.ofNullable(booksMap.get(req.bookId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена с id: " + req.bookId()));
            User user = Optional.ofNullable(usersMap.get(req.userId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с id: " + req.userId()));
            return new Comment(req.text(), req.rating(), book, user);
        }).toList();

        // 4. Сохраняем ОДНИМ запросом (Batch Insert)
        List<Comment> saved = commentRepository.saveAll(commentsToSave);
        bookCacheManager.invalidate();

        return saved.stream().map(commentMapper::toDto).toList();
    }

    public List<CommentDto> createBulkCommentsNoTransaction(final List<CommentRequest> requests) {
        LOG.info("Bulk-создание {} комментариев БЕЗ транзакции (Демонстрация)", requests.size());

        // Здесь мы намеренно обрабатываем по-одному (без оптимизации N+1),
        // чтобы при падении 2-го элемента, 1-й УЖЕ лежал в базе данных.
        List<CommentDto> result = requests.stream().map(req -> {
            Book book = bookRepository.findById(req.bookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена с id: " + req.bookId()));
            User user = userRepository.findById(req.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с id: " + req.userId()));

            Comment comment = new Comment(req.text(), req.rating(), book, user);
            Comment saved = commentRepository.save(comment); // Сохранение сразу в цикле!
            LOG.debug("Сохранён комментарий id={}", saved.getId());
            return commentMapper.toDto(saved);
        }).toList();

        bookCacheManager.invalidate();
        return result;
    }

    @Transactional
    public CommentDto updateComment(final Long id, final CommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Комментарий не найден с id: " + id));

        comment.setText(request.text());
        comment.setRating(request.rating());

        Comment updated = commentRepository.save(comment);
        bookCacheManager.invalidate();
        return commentMapper.toDto(updated);
    }

    public void deleteComment(final Long id) {
        commentRepository.deleteById(id);
        bookCacheManager.invalidate();
    }
}