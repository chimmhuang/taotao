package com.taotao.manager.service.impl;

import com.taotao.manager.service.ItemParamService;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.pojo.TbItemParamItemExample.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemParamServiceImpl implements ItemParamService {

    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    /**
     * 通过ID查找商品规格
     * @param id
     * @return
     */
    @Override
    public TbItemParamItem getItemParamById(Long id) {
        TbItemParamItemExample example = new TbItemParamItemExample();
        Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(id);
        List<TbItemParamItem> items = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
        return items.get(0);
    }





}
