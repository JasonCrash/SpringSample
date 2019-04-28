package hello.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import hello.entity.MongoDemo;

public interface MongoDemoRepository extends MongoRepository<MongoDemo, ObjectId>{
    MongoDemo findByName(String name);
}
