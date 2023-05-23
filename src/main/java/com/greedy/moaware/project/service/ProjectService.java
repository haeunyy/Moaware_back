package com.greedy.moaware.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greedy.moaware.employee.dto.AuthEmpDto;
import com.greedy.moaware.employee.entity.AuthEmp;
import com.greedy.moaware.employee.repository.AuthEmpRepository;
import com.greedy.moaware.exception.UserNotFoundException;
import com.greedy.moaware.project.dto.CreateProjectDto;
import com.greedy.moaware.project.dto.ProjEmpDto;
import com.greedy.moaware.project.dto.ProjParticipantDto;
import com.greedy.moaware.project.dto.ProjParticipantPkDto;
import com.greedy.moaware.project.entity.CreateProject;
import com.greedy.moaware.project.entity.ProjParticipant;
import com.greedy.moaware.project.repository.CreateProjectRepository;
import com.greedy.moaware.project.repository.ProjectRepository;
import com.greedy.moaware.project.repository.ProjectparticipantRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProjectService {
	
	private final ModelMapper modelMapper;
	private final ProjectRepository projResitory;
	private final AuthEmpRepository authEmpRepository;
	private final CreateProjectRepository createProjectRepository;
	private final ProjectparticipantRepository projectparticipantRepository;
	
	public ProjectService(ProjectRepository projResitory, ModelMapper modelMapper, AuthEmpRepository authEmpRepository, CreateProjectRepository createProjectRepository,
			ProjectparticipantRepository projectparticipantRepository) {
		this.modelMapper = modelMapper;
		this.projResitory = projResitory;
		this.authEmpRepository = authEmpRepository;
		this.createProjectRepository = createProjectRepository;
		this.projectparticipantRepository = projectparticipantRepository;
	}
	
	public Page<CreateProjectDto> selectMyProgressProj(Integer empCode, int page) {
		
		log.info("[ProjectService] selectMyproject start ===========================");
		log.info("[ProjectService] empCode : {}", empCode);
		
		
		AuthEmp emp = authEmpRepository.findById(empCode)
				.orElseThrow(() -> new UserNotFoundException("해당 사원이 없습니다."));
		
		AuthEmpDto projEmp = new AuthEmpDto();
		projEmp.setEmpCode(empCode);

		
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("projCode").descending());
		Page<CreateProject> projList = createProjectRepository.findByEmployeeEmpCodeAndProjStatus(pageable, projEmp.getEmpCode(), "진행중");
		
		Page<CreateProjectDto> projDtoList = projList.map(proj -> modelMapper.map(proj, CreateProjectDto.class));
		
		//참여자 정보 매칭
	    projDtoList.forEach(dto -> {
	        List<ProjParticipant> participants = projectparticipantRepository.findByProjCodeProjCode(dto.getProjCode());
	        List<ProjParticipantDto> participantDtos = participants.stream()
	                .map(participant -> {
	                    ProjParticipantDto participantDto = new ProjParticipantDto();
	                    ProjParticipantPkDto projParticipantPkDto = new ProjParticipantPkDto();
	                    projParticipantPkDto.setProjCode(participant.getProjCode().getProjCode());
	                    projParticipantPkDto.setProjMember(participant.getProjCode().getProjMember());
	                    participantDto.setProjMember(projParticipantPkDto);
	                    participantDto.setEmp(modelMapper.map(participant.getEmp(), ProjEmpDto.class));
	                    return participantDto;
	                })
	                .collect(Collectors.toList());
	        dto.setProjMember(participantDtos);
	    });
		return projDtoList;
	}

	public Page<CreateProjectDto> selectMyDoneProj(Integer empCode, int page) {
		
		log.info("[ProjectService] selectMyDoneProj start ===========================");
		log.info("[ProjectService] empCode : {}", empCode);
		
		AuthEmp emp = authEmpRepository.findById(empCode)
				.orElseThrow(() -> new UserNotFoundException("해당 사원이 없습니다."));
		
		AuthEmpDto projEmp = new AuthEmpDto();
		projEmp.setEmpCode(empCode);
		
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("projCode").descending());
		Page<CreateProject> projList = createProjectRepository.findByEmployeeEmpCodeAndProjStatus(pageable, projEmp.getEmpCode(), "완료");
		
		Page<CreateProjectDto> projDtoList = projList.map(proj -> modelMapper.map(proj, CreateProjectDto.class));
		
		//참여자 정보 매칭
	    projDtoList.forEach(dto -> {
	        List<ProjParticipant> participants = projectparticipantRepository.findByProjCodeProjCode(dto.getProjCode());
	        List<ProjParticipantDto> participantDtos = participants.stream()
	                .map(participant -> {
	                    ProjParticipantDto participantDto = new ProjParticipantDto();
	                    ProjParticipantPkDto projParticipantPkDto = new ProjParticipantPkDto();
	                    projParticipantPkDto.setProjCode(participant.getProjCode().getProjCode());
	                    projParticipantPkDto.setProjMember(participant.getProjCode().getProjMember());
	                    participantDto.setProjMember(projParticipantPkDto);
	                    participantDto.setEmp(modelMapper.map(participant.getEmp(), ProjEmpDto.class));
	                    return participantDto;
	                })
	                .collect(Collectors.toList());
	        dto.setProjMember(participantDtos);
	    });
		
		return projDtoList;
	}
	
	@Transactional
	public void createPorj(CreateProjectDto CreateProjectEmpDto) {
		
	/*프로젝트 테이블이 정보 입력*/
		
	log.info("[ProjectService] createPorj start ===========================");
	
	createProjectRepository.save(modelMapper.map(CreateProjectEmpDto, CreateProject.class));
		
	log.info("[ProjectService] createPorj end ===========================");	
		
		
		
	}

}