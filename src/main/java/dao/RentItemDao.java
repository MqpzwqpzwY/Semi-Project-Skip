package dao;

import dto.RentItemDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import service.SqlSessionFactoryService;

import java.util.List;
import java.util.Map;

public class RentItemDao {
    SqlSessionFactory sqlSessionFactory = SqlSessionFactoryService.getSqlSessionFactory();
    private final String NAMESPACE = "mybatis.mapper.RentItemMapper";

    public RentItemDTO getRentItem(Map<String, Object> map) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectOne(NAMESPACE + ".selectRentItem", map );
        }
    }

    public List<RentItemDTO> getRentItemList(Map<String, Object> map) {
        System.out.println("selectRentItemList");
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<RentItemDTO> s = sqlSession.selectList(NAMESPACE + ".getList", map);
            System.out.println(s);
            return s;
        }
    }

    public int insertRentItem(RentItemDTO dto) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            int n = sqlSession.insert(NAMESPACE + ".insertRentItem", dto);
            sqlSession.commit();
            return n;
        }
    }

    public int deleteRentItem(int item_id) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            int n = sqlSession.delete(NAMESPACE + ".deleteRentItem", item_id);
            sqlSession.commit();
            return n;
        }
    }

}