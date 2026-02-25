package trutzio.blog.server;

import java.util.List;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogPostController {

    private final BlogPostRepository repository;

    public BlogPostController(BlogPostRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/posts")
    public List<BlogPost> all() {
        return repository.findAll();
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

}
