package dao;

import dto.SaleDTO;
import dto.UsersDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import service.SqlSessionFactoryService;

public class SaleDao {

    private SqlSessionFactory sqlSessionFactory = null;
    public SaleDao() {
        this.sqlSessionFactory = SqlSessionFactoryService.getSqlSessionFactory();
    }
    public SaleDTO select(int sale_id) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectOne("mybatis.mapper.saleMapper.select",sale_id);
        }
    }
}
