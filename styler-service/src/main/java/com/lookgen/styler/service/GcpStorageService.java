package com.lookgen.styler.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Storage.PredefinedAcl;
import com.lookgen.styler.config.GcpProperties;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GcpStorageService {
    private final Storage storage;
    private final String bucket;

    public GcpStorageService(GcpProperties props) {
        this.storage = StorageOptions.getDefaultInstance().getService();
        this.bucket = props.getBucket();
    }

    public String upload(byte[] data, String contentType) {
        String name = UUID.randomUUID().toString() + ".png";
        BlobId blobId = BlobId.of(bucket, name);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();
        storage.create(blobInfo, data, Storage.BlobTargetOption.predefinedAcl(PredefinedAcl.PUBLIC_READ));
        return String.format("https://storage.googleapis.com/%s/%s", bucket, name);
    }
}
