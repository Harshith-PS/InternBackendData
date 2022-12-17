package com.infy.infyinterns;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.entity.Mentor;
import com.infy.infyinterns.exception.InfyInternException;
import com.infy.infyinterns.repository.MentorRepository;
import com.infy.infyinterns.service.ProjectAllocationService;
import com.infy.infyinterns.service.ProjectAllocationServiceImpl;

@SpringBootTest
public class InfyInternsApplicationTests {

	@Mock
	private MentorRepository mentorRepository;

	@InjectMocks
	private ProjectAllocationService projectAllocationService = new ProjectAllocationServiceImpl();

	@Test
	public void allocateProjectCannotAllocateTest() throws Exception {

		Mentor mentor = new Mentor();
		mentor.setNumberOfProjectsMentored(3);
		mentor.setMentorId(2001);
		mentor.setMentorName("Harshith");
		
		ProjectDTO projectDTO = new ProjectDTO();
		
		MentorDTO mentorDTO = new MentorDTO();
		mentorDTO.setMentorId(2001);
		
		projectDTO.setMentorDTO(mentorDTO);
		
		Mockito.when(mentorRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(mentor));
		
		InfyInternException exception = Assertions.assertThrows(InfyInternException.class, ()->projectAllocationService.allocateProject(projectDTO));
		Assertions.assertEquals("Service.CANNOT_ALLOCATE_PROJECT", exception.getMessage());
		
	}

	@Test
	public void allocateProjectMentorNotFoundTest() throws Exception {
	
//		Mentor mentor = new Mentor();
//		mentor.setNumberOfProjectsMentored(3);
//		mentor.setMentorId(2001);
//		mentor.setMentorName("Harshith");
		
		ProjectDTO projectDTO = new ProjectDTO();
		
		MentorDTO mentorDTO = new MentorDTO();
		mentorDTO.setMentorId(2001);
		
		projectDTO.setMentorDTO(mentorDTO);
		
		Mockito.when(mentorRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		
		InfyInternException exception = Assertions.assertThrows(InfyInternException.class, ()->projectAllocationService.allocateProject(projectDTO));
		Assertions.assertEquals("Service.MENTOR_NOT_FOUND", exception.getMessage());
	}
}