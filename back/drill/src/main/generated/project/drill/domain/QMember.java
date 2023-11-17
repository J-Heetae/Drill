package project.drill.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1066859396L;

    public static final QMember member = new QMember("member1");

    public final EnumPath<Center> center = createEnum("center", Center.class);

    public final EnumPath<Difficulty> difficulty = createEnum("difficulty", Difficulty.class);

    public final NumberPath<Long> max_score = createNumber("max_score", Long.class);

    public final NumberPath<Long> member_score = createNumber("member_score", Long.class);

    public final StringPath memberEmail = createString("memberEmail");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath memberNickname = createString("memberNickname");

    public final StringPath password = createString("password");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

