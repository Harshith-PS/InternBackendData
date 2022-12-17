package com.infy.infyinterns.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.entity.Mentor;
import com.infy.infyinterns.entity.Project;
import com.infy.infyinterns.exception.InfyInternException;
import com.infy.infyinterns.repository.MentorRepository;
import com.infy.infyinterns.repository.ProjectRepository;

@Service(value = "projectAllocationService")
@Transactional
public class ProjectAllocationServiceImpl implements ProjectAllocationService {
	
	@Autowired
	private ProjectRepository projectReposity; 
	
	@Autowired
	private MentorRepository mentorRespository;

	@Override
	public Integer allocateProject(ProjectDTO project) throws InfyInternException {
		Optional<Mentor> mentorEntity = mentorRespository.findById(project.getMentorDTO().getMentorId());
		Mentor mentor = mentorEntity.orElseThrow(()-> new InfyInternException("Service.MENTOR_NOT_FOUND"));
		
		Project projectEntity = new Project();
		
		if(mentor.getNumberOfProjectsMentored()>=3)
		{
			throw new InfyInternException("Service.CANNOT_ALLOCATE_PROJECT");
		}
		else
		{
			
			projectEntity.setProjectId(project.getProjectId());
			projectEntity.setProjectName(project.getProjectName());
			projectEntity.setIdeaOwner(project.getIdeaOwner());
			projectEntity.setReleaseDate(project.getReleaseDate());			
			projectEntity.setMentor(mentor);
			
			mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored()+1);
			
			projectReposity.save(projectEntity);
		}
			
		return projectEntity.getProjectId();
	}

	
	@Override
	public List<MentorDTO> getMentors(Integer numberOfProjectsMentored) throws InfyInternException {
		List<Mentor> mentorList = mentorRespository.findByNumberOfProjectsMentored(numberOfProjectsMentored);
		
		System.out.println(mentorList);
		List<MentorDTO> mentorDTOs = new ArrayList<>();
		if(mentorList.isEmpty()) 
			throw new InfyInternException("Service.MENTOR_NOT_FOUND");
		for(Mentor m: mentorList) {
			MentorDTO mentorDTO = new MentorDTO();
			mentorDTO.setMentorId(m.getMentorId());
			mentorDTO.setMentorName(m.getMentorName());
			mentorDTO.setNumberOfProjectsMentored(m.getNumberOfProjectsMentored());
			mentorDTOs.add(mentorDTO);
		}
		return mentorDTOs;
	}


	@Override
	public void updateProjectMentor(Integer projectId, Integer mentorId) throws InfyInternException {
	Optional<Mentor> opMentor =mentorRespository.findById(mentorId);
	Mentor mentor = opMentor.orElseThrow(()-> new InfyInternException("Service.MENTOR_NOT_FOUND"));
	if(mentor.getNumberOfProjectsMentored()>=3)
		throw new InfyInternException("Service.CANNOT_ALLOCATE_PROJECT");
	
	Optional<Project> opProj =projectReposity.findById(projectId);
	Project project = opProj.orElseThrow(()-> new InfyInternException("Service.PROJECT_NOT_FOUND"));
	
	project.setMentor(mentor);
	
	mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored()+1);
	}
	

	@Override
	public void deleteProject(Integer projectId) throws InfyInternException {
		Optional<Project> opProj =projectReposity.findById(projectId);
		Project project = opProj.orElseThrow(()-> new InfyInternException("Service.PROJECT_NOT_FOUND"));
		
		if(project.getMentor()!=null)
		{
			project.getMentor().setNumberOfProjectsMentored(project.getMentor().getNumberOfProjectsMentored()-1);
			project.setMentor(null);		
		}
		projectReposity.delete(project);		
	}


	@Override
	public MentorDTO getMentorDetails(Integer mentorId) throws InfyInternException {
		Optional<Mentor> opMentor =mentorRespository.findById(mentorId);
		Mentor m = opMentor.orElseThrow(()-> new InfyInternException("Service.MENTOR_NOT_FOUND"));
		MentorDTO mentorDTO = new MentorDTO();
		mentorDTO.setMentorId(m.getMentorId());
		mentorDTO.setMentorName(m.getMentorName());
		mentorDTO.setNumberOfProjectsMentored(m.getNumberOfProjectsMentored());
		return mentorDTO;
	}
}