package com.codequest.platform.repository;
import com.codequest.platform.model.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface InternshipRepository extends JpaRepository<Internship, Long> {
    List<Internship> findByOpenTrueOrderByLastDateAsc();
}
