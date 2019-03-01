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
	/*俩级缓存：
	 * 一级缓存:(本地缓存)
	 * 		与数据同一次会话期间查询到的数据会被放在本地缓存中
	 * 		以后如果需要获取到相同的数据，直接从缓存中取出，没必要再次去查询数据库
	 * 
	 * 一级缓存失效情况(没有使用当前一级缓存的情况，效果就是，还需要再向数据库发出查询)
	 * 		1：sqlsession不同，
	 * 		2：sqlsession相同，查询条件不同(当期一级缓存还没有这个视频)
	 * 		3：在俩次查询期间，进行一次增删改操作(这次增删改可能对当前数据操作，所以在增删改后再查询一次)
	 * 		4：sqlsession相同，手动清除过一级缓存！(opensession.clear)方法
	 * 二级缓存(全局缓存) 基于namespace级别的缓存，一个namespace对应一个二级缓存
	 * 		工作机制：
	 * 		1：一个会话，查询一条语句，这个数据就会被放在当前会话的一级缓存中
	 * 		2：如果会话关闭，mybatis将一级缓存的数据保存到二级缓存中！新的查询信息就可以参照二级缓存中
	 * 		3：sqlSession	=== EmployeeMapper====》Employee
	 * 				DepartmentMapper==》Department
	 * 		不同的namespace查询出的数据会放在自己的缓存中(缓存在mybatis中即一个map)
	 * 
	 * 二级缓存使用:
	 * 		1：开启全局二级缓存(默认开启的)<setting name="cacheEnabled" value="true"/>
	 * 		2：对POJO实现序列化接口
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
	 * 结果：DEBUG 01-02 17:06:23,636 Cache Hit Ratio [com.cduestc.mybatis.dao.EmployeeMapper]: 0.0  (LoggingCache.java:62) 
		DEBUG 01-02 17:06:23,644 ==>  Preparing: select * from tbl_employee where id = ?;   (BaseJdbcLogger.java:145) 
		DEBUG 01-02 17:06:23,684 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:145) 
		DEBUG 01-02 17:06:23,699 <==      Total: 1  (BaseJdbcLogger.java:145) 
		Employee [id=1, lastname=TOM, gender=0, email=TOM@qq.com, dept=null]
		DEBUG 01-02 17:06:23,717 Cache Hit Ratio [com.cduestc.mybatis.dao.EmployeeMapper]: 0.5  (LoggingCache.java:62) 
		Employee [id=1, lastname=TOM, gender=0, email=TOM@qq.com, dept=null]
		false
		
		注：上面结果只有一条sql语句
		
		二级缓存的属性的设置
			1：cacheEnabled=false关闭二级缓存
			2：每一个select标签都有 usercache=true的默认配置属性
			false：不使用二级缓存(一级任然可以使用 )
			3：每个增删改标签；flushCache=“true” 操作完成后就一级和二级缓存都会被清除
			4；sqlsession.clearCache()知识清除当前session的一级缓存
			5；localCacheScope:本地缓存作用域(一级缓存取值SESSION)当前会话的所有数据保存在会话缓存中
								取值STATEMENT:可以禁用一级缓存(一般不配置)
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
