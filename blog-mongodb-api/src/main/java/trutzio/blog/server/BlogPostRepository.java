package trutzio.blog.server;

import java.util.Collection;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogPostRepository extends MongoRepository<BlogPost, ObjectId> {

    Collection<OnlyBlogPostHeaders> findAllByPublishedIsNotNull();

    Collection<OnlyBlogPostHeaders> findAllByPublishedIsNull();

}
