package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PostRepository {
    private final List<Post> posts = new CopyOnWriteArrayList<>();
    private final AtomicLong nextPostId = new AtomicLong(1L);

    public List<Post> all() {
        return posts.stream()
                .filter(post -> !post.isRemoved())
                .collect(Collectors.toList());
    }

    public Post getById(long id) {
        return posts.stream()
                .filter(post -> post.getId() == id && !post.isRemoved())
                .findFirst().orElseThrow(NotFoundException::new);
    }

    public Post save(Post post) {
        if (post.getId() == 0L) {
            post.setId(nextPostId.getAndIncrement());
            posts.add(post);
            return post;
        } else {
            final Post oldPost = getById(post.getId());
            oldPost.setContent(post.getContent());
            return oldPost;
        }
    }

    public void removeById(long id) {
        getById(id).setRemoved(true);
    }
}
