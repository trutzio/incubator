package trutzio.blog.server;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogPostController {

    private final BlogPostRepository repository;

    public BlogPostController(BlogPostRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/posts")
    public Collection<OnlyBlogPostHeaders> all(@RequestParam(required = false) Optional<Boolean> published) {
        return published.map(p -> {
            if (p) {
                return repository.findAllByPublishedIsNotNull();
            } else {
                return repository.findAllByPublishedIsNull();
            }
        }).orElse(repository.findAllByPublishedIsNotNull());
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<BlogPost> getOne(@PathVariable String id) {
        return repository.findById(new ObjectId(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/post")
    public ResponseEntity<BlogPost> postOne(@RequestBody BlogPost entity) {
        return Stream.of(repository.save(entity))
                .map(ResponseEntity::ok)
                .findFirst()
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/post/publish/{id}")
    public ResponseEntity<BlogPost> publishOne(@PathVariable String id) {
        return repository.findById(new ObjectId(id))
                .map(post -> {
                    post.setPublished(LocalDate.now());
                    return repository.save(post);
                })
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

}
