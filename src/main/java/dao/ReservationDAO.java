package dao;

import dto.RentReservationItemDTO;
import dto.RentalReservationDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import service.SqlSessionFactoryService;

import java.util.HashMap;
import java.util.Map;

public class ReservationDAO {
    private final SqlSessionFactory sqlSessionFactory;
    
    public ReservationDAO() {
        this.sqlSessionFactory = SqlSessionFactoryService.getSqlSessionFactory();
    }
    
    public int insertRentalReservation(RentalReservationDTO dto) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            session.insert("mybatis.mapper.ReservationMapper.insertRentalReservation", dto);
            return dto.getRent_reserv_id();
        }
    }
    
    public int insertRentReservationItem(RentReservationItemDTO dto) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            session.insert("mybatis.mapper.ReservationMapper.insertRentReservationItem", dto);
            return dto.getRent_item_id();
        }
    }

    public RentReservationItemDTO getRentReservationItemById(int rentItemId) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            return session.selectOne("mybatis.mapper.ReservationMapper.getRentReservationItemById", rentItemId);
        }
    }


} 