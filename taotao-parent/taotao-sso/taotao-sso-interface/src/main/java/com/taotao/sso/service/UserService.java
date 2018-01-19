package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {

    TaotaoResult checkData(String param,int type);
    TaotaoResult createUser(TbUser user);
    TaotaoResult login(String username ,String password);
    TaotaoResult getUserByToken(String token);
    TaotaoResult logout(String token);
}
