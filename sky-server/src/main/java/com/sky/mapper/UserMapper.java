package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface UserMapper {
    @Select("select * from user where openid=#{openid}")
    User getByOpenid(String openid);

    /**
     * 插入数据
     * @param user
     */
    void insert(User user);

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    @Select("SELECT * FROM user WHERE id = #{id}")
    User getById(Long id);

    Integer countByMap(@Param("begin")LocalDateTime begin,
                       @Param("end") LocalDateTime end);

    /**
     * 根据条件查询用户数量
     * @param map
     * @return
     */
    //Integer countByMap(Map map);
}
