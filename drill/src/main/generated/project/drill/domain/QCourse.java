package project.drill.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCourse is a Querydsl query type for Course
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCourse extends EntityPathBase<Course> {

    private static final long serialVersionUID = -1343661571L;

    public static final QCourse course = new QCourse("course");

    public final EnumPath<Center> center = createEnum("center", Center.class);

    public final NumberPath<Long> courseId = createNumber("courseId", Long.class);

    public final StringPath courseName = createString("courseName");

    public final EnumPath<Difficulty> difficulty = createEnum("difficulty", Difficulty.class);

    public final BooleanPath isNew = createBoolean("isNew");

    public QCourse(String variable) {
        super(Course.class, forVariable(variable));
    }

    public QCourse(Path<? extends Course> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCourse(PathMetadata metadata) {
        super(Course.class, metadata);
    }

}

