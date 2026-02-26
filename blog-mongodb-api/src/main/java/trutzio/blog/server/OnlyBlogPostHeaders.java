package trutzio.blog.server;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;

public interface OnlyBlogPostHeaders {

    @Value("#{target.id.toString()}")
    String getId();

    String getTitle();

    LocalDate getPublished();
}
