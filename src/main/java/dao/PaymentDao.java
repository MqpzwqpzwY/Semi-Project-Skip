package dao;

import dto.PaymentDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import service.SqlSessionFactoryService;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentDao {
    private final SqlSessionFactory sqlSessionFactory;

    public PaymentDao() {
        this.sqlSessionFactory = SqlSessionFactoryService.getSqlSessionFactory();
    }

    public int insert(PaymentDTO dto) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) { // 자동 커밋
            System.out.println(dto);
             sqlSession.insert("mybatis.mapper.PaymentMapper.insert", dto);
            return dto.getPayment_id();
        }
    }

    public boolean updatePaymentStatusToRefunded(String impId) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) { // 자동 커밋
            return sqlSession.update("mybatis.mapper.PaymentMapper.updatePaymentStatusToRefunded", impId) > 0;
        } catch (Exception e) {
            return false;
        }
    }


    public PaymentDTO getPaymentByImpUid(String impUid) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectOne("mybatis.mapper.PaymentMapper.getPaymentByImpUid", impUid);
        }
    }

    public PaymentDTO getPaymentById(int paymentId) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            return sqlSession.selectOne("mybatis.mapper.PaymentMapper.getPaymentById", paymentId);
        }
    }
    public List<PaymentDTO> getPaymentsByUuid(int uuid) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectList("mybatis.mapper.PaymentMapper.getPaymentsByUuid", uuid);
        }
    }
}

