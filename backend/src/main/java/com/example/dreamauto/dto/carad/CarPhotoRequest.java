package com.example.dreamauto.dto.carad;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CarPhotoRequest(@NotEmpty @Size(max = 10) List<String> photoUrls) {
}
