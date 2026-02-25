package trutzio.blog.server;

import java.time.LocalDate;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;

@Data
public class BlogPost {

    @MongoId
    private ObjectId id;

    private String title;

    private String content;

    private LocalDate published;

}
