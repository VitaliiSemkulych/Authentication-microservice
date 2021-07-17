package com.webbook.resourceserver.dto.resourceserverDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResourceServerCredentialsDTO {
   private String resourceServerId;
   private String resourceServerSecret;

}
