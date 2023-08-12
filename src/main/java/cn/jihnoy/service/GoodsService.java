package cn.jihnoy.service;

import cn.jihnoy.dao.GoodsDao;
import cn.jihnoy.domain.Goods;
import cn.jihnoy.domain.MiaoshaGoods;
import cn.jihnoy.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo goodsById(long goodsId) {
        return goodsDao.goodsById(goodsId);
    }

    public void reduceStock(GoodsVo goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        g.setStockCount(goods.getStockCount() - 1);
        goodsDao.reduceStock(g);
    }
}
