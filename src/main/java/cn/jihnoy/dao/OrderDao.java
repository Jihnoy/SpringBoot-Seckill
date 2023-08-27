package cn.jihnoy.dao;

import cn.jihnoy.domain.MiaoshaOrder;
import cn.jihnoy.domain.OrderInfo;
import cn.jihnoy.vo.GoodsVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderDao {

    @Select("select * from miaosha_order where user_id = #{userId} and goods_id = #{goodsId}")
    public MiaoshaOrder getMiaoshaOrderByUidGoodsId(@Param("userId")long userId, @Param("goodsId") long goodsId);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status," +
            "create_date) values(#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, " +
            "#{createDate})")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_" +
            "insert_id()")
    public long insertOrderInfo(OrderInfo orderInfo);

    @Insert("insert into miaosha_order (user_id, goods_id, order_id) values (#{userId}, #{goodsId}, #{orderId})")
    void insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    @Select("select * from order_info where id = #{orderId}")
    OrderInfo getOrderById(long orderId);
}
