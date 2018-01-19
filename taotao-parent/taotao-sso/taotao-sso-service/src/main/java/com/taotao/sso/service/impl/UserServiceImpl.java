package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.jedis.JedisClient;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * 用户处理Service
 */
@Service
public class UserServiceImpl implements UserService{


    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${USER_SESSION}")
    private String USER_SESSION;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;//session的过期时间


    /**
     * 检查数据是否可用
     * @param param 要检验的数据
     * @param type 校验的数据类型 // 1.username   2.phone   3.email
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult checkData(String param, int type) {
        //1. 从tb_user表中查询数据
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();

        //查询条件动态生成
        switch (type){
            case 1 :
                criteria.andUsernameEqualTo(param);
                break;
            case 2 :
                criteria.andPhoneEqualTo(param);
                break;
            case 3 :
                criteria.andEmailEqualTo(param);
                break;
            default:
                return TaotaoResult.build(400,"非法的参数");
        }

        //2. 执行查询
        List<TbUser> list = userMapper.selectByExample(example);

        //3. 判断查询结果，如果查询到数据返回false
        if (list.size() == 0 || list == null){
            //如果没有返回true
            return TaotaoResult.ok(true);
        }
        return TaotaoResult.ok(false);
    }


    /**
     * 创建用户
     * @param user TbUser对象
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult createUser(TbUser user) {
        //1.使用TbUser接受提交的请求

        if (StringUtils.isBlank(user.getUsername())){
            return TaotaoResult.build(400,"用户名不能为空");
        }
        if (StringUtils.isBlank(user.getPassword())){
            return TaotaoResult.build(400,"密码不能为空");
        }

        //校验数据是否可用
        TaotaoResult  result = checkData(user.getUsername(),1);
        if (!(boolean)result.getData()){
            return TaotaoResult.build(400,"此用户名已被使用");
        }

        //校验电话是否可以
        if (StringUtils.isNotEmpty(user.getPhone())){
            result = checkData(user.getPhone(),2);
            if (!(boolean)result.getData()){
                return TaotaoResult.build(400,"此手机号已被使用");
            }
        }

        //校验邮箱是否可以
        if (StringUtils.isNotEmpty(user.getEmail())){
            result = checkData(user.getEmail(),3);
            if (!(boolean)result.getData()){
                return TaotaoResult.build(400,"此邮箱已被使用");
            }
        }


        //2.补全其他属性
        user.setCreated(new Date());
        user.setUpdated(new Date());


        //3.密码要进行MD5加密
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);

        //4.把用户信息写入数据库
        userMapper.insert(user);

        //5.返回TaotaoResult
        return TaotaoResult.ok();
    }


    /**
     * 用户登陆
     * @param username 用户名
     * @param password 密码
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult login(String username, String password) {
        //1.判断用户名密码是否正确
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);

        List<TbUser> list = userMapper.selectByExample(example);
        if (list == null || list.size() == 0){
            //没有查到用户
            //返回登陆失败
            return TaotaoResult.build(400,"用户名或密码不正确");
        }
        //查询到了用户，进行密码校验
        //密码要进行MD5加密，然后在进行校验
        TbUser user = list.get(0);
        if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
            return TaotaoResult.build(400,"用户名或密码不正确");
        }

        //2.校验成功，生成token，使用UUID
        String token = UUID.randomUUID().toString();
        //把用户信息保存到redis，key就是token，value就是用户信息，注入jedisClient
        //能走到这一步，说明账号和密码都通过了，所以为了安全，把密码给清楚掉
        user.setPassword(null);
        jedisClient.set(USER_SESSION + ":" + token, JsonUtils.objectToJson(user));
        //设置key的过期时间
        jedisClient.expire(USER_SESSION+ ":" +token,SESSION_EXPIRE);
        //返回登陆成功，其中要把token返回
        return TaotaoResult.ok(token);
    }


    /**
     * 通过token查询用户信息
     * @param token sessionId
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult getUserByToken(String token) {
        //1.根据token查询redis
        String json = jedisClient.get(USER_SESSION + ":" + token);
        if (StringUtils.isBlank(json)){
            //没有查到用户信息
            return TaotaoResult.build(400,"用户登陆已经过期，请重新登陆");
        }
        //查到了用户信息
        //重置这个Session的过期时间
        jedisClient.expire(USER_SESSION + ":" + token,SESSION_EXPIRE);
        //把这个json转换成user对象
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        return TaotaoResult.ok(user);
    }


    /**
     * 安全退出
     * @param token SessionID
     * @return TaotaoResult
     */
    @Override
    public TaotaoResult logout(String token) {
        jedisClient.expire(USER_SESSION + ":" + token,0);
        return TaotaoResult.ok();
    }
}
