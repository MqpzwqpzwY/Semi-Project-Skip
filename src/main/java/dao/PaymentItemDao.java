package dao;

import dto.PaymentItemDTO;
import dto.UsersDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import service.SqlSessionFactoryService;

import java.util.List;

public class PaymentItemDao {
    private SqlSessionFactory sqlSessionFactory = null;
    public PaymentItemDao() {
        this.sqlSessionFactory = SqlSessionFactoryService.getSqlSessionFactory();
    }

    public List<PaymentItemDTO> selectItem(int category_id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectList("mybatis.mapper.itemMapper.getAllRentItems", category_id);
        }
    }
}
