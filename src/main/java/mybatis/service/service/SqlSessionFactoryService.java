package service;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionFactoryService {
    private static SqlSessionFactory sqlSessionFactory;
    static {
        String resource = "config/payment-settings.xml";
        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public static SqlSessionFactory getSqlSessionFactory() {
        System.out.println("8888888888888=="+sqlSessionFactory);
        return sqlSessionFactory;
    }
}
