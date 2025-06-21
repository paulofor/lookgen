package com.lookgen.styler.web;

import com.lookgen.styler.domain.Stage;
import com.lookgen.styler.domain.User;
import com.lookgen.styler.repo.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    private final UserRepository userRepo;

    public AdminController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/v1/admin/replay")
    @Transactional
    public ResponseEntity<Void> replay(@RequestParam("uid") Long uid) {
        User user = userRepo.findById(uid).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        user.setStage(Stage.PENDING_SKETCH);
        user.setRetryCount((short) 0);
        userRepo.save(user);
        return ResponseEntity.accepted().build();
    }
}
