package com.zheng.hotel.service;

import com.zheng.hotel.bean.rbac.SystemUser;
import com.zheng.hotel.bean.record.SystemOperation;
import com.zheng.hotel.repository.PermissionLongRepository;
import com.zheng.hotel.repository.SystemOperationLongRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemOperationService {

    private final SystemOperationLongRepository systemOperationRepository;
    private final PermissionLongRepository permissionRepository;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    //异步记录管理员操作，只记录写操作，不记录读操作
    public void recordOperation(SystemUser systemUser, String permission) {
        threadPoolTaskExecutor.execute(() -> {
            if (!StringUtils.substringAfterLast(permission, ":").startsWith("get")) {
                permissionRepository.findByPermission(permission).ifPresent(p -> {
                    systemOperationRepository.save(new SystemOperation(systemUser, p));
                });
            }
        });
    }

}
