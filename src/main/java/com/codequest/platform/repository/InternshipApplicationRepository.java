package com.codequest.platform.repository;
import com.codequest.platform.model.InternshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Long> {
    List<InternshipApplication> findByStudentIdOrderByAppliedAtDesc(Long studentId);
    Optional<InternshipApplication> findByIdAndStudentId(Long id, Long studentId);
    boolean existsByStudentIdAndInternshipId(Long studentId, Long internshipId);
}
