package project.drill.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCenter is a Querydsl query type for Center
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCenter extends EntityPathBase<Center> {

    private static final long serialVersionUID = -1353103817L;

    public static final QCenter center = new QCenter("center");

    public final NumberPath<Long> centerId = createNumber("centerId", Long.class);

    public final StringPath centerName = createString("centerName");

    public final StringPath centerRegion = createString("centerRegion");

    public QCenter(String variable) {
        super(Center.class, forVariable(variable));
    }

    public QCenter(Path<? extends Center> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCenter(PathMetadata metadata) {
        super(Center.class, metadata);
    }

}

