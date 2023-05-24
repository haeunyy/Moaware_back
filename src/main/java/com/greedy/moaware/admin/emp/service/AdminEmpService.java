package com.greedy.moaware.admin.emp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;

import com.greedy.moaware.admin.emp.dto.AdminEmpDto;
import com.greedy.moaware.admin.emp.entity.AdminEmp;
import com.greedy.moaware.admin.emp.repository.AdminEmpRepository;
import com.greedy.moaware.employee.dto.EmpDto;
import com.greedy.moaware.employee.entity.Emp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminEmpService {

	private final AdminEmpRepository adminEmpRepository;
//	private final DeptRepository deptRepository;
	private final ModelMapper modelMapper;
//	
//	@Value("${image.image-url}")
//	private String IMAGE_URL;
//	
//	@Value("${image.image-dir}")
//	private String IMAGE_DIR;
	
	public AdminEmpService( AdminEmpRepository adminEmpRepository, ModelMapper modelMapper)
//			DeptRepository deptRepository 
	{
		this.adminEmpRepository = adminEmpRepository;
		//this.deptRepository = deptRepository;
		this.modelMapper = modelMapper;
	}
	
	/* 사원 전체 조회*/
	public List<AdminEmpDto> selectAdminEmpList() {
		
		
		List<AdminEmp> adminEmpList = adminEmpRepository.findAll();
		
		
		List<AdminEmpDto> adminEmpDtoList = adminEmpList.stream().map( adminEmp -> modelMapper.map(adminEmp, AdminEmpDto.class)).collect(Collectors.toList());
		
		
		
		return adminEmpDtoList;
		
	}
	
	/*사원 상세조회*/
	public AdminEmpDto selectAdminEmpDetail(Integer empCode) {
		log.info("[AdminEmpService] selectAdminEmpDetail start ============================== ");
		log.info("[AdminEmpService] empCode : {}" , empCode);
		
		AdminEmp adminEmpList = adminEmpRepository.findById(empCode)
				.orElseThrow( ()-> new IllegalArgumentException("해당 사번을 가진 사원이 없습니다. 사번 = " + empCode));
		log.info("[EmpService] empList : {}" , adminEmpList);
		
		AdminEmpDto adminEmpDto = modelMapper.map(adminEmpList, AdminEmpDto.class);
		
		//empDto.getFileCategory().getFile().setFilePath(IMAGE_URL+empDto.getFileCategory().getFile().getFilePath());
		
		log.info("[AdminEmpService] empDto : {}" , adminEmpDto);
		
		log.info("[AdminEmpService] selectAdminEmpDetail end ================================ ");
		return adminEmpDto;
		
	}
	
	
	
	
}