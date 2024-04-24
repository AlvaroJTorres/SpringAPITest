package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

  private final StudentRepository studentRepository;

  @Autowired
  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  @Autowired
  public List<Student> getStudents() {
		return studentRepository.findAll();
	}

  public void addNewStudent(Student student) {
    Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
    if(studentOptional.isPresent()) {
      throw new IllegalStateException("email taken");
    }
    studentRepository.save(student);
  }

  public void deleteStudent(Long studentId) {
    boolean exists = studentRepository.existsById(studentId);
    if(!exists) {
      throw new IllegalStateException("student with id " + studentId + " does not exists");
    }
    studentRepository.deleteById(studentId);
  }

  @Transactional
  public void updateStudent(Long studentId, Student studentData) {
    Student studentToUpdate = studentRepository.findById(studentId).orElseThrow(() -> 
    new IllegalStateException("student with id " + studentId + " does not exist")
    );
    
    String name = studentData.getName();
    String email = studentData.getEmail();

    if(name != null && name.length() > 0 && !Objects.equals(studentToUpdate.getName(), name)) {
      studentToUpdate.setName(name);
    }

    if(email != null && email.length() > 0 && !Objects.equals(studentToUpdate.getEmail(), email)) {
      Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
      if(studentOptional.isPresent()) {
        throw new IllegalStateException("email taken");
      }
      studentToUpdate.setEmail(email);
    }
  }
}
