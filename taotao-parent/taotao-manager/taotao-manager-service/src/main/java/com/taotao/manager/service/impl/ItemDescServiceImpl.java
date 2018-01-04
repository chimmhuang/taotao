package com.taotao.manager.service.impl;

import com.taotao.manager.service.ItemDescService;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.pojo.TbItemDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemDescServiceImpl implements ItemDescService{

    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    /**
     * 查询商品描述
     * @param id 商品ID
     * @return String desc
     */
    @Override
    public TbItemDesc getItemDescById(Long id) {
        TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(id);
        return itemDesc;
    }
}
