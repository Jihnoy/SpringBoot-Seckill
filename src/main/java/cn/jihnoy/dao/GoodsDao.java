package cn.jihnoy.dao;

import cn.jihnoy.domain.Goods;
import cn.jihnoy.domain.MiaoshaGoods;
import cn.jihnoy.domain.MiaoshaUser;
import cn.jihnoy.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDao {
    @Select("select g.*, mg.stock_count, mg.start_date, mg.end_date, miaosha_price from miaosha_goods mg left join " +
            "goods g on mg.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();

    @Select("select g.*, mg.stock_count, mg.start_date, mg.end_date, miaosha_price from miaosha_goods mg left join " +
            "goods g on mg.goods_id = g.id where mg.goods_id = #{goodsId}")
    public GoodsVo goodsById(@Param("goodsId") long goodsId);

    @Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count>0")
    public int reduceStock(MiaoshaGoods g);
}
