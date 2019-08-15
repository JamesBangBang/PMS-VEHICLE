package com.starnetsecurity.parkClientServer.service;

import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.parkClientServer.entity.AdminRole;
import com.starnetsecurity.parkClientServer.entity.AdminUser;

import java.util.List;

public interface BasicDataSyncService {

    void regist();

    void subSysRegist(String URL,
                             String IP,
                             Integer PORT,
                             String icon,
                             String mainUri,
                             String permission,
                             String pushUri,
                             String serverName) throws BizException;

    void synChronizeSystemAdminRole(AdminRole adminRole, List<String> roleResourceList, String CtrlType) throws BizException;

    Boolean isUseBasicPlatform();

    void setToken(String token);

    String getToken();

    /**
     * merge角色信息，包括角色对应权限的操作，这个操作不需要再同步到平台
     * @param adminRole
     * @param permissions
     * @throws BizException
     */
    public void mergeAdminRole(AdminRole adminRole, List<String> permissions) throws BizException;

    public void deleteAdminRole(String id) throws BizException;

    /**
     * merge用户信息，这个操作不需要再同步到平台
     * @param adminUser
     * @throws BizException
     */
    public void mergeAdminUser(AdminUser adminUser) throws BizException;
    public void deleteAdminUser(String id) throws BizException;

    void synChronizeSystemAdminUser(AdminUser adminUser,  String roleId, String CtrlType) throws BizException;

    /**
     * merge人员信息，这个操作不需要再同步到平台
     * @param systemPersonEntity
     * @throws BizException
     */
//    public void mergeSytemPerson(SystemPersonEntity systemPersonEntity) throws BizException;
//    public void deleteSytemPerson(String id) throws BizException;

    /**
     * merge组织信息，这个操作不需要再同步到平台
     * @param adminResource
     * @throws BizException
     */
//    public void mergeAdminResource(AdminResource adminResource) throws BizException;
//    public void deleteAdminResource(String id) throws BizException;


}