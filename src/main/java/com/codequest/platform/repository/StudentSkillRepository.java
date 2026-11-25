package com.codequest.platform.repository;
import com.codequest.platform.model.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface StudentSkillRepository extends JpaRepository<StudentSkill, Long> {
    List<StudentSkill> findByStudentId(Long studentId);
    Optional<StudentSkill> findByIdAndStudentId(Long id, Long studentId);
    List<StudentSkill> findBySkillNameIgnoreCase(String name);
}
