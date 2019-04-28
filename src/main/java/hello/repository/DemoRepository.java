package hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.entity.Demo;

public interface DemoRepository extends JpaRepository<Demo, Long>{

}
