package com.charmmy.x_synapse.mapper;

import com.charmmy.x_synapse.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("select * from x_synapse.user where username = #{username}")
    User selectByUsername(String username);
    @Insert("insert into x_synapse.user(username,password,create_time,update_time) values(#{username},#{Password},now(),now())")
    void addUser(String username,String Password);
    @Update("update x_synapse.user set username = #{username},bio = #{bio},update_time = now() where id = #{id}")
    void update(User user);
    @Update("update x_synapse.user set avatar = #{avatar} where username = #{username}")
    void updateAvatar(String avatar,String username);
    @Update("update x_synapse.user set password = #{password} where username = #{username}")
    void updatePassword(String username, String password);
}
