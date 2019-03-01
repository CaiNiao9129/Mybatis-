package com.cduestc.mybatis.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.cduestc.mybatis.Employee;
import com.cduestc.mybatis.dao.EmployeeMapper;



public class MybatisTestPlus {
	public SqlSessionFactory getSqlSessionFactory() throws IOException{
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		return sqlSessionFactory;
	}
	/*�������棺
	 * һ������:(���ػ���)
	 * 		������ͬһ�λỰ�ڼ��ѯ�������ݻᱻ���ڱ��ػ�����
	 * 		�Ժ������Ҫ��ȡ����ͬ�����ݣ�ֱ�Ӵӻ�����ȡ����û��Ҫ�ٴ�ȥ��ѯ���ݿ�
	 * 
	 * һ������ʧЧ���(û��ʹ�õ�ǰһ������������Ч�����ǣ�����Ҫ�������ݿⷢ����ѯ)
	 * 		1��sqlsession��ͬ��
	 * 		2��sqlsession��ͬ����ѯ������ͬ(����һ�����滹û�������Ƶ)
	 * 		3�������β�ѯ�ڼ䣬����һ����ɾ�Ĳ���(�����ɾ�Ŀ��ܶԵ�ǰ���ݲ�������������ɾ�ĺ��ٲ�ѯһ��)
	 * 		4��sqlsession��ͬ���ֶ������һ�����棡(opensession.clear)����
	 * ��������(ȫ�ֻ���) ����namespace����Ļ��棬һ��namespace��Ӧһ����������
	 * 		�������ƣ�
	 * 		1��һ���Ự����ѯһ����䣬������ݾͻᱻ���ڵ�ǰ�Ự��һ��������
	 * 		2������Ự�رգ�mybatis��һ����������ݱ��浽���������У��µĲ�ѯ��Ϣ�Ϳ��Բ��ն���������
	 * 		3��sqlSession	=== EmployeeMapper====��Employee
	 * 				DepartmentMapper==��Department
	 * 		��ͬ��namespace��ѯ�������ݻ�����Լ��Ļ�����(������mybatis�м�һ��map)
	 * 
	 * ��������ʹ��:
	 * 		1������ȫ�ֶ�������(Ĭ�Ͽ�����)<setting name="cacheEnabled" value="true"/>
	 * 		2����POJOʵ�����л��ӿ�
	 * 
	 */
	@Test
	public void testFirstLevelCache() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession();
		SqlSession sqlSession2 = sqlSessionFactory.openSession();
		try {
			EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
			EmployeeMapper mapper2 = sqlSession2.getMapper(EmployeeMapper.class);
			
			Employee emp1 = mapper.getEmployeeById(1);
			Employee emp2 = mapper2 .getEmployeeById(1);
			System.out.println(emp1);
			System.out.println(emp2);
			System.out.println(emp1==emp2);//fasle
			sqlSession.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			sqlSession.close();
		}
	}
	
	/*
	 * �����DEBUG 01-02 17:06:23,636 Cache Hit Ratio [com.cduestc.mybatis.dao.EmployeeMapper]: 0.0  (LoggingCache.java:62) 
		DEBUG 01-02 17:06:23,644 ==>  Preparing: select * from tbl_employee where id = ?;   (BaseJdbcLogger.java:145) 
		DEBUG 01-02 17:06:23,684 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:145) 
		DEBUG 01-02 17:06:23,699 <==      Total: 1  (BaseJdbcLogger.java:145) 
		Employee [id=1, lastname=TOM, gender=0, email=TOM@qq.com, dept=null]
		DEBUG 01-02 17:06:23,717 Cache Hit Ratio [com.cduestc.mybatis.dao.EmployeeMapper]: 0.5  (LoggingCache.java:62) 
		Employee [id=1, lastname=TOM, gender=0, email=TOM@qq.com, dept=null]
		false
		
		ע��������ֻ��һ��sql���
		
		������������Ե�����
			1��cacheEnabled=false�رն�������
			2��ÿһ��select��ǩ���� usercache=true��Ĭ����������
			false����ʹ�ö�������(һ����Ȼ����ʹ�� )
			3��ÿ����ɾ�ı�ǩ��flushCache=��true�� ������ɺ��һ���Ͷ������涼�ᱻ���
			4��sqlsession.clearCache()֪ʶ�����ǰsession��һ������
			5��localCacheScope:���ػ���������(һ������ȡֵSESSION)��ǰ�Ự���������ݱ����ڻỰ������
								ȡֵSTATEMENT:���Խ���һ������(һ�㲻����)
	 */
	@Test
	public void testSecondLevelCache() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession();
		SqlSession sqlSession2 = sqlSessionFactory.openSession();
		try {
			EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
			EmployeeMapper mapper2 = sqlSession2.getMapper(EmployeeMapper.class);
			
			Employee emp1 = mapper.getEmployeeById(1);
			System.out.println(emp1);
			sqlSession.close();
			
			Employee emp2 = mapper2 .getEmployeeById(1);
			System.out.println(emp2);
			sqlSession2.close();
			
			System.out.println(emp1==emp2);//fasle
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			
		}
	}
}
