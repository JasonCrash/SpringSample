package hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import hello.entity.Demo;

@Transactional
public interface DemoRepository extends JpaRepository<Demo, Long>{

}
