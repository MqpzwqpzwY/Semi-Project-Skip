package dao;

import dto.UsersDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import service.SqlSessionFactoryService;

import java.util.List;

public class UserDao {
    private SqlSessionFactory sqlSessionFactory = null;
    public UserDao() {
        this.sqlSessionFactory = SqlSessionFactoryService.getSqlSessionFactory();
    }
    public UsersDTO select(int uuid) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectOne("mybatis.mapper.UsersMapper.select",uuid);
        }
    }
}
