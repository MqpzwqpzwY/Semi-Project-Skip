package dao;

import dto.RentalShopDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import service.SqlSessionFactoryService;

public class RentalShopDao {
    SqlSessionFactory sqlSessionFactory = SqlSessionFactoryService.getSqlSessionFactory();
    private final String NAMESPACE = "mybatis.mapper.RentalShopMapper";

    public RentalShopDTO getRentalShop(int id) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
            RentalShopDTO dto = sqlSession.selectOne(NAMESPACE + ".getInfo", id);
            return dto;
        }
    }

    public int insertRentalShop(RentalShopDTO dto) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            int n = sqlSession.insert(NAMESPACE + ".insertRentalShop", dto);
            sqlSession.commit();
            return n;
        }
    }

    public int deleteRentalShop(int rentalShop_id) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            int n = sqlSession.delete(NAMESPACE + ".deleteRentalShop", rentalShop_id);
            sqlSession.commit();
            return n;
        }
    }

}
