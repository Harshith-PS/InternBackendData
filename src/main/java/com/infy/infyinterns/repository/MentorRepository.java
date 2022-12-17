package com.infy.infyinterns.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.infy.infyinterns.entity.Mentor;

public interface MentorRepository extends CrudRepository<Mentor, Integer>
{
    List<Mentor> findByNumberOfProjectsMentored(Integer numberOfProjectsMentored);
}
