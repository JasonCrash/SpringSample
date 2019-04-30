package hello.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

import hello.entity.MongoDemo;

@Transactional
public interface MongoDemoRepository extends MongoRepository<MongoDemo, ObjectId>{
    MongoDemo findByName(String name);
}
