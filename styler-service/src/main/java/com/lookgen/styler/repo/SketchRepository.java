package com.lookgen.styler.repo;

import com.lookgen.styler.domain.Sketch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SketchRepository extends JpaRepository<Sketch, Long> {
    Sketch findByUserId(Long userId);
}
