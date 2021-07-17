package com.webbook.resourceserver.dto.resourceserverDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResourceServerRequestDTO<T> {
    private final ResourceServerCredentialsDTO resourceServerCredentialsDTO;
    private final T userInfo;
}
