package com.lookgen.controller;

import com.lookgen.model.CategoriaItem;
import com.lookgen.model.FotoItem;
import com.lookgen.repository.FotoItemRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UploadController {

    private final FotoItemRepository fotoItemRepo;          // ⬅️ injeta o repo

    public UploadController(FotoItemRepository repo) {
        this.fotoItemRepo = repo;
    }

    @Value("${media.root}")
    private Path mediaRoot;

    @PostMapping("/upload")
    public Map<String, String> upload(@RequestPart MultipartFile file) throws IOException {

        /* 1. gera UUID -------------------------------------------------- */
        String id = UUID.randomUUID().toString();

        /* 2. diretórios -------------------------------------------------- */
        Path origDir  = mediaRoot.resolve("original");
        Path midDir   = mediaRoot.resolve("mid-1024");
        Path thumbDir = mediaRoot.resolve("thumb-200");
        Files.createDirectories(origDir);
        Files.createDirectories(midDir);
        Files.createDirectories(thumbDir);

        /* 3. salva original --------------------------------------------- */
        Path orig = origDir.resolve(id + ".jpg");
        Files.copy(file.getInputStream(), orig, StandardCopyOption.REPLACE_EXISTING);

        /* 4. gera mid + thumb ------------------------------------------- */
        BufferedImage img = ImageIO.read(orig.toFile());
        Thumbnails.of(img)
                .size(1024, 1024)
                .outputQuality(0.82)
                .toFile(midDir.resolve(id + ".jpg").toFile());

        Thumbnails.of(img)
                .size(200, 200)
                .outputQuality(0.60)
                .toFile(thumbDir.resolve(id + ".jpg").toFile());

        /* 5. grava FotoItem no banco ------------------------------------ */
        FotoItem item = new FotoItem();
        item.setId(id);
        item.setUrl("/media/mid-1024/" + id + ".jpg");   // ajuste se quiser
        item.setCategoria(CategoriaItem.ROUPA);          // default
        fotoItemRepo.save(item);

        /* 6. devolve JSON p/ o front ------------------------------------ */
        return Map.of(
                "id", id,
                "midUrl",   "/media/mid-1024/" + id + ".jpg",
                "thumbUrl", "/media/thumb-200/" + id + ".jpg"
        );
    }
}
