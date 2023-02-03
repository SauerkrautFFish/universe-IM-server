package edu.yjzxc.universeimserver.mapper;

import edu.yjzxc.universeimserver.entity.UserCenter;
import edu.yjzxc.universeimserver.entity.UserCenterExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserCenterMapper {
    int countByExample(UserCenterExample example);

    int deleteByExample(UserCenterExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserCenter record);

    int insertSelective(UserCenter record);

    List<UserCenter> selectByExample(UserCenterExample example);

    UserCenter selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserCenter record, @Param("example") UserCenterExample example);

    int updateByExample(@Param("record") UserCenter record, @Param("example") UserCenterExample example);

    int updateByPrimaryKeySelective(UserCenter record);

    int updateByPrimaryKey(UserCenter record);
}