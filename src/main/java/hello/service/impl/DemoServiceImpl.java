package hello.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.entity.Demo;
import hello.repository.DemoRepository;
import hello.service.DemoService;

@Service
public class DemoServiceImpl implements DemoService {
	@Resource
	private DemoRepository demoRepository;
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void saveOrUpdateDemo(Demo demo){
		demoRepository.save(demo);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void saveOrUpdateDemo1(Demo demo) throws Exception {
		demoRepository.save(demo);
		throw new Exception();
	}
	
	@Transactional
	@Override
	public void saveOrUpdateDemo2(Demo demo) throws Exception {
		demoRepository.save(demo);
		throw new Exception();
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void saveOrUpdateDemo3(Demo demo) {
		try {
			demoRepository.save(demo);
			throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	@Override
	public void saveMoreDemo(List<Demo> demos){
		for (Demo demo : demos) {
			saveOrUpdateDemo(demo);
		}
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
