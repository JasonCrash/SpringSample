package hello.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hello.entity.Demo;
import hello.repository.DemoRepository;
import hello.service.DemoService;

@Service
public class DemoServiceImpl implements DemoService {
	@Resource
	private DemoRepository demoRepository;
	
	@Override
	public void saveOrUpdateDemo(Demo demo) {
		demoRepository.save(demo);
	}
	
	@Override
	public void deleteDemo(long id) {
		demoRepository.deleteById(id);
	}
	
	@Override
	public List<Demo> getDemoList(){
		Sort sort = new Sort(Sort.Direction.DESC,"id");
		return demoRepository.findAll(sort);
	}
}
