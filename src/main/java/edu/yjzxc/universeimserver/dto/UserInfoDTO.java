package edu.yjzxc.universeimserver.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    private Long id;
    private String account;
    private String faceImage;
    private String nickname;
    private String qrcode;
}
