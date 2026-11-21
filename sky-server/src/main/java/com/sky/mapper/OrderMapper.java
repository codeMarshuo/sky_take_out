package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param order
     */
    void insert(Orders order);
    /**
     * 根据订单号和用户id查询订单
     * @param orderNumber
     * @param userId
     */
    @Select("select * from orders where number = #{orderNumber} and user_id= #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 查询id
     * @param id
     * @return
     */
    @Select("select * from orders where id=#{id}")
    Orders geById(Long id);

    /**
     * 根据状态统计订单数量
     * @param toBeConfirmed
     * @return
     */
    @Select("select count(id) from orders where status=#{status}")
    Integer countStatus(Integer toBeConfirmed);

    /**
     * 根据状态和下单时间查询订单
     * @param status
     * @param orderTime
     */
    @Select("select * from orders where status=#{status} and order_time<#{orderTime}")
    List<Orders> getByStatusAndOrdertimeLT(Integer status, LocalDateTime orderTime);
    /**
     * 根据动态条件统计营业额
     *
     */

    Double sumByMap(
            @Param("status") Integer status,
            @Param("begin") LocalDateTime begin,
            @Param("end") LocalDateTime end
    );

    Integer countByMap(@Param("status") Integer status,
                       @Param("begin") LocalDateTime begin,
                       @Param("end") LocalDateTime end);

    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);


    Integer countByMap(Map map);

    Double sumByMap(Map map);
}
