package com.charmmy.x_synapse.mapper;

import com.charmmy.x_synapse.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from x_synapse.user where username = #{username}")
    User selectByUsername(String username);
    @Insert("insert into x_synapse.user(username,password,create_time,update_time) values(#{username},#{repassword},now(),now())")
    void addUser(String username,String repassword);
}
